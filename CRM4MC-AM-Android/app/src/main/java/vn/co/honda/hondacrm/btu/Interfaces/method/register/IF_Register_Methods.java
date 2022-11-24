package vn.co.honda.hondacrm.btu.Interfaces.method.register;


public interface IF_Register_Methods {
    boolean startRegister();
    boolean registerPinCode(String pinCode);
    boolean registerUserName(String userName);
    boolean replaceUser(String userName);
    boolean unRegister();
}
