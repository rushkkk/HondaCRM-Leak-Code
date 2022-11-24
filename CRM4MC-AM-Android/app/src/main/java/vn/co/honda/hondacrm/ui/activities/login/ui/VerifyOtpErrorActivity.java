package vn.co.honda.hondacrm.ui.activities.login.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;

public class VerifyOtpErrorActivity extends BaseActivity {
    private Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_fail);
        setTitleHeader(getString(R.string.lbs_sign_up_title));
        btnFinish =  findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(VerifyOtpErrorActivity.this,LoginActivity.class,true);
            }
        });
    }
}
