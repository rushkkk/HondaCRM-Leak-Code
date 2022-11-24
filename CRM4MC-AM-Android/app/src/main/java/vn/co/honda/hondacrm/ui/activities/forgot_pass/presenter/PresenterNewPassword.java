package vn.co.honda.hondacrm.ui.activities.forgot_pass.presenter;

import vn.co.honda.hondacrm.ui.activities.forgot_pass.model.ModelNewPassword;

public class PresenterNewPassword {
    ModelNewPassword modelNewPassword;

    public void validatePassword(){
        modelNewPassword.handleValidatePassword();
    }
}
