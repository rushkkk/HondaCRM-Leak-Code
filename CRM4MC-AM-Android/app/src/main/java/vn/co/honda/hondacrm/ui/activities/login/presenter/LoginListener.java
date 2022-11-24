package vn.co.honda.hondacrm.ui.activities.login.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.zing.zalo.zalosdk.oauth.OAuthCompleteListener;
import com.zing.zalo.zalosdk.oauth.OauthResponse;
import com.zing.zalo.zalosdk.oauth.ValidateOAuthCodeCallback;
import com.zing.zalo.zalosdk.oauth.ZaloOpenAPICallback;
import com.zing.zalo.zalosdk.oauth.ZaloSDK;

import org.json.JSONException;
import org.json.JSONObject;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.user.UserResponse;
import vn.co.honda.hondacrm.ui.activities.home.MainActivity;
import vn.co.honda.hondacrm.ui.activities.login.model.User;
import vn.co.honda.hondacrm.ui.activities.login.ui.LoginSocicalActivity;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

public class LoginListener extends OAuthCompleteListener {
    public static String userId = "";
    private Context ctx;
    User userProfile;

    public LoginListener(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onSkipProtectAcc(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onProtectAccComplete(int errorCode, String message, Dialog dialog) {

        if (errorCode == 0) {

            ZaloSDK.Instance.isAuthenticate(new ValidateOAuthCodeCallback() {

                @Override
                public void onValidateComplete(boolean validated, int error_Code, long userId, String oauthCode) {

                }
            });

            dialog.dismiss();
        }

        com.zing.zalo.zalosdk.payment.direct.Utils.showAlertDialog(ctx, message, null);

    }

    @Override
    public void onAuthenError(int errorCode, String message) {
        if (ctx != null && !TextUtils.isEmpty(message)) {

            com.zing.zalo.zalosdk.payment.direct.Utils.showAlertDialog(ctx, message, null);
        }
        super.onAuthenError(errorCode, message);
    }

    @Override
    public void onGetOAuthComplete(OauthResponse response) {
        super.onGetOAuthComplete(response);

        DialogUtils.showDialogLoadProgress(ctx);

//        ZaloSDK.Instance.submitAppUserData("" + ZaloSDK.Instance.getZaloId(), ZaloSDK.Instance.getLastestLoginChannel(), "zalo", "appUTMSource", null);

        userId = String.valueOf(response.getuId());
        String oauthCode = response.getOauthCode();
        // SavaTokenSharePreference.setTokenZalo(ctx, response.getOauthCode());

        String[] Fields = {"id", "birthday", "gender", "picture", "name"};
        ZaloSDK.Instance.getProfile(ctx, new ZaloOpenAPICallback() {
            @Override
            public void onResult(JSONObject arg0) {
                DialogUtils.hideDialogLoadProgress();
//                try {
                if (arg0 != null) {
                    if (arg0.has("name")) {

                        String name = "";
                        try {
                            name = arg0.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        LoginActivity.userCurent.setFullName(name);
                        String image = "";
                        try {
                            image = arg0.getJSONObject("picture").getJSONObject("data").getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        LoginActivity.userCurent.setAvatar(image);
                        String gender = "";
                        try {
                            gender = arg0.getString("gender");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String birtday = "";
                        try {
                            birtday = arg0.getString("birthday");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        userProfile = new User(name, image, gender, birtday, "");
                        requestProfileSocialNetwork(oauthCode);
                        SaveSharedPreference.setInforUser(ctx, userProfile);

                        response.getSocialId();
                    }
                }
//                } catch (Exception ex) {
//
//                }

            }
        }, Fields);
//
//        if (SaveSharedPreference.getUserName(ctx).isEmpty()) {
//            Intent intents = new Intent(ctx, LoginSocicalActivity.class);
//            ctx.startActivity(intents);
//        } else {
//            Intent intent = new Intent(ctx, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            ctx.startActivity(intent);
//
//        }

    }

    private void requestProfileSocialNetwork(String accessTokenString) {
        ApiService apiService = ApiClient.getClient(this.ctx).create(ApiService.class);
        String network = "";
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.ctx);
        apiService
                .loginGoogle(
                        "social",
                        Constants.CLIENT_ID,
                        Constants.SECRET_KEY,
                        "zalo",
                        accessTokenString
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<UserResponse>() {
                    @Override
                    public void onSuccess(UserResponse user) {
                        DialogUtils.hideDialogLoadProgress();
                        String access = user.getTokenType() + Constants.SPACE + user.getAccessToken();
                        if (TextUtils.equals(user.getAccessToken(), PrefUtils.getFullTypeAccessToken(ctx))) {
                            Intent intent = new Intent(ctx, MainActivity.class);

                            ctx.startActivity(intent);
                            ((Activity) ctx).finish();
                        } else{
                            Intent intent = new Intent(ctx, LoginSocicalActivity.class);
                            intent.putExtra(Constants.KEY_SOCIAL, userProfile);
                            intent.putExtra(Constants.KEY_ACCESS_TOKEN, access);
                            ctx.startActivity(intent);
                            PrefUtils.storeAccessTokenKey(ctx, access);
                        }
                        // Storing user access Token in preferences
//                        loginSocialView.hideProgressDialog();
//                        PrefUtils.storeAccessTokenKey((Context) loginSocialView, user.getAccessToken());
//                        PrefUtils.storeTokenTypeKey((Context) loginSocialView, user.getTokenType());
//                        loginSocialView.validateSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtils.hideDialogLoadProgress();
//                        DialogUtils.showDialogConfirmLogin((Context) loginSocialView, R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, e.getMessage());
//                        loginSocialView.hideProgressDialog();
                    }
                });

    }
}