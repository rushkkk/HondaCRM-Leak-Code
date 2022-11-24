package vn.co.honda.hondacrm.ui.activities.forgot_pass;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.base.ResponseOTPObject;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.forgot_pass.view.VerifyOTPForgotActivity;
import vn.co.honda.hondacrm.ui.activities.login.ui.VerifyOPTForgotPassAccountNotActiveActivity;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;


public class ForgotPasswordActivity extends BaseActivity {
    Button btnNext;
    EditText edtPhone;
    TextView txtErrorPhoneForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        setTitleHeader(getString(R.string.lb_forgot_pass_title));
        btnNext = findViewById(R.id.btn_next);
        edtPhone = findViewById(R.id.edt_phone_number);
        txtErrorPhoneForgot = findViewById(R.id.txtErrorPhoneForgot);

        edtPhone.requestFocus();
        setEnableButton(false);
        btnNext.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString();
            if (edtPhone.getText().toString().equals("")) {
                txtErrorPhoneForgot.setVisibility(View.VISIBLE);
                txtErrorPhoneForgot.setText(getString(R.string.errPhoneEmpty));
            }else if (phone.length() ==9 || phone.length() ==10 ) {
                txtErrorPhoneForgot.setVisibility(View.INVISIBLE);
                if (phone.length() ==9 ){
                    phone = "0" + phone;
                    apiNumber(phone);
                }else {
                    if (phone.charAt(0) != '0'){
                        edtPhone.setError(getString(R.string.errPhoneIncorrect) );
                    }else {
                        apiNumber(phone);
                    }
                }
            }  else {
                txtErrorPhoneForgot.setVisibility(View.VISIBLE);
                txtErrorPhoneForgot.setText(getString(R.string.errPhoneIncorrect));
            }
        });
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()){
                    setEnableButton(false);
                }else {
                    setEnableButton(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public void apiNumber(String phone) {
        showProgressBar();
        ApiService apiService = ApiClient.getClient(ForgotPasswordActivity.this.getApplicationContext()).create(ApiService.class);
        apiService.forgotPassword(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ResponseOTPObject>() {
                    @Override
                    public void onSuccess(ResponseOTPObject response) {
                        hideProgressBar();
                        String msg = response.getMessage() == null ? Constants.EMPTY : response.getMessage();
                        String OTP_SEND_MESSAGE = response.getData();
                        if (TextUtils.equals(OTP_SEND_MESSAGE, Constants.ACCOUNT_IS_NOT_ACTIVE)) {
                            DialogUtils.showDialogLogout(ForgotPasswordActivity.this, R.layout.diaglog_confirm_delete, 0.9f, 0.3f, new DialogUtils.DialogListener() {
                                @Override
                                public void okButtonClick(Dialog dialog) {
                                    Intent intent = new Intent(ForgotPasswordActivity.this, VerifyOPTForgotPassAccountNotActiveActivity.class);
                                    intent.putExtra(Constants.KEY_PHONE, edtPhone.getText().toString());
                                    startActivity(intent);
                                    dialog.dismiss();
                                }

                                @Override
                                public void cancelButtonClick() {

                                }
                            }, getString(R.string.lb_account_is_not_active));
                        } else {
                            Intent intent = new Intent(ForgotPasswordActivity.this, VerifyOTPForgotActivity.class);
                            intent.putExtra(Constants.KEY_PHONE, phone);
                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtils.showDialogConfirmLogin(ForgotPasswordActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));
                        hideProgressBar();
                    }
                });
    }
    public void setEnableButton(boolean b) {
        btnNext.setEnabled(b);
        if (b) {
            btnNext.setBackground(getDrawable(R.drawable.selector_red_button));
        } else {
            btnNext.setBackground(getDrawable(R.drawable.btn_signup_disable));
        }
    }
    public void showProgressBar() {
        DialogUtils.showDialogLoadProgress(ForgotPasswordActivity.this);
    }

    public void hideProgressBar() {
        DialogUtils.hideDialogLoadProgress();
    }
}
