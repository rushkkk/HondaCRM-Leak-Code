package com.honda.connmc.Interfaces.listener.register;

public interface IF_VehicleRegisterListener {
    void onRequestPinCode();
    void onVerifyPinCodeFail();
    void onRequestUserName();
    void onResultTooManyUser(String userNameDelete);
    void onRequestEditUserName(String userName);
    void onRegisterResultFail(int result);
    void onUnRegisterResult();
}
