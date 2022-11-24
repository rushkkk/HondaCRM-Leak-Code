package vn.co.honda.hondacrm.ui.activities.forgot_pass.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.base.ResponseOTPObject;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.login.ui.VerifyOtpErrorActivity;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;

public class VerifyOTPForgotActivity extends BaseActivity {
    @BindView(R.id.btnVerify)
    Button btnVerify;
    String phone;
    TextView txtCountTime, txtRemaningTime, tvMessageWrong, btnResent,txtErrVerify;
    EditText editOTP;
    private int numberRequest = 0;
    public boolean isTime = false;
    private CountDownTimer countDownTimer;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_otp);
        ButterKnife.bind(this);
        setTitleHeader(getString(R.string.title_forgot_password));

        txtCountTime = findViewById(R.id.txtCount);
        txtRemaningTime = findViewById(R.id.time);
        tvMessageWrong = findViewById(R.id.tvMessageWrong);
        editOTP = findViewById(R.id.editOTP);
        editOTP.requestFocus();
        btnVerify = findViewById(R.id.btnVerify);
        btnResent = findViewById(R.id.btnResent);
        txtErrVerify = findViewById(R.id.txtErrorVerify);
        apiService = ApiClient.getClient(getApplicationContext()).create(ApiService.class);
        setEnableButton(false);
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            phone = intent.getString(Constants.KEY_PHONE);
        }

        // Default display remaining time send OTP
        setRemainingTime();


        btnVerify.setOnClickListener(view -> {
            String number = editOTP.getText().toString();
            if (number.isEmpty()) {
                txtErrVerify.setText(R.string.msg_empty_otp);
                txtErrVerify.setVisibility(View.VISIBLE);
            } else {
                if (!isTime) {
                    if (numberRequest >= Constants.MAX_REQUEST_OTP) {
                        tvMessageWrong.setText(R.string.msg_wrong_otp_exceeded_maxium);
                        tvMessageWrong.setVisibility(View.VISIBLE);
                        txtRemaningTime.setVisibility(View.INVISIBLE);
                        txtCountTime.setVisibility(View.INVISIBLE);
                        txtErrVerify.setVisibility(View.INVISIBLE);
                    } else if (number.length() != Constants.MAX_LENGHT_OTP) {
                        txtErrVerify.setText(R.string.msg_wrong_valid_otp);
                        txtErrVerify.setVisibility(View.VISIBLE);
                    } else {
                        txtErrVerify.setVisibility(View.INVISIBLE);
                        showProgressBar();
                        verifyOTP(number);
                    }

                } else {
                    tvMessageWrong.setText(R.string.msg_wrong_otp_expired);
                    tvMessageWrong.setVisibility(View.VISIBLE);
                    txtRemaningTime.setVisibility(View.INVISIBLE);
                    txtCountTime.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnResent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();
                apiService.forgotPassword(
                        phone
                )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleSubscriber<ResponseOTPObject>() {
                            @Override
                            public void onSuccess(ResponseOTPObject response) {

                                hideProgressBar();
                                String OTP_SEND_MESSAGE = response.getData();
                                if (TextUtils.equals(OTP_SEND_MESSAGE, Constants.OTP_SEND_WITHIN_MAX_TIMES)) {
                                    startActivity(VerifyOTPForgotActivity.this, VerifyOtpErrorActivity.class, true);
                                } else {
                                    txtErrVerify.setVisibility(View.INVISIBLE);
                                    isTime = false;
                                    numberRequest = 0;
                                    editOTP.setEnabled(true);
                                    btnVerify.setEnabled(true);
                                    tvMessageWrong.setVisibility(View.INVISIBLE);
                                    txtRemaningTime.setVisibility(View.VISIBLE);
                                    txtCountTime.setVisibility(View.VISIBLE);
                                    setRemainingTime();

                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                DialogUtils.showDialogConfirmLogin(VerifyOTPForgotActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));
                                hideProgressBar();
                            }
                        });
            }

        });
        editOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence.toString())){
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
    public void setEnableButton(boolean b) {
        btnVerify.setEnabled(b);
        if (b) {
            btnVerify.setBackground(getDrawable(R.drawable.selector_red_button));
        } else {
            btnVerify.setBackground(getDrawable(R.drawable.btn_signup_disable));
        }
    }


    private void setRemainingTime() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDown();
        } else {
            countDown();
        }
    }
    private void verifyOTP(String number) {

        apiService.verifyOTPForgotPassword(
                number,
                phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ResponseOTPObject>() {
                    @Override
                    public void onSuccess(ResponseOTPObject response) {
                        hideProgressBar();
                        numberRequest++;
                        String OTP_SEND_MESSAGE = response.getData();
                        if (TextUtils.equals(OTP_SEND_MESSAGE, Constants.OTP_SEND_WITHIN_MAX_TIMES)) {
                            startActivity(VerifyOTPForgotActivity.this, VerifyOtpErrorActivity.class, true);
                        } else if (TextUtils.equals(Constants.OTP_IS_NOT_MATCH, OTP_SEND_MESSAGE)) {
                            txtErrVerify.setText(R.string.msg_wrong_otp);
                            txtErrVerify.setVisibility(View.VISIBLE);
                        } else {
                            hideProgressBar();
                            Bundle bundle = new Bundle();
                            bundle.putString("phone", phone);
                            bundle.putString("otp", number);
                            Intent intents = new Intent(VerifyOTPForgotActivity.this, InputNewPassword.class);
                            intents.putExtras(bundle);
                            startActivity(intents);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        txtErrVerify.setText(R.string.msg_wrong_otp);
                        txtErrVerify.setVisibility(View.VISIBLE);
                        hideProgressBar();
                    }
                });
    }

    public void countDown() {
        countDownTimer = new CountDownTimer(300000, 1000) {

            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {
                //here you can have your logic to set text to edittext
                txtCountTime.setText("" + String.format("%d:%d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                isTime = true;
                editOTP.setEnabled(false);
                btnVerify.setEnabled(false);
                tvMessageWrong.setText(R.string.msg_wrong_otp_expired);
                tvMessageWrong.setVisibility(View.VISIBLE);
                txtRemaningTime.setVisibility(View.INVISIBLE);
                txtCountTime.setVisibility(View.INVISIBLE);

            }

        }.start();
    }

    public void showProgressBar() {
        DialogUtils.showDialogLoadProgress(VerifyOTPForgotActivity.this);
    }

    public void hideProgressBar() {
        DialogUtils.hideDialogLoadProgress();
    }
}
