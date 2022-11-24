package com.honda.connmc.Bluetooth.register;

import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.timer.IF_TimeOutListener;
import com.honda.connmc.Bluetooth.timer.TimeManager;
import com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Constants.message.ParamTag;
import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.register.VehicleRegistrationStatus;
import com.honda.connmc.Constants.register.VehicleRegistrationType;
import com.honda.connmc.Interfaces.listener.register.IF_VehicleRegisterListener;
import com.honda.connmc.Interfaces.method.register.IF_Register_Methods;
import com.honda.connmc.Model.message.VehicleMessageStatusInquiry;
import com.honda.connmc.Model.message.common.VehicleMessage;
import com.honda.connmc.Model.message.common.VehicleParam;
import com.honda.connmc.Model.message.register.request.VehicleMessageRegistrationRequest;
import com.honda.connmc.Model.message.register.response.RegistrationStatusIndicationMessage;
import com.honda.connmc.Model.message.register.response.VehicleMessageSecurityConfigResponse;
import com.honda.connmc.Model.message.register.response.VehicleSecurityConfigMessage;
import com.honda.connmc.NativeLib.NativeLibController;
import com.honda.connmc.Utils.LogUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_BTU_NAME_REGISTER_SUCCESS;
import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_REGISTER_SUCCESS;
import static com.honda.connmc.Constants.message.ParamTag.PUBLIC_KEY;
import static com.honda.connmc.Constants.message.ParamTag.SECURITY_CONFIG_CONTAINER;
import static com.honda.connmc.Constants.register.VehicleRegistrationType.VEHICLE_REGISTRATION_TYPE_REGISTER;

public class BleRegisterController implements IF_Register_Methods, IF_TimeOutListener {
    private final TimeManager mTimeManager;
    private static final long REGISTRATION_RESPONSE_TIMEOUT = 30000;
    private boolean isStartRegister;
    private String mUserName;
    private IF_VehicleRegisterListener mRegistrationEventListener;
    private BleHandleRegistrationResult mBleHandleRegistrationResult;
    private BleHandleAuthenticationResult mBleHandleAuthenticationResult;
    private final Object LOCK_OBJECT = new Object();

    public BleRegisterController() {
        mTimeManager = new TimeManager();
        mBleHandleAuthenticationResult = new BleHandleAuthenticationResult(this);
        mBleHandleRegistrationResult = new BleHandleRegistrationResult(this);
    }

    public void resetData() {
        synchronized (LOCK_OBJECT) {
            isStartRegister = false;
            mBleHandleRegistrationResult.resetData();
            stopTimer();
        }
    }

    public synchronized void setRegistrationEventListener(IF_VehicleRegisterListener registrationEventListener) {
        if (registrationEventListener == null) {
            LogUtils.e("Listener register : null!");
            return;
        }
        LogUtils.d("Listener register : " + registrationEventListener.getClass().getCanonicalName());
        mRegistrationEventListener = registrationEventListener;
        mBleHandleRegistrationResult.setRegistrationEventListener(mRegistrationEventListener);
    }

