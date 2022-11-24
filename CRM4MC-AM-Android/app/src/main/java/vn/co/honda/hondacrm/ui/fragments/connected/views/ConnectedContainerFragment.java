package vn.co.honda.hondacrm.ui.fragments.connected.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Objects;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.connectedVehicle.ConnectedVehicleResponse;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleResponse;
import vn.co.honda.hondacrm.ui.activities.connected.view.ListConnectedVehicleActivity;
import vn.co.honda.hondacrm.ui.activities.connected.view.base.BaseFragment;
import vn.co.honda.hondacrm.ui.activities.home.MainActivity;
import vn.co.honda.hondacrm.ui.adapters.connected.VpgContainerAdapter;
import vn.co.honda.hondacrm.ui.fragments.connected.local.model.VehicleEntity;
import vn.co.honda.hondacrm.ui.fragments.connected.local.roomDatabase.VehicleRoomDb;
import vn.co.honda.hondacrm.ui.fragments.connected.models.DetailInformationItem;
import vn.co.honda.hondacrm.ui.fragments.connected.models.Vehicle;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.PrefUtils;

import static vn.co.honda.hondacrm.utils.Constants.ACTION_SET_CURRENT_DETAIL_VEHICLE;
import static vn.co.honda.hondacrm.utils.Constants.POSITION_VEHICLE_DETAIL;

public class ConnectedContainerFragment extends BaseFragment implements View.OnClickListener {
    private ViewPager vpgContainer;
    private LinearLayout layoutConnectedVehicleList;
    private ConstraintLayout layoutConnectedVehicleTop;

    public static ArrayList<Vehicle> alVehicle;
    private ArrayList<DetailInformationItem> alItemData;
    private boolean isWait;

    private ApiService apiService;
    private String mTypeFullAccessToken;
    private ScrollingPagerIndicator pagerIndicator;

