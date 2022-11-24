package vn.co.honda.hondacrm.ui.activities.login.presenter;

public interface LoginView {
    public void setTextUsername(String s);

    public void setUsernameError(String s);

    public void showProgressBar();

    public void hideProgressBar();

    public void setPasswordError(String s);

    public void setHideErrorUser();

    public void setHideErrorPass();

    public void setDisableButton();

    public void setActiveButton();

    public void navigateToHome();

    public void navigationToActiveAccount();

    public void navigateToForgotPassword();

    public void navigateToSignin();

    public void setVisibilityUsername(boolean b);

    public void setVisibilityPassword(boolean b );

    public void setImageUserNameDeleteFocus(boolean b);

    public void setImagePassWordDeleteFocus(boolean b);


}
