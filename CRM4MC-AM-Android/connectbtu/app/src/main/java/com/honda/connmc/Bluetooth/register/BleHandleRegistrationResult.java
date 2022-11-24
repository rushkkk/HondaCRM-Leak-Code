package com.honda.connmc.Bluetooth.register;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils;
import com.honda.connmc.Constants.message.ParamTag;
import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.register.VehicleRegistrationType;
import com.honda.connmc.Interfaces.listener.register.IF_VehicleRegisterListener;
import com.honda.connmc.Model.message.common.VehicleParam;
import com.honda.connmc.Model.message.register.response.VehicleRegistrationResponseMessage;
import com.honda.connmc.Model.message.register.response.VehicleSecurityConfigMessage;
import com.honda.connmc.NativeLib.NativeLibController;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.Utils.PermissionUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_BTU_NAME_REGISTER_SUCCESS;
import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_REGISTER_SUCCESS;
import static com.honda.connmc.Constants.message.ParamTag.SECURITY_CONFIG_CONTAINER;
import static com.honda.connmc.Constants.message.ParamTag.USER_NAME;

public class BleHandleRegistrationResult {
    private BleRegisterController mBleRegisterController;
    private IF_VehicleRegisterListener mRegistrationEventListener;
    private static final int TID_VALUE_SIZE = 10;
    private static final String HEXA_DECIMAL_FORMAT = "^[0-9a-fA-F]+$";
    private static final String DECIMAL_FORMAT = "^[0-9]+$";
    private static final int CONVERT_FROM_HEX = 16;
    private boolean isRequestPinFail;
    private boolean isTooManyUser;
    private final Object LOCK_OBJECT = new Object();

    public BleHandleRegistrationResult(BleRegisterController bleRegisterController) {
        this.mBleRegisterController = bleRegisterController;
    }

    public void resetData() {
        isTooManyUser = false;
        isRequestPinFail = false;
    }

    public void setRegistrationEventListener(IF_VehicleRegisterListener registrationEventListener) {
        this.mRegistrationEventListener = registrationEventListener;
    }

    public void extractRegistrationResultMessage(VehicleRegistrationResponseMessage message) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            int result = message.getRegistrationResult();
            int cause = message.getRegistrationResultCause();

