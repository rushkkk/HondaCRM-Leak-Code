package com.honda.connmc.View.activity;

import android.content.Intent;
import android.os.Handler;

import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.R;
import com.honda.connmc.View.base.BaseActivity;

public class SplashActivity extends BaseActivity {
    private final static int SPLASH_DISPLAY_LENGTH = 1500;
    private boolean isRunSplash = false;
    @Override
    protected int getIdLayout() {
        return R.layout.splash_screen;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void setActionForView() {
        runSplash();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRunSplash) {
            processCheckBluetooth();
        }
    }

    private void processCheckBluetooth() {
        if (!BluetoothUtils.checkBluetooth()) {
            onBluetoothOFF();
        } else {
            onBluetoothON();
        }
    }

    @Override
    protected void onBluetoothON() {
        super.onBluetoothON();
        if (mBluetoothRequestDialog == null || !mBluetoothRequestDialog.isShowing()) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onBluetoothOFF() {
        super.onBluetoothOFF();
    }

    private void runSplash() {
        new Handler().postDelayed(() -> {
            isRunSplash  = true;
            processCheckBluetooth();
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected boolean isNeedCheckBlueToothWhenAppResume() {
        return false;
    }
}
