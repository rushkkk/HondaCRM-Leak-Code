package vn.co.honda.hondacrm.ui.fragments.connected.views;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.VehicleInfoMsgResolver;
import vn.co.honda.hondacrm.btu.Bluetooth.timer.IF_TimeScheduleListener;
import vn.co.honda.hondacrm.btu.Bluetooth.timer.TimeManager;
import vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothPrefUtils;
import vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothUtils;
import vn.co.honda.hondacrm.btu.Interfaces.listener.connect.IF_VehicleConnectListener;
import vn.co.honda.hondacrm.btu.Model.BluetoothStatus;
import vn.co.honda.hondacrm.btu.Service.ConnMCService;
import vn.co.honda.hondacrm.btu.Utils.DialogCommon;
import vn.co.honda.hondacrm.btu.Utils.FragmentUtils;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;
import vn.co.honda.hondacrm.btu.Utils.PermissionUtils;
import vn.co.honda.hondacrm.ui.activities.connected.view.base.BaseFragment;
import vn.co.honda.hondacrm.ui.adapters.connected.StatusAdapter;

import static vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_BTU_NAME_REGISTER_SUCCESS;


public class VehicleStatusFragment extends BaseFragment implements IF_VehicleConnectListener, VehicleInfoMsgResolver.VehicleInfoListener {

    public static final String KEY_WAITING_BTU_DISCONNECT = "KEY_WAITING_BTU_DISCONNECT";
    private final int PERMISSION_REQUEST_CODE = 1;
    private final int TIME_OUT_RECONNECT = 30000;
    private LinearLayout mLinearUnregister;
    private RecyclerView mRcvStatus;
    private StatusAdapter mAdapter;
    private Button mBtnButtonBlue;
    private LinearLayout mlinearBluetoothStatus;
    private SwitchCompat mSwitchRawData;
    private ConstraintLayout mlayoutListHeader;
    private ImageView mBtnRawScreen;
    private boolean isWaitingBtuDisconnect;
    private Dialog mDialogInactive;
    private Dialog mDialogTimeOutReconnect;
    private TimeManager mTimeManager;

    @Override
    protected int getIdLayout() {
        return R.layout.vehicle_status;
    }

    @Override
    protected void initView() {
        mLinearUnregister = findViewById(R.id.linearUnregister);
        mBtnButtonBlue = findViewById(R.id.btnBluetoothStatus);
        mlinearBluetoothStatus = findViewById(R.id.linearBluetoothStatus);
        mRcvStatus = findViewById(R.id.rcvStatus);
        mSwitchRawData = findViewById(R.id.switchRawData);
        mlayoutListHeader = findViewById(R.id.layoutListHeader);
        mBtnRawScreen = findViewById(R.id.btnRawScreen);

        initListBluetoothStatus();
    }

    @Override
    protected void onBackPressedFragment() {
        getActivity().finish();
    }

    @Override
    protected void setActionForView() {
        Intent intent = new Intent(getActivity(), ConnMCService.class);
        getActivity().startService(intent);
        Bundle bundle = getArguments();
        setDataListKey();
        if (bundle != null && bundle.containsKey(KEY_WAITING_BTU_DISCONNECT)) {
            isWaitingBtuDisconnect = bundle.getBoolean(KEY_WAITING_BTU_DISCONNECT);
        }
//        updateStateBluetooth(true);
        showProgressDialog();
        BluetoothManager.getInstance().setConnectListener(this);
        VehicleInfoMsgResolver.getInstance().registerListener(this);
        checkPermissionReadContact();
        mLinearUnregister.setOnClickListener(v -> {
            if (!isEnableClick()) {
                return;
            }
            unregisterFromCurrentVehicle();
            handleUnRegister();
        });

        mSwitchRawData.setOnClickListener(v -> {
            if (mSwitchRawData.isChecked()) {
                mlayoutListHeader.setVisibility(View.VISIBLE);
                mAdapter.setDisplayRaw(true);
            } else {
                mlayoutListHeader.setVisibility(View.INVISIBLE);
                mAdapter.setDisplayRaw(false);
            }
        });

        mBtnRawScreen.setOnClickListener(v -> {
            getActivity().runOnUiThread(() -> {
                RawDataFragment fragment = new RawDataFragment();
                FragmentUtils.addFragment(getFragmentManager(), fragment, R.id.fragment_container, true);
            });

        });
        mTimeManager = new TimeManager();
        processConnectWithBTU();
    }

    //
    @Override
    public void onResume() {
        super.onResume();
        if (!BluetoothUtils.isBluetoothDeviceConnected()
                || BluetoothManager.getInstance().getBleConnectController().isConnecting()
                || BluetoothManager.getInstance().getBleRegisterController().isRegisterInProgress()) {
//            updateStateBluetooth(true);
        }
        if (!isWaitingBtuDisconnect) {
            processCheckNotificationAccess();
        } else {
            isWaitingBtuDisconnect = false;
            dismissNotificationAccessPopup();
        }
        if (mDialogTimeOutReconnect != null && !mDialogTimeOutReconnect.isShowing()) {
            mDialogTimeOutReconnect.show();
        }
        if (mDialogInactive != null && !mDialogInactive.isShowing()) {
            mDialogInactive.show();
        }
    }

    private void initListBluetoothStatus() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRcvStatus.setLayoutManager(layoutManager);
        mAdapter = new StatusAdapter(false);
        mRcvStatus.setAdapter(mAdapter);
    }

    //
    private void handleUnRegister() {
        getActivity().runOnUiThread(() -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if (fragmentManager != null) {
                FragmentUtils.clearAllFragment(getFragmentManager());
                FragmentUtils.replaceFragment(fragmentManager, new ScanVehicleFragment(), R.id.fragment_container, false, FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);
            }
        });

    }

    //
    private void unregisterFromCurrentVehicle() {
        getActivity().stopService(new Intent(getActivity(), ConnMCService.class));
        BluetoothManager.getInstance().unRegisterDevice();
    }

