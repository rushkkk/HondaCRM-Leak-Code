package vn.co.honda.hondacrm.ui.activities.login.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.zing.zalo.zalosdk.oauth.ValidateOAuthCodeCallback;
import com.zing.zalo.zalosdk.oauth.ZaloSDK;

import java.text.ParseException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.user.UserProfile;
import vn.co.honda.hondacrm.net.model.user.UserProfileResponse;
import vn.co.honda.hondacrm.net.model.user.UserResponse;
import vn.co.honda.hondacrm.ui.activities.login.model.User;
import vn.co.honda.hondacrm.ui.activities.login.ui.LoginSocialView;
import vn.co.honda.hondacrm.ui.activities.profile.ProfileActivity;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DateTimeUtils;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;
import vn.co.honda.hondacrm.utils.Utils;

public class LoginSocialPresenterImpl implements LoginSocialPresenter, ValidateOAuthCodeCallback {
    int sumTrue;
    LoginSocialView loginSocialView;
    ApiService apiService;
    String mTypeFullAccessToken;

    public LoginSocialPresenterImpl(LoginSocialView loginSocialView) {
        this.loginSocialView = loginSocialView;
    }


    @Override
    public void setShowDialog() {
        loginSocialView.showProgressDialog();
    }

    @Override
    public void setHideDialog() {
        loginSocialView.hideProgressDialog();
    }


    @Override
    public void validate(String name, String phoneNumber, String passWorld, String confirmPassWorld,
                         String yearBirth, boolean isAccessPolicy, int gender) {

        apiService = ApiClient.getClient((Context) loginSocialView).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken((Context) loginSocialView);
        Log.d("TAGGGGGGGGGG", mTypeFullAccessToken);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        sumTrue = 0;
        if (TextUtils.isEmpty(name)) {
            loginSocialView.showErrName(((Context) loginSocialView).getString(R.string.errNameEmpty));
        } else {
            loginSocialView.hideErrName();
            sumTrue += 1;
        }
        if (phoneNumber.isEmpty()) {
            loginSocialView.showErrPhoneNumber(((Context) loginSocialView).getString(R.string.errPhoneEmpty));
        } else if (phoneNumber.length() == 9 || phoneNumber.length() == 10) {
            if (phoneNumber.length() == 9) {
                loginSocialView.hideErrPhone();
                sumTrue += 1;
            } else {
                if (phoneNumber.charAt(0) != '0') {
                    loginSocialView.showErrPhoneNumber(((Context) loginSocialView).getString(R.string.errPhoneIncorrect));
                } else {
                    loginSocialView.hideErrPhone();
                    sumTrue += 1;
                }
            }
        } else {
            loginSocialView.showErrPhoneNumber(((Context) loginSocialView).getString(R.string.errPhoneIncorrect));
        }
        if (TextUtils.isEmpty(passWorld)) {
            loginSocialView.showErrPassWord(((Context) loginSocialView).getString(R.string.errPassEmpty));
        } else if (checkPassword(passWorld)) {
            loginSocialView.showErrPassWord(((Context) loginSocialView).getString(R.string.errWhiteSpace));
        } else if (passWorld.length() < 6) {
            loginSocialView.showErrPassWord(((Context) loginSocialView).getString(R.string.errPassleast6));
        } else {
            loginSocialView.hideErrPass();
            sumTrue += 1;
        }
        if (TextUtils.isEmpty(confirmPassWorld)) {
            loginSocialView.showErrConfirmPassWorld(((Context) loginSocialView).getString(R.string.errConfirmPassEmpty));
        } else if (!confirmPassWorld.equals(passWorld)) {
            loginSocialView.showErrConfirmPassWorld(((Context) loginSocialView).getString(R.string.err_wrong_confirm));
        } else {
            loginSocialView.hideErrPassConfirm();
            sumTrue += 1;
        }

        if (!isAccessPolicy) {
        } else {
            sumTrue += 1;

        }
        if (!TextUtils.isEmpty(yearBirth)) {
            try {
                if (!DateTimeUtils.isValidateBirthDate(yearBirth)) {
                    loginSocialView.showErrYearOfBirth(((Context) loginSocialView).getString(R.string.err_year_incorrect));
                } else {
                    loginSocialView.hideErrBirth();
                    sumTrue += 1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            loginSocialView.hideErrBirth();
            sumTrue += 1;
        }

        if (sumTrue == 6) {
            loginSocialView.showProgressDialog();
            requestApiSocial(name, phoneNumber, passWorld, confirmPassWorld, yearBirth, gender);

        }
    }

    private void requestApiSocial(String name, String phone, String pass, String confirmPass, String dateOfBirth, Integer gender) {
        RequestBody requestFile;
        MultipartBody.Part body = null;
        RequestBody userName = RequestBody.create(
                MediaType.parse("text/plain"),
                name);

        RequestBody phones = RequestBody.create(
                MediaType.parse("text/plain"),
                phone);

        RequestBody passs = RequestBody.create(
                MediaType.parse("text/plain"),
                pass);
        RequestBody confirmPasss = RequestBody.create(
                MediaType.parse("text/plain"),
                confirmPass);

        RequestBody dateOfBirths;
        if (!TextUtils.isEmpty(dateOfBirth)) {
            dateOfBirths = RequestBody.create(
                    MediaType.parse("text/plain"),
                    DateTimeUtils.convertToSendServer(dateOfBirth));
        } else {
            dateOfBirths = RequestBody.create(
                    MediaType.parse("text/plain"),
                    Constants.EMPTY);
        }

        apiService.updateProfileSocial(
                mTypeFullAccessToken,
                userName,
                phones,
                passs,
                confirmPasss,
                dateOfBirths,
                gender)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<UserProfileResponse>() {
                    @Override
                    public void onSuccess(UserProfileResponse response) {
                        loginSocialView.hideProgressDialog();
//                        SaveSharedPreference.setUser((Context) loginSocialView, new User("", confirmPassWorld));
                        loginSocialView.validateSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loginSocialView.showErrPhoneNumber(((Context) loginSocialView).getString(R.string.err_phone_is_exits));
                        loginSocialView.hideProgressDialog();
//                        DialogUtils.showDialogConfirmLogin(ProfileActivity.this, R.layout.dialog_notification_erro_network, 0.9f, 0.2f, e.getMessage());
                    }
                });
    }

    private boolean checkPassword(String password) {
        final String PASSWORD_PATTERN = "(?=\\S+$)";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    @Override
    public void onValidateComplete(boolean z, int i, long j, String str) {

    }
}
