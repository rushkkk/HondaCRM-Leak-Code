package vn.co.honda.hondacrm.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.home.MainActivity;
import vn.co.honda.hondacrm.ui.activities.login.presenter.SaveSharedPreference;
import vn.co.honda.hondacrm.ui.activities.login.ui.LoginActivity;
import vn.co.honda.hondacrm.ui.activities.login.ui.WelcomeActivity;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.PrefUtils;

/**
 * @author CuongNV31
 */
public class SplashActivity extends BaseActivity {

    private static final Handler HANDLER = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreenNoStatusBar();
        setContentView(R.layout.activity_splash);
        // making notification bar transparent
        changeStatusBarColor(Color.TRANSPARENT);

        HANDLER.postDelayed(mRunnable, Constants.DELAY_TIME);
    }

    // Handle schedule timing delay
    private final Runnable mRunnable = this::switchOpenScreens;

    /**
     * Switch from opening Screens
     */
    private void switchOpenScreens() {
        if (!TextUtils.equals(PrefUtils.getFullTypeAccessToken(SplashActivity.this),null)) {
            openHomeScreen();
        } else {
            openLoginScreen();
        }
    }

    /**
     * Direct to Home Screen
     */
    private void openHomeScreen() {
        SaveSharedPreference.setCountLogin(getApplicationContext(), Constants.FIRST);
        if(SaveSharedPreference.getCountLogin(getApplicationContext()) == Constants.ZERO){
            startActivity(SplashActivity.this, WelcomeActivity.class,true);
        }else {
            startHomeActivity(SplashActivity.this,
                    MainActivity.class);
        }

    }

    /**
     * Direct to Logn Screen
     */
    private void openLoginScreen() {
        startActivity(this, LoginActivity.class, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callBack
        HANDLER.removeCallbacks(mRunnable);
    }
}

