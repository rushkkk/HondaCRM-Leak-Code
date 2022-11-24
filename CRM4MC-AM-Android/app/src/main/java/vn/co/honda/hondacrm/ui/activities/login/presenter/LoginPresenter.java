package vn.co.honda.hondacrm.ui.activities.login.presenter;

import android.widget.EditText;

public interface LoginPresenter {

    public void validateCredentials(String username, String password,int delay);
    public void validateEmpty(EditText username, EditText password);

}