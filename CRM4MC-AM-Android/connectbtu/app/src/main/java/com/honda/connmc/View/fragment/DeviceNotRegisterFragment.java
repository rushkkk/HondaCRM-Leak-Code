package com.honda.connmc.View.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import com.honda.connmc.R;
import com.honda.connmc.Utils.FragmentUtils;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.Utils.PermissionUtils;
import com.honda.connmc.View.base.BaseFragment;
import com.honda.connmc.View.dialog.DialogCommon;

public class DeviceNotRegisterFragment extends BaseFragment {

    public static final int REQUEST_FINE_LOCATION = 1;
    private Dialog mLocationRequireDialog;
    private LinearLayout mScanView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getIdLayout() {
        return R.layout.scan;
    }

    @Override
    protected void initView() {
        mScanView = findViewById(R.id.scan);
    }

    @Override
    protected void onBackPressedFragment() {
        getActivity().finish();
    }

    @Override
    protected void setActionForView() {
        mScanView.setOnClickListener(view -> onScanClick());
    }

    private void onScanClick() {
        if (PermissionUtils.hasPermissions(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isLocationEnabled(getActivity())) {
                    Fragment fragment = new DeviceListFragment();
                    FragmentUtils.addFragment(getActivity().getSupportFragmentManager(), fragment, R.id.fragment_container, true);
                } else {
                    showBluetoothRequestDialog();
                }
            } else {
                Fragment fragment = new DeviceListFragment();
                FragmentUtils.addFragment(getActivity().getSupportFragmentManager(), fragment, R.id.fragment_container, true);
            }
        } else {
            checkPermissionRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_FINE_LOCATION) {
            if (PermissionUtils.hasPermissions(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (isLocationEnabled(getActivity())) {
                        Fragment fragment = new DeviceListFragment();
                        FragmentUtils.addFragment(getActivity().getSupportFragmentManager(), fragment, R.id.fragment_container, true);
                    } else {
                        showBluetoothRequestDialog();
                    }
                } else {
                    Fragment fragment = new DeviceListFragment();
                    FragmentUtils.addFragment(getActivity().getSupportFragmentManager(), fragment, R.id.fragment_container, true);
                }
            }
        }
    }

    private void checkPermissionRequest() {
        if (!PermissionUtils.hasPermissions(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_CONTACTS}, REQUEST_FINE_LOCATION);
            }
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode;

        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

        } catch (Settings.SettingNotFoundException e) {
            LogUtils.e(e.toString());
            return false;
        }

        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    private void showBluetoothRequestDialog() {
        if (!isFragmentAlife()) {
            return;
        }
        getActivity().runOnUiThread(() -> {
            if (mLocationRequireDialog == null) {
                mLocationRequireDialog = DialogCommon.createPopup(getActivity(),
                        getString(R.string.text_button_dialog_ok),
                        getString(R.string.text_message_require_location), view -> {
                            Intent intent =
                                    new Intent(
                                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        });

            }
            if (!mLocationRequireDialog.isShowing()) {
                mLocationRequireDialog.show();
            }
        });
    }
}
