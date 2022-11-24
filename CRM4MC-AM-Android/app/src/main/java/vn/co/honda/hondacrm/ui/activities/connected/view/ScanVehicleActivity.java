package vn.co.honda.hondacrm.ui.activities.connected.view;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.Bluetooth.scan.BleScanController;
import vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothUtils;
import vn.co.honda.hondacrm.btu.Interfaces.listener.connect.IF_VehicleConnectListener;
import vn.co.honda.hondacrm.btu.Interfaces.listener.register.IF_VehicleRegisterListener;
import vn.co.honda.hondacrm.btu.Interfaces.listener.scan.IF_VehicleScanListener;
import vn.co.honda.hondacrm.btu.Model.BluetoothScan;
import vn.co.honda.hondacrm.btu.Utils.DialogCommon;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;
import vn.co.honda.hondacrm.btu.Utils.PermissionUtils;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.adapters.connected.DeviceListAdapter;
import vn.co.honda.hondacrm.utils.DialogUtils;

import static android.provider.Settings.Secure.getInt;

public class ScanVehicleActivity extends BaseActivity implements IF_VehicleRegisterListener, IF_VehicleScanListener, IF_VehicleConnectListener, DeviceListAdapter.OnItemClickListener {
    public static final int REQUEST_FINE_LOCATION = 1;
    private Dialog mLocationRequireDialog;
    private ArrayList<BluetoothScan> listBluetooth = new ArrayList<>();
    private DeviceListAdapter mAdapterDevices;
    private RecyclerView mRcvDevices;
    private LinearLayout llNull, llNotNull;
    private String username = "lala";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_vehicle);
        changeStatusBarColor(Color.TRANSPARENT);
        ButterKnife.bind(this);
        setTitleHeader(getString(R.string.scanning_vehicle));
        initView();
        setupRecyclerView();
        onScanClick();


    }


    private void initView() {
        mRcvDevices = findViewById(R.id.rcvDevices);
        llNull = findViewById(R.id.ll_null);
        llNotNull = findViewById(R.id.ll_not_null);
    }

    private void setupRecyclerView() {
        mRcvDevices.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRcvDevices.setLayoutManager(layoutManager);
        mAdapterDevices = new DeviceListAdapter(listBluetooth);
        mAdapterDevices.setOnItemClick(this);
        mRcvDevices.setAdapter(mAdapterDevices);
    }

    private void onScanClick() {
        if (PermissionUtils.hasPermissions(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isLocationEnabled(getApplicationContext())) {
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

    private DialogUtils.DialogListener dialogListener = new DialogUtils.DialogListener() {
        @Override
        public void okButtonClick(Dialog dialog) {
            Intent intent = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        @Override
        public void cancelButtonClick() {

        }
    };

    private void showBluetoothRequestDialog() {
//        if (!isFragmentAlife()) {
//            return;
//        }
        runOnUiThread(() -> {
            if (mLocationRequireDialog == null) {
//                mLocationRequireDialog = DialogCommon.createPopup(getApplicationContext(),
//                        getString(R.string.text_button_dialog_ok),
//                        getString(R.string.text_message_require_location), view -> {
//                            Intent intent =
//                                    new Intent(
//                                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(intent);
//                        });

                DialogUtils.showDialogDefault(getApplicationContext(), 300, 200, R.layout.diaglog_confirm_delete, dialogListener);

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
        if (!PermissionUtils.hasPermissions(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_CONTACTS}, REQUEST_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onScanDeviceResult(BluetoothScan device) {

    }

    @Override
    public void onScanDeviceResult(List<BluetoothScan> devices) {
        LogUtils.d(devices.size() + " SIZE");
        Log.e("hoaiii", devices.size() + " SIZE");
        listBluetooth.clear();
        listBluetooth.addAll(devices);
        dataChangeAndUpdateUi();
    }

    @Override
    public void onStart() {
        super.onStart();
        startScan();
    }

    private void startScan() {
//        if (mProgressbar != null) {
//            mProgressbar.setVisibility(View.VISIBLE);
//        }
        BluetoothManager.getInstance().setScanListener(this);
        BluetoothManager.getInstance().startScan(getApplicationContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        stopScan();
    }


    private void stopScan() {
//        mProgressbar.setVisibility(View.GONE);
        BluetoothManager.getInstance().stopScan();
    }

    @Override
    public void onScanFail(int errorCode) {
        LogUtils.startEndMethodLog(true);
        LogUtils.startEndMethodLog(false);
    }

    private void dataChangeAndUpdateUi() {
        runOnUiThread(() -> mAdapterDevices.notifyDataSetChanged());
        changeUI();
    }

    private void changeUI() {
        runOnUiThread(() -> {
            if (listBluetooth.size() > 0) {
                llNull.setVisibility(View.GONE);
                llNotNull.setVisibility(View.VISIBLE);
                setTitleHeader(getResources().getString(R.string.vehicle_scan_list));
            } else {
                llNull.setVisibility(View.VISIBLE);
                llNotNull.setVisibility(View.GONE);
                setTitleHeader(getResources().getString(R.string.scanning_vehicle));
            }
        });

    }

    private Dialog mDialogConnectFail;

    private boolean connectDevice(BluetoothScan device) {
        stopScan();
        BluetoothManager.getInstance().setConnectListener(this);
        BluetoothManager.getInstance().setRegisterListener(this);
        return BluetoothManager.getInstance().startConnect(device.getDevice());
    }

    ProgressDialog progressDialog;

    @Override
    public void onItemClick(BluetoothScan device) {
        if (device.getUuidMode() == BleScanController.UuidMode.MODE_REGISTER) {
            if (connectDevice(device)) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setCancelable(false);
                progressDialog.show();

            }
        } else {
            showPopupConnectError(v -> mDialogConnectFail = null);
        }
    }

    private void showPopupConnectError(View.OnClickListener onClickListener) {
        if (!BluetoothUtils.checkBluetooth()) {
            return;
        }
        runOnUiThread(() -> {
            progressDialog.dismiss();

            if (mDialogConnectFail == null) {
                mDialogConnectFail = DialogCommon.createPopup(this, getString(R.string.text_button_dialog_ok),
                        getString(R.string.text_message_connect_fail_notice), onClickListener);
            }
            if (!mDialogConnectFail.isShowing()) {
                mDialogConnectFail.show();
            }
            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onConnectStateNotice(BluetoothManager.EnumDeviceConnectStatus connectState) {
        LogUtils.startEndMethodLog(true);
        LogUtils.d(connectState.name());
        Log.e("hoaiii", "connectState = " + connectState);
        progressDialog.dismiss();
        switch (connectState) {
            case DISCONNECTED:
                showPopupConnectError(v -> {
                    BluetoothManager.getInstance().resetData();
                    startScan();
                });
                break;
            case BTU_ACTIVE:
                Log.e("hoaiii", "btu_active");
                runOnUiThread(() -> startActivity(new Intent(this, FuelStatusActivity.class)));
                break;
            default:
                break;
        }
        LogUtils.startEndMethodLog(false);
    }


    @Override
    public void onRequestPinCode() {
        progressDialog.dismiss();
        Log.e("hoaiii", "pincode");
        startActivity(new Intent(this, PinCodeActivity.class));
    }

    @Override
    public void onVerifyPinCodeFail() {
        LogUtils.startEndMethodLog(true);
        runOnUiThread(() -> {
            PinCodeActivity pinCodeActivity = new PinCodeActivity();
            pinCodeActivity.handlePinWrong();
        });
        LogUtils.startEndMethodLog(false);

    }

    @Override
    public void onRequestUserName() {
        LogUtils.startEndMethodLog(true);
        runOnUiThread(() -> {
            Log.e("hoaii", "requestname");
            PinCodeActivity pinCodeActivity = new PinCodeActivity();
            pinCodeActivity.hideProgressDialog();
            BluetoothManager.getInstance().getBleRegisterController().registerUserName(username);
        });
        LogUtils.startEndMethodLog(false);
    }

    @Override
    public void onResultTooManyUser(String userNameDelete) {
        LogUtils.startEndMethodLog(true);
        LogUtils.d("User Name : " + userNameDelete);
        runOnUiThread(() -> handlerTooManyUser(userNameDelete));

        LogUtils.startEndMethodLog(false);
    }

    private Dialog dialogManyUser;

    public void handlerTooManyUser(String userNameDelete) {

        dialogManyUser = DialogUtils.showDialogDefault(this, R.layout.diaglog_confirm_delete, 300, 200, new DialogUtils.DialogListener() {
            @Override
            public void okButtonClick(Dialog dialog) {
                if (BluetoothUtils.isBluetoothDeviceConnected()) {
                    BluetoothManager.getInstance().getBleRegisterController().replaceUser(userNameDelete);
                } else {
//                    showRegistrationProblem();
                }
            }

            @Override
            public void cancelButtonClick() {

            }
        });
        Button btnCancle = dialogManyUser.findViewById(R.id.btn_cancel);
        btnCancle.setText("No");
        Button btnok = dialogManyUser.findViewById(R.id.btn_ok);
        btnok.setText("YES");
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(
                "Too many smartphones were registered.\n" +
                        "Inactive smartphone for longest time \n" +
                        "( username:" + username + "will be deregistered and\n" +
                        " will need to register again.\n" +
                        " ");

    }


    @Override
    public void onRequestEditUserName(String userName) {
        LogUtils.startEndMethodLog(true);
        runOnUiThread(() -> DialogCommon.createPopup(ScanVehicleActivity.this, "OK", "User name duplicate, Please edit user name!", v -> {

        }).show());

        LogUtils.startEndMethodLog(false);
    }

    @Override
    public void onRegisterResultFail(int result) {

    }

    @Override
    public void onUnRegisterResult() {

    }
}
