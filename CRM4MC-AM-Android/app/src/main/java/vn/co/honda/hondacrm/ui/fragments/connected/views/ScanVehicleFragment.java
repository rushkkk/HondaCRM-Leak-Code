package vn.co.honda.hondacrm.ui.fragments.connected.views;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.Bluetooth.scan.BleScanController;
import vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothUtils;
import vn.co.honda.hondacrm.btu.Interfaces.listener.connect.IF_VehicleConnectListener;
import vn.co.honda.hondacrm.btu.Interfaces.listener.scan.IF_VehicleScanListener;
import vn.co.honda.hondacrm.btu.Model.BluetoothScan;
import vn.co.honda.hondacrm.btu.Service.ConnMCService;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;
import vn.co.honda.hondacrm.btu.Utils.PermissionUtils;
import vn.co.honda.hondacrm.btu.Utils.PrefUtils;
import vn.co.honda.hondacrm.ui.activities.connected.view.base.BaseFragment;
import vn.co.honda.hondacrm.ui.adapters.connected.DeviceListAdapter;
import vn.co.honda.hondacrm.utils.DialogUtils;

import static android.provider.Settings.Secure.getInt;

public class ScanVehicleFragment extends BaseFragment implements IF_VehicleConnectListener, DeviceListAdapter.OnItemClickListener, IF_VehicleScanListener {
    public static final int REQUEST_FINE_LOCATION = 1;
    private Dialog mLocationRequireDialog;
    private ArrayList<BluetoothScan> listBluetooth = new ArrayList<>();
    private DeviceListAdapter mAdapterDevices;
    private RecyclerView mRcvDevices;
    private LinearLayout llNull, llNotNull, lyNobluetooth;
    private TextView tvTitle;
    private ImageButton imBack;
    private FrameLayout signUpHeader;

    public ScanVehicleFragment() {
        // Required empty public constructor
    }

