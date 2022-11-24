package vn.co.honda.hondacrm.ui.activities.connected.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.Bluetooth.timer.IF_TimeScheduleListener;
import vn.co.honda.hondacrm.btu.Bluetooth.timer.TimeManager;
import vn.co.honda.hondacrm.btu.Interfaces.listener.connect.IF_VehicleConnectListener;
import vn.co.honda.hondacrm.btu.Utils.DialogCommon;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleResponse;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.connected.model.Vehicle;
import vn.co.honda.hondacrm.ui.adapters.connected.RcvListConnectedVehicle;
import vn.co.honda.hondacrm.ui.fragments.connected.local.model.VehicleEntity;
import vn.co.honda.hondacrm.ui.fragments.connected.local.roomDatabase.VehicleRoomDb;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;
import vn.co.honda.hondacrm.utils.StateConnectPre;

import static vn.co.honda.hondacrm.utils.Constants.ACTION_SET_CURRENT_DETAIL_VEHICLE;
import static vn.co.honda.hondacrm.utils.Constants.BTU_ACTIVE;
import static vn.co.honda.hondacrm.utils.Constants.BTU_INACTIVE;
import static vn.co.honda.hondacrm.utils.Constants.BTU_UNREGISTERED;
import static vn.co.honda.hondacrm.utils.Constants.POSITION_VEHICLE_DETAIL;
import static vn.co.honda.hondacrm.utils.Constants.TAB_DEALERS;
import static vn.co.honda.hondacrm.utils.Constants.TYPE_VIEWPAGE;
import static vn.co.honda.hondacrm.utils.Constants.UPDATE_CURRENT_VIEWPAGER;

public class ListConnectedVehicleActivity extends BaseActivity implements IF_VehicleConnectListener {

    private RecyclerView rcvList;
    private RecyclerView.LayoutManager mViewManager;
    private RcvListConnectedVehicle mAdapter;
    private ApiService apiService;
    private String mTypeFullAccessToken;
    private ArrayList<Vehicle> alVehicle = new ArrayList<>();
    private Dialog mProgressDialog;
    private TimeManager mTimeManager;
    private Dialog mDialogTimeOutReconnect;
    private static final int TIME_OUT_RECONNECT = 30000;
    private String btuName = "";
    private VehicleRoomDb vehicleRoomDb;

