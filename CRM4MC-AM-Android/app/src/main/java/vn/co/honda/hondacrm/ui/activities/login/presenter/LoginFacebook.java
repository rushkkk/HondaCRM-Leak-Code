package vn.co.honda.hondacrm.ui.activities.login.presenter;

import android.widget.ImageButton;

import com.facebook.AccessToken;
import com.facebook.login.widget.LoginButton;

public interface LoginFacebook {
    public void accessToken();
    public void callbackManager(LoginButton loginButton, ImageButton btnButtonLogin);
    public void userLogin(AccessToken accessToken);

}
