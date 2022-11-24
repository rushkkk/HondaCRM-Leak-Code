package com.honda.connmc.View.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Interfaces.listener.connect.IF_VehicleConnectListener;
import com.honda.connmc.R;
import com.honda.connmc.Utils.FragmentUtils;
import com.honda.connmc.Utils.KeyboardUtils;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.Utils.StringUtils;
import com.honda.connmc.View.base.BaseFragment;
import com.honda.connmc.View.dialog.DialogCommon;

public class EnterUsernameFragment extends BaseFragment implements View.OnClickListener, IF_VehicleConnectListener {

    private EditText mInput;
    private Button mBtnSubmit;
    private Dialog mDialogTooManyUser;

    @Override
    protected int getIdLayout() {
        return R.layout.input_view;
    }

    @Override
    protected void initView() {
        mInput = findViewById(R.id.edtInput);
        mBtnSubmit = findViewById(R.id.submit);
        mInput.requestFocus();
        findViewById(R.id.btnBack).setOnClickListener(this);
    }

    @Override
    protected void onBackPressedFragment() {
        goToDeviceListFragment(FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);
    }

    @Override
    protected void setActionForView() {
        BluetoothManager.getInstance().setConnectListener(this);
        mBtnSubmit.setOnClickListener(v -> {
            if (!isEnableClick()) {
                return;
            }
            handleUsername();
        });

        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBtnSubmit.setEnabled(!StringUtils.isEmpty(mInput));
            }
        });
    }

    private void handleUsername() {
        KeyboardUtils.hideKeyboard(mRootView);
        setNeedToShowDisconnectPopup(true);
        if (BluetoothUtils.isBluetoothDeviceConnected()) {
            showProgressDialog();
            String userName = mInput.getText().toString().trim();
            if (TextUtils.isEmpty(userName)) {
                return;
            }
            BluetoothManager.getInstance().getBleRegisterController().registerUserName(userName);
        } else {
            showRegistrationProblem();
        }
    }

    public void handlerTooManyUser(String userNameDelete) {
        setNeedToShowDisconnectPopup(false);
        mDialogTooManyUser = DialogCommon.createPopup(getActivity()
                , getString(R.string.text_button_yes)
                , getString(R.string.text_button_no)
                , getString(R.string.msg_too_many_user)
                , v -> {
                    mDialogTooManyUser.dismiss();
                    setNeedToShowDisconnectPopup(true);
                    if (BluetoothUtils.isBluetoothDeviceConnected()) {
                        BluetoothManager.getInstance().getBleRegisterController().replaceUser(userNameDelete);
                    } else {
                        showRegistrationProblem();
                    }
                }
                , v -> {
                    mDialogTooManyUser.dismiss();
                    goToDeviceNotRegisterFragment();
                });
        mDialogTooManyUser.show();
    }

    private void goToDeviceNotRegisterFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentUtils.clearAllFragment(fragmentManager);
            FragmentUtils.replaceFragment(fragmentManager, new DeviceNotRegisterFragment(), R.id.fragment_container, false, FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);
        }
    }

    @Override
    public void onClick(View v) {
        if (!isEnableClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.btnBack:
                goToDeviceListFragment(FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);
                break;
            default:
                break;
        }
    }

    @Override
    public void onConnectStateNotice(BluetoothManager.EnumDeviceConnectStatus connectState) {
        if (!isFragmentAlife()) {
            return;
        }
        LogUtils.startEndMethodLog(true);
        LogUtils.d(connectState.name());
        hideProgressDialog();
        switch (connectState) {
            case DISCONNECTED:
                showRegistrationProblem();
                break;
            case BTU_ACTIVE:
                getActivity().runOnUiThread(() -> {
                    Fragment fragment = new VehicleStatusFragment();
                    FragmentUtils.clearAllFragment(getFragmentManager());
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(VehicleStatusFragment.KEY_WAITING_BTU_DISCONNECT, true);
                    fragment.setArguments(bundle);
                    FragmentUtils.replaceFragment(getActivity().getSupportFragmentManager(), fragment, R.id.fragment_container, false, FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_GO);
                });
                break;
            default:
                break;
        }
        LogUtils.startEndMethodLog(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BluetoothManager.getInstance().getBleConnectController().unRegisterListener(this);
    }
}
