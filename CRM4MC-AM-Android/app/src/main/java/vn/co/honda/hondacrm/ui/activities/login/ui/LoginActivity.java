package vn.co.honda.hondacrm.ui.activities.login.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.zing.zalo.zalosdk.oauth.LoginVia;
import com.zing.zalo.zalosdk.oauth.OAuthCompleteListener;
import com.zing.zalo.zalosdk.oauth.OauthResponse;
import com.zing.zalo.zalosdk.oauth.ValidateOAuthCodeCallback;
import com.zing.zalo.zalosdk.oauth.ZaloOpenAPICallback;
import com.zing.zalo.zalosdk.oauth.ZaloSDK;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.base.ResponseOTPObject;
import vn.co.honda.hondacrm.net.model.user.UserProfile;
import vn.co.honda.hondacrm.net.model.user.UserProfileResponse;
import vn.co.honda.hondacrm.net.model.user.UserResponse;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.forgot_pass.ForgotPasswordActivity;
import vn.co.honda.hondacrm.ui.activities.home.MainActivity;
import vn.co.honda.hondacrm.ui.activities.login.model.User;
import vn.co.honda.hondacrm.ui.activities.login.presenter.LoginFacebook;
import vn.co.honda.hondacrm.ui.activities.login.presenter.LoginPresenter;
import vn.co.honda.hondacrm.ui.activities.login.presenter.LoginPresenterImpl;
import vn.co.honda.hondacrm.ui.activities.login.presenter.LoginView;
import vn.co.honda.hondacrm.ui.activities.login.presenter.SavaTokenSharePreference;
import vn.co.honda.hondacrm.ui.activities.login.presenter.SaveSharedPreference;
import vn.co.honda.hondacrm.ui.activities.register.ui.RegisterActivity;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.FirebaseUtil;
import vn.co.honda.hondacrm.utils.PrefUtils;

/**
 * @author CuongNV31
 */
