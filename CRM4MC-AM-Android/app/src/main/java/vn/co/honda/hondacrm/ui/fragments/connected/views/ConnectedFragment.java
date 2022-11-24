package vn.co.honda.hondacrm.ui.fragments.connected.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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
import vn.co.honda.hondacrm.btu.Service.PushDataFromBTUService;
import vn.co.honda.hondacrm.btu.Utils.DialogCommon;
import vn.co.honda.hondacrm.btu.Utils.FragmentUtils;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;
import vn.co.honda.hondacrm.btu.Utils.PermissionUtils;
import vn.co.honda.hondacrm.btu.Utils.ProgressDialogCommon;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.consumption.ConsumptionResponse;
import vn.co.honda.hondacrm.ui.activities.connected.view.ConnectedMainActivity;
import vn.co.honda.hondacrm.ui.activities.connected.view.FuelStatusActivity;
import vn.co.honda.hondacrm.ui.activities.connected.view.base.BaseFragment;
import vn.co.honda.hondacrm.ui.activities.home.MainActivity;
import vn.co.honda.hondacrm.ui.adapters.connected.RcvConnectedDetailAdapter;
import vn.co.honda.hondacrm.ui.adapters.connected.RcvConnectedDetailListAdapter;
import vn.co.honda.hondacrm.ui.fragments.connected.local.model.InfoVehicle;
import vn.co.honda.hondacrm.ui.fragments.connected.local.model.VehicleEntity;
import vn.co.honda.hondacrm.ui.fragments.connected.local.roomDatabase.VehicleRoomDb;
import vn.co.honda.hondacrm.ui.fragments.connected.models.DetailInformationItem;
import vn.co.honda.hondacrm.ui.fragments.connected.models.Vehicle;
import vn.co.honda.hondacrm.ui.fragments.connected.models.VehicleParameter;
import vn.co.honda.hondacrm.ui.fragments.connected.presenter.GridSpacingItemDecoration;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.NotificationChangeOil;
import vn.co.honda.hondacrm.utils.PrefUtils;
import vn.co.honda.hondacrm.utils.StateChangeOilPre;
import vn.co.honda.hondacrm.utils.StateConnectPre;

import static vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_BTU_NAME_REGISTER_SUCCESS;
import static vn.co.honda.hondacrm.utils.Constants.ACTION_UPDATE_API;
import static vn.co.honda.hondacrm.utils.Constants.BTU_ACTIVE;
import static vn.co.honda.hondacrm.utils.Constants.BTU_INACTIVE;
import static vn.co.honda.hondacrm.utils.Constants.BTU_UNREGISTERED;
import static vn.co.honda.hondacrm.utils.Constants.DISCONNECT;
import static vn.co.honda.hondacrm.utils.Constants.TAB_CONNECTED;
import static vn.co.honda.hondacrm.utils.Constants.TAB_DEALERS;
import static vn.co.honda.hondacrm.utils.Constants.TYPE_VIEWPAGE;
import static vn.co.honda.hondacrm.utils.Constants.UPDATE_CURRENT_VIEWPAGER;

/**
 * Create by CuongNV31
 */
