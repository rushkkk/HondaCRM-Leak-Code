package com.honda.connmc.View.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.R;
import com.honda.connmc.Utils.FragmentUtils;
import com.honda.connmc.View.base.BaseFragment;

public class UserRemovedFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getIdLayout() {
        return R.layout.user_removed_layout;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void onBackPressedFragment() {
    }

    @Override
    protected void setActionForView() {
        findViewById(R.id.btnLeftDialog).setOnClickListener(this);
        findViewById(R.id.btnRightDialog).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLeftDialog:
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            case R.id.btnRightDialog:
                BluetoothManager.getInstance().unRegisterDevice();
                goToDeviceListFragment(FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);
                break;
            default:
                break;
        }
    }
}
