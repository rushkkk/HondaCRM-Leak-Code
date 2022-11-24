package vn.co.honda.hondacrm.ui.activities.notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;

public class NotificationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setTitleHeader(getString(R.string.lb_notification_title));
    }
}