            if (result == ParamValue.Security.RegistrationResult.REDIRECT) {
                processReceiveResultRedirect(message, cause);
            } else if (result == ParamValue.Security.RegistrationResult.REJECT) {
                if (cause == ParamValue.Security.ResultCause.INVALID_PIN_CODE) {
                    LogUtils.d("INVALID_PIN_CODE");
                    mBleRegisterController.stopTimer();
                    mRegistrationEventListener.onVerifyPinCodeFail();
                } else {
                    LogUtils.w("received unknown registration response from VEHICLE_REGISTRATION_RESULT_CAUSE_INVALID_PIN_CODE");
                    mBleRegisterController.registrationFailureProcess();
                }
            } else if (result == ParamValue.Security.RegistrationResult.FAILURE) {
                LogUtils.w("registration failed with result : " + message.getRegistrationResultCause());
                mBleRegisterController.registrationFailureProcess();
            } else if (result == ParamValue.Security.RegistrationResult.SUCCESS) {
                if (cause == ParamValue.Security.ResultCause.UNSPECIFIED) {
                    LogUtils.d("result - SUCCESS | cause - UNSPECIFIED ==> ACTIVE");
                    if (mRegistrationEventListener != null) {
                        mBleRegisterController.stopTimer();
                        BluetoothPrefUtils.getInstance().setBool(KEY_REGISTER_SUCCESS, true);
                        BluetoothManager.getInstance().setDeviceCurrentStatus(BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE);
                        BluetoothManager.getInstance().getDataMessageManager().setEnableEncrypt(true);
                        BluetoothPrefUtils.getInstance().setString(KEY_BTU_NAME_REGISTER_SUCCESS, BluetoothManager.getInstance().getBleConnectController().getBluetoothDeviceConnect().getName());
                        BluetoothManager.getInstance().getBleConnectController().notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE);
                    }
                    NativeLibController.getInstance().deleteRegistrationKey();
                } else {
                    LogUtils.w("unknown registration result message");
                    mBleRegisterController.registrationFailureProcess();
                }
            } else {
                LogUtils.w("unknown registration result value");
                mBleRegisterController.registrationFailureProcess();
            }
            LogUtils.startEndMethodLog(false);
        }
    }

    private void processReceiveResultRedirect(VehicleRegistrationResponseMessage message, int cause) {
        LogUtils.startEndMethodLog(true);
        final int MAX_DEVICE = 6;
        List<String> userNameList = message.getUserNameList();
        if (userNameList != null && userNameList.size() > 0) {
            if (userNameList.size() >= MAX_DEVICE) {
                isTooManyUser = true;
            }
        }
        switch (cause) {
            case ParamValue.Security.ResultCause.PUBLIC_KEY_REQUIRED:
                LogUtils.d("PUBLIC_KEY_REQUIRED");
                mBleRegisterController.requestPublicKeyGeneration();
                break;
            case ParamValue.Security.ResultCause.INVALID_PIN_CODE:
                LogUtils.d("INVALID_PIN_CODE");
                mBleRegisterController.stopTimer();
                mRegistrationEventListener.onVerifyPinCodeFail();
                break;
            case ParamValue.Security.ResultCause.USER_NAME_REQUIRED:
                LogUtils.d("USER_NAME_REQUIRED ==> send user name to BTU");
                isRequestPinFail = false;
                mBleRegisterController.stopTimer();
                mRegistrationEventListener.onRequestUserName();
                break;
            case ParamValue.Security.ResultCause.USER_NAME_DUPLICATE:
                LogUtils.d("USER_NAME_DUPLICATE ==> edit user name");
                mBleRegisterController.stopTimer();
                if (BluetoothPrefUtils.getInstance() == null) {
                    mBleRegisterController.registrationFailureProcess();
                    break;
                }
                mRegistrationEventListener.onRequestEditUserName(BluetoothPrefUtils.getInstance().getUserName());
                break;
            case ParamValue.Security.ResultCause.TOO_MANY_USERS:
                LogUtils.d("TOO_MANY_USERS ==> delete user to replace with new user");
                List<String> registeredNameList = message.getUserNameList();
                if (registeredNameList != null && !registeredNameList.isEmpty()) {
                    mBleRegisterController.stopTimer();
                    String userName = registeredNameList.get(registeredNameList.size() - 1);
                    mRegistrationEventListener.onResultTooManyUser(userName);
                } else {
                    LogUtils.w("received broken data");
                    mBleRegisterController.registrationFailureProcess();
                }
                break;
            case ParamValue.Security.ResultCause.BTU_REGISTERED:
                LogUtils.d("BTU REGISTERED with user name ==> delete user to replace with new user");

                if (userNameList != null && userNameList.size() > 0) {
                    LogUtils.d("Size List: " + userNameList.size());
                    String username = userNameList.get(0);
                    boolean isDuplicateUserName = false;
                    for (String user : userNameList) {
                        LogUtils.w("List devices REGISTERED: " + user);
                        if (TextUtils.equals(user, mBleRegisterController.getUserName())) {
                            isDuplicateUserName = true;
                            username = user;
                            break;
                        }
                    }
//                        LogUtils.d( "BTU_REGISTERD with user name:" + username);
//                    if (mRegistrationEventListener != null) {
//                        mRegistrationEventListener.onRequestReplaceUserName(username);
//                    }
                    if (isDuplicateUserName) {
                        LogUtils.d("USER_NAME_DUPLICATE => replace user");
                        mBleRegisterController.replaceUser(username);
//                        processReceiveResultRedirect(message, ParamValue.Security.ResultCause.USER_NAME_DUPLICATE);
                    } else {
                        if (isTooManyUser) {
                            LogUtils.d("TOO_MANY_USERS => show popup many users");
                            processReceiveResultRedirect(message, ParamValue.Security.ResultCause.TOO_MANY_USERS);
                        } else {
                            LogUtils.d("Device registered => replace user");
                            mBleRegisterController.replaceUser(username);
                        }
                    }
                } else {
                    LogUtils.e("Username list is null or empty");
                    mBleRegisterController.registrationFailureProcess();
                }
                break;
            default:
                LogUtils.e("Received unknown registration response from VEHICLE_REGISTRATION_RESULT_CAUSE_BTU_REGISTERED");
                mBleRegisterController.registrationFailureProcess();
                break;
        }
        LogUtils.startEndMethodLog(false);
    }

    public int saveUserInfo(String userName) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            int res = ParamValue.Security.RegistrationResult.OTHER_ERROR;

            byte[] convertName;
            try {
                convertName = userName.getBytes("Shift-JIS");
            } catch (UnsupportedEncodingException e) {
                LogUtils.e(e.toString());
                return res;
            }
            if (convertName == null || (convertName.length <= 0) || (convertName.length > ParamTag.getLength(USER_NAME))) {
                LogUtils.e("convert name error!");
                return res;
            }
            NativeLibController instance = NativeLibController.getInstance();
            if (instance == null) {
                LogUtils.e("NativeLibController is null");
                return res;
            }

            ByteBuffer tidBuf = getDeviceId();
            if (tidBuf == null) {
                LogUtils.e("getDeviceId is null");
                return res;
            }
            res = instance.setUserInfo(convertName, tidBuf.array());
            if (res == NativeLibController.VEHICLE_CRYPTO_RESULT_SUCCESS) {
                if (BluetoothPrefUtils.getInstance() != null) {
                    BluetoothPrefUtils.getInstance().saveUserName(userName);
                }
            }
            LogUtils.startEndMethodLog(false);
            return res;
        }
    }

    private ByteBuffer getDeviceId() {
        LogUtils.startEndMethodLog(true);
        ByteBuffer tidBuf = ByteBuffer.allocate(TID_VALUE_SIZE);
        TelephonyManager telManager = (TelephonyManager) BluetoothManager.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (PermissionUtils.hasPermissions(BluetoothManager.getInstance().getApplicationContext(), Manifest.permission.READ_PHONE_STATE)) {
            String tid = null;
            try {
                tid = telManager.getDeviceId();
            } catch (SecurityException e) {
                LogUtils.e(e.toString());
            }

            if (tid != null) {
                Long tidVal;
                try {
                    if (tid.matches(DECIMAL_FORMAT) == true) {
                        tidVal = Long.valueOf(tid);
                    } else if (tid.matches(HEXA_DECIMAL_FORMAT) == true) {
                        tidVal = Long.valueOf(tid, CONVERT_FROM_HEX);
                    } else {
                        return null;
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
                tidBuf.putLong(tidVal);
            } else {
                tid = Build.SERIAL;
                try {
                    byte[] serial = tid.getBytes("Shift-JIS");
                    int byteCount = (serial.length < TID_VALUE_SIZE) ? serial.length : TID_VALUE_SIZE;
                    tidBuf.put(serial, 0, byteCount);
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }
        }
        LogUtils.startEndMethodLog(false);
        return tidBuf;
    }


    public void onRegistrationResponseNotified(List<VehicleParam> paramList) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);

            if ((paramList == null) || (paramList.size() <= 0)) {
                LogUtils.e("Parameter Error!");
                mBleRegisterController.registrationFailureProcess();
                NativeLibController.getInstance().deleteRegistrationKey();
                return;
            }
            VehicleRegistrationResponseMessage message =
                    new VehicleRegistrationResponseMessage(paramList);

            switch (message.getRegistrationType()) {
                case VehicleRegistrationType.VEHICLE_REGISTRATION_TYPE_REGISTER:
                    LogUtils.d("VEHICLE_REGISTRATION_TYPE_REGISTER");
                    extractRegistrationResultMessage(message);
                    break;
                case VehicleRegistrationType.VEHICLE_REGISTRATION_TYPE_DEREGISTER:
                    LogUtils.d("VEHICLE_REGISTRATION_TYPE_DEREGISTER");
                    extractRegistrationDeregisterResultMessage(message);
                    break;
                case VehicleRegistrationType.VEHICLE_REGISTRATION_TYPE_RE_REGISTER:
                    LogUtils.d("VEHICLE_REGISTRATION_TYPE_RE_REGISTER");
//                extractReRegistrationResultMessage(message);
                    break;
                default:
                    LogUtils.e("Unknown Error!");
                    mBleRegisterController.registrationFailureProcess();
                    NativeLibController.getInstance().deleteRegistrationKey();
                    break;
            }
        }
    }

    //    private void extractReRegistrationResultMessage(VehicleRegistrationResponseMessage message) {