public class ConnectedFragment extends BaseFragment implements
        IF_VehicleConnectListener, VehicleInfoMsgResolver.VehicleInfoListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private RecyclerView rcvList, rcv_ll;
    private ConstraintLayout layoutMiddle;
    private Spinner snTimeUpdate;
    private RecyclerView.LayoutManager viewManager;
    private ImageView ivTabListView;
    private ImageView ivIabGridView;

    private ConstraintLayout layoutTop;
    private ConstraintLayout layoutChangeOil;
    private TextView tvNameVehicle;
    private TextView tvLicensePlate;
    private TextView tvVinCode;
    private TextView tvStatus;
    private TextView tvChangeOil;
    private TextView tvBookService;
    private TextView tvConnectNow;
    private ImageView ivStatus;
    private ImageView ivChangeOil;
    private SwitchCompat swSetDefault;
    private LinearLayout llListConnectedVehicle;
    private NestedScrollView svMain;

    //    private RcvConnectDetailListAdapter mRcvConnectDetailListAdapter;
    private RcvConnectedDetailListAdapter rcvConnectedDetailLinearLayout;
    private RcvConnectedDetailAdapter mRcvConnectDetailListAdapter;


    private int timeToUpdate;
    private boolean isGridViewShow; // check if recycler view detail use grid view

    protected ProgressDialogCommon mProgressDialog;
    private TimeManager mTimeManager;
    private Dialog mDialogInactive;
    private Dialog mDialogTimeOutReconnect;
    private Dialog dialogCheckBluetooth;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int TIME_OUT_RECONNECT = 30000;

    private Vehicle mVehicle;
    private boolean isWaitingBtuDisconnect;
    private ArrayList<DetailInformationItem> alItemData;
    private String mTypeFullAccessToken;
    private VehicleRoomDb mDb;

    private ApiService apiService;
    private boolean isConnectedNow;
    private String btuName = "";


    public ConnectedFragment() {
    }

    @SuppressLint("ValidFragment")
    public ConnectedFragment(Vehicle v, ArrayList<DetailInformationItem> alItem, boolean isWaitingBtuDisconnect) {
        this.mVehicle = v;
        try {
            btuName = VehicleRoomDb.getInstance(getActivity()).daoAccess().fetchVehicleByVin(mVehicle.getVIN()).getBtuName();
        } catch (Exception e) {
            Log.e("Error", "ConnectedFragment: Can't get btuName from room ! ");
        }
        this.alItemData = alItem;
        this.isWaitingBtuDisconnect = isWaitingBtuDisconnect;
    }

    private int engineer = 0, distance = 0, battery = 0, water = 0, intake = 0, grip = 0, throttle = 0, sensor = 0;
    private int statusBTU;

    private Activity activity;
    private IntentFilter mIntentFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_conected_vehicle_detail, container, false);
        initView();
        activity = getActivity();

        apiService = ApiClient.getClient(activity.getApplicationContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(activity.getApplicationContext());
//        mToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImUxMjMyNDAyZDViNTU2ZDM5YWUyYTlmMTc0MjlmNzc3Y2EzY2NmZjQ1ZDhkZTRiMzY5Y2E5OTQyY2Y3NzhmYzdiM2I1NmNmZTE3MWVkZjFmIn0.eyJhdWQiOiIyIiwianRpIjoiZTEyMzI0MDJkNWI1NTZkMzlhZTJhOWYxNzQyOWY3NzdjYTNjY2ZmNDVkOGRlNGIzNjljYTk5NDJjZjc3OGZjN2IzYjU2Y2ZlMTcxZWRmMWYiLCJpYXQiOjE1NjM5Mzg4MTcsIm5iZiI6MTU2MzkzODgxNywiZXhwIjoxNTk1NTYxMjE3LCJzdWIiOiI2MjAiLCJzY29wZXMiOltdfQ.QLEZgxwONt_zweJAKFwOazh-8bzlsqV78lN38tuDx56_YSHyPVKLzXUZuBzJw0s2vhGT3xFnZ9NUOBn55C4Ee2qyLT3l4FJxkpVVnmNi395SGbclCrELy9PkV5adDBWWexmo0ypPs5GkzBjQhpYsrw0cg4bt3pCBNl80FXDk4F9RTi46EB0nM5nI6vTiBwjv-000G3FRlb05BqkExbbtZB4rphv_WbyyGoamzqDNGvKrDVKpCiImv2kbkzUndTMEt_lR_AsicQaecV9xZzq5Aid0OnVXRwv6AM6W016-MHZw1jI8MyA2RWkGs1iKb59Rgai7DYaTEnnN9hsOaH5cPzYdndWH2Ms7vfO3Uk0qxd-xXTNQHyNdSOxnFzIIsAnY8x4vMKwpF6UC45RDdSHf36Wcq-MGOBF6ZK9iMorFdB8qqLEqyTQyqnohw7vzZ2RHq3suFz6SBCdfh8KqSG9zbObrEZV3yS6-dCWygL8LobATBv8IgTNa6KzsAxa1-vhjaVgFo29pPVKY9VnluLcQZIGiX1gNUeXVL79W0F_ZXO7Xs9QsQSA3FesYkFmMli9M0ezlPC92pvmgzU7bJjsx8eoPMGJYo-ykvYnkTB1f3f4uLo_b7XYr2L0CotIU3X_T_yd90-Sj_6SqGBJV9rHBr-PSxU8P2FjvhQ9kaY_CI8k";

        return mRootView;
    }

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_conected_vehicle_detail;
    }

    @Override
    protected void initView() {
//       layout middle
        layoutMiddle = findViewById(R.id.testtt);
        snTimeUpdate = layoutMiddle.findViewById(R.id.sn_item_sn_connected_vehicle_time_to_update);
        ivIabGridView = layoutMiddle.findViewById(R.id.iv_grid_view_tab);
        ivTabListView = layoutMiddle.findViewById(R.id.iv_list_view_tab);

//       layout top
        layoutTop = findViewById(R.id.layout_connected_top);
        tvNameVehicle = layoutTop.findViewById(R.id.tv_item_connected_vehicle_detail_top_name);
        tvLicensePlate = layoutTop.findViewById(R.id.tv_item_connected_vehicle_detail_top_license);
        tvVinCode = layoutTop.findViewById(R.id.tv_item_connected_vehicle_detail_top_vin_code);
        tvStatus = layoutTop.findViewById(R.id.tv_item_connected_vehicle_detail_top_status_title);
        tvChangeOil = layoutTop.findViewById(R.id.tv_item_connected_vehicle_detail_top_change_oil);
        tvBookService = layoutTop.findViewById(R.id.btn_item_connected_vehicle_detail_top_service);
        tvConnectNow = layoutTop.findViewById(R.id.btn_item_connected_vehicle_detail_top_connect_now);
        ivStatus = layoutTop.findViewById(R.id.iv_item_connected_vehicle_detail_top_status_img);
        ivChangeOil = layoutTop.findViewById(R.id.iv_item_connected_vehicle_detail_top_change_oil_img);
        layoutChangeOil = layoutTop.findViewById(R.id.cl_connect_oil);
        swSetDefault = findViewById(R.id.sw_connected_vehicle_detail_slide);
        svMain = findViewById(R.id.sv_connected);
        rcv_ll = findViewById(R.id.rvc_ll);
        rcvList = findViewById(R.id.rcv_connected_vehicle_detail_information);

        timeToUpdate = 0;
        isGridViewShow = true;
        isConnectedNow = false;
        isConnectedNow = PrefUtils.getStringPref(getContext(), Constants.VIN_OF_CONNECTED_VEHICLE_NOW).equals(mVehicle.getVIN());
        mDb = VehicleRoomDb.getInstance(getContext());

        // set up layout view manager for rcv list detail
        viewManager = new GridLayoutManager(getContext(), 2);
        rcvList.addItemDecoration(new GridSpacingItemDecoration(2,
                Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(String.valueOf(16)),
                        getResources().getDisplayMetrics())), true));

        // setup spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_array, R.layout.item_drop_down_spinner_connected_vehicle);
        spinnerAdapter.setDropDownViewResource(R.layout.layout_drop_down_spinner_connected_vehicle);
        snTimeUpdate.setPopupBackgroundResource(R.drawable.border_spinner);
        snTimeUpdate.setAdapter(spinnerAdapter);

        // init adapter for rcv detail grid
        mRcvConnectDetailListAdapter = new RcvConnectedDetailAdapter(alItemData);
        rcvList.setLayoutManager(viewManager);
        rcvList.setAdapter(mRcvConnectDetailListAdapter);
        updateDataInRoom();
    }

    private void updateDataInRoom() {
        try {
            VehicleEntity ve = new VehicleEntity(
                    mVehicle.getVIN(),
                    mVehicle.getName(),
                    mVehicle.getLicense_plate(),
                    mVehicle.getType(),
                    false,
                    mVehicle.getVIN().equals(PrefUtils.getStringPref(getContext(), Constants.DEFAULT_VEHICLE)),
                    false,
                    mDb.daoAccess().fetchVehicleByVin(mVehicle.getVIN()).getBtuName(),
                    mVehicle.getVehicleImage(),
                    mVehicle.getDistanceOfMaintenance(), mVehicle.getDateReminder(), mVehicle.getReminderCount(), mVehicle.getLastDistance()
            );
            mDb.daoAccess().updateVehicle(ve);
        } catch (Exception e) {
            Log.e("Error", "updateDataInRoom: Can't update room !");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void setActionForView() {
        // layout top
        setUpView(mVehicle);
        tvBookService.setOnClickListener(this);
        tvConnectNow.setOnClickListener(this);
        layoutChangeOil.setOnClickListener(this);
        swSetDefault.setOnCheckedChangeListener(this);

        // layout middle
        ivIabGridView.setOnClickListener(this);
        ivTabListView.setOnClickListener(this);
        snTimeUpdate.setOnItemSelectedListener(this);

//        --------------- start_1 ---------------
        Intent intent = new Intent(activity, ConnMCService.class);
        activity.startService(intent);

        // update api
        Intent service = new Intent(getActivity(), PushDataFromBTUService.class);
        getActivity().startService(service);

        updateStateBluetooth(true);

        BluetoothManager.getInstance().setConnectListener(this);
        VehicleInfoMsgResolver.getInstance().registerListener(this);
        checkPermissionReadContact();
        mTimeManager = new TimeManager();
        processConnectWithBTU();
//        ------------- end_1 -------------------


        // reconnect if this vehicle has been setDefault
//        handleConnectDefault();
    }

    private void handleConnectDefault() {
        String mDefaultVehicleVin = PrefUtils.getStringPref(getContext(), Constants.DEFAULT_VEHICLE);
        if (mDefaultVehicleVin.equals(mVehicle.getVIN())) {
//            reConnect();
            String btuName = mDb.daoAccess().fetchVehicleByVin(mVehicle.getVIN()).getBtuName();
            if (TextUtils.isEmpty(btuName)) {
                LogUtils.e("BTU name is empty!");
                return;
            }
            reConnect(btuName);
        }
    }

    // spinner onSelected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object item = parent.getItemAtPosition(position);
        if (item != null) {
            timeToUpdate = Integer.parseInt(item.toString());
            PrefUtils.setStringPref(getContext(), Constants.TIME_TO_UPDATE_VEHICLE_DETAIL, item.toString());
        }
    }

    // spinner onSelected
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        timeToUpdate = 5;
    }

    // view in fragment onClick
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_grid_view_tab:
                isGridViewShow = true;
                changeView(true);
                mRcvConnectDetailListAdapter = new RcvConnectedDetailAdapter(
                        alItemData);
                viewManager = new GridLayoutManager(getContext(), 2);
                rcvList.setLayoutManager(viewManager);
                rcvList.setAdapter(mRcvConnectDetailListAdapter);
                break;
            case R.id.iv_list_view_tab:
                isGridViewShow = false;
                changeView(false);

                RecyclerView.LayoutManager rl = new LinearLayoutManager(getContext());
                rcvConnectedDetailLinearLayout = new RcvConnectedDetailListAdapter(alItemData,
                        activity);
                rcv_ll.setLayoutManager(rl);
                rcv_ll.setAdapter(rcvConnectedDetailLinearLayout);
                break;
            case R.id.tv_item_connected_vehicle_detail_top_change_oil:
                intent = new Intent(getActivity(), FuelStatusActivity.class);
                intent.putExtra(Constants.VEHICLE_OIL, mVehicle);
                startActivity(intent);
                break;
            case R.id.btn_item_connected_vehicle_detail_top_service:
                Toast.makeText(getContext(), "Clicked book service!", Toast.LENGTH_SHORT).show();

                Intent i = new Intent();
                i.setAction(UPDATE_CURRENT_VIEWPAGER);
                i.putExtra(TYPE_VIEWPAGE, TAB_CONNECTED);
                getActivity().sendBroadcast(i);

                break;
            case R.id.btn_item_connected_vehicle_detail_top_connect_now:
                connectNow();
                break;
            case R.id.cl_connect_oil:
                if (isConnectedNow) {
                    intent = new Intent(activity, FuelStatusActivity.class);
                    intent.putExtra(Constants.VEHICLE_OIL, mVehicle);
                    intent.putExtra(Constants.DISTANCE_OIL, distance);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "This vehicle isn't connecting", Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            PrefUtils.setStringPref(getContext(), Constants.DEFAULT_VEHICLE, mVehicle.getVIN());
        } else {
            String sVin = PrefUtils.getStringPref(getContext(), Constants.DEFAULT_VEHICLE);
            PrefUtils.setStringPref(getContext(), Constants.DEFAULT_VEHICLE, "");
            if (sVin.equals(mVehicle.getVIN())) {
                PrefUtils.setStringPref(getContext(), Constants.DEFAULT_VEHICLE, "");
            }
        }
    }

    private void setUpView(Vehicle v) {
        if (mVehicle != null) {
            tvNameVehicle.setText(mVehicle.getName());
            tvLicensePlate.setText(mVehicle.getLicense_plate());
            tvVinCode.setText(mVehicle.getVIN());
            String btuName = BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS);
            if (btuName != null && !btuName.equals("") && PrefUtils.getStringPref(getContext(), Constants.DEFAULT_VEHICLE).equals("")) {
                PrefUtils.setStringPref(getContext(), Constants.DEFAULT_VEHICLE, mVehicle.getVIN());
            }
            if (PrefUtils.getStringPref(getContext(), Constants.DEFAULT_VEHICLE).equals(mVehicle.getVIN())) {
                swSetDefault.setChecked(true);
            } else {
                swSetDefault.setChecked(false);
            }
            // if connected or not
//            if (isConnect) {
//                tvStatus.setText(getResources().getString(R.string.connect_is_connected));
//                ivStatus.setImageResource(R.drawable.ic_connected);
//                tvNameVehicle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRedDark2));
//                tvConnectNow.setVisibility(View.GONE);
//            } else {
//                tvStatus.setText(getResources().getString(R.string.connect_is_disconnected));
//                ivStatus.setImageResource(R.drawable.ic_bluetooth_gray);
//                tvNameVehicle.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
//                tvConnectNow.setVisibility(View.VISIBLE);
//            }
            // if need change oil

        }
    }

    private void updateStateBluetooth(boolean isOn) {
        getActivity().runOnUiThread(() -> {
            if (isOn) {
                tvStatus.setText(getResources().getString(R.string.connect_is_connected));
                ivStatus.setImageResource(R.drawable.ic_connected);
                tvNameVehicle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRedDark2));
                tvConnectNow.setVisibility(View.GONE);
            } else {
                tvStatus.setText(getResources().getString(R.string.connect_is_disconnected));
                ivStatus.setImageResource(R.drawable.ic_bluetooth_gray);
                tvNameVehicle.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                tvConnectNow.setVisibility(View.VISIBLE);
            }

        });
    }

    // change view if switch recycler view
    private void changeView(boolean isGridViewShow) {
        if (isGridViewShow) {
            rcvList.setVisibility(View.VISIBLE);
            rcv_ll.setVisibility(View.GONE);
            ivIabGridView.setImageResource(R.drawable.ic_grid_view_tab);
            ivTabListView.setImageResource(R.drawable.ic_list_view_tab);
        } else {
            rcvList.setVisibility(View.GONE);
            rcv_ll.setVisibility(View.VISIBLE);
            ivIabGridView.setImageResource(R.drawable.ic_grid_view_tab_un_selected);
            ivTabListView.setImageResource(R.drawable.ic_list_view_tab_selected);
        }
    }

    /**
     * ---------------------------------------------------------------------
     * ---------------------------------------------------------------------
     * ---------------------------------------------------------------------
     */

    @Override
    public void onResume() {
        super.onResume();
        if (!BluetoothUtils.isBluetoothDeviceConnected()
                || BluetoothManager.getInstance().getBleConnectController().isConnecting()
                || BluetoothManager.getInstance().getBleRegisterController().isRegisterInProgress()) {
            updateStateBluetooth(false);
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


        //broadcast
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(ACTION_UPDATE_API);
        mIntentFilter.addAction("UPDATECHNAGEOIL");
        activity.registerReceiver(mReceiver, mIntentFilter);
    }


    private void processConnectWithBTU() {
        BluetoothDevice bluetoothDevice = BluetoothManager.getInstance().getBleConnectController().getBluetoothDeviceConnect();
        BluetoothManager.EnumDeviceConnectStatus status = BluetoothManager.getInstance().getDeviceCurrentStatus();
        switch (status) {
            case BTU_ACTIVE:
                statusBTU = BTU_ACTIVE;
                isConnectedNow = true;
                break;
            case BTU_UNREGISTERED:
                statusBTU = BTU_UNREGISTERED;
                isConnectedNow = false;
                break;
            case BTU_INACTIVE:
                statusBTU = BTU_INACTIVE;
                isConnectedNow = false;
                break;
            case DISCONNECTED:
                statusBTU = DISCONNECT;
                isConnectedNow = false;
                break;
        }
        Log.e("hoaiii", "status + " + mVehicle.getVIN() + "---" + statusBTU);
        StateConnectPre.getInstance(getActivity()).saveState(mVehicle.getVIN(), statusBTU);
        if (status != BluetoothManager.EnumDeviceConnectStatus.INITIAL && status != BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE) {
            hideProgressDialog();
            onConnectStateNotice(status);
            return;
        }
        if (bluetoothDevice != null) {
            if (BluetoothUtils.isBluetoothDeviceConnected()) {
                if (!isWaitingBtuDisconnect && status == BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE) {
                    hideProgressDialog();
                    updateStateBluetooth(true);
                    BluetoothManager.getInstance().getBleRegisterController().inquire(false);
                }
            } else {
                if (status == BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE || status == BluetoothManager.EnumDeviceConnectStatus.INITIAL) {
                    reConnect(btuName);
                }
            }
        } else {
            if (status == BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE || status == BluetoothManager.EnumDeviceConnectStatus.INITIAL) {
                reConnect(btuName);
            }
        }
    }

//    private void reConnect() {
//        LogUtils.startEndMethodLog(true);
//        String btuName = BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS);
//        if (TextUtils.isEmpty(btuName)) {
//            LogUtils.e("BTU name is empty!");
//            return;
//        }
//        LogUtils.d("Reconnect with BTU : " + btuName);
//        BluetoothManager.getInstance().reConnect(getActivity());
//        if (mProgressDialog != null) {
//            mTimeManager.startTimer((IF_TimeScheduleListener) () -> {
//                BluetoothManager.getInstance().getBleConnectController().stopReconnect();
//                showPopupTimeOutReconnect();
//            }, TIME_OUT_RECONNECT);
//        }
//        LogUtils.startEndMethodLog(false);
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean isNeedReceiveBluetoothState() {
        return true;
    }

    @Override
    protected boolean isNeedCheckNotificationAccess() {
        return true;
    }

    @Override
    public void onBluetoothON() {
        super.onBluetoothON();
        updateStateBluetooth(true);
    }

    @Override
    public void onBluetoothOFF() {
        super.onBluetoothOFF();
        updateStateBluetooth(false);
        dismissNotificationAccessPopup();
    }

    @Override
    public void onConnectStateNotice(BluetoothManager.EnumDeviceConnectStatus
                                             connectState) {
        if (!isFragmentAlife()) {
            return;
        }
        mTimeManager.stopTimeSchedule();
        LogUtils.startEndMethodLog(true);
        LogUtils.d(connectState.name());

        switch (connectState) {
            case BTU_ACTIVE:
                statusBTU = BTU_ACTIVE;
                hideProgressDialog();
                isConnectedNow = true;
                activity.runOnUiThread(() -> {
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
                updateStateBluetooth(true);
                break;
            case BTU_INACTIVE:
                statusBTU = BTU_INACTIVE;
                isConnectedNow = false;
                hideProgressDialog();
                actionInactive();
                updateStateBluetooth(false);
                break;
            case DISCONNECTED:
                statusBTU = DISCONNECT;
                isConnectedNow = false;
                hideProgressDialog();
                updateStateBluetooth(false);

                break;
            case BTU_UNREGISTERED:
                isConnectedNow = false;
                statusBTU = BTU_UNREGISTERED;
                hideProgressDialog();
                registerAgain();
                updateStateBluetooth(false);
                break;
            default:
                break;
        }


        Log.e("hoaiii", "status = " + mVehicle.getVIN() + "----" + statusBTU);

        StateConnectPre.getInstance(getActivity()).saveState(mVehicle.getVIN(), statusBTU);
        LogUtils.startEndMethodLog(false);
    }

    VehicleParameter vp = new VehicleParameter();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResolvedVehicleInfo(LinkedHashMap<String, BluetoothStatus> statuses) {
        if (!isFragmentAlife()) {
            return;
        }
        if (statuses == null) {
            LogUtils.e("list phone gw data NULL!");
        } else {
            LogUtils.d("list phone gw data size = " + statuses.size());
        }
        Intent service = new Intent(getActivity(), PushDataFromBTUService.class);
        getActivity().startService(service);

        if (statuses != null) {
            for (final String key : statuses.keySet()) {
                BluetoothStatus value = statuses.get(key);
                if (value != null && value.getRawName() != null) {
                    if (value.getRawName().equals("C_ENGTEMP")) {
                        engineer = value.getRawValue();
                        vp.setEngineTemperature(value.getValue());
                    }
                    if (value.getRawName().equals("C_ODO")) {
                        distance = value.getRawValue();
                        vp.setDistanceOfMaintenance(value.getValue());
                        if (distance >= (Integer.parseInt(mVehicle.getLastDistance()) + Integer.parseInt(mVehicle.getDistanceOfMaintenance()))) {
                            if (StateChangeOilPre.getInstance(activity).getCountChangeOil(mVehicle.getVIN()) == -1) {
                                Calendar calendar = Calendar.getInstance();
                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                int cout = Integer.parseInt(mVehicle.getReminderCount()) * (Integer.parseInt(mVehicle.getDateReminder()) - 1) + (24 - hour) / (24 / (Integer.parseInt(mVehicle.getReminderCount())));
                                StateChangeOilPre.getInstance(getActivity()).saveCountChangeOil(mVehicle.getVIN(), cout - 1);
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                NotificationChangeOil.showNotificationFirst(activity, mVehicle.getName() + " have a oil change appointment", mVehicle.getVIN(), 1, intent, Integer.parseInt(mVehicle.getReminderCount()));
                            }

                        }
                    }
                    if (value.getRawName().equals("ENG_BTU_VB_11")) {
                        battery = value.getRawValue();
                        vp.setBatteryVoltage(value.getValue());
                    }
                    if (value.getRawName().equals("ENG_BTU_ECT_11")) {
                        water = value.getRawValue();
                        vp.setWaterTemperature(value.getValue());
                    }
                    if (value.getRawName().equals("C_INTAKE_AIR_TEMP")) {
                        intake = value.getRawValue();
                        vp.setIntakeAirTemperature(value.getValue());
                    }
                    if (value.getRawName().equals("C_ASP")) {
                        grip = value.getRawValue();
                        vp.setGridOpeningDegree(value.getValue());
                    }
                    if (value.getRawName().equals("ENG_BTU_THDEG_11")) {
                        throttle = value.getRawValue();
                        vp.setThrottleOpeningDegree(value.getValue());
                    }
                    if (value.getRawName().equals("ENG_BTU_VO2_20")) {
                        sensor = value.getRawValue();
                        vp.setO2Sensor(value.getValue());
                    }
                }
            }
        }

        if (isConnectedNow) {
            if (isGridViewShow) {
                LogUtils.d("list zxcxzc = " + statuses.size() + " - " + mVehicle.getVIN());
                activity.runOnUiThread(() ->
//                        mRcvConnectDetailListAdapter.updateListStatus(statuses));
                        mRcvConnectDetailListAdapter.updateListStatus(vp));
            } else {
                activity.runOnUiThread(() ->
                        rcvConnectedDetailLinearLayout.updateListStatus(vp));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        VehicleInfoMsgResolver.getInstance().unRegisterListener(this);
        BluetoothManager.getInstance().getBleConnectController().unRegisterListener(this);
        activity.unregisterReceiver(mReceiver);
    }

    private void reConnect(String btuName) {
        LogUtils.startEndMethodLog(true);
//        String btuName = BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS);
        if (TextUtils.isEmpty(btuName)) {
            LogUtils.e("BTU name is empty!");
            return;
        }
        LogUtils.d("Reconnect with BTU : " + btuName);
        BluetoothManager.getInstance().reConnect(activity, btuName);
        if (mProgressDialog != null) {
            mTimeManager.startTimer((IF_TimeScheduleListener) () -> {
                BluetoothManager.getInstance().getBleConnectController().stopReconnect();
                showPopupTimeOutReconnect();
            }, TIME_OUT_RECONNECT);
        }
        LogUtils.startEndMethodLog(false);
    }

    Dialog dialogProblem;

    private void reConnectDisconnect(String btuName) {
        LogUtils.startEndMethodLog(true);
        if (TextUtils.isEmpty(btuName)) {
            hideProgressDialog();
            LogUtils.e("BTU name is empty!");
            return;
        }

        if (BluetoothManager.getInstance().reConnect(activity, btuName)) {
            dialogProblem = DialogCommon.createPopup(getActivity(), getString(R.string.connection_problem_), getString(R.string.text_button_dialog_ok), view -> {
                dialogProblem.dismiss();
            });
            dialogProblem.show();
            hideProgressDialog();
        } else {
            hideProgressDialog();

        }

        LogUtils.startEndMethodLog(false);
    }

    private void showPopupTimeOutReconnect() {
        if (isFragmentAlife()) {
            hideProgressDialog();
            activity.runOnUiThread(() -> {
                if (mDialogTimeOutReconnect == null || !mDialogTimeOutReconnect.isShowing()) {
                    mDialogTimeOutReconnect = DialogUtils.showDialogDefaultNew(activity, R.layout.dialog_report, new DialogUtils.DialogListener() {
                        @Override
                        public void okButtonClick(Dialog dialog1) {
                            mDialogTimeOutReconnect.dismiss();
                            mDialogTimeOutReconnect = null;
                            showProgressDialog();
                            reConnect(btuName);
                            dialog1.dismiss();
                        }

                        @Override
                        public void cancelButtonClick() {
                        }
                    });
                    TextView tvHelp = mDialogTimeOutReconnect.findViewById(R.id.btn_cancel);
                    tvHelp.setText(getResources().getString(R.string.help));
                    tvHelp.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_background));
                    TextView tvTitle = mDialogTimeOutReconnect.findViewById(R.id.txt_title_logout);
                    tvTitle.setText(activity.getResources().getString(R.string.connection_problem));
                    tvHelp.setOnClickListener(v -> {
                        mDialogTimeOutReconnect.dismiss();
                        DialogUtils.showDialogHelp(activity, R.layout.dialog_help, () -> {
                            //intent to dealer
                            Intent intent = new Intent();
                            intent.setAction(UPDATE_CURRENT_VIEWPAGER);
                            intent.putExtra(TYPE_VIEWPAGE, TAB_DEALERS);
                            activity.sendBroadcast(intent);
                            mDialogTimeOutReconnect = null;
                        });
                    });

                    if (isResumed() && !mDialogTimeOutReconnect.isShowing()) {
                        mDialogTimeOutReconnect.show();
                    }
                }
            });
        }
    }


    private void checkPermissionReadContact() {
        if (!PermissionUtils.hasPermissions(activity, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void unregisterFromCurrentVehicle() {
        activity.stopService(new Intent(activity, ConnMCService.class));
        BluetoothManager.getInstance().unRegisterDevice();
    }

    private void handleUnRegister() {
        activity.runOnUiThread(() -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if (fragmentManager != null) {
                FragmentUtils.clearAllFragment(getFragmentManager());
                FragmentUtils.replaceFragment(fragmentManager, new ScanVehicleFragment(), R.id.fragment_container, false, FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);
            }
        });
    }

    @Override
    protected void onBackPressedFragment() {
        activity.finish();
    }

    // click connect now
    private void connectNow() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            showBluetoothRequestDialog();
        } else {
            switch (statusBTU) {
                case BTU_INACTIVE:
                    actionInactive();
                    break;
                case BTU_UNREGISTERED:
                    registerAgain();
                    break;
                case DISCONNECT:
                    showProgressDialog();
                    reConnectDisconnect(btuName);
                    break;
                default:
                    showProgressDialog();
                    reConnectDisconnect("");
                    break;
            }
        }
    }


    private void registerAgain() {
        activity.runOnUiThread(() -> {
            DialogUtils.showDialogReconnected(activity, mVehicle.getName(), () ->
                    startActivity(new Intent(activity, ConnectedMainActivity.class)));
        });


    }

    private void actionInactive() {
        activity.runOnUiThread(() -> {
            if (mDialogTimeOutReconnect != null) {
                mDialogTimeOutReconnect.dismiss();
                mDialogTimeOutReconnect = null;
            }
            String content = mVehicle.getName() + " is currently INACTIVE\nDo you want to connect and set\n ACTIVE for this vehicle? ";
            if (mDialogInactive == null || !mDialogInactive.isShowing()) {
                mDialogInactive = DialogCommon.createPopup(activity
                        , getString(R.string.text_button_dialog_cancel)
                        , getString(R.string.text_button_dialog_ok)
                        , content
                        , v -> {

                            if (mDialogInactive != null) {
                                mDialogInactive.dismiss();
                                mDialogInactive = null;
                            }
                        }
                        , v -> {
                            if (mDialogInactive != null) {
                                mDialogInactive.dismiss();
                                mDialogInactive = null;
                            }
                            showProgressDialog();
                            BluetoothManager.getInstance().setDeviceCurrentStatus(BluetoothManager.EnumDeviceConnectStatus.BTU_INACTIVE_SECOND_TIME);
                            reConnect(btuName);
                        });
                if (isResumed() && !mDialogInactive.isShowing()) {
                    mDialogInactive.show();
                }
            }
        });
    }

    private void showBluetoothRequestDialog() {
        activity.runOnUiThread(() -> {
            if (dialogCheckBluetooth == null) {
                dialogCheckBluetooth = DialogUtils.showDialogDefaultNew(activity, R.layout.dialog_report, new DialogUtils.DialogListener() {
                    @Override
                    public void okButtonClick(Dialog dialog1) {
                        dialog1.dismiss();
                        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                        startActivity(intent);
                    }

                    @Override
                    public void cancelButtonClick() {
                        dialogCheckBluetooth.dismiss();
                    }
                });
                TextView tvContent = dialogCheckBluetooth.findViewById(R.id.txt_title_logout);
                tvContent.setText("This application requires bluetooth connection.\nPlease turn on bluetooth");
//                if (!dialogCheckBluetooth.isShowing()) {
//                    dialogCheckBluetooth.show();
//                }
            }
        });
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ACTION_UPDATE_API)) {
                if (isConnectedNow)
                    configConsumptionSetting(mVehicle.getVIN(), distance, engineer, battery, water, intake, grip, throttle, sensor);
            } else {
                if (intent.getAction().equals(ACTION_UPDATE_API)) {
                    if (intent.getStringExtra("VINCHANGEOIL").equals(mVehicle.getVIN()))
                        if (intent.getBooleanExtra("ISCHANGEOIL", false)) {
                            tvChangeOil.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRedDark2));
                            ivChangeOil.setImageResource(R.drawable.ic_change_oil_red);
                        } else {
                            tvChangeOil.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                            ivChangeOil.setImageResource(R.drawable.ic_change_oil_black);
                        }
                }
            }
        }
    };

    private void configConsumptionSetting(String vin, int distance,
                                          int engine, int battery, int water, int intake, int grip, int throttle,
                                          int sensor) {
        apiService.updateConsumptionFromBTU(
                mTypeFullAccessToken,
                vin, distance, engine, battery, water, intake,
                grip, throttle, sensor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ConsumptionResponse>() {
                    @Override
                    public void onSuccess(ConsumptionResponse response) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        InfoVehicle infoVehicle = new InfoVehicle(vin, distance, engine, battery, water, intake, grip, throttle, sensor);
                        VehicleRoomDb vehicleRoomDb = VehicleRoomDb.getInstance(getContext());
                        List<InfoVehicle> list = vehicleRoomDb.infoVehicleDao().fetchAllVehicle();
                        boolean isCheck = false;
                        for (InfoVehicle infoVehicle1 : list) {
                            if (infoVehicle1.getVinId().equals(vin)) {
                                isCheck = true;
                            }
                        }
                        if (isCheck) {
                            vehicleRoomDb.infoVehicleDao().insertOnlySingleVehicle(infoVehicle);
                        } else {
                            infoVehicle.setDistance(distance);
                            infoVehicle.setEnginner(engine);
                            infoVehicle.setBattery(battery);
                            infoVehicle.setWater(water);
                            infoVehicle.setIntake(intake);
                            infoVehicle.setGrip(grip);
                            infoVehicle.setThrottle(throttle);
                            infoVehicle.setSensor(sensor);
                            vehicleRoomDb.infoVehicleDao().updateVehicle(infoVehicle);
                        }
                    }
                });
    }


    //changing oil

}

