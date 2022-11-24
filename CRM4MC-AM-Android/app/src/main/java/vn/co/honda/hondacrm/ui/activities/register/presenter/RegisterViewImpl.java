package vn.co.honda.hondacrm.ui.activities.register.presenter;

public interface RegisterViewImpl {

    void showErrName(String err);

    void showErrPhoneNumber(String err);

    void showErrPassWord(String err);

    void showErrConfirmPassWorld(String err);

    void showErrYearOfBirth(String err);

    void hideErrName();

    void hideErrPhone();

    void hideErrPass();

    void hideErrPassConfirm();

    void hideErrBirth();

    void showErrCheckBox(String err);

    void validateSuccess(String token);

    void showProgressDialog();

    void hideProgressDialog();

    void setEnableButton(boolean b);

    void showExistPhoneNumber();


}
