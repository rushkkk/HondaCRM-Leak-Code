package vn.co.honda.hondacrm.ui.activities.register.ui;

import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.home.MainActivity;
import vn.co.honda.hondacrm.ui.activities.login.presenter.SaveSharedPreference;
import vn.co.honda.hondacrm.ui.activities.login.ui.LoginActivity;
import vn.co.honda.hondacrm.ui.activities.login.ui.WelcomeActivity;
import vn.co.honda.hondacrm.utils.Constants;

public class SendOTPSuccessActivity extends BaseActivity {

    @BindView(R.id.btnNext)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otpsuccess);
    }

    @OnClick(R.id.btnNext)
    public void onNextClick() {
        if (SaveSharedPreference.getCountLogin(getApplicationContext()) == Constants.ZERO) {
            startActivity(SendOTPSuccessActivity.this, WelcomeActivity.class,true);
        } else {
            startHomeActivity(this, MainActivity.class);
        }
    }
}
