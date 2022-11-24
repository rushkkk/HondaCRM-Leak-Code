package vn.co.honda.hondacrm.btu.Bluetooth.register;


import java.nio.ByteBuffer;

import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.Constants.message.ParamTag;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleMessage;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;
import vn.co.honda.hondacrm.btu.Model.message.register.response.VehicleMessageAuthenticationResponse;
import vn.co.honda.hondacrm.btu.Model.message.register.security.VehicleParameterTransactionIdentity;
import vn.co.honda.hondacrm.btu.Model.message.register.security.VehicleParameterTypeResultSuccess;
import vn.co.honda.hondacrm.btu.Model.message.register.security.VehicleParameterTypeSecurityAuthResponseValue;
import vn.co.honda.hondacrm.btu.Model.message.register.security.VehicleParameterTypeSecurityConfigIdentifier;
import vn.co.honda.hondacrm.btu.Model.message.register.security.VehicleParameterTypeSecurityRandomValue;
import vn.co.honda.hondacrm.btu.Model.message.register.security.VehicleParameterTypeSecurityVehicleAuthValue;
import vn.co.honda.hondacrm.btu.NativeLib.NativeLibController;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;

import static vn.co.honda.hondacrm.btu.Constants.message.ParamTag.AUTH_RESPONSE_VALUE;


public class BleHandleAuthenticationResult {
    private BleRegisterController mBleRegisterController;
    private final Object LOCK_OBJECT = new Object();
    public BleHandleAuthenticationResult(BleRegisterController bleRegisterController) {
        this.mBleRegisterController = bleRegisterController;
    }

    public void onAuthenticationRequestResult(VehicleMessage data) {
        synchronized (LOCK_OBJECT) {
            VehicleParameterTypeSecurityConfigIdentifier securityConfId = null;
            VehicleParameterTypeSecurityRandomValue randomValue = null;
            VehicleParameterTypeSecurityVehicleAuthValue vehicleAuthValue = null;

            for (VehicleParam param : data.mParamArray) {
                if (param == null) {
                    LogUtils.w("null Parameter");
                    continue;
                }
                switch (param.mTag) {
                    case ParamTag.SECURITY_CONFIG_IDENTIFIER:
                        securityConfId = new VehicleParameterTypeSecurityConfigIdentifier(param.mValue[0]);
                        break;

                    case ParamTag.RANDOM_VALUE:
                        if (param.mValue == null) {
                            LogUtils.e("could not get random value");
                            return;
                        }
                        randomValue = new VehicleParameterTypeSecurityRandomValue(param.mValue);
                        break;

                    case ParamTag.VEHICLE_AUTH_VALUE:
                        if (param.mValue == null) {
                            LogUtils.e("could not get vehicle auth value");
                            return;
                        }
                        vehicleAuthValue = new VehicleParameterTypeSecurityVehicleAuthValue(param.mValue);
                        break;

                    case ParamTag.TRANSACTION_IDENTITY:
                        LogUtils.d("receive data TAG TRANSACTION_IDENTITY : " + param.mValue[0]);
                        break;
                    default:
                        LogUtils.e("UNKNOWN VEHICLE_MESSAGE_PARAM_ID");
                        break;
                }
            }

            if (securityConfId == null || randomValue == null || vehicleAuthValue == null) {
                LogUtils.e("Check AuthenticationRequestParameter ERROR");
                performAuthentication(null, null, null);
                return;
            }
            tryAuthenticationAsync(securityConfId.mValue, randomValue.mValue, vehicleAuthValue.mValue);
        }
    }

    private void tryAuthenticationAsync(final byte[] securityConfigId, final byte[] randomVal, final byte[] authVal) {
        LogUtils.startEndMethodLog(true);
        Thread authenticationProcessThread = new Thread(() -> performAuthentication(securityConfigId, randomVal, authVal));
        authenticationProcessThread.start();
        LogUtils.startEndMethodLog(false);
    }

    public void performAuthentication(byte[] securityConfigId, byte[] randomVal, byte[] authVal) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);

            if (securityConfigId == null || randomVal == null || authVal == null) {
                onVehicleAuthenticationResult(authVal, false);
                return;
            }

            ByteBuffer bufConfig = ByteBuffer.wrap(securityConfigId);
            byte config = bufConfig.get();

            switch (config) {
                case 0:
                    onVehicleAuthenticationResult(authVal, true);
                    break;
                case 1:
                    byte[] devAuth = new byte[ParamTag.getLength(AUTH_RESPONSE_VALUE)];
                    NativeLibController cryptoService = NativeLibController.getInstance();
                    if (cryptoService == null) {
                        onVehicleAuthenticationResult(authVal, false);
                        break;
                    }
                    boolean ret = true;
                    if (cryptoService.tryAuthentication(randomVal, authVal, devAuth) != 0) {
                        LogUtils.e("authentication failure");
                        ret = false;
                    }
                    onVehicleAuthenticationResult(devAuth, ret);
                    break;
                default:
                    break;
            }

            LogUtils.startEndMethodLog(false);
        }
    }

    public void onVehicleAuthenticationResult(byte[] authResultVal, boolean result) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            if ((!result) || (authResultVal == null)) {
                LogUtils.e("failed authentication");
                mBleRegisterController.registrationFailureProcess();
                return;
            }

            final int REASON_NONE = 0x00;

            VehicleMessageAuthenticationResponse message;
            message = new VehicleMessageAuthenticationResponse();
            message.mParamArray.add(new VehicleParameterTransactionIdentity(BluetoothManager.getInstance().getTransactionIdentity()));
            message.mParamArray.add(new VehicleParameterTypeResultSuccess(REASON_NONE));
            message.mParamArray.add(new VehicleParameterTypeSecurityAuthResponseValue(authResultVal));
            BluetoothManager.getInstance().sendMessage(message);
//        BluetoothManager.getInstance().getDataMessageManager().setEnableEncrypt(true);
//            BluetoothPrefUtils.getInstance().setString(KEY_BTU_NAME_REGISTER_SUCCESS, BluetoothManager.getInstance().getBleConnectController().getBluetoothDeviceConnect().getName());
//        BluetoothManager.getInstance().getBleConnectController().notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE);
            LogUtils.startEndMethodLog(true);
        }
    }
}
