package com.honda.connmc.View.fragment;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Interfaces.listener.connect.IF_VehicleConnectListener;
import com.honda.connmc.R;
import com.honda.connmc.Utils.KeyboardUtils;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.Utils.StringUtils;
import com.honda.connmc.View.base.BaseFragment;
import com.honda.connmc.View.dialog.DialogCommon;

public class EnterPinFragment extends BaseFragment implements View.OnClickListener, IF_VehicleConnectListener {

    private TextView mTitle;
    private EditText mInput;
    private Button mBtnSubmit;
    private char mCountInputPinWrong;

    @Override
    protected int getIdLayout() {
        return R.layout.input_view;
    }

    @Override
    protected void initView() {
        mTitle = findViewById(R.id.title);
        mInput = findViewById(R.id.edtInput);
        mBtnSubmit = findViewById(R.id.submit);
        findViewById(R.id.btnBack).setOnClickListener(this);
    }

    @Override
    protected void onBackPressedFragment() {
        if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    protected void setActionForView() {
        BluetoothManager.getInstance().setConnectListener(this);
        mBtnSubmit.setOnClickListener(v -> {
             registerPin();
        });
        mTitle.setText(R.string.enter_pin);
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

    private void registerPin() {
        if (!isEnableClick()) {
            return;
        }
        KeyboardUtils.hideKeyboard(mInput);
        setNeedToShowDisconnectPopup(true);
        if (BluetoothUtils.isBluetoothDeviceConnected()) {
            showProgressDialog();
            BluetoothManager.getInstance().getBleRegisterController().registerPinCode(mInput.getText().toString());
        } else {
            showRegistrationProblem();
        }
    }
    public void handlePinWrong() {
        mCountInputPinWrong++;
        hideProgressDialog();
        mTitle.setTextColor(getResources().getColor(R.color.darkRed));
        mTitle.setText(R.string.pin_wrong);
        if (mCountInputPinWrong == 3) {
            setNeedToShowDisconnectPopup(false);
            showDialogPinWrong();
        }
    }

    private void showDialogPinWrong() {
        Dialog dialogPinWrong = DialogCommon.createPopup(getActivity(), getString(R.string.text_button_dialog_ok), getString(R.string.pin_wrong_3_time), view ->
                onBackPressedFragment());
        dialogPinWrong.show();
    }

    @Override
    public void onClick(View v) {
        if (!isEnableClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressedFragment();
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

        switch (connectState) {
            case DISCONNECTED:
                showRegistrationProblem();
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
