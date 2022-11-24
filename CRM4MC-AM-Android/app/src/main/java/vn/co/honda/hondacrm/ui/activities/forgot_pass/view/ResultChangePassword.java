package vn.co.honda.hondacrm.ui.activities.forgot_pass.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.login.model.User;
import vn.co.honda.hondacrm.ui.activities.login.presenter.SaveSharedPreference;
import vn.co.honda.hondacrm.ui.activities.login.ui.LoginActivity;

public class ResultChangePassword extends BaseActivity {
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_change_password);
        setTitleHeader(getString(R.string.lb_forgot_pass_title));
        ImageButton imgBack = findViewById(R.id.btnBack);
        setViewGone(imgBack);
        btnNext =  findViewById(R.id.btn_next_success);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveSharedPreference.setUser(getApplicationContext(),new User("",""));
                startActivity(ResultChangePassword.this, LoginActivity.class,true);
            }
        });

    }
}