    private void onScanClick() {
        if (PermissionUtils.hasPermissions(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isLocationEnabled(getActivity())) {
                    //add fragment
                    BluetoothManager.getInstance().disconnectDevice();
                    BluetoothManager.getInstance().resetData();
                    setupRecyclerView();
                } else {
                    showBluetoothRequestDialog();
                }
            } else {
                //add fragment
                BluetoothManager.getInstance().disconnectDevice();
                BluetoothManager.getInstance().resetData();
                setupRecyclerView();
            }
        } else {
            checkPermissionRequest();
        }
    }

    private void showBluetoothRequestDialog() {
        if (!isFragmentAlife()) {
            return;
        }
        getActivity().runOnUiThread(() -> {
            if (mLocationRequireDialog == null) {
                mLocationRequireDialog = DialogUtils.showDialogDefaultNew(getActivity(), R.layout.dialog_report, new DialogUtils.DialogListener() {
                    @Override
                    public void okButtonClick(Dialog dialog) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }

                    @Override
                    public void cancelButtonClick() {

                    }
                });

                TextView tvContent = mLocationRequireDialog.findViewById(R.id.txt_title_logout);
                tvContent.setText(getResources().getString(R.string.text_message_require_location));
                TextView tvCancel = mLocationRequireDialog.findViewById(R.id.btn_cancel);
                tvCancel.setVisibility(View.GONE);

            }
            if (!mLocationRequireDialog.isShowing()) {
                mLocationRequireDialog.show();
            }
        });
    }


    public static boolean isLocationEnabled(Context context) {
        int locationMode;

        try {
            locationMode = getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

        } catch (Settings.SettingNotFoundException e) {
            LogUtils.e(e.toString());
            return false;
        }

        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    private void checkPermissionRequest() {
        if (!PermissionUtils.hasPermissions(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_CALL_LOG
                        , Manifest.permission.READ_CONTACTS}, REQUEST_FINE_LOCATION);
            }
        }
    }

    private void setupRecyclerView() {
        mRcvDevices.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRcvDevices.setLayoutManager(layoutManager);
        mAdapterDevices = new DeviceListAdapter(listBluetooth);
        mAdapterDevices.setOnItemClick(this::onItemClick);
        mRcvDevices.setAdapter(mAdapterDevices);
    }


    @Override
    protected int getIdLayout() {
        return R.layout.activity_scan_vehicle;
    }

    @Override
    protected void initView() {
        mRcvDevices = findViewById(R.id.rcvDevices);
        llNull = findViewById(R.id.ll_null);
        llNotNull = findViewById(R.id.ll_not_null);
        signUpHeader = findViewById(R.id.sign_up_header);
        tvTitle = signUpHeader.findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.scanning_vehicle));
        imBack = signUpHeader.findViewById(R.id.btnBack);
        lyNobluetooth = findViewById(R.id.lyNobluetooth);
        imBack.setOnClickListener(v -> getActivity().finish());

    }

    @Override
    protected void onBackPressedFragment() {

    }

    @Override
    protected void setActionForView() {
        setupRecyclerView();
        onScanClick();

    }


    private boolean connectDevice(BluetoothScan device) {
        stopScan();
        BluetoothManager.getInstance().setConnectListener(this);
        PrefUtils.setString("NAMEBTU", device.getDevice().getName());
        return BluetoothManager.getInstance().startConnect(device.getDevice());
    }

    private void stopScan() {
        BluetoothManager.getInstance().stopScan();
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

    private void disconnectFromCurrentVehicle() {
        getActivity().stopService(new Intent(getActivity(), ConnMCService.class));
        BluetoothManager.getInstance().disconnectDevice();
    }

    private void dataChangeAndUpdateUi() {
        getActivity().runOnUiThread(() -> mAdapterDevices.notifyDataSetChanged());
        changeUI();
    }

    @Override
    public void onScanDeviceResult(BluetoothScan device) {

    }

    @Override
    public void onScanDeviceResult(List<BluetoothScan> devices) {
        if (!isFragmentAlife()) {
            return;
        }
        listBluetooth.clear();
        listBluetooth.addAll(devices);

        dataChangeAndUpdateUi();
    }

    private void changeUI() {
        getActivity().runOnUiThread(() -> {
            if (listBluetooth.size() > 0) {
                llNull.setVisibility(View.GONE);
                llNotNull.setVisibility(View.VISIBLE);
                tvTitle.setText(getResources().getString(R.string.vehicle_scan_list));
            } else {
                llNull.setVisibility(View.VISIBLE);
                llNotNull.setVisibility(View.GONE);
                tvTitle.setText(getResources().getString(R.string.scanning_vehicle));
                lyNobluetooth.setVisibility(View.VISIBLE);
                new CountDownTimer(5000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        lyNobluetooth.setVisibility(View.INVISIBLE);
                        if (listBluetooth.size() < 0) {
                            this.start();
                        } else {
                            this.cancel();
                        }
                    }
                }.start();
            }
        });
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

    @Override
    public void onStart() {
        super.onStart();
        startScan();
    }

    private Dialog mDialogConnectFail;

    @SuppressLint("SetTextI18n")
    private void showPopupConnectError(View.OnClickListener onClickListener) {
        if (!BluetoothUtils.checkBluetooth()) {
            return;
        }
        getActivity().runOnUiThread(() -> {
            hideProgressDialog();
            if (mDialogConnectFail == null) {
                mDialogConnectFail = DialogUtils.showDialogDefaultNew(getActivity(), R.layout.dialog_report, new DialogUtils.DialogListener() {
                    @Override
                    public void okButtonClick(Dialog dialog) {
                        mDialogConnectFail.dismiss();
                    }

                    @Override
                    public void cancelButtonClick() {

                    }
                });

                TextView tvContent = mDialogConnectFail.findViewById(R.id.txt_title_logout);
                tvContent.setText(getResources().getString(R.string.connection_problem_));
                TextView tvCancel = mDialogConnectFail.findViewById(R.id.btn_cancel);
                tvCancel.setVisibility(View.GONE);

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

    private void startScan() {
        BluetoothManager.getInstance().setScanListener(this);
        BluetoothManager.getInstance().startScan(getActivity());
    }

    @Override
    public void onScanFail(int errorCode) {
        if (!isFragmentAlife()) {
            return;
        }
        LogUtils.startEndMethodLog(true);
        LogUtils.startEndMethodLog(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        stopScan();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDialogConnectFail != null && !mDialogConnectFail.isShowing()) {
            mDialogConnectFail.show();
        }
    }

    @Override
    public void onBluetoothOFF() {
        super.onBluetoothOFF();
        hideProgressDialog();
        if (mDialogConnectFail != null && mDialogConnectFail.isShowing()) {
            mDialogConnectFail.dismiss();
        }
        stopScan();
        BluetoothManager.getInstance().resetData();
    }

    @Override
    public void onBluetoothON() {
        super.onBluetoothON();
        startScan();
    }

    @Override
    public boolean isNeedReceiveBluetoothState() {
        return true;
    }

}