//        LogUtils.startEndMethodLog(true);
//        int responseResult = message.getRegistrationResult();
//        int responseCause = message.getRegistrationResultCause();
//        List<String> userNameList = message.getUserNameList();
//        String userName;
//        if (userNameList != null && userNameList.size() > 0) {
//            userName = userNameList.get(0);
//        } else {
//            LogUtils.w("username is invalid");
//            return;
//        }
//        LogUtils.d("Result: " + responseResult + "; cause: " + responseCause + "; username: " + userName);
//        if (responseResult == VehicleRegistrationResult.VEHICLE_REGISTRATION_RESULT_REDIRECT
//                && responseCause == VehicleRegistrationResultCause.VEHICLE_REGISTRATION_RESULT_CAUSE_USER_NAME_REQUIRED) {
//
//        }
//
//    }
//
    private void extractRegistrationDeregisterResultMessage(
            VehicleRegistrationResponseMessage message) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            int vehicleResult = message.getRegistrationResult();
            int cause = message.getRegistrationResultCause();
            List<String> userNameList = message.getUserNameList();

            boolean isSuccess = false;
            if ((vehicleResult == ParamValue.Security.RegistrationResult.REDIRECT)
                    && (cause == ParamValue.Security.ResultCause.USER_NAME_UNKNOWN)) {
                LogUtils.d("ResultCause.USER_NAME_UNKNOWN");
                if (userNameList != null) {
                    LogUtils.d("SIZE username list: " + userNameList.size());
                    isSuccess = true;
                }
            } else if ((vehicleResult == ParamValue.Security.RegistrationResult.SUCCESS)
                    && (cause == ParamValue.Security.ResultCause.UNSPECIFIED)) {
                LogUtils.d("ResultCause.UNSPECIFIED");
                isSuccess = true;
                // check whether username list includes username of this application
                if (userNameList == null) {
                    isSuccess = false;
                } else {
                    LogUtils.d("SIZE username list: " + userNameList.size());
                    if (BluetoothPrefUtils.getInstance() != null) {
                        String currentUser = BluetoothPrefUtils.getInstance().getUserName();
                        for (String receivedName : userNameList) {
                            LogUtils.d("UserName is registered: " + receivedName);
                            if (receivedName != null && TextUtils.equals(receivedName, currentUser)) {
                                isSuccess = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!isSuccess) {
                if (mRegistrationEventListener != null) {
                    mRegistrationEventListener.onUnRegisterResult();
                }
            }
            LogUtils.startEndMethodLog(false);
        }
    }

//    private void extractUserNameChangeResultMessage(VehicleRegistrationResponseMessage message) {
//        int vehicleResult = message.getRegistrationResult();
//        int cause = message.getRegistrationResultCause();
//        List<String> userNameList = message.getUserNameList();
//        String targetName = mRegistrationManager.getUserName();
//        if ((vehicleResult == VehicleRegistrationResult.VEHICLE_REGISTRATION_RESULT_SUCCESS)
//                && (cause == VehicleRegistrationResultCause.VEHICLE_REGISTRATION_RESULT_CAUSE_UNSPECIFIED)) {
//            if (userNameList != null) {
//                for (String registeredName : userNameList) {
//                    if (registeredName.equals(targetName) == true) {
//                        mRegistrationManager.storeTempUserName(null);
//                    }
//                }
//            }
//        } else if ((vehicleResult == VehicleRegistrationResult.VEHICLE_REGISTRATION_RESULT_REDIRECT)
//                && (cause == VehicleRegistrationResultCause.VEHICLE_REGISTRATION_RESULT_CAUSE_USER_NAME_DUPLICATE)) {
//            resetUserName();
//            if (registrationEventListener != null) {
//                registrationEventListener.onRequestUserNameEdit(targetName, true);
//            }
//            return;
//        }

//    }

    public void extractRegistrationCryptionKey(VehicleSecurityConfigMessage message) {
        synchronized (LOCK_OBJECT) {
            // get AES_SHARED_KEY and then pin code input
            LogUtils.startEndMethodLog(true);
            final byte[] encryptedData = message.getSecurityConfigContainer();
            if (encryptedData == null) {
                LogUtils.e("failed to get securityConfigContainer buffer");
                mBleRegisterController.registrationFailureProcess();
                return;
            }
            new AsyncTask<Void, Void, Integer>() {

                @Override
                protected Integer doInBackground(Void... voids) {
                    return NativeLibController.getInstance().extractSharedKey(encryptedData);
                }

                @Override
                protected void onPostExecute(Integer integer) {
                    super.onPostExecute(integer);
                    if (integer.intValue() != NativeLibController.VEHICLE_CRYPTO_RESULT_SUCCESS) {
                        LogUtils.e("failed to get AES KEY");
                        mBleRegisterController.registrationFailureProcess();
                    } else {
                        if (mRegistrationEventListener != null) {
                            mBleRegisterController.stopTimer();
                            BluetoothManager.getInstance().getBleConnectController().stopTimer();
                            if (!isRequestPinFail) {
                                isRequestPinFail = true;
                                mRegistrationEventListener.onRequestPinCode();
                            } else {
                                mRegistrationEventListener.onVerifyPinCodeFail();
                            }
                        }
                    }
                }
            }.execute();
            LogUtils.startEndMethodLog(false);
        }
    }

    public void exchangeAuthenticationData(VehicleSecurityConfigMessage message) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            // get vehicle authentication data (CID, kd, ke)
            final byte[] encryptedVehicleData = message.getSecurityConfigContainer();
//            if (encryptedVehicleData == null) {
//                LogUtils.e("failed to get securityConfigContainer buffer");
//                mBleRegisterController.registrationFailureProcess();
//                NativeLibController.getInstance().deleteRegistrationKey();
//                return;
//            }
            ByteBuffer buf =
                    ByteBuffer
                            .allocate(ParamTag.getLength(SECURITY_CONFIG_CONTAINER));
            byte[] output = buf.array();
            new AsyncTask<Void, Void, Integer>() {

                @Override
                protected Integer doInBackground(Void... voids) {
                    return NativeLibController.getInstance().createAuthKey(encryptedVehicleData, output);
                }

                @Override
                protected void onPostExecute(Integer integer) {
                    super.onPostExecute(integer);
                    if (integer.intValue() != NativeLibController.VEHICLE_CRYPTO_RESULT_SUCCESS) {
                        LogUtils.e("failed to extract authkey");
                        mBleRegisterController.registrationFailureProcess();
                    } else {
                        int type = ParamValue.Security.SecurityConfigType.ID_EXCHANGE;
                        VehicleSecurityConfigMessage sendMsg =
                                new VehicleSecurityConfigMessage(type, output);
                        List<VehicleParam> sendParam = sendMsg.getVehicleParameters();
                        if (sendParam == null) {
                            LogUtils.e("failed to get security config response message");
                            mBleRegisterController.registrationFailureProcess();
                            return;
                        }
                        mBleRegisterController.sendSecurityConfigResponse(sendParam);
                    }
                }
            }.execute();
            LogUtils.startEndMethodLog(false);
        }
    }
}
