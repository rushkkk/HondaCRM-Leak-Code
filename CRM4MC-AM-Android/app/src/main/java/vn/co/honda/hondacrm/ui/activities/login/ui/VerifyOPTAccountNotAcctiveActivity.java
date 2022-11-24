package vn.co.honda.hondacrm.ui.activities.login.ui;

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
import vn.co.honda.hondacrm.ui.activities.login.presenter.SaveSharedPreference;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

public class VerifyOPTAccountNotAcctiveActivity extends BaseActivity {

    @BindView(R.id.btnVerify)
    Button btnVerify;
    String phone;
    String password;
    TextView txtCountTime, txtRemaningTime, tvMessageWrong, btnResent,txtErrVerify;
    EditText editOTP;
    private int numberRequest = 0;
    private CountDownTimer mCountDownTimer;
    public boolean isTime = false;
    private CountDownTimer countDownTimer;
    private ApiService apiService;
    private String mTypeFullAccessToken;
    private Dialog dialog;
    static int numberResent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_otp);
        ButterKnife.bind(this);
        setTitleHeader(getString(R.string.lb_active_account_otp));

        txtCountTime = findViewById(R.id.txtCount);
        txtRemaningTime = findViewById(R.id.time);
        tvMessageWrong = findViewById(R.id.tvMessageWrong);
        editOTP = findViewById(R.id.editOTP);
        btnVerify = findViewById(R.id.btnVerify);
        btnResent = findViewById(R.id.btnResent);
        txtErrVerify = findViewById(R.id.txtErrorVerify);

        editOTP.requestFocus();

        apiService = ApiClient.getClient(getApplicationContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(getApplicationContext());
        setEnableButton(false);
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            phone = intent.getString("phone");
            password = intent.getString("pass");
        }
        activeCustomer();
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
                        verifyOTP();
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
                showProgressDialog();
                apiService.activeCustomer(
                        phone)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleSubscriber<ResponseOTPObject>() {
                            @Override
                            public void onSuccess(ResponseOTPObject response) {
                                String OTP_SEND_MESSAGE = response.getData();
                                hideProgressDialog();
                                if (TextUtils.equals(OTP_SEND_MESSAGE, Constants.OTP_SEND_WITHIN_MAX_TIMES)) {
                                    startActivity(VerifyOPTAccountNotAcctiveActivity.this, VerifyOtpErrorActivity.class, true);
                                } else {
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
                                DialogUtils.showDialogConfirmLogin(VerifyOPTAccountNotAcctiveActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));
                                hideProgressDialog();
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

    private void countDown() {
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

    private void verifyOTP() {
        showProgressDialog();
        apiService.verifyOTPActiveAccount(
                editOTP.getText().toString(),
                phone,
                password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ResponseOTPObject>() {
                    @Override
                    public void onSuccess(ResponseOTPObject response) {
                        numberRequest++;
                        hideProgressDialog();
                        String OTP_SEND_MESSAGE = response.getData();
                        if (TextUtils.equals(OTP_SEND_MESSAGE, Constants.OTP_SEND_WITHIN_MAX_TIMES)) {
                            startActivity(VerifyOPTAccountNotAcctiveActivity.this, VerifyOtpErrorActivity.class, true);
                        } else if (TextUtils.equals("OTP_NOT_MATCH", OTP_SEND_MESSAGE)) {
                            txtErrVerify.setText(R.string.msg_wrong_otp);
                            txtErrVerify.setVisibility(View.VISIBLE);
                        } else {
                            txtErrVerify.setVisibility(View.INVISIBLE);
                            SaveSharedPreference.setFirstLogin(getApplicationContext(), true);
                            SaveSharedPreference.setCountLogin(getApplicationContext(), 0);
                            Intent intents = new Intent(VerifyOPTAccountNotAcctiveActivity.this, VerifyOtpSuccessBackLoginActivity.class);
                            intents.putExtra("KEY_OTP", "active");
                            startActivity(intents);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtils.showDialogConfirmLogin(VerifyOPTAccountNotAcctiveActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));
                        hideProgressDialog();
                    }
                });
    }

    private void activeCustomer() {
        showProgressDialog();
        apiService.activeCustomer(
                phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ResponseOTPObject>() {
                    @Override
                    public void onSuccess(ResponseOTPObject response) {
                        hideProgressDialog();

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtils.showDialogConfirmLogin(VerifyOPTAccountNotAcctiveActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));
                        hideProgressDialog();
                    }
                });
    }

    private void hideProgressDialog() {
        DialogUtils.hideDialogLoadProgress();
    }

    private void showProgressDialog() {
        // todo something

        DialogUtils.showDialogLoadProgress(VerifyOPTAccountNotAcctiveActivity.this);
    }
}
