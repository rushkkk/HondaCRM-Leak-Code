package vn.co.honda.hondacrm.ui.activities.login.presenter;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.login.model.User;
import vn.co.honda.hondacrm.ui.activities.login.ui.LoginActivity;
import vn.co.honda.hondacrm.ui.activities.profile.ProfileActivity;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;

public class LoginPresenterImpl implements OnLoginFinishedListener, LoginPresenter, LoginFacebook {
    private LoginView loginView;
    private LoginInteractor loginInteractor;
    private AccessTokenTracker accessTokenTracker;
    private CallbackManager callbackManager;
    private String uesr = "";
    private String pass ="";

    public LoginPresenterImpl(LoginActivity loginView) {
        this.loginView = loginView;
        loginInteractor = new LoginInteractorImpl();
    }

    @Override
    public void setShowProgressbar() {
        loginView.showProgressBar();
    }

    @Override
    public void setHideProgressbar() {
        loginView.hideProgressBar();
    }

    @Override
    public void onAccountNotActive() {
        loginView.navigationToActiveAccount();
    }

    @Override
    public void validateCredentials(String username, String password, int delay) {
        String phone = "";
        if (username.isEmpty() || password.isEmpty()) {
            if (username.isEmpty()) {
                if (!password.isEmpty()) {
                    loginView.setUsernameError(Constants.EMPTY);
                }

                DialogUtils.showDialogConfirmLogin(((Context) loginView), R.layout.dialog_notification_erro_network, 0.9f, 0.2f, ((Context) loginView).getString(R.string.errPhoneEmpty));
            } else {
                onHideUserNameError();
            }
            if (password.isEmpty()) {
                if (!username.isEmpty()) {
                    loginView.setUsernameError(Constants.EMPTY);
                }

                DialogUtils.showDialogConfirmLogin(((Context) loginView), R.layout.dialog_notification_erro_network, 0.9f, 0.2f, ((Context) loginView).getString(R.string.errPassEmpty));
            } else {
                onHidePasswordError();
            }
        } else if (username.length() != 10 && username.length() != 12){

            DialogUtils.showDialogConfirmLogin(((Context) loginView), R.layout.dialog_notification_erro_network, 0.9f, 0.2f, ((Context) loginView).getString(R.string.errPhoneIncorrect));
        }else if(password.length() < 6) {
            DialogUtils.showDialogConfirmLogin(((Context) loginView), R.layout.dialog_notification_erro_network, 0.9f, 0.2f, ((Context) loginView).getString(R.string.errPassleast6));
        }  else if ( username.length() == 10 || username.length() == 12) {

            if (username.length() == 12) {
                if (!username.substring(0, 3).equals("+84")) {

                    DialogUtils.showDialogConfirmLogin(((Context) loginView), R.layout.dialog_notification_erro_network, 0.9f, 0.2f, ((Context) loginView).getString(R.string.errPhoneIncorrect));
                } else {
                    setShowProgressbar();
                    loginInteractor.login(new User("0"+username.substring(3), password), this, (Context) loginView, delay);
                }
            } else {
                if (username.charAt(0) != '0') {

                    DialogUtils.showDialogConfirmLogin(((Context) loginView), R.layout.dialog_notification_erro_network, 0.9f, 0.2f, ((Context) loginView).getString(R.string.errPhoneIncorrect));
                } else {
                    setShowProgressbar();
                    phone = username;
                    loginInteractor.login(new User(phone, password), this, (Context) loginView, delay);
                }
            }
        }
    }

    @Override
    public void validateEmpty(final EditText username, final EditText password) {

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                loginView.setImageUserNameDeleteFocus(b);
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                uesr = charSequence.toString();
                if (charSequence.length() > 0) {
                    loginView.setVisibilityUsername(true);
                } else {
                    loginView.setVisibilityUsername(false);
                }
                if (uesr.isEmpty()|| pass.isEmpty()){
                    loginView.setDisableButton();
                }else {
                    loginView.setActiveButton();
                }
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                loginView.setImagePassWordDeleteFocus(b);
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pass = charSequence.toString();
                if (charSequence.length() > 0) {
                    loginView.setVisibilityPassword(true);

                } else {
                    loginView.setVisibilityPassword(false);

                }
                if (uesr.isEmpty()|| pass.isEmpty()){
                    loginView.setDisableButton();
                }else {
                    loginView.setActiveButton();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (uesr.isEmpty()|| pass.isEmpty()){
            loginView.setDisableButton();
        }else {
            loginView.setActiveButton();
        }

    }


    private String printPhone(String phoneNum) {
        StringBuilder sb = new StringBuilder(15);
        StringBuilder temp = new StringBuilder(phoneNum.toString());

//        while (temp.length() < 10)
//            temp.insert(0, "0");

        char[] chars = temp.toString().toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (i == 0) {
                sb.append("+84");
            } else if (i == 3) {
                sb.append(" ");
            } else if (i == 6) {
                sb.append(" ");
            }
            sb.append(chars[i]);
        }

        return sb.toString();
    }

    @Override
    public void onUsernameError(String s) {
        loginView.setUsernameError(s);
    }

    @Override
    public void onPasswordError(String s) {
        loginView.setPasswordError(s);
    }

    @Override
    public void onHideUserNameError() {
        loginView.setHideErrorUser();
    }

    @Override
    public void onHidePasswordError() {
        loginView.setHideErrorPass();
    }

    @Override
    public void onSuccessLogin() {
        loginView.navigateToHome();
    }


    @Override
    public void accessToken() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            userLogin(accessToken);
        }
        //This starts the access token tracking
        accessTokenTracker.startTracking();
    }

    @Override
    public void callbackManager(final LoginButton loginButton, ImageButton btnButtonLogin) {
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();
        // Registering CallbackManager with the LoginButton
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Retrieving access token using the LoginResult
                AccessToken accessToken = loginResult.getAccessToken();
                userLogin(accessToken);
                onSuccessLogin();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
        // Defining the AccessTokenTracker
        accessTokenTracker = new AccessTokenTracker() {
            // This method is invoked everytime access token changes
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken != null) {
                    // AccessToken is not null implies user is logged in and hence we sen the GraphRequest
                    userLogin(currentAccessToken);
                }

            }
        };

    }

    @Override
    public void userLogin(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }
}