    private VehicleRoomDb mDb;
    private IntentFilter mIntentFilter;

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_connected_container;
    }

    @Override
    protected void initView() {
        setDataItem();
        vpgContainer = findViewById(R.id.vpg_connected_container);
        layoutConnectedVehicleList = findViewById(R.id.layout_connected_vehicle_list);
        layoutConnectedVehicleTop = findViewById(R.id.layout_connected_vehicle_top);
        pagerIndicator = findViewById(R.id.pager_indicator);
        layoutConnectedVehicleList.setOnClickListener(this);
        mDb = VehicleRoomDb.getInstance(getContext());
        isWait = false;
        layoutConnectedVehicleTop.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void onBackPressedFragment() {
        getActivity().finish();
    }

    @Override
    protected void setActionForView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_connected_vehicle_list:
                Intent i = new Intent(getActivity(), ListConnectedVehicleActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    private void getAllConnectedVehicle(ArrayList<String> al) {
        ArrayList<Vehicle> alV = new ArrayList<>();
        apiService
                .getAllConnectedVehicle(mTypeFullAccessToken, al)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ConnectedVehicleResponse>() {
                    @Override
                    public void onSuccess(ConnectedVehicleResponse response) {
                        Log.d("getAllConnectedVehicle", " onSuccess ");
//                        hideProgressDialog();
                        alVehicle = new ArrayList<>();
//                        alVehicle.clear();
                        if (response.getData() != null && response.getData().size() > 0) {
                            for (int i = 0; i < response.getData().size(); i++) {
                                if (response.getData().get(i).getFuelStatus() != null) {

                                }
                                Vehicle v = new Vehicle(
                                        response.getData().get(i).getVin(),
                                        response.getData().get(i).getVehicleName(),
                                        response.getData().get(i).getLicensePlate(),
                                        response.getData().get(i).getVehicleModel(),
                                        (response.getData().get(i).getVehicleType() == 1) ? "AutoMobile" : "motor",
                                        response.getData().get(i).getVehicleImage(),
                                        true,
                                        false,
                                        null,
                                        response.getData().get(i).getFuelStatus().getDistanceOfMaintenance(),
                                        response.getData().get(i).getFuelStatus().getDateReminder(),
                                        response.getData().get(i).getFuelStatus().getReminderCount(),
                                        response.getData().get(i).getFuelStatus().getLastDistance()

                                );
                                alVehicle.add(v);
                            }
                            setupViewPager(vpgContainer, alVehicle, isWait);
                        } else {
                            ArrayList<VehicleEntity> al = (ArrayList<VehicleEntity>) mDb.daoAccess().fetchAllVehicle();
                            alVehicle = new ArrayList<>();
                            for (int i = 0; i < al.size(); i++) {
                                Vehicle v = new Vehicle(
                                        al.get(i).getVinId(),
                                        al.get(i).getVehicleName(),
                                        al.get(i).getLicensePlate(),
                                        "",
                                        al.get(i).getVehicleImage(),
                                        al.get(i).getType(),
                                        al.get(i).isConnected(),
                                        false,
                                        null,
                                        response.getData().get(i).getFuelStatus().getDistanceOfMaintenance(),
                                        response.getData().get(i).getFuelStatus().getDateReminder(),
                                        response.getData().get(i).getFuelStatus().getReminderCount(),
                                        response.getData().get(i).getFuelStatus().getLastDistance()
                                );
                                alVehicle.add(v);
                            }
                            setupViewPager(vpgContainer, alVehicle, isWait);
                        }
//                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("getAllConnectedVehicle", "onError: " + e);
                        ArrayList<VehicleEntity> al = (ArrayList<VehicleEntity>) mDb.daoAccess().fetchAllVehicle();
                        alVehicle = new ArrayList<>();
                        for (int i = 0; i < al.size(); i++) {
                            Vehicle v = new Vehicle(
                                    al.get(i).getVinId(),
                                    al.get(i).getVehicleName(),
                                    al.get(i).getLicensePlate(),
                                    "",
                                    al.get(i).getType(),
                                    al.get(i).getVehicleImage(),
                                    al.get(i).isConnected(),
                                    false,
                                    null,
                                    al.get(i).getDistanceOfMaintenance(),
                                    al.get(i).getDateReminder(),
                                    al.get(i).getReminderCount(),
                                    al.get(i).getLastDistance()
                            );
                            alVehicle.add(v);
                        }
                        setupViewPager(vpgContainer, alVehicle, isWait);
//                        hideProgressDialog();
                    }
                });
    }


    private void getAllVehicle() {
        apiService
                .getAllVehicleByUser(mTypeFullAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VehicleResponse>() {
                    @Override
                    public void onSuccess(VehicleResponse response) {
                        Log.d("getAllVehicle", "onSuccess ");
                        alVehicle.clear();
                        if (response.getVehicle() != null && response.getVehicle().getVehicles() != null) {
                            for (int i = 0; i < response.getVehicle().getVehicles().size(); i++) {
//                                if (response.getVehicle().getVehicles().get(i).getIsConnected() == 1) {
                                Vehicle v = new Vehicle(
                                        response.getVehicle().getVehicles().get(i).getVin(),
                                        response.getVehicle().getVehicles().get(i).getVehicleName(),
                                        response.getVehicle().getVehicles().get(i).getLicensePlate(),
                                        response.getVehicle().getVehicles().get(i).getVehicleModelName(),
                                        (response.getVehicle().getVehicles().get(i).getVehicleType() == 1) ? "AutoMobile" : "motor",
                                        response.getVehicle().getVehicles().get(i).getVehicleImage(),
                                        response.getVehicle().getVehicles().get(i).getIsConnected() == 1,
                                        false,
                                        null, "", "", "", ""
                                );
                                alVehicle.add(v);
                            }
                        }
//                        }
                        hideProgressDialog();
                        setupViewPager(vpgContainer, alVehicle, isWait);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("getAllVehicle", "onError: " + e);
                        ArrayList<VehicleEntity> al = (ArrayList<VehicleEntity>) mDb.daoAccess().fetchAllVehicle();
                        alVehicle = new ArrayList<>();
                        for (int i = 0; i < al.size(); i++) {
                            Vehicle v = new Vehicle(
                                    al.get(i).getVinId(),
                                    al.get(i).getVehicleName(),
                                    al.get(i).getLicensePlate(),
                                    "",
                                    al.get(i).getType(),
                                    al.get(i).getVehicleImage(),
                                    al.get(i).isConnected(),
                                    false,
                                    null
                                    , "", "", "", ""
                            );
                            alVehicle.add(v);
                        }
                        setupViewPager(vpgContainer, alVehicle, isWait);
                    }
                });
    }


    private void setupViewPager(ViewPager vpgContainer, ArrayList<Vehicle> al, boolean is) {
        VpgContainerAdapter adapter = new VpgContainerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager());
        int mVinDefaultPosition = 0;
        String mVinDefault;
        if (al.size() <= 0) {
            adapter.addFragment(new IntroduceNewProductFragment(), "");
            vpgContainer.setAdapter(adapter);
            layoutConnectedVehicleTop.setVisibility(View.INVISIBLE);
        } else {
            layoutConnectedVehicleTop.setVisibility(View.VISIBLE);
            if (!PrefUtils.getStringPref(getContext(), Constants.DEFAULT_VEHICLE).equals("")) {
                mVinDefault = PrefUtils.getStringPref(getContext(), Constants.DEFAULT_VEHICLE);
                if (al.size() > 0) {
                    for (int i = 0; i < al.size(); i++) {
                        if (al.get(i).getVIN().equals(mVinDefault)) {
//                            Collections.rotate(al.subList(0, i + 1), 1);
                            mVinDefaultPosition = i;
                        }
                    }
                }
            }
            for (int i = 0; i < al.size(); i++) {
                if (al.get(i).getVIN().equals(PrefUtils.getStringPref(getContext(), Constants.VIN_OF_CONNECTED_VEHICLE_NOW))) {
                    adapter.addFragment(new ConnectedFragment(al.get(i), alItemData, is), "");
                } else {
                    adapter.addFragment(new ConnectedFragment(al.get(i), alItemData, false), "");
                }
            }
            vpgContainer.setCurrentItem(al.size() - 1);
            Log.d("setupViewPager2", "" + al.size());
            vpgContainer.setAdapter(adapter);
            vpgContainer.setOffscreenPageLimit(5);
            pagerIndicator.attachToPager(vpgContainer);
            pagerIndicator.setVisibleDotCount(7);
            int count = pagerIndicator.getVisibleDotCount();
            pagerIndicator.setPositionDefault(0);

            vpgContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    MainActivity.updateTitle(al.get(i-1).getName());
//                    vpgContainer.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });


//            vpgContainer.setCurrentItem(al.size());
//            tabContainer.setTabGravity(TabLayout.GRAVITY_FILL);
//            tabContainer.setupWithViewPager(vpgContainer);
            pagerIndicator.setPositionDefault(mVinDefaultPosition);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(getContext());
//        MainActivity m = (MainActivity) getActivity();
//        m.setAboutDataListener(key -> isWait = key);  // get key isWaitingDisconnect
//        m.setOnClickItemListener(() -> {
////            Toast.makeText(m, "Click !", Toast.LENGTH_SHORT).show();
//            try {
//                setUpDataPage();
////                LoadDataAsyncTask l = new LoadDataAsyncTask();
////                l.execute();
//            } catch (Exception e) {
//                Log.e("Error", "ConnectedContainer Load data page fail! ");
//            }
//        });
    }

    public class LoadDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            setUpDataPage();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            hideProgressDialog();
        }
    }

    private void setUpDataPage() {
        MainActivity m = (MainActivity) getActivity();
        m.setAboutDataListener(key -> isWait = key); // get key isWaitingDisconnect
        ArrayList<String> myList = new ArrayList<>();
        try {
            ArrayList<VehicleEntity> alRoom = (ArrayList<VehicleEntity>) mDb.daoAccess().fetchAllVehicle();
            Log.d("setUpDataPage", " " + alRoom.size());
            if (alRoom.size() > 0) {
                for (int i = 0; i < alRoom.size(); i++) {
                    myList.add(alRoom.get(i).getVinId());
                }
            }
        } catch (Exception e) {
            Log.e("Error", "fetch data fail ! ");
        }
        getAllConnectedVehicle(myList);
//        getAllVehicle();
    }


    private void setUpDataPageReturn() {
        ArrayList<String> myList = new ArrayList<>();
//        myList.add("VIN12345678922211");
        try {
            ArrayList<VehicleEntity> alRoom = (ArrayList<VehicleEntity>) mDb.daoAccess().fetchAllVehicle();
            Log.d("setUpDataPage", " " + alRoom.size());
            if (alRoom.size() > 0) {
                for (int i = 0; i < alRoom.size(); i++) {
                    myList.add(alRoom.get(i).getVinId());
                }
            }
        } catch (Exception e) {
            Log.e("Error", "fetch data fail ! ");
        }
        getAllConnectedVehicle(myList);
//        getAllVehicle();
    }


    // fake data
    private void setDataItem() {
        alVehicle = new ArrayList<>();
        alItemData = new ArrayList<>();

        alItemData.add(new DetailInformationItem(0, "Engine temperature", R.drawable.ic_engine_tempurature, "Battery voltage is an indicator of battery quality in your bike.\n" +
                "- Standard battery voltage is 14.0V.\n" +
                "- Stable battery voltage will make driving safer.\n" +
                "- If the battery voltage drops below 7V, you should go to the nearest Head to check."));
        alItemData.add(new DetailInformationItem(1, "Distance", R.drawable.ic_distance_of_maintenance, "Battery voltage is an indicator of battery quality in your bike.\n" +
                "- Standard battery voltage is 14.0V.\n" +
                "- Stable battery voltage will make driving safer.\n" +
                "- If the battery voltage drops below 7V, you should go to the nearest Head to check."));
        alItemData.add(new DetailInformationItem(2, "Battery voltage", R.drawable.ic_battery_voltage, "Battery voltage is an indicator of battery quality in your bike.\n" +
                "- Standard battery voltage is 14.0V.\n" +
                "- Stable battery voltage will make driving safer.\n" +
                "- If the battery voltage drops below 7V, you should go to the nearest Head to check."));
        alItemData.add(new DetailInformationItem(3, "Water temperature", R.drawable.ic_water_temperature, "Battery voltage is an indicator of battery quality in your bike.\n" +
                "- Standard battery voltage is 14.0V.\n" +
                "- Stable battery voltage will make driving safer.\n" +
                "- If the battery voltage drops below 7V, you should go to the nearest Head to check."));
        alItemData.add(new DetailInformationItem(4, "Intake air temperature", R.drawable.ic_intake_air_tmpurature, "Battery voltage is an indicator of battery quality in your bike.\n" +
                "- Standard battery voltage is 14.0V.\n" +
                "- Stable battery voltage will make driving safer.\n" +
                "- If the battery voltage drops below 7V, you should go to the nearest Head to check."));
        alItemData.add(new DetailInformationItem(5, "Grip opening degree", R.drawable.ic_grip_opening_degree, "Battery voltage is an indicator of battery quality in your bike.\n" +
                "- Standard battery voltage is 14.0V.\n" +
                "- Stable battery voltage will make driving safer.\n" +
                "- If the battery voltage drops below 7V, you should go to the nearest Head to check."));
        alItemData.add(new DetailInformationItem(6, "Throttle opening degree", R.drawable.ic_thottle_opening_degree, "Battery voltage is an indicator of battery quality in your bike.\n" +
                "- Standard battery voltage is 14.0V.\n" +
                "- Stable battery voltage will make driving safer.\n" +
                "- If the battery voltage drops below 7V, you should go to the nearest Head to check."));
        alItemData.add(new DetailInformationItem(7, "O2 sensor", R.drawable.ic_o2_sensor, "Battery voltage is an indicator of battery quality in your bike.\n" +
                "- Standard battery voltage is 14.0V.\n" +
                "- Stable battery voltage will make driving safer.\n" +
                "- If the battery voltage drops below 7V, you should go to the nearest Head to check."));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("asdasdas", "onResume: " + "fidhghfg");

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(ACTION_SET_CURRENT_DETAIL_VEHICLE);
        getActivity().registerReceiver(mReceiver, mIntentFilter);

        MainActivity m = (MainActivity) getActivity();
        m.setAboutDataListener(key -> isWait = key);  // get key isWaitingDisconnect
        m.setOnClickItemListener(() -> {
            try {
                Log.e("hoaiii", "vaoday");
//                showProgressDialog();
                setUpDataPage();
//                LoadDataAsyncTask l = new LoadDataAsyncTask();
//                l.execute();
            } catch (Exception e) {
                Log.e("Error", "Load data page fail 1 ! ");
            }
        });

    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_SET_CURRENT_DETAIL_VEHICLE)) {
                int position = intent.getIntExtra(POSITION_VEHICLE_DETAIL, -1);
                if (position != -1) {
                    vpgContainer.setCurrentItem(position);
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    public static String getTitleCurrent() {
        return alVehicle.get(alVehicle.size() - 1).getName();
    }
}
