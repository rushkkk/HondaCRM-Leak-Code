package vn.co.honda.hondacrm.ui.activities.connected.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.Interfaces.listener.register.IF_VehicleRegisterListener;
import vn.co.honda.hondacrm.btu.Utils.DialogCommon;
import vn.co.honda.hondacrm.btu.Utils.FragmentUtils;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;
import vn.co.honda.hondacrm.ui.activities.connected.view.base.BaseActivity;
import vn.co.honda.hondacrm.ui.fragments.connected.views.EnterUsernameFragment;
import vn.co.honda.hondacrm.ui.fragments.connected.views.ScanVehicleFragment;

import static vn.co.honda.hondacrm.utils.Constants.MY_LOG_TAG;

public class ConnectedMainActivity extends BaseActivity implements IF_VehicleRegisterListener {

    @Override
    protected int getIdLayout() {
        return R.layout.activity_connected_main;
    }

    @Override
    protected void initView() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void setActionForView() {
        BluetoothManager.getInstance().setRegisterListener(this);
        gotoList();
    }

    private void gotoList() {
        Log.d(MY_LOG_TAG, " gotoList ");
        FragmentUtils.addFragment(getSupportFragmentManager(), new ScanVehicleFragment(), R.id.fragment_container, true);
    }

    @Override
    protected boolean isNeedCheckBlueToothWhenAppResume() {
        return true;
    }

    @Override
    public void onRequestPinCode() {
        LogUtils.startEndMethodLog(true);
        runOnUiThread(() -> {
            Fragment fragment = new PinCodeActivity();
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
            if (f instanceof PinCodeActivity) {
                PinCodeActivity enterPinFragment = (PinCodeActivity) f;
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
//                enterUsernameFragment.handlerTooManyUser(userNameDelete);
            }
        });

        LogUtils.startEndMethodLog(false);
    }

    @Override
    public void onRequestEditUserName(String userName) {
        LogUtils.startEndMethodLog(true);
        runOnUiThread(() -> {
            hideProgressDialog();
            DialogCommon.createPopup(ConnectedMainActivity.this, "User name duplicate, Please edit user name!", "OK", v -> {

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
    }
}
