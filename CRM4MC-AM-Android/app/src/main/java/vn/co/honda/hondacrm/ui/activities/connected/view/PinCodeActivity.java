package vn.co.honda.hondacrm.ui.activities.connected.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothUtils;
import vn.co.honda.hondacrm.btu.Interfaces.listener.connect.IF_VehicleConnectListener;
import vn.co.honda.hondacrm.btu.Utils.KeyboardUtils;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;
import vn.co.honda.hondacrm.btu.Utils.StringUtils;
import vn.co.honda.hondacrm.ui.activities.connected.view.base.BaseFragment;
import vn.co.honda.hondacrm.utils.DialogUtils;

import static vn.co.honda.hondacrm.utils.Constants.TAB_DEALERS;
import static vn.co.honda.hondacrm.utils.Constants.TYPE_VIEWPAGE;
import static vn.co.honda.hondacrm.utils.Constants.UPDATE_CURRENT_VIEWPAGER;

public class PinCodeActivity extends BaseFragment implements IF_VehicleConnectListener {
    private TextView tvReportPinCode;
    private EditText edPinCode;
    private TextView btnSubmit;
    private char mCountInputPinWrong;
    private Activity activity;
    private FrameLayout lyTop;
    private TextView tvTitle;
    private ImageButton imBack;
    private LinearLayout llCheckPin;


    @Override
    protected int getIdLayout() {
        return R.layout.activity_pin_code;
    }

    @SuppressLint("SetTextI18n")
    protected void initView() {
        tvReportPinCode = findViewById(R.id.tv_report_pincode);
        edPinCode = findViewById(R.id.ed_pin_code);
        btnSubmit = findViewById(R.id.btn_submit);
        lyTop = findViewById(R.id.sign_up_header);
        tvTitle = lyTop.findViewById(R.id.tvTitle);
        imBack = lyTop.findViewById(R.id.btnBack);
        tvTitle.setText(getResources().getString(R.string.verification_pin));
        imBack.setOnClickListener(lsClick);
        llCheckPin = findViewById(R.id.lyCheckPin);
        llCheckPin.setOnClickListener(lsClick);
        btnSubmit.setEnabled(false);


        edPinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isEmpty(edPinCode)) {
                    btnSubmit.setEnabled(false);
                    btnSubmit.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_gray_background));
                } else {
                    btnSubmit.setEnabled(true);
                    btnSubmit.setBackground(ContextCompat.getDrawable(activity, R.drawable.bg_red));
                }

            }
        });

        btnSubmit.setOnClickListener(lsClick);
    }

    @Override
    protected void onBackPressedFragment() {
        if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    protected void setActionForView() {
        activity = getActivity();
        initView();
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

    private View.OnClickListener lsClick = v -> {
        switch (v.getId()) {
            case R.id.btn_submit:
                registerPin();
                break;
            case R.id.btnBack:
                if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack();
                }
                break;
            case R.id.lyCheckPin:
                DialogUtils.showDialogCheckPin(getActivity());
                break;
        }
    };

    private void registerPin() {
        KeyboardUtils.hideKeyboard(edPinCode);
        setNeedToShowDisconnectPopup(true);
        if (BluetoothUtils.isBluetoothDeviceConnected()) {
            showProgressDialog();
            BluetoothManager.getInstance().getBleRegisterController().registerPinCode(edPinCode.getText().toString());
        } else {
            showRegistrationProblem();
        }
    }

    @SuppressLint("SetTextI18n")
    public void handlePinWrong() {
        mCountInputPinWrong++;
//        hideProgressDialog();
        hideProgressDialog();
        tvReportPinCode.setTextColor(ContextCompat.getColor(activity, R.color.red));
        tvReportPinCode.setText(getResources().getString(R.string.pin_incorrect));
        if (mCountInputPinWrong == 3) {
            setNeedToShowDisconnectPopup(false);
            Dialog dialog = DialogUtils.showDialogDefaultNew(activity, R.layout.dialog_report, new DialogUtils.DialogListener() {
                @Override
                public void okButtonClick(Dialog dialog1) {
                    if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }

                    dialog1.dismiss();
                }

                @Override
                public void cancelButtonClick() {

                }
            });
            TextView tvHelp = dialog.findViewById(R.id.btn_cancel);
            tvHelp.setText(getResources().getString(R.string.help));
            tvHelp.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_background));
            TextView tvTitle = dialog.findViewById(R.id.txt_title_logout);
            tvTitle.setText("Connection is refused because\n" +
                    "you have entered wrong PIN for 3 times\n" +
                    " consecutively.\n" +
                    " ");

            tvHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    DialogUtils.showDialogHelp(activity, R.layout.dialog_help, () -> {
                        //intent to dealer
                        Intent intent = new Intent();
                        intent.setAction(UPDATE_CURRENT_VIEWPAGER);
                        intent.putExtra(TYPE_VIEWPAGE, TAB_DEALERS);
                        activity.sendBroadcast(intent);
                        activity.finish();
                    });
                }
            });

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BluetoothManager.getInstance().getBleConnectController().unRegisterListener(this);
    }
}
