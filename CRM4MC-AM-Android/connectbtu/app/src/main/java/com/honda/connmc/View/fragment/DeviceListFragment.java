package com.honda.connmc.View.fragment;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.scan.BleScanController;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Interfaces.listener.connect.IF_VehicleConnectListener;
import com.honda.connmc.Interfaces.listener.scan.IF_VehicleScanListener;
import com.honda.connmc.Model.BluetoothScan;
import com.honda.connmc.R;
import com.honda.connmc.Utils.FragmentUtils;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.View.adapter.DeviceListAdapter;
import com.honda.connmc.View.base.BaseFragment;
import com.honda.connmc.View.dialog.DialogCommon;

import java.util.ArrayList;
import java.util.List;

public class DeviceListFragment extends BaseFragment implements View.OnClickListener, IF_VehicleScanListener, DeviceListAdapter.OnItemClickListener, IF_VehicleConnectListener {
    private Dialog mDialogConnectFail;
    private RecyclerView mRcvDevices;
    private ImageView mImvWarning;
    private TextView mTxtTitleDeviceList;
    private TextView mTxtMessage;
    private ProgressBar mProgressbar;
    private RelativeLayout mRelativeDevicesList;

    private List<BluetoothScan> mBluetoothDevices;
    private DeviceListAdapter mAdapterDevices;

    @Override
    protected int getIdLayout() {
        return R.layout.list_bluetooth;
    }

    @Override
    protected void initView() {
        mRcvDevices = findViewById(R.id.rcvDevices);
        mImvWarning = findViewById(R.id.imv_warning);

        mTxtTitleDeviceList = findViewById(R.id.txt_title_devices_list);
        mRelativeDevicesList = findViewById(R.id.relativeListDevice);
        mTxtMessage = findViewById(R.id.txt_message);
        mProgressbar = findViewById(R.id.progressBar);
//        mProgressbar.setVisibility(View.VISIBLE);
        mBluetoothDevices = new ArrayList<>();

        mImvWarning.setVisibility(View.GONE);
        mTxtTitleDeviceList.setVisibility(View.GONE);
        mRelativeDevicesList.setVisibility(View.GONE);
        mTxtMessage.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDialogConnectFail != null && !mDialogConnectFail.isShowing()) {
            mDialogConnectFail.show();
        }
    }

    @Override
    protected void onBluetoothOFF() {
        super.onBluetoothOFF();
        hideProgressDialog();
        if (mDialogConnectFail != null && mDialogConnectFail.isShowing()) {
            mDialogConnectFail.dismiss();
        }
        stopScan();
        BluetoothManager.getInstance().resetData();
    }

    @Override
    protected void onBluetoothON() {
        super.onBluetoothON();
        startScan();
    }

    @Override
    protected boolean isNeedReceiveBluetoothState() {
        return true;
    }

    @Override
    protected void onBackPressedFragment() {
        Fragment fragment = new DeviceNotRegisterFragment();
        FragmentUtils.replaceFragment(getActivity().getSupportFragmentManager(), fragment, R.id.fragment_container, false, FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);
    }

    @Override
    protected void setActionForView() {
        BluetoothManager.getInstance().disconnectDevice();
        setNeedToShowDisconnectPopup(true);
        BluetoothManager.getInstance().resetData();
        findViewById(R.id.btnBack).setOnClickListener(this);
        setupRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        startScan();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopScan();
    }

    private void startScan() {
        if (mProgressbar != null) {
            mProgressbar.setVisibility(View.VISIBLE);
        }
        BluetoothManager.getInstance().setScanListener(this);
        BluetoothManager.getInstance().startScan(getActivity());
    }

    private void stopScan() {
        mProgressbar.setVisibility(View.GONE);
        BluetoothManager.getInstance().stopScan();
    }

    private void setupRecyclerView() {
        mRcvDevices.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRcvDevices.setLayoutManager(layoutManager);
        mAdapterDevices = new DeviceListAdapter(mBluetoothDevices);
        mAdapterDevices.setOnItemClick(this);
        mRcvDevices.setAdapter(mAdapterDevices);
    }

    @Override
    public void onClick(View view) {
        if (!isEnableClick()) {
            return;
        }
        if (view.getId() == R.id.btnBack) {
            stopScan();
            Fragment fragment = new DeviceNotRegisterFragment();
            FragmentUtils.clearAllFragment(getActivity().getSupportFragmentManager());
            FragmentUtils.replaceFragment(getActivity().getSupportFragmentManager(), fragment, R.id.fragment_container, false, FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);
        }
    }


    @Override
    public void onScanDeviceResult(BluetoothScan device) {
    }

    @Override
    public void onScanDeviceResult(List<BluetoothScan> devices) {
        if (!isFragmentAlife()) {
            return;
        }
        LogUtils.d(devices.size() + " SIZE");
        mBluetoothDevices.clear();
        mBluetoothDevices.addAll(devices);
        dataChangeAndUpdateUi();
    }

    private void dataChangeAndUpdateUi() {
        getActivity().runOnUiThread(() -> {
            mAdapterDevices.notifyDataSetChanged();
        });
        changeUI();
    }

    @Override
    public void onScanFail(int errorCode) {
        if (!isFragmentAlife()) {
            return;
        }
        LogUtils.startEndMethodLog(true);
        LogUtils.startEndMethodLog(false);
    }

    private void changeUI() {
        getActivity().runOnUiThread(() -> {
            if (!mBluetoothDevices.isEmpty()) {
                mImvWarning.setVisibility(View.GONE);
                mTxtTitleDeviceList.setText(getString(R.string.please_select_the_vehicle_to_connected));
                mTxtTitleDeviceList.setVisibility(View.VISIBLE);
                mRelativeDevicesList.setVisibility(View.VISIBLE);
                mTxtMessage.setVisibility(View.GONE);
            } else {
                mImvWarning.setVisibility(View.VISIBLE);
                mTxtTitleDeviceList.setText(getString(R.string.title_not_found_bluetooth));
                mTxtTitleDeviceList.setVisibility(View.VISIBLE);
                mRelativeDevicesList.setVisibility(View.GONE);
                mTxtMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean connectDevice(BluetoothScan device) {
        stopScan();
        BluetoothManager.getInstance().setConnectListener(this);
        return BluetoothManager.getInstance().startConnect(device.getDevice());
    }

    @Override
    public void onItemClick(BluetoothScan device) {
        if (!isEnableClick()) {
            return;
        }
        if (device.getUuidMode() == BleScanController.UuidMode.MODE_REGISTER) {
            if (connectDevice(device)) {
                showProgressDialog();
            }
        } else {
            showPopupConnectError(v -> {
                mDialogConnectFail = null;
            });
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
                showPopupConnectError(v -> {
                    BluetoothManager.getInstance().resetData();
                    startScan();
                    mDialogConnectFail = null;
                });
                break;
            default:
                break;
        }
        LogUtils.startEndMethodLog(false);
    }

    private void showPopupConnectError(View.OnClickListener onClickListener) {
        if (!BluetoothUtils.checkBluetooth()) {
            return;
        }
        getActivity().runOnUiThread(() -> {
            hideProgressDialog();
            if (mDialogConnectFail == null) {
                mDialogConnectFail = DialogCommon.createPopup(getActivity(), getString(R.string.text_button_dialog_ok),
                        getString(R.string.text_message_connect_fail_notice), onClickListener);
            }
            if (isResumed() && !mDialogConnectFail.isShowing()) {
                mDialogConnectFail.show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BluetoothManager.getInstance().getBleConnectController().unRegisterListener(this);
    }
}
