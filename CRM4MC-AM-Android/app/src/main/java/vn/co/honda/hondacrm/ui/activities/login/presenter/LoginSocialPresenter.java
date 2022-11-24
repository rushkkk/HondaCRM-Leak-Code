package vn.co.honda.hondacrm.ui.activities.login.presenter;

public interface LoginSocialPresenter {


    void setShowDialog();

    void setHideDialog();

    void validate(String name, String phoneNumber, String passWorld, String confirmPassWorld,
                  String yearBirth, boolean isAccessPolicy, int gender);

}
