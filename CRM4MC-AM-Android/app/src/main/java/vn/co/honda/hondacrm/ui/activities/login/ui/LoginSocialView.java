package vn.co.honda.hondacrm.ui.activities.login.ui;

public interface LoginSocialView {
    void showErrName(String err);

    void showErrPhoneNumber(String err);

    void showErrBirth(String err);

    void hideErrName();

    void hideErrPhone();

    void hideErrBirth();

    void showErrPassWord(String err);

    void showErrConfirmPassWorld(String err);


    void hideErrPass();

    void hideErrPassConfirm();


    void showErrYearOfBirth(String err);


    void showErrCheckBox(String err);

    void validateSuccess();

    void showProgressDialog();

    void hideProgressDialog();

    void setEnableButton(boolean b);
}
