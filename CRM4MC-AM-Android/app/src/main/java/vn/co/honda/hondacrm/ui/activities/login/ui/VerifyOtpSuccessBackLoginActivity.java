package vn.co.honda.hondacrm.ui.activities.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.forgot_pass.ForgotPasswordActivity;
import vn.co.honda.hondacrm.ui.activities.forgot_pass.view.InputNewPassword;
import vn.co.honda.hondacrm.ui.activities.forgot_pass.view.VerifyOTPForgotActivity;
import vn.co.honda.hondacrm.ui.activities.login.presenter.SaveSharedPreference;

public class VerifyOtpSuccessBackLoginActivity extends BaseActivity {
    Button btnNext ;
    Boolean isForgot = false;
    private String phone;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        isForgot = getIntent().getBooleanExtra("KEY_FORGOT", false);
//        phone = getIntent().getStringExtra("phone");
//        number = getIntent().getStringExtra("otp");
        setTitleHeader(getString(R.string.lb_active_account_otp));
        ImageButton imgBack = findViewById(R.id.btnBack);
        setViewGone(imgBack);

        btnNext =  findViewById(R.id.btnNext);
        btnNext.setOnClickListener(view -> {
            if(isForgot) {
                Bundle bundle = new Bundle();
//                bundle.putString("phone", phone);
//                bundle.putString("otp", number);
                Intent intents = new Intent(VerifyOtpSuccessBackLoginActivity.this, ForgotPasswordActivity.class);
                intents.putExtras(bundle);
                startActivity(intents);
                finish();
            } else {
                startActivity(VerifyOtpSuccessBackLoginActivity.this, LoginActivity.class, true);
                SaveSharedPreference.setFirstLogin(getApplicationContext(), true);
            }
        });

    }
}
