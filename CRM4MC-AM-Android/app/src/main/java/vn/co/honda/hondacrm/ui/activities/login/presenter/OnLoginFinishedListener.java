package vn.co.honda.hondacrm.ui.activities.login.presenter;

public interface OnLoginFinishedListener {

    public void onUsernameError(String s);

    public void onPasswordError(String s);

    public void onHideUserNameError();

    public void onHidePasswordError();

    public void onSuccessLogin();

    public void setShowProgressbar();

    public void setHideProgressbar();

    public void onAccountNotActive();

}