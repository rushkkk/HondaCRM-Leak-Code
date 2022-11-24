package vn.co.honda.hondacrm.ui.activities.login.presenter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.user.UserResponse;
import vn.co.honda.hondacrm.ui.activities.login.model.User;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;


public class LoginInteractorImpl implements LoginInteractor {

    @Override
    public void login(final User user, final OnLoginFinishedListener listener, final Context context, int delay) {
        // Mock login. I'm creating a handler to delay the answer a couple of seconds

        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);

        apiService
                .loginPhone(
                        user.getNumberPhone(),
                        Constants.CLIENT_ID,
                        Constants.SECRET_KEY,
                        Constants.GRAND_TYPE,
                        user.getPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<UserResponse>() {
                    @Override
                    public void onSuccess(UserResponse userResponse) {
                        // Storing user access Token in preferences
                        listener.setHideProgressbar();
                        if (userResponse.getData() != null) {
                            String message = userResponse.getData();
                            String msg = userResponse.getMessage() == null ? Constants.EMPTY : userResponse.getMessage();
                            if (message.equals(Constants.ACCOUNT_IS_NOT_ACTIVE)) {
                                DialogUtils.showDialogConfirmInfo(context, R.layout.diaglog_confirm, 0.9f, 0.3f, new DialogUtils.DialogListener() {
                                    @Override
                                    public void okButtonClick(Dialog dialog) {
                                        listener.onAccountNotActive();
                                        listener.onHideUserNameError();
                                        listener.onHidePasswordError();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void cancelButtonClick() {

                                    }
                                });

                            } else if (message.equals(Constants.ACCOUNT_IS_NOT_EXISTS)) {

                                DialogUtils.showDialogConfirmLogin(((Context) context), R.layout.dialog_notification_erro_network, 0.9f, 0.2f, ((Context) context).getString(R.string.msg_wrong_login));
                                listener.onHidePasswordError();
                            } else if ((message.equals(Constants.ACCOUNT_IS_NOT_MATCH_PASSWORD))) {
                                listener.onHideUserNameError();
                                DialogUtils.showDialogConfirmLogin(((Context) context), R.layout.dialog_notification_erro_network, 0.9f, 0.2f, ((Context) context).getString(R.string.msg_wrong_login));
                            }else {
                                listener.onHidePasswordError();
                            }
                        } else {
                            // login
                            listener.onHideUserNameError();
                            listener.onHidePasswordError();
                            if (userResponse.getAccessToken() != null) {
                                String currentToken = userResponse.getTokenType()
                                        + Constants.SPACE + userResponse.getAccessToken();
                                if (TextUtils.equals(PrefUtils.getFullTypeAccessToken(context), currentToken)) {
                                    SaveSharedPreference.setCountLogin(context, Constants.FIRST);
                                } else {
                                    PrefUtils.storeAccessTokenKey(context, userResponse.getTokenType()
                                            + Constants.SPACE + userResponse.getAccessToken());
                                }
                                SaveSharedPreference.setUser(context, user);

                                listener.onSuccessLogin();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //listener.onUsernameError("Account incorrect");
                        DialogUtils.showDialogConfirmLogin(context, R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, context.getString(R.string.networkError));
                        listener.setHideProgressbar();
                    }
                });
    }


}