package vn.co.honda.hondacrm.ui.activities.register.presenter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.ParseException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.user.UserRegisterResponse;
import vn.co.honda.hondacrm.ui.activities.login.model.User;
import vn.co.honda.hondacrm.ui.activities.login.presenter.SaveSharedPreference;
import vn.co.honda.hondacrm.ui.fragments.connected.views.ConnectedFragment;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DateTimeUtils;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

public class RegisterPresenter implements RegisterPresentImpl {

    RegisterViewImpl registerView;
    private String names ="";
    private String phones ="";
    private String passs= "";
    private String confirmPasss = "";
    private boolean isCheck = false;

    int sumTrue;

    public RegisterPresenter(RegisterViewImpl registerView) {
        this.registerView = registerView;
    }


    @Override
    public void setShowDialog() {
        registerView.showProgressDialog();
    }

    @Override
    public void setHideDialog() {
        registerView.hideProgressDialog();
    }

    @Override
    public void validate(String name, String phoneNumber, String passWorld, String confirmPassWorld,
                         String yearBirth, boolean isAccessPolicy, int gender) {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        sumTrue = 0;
        if (TextUtils.isEmpty(name)) {
            registerView.showErrName(((Context) registerView).getString(R.string.errNameEmpty));
        } else {
            registerView.hideErrName();
            sumTrue += 1;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            registerView.showErrPhoneNumber(((Context) registerView).getString(R.string.errPhoneEmpty));
        } else if (phoneNumber.length() == 9 || phoneNumber.length() == 10) {
            if (phoneNumber.length() == 9) {
                registerView.hideErrPhone();
                phoneNumber = "0" + phoneNumber;
                sumTrue += 1;
            } else {
                if (phoneNumber.charAt(0) != '0') {
                    registerView.showErrPhoneNumber(((Context) registerView).getString(R.string.errPhoneIncorrect));
                } else {
                    registerView.hideErrPhone();
                    sumTrue += 1;
                }
            }
        } else {
            registerView.showErrPhoneNumber(((Context) registerView).getString(R.string.errPhoneIncorrect));
        }
        if (TextUtils.isEmpty(passWorld)) {
            registerView.showErrPassWord(((Context) registerView).getString(R.string.errPassEmpty));
        } else if (checkPassword(passWorld)) {
            registerView.showErrPassWord(((Context) registerView).getString(R.string.errWhiteSpace));
        } else if (passWorld.length() < 6) {
            registerView.showErrPassWord(((Context) registerView).getString(R.string.errPassleast6));
        } else {
            registerView.hideErrPass();
            sumTrue += 1;
        }
        if (TextUtils.isEmpty(confirmPassWorld)) {
            registerView.showErrConfirmPassWorld(((Context) registerView).getString(R.string.errConfirmPassEmpty));
        } else if (!confirmPassWorld.equals(passWorld)) {
            registerView.showErrConfirmPassWorld(((Context) registerView).getString(R.string.err_wrong_confirm));
        } else {
            registerView.hideErrPassConfirm();
            sumTrue += 1;
        }
        if (!isAccessPolicy) {

        } else {
            sumTrue += 1;
        }
        if (!TextUtils.isEmpty(yearBirth)) {
            try {
                if (!DateTimeUtils.isValidateBirthDate(yearBirth)) {
                    registerView.showErrYearOfBirth(((Context) registerView).getString(R.string.err_year_incorrect));
                } else {
                    registerView.hideErrBirth();
                    sumTrue += 1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            registerView.hideErrBirth();
            sumTrue += 1;
        }

        if (sumTrue == 6) {
            registerView.showProgressDialog();

            ApiService apiService = ApiClient.getClient((Context) registerView).create(ApiService.class);

            apiService
                    .registerPhone(
                            name,
                            phoneNumber,
                            passWorld,
                            confirmPassWorld,
                            Constants.CLIENT_ID,
                            Constants.SECRET_KEY,
                            gender,
                            DateTimeUtils.convertToSendServer(yearBirth))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleSubscriber<UserRegisterResponse>() {
                        @Override
                        public void onSuccess(UserRegisterResponse user) {
                            registerView.hideProgressDialog();
                            if (user.getData() != null && TextUtils.equals(user.getData().getPhone().get(Constants.ZERO), Constants.NUMBER_PHONE_EXISTS)) {
                                registerView.showExistPhoneNumber();
                            } else {
                                SaveSharedPreference.setUser((Context) registerView, new User("", confirmPassWorld));
                                registerView.validateSuccess(user.getTokenType() + Constants.SPACE + user.getAccessToken());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            DialogUtils.showDialogConfirmLogin((Context) registerView, R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, ((Context) registerView).getString(R.string.networkError));
                            registerView.hideProgressDialog();
                        }
                    });
        }
    }

    @Override
    public void validateEmpty(EditText name, EditText phone, EditText passworld, EditText confirmPass, CheckBox checkBox) {
         name.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 names =  charSequence.toString();
                 if (TextUtils.isEmpty(names)||TextUtils.isEmpty(phones)||TextUtils.isEmpty(passs)||TextUtils.isEmpty(confirmPasss)|| isCheck==false){
                     registerView.setEnableButton(false);
                 }else {
                     registerView.setEnableButton(true);
                 }

             }

             @Override
             public void afterTextChanged(Editable editable) {

             }
         });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phones =  charSequence.toString();
                if (TextUtils.isEmpty(names)||TextUtils.isEmpty(phones)||TextUtils.isEmpty(passs)||TextUtils.isEmpty(confirmPasss)|| isCheck==false){
                    registerView.setEnableButton(false);
                }else {
                    registerView.setEnableButton(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        passworld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passs =  charSequence.toString();
                if (TextUtils.isEmpty(names)||TextUtils.isEmpty(phones)||TextUtils.isEmpty(passs)||TextUtils.isEmpty(confirmPasss)|| isCheck==false){
                    registerView.setEnableButton(false);
                }else {
                    registerView.setEnableButton(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmPasss =  charSequence.toString();
                if (TextUtils.isEmpty(names)||TextUtils.isEmpty(phones)||TextUtils.isEmpty(passs)||TextUtils.isEmpty(confirmPasss)|| isCheck==false){
                    registerView.setEnableButton(false);
                }else {
                    registerView.setEnableButton(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isCheck = b;
                if (TextUtils.isEmpty(names)||TextUtils.isEmpty(phones)||TextUtils.isEmpty(passs)||TextUtils.isEmpty(confirmPasss) || isCheck==false){
                    registerView.setEnableButton(false);
                }else {
                    registerView.setEnableButton(true);
                }
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
}