public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener, ValidateOAuthCodeCallback {

    @BindView(R.id.btnLogin)
    TextView btnLogin;

    @BindView(R.id.btnSignup)
    TextView btnSignup;

    @BindView(R.id.txtForgotpassword)
    TextView txtForgotpassword;

    @BindView(R.id.btn_delete_username)
    ImageButton btnDeleteUser;
    @BindView(R.id.btn_delete_password)
    ImageButton btnDeletePass;

    private EditText edtUserName;
    private EditText edtPassword;
    private LoginPresenter presenter;
    private ImageButton btnLoginButtonFb, btnLoginButtonGG;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private LoginFacebook loginFacebook;
    private ImageButton btnLoginZalo;
    private LoginListener listener;
    int RC_SIGN_IN = 0;
    private SignInButton signInButtonGG;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView txtErrorUser, txtxErrorPass;
    private User mUserProfile;
    private ApiService apiService;
    private String typeNetwork = "facebook";
    private String mTypeFullAccessToken;
    private String mOauthCode;
    private ImageView imgLogoHonda;
    private View viewSocial;

    @Override
    public void onStart() {
        super.onStart();
        // checkOAuthCode();
//        ZaloSDK.Instance.unauthenticate();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });

        SaveSharedPreference.setInforUser(getApplicationContext(), new User());
        SaveSharedPreference.setUser(getApplicationContext(), new User("", ""));

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        imgLogoHonda = findViewById(R.id.imgLogoHonda);
        viewSocial = findViewById(R.id.loginSocial);
        setUpSocial();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        loginFacebook = new LoginPresenterImpl(this);
        presenter = new LoginPresenterImpl(this);
        btnLoginButtonFb = findViewById(R.id.btnLoginFb);
        btnLoginButtonGG = findViewById(R.id.btnLoginGG);
        btnLoginZalo = findViewById(R.id.btnLoginZalo);
        btnLoginZalo.setOnClickListener(this);
        loginButton = findViewById(R.id.login_button);
        edtUserName = findViewById(R.id.edtUsername);
        signInButtonGG = findViewById(R.id.sign_in_button);
        edtPassword = findViewById(R.id.edtPassword);
        edtPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        txtErrorUser = findViewById(R.id.txtErrorUser);
        txtxErrorPass = findViewById(R.id.txtErrorPass);

        btnLoginButtonGG.setOnClickListener(this);
        btnLoginButtonFb.setOnClickListener(this);
        loginButton.setReadPermissions("email", "public_profile", "user_friends");
        callbackManager = CallbackManager.Factory.create();
        presenter.validateEmpty(edtUserName, edtPassword);
        setDisableButton();

        // Registering CallbackManager with the LoginButton
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                showProgressBar();
                AccessToken accessToken = loginResult.getAccessToken();
                if (accessToken != null) {
                    SavaTokenSharePreference.setTokenFacebook(getApplicationContext(), accessToken.getToken());
                    useLoginInformation(accessToken);
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("ttt", error.toString());
            }
        });
    }

    private void setUpSocial() {
        listener = new LoginListener(this);
        apiService = ApiClient.getClient(getApplicationContext()).create(ApiService.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn() {
        showProgressBar();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.btnLogin)
    public void onClickLoginButton() {

        presenter.validateCredentials(edtUserName.getText().toString(), edtPassword.getText().toString(), 1000);
    }

    @OnClick(R.id.btn_delete_username)
    public void onClickDeleteUser() {
        edtUserName.setText("");
    }

    @OnClick(R.id.btn_delete_password)
    public void onClickDeletepass() {
        edtPassword.setText("");
    }

    @OnClick(R.id.btnSignup)
    public void onClickSignupButton() {
        startActivity(this, RegisterActivity.class, false);
    }

    @OnClick(R.id.txtForgotpassword)
    public void onClickForgotpasswordButton() {
        startActivity(this, ForgotPasswordActivity.class, false);
    }

    /**
     * Login with Facebook
     *
     * @param accessToken
     */
    public void useLoginInformation(AccessToken accessToken) {
        /**
         Creating the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/
        GraphRequest request = GraphRequest.newMeRequest(accessToken, (object, response) -> {

            String name = "";
            try {
                name = object.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String email = "";
            try {
                email = object.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String image = "";
            try {
                image = object.getJSONObject("picture").getJSONObject("data").getString("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mUserProfile = new User(name, image, "", "", email);

            if (accessToken.getToken() != null) {
                requestProfileSocialNetwork(accessToken.getToken());
                SaveSharedPreference.setInforUser(getApplicationContext(), mUserProfile);
                SaveSharedPreference.setUser(getApplicationContext(), new User("", ""));
            } else {
                hideProgressBar();
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }

    private void requestProfile() {
        apiService.getUserProfile(
                mTypeFullAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<UserProfileResponse>() {
                    @Override
                    public void onSuccess(UserProfileResponse response) {
                        UserProfile userProfile = response.getUserProfile();
                        if (userProfile.getPhone() != null && userProfile.getPhoneVerifiedAt() != null) {
                            if (TextUtils.equals(PrefUtils.getFullTypeAccessToken(LoginActivity.this), mTypeFullAccessToken)) {
                                SaveSharedPreference.setCountLogin(LoginActivity.this, Constants.FIRST);
                            } else {
                                PrefUtils.storeAccessTokenKey(LoginActivity.this, mTypeFullAccessToken);
                            }
                            SaveSharedPreference.setUser(getApplicationContext(), new User(mUserProfile.getNumberPhone(), ""));

                            User user = new User();
                            user.setFullName(userProfile.getName());
                            user.setBirtDay(userProfile.getDateOfBirth());
                            user.setGender(userProfile.getGender());
                            user.setEmail(userProfile.getEmail());
                            user.setAvatar(mUserProfile.getAvatar());
                            SaveSharedPreference.setInforUser(LoginActivity.this, user);
                            FirebaseUtil.logEventLogin(FirebaseAnalytics.getInstance(LoginActivity.this.getApplicationContext()),
                                    typeNetwork);
                            if (SaveSharedPreference.getCountLogin(getApplicationContext()) == Constants.ZERO) {
                                startActivity(LoginActivity.this, WelcomeActivity.class, true);
                            } else {
                                startActivity(LoginActivity.this, MainActivity.class, true);
                            }

                        } else if (userProfile.getPhone() != null && userProfile.getPhoneVerifiedAt() == null) {
                            DialogUtils.showDialogLogout(LoginActivity.this, R.layout.diaglog_confirm_delete, 0.9f, 0.3f, new DialogUtils.DialogListener() {
                                @Override
                                public void okButtonClick(Dialog dialog) {
                                    dialog.dismiss();
                                    apiNumber(userProfile.getPhone());
                                }

                                @Override
                                public void cancelButtonClick() {

                                }
                            }, getString(R.string.lb_account_is_not_active));
                            hideProgressBar();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, LoginSocicalActivity.class);
                            intent.putExtra(Constants.KEY_TYPE_SOCIAL, typeNetwork);
                            intent.putExtra(Constants.KEY_SOCIAL, mUserProfile);
                            intent.putExtra(Constants.KEY_ACCESS_TOKEN, mTypeFullAccessToken);
                            startActivity(intent);
                            hideProgressBar();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressBar();
                    }
                });
    }

    public void apiNumber(String phone) {
        showProgressBar();
        ApiService apiService = ApiClient.getClient(LoginActivity.this.getApplicationContext()).create(ApiService.class);
        apiService.forgotPassword(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ResponseOTPObject>() {
                    @Override
                    public void onSuccess(ResponseOTPObject response) {
                        hideProgressBar();
//                        String msg = response.getMessage() == null ? Constants.EMPTY : response.getMessage();
//                        String OTP_SEND_MESSAGE = response.getData();
//                        if (TextUtils.equals(OTP_SEND_MESSAGE, Constants.ACCOUNT_IS_NOT_EXISTS)) {
//                            txtErrorPhoneForgot.setVisibility(View.VISIBLE);
//                            txtErrorPhoneForgot.setText(msg);
//                        } else if (TextUtils.equals(OTP_SEND_MESSAGE, Constants.OTP_SEND_WITHIN_MAX_TIMES)) {
//                            txtErrorPhoneForgot.setVisibility(View.INVISIBLE);
//                            startActivity(ForgotPasswordActivity.this, VerifyOtpErrorActivity.class, true);
//                        } else if (TextUtils.equals(OTP_SEND_MESSAGE, Constants.OTP_SEND_WITHIN_5_MINS)) {
//                            DialogUtils.showDialogConfirmLogin(ForgotPasswordActivity.this,
//                                    R.layout.dialog_notification_erro_network, 0.9f, 0.2f, response.getMessage());
//                        } else {
                        Intent intent = new Intent(LoginActivity.this, VerifyOPTAccountNotAcctiveActivity.class);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
//                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressBar();
                    }
                });
    }

    private void requestProfileSocialNetwork(String accessTokenString) {
        apiService
                .loginGoogle(
                        Constants.GRAND_TYPE_SOCIAL,
                        Constants.CLIENT_ID,
                        Constants.SECRET_KEY,
                        typeNetwork,
                        accessTokenString
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<UserResponse>() {
                    @Override
                    public void onSuccess(UserResponse user) {
                        mTypeFullAccessToken = user.getTokenType() + Constants.SPACE + user.getAccessToken();
                        requestProfile();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressBar();
                        DialogUtils.showDialogConfirmLogin(LoginActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resulrCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resulrCode, data);
        super.onActivityResult(requestCode, resulrCode, data);
        ZaloSDK.Instance.onActivityResult(this, requestCode, resulrCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    /**
     * Handle Login with Google
     *
     * @param completedTask
     */
    private void handleSignInResult(com.google.android.gms.tasks.Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                mUserProfile = new User(account.getDisplayName(), "", "", "", account.getEmail());
                requestProfileSocialNetwork(account.getIdToken());
//                SaveSharedPreference.setInforUser(getApplicationContext(), mUserProfile);
            }
        } catch (ApiException e) {
            hideProgressBar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void setTextUsername(String s) {
        edtUserName.setText(s, TextView.BufferType.EDITABLE);
    }

    @Override
    public void setUsernameError(String s) {
        txtErrorUser.setVisibility(View.VISIBLE);
        txtErrorUser.setText(s);
    }

    @Override
    public void showProgressBar() {
        DialogUtils.showDialogLoadProgress(LoginActivity.this);
    }

    @Override
    public void hideProgressBar() {
        DialogUtils.hideDialogLoadProgress();
    }

    @Override
    public void setPasswordError(String s) {
        txtxErrorPass.setVisibility(View.VISIBLE);
        txtxErrorPass.setText(s);

    }

    @Override
    public void setHideErrorUser() {
        txtErrorUser.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setHideErrorPass() {
        txtxErrorPass.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setDisableButton() {
        btnLogin.setEnabled(false);
        btnLogin.setBackground(getDrawable(R.drawable.background_btn_login_disable));
    }

    @Override
    public void setActiveButton() {
        btnLogin.setEnabled(true);
        btnLogin.setBackground(getDrawable(R.drawable.background_btn_login));
    }

    @Override
    public void navigateToHome() {
        if (SaveSharedPreference.getCountLogin(getApplicationContext()) == Constants.ZERO) {
            //show tutorial here
            startActivity(LoginActivity.this, WelcomeActivity.class, true);
            //SaveSharedPreference.setCountLogin(getApplicationContext(), Constants.FIRST);
        } else {
            FirebaseUtil.logEventLogin(FirebaseAnalytics.getInstance(this.getApplicationContext()),
                    FirebaseUtil.EVENT_LOGIN_PHONE);
            startHomeActivity(this, MainActivity.class);
        }

    }

    @Override
    public void navigationToActiveAccount() {
        Intent intent = new Intent(LoginActivity.this, VerifyOPTAccountNotAcctiveActivity.class);
        intent.putExtra(Constants.KEY_PHONE, edtUserName.getText().toString());
        intent.putExtra(Constants.KEY_PASS, edtPassword.getText().toString());
        startActivity(intent);
    }

    @Override
    public void navigateToForgotPassword() {

    }

    @Override
    public void navigateToSignin() {

    }

    @Override
    public void setVisibilityUsername(boolean b) {
        if (b) {
            btnDeleteUser.setVisibility(View.VISIBLE);
        } else {
            btnDeleteUser.setVisibility(View.GONE);
        }

    }

    @Override
    public void setVisibilityPassword(boolean b) {
        if (b) {
            btnDeletePass.setVisibility(View.VISIBLE);
        } else {
            btnDeletePass.setVisibility(View.GONE);
        }
    }


    @Override
    public void setImageUserNameDeleteFocus(boolean b) {
        if (b && !TextUtils.isEmpty(String.valueOf(edtUserName.getText()))) {
            btnDeleteUser.setVisibility(View.VISIBLE);
        } else {
            btnDeleteUser.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setImagePassWordDeleteFocus(boolean b) {
        if (b && !TextUtils.isEmpty(String.valueOf(edtPassword.getText()))) {
            btnDeletePass.setVisibility(View.VISIBLE);
        } else {
            btnDeletePass.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoginFb:
                typeNetwork = "facebook";
                LoginManager.getInstance().logOut();
                loginButton.performClick();
                break;
            case R.id.btnLoginGG:
                typeNetwork = "google";
                signIn();
                break;
            case R.id.btnLoginZalo:
                showProgressBar();
                typeNetwork = "zalo";
                login_zalo_via_web_click();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callbackManager = null;
    }


    @Override
    public void onValidateComplete(boolean z, int i, long j, String str) {

    }

    public void login_zalo_via_web_click() {
        ZaloSDK.Instance.unauthenticate();
        ZaloSDK.Instance.authenticate(this, LoginVia.APP_OR_WEB, listener);
    }

    public void getProfile() {
        showProgressBar();
        String[] fields = {"id", "birthday", "gender", "picture", "name"};
        ZaloSDK.Instance.getProfile(this, mZaloOpenAPICallback, fields);
    }

    /**
     *
     */
    private final ZaloOpenAPICallback mZaloOpenAPICallback = new ZaloOpenAPICallback() {
        @Override
        public void onResult(JSONObject jSONObject) {
            if (jSONObject.has("name")) {
                String name = "";
                try {
                    name = jSONObject.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String image = "";
                try {
                    image = jSONObject.getJSONObject("picture").getJSONObject("data").getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String gender = "";
                try {
                    gender = jSONObject.getString("gender");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String birtday = "";
                try {
                    birtday = jSONObject.getString("birthday");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mUserProfile = new User(name, image, gender, birtday, "");
                requestProfileSocialNetwork(mOauthCode);
            } else {
                hideProgressBar();
            }
        }
    };

    //Login Zalo callback
    private class LoginListener extends OAuthCompleteListener {

        private Activity ctx;

        public LoginListener(Activity ctx) {
            this.ctx = ctx;
        }

        @Override
        public void onSkipProtectAcc(Dialog dialog) {
            hideProgressBar();
            dialog.dismiss();
        }

        @Override
        public void onProtectAccComplete(int errorCode, String message, Dialog dialog) {
            hideProgressBar();
            if (errorCode == 0) {

                ZaloSDK.Instance.isAuthenticate((validated, error_Code, userId, oauthCode) -> {

                });

                dialog.dismiss();
            }

            com.zing.zalo.zalosdk.payment.direct.Utils.showAlertDialog(ctx, message, null);

        }

        @Override
        public void onAuthenError(int errorCode, String message) {
            hideProgressBar();
            if (!TextUtils.isEmpty(message))
                com.zing.zalo.zalosdk.payment.direct.Utils.showAlertDialog(ctx, message, null);
            super.onAuthenError(errorCode, message);
        }

        @Override
        public void onGetOAuthComplete(OauthResponse response) {
            hideProgressBar();
            super.onGetOAuthComplete(response);
            mOauthCode = response.getOauthCode();
            getProfile();
        }
    }
}