    private ArrayList<VehicleEntity> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_connected_vehicle);
        apiService = ApiClient.getClient(getApplicationContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(getApplicationContext());
        initView();

        mTimeManager = new TimeManager();
    }


    private void initView() {
        rcvList = findViewById(R.id.rcv_list_connected_vehicle);
        setTitleHeader(getString(R.string.activity_list_vehicle));
        vehicleRoomDb = VehicleRoomDb.getInstance(this);
        try {
            list = (ArrayList<VehicleEntity>) vehicleRoomDb.daoAccess().fetchAllVehicle();
            setUpAdapter(list);
        } catch (Exception e) {

        }

        getDataAsync();
    }

    @SuppressLint("StaticFieldLeak")
    public void getDataAsync() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                getAllVehicle();
                return null;
            }
        }.execute();
    }

    private void setUpAdapter(ArrayList<VehicleEntity> al) {
        mViewManager = new LinearLayoutManager(this);
        mAdapter = new RcvListConnectedVehicle(al, this);
        rcvList.setLayoutManager(mViewManager);
        rcvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickedListener(vehicle -> {
            String vinConnect = PrefUtils.getStringPref(this, Constants.VIN_OF_CONNECTED_VEHICLE_NOW);
            btuName = VehicleRoomDb.getInstance(this).daoAccess().fetchVehicleByVin(vinConnect).getBtuName();
            int state = StateConnectPre.getInstance(this).getState(vinConnect);
            switch (state) {
                case BTU_ACTIVE:
                    Intent intent = new Intent();
                    intent.setAction(ACTION_SET_CURRENT_DETAIL_VEHICLE);
                    intent.putExtra(POSITION_VEHICLE_DETAIL, al.indexOf(vehicle));
                    sendBroadcast(intent);
                    finish();
                    break;
                case BTU_INACTIVE:
                    actionInactive(vehicle, btuName);
                    break;
                case BTU_UNREGISTERED:
                    registerAgain(vehicle);
                    break;
            }

        });
    }



    private void reConnect(String btuName) {
        LogUtils.startEndMethodLog(true);
//        String btuName = BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS);
        if (TextUtils.isEmpty(btuName)) {
            LogUtils.e("BTU name is empty!");
            return;
        }
        LogUtils.d("Reconnect with BTU : " + btuName);
        BluetoothManager.getInstance().reConnect(this, btuName);
        if (mProgressDialog != null) {
            mTimeManager.startTimer((IF_TimeScheduleListener) () -> {
                BluetoothManager.getInstance().getBleConnectController().stopReconnect();
                showPopupTimeOutReconnect();
            }, TIME_OUT_RECONNECT);
        }
        LogUtils.startEndMethodLog(false);

    }

    private void showPopupTimeOutReconnect() {
        hideProgressDialog();
        runOnUiThread(() -> {
            if (mDialogTimeOutReconnect == null || !mDialogTimeOutReconnect.isShowing()) {
                mDialogTimeOutReconnect = DialogUtils.showDialogDefaultNew(this, R.layout.dialog_report, new DialogUtils.DialogListener() {
                    @Override
                    public void okButtonClick(Dialog dialog1) {
                        mDialogTimeOutReconnect.dismiss();
                        mDialogTimeOutReconnect = null;
                        showProgressDialog();
                        reConnect("");
                        dialog1.dismiss();
                    }


                    @Override
                    public void cancelButtonClick() {
                    }
                });
                TextView tvHelp = mDialogTimeOutReconnect.findViewById(R.id.btn_cancel);
                tvHelp.setText(getResources().getString(R.string.help));
                tvHelp.setBackground(ContextCompat.getDrawable(this, R.drawable.button_background));
                TextView tvTitle = mDialogTimeOutReconnect.findViewById(R.id.txt_title_logout);
                tvTitle.setText(getResources().getString(R.string.connection_problem));
                tvHelp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogTimeOutReconnect.dismiss();
                        DialogUtils.showDialogHelp(ListConnectedVehicleActivity.this, R.layout.dialog_help, () -> {
                            //intent to dealer
                            Intent intent = new Intent();
                            intent.setAction(UPDATE_CURRENT_VIEWPAGER);
                            intent.putExtra(TYPE_VIEWPAGE, TAB_DEALERS);
                            sendBroadcast(intent);
                            mDialogTimeOutReconnect = null;
                        });
                    }
                });

                if (!mDialogTimeOutReconnect.isShowing()) {
                    mDialogTimeOutReconnect.show();
                }
            }
        });

    }

    private void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new Dialog(this);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void getAllVehicle() {
        apiService
                .getAllVehicleByUser(mTypeFullAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VehicleResponse>() {
                    @Override
                    public void onSuccess(VehicleResponse response) {
                        alVehicle.clear();
                        if (response.getVehicle() != null && response.getVehicle().getVehicles() != null) {
                            for (int i = 0; i < response.getVehicle().getVehicles().size(); i++) {
                                response.getVehicle().getVehicles().get(0);
                                if (response.getVehicle().getVehicles().get(i).getIsConnected() == 1) {
                                    Vehicle v = new Vehicle(
                                            response.getVehicle().getVehicles().get(i).getVin(),
                                            response.getVehicle().getVehicles().get(i).getVehicleName(),
                                            response.getVehicle().getVehicles().get(i).getLicensePlate(),
                                            response.getVehicle().getVehicles().get(i).getVehicleModelName(),
                                            "aa",
                                            false,
                                            false,
                                            null
                                    );
                                    alVehicle.add(v);
                                }


                            }
                        }
//                        setUpAdapter(alVehicle);
                    }

                    @Override
                    public void onError(Throwable e) {
                        alVehicle = new ArrayList<>();
//                        setUpAdapter(alVehicle);
                        DialogUtils.showDialogConfirmLogin(getApplicationContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, e.getMessage());
                    }
                });
    }

    private Dialog mDialogInactive;

    @Override
    public void onConnectStateNotice(BluetoothManager.EnumDeviceConnectStatus connectState) {
        mTimeManager.stopTimeSchedule();
        LogUtils.startEndMethodLog(true);
        LogUtils.d(connectState.name());

        switch (connectState) {
            case BTU_ACTIVE:
//                statusBTU = BTU_ACTIVE;
                hideProgressDialog();
//                updateStateBluetooth(false);
                runOnUiThread(() -> {
                    if (mDialogInactive != null) {
                        mDialogInactive.dismiss();
                        mDialogInactive = null;
                    }
                    if (mDialogTimeOutReconnect != null) {
                        mDialogTimeOutReconnect.dismiss();
                        mDialogTimeOutReconnect = null;
                    }
//                    processCheckNotificationAccess();
                });
                break;
            case BTU_INACTIVE:
//                statusBTU = BTU_INACTIVE;
                hideProgressDialog();
//                statusBTU = BTU_INACTIVE;
                break;
            case DISCONNECTED:
//                updateStateBluetooth(true);
//                statusBTU = DISCONNECT;
                break;
            case BTU_UNREGISTERED:
//                statusBTU = BTU_UNREGISTERED;
                hideProgressDialog();
                break;
            default:
                break;
        }
        LogUtils.startEndMethodLog(false);

    }

    private void registerAgain(VehicleEntity vehicleEntity) {
        DialogUtils.showDialogReconnected(this, vehicleEntity.getVehicleName(), () -> {
                    Intent intent = new Intent(this, ConnectedMainActivity.class);
                    intent.putExtra("VIN", vehicleEntity.getVinId());
                    startActivity(intent);
                }
        );
    }

    private void actionInactive(VehicleEntity vehicle, String btuName) {
        String content = vehicle.getVehicleName() + " is currently INACTIVE\nDo you want to connect and set\n ACTIVE for this vehicle? ";
        runOnUiThread(() -> {
            if (mDialogTimeOutReconnect != null) {
                mDialogTimeOutReconnect.dismiss();
                mDialogTimeOutReconnect = null;
            }
            if (mDialogInactive == null || !mDialogInactive.isShowing()) {
                mDialogInactive = DialogCommon.createPopup(getApplicationContext()
                        , getString(R.string.text_button_dialog_cancel)
                        , getString(R.string.text_button_dialog_ok)
                        , content
                        , v -> {
                            if (mDialogInactive != null) {
                                mDialogInactive.dismiss();
                                mDialogInactive = null;
                            }
                            showProgressDialog();
                            BluetoothManager.getInstance().setDeviceCurrentStatus(BluetoothManager.EnumDeviceConnectStatus.BTU_INACTIVE_SECOND_TIME);
                            reConnect(btuName);
                        }
                        , v -> {
                            if (mDialogInactive != null) {
                                mDialogInactive.dismiss();
                                mDialogInactive = null;
                            }
                        });
                if (!mDialogInactive.isShowing()) {
                    mDialogInactive.show();
                }
            }
        });
    }
}
