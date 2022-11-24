package vn.co.honda.hondacrm.ui.activities.register.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;

/**
 * @author CuongNV31
 */
public class RegisterFailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_new_password);
        ButterKnife.bind(this);
    }
}
