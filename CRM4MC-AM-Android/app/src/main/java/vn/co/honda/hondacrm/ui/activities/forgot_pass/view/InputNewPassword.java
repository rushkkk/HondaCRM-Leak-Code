package vn.co.honda.hondacrm.ui.activities.forgot_pass.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.base.ResponseObject;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.login.model.User;
import vn.co.honda.hondacrm.ui.activities.login.presenter.SaveSharedPreference;
import vn.co.honda.hondacrm.utils.DialogUtils;

public class InputNewPassword extends BaseActivity {
    private Button btnChangePassword;
    private EditText edtNewPassword, edtConfirmPassword;
    private TextView txtErrNewPass, txtErrNewConfirmPass;
    private String pass="";
    private String confirmPass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_new_password);
        setTitleHeader(getString(R.string.lb_forgot_pass_title));

        btnChangePassword = findViewById(R.id.btn_change_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        edtNewPassword = findViewById(R.id.edt_new_password);
        txtErrNewPass = findViewById(R.id.txtNewPass);
        txtErrNewConfirmPass = findViewById(R.id.txtNewConfirmPass);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String phone = bundle.getString("phone");
        String otp = bundle.getString("otp");
        setEnableButton(false);
        btnChangePassword.setOnClickListener(v -> {
            if (edtConfirmPassword.getText().toString().isEmpty() || edtNewPassword.getText().toString().isEmpty()) {
                if (edtNewPassword.getText().toString().isEmpty()) {
                    txtErrNewPass.setVisibility(View.VISIBLE);
                    txtErrNewPass.setText(getString(R.string.err_password_empty));
                } else {
                    txtErrNewPass.setVisibility(View.INVISIBLE);
                }
                if (edtConfirmPassword.getText().toString().isEmpty()) {
                    txtErrNewConfirmPass.setVisibility(View.VISIBLE);
                    txtErrNewConfirmPass.setText(getString(R.string.err_not_null));
                } else {
                    txtErrNewConfirmPass.setVisibility(View.INVISIBLE);
                }
            } else if (checkPassword(edtNewPassword.getText().toString())) {
                txtErrNewPass.setVisibility(View.VISIBLE);
                txtErrNewPass.setText(getString(R.string.errWhiteSpace));
            } else if (edtNewPassword.getText().toString().length() < 6) {
                txtErrNewPass.setVisibility(View.VISIBLE);
                txtErrNewPass.setText(getString(R.string.errPassleast6));
                txtErrNewConfirmPass.setVisibility(View.INVISIBLE);
            } else if (!edtConfirmPassword.getText().toString().equals(edtNewPassword.getText().toString())) {
                txtErrNewPass.setVisibility(View.INVISIBLE);
                txtErrNewConfirmPass.setVisibility(View.VISIBLE);
                txtErrNewConfirmPass.setText(getString(R.string.err_wrong_confirm));
            } else {
                txtErrNewConfirmPass.setVisibility(View.INVISIBLE);
                showProgressBar();
                ApiService apiService = ApiClient.getClient(InputNewPassword.this.getApplicationContext()).create(ApiService.class);
                apiService.resetPassword(
                        otp, phone, edtNewPassword.getText().toString(), edtConfirmPassword.getText().toString()
                )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleSubscriber<ResponseObject>() {
                            @Override
                            public void onSuccess(ResponseObject response) {
                                hideProgressBar();
                                SaveSharedPreference.setUser(InputNewPassword.this, new User(phone, edtConfirmPassword.getText().toString()));
                                Intent intents = new Intent(InputNewPassword.this, ResultChangePassword.class);
                                startActivity(intents);
                                finish();

                            }

                            @Override
                            public void onError(Throwable e) {
                                DialogUtils.showDialogConfirmLogin(InputNewPassword.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, e.getMessage());
                                hideProgressBar();

                            }
                        });


            }
        });
        edtNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pass= charSequence.toString();
                if (TextUtils.isEmpty(pass)||TextUtils.isEmpty(confirmPass)){
                    setEnableButton(false);
                }else {
                    setEnableButton(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmPass= charSequence.toString();
                if (TextUtils.isEmpty(pass)||TextUtils.isEmpty(confirmPass)){
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

    private boolean checkPassword(String password) {
        final String PASSWORD_PATTERN = "(?=\\S+$)";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
    public void setEnableButton(boolean b) {
        btnChangePassword.setEnabled(b);
    }

    public void showProgressBar() {
        DialogUtils.showDialogLoadProgress(InputNewPassword.this);
    }

    public void hideProgressBar() {
        DialogUtils.hideDialogLoadProgress();
    }
}