    @Override
    public boolean startRegister() {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            BluetoothDevice bluetoothDevice = BluetoothManager.getInstance().getBleConnectController().getBluetoothDeviceConnect();
            if (bluetoothDevice == null) {
                LogUtils.e("bluetoothDevice is null!");
                return false;
            }
            if (isStartRegister) {
                LogUtils.w("already start register");
                return false;
            }
            if (!NativeLibController.getInstance().isExistVehicleInfo(bluetoothDevice.getName())) {
                isStartRegister = true;
                int type = VEHICLE_REGISTRATION_TYPE_REGISTER;
                VehicleMessageRegistrationRequest sendMessage = new VehicleMessageRegistrationRequest(
                        type, null, null);
                BluetoothManager.getInstance().sendMessage(sendMessage);
            } else {
                LogUtils.w("device already registered");
                return false;
            }
            mTimeManager.startTimer(BleRegisterController.this, REGISTRATION_RESPONSE_TIMEOUT);
            LogUtils.startEndMethodLog(false);
        }
        return true;
    }

    public synchronized void stopTimer() {
        synchronized (LOCK_OBJECT) {
            isStartRegister = false;
            mTimeManager.stopTimeOut();
        }
    }

    @Override
    public boolean registerPinCode(String pinCode) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            byte[] input = checkFormat(pinCode);
            if ((input == null)
                    || (input.length > ParamTag.getLength(SECURITY_CONFIG_CONTAINER))) {
                registrationFailureProcess();
                return false;
            }
            sendPinCodeAsync(input);
            mTimeManager.startTimer(this, REGISTRATION_RESPONSE_TIMEOUT);
            LogUtils.startEndMethodLog(false);
            return false;
        }
    }

    private void sendPinCodeAsync(final byte[] pinData) {
        Thread t = new Thread(() -> {
            synchronized (LOCK_OBJECT) {
                ByteBuffer inputBuf =
                        ByteBuffer
                                .allocate(ParamTag.getLength(SECURITY_CONFIG_CONTAINER));
                inputBuf.put(pinData);
                byte[] input = inputBuf.array();
                ByteBuffer outputBuf =
                        ByteBuffer
                                .allocate(ParamTag.getLength(SECURITY_CONFIG_CONTAINER));
                byte[] output = outputBuf.array();
                NativeLibController.getInstance().getPinCodeSendData(input, output);
                int type = ParamValue.Security.SecurityConfigType.VERIFY_PIN;
                VehicleSecurityConfigMessage sendMsg =
                        new VehicleSecurityConfigMessage(type, output);
                List<VehicleParam> sendParam = sendMsg.getVehicleParameters();
                if (sendParam == null) {
                    registrationFailureProcess();
                    return;
                }
                sendSecurityConfigResponse(sendParam);
            }
        });
        t.start();
    }

    public synchronized void registrationFailureProcess() {
        LogUtils.e("registrationFailureProcess");
        stopTimer();
        BluetoothManager.getInstance().disconnectDevice();
        NativeLibController.getInstance().deleteRegistrationKey();
        if (BluetoothUtils.checkBluetooth() && mRegistrationEventListener != null) {
            mRegistrationEventListener
                    .onRegisterResultFail(ParamValue.Security.RegistrationResult.OTHER_ERROR);
            if (BluetoothManager.getInstance().getBleConnectController() != null)
                BluetoothManager.getInstance().getBleConnectController().notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
        }
    }

    private byte[] checkFormat(String target) {
        if (target == null) {
            return null;
        }
        if (target.length() > ParamTag.getLength(SECURITY_CONFIG_CONTAINER)) {
            return null;
        }
        byte[] targetBuf;
        try {
            targetBuf = target.getBytes("Shift-JIS");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return targetBuf;
    }

    @Override
    public boolean registerUserName(String userName) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            boolean requestUser = requestUserName(userName);
            LogUtils.startEndMethodLog(false);
            return requestUser;
        }
    }

    @Override
    public boolean replaceUser(String replaceUser) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            LogUtils.d("User Name will delete: " + replaceUser);
            int type = VehicleRegistrationType.VEHICLE_REGISTRATION_TYPE_DEREGISTER;
            if (TextUtils.isEmpty(replaceUser)) {
                return false;
            }
            List<String> userNameList = Arrays.asList(replaceUser);
            VehicleMessageRegistrationRequest sendMessage = new VehicleMessageRegistrationRequest(
                    type, userNameList, null);
            List<VehicleParam> paramList = sendMessage
                    .getParams();
            if (paramList == null) {
                LogUtils.e("failed to get VehicleRegistrationRequestMessage");
                return false;
            }
            BluetoothManager.getInstance().sendMessage(sendMessage);
            LogUtils.startEndMethodLog(false);
            return true;
        }
    }

    @Override
    public boolean unRegister() {
        new Thread(() -> {
            synchronized (LOCK_OBJECT) {
                String vehicleName = BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS);
                if (!TextUtils.isEmpty(vehicleName)) {
                    NativeLibController.getInstance().deleteVehicleInfo(vehicleName);
                    NativeLibController.getInstance().deleteRegistrationKey();
                    NativeLibController.getInstance().stopAuthenticationDataCryption();
                    NativeLibController.getInstance().stopRegistrationCryption();
                    BluetoothPrefUtils.getInstance().removeKeyPref(KEY_BTU_NAME_REGISTER_SUCCESS);
                    BluetoothPrefUtils.getInstance().removeKeyPref(KEY_REGISTER_SUCCESS);
                }
            }
        }).start();
        return true;
    }

    @Override
    public void onTimeout() {
        registrationFailureProcess();
    }

    public void onReceiveDataRegistrationResponse(VehicleMessage data) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            LogUtils.d("Receive Register Data(VehicleMessage)");
            if (data == null) {
                LogUtils.e("Received NULL MESSAGE");
                registrationFailureProcess();
                return;
            }
            mBleHandleRegistrationResult.onRegistrationResponseNotified(data.getParams());

            LogUtils.startEndMethodLog(false);
        }
    }

    public void onSecurityConfigRequestNotified(VehicleMessage data) {
        synchronized (LOCK_OBJECT) {
            if (data == null || data.getParams() == null || data.mParamArray.size() <= 0) {
                registrationFailureProcess();
                return;
            }
            VehicleSecurityConfigMessage message = new VehicleSecurityConfigMessage(data.mParamArray);
            if (message.getSecurityConfigType() == ParamValue.Security.SecurityConfigType.VERIFY_PIN) {
                mBleHandleRegistrationResult.extractRegistrationCryptionKey(message);
            } else if (message.getSecurityConfigType() == ParamValue.Security.SecurityConfigType.ID_EXCHANGE) {
                mBleHandleRegistrationResult.exchangeAuthenticationData(message);
            } else {
                registrationFailureProcess();
            }
        }
    }


    public void onRegistrationStatusIndicationNotified(VehicleMessage data) {
        LogUtils.startEndMethodLog(true);
        if (data == null || data.mParamArray == null || data.mParamArray.size() <= 0) {
            registrationFailureProcess();
            return;
        }
        RegistrationStatusIndicationMessage registrationStatusIndicationMethods =
                new RegistrationStatusIndicationMessage(data.mParamArray);
        int registrationStatusIndicationValue =
                registrationStatusIndicationMethods.getRegistrationStatusValue();
        switch (registrationStatusIndicationValue) {
            case VehicleRegistrationStatus.REGISTRATION_STATUS_VALUE_ACTIVE:
                LogUtils.d("REGISTRATION_STATUS_VALUE_ACTIVE");
                stopTimer();
                BluetoothManager.getInstance().setDeviceCurrentStatus(BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE);
                BluetoothManager.getInstance().getDataMessageManager().setEnableEncrypt(true);
                BluetoothPrefUtils.getInstance().setString(KEY_BTU_NAME_REGISTER_SUCCESS, BluetoothManager.getInstance().getBleConnectController().getBluetoothDeviceConnect().getName());
                BluetoothManager.getInstance().getBleConnectController().notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE);
                break;
            case VehicleRegistrationStatus.REGISTRATION_STATUS_VALUE_INACTIVE:
                LogUtils.d("REGISTRATION_STATUS_VALUE_INACTIVE");
                stopTimer();
                BluetoothManager.EnumDeviceConnectStatus status = BluetoothManager.getInstance().getDeviceCurrentStatus();
                if (status == BluetoothManager.EnumDeviceConnectStatus.BTU_INACTIVE_SECOND_TIME) {
                    // send REGISTRATION_REQUEST
                    BluetoothManager.getInstance().getDataMessageManager().setEnableEncrypt(true);
                    String userName = BluetoothPrefUtils.getInstance().getUserName();
                    if (TextUtils.isEmpty(userName)) {
                        registrationFailureProcess();
                        return;
                    }
                    List<String> userNameList = new ArrayList<>();
                    userNameList.add(userName);
                    VehicleMessageRegistrationRequest registerMsg =
                            new VehicleMessageRegistrationRequest(
                                    VehicleRegistrationType.VEHICLE_REGISTRATION_TYPE_RE_REGISTER,
                                    userNameList, null);
                    BluetoothManager.getInstance().sendMessage(registerMsg);
                } else {
                    // Update BTU_INACTIVE status and waiting BTU disconnect
                    BluetoothManager.getInstance().disconnectDevice();
                    BluetoothManager.getInstance().setDeviceCurrentStatus(BluetoothManager.EnumDeviceConnectStatus.BTU_INACTIVE);
                    BluetoothManager.getInstance().getBleConnectController().notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.BTU_INACTIVE);
                }

                break;
            case VehicleRegistrationStatus.REGISTRATION_STATUS_VALUE_NOT_REGISTER:
                LogUtils.d("Receive REGISTRATION_STATUS_VALUE_NOT_REGISTER status from BTU");
                stopTimer();
                BluetoothManager.getInstance().setDeviceCurrentStatus(BluetoothManager.EnumDeviceConnectStatus.BTU_UNREGISTERED);
                // process disconnect
                boolean disconnectResult = BluetoothManager.getInstance().disconnectDevice();
                if (!disconnectResult) {
                    LogUtils.e("RegistrationStatusIndication Not register, could not disconnect!!!");
                }
                // need show error
                BluetoothManager.getInstance().getBleConnectController().notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.BTU_UNREGISTERED);
                break;
            default:
                LogUtils.e("Unknown message!");
                registrationFailureProcess();
                break;
        }
    }

    public void requestPublicKeyGeneration() {
        new AsyncTask<Void, Void, Integer>() {
            private byte[] kyeBuf;

            @Override
            protected Integer doInBackground(Void... voids) {
                ByteBuffer data =
                        ByteBuffer.allocate(ParamTag.getLength(PUBLIC_KEY) - 1);
                kyeBuf = data.array();
                return NativeLibController.getInstance().generatePublicKey(kyeBuf);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                if (integer.intValue() != NativeLibController.VEHICLE_CRYPTO_RESULT_SUCCESS) {
                    registrationFailureProcess();
                    return;
                }
                int type = VehicleRegistrationType.VEHICLE_REGISTRATION_TYPE_REGISTER;
                VehicleMessageRegistrationRequest sendMessage =
                        new VehicleMessageRegistrationRequest(type, null, kyeBuf);
                List<VehicleParam> paramList = sendMessage.getParams();
                if (paramList == null) {
                    registrationFailureProcess();
                    return;
                }
                // Send public key to BTU
                BluetoothManager.getInstance().sendMessage(sendMessage);
            }
        }.execute();
    }

    public void sendSecurityConfigResponse(List<VehicleParam> data) {
        synchronized (LOCK_OBJECT) {
            VehicleMessageSecurityConfigResponse requestMessage = new VehicleMessageSecurityConfigResponse();
            for (VehicleParam param : data) {
                requestMessage.mParamArray.add(param);
            }
            BluetoothManager.getInstance().sendMessage(requestMessage);
        }
    }

    public void onReceiveDataAuthenticationRequest(VehicleMessage vehicleMessage) {
        mBleHandleAuthenticationResult.onAuthenticationRequestResult(vehicleMessage);
    }

    public String getUserName() {
        return mUserName;
    }

    private boolean requestUserName(String userName) {
        synchronized (LOCK_OBJECT) {
            if (TextUtils.isEmpty(userName)) {
                LogUtils.e("username is null or empty!");
                return false;
            }
            this.mUserName = userName;
            if (BluetoothPrefUtils.getInstance() == null) {
                return false;
            }
            String currentUserName = BluetoothPrefUtils.getInstance().getUserName();
            if (TextUtils.isEmpty(currentUserName) || (!TextUtils.equals(currentUserName, userName))) {
                if (mBleHandleRegistrationResult.saveUserInfo(userName) != NativeLibController.VEHICLE_CRYPTO_RESULT_SUCCESS) {
                    registrationFailureProcess();
                    return false;
                }
            }
            int type = VehicleRegistrationType.VEHICLE_REGISTRATION_TYPE_REGISTER;
            List<String> userNameList = new ArrayList<String>();
            userNameList.add(userName);
            VehicleMessageRegistrationRequest sendMessage =
                    new VehicleMessageRegistrationRequest(type, userNameList, null);
            List<VehicleParam> paramList = sendMessage.getParams();
            if (paramList == null) {
                registrationFailureProcess();
                return false;
            }
            BluetoothManager.getInstance().sendMessage(sendMessage);
            return true;
        }
    }

    public void inquire(boolean isForRegister) {
        LogUtils.d("inquire()");
        if (isForRegister) {
            isStartRegister = true;
        }
        VehicleMessageStatusInquiry sendData;
        sendData	= new VehicleMessageStatusInquiry();
        BluetoothManager.getInstance().sendMessage(sendData);
    }

    public boolean isRegisterInProgress() {
        return isStartRegister;
    }
}
