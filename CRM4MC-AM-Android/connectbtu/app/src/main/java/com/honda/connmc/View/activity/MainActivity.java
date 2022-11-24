package com.honda.connmc.View.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Interfaces.listener.register.IF_VehicleRegisterListener;
import com.honda.connmc.R;
import com.honda.connmc.Utils.FragmentUtils;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.View.base.BaseActivity;
import com.honda.connmc.View.dialog.DialogCommon;
import com.honda.connmc.View.fragment.EnterPinFragment;
import com.honda.connmc.View.fragment.EnterUsernameFragment;
import com.honda.connmc.View.fragment.LoginFragment;

public class MainActivity extends BaseActivity implements IF_VehicleRegisterListener {

    @Override
    protected int getIdLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setActionForView() {
        BluetoothManager.getInstance().setRegisterListener(this);
        gotoLogin();
    }

    private void gotoLogin() {
        FragmentUtils.replaceFragment(getSupportFragmentManager(), new LoginFragment(),
                R.id.fragment_container, true, FragmentUtils.FragmentAnimationType.TYPE_NO_ANIMATION);
    }

    @Override
    protected boolean isNeedCheckBlueToothWhenAppResume() {
        return true;
    }

    @Override
    public void onRequestPinCode() {
        LogUtils.startEndMethodLog(true);
        runOnUiThread(() -> {
            Fragment fragment = new EnterPinFragment();
            FragmentUtils.replaceFragment(getSupportFragmentManager(), fragment, R.id.fragment_container, true, FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_GO);
        });
        LogUtils.startEndMethodLog(false);
    }

    @Override
    public void onVerifyPinCodeFail() {
        LogUtils.startEndMethodLog(true);
        runOnUiThread(() -> {
            FragmentManager fm = getSupportFragmentManager();
            Fragment f = fm.findFragmentById(R.id.fragment_container);
            if (f instanceof EnterPinFragment) {
                EnterPinFragment enterPinFragment = (EnterPinFragment) f;
                enterPinFragment.handlePinWrong();
            }
        });
        LogUtils.startEndMethodLog(false);
    }

    @Override
    public void onRequestUserName() {
        LogUtils.startEndMethodLog(true);
        runOnUiThread(() -> {
            Fragment fragment = new EnterUsernameFragment();
            FragmentUtils.replaceFragment(getSupportFragmentManager(), fragment, R.id.fragment_container, false, FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_GO);
        });
        LogUtils.startEndMethodLog(false);
    }

    @Override
    public void onResultTooManyUser(String userNameDelete) {
        LogUtils.startEndMethodLog(true);
        LogUtils.d("User Name : " + userNameDelete);
        runOnUiThread(() -> {
            hideProgressDialog();
            FragmentManager fm = getSupportFragmentManager();
            Fragment f = fm.findFragmentById(R.id.fragment_container);
            if (f instanceof EnterUsernameFragment) {
                EnterUsernameFragment enterUsernameFragment = (EnterUsernameFragment) f;
                enterUsernameFragment.handlerTooManyUser(userNameDelete);
            }
        });

        LogUtils.startEndMethodLog(false);
    }

    @Override
    public void onRequestEditUserName(String userName) {
        LogUtils.startEndMethodLog(true);
        runOnUiThread(() -> {
            hideProgressDialog();
            DialogCommon.createPopup(MainActivity.this, "OK", "User name duplicate, Please edit user name!", v -> {

            }).show();
        });

        LogUtils.startEndMethodLog(false);
    }

    @Override
    public void onRegisterResultFail(int result) {
        LogUtils.e("Register fail with error :" + result);
//        showRegistrationProblem();
    }

    @Override
    public void onUnRegisterResult() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
