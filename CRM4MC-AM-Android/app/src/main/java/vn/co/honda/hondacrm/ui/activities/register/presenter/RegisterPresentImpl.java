package vn.co.honda.hondacrm.ui.activities.register.presenter;

import android.widget.CheckBox;
import android.widget.EditText;

public interface RegisterPresentImpl {
    void setShowDialog();
    void setHideDialog();

    void validate(String name, String phoneNumber, String passWorld, String confirmPassWorld,
                  String yearBirth, boolean isAccessPolicy, int gender);
    void validateEmpty(EditText name, EditText phone, EditText passworld, EditText confirmPass, CheckBox checkBox);
}