//    private void updateStateBluetooth(boolean isOff) {
//        getActivity().runOnUiThread(() -> {
//            mBtnButtonBlue.setText(isOff ? R.string.disconnected : R.string.connected);
//            int drawableDisconnected = getResId(R.attr.bgLoginEditText);
//            int drawableConnected = getResId(R.attr.redBtnBg);
//            mlinearBluetoothStatus.setBackground(getResources().getDrawable(isOff ? drawableDisconnected : drawableConnected));
//        });
//    }

    //
    private void checkPermissionReadContact() {
        if (!PermissionUtils.hasPermissions(getActivity(), Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //
    @Override
    public boolean isNeedReceiveBluetoothState() {
        return true;
    }

    //
    @Override
    protected boolean isNeedCheckNotificationAccess() {
        return true;
    }

    //
    @Override
    public void onBluetoothON() {
        super.onBluetoothON();
    }

    //
    private void processConnectWithBTU() {
        BluetoothDevice bluetoothDevice = BluetoothManager.getInstance().getBleConnectController().getBluetoothDeviceConnect();
        BluetoothManager.EnumDeviceConnectStatus status = BluetoothManager.getInstance().getDeviceCurrentStatus();
        if (status != BluetoothManager.EnumDeviceConnectStatus.INITIAL && status != BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE) {
            hideProgressDialog();
            onConnectStateNotice(status);
            return;
        }
        if (bluetoothDevice != null) {
            if (BluetoothUtils.isBluetoothDeviceConnected()) {
                if (!isWaitingBtuDisconnect && status == BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE) {
                    hideProgressDialog();
//                    updateStateBluetooth(false);
                    BluetoothManager.getInstance().getBleRegisterController().inquire(false);
                }
            } else {
                if (status == BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE || status == BluetoothManager.EnumDeviceConnectStatus.INITIAL) {
                    reConnect();
                }
            }
        } else {
            if (status == BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE || status == BluetoothManager.EnumDeviceConnectStatus.INITIAL) {
                reConnect();
            }
        }
    }

    //
    private void reConnect() {
        LogUtils.startEndMethodLog(true);
        String btuName = BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS);
        if (TextUtils.isEmpty(btuName)) {
            LogUtils.e("BTU name is empty!");
            return;
        }
        LogUtils.d("Reconnect with BTU : " + btuName);
        BluetoothManager.getInstance().reConnect(getActivity());
        if (mProgressDialog != null) {
            mTimeManager.startTimer((IF_TimeScheduleListener) () -> {
                BluetoothManager.getInstance().getBleConnectController().stopReconnect();
                showPopupTimeOutReconnect();
            }, TIME_OUT_RECONNECT);
        }
        LogUtils.startEndMethodLog(false);

    }

    //
    @Override
    public void onBluetoothOFF() {
        super.onBluetoothOFF();
//        updateStateBluetooth(true);
        dismissNotificationAccessPopup();
    }

    //
    @Override
    public void onConnectStateNotice(BluetoothManager.EnumDeviceConnectStatus connectState) {
        if (!isFragmentAlife()) {
            return;
        }
        mTimeManager.stopTimeSchedule();
        LogUtils.startEndMethodLog(true);
        LogUtils.d(connectState.name());

        switch (connectState) {
            case BTU_ACTIVE:
                hideProgressDialog();
//                updateStateBluetooth(false);
                getActivity().runOnUiThread(() -> {
                    if (mDialogInactive != null) {
                        mDialogInactive.dismiss();
                        mDialogInactive = null;
                    }
                    if (mDialogTimeOutReconnect != null) {
                        mDialogTimeOutReconnect.dismiss();
                        mDialogTimeOutReconnect = null;
                    }
                    processCheckNotificationAccess();
                });
                break;
            case BTU_INACTIVE:
//                updateStateBluetooth(true);
                hideProgressDialog();
                getActivity().runOnUiThread(() -> {
                    if (mDialogTimeOutReconnect != null) {
                        mDialogTimeOutReconnect.dismiss();
                        mDialogTimeOutReconnect = null;
                    }
                    if (mDialogInactive == null || !mDialogInactive.isShowing()) {
                        mDialogInactive = DialogCommon.createPopup(getActivity()
                                , getString(R.string.text_button_dialog_cancel)
                                , getString(R.string.text_button_dialog_ok)
                                , getString(R.string.msg_inactive_user)
                                , v -> {
                                    if (mDialogInactive != null) {
                                        mDialogInactive.dismiss();
                                        mDialogInactive = null;
                                    }
                                    showProgressDialog();
                                    BluetoothManager.getInstance().setDeviceCurrentStatus(BluetoothManager.EnumDeviceConnectStatus.BTU_INACTIVE_SECOND_TIME);
                                    reConnect();
                                }
                                , v -> {
                                    if (mDialogInactive != null) {
                                        mDialogInactive.dismiss();
                                        mDialogInactive = null;
                                    }
                                });
                        if (isResumed() && !mDialogInactive.isShowing()) {
                            mDialogInactive.show();
                        }
                    }
                });
                break;
            case DISCONNECTED:
//                updateStateBluetooth(true);
                break;
            case BTU_UNREGISTERED:
                hideProgressDialog();
                gotoUserRemovedFragment();
                break;
            default:
                break;
        }
        LogUtils.startEndMethodLog(false);
    }

    //
    private void gotoUserRemovedFragment() {
        getActivity().runOnUiThread(() -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if (fragmentManager != null) {
                FragmentUtils.clearAllFragment(fragmentManager);
//                FragmentUtils.replaceFragment(fragmentManager, new UserRemovedFragment(), R.id.fragment_container, false, FragmentUtils.FragmentAnimationType.TYPE_NO_ANIMATION);
            }
        });
    }

    //
    @Override
    public void onResolvedVehicleInfo(LinkedHashMap<String, BluetoothStatus> statuses) {
        if (!isFragmentAlife()) {
            return;
        }
        List<BluetoothStatus> list = new ArrayList<>();
        if (statuses == null) {
            LogUtils.e("list phone gw data NULL!");
        } else {
            LogUtils.d("list phone gw data size = " + statuses.size());
        }
//        filterList();
        getActivity().runOnUiThread(() -> mAdapter.updateListStatus(statuses));
    }

    private ArrayList<String> listKey = new ArrayList<>();

    private void setDataListKey() {
        listKey.add("C_ENGTEMP ");
        listKey.add("C_ODO  ");
        listKey.add("ENG_BTU_VB_11 ");
        listKey.add("ENG_BTU_ECT_11 ");
        listKey.add("C_INTAKE_AIR_TEMP  ");
        listKey.add("C_ASP  ");
        listKey.add("ENG_BTU_THDEG_11 ");
        listKey.add("ENG_BTU_VO2_20  ");
    }

    //
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        VehicleInfoMsgResolver.getInstance().unRegisterListener(this);
        BluetoothManager.getInstance().getBleConnectController().unRegisterListener(this);
    }

    //
    private void showPopupTimeOutReconnect() {
        if (isFragmentAlife()) {
            hideProgressDialog();
            getActivity().runOnUiThread(() -> {
                if (mDialogTimeOutReconnect == null || !mDialogTimeOutReconnect.isShowing()) {
                    mDialogTimeOutReconnect = DialogCommon.createPopup(getActivity()
                            , getString(R.string.text_button_dialog_cancel)
                            , getString(R.string.text_button_reconnect)
                            , getString(R.string.text_message_timeout_reconnect)
                            , v -> {
                                mDialogTimeOutReconnect.dismiss();
                                mDialogTimeOutReconnect = null;
                            }
                            , v -> {
                                mDialogTimeOutReconnect.dismiss();
                                mDialogTimeOutReconnect = null;
                                showProgressDialog();
                                reConnect();
                            });
                    if (isResumed() && !mDialogTimeOutReconnect.isShowing()) {
                        mDialogTimeOutReconnect.show();
                    }
                }
            });
        }
    }
}
