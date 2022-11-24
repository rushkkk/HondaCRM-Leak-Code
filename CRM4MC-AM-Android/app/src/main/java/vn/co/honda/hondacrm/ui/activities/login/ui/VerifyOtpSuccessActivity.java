package vn.co.honda.hondacrm.ui.activities.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.SplashActivity;
import vn.co.honda.hondacrm.ui.activities.home.MainActivity;
import vn.co.honda.hondacrm.ui.activities.login.presenter.SaveSharedPreference;
import vn.co.honda.hondacrm.utils.Constants;

public class VerifyOtpSuccessActivity extends BaseActivity {
    Button btnNext ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        ImageButton imgBack = findViewById(R.id.btnBack);
        setViewGone(imgBack);


        btnNext =  findViewById(R.id.btnNext);
        btnNext.setOnClickListener(view -> {

            if(SaveSharedPreference.getCountLogin(getApplicationContext()) == Constants.ZERO){
                startActivity(VerifyOtpSuccessActivity.this, WelcomeActivity.class,true);
            }else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            finish();
            SaveSharedPreference.setFirstLogin(getApplicationContext(),true);
            SaveSharedPreference.setCountLogin(getApplicationContext(),0);
        });

    }
}
