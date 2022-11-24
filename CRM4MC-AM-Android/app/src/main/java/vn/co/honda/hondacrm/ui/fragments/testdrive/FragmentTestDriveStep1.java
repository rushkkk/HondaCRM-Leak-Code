package vn.co.honda.hondacrm.ui.fragments.testdrive;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.dealer.DealerResponse;
import vn.co.honda.hondacrm.net.model.dealer.Dealers;
import vn.co.honda.hondacrm.net.model.dealer.District;
import vn.co.honda.hondacrm.net.model.dealer.DistrictResponse;
import vn.co.honda.hondacrm.net.model.dealer.Province;
import vn.co.honda.hondacrm.net.model.dealer.ProvinceResponse;
import vn.co.honda.hondacrm.ui.activities.testdrive.INumberStepCallBack;
import vn.co.honda.hondacrm.ui.activities.testdrive.TestDriveActivity;

import vn.co.honda.hondacrm.ui.fragments.testdrive.adapter.DealersAdapter;
import vn.co.honda.hondacrm.ui.fragments.testdrive.adapter.DealersAdapterFull;
import vn.co.honda.hondacrm.ui.fragments.testdrive.adapter.OnClick;
import vn.co.honda.hondacrm.ui.fragments.testdrive.adapter.SpinnerDistrictAdapter;
import vn.co.honda.hondacrm.ui.fragments.testdrive.adapter.SpinnerProvinceAdapter;
import vn.co.honda.hondacrm.ui.fragments.testdrive.models.TestDrive;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

//library

public class FragmentTestDriveStep1 extends Fragment implements OnMapReadyCallback, OnClick, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener , INumberStepCallBack {

    private GoogleMap mMap;

    //var
    private static final String MYTAG = "MYTAG";
    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    private Spinner spCity;
    private Spinner spDistrict;
    private LinearLayout imgHideDealer;
    private TextView txt_dealer_gannhat;
    private ImageView btnSetting;
    private RecyclerView recDealer;
    private DealersAdapter dealerAdapter;
    private DealersAdapterFull dealerAdapterfull;
    private RecyclerView recAllDealer;
    ScrollView view_top3_dealer;
    boolean hidetopdealer = true;
    private boolean moto = false;
    private boolean oto = false;
    List<Dealers> dealerList;
    List<Dealers> dealerListTam;
    List<Dealers> dealerListTamMoto;
    List<Dealers> dealerListTamOto;
    List<Dealers> dealerListFull;
    List<Province> provinceList;
    List<District> districtResponses;
    CheckBox elect_dealer_oto, elect_dealer_moto;
    private static List<Province> listProvince;
    private static SpinnerProvinceAdapter spinnerProvinceAdapter;
    private static SpinnerDistrictAdapter spinnerDistrictAdapter;
    private int id_province1;
    private int id_district;
    private Province provinceitem;
    Boolean typeMap=true;
    private  View view_dealer_top3;
    boolean checkChange = false;
    View map,mapview_top3;
    View location_not_found;
    private TestDrive testDrive = new TestDrive();

    public static FragmentTestDriveStep1 newInstance() {
        FragmentTestDriveStep1 fragment = new FragmentTestDriveStep1();
        return fragment;
    }

    //api
    ApiService apiService;
    String mTypeFullAccessToken;

    // location
    private Location location;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    private Location mCurrentLocation;
    private Context mContext;
    private ProgressBar mProgressBarLoading;
    private INumberStepCallBack mNumberStepCallBack;
    //dbien dem
    int dem=1,demfull=1;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof TestDriveActivity) {
            this.mNumberStepCallBack = (TestDriveActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_test_drive, container, false);
        typeMap=true;
        int demfull=1;
        mProgressBarLoading = view.findViewById(R.id.loading);
        mCurrentLocation = new Location("service Provider");
        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > Constants.ZERO) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(mContext).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();

        //get api
        apiService = ApiClient.getClient(mContext.getApplicationContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(mContext.getApplicationContext());
        dealerList = new ArrayList<>();
        dealerListTamMoto = new ArrayList<>();
        dealerListTamOto = new ArrayList<>();
        dealerListTam = new ArrayList<>();
        dealerListFull = new ArrayList<>();
        districtResponses = new ArrayList<>();

        provinceList = new ArrayList<>();
        //creak
        initComponent(view);
        customSpinner();
        customSpinnerDistrick();
        //map
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        //set onclick item
        recDealer.setLayoutManager(new LinearLayoutManager(mContext));
        dealerAdapter = new DealersAdapter(mContext, dealerList, this, false);
        recDealer.setAdapter(dealerAdapter);
        dealerAdapter.notifyDataSetChanged();

        // init list show all
        showListAllDealer();

        imgHideDealer.setOnClickListener(v -> {
                    if (hidetopdealer) {
                        view_top3_dealer.setVisibility(View.GONE);
                        hidetopdealer = false;
                    } else {
                        view_top3_dealer.setVisibility(View.VISIBLE);
                        hidetopdealer = true;
                    }
                }
        );

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if (position != 0) {
                    dealerListTam.clear();
                    dealerListTamOto.clear();
                    dealerListTamMoto.clear();
                    checkChange = false;
                    typeMap = false;
                    recAllDealer.setVisibility(View.VISIBLE);
                    mapview_top3.setVisibility(View.GONE);
                    Province province = spinnerProvinceAdapter.getItem(position);
                    districtResponses.clear();
                    imgHideDealer.setVisibility(View.INVISIBLE);
                    if (province != null && province.getId() != null) {
                        requestDistrictList(province.getId());
                        id_province1 = province.getId();
                        dealerListFull.clear();
                        District district1 = new District("District");
                        spinnerDistrictAdapter.insert(district1, 0);
                        spinnerDistrictAdapter.notifyDataSetChanged();
                        requestDealerListFull(null, province.getId(), 3);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    dealerListTam.clear();
                    dealerListTamOto.clear();
                    dealerListTamMoto.clear();
                    checkChange = false;
                    dealerListFull.clear();
                    District district = spinnerDistrictAdapter.getItem(position);
                    if (district != null) {
                        requestDealerListFull(district.getId(), id_province1, 3);
                    }
                    dealerAdapterfull.notifyDataSetChanged();
                    Log.d("viewlistfinal", "onSuccess: " + dealerListFull.size());
                }else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSetting.setOnClickListener(view1 -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        });
        return view;
    }

    private void requestDealerList(Integer id_district, Integer id_province1) {
        mProgressBarLoading.setVisibility(View.VISIBLE);
        apiService.getDealerList(
                mTypeFullAccessToken,
                id_district,
                id_province1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<DealerResponse>() {
                    @Override
                    public void onSuccess(DealerResponse dealerResponse) {
                            try {
                                if (dealerResponse != null) {
                                    dealerList.clear();
                                    dealerResponse.getData().size();
                                    for (Dealers dealers : dealerResponse.getData()) {
                                        float distance;
                                        float[] results = new float[1];
                                        if (mCurrentLocation != null) {
                                            if(dealers.getLatitude()!=null && dealers.getLongtitude()!=null){
                                                Location.distanceBetween(dealers.getLatitude(), dealers.getLongtitude(),
                                                        mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), results);
                                                dealers.setDistance(((float) Math.round(((results[0] / 1000) * 10)) / 10));
                                                dealerList.add(dealers);
                                            }else {
                                                dealers.setDistance(null);
                                                continue;
                                            }
                                        }
                                        if (dealerList.size() == 3) {
                                            break;
                                        }
                                    }
                                    Collections.sort(dealerList, new Comparator<Dealers>() {
                                        @Override
                                        public int compare(Dealers o1, Dealers o2) {
                                            return Double.compare(o1.getDistance(), o2.getDistance());
                                        }
                                    });
                                    txt_dealer_gannhat.setText(dealerList.size() + " đại lý gần nhất");
                                    dealerAdapter.notifyDataSetChanged();
                                    showMyLocation(dealerList.get(0));
                                    requestProvinceList();
                                } else {
                                    requestProvinceList();
                                    txt_dealer_gannhat.setText(dealerList.size() + " đại lý gần nhất");
                                    Log.d("onmullll_dealer11111", "onSuccess: ");
                                }
                                mProgressBarLoading.setVisibility(View.INVISIBLE);
                            } catch (Exception e) {
                                txt_dealer_gannhat.setText(dealerList.size() + " đại lý gần nhất");
                                mProgressBarLoading.setVisibility(View.INVISIBLE);
                                dealerAdapter.notifyDataSetChanged();
                                requestProvinceList();
                            };
                    }

                    @Override
                    public void onError(Throwable error) {
                        txt_dealer_gannhat.setText(dealerList.size() + " đại lý gần nhất");
                        mProgressBarLoading.setVisibility(View.INVISIBLE);
                        requestProvinceList();
                        DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));
                    }
                });
    }

    private void requestDealerListFull(Integer id_district, Integer id_province1, Integer type_Dealer) {
        showProgressDialog();
        apiService.getDealerList(
                mTypeFullAccessToken,
                id_district,
                id_province1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<DealerResponse>() {
                    @Override
                    public void onSuccess(DealerResponse dealerResponse) {
                        try {
                            dealerListFull.clear();
                            if (dealerResponse != null && dealerResponse.getData() != null) {

                                for (Dealers dealers : dealerResponse.getData()) {
                                    float[] results = new float[1];
                                    if (mCurrentLocation != null) {
                                        if(dealers.getLatitude()!=null && dealers.getLongtitude()!=null) {
                                            Location.distanceBetween(dealers.getLatitude(), dealers.getLongtitude(),
                                                    mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), results);
                                            dealers.setDistance(((float) Math.round(((results[0] / 1000) * 10)) / 10));
                                        }else{
                                            dealers.setDistance(null);
                                            continue;
                                        }
                                    }

                                    if ((type_Dealer <= 2) && (Integer.parseInt(dealers.getDealerType()) != type_Dealer)) {
                                        continue;
                                    }
                                    if ((Integer.parseInt(dealers.getDealerType()) == 1)) {
                                        dealerListTamOto.add(dealers);
                                    } else {
                                        dealerListTamMoto.add(dealers);
                                    }
                                    dealerListFull.add(dealers);
                                    txt_dealer_gannhat.setText(dealerListFull.size() + " đại lý gần nhất");
                                }
                                Collections.sort(dealerListFull, new Comparator<Dealers>() {
                                    @Override
                                    public int compare(Dealers o1, Dealers o2) {
                                        return Double.compare(o1.getDistance(), o2.getDistance());
                                    }
                                });
                                dealerAdapterfull.notifyDataSetChanged();
                                Log.d("onSuccess_dealer23232", "onSuccess: " + dealerListFull.size());
                            } else {
                                txt_dealer_gannhat.setText(dealerListFull.size() + " đại lý gần nhất");
                                dealerAdapterfull.notifyDataSetChanged();
                                Log.d("onmullll_dealer11111", "onSuccess: ");
                            }
                            hideProgressDialog();
                        } catch (Exception e) {
                            txt_dealer_gannhat.setText(dealerListFull.size() + " đại lý gần nhất");
                            dealerAdapterfull.notifyDataSetChanged();
                            hideProgressDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        dealerListFull.clear();
                        dealerAdapterfull.notifyDataSetChanged();
                        txt_dealer_gannhat.setText(dealerListFull.size() + " đại lý gần nhất");
                        hideProgressDialog();
                        DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));
                    }
                });
    }

    private void requestProvinceList() {
        apiService.getProvinceList(
                mTypeFullAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ProvinceResponse>() {
                    @Override
                    public void onSuccess(ProvinceResponse provinceResponse) {
                        if (provinceResponse.getData() != null) {
                            provinceList.clear();
                            provinceList.addAll(provinceResponse.getData());
                            Province province = new Province("City");
                            spinnerProvinceAdapter.insert(province, 0);
                            spinnerProvinceAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));
                    }
                });
    }

    private void requestDistrictList(int id) {
        districtResponses.clear();
        apiService.getDistrictListId(
                mTypeFullAccessToken,
                id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<DistrictResponse>() {

                    @Override
                    public void onSuccess(DistrictResponse districtResponse) {
                        if (districtResponse != null) {
                            Log.d("ProvinceResponse", "onSuccess: " + districtResponse.getData().size());
                            districtResponses.addAll(districtResponse.getData());
                            spinnerDistrictAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onError(Throwable error) {
                        DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));
                    }
                });
    }

    private void askPermissionsAndShowMyLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarsePermission
                    = checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFinePermission
                    = checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);


            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                    || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions,
                        REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
            }
        }


    }

    private void hideListDealers() {
        if (checkGPS()) {
            //typeMap = true;
            view_dealer_top3.setVisibility(View.VISIBLE);
            location_not_found.setVisibility(View.INVISIBLE);
            map.setVisibility(View.VISIBLE);
        } else {
            //typeMap = false;
            hideProgressDialog();
            view_dealer_top3.setVisibility(View.INVISIBLE);
            location_not_found.setVisibility(View.VISIBLE);
            map.setVisibility(View.INVISIBLE);
        }
    }

    private boolean checkGPS() {
        LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showProgressDialog() {
        DialogUtils.showDialogLoadProgress(mContext);
    }

    private void hideProgressDialog() {
        DialogUtils.hideDialogLoadProgress();
    }

    private void initComponent(View view) {
        map = view.findViewById(R.id.map);
        mapview_top3=view.findViewById(R.id.mapview_top3);
        view_dealer_top3=view.findViewById(R.id.view_dealer_top3);
        txt_dealer_gannhat = view.findViewById(R.id.txt_dealer_gannhat);
        btnSetting = view.findViewById((R.id.btn_setting));
        location_not_found = view.findViewById(R.id.location_not_found);
        view_top3_dealer = view.findViewById(R.id.view_top3_dealer);
        spCity = view.findViewById(R.id.sp_city);
        spDistrict = view.findViewById(R.id.sp_district);
        imgHideDealer = view.findViewById(R.id.ln_hide_dealer);
        recDealer = view.findViewById(R.id.rec_three_dealer);
        recAllDealer = view.findViewById(R.id.rec_all_dealer);

    }

    private void customSpinner() {
        spinnerProvinceAdapter = new SpinnerProvinceAdapter(mContext, provinceList);
        Province province = new Province("City");
        spinnerProvinceAdapter.insert(province, 0);
        spCity.setAdapter(spinnerProvinceAdapter);
        spinnerProvinceAdapter.notifyDataSetChanged();

    }

    private void customSpinnerDistrick() {
        spinnerDistrictAdapter = new SpinnerDistrictAdapter(mContext, districtResponses);
        District district = new District("District");
        spinnerDistrictAdapter.insert(district, 0);
        spDistrict.setAdapter(spinnerDistrictAdapter);
        spinnerDistrictAdapter.notifyDataSetChanged();
    }


    private void showListAllDealer() {
        recAllDealer.setLayoutManager(new LinearLayoutManager(mContext));
        dealerAdapterfull = new DealersAdapterFull(mContext, dealerListFull, this, true);
        recAllDealer.setAdapter(dealerAdapterfull);
        dealerAdapterfull.notifyDataSetChanged();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                hideProgressDialog();
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                askPermissionsAndShowMyLocation();
                hideListDealers();
                if (checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);


            }
        });
    }

    private void showMyLocation(Dealers dealer) {
        if (mCurrentLocation != null) {
            hideListDealers();

            LatLng latLng = new LatLng(dealer.getLatitude(), dealer.getLongtitude());
            LatLng latLngCamara = new LatLng(dealer.getLatitude(), dealer.getLongtitude() + 0.012);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLngCamara)
                    .zoom(15)
                    .bearing(90)
                    .tilt(40)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.custom_location, null);
                    TextView textView = (TextView) v.findViewById(R.id.txt_dealer_name);
                    TextView txt_dealer_address = v.findViewById(R.id.txt_dealer_address);
                    textView.setText(dealer.getDealerName());
                    txt_dealer_address.setText(dealer.getAddress());
                    textView.setSelected(true);
                    return v;

                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });
            mMap.setOnInfoWindowClickListener(marker -> {
                try {
                    String uri = "google.navigation:q=" + dealer.getLatitude() + "," + dealer.getLongtitude();
                    Uri gmmIntentUri = Uri.parse(uri);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } catch (Exception e) {
                    String geoUriString = "http://maps.google.com/maps?" +
                            "saddr=" + mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude() + "&daddr=" + dealer.getLatitude() + "," + dealer.getLongtitude();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUriString));
                    startActivity(intent);
                }
            });

            MarkerOptions option = new MarkerOptions();
            option.icon(BitmapDescriptorFactory.fromResource(R.drawable.market));
            option.position(latLng);
            Marker currentMarker = mMap.addMarker(option);
            currentMarker.showInfoWindow();
        } else {
            hideListDealers();
        }
    }
    @Override
    public void onClickGroup(int position, Dealers dealer) {
        showMyLocation(dealer);
    }

    @Override
    public void onClickBooking(Dealers dealers) {
        if (mNumberStepCallBack != null) {
            testDrive.setDealer(dealers);
            mNumberStepCallBack.setStepIndicator(2);
            Bundle bundle = new Bundle();
            bundle.putParcelable("testDrive", testDrive);
            Fragment toFragment = new FragmentTestDriveStep2();
            toFragment.setArguments(bundle);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().replace(R.id.frame_test_drive, toFragment).commit();
            }
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mCurrentLocation.setLatitude(location.getLatitude());
            mCurrentLocation.setLongitude(location.getLongitude());
            if (dealerList.isEmpty() && dealerAdapter.getItemCount() == Constants.ZERO) {
                requestDealerList(null, null);
            }
        }
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // stop location updates
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(mContext);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            }

            return false;
        }

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            mCurrentLocation.setLatitude(location.getLatitude());
            mCurrentLocation.setLongitude(location.getLongitude());
            requestDealerList(null, null);
        } else {
            if (dealerList == null || dealerList.isEmpty()) {
                hideListDealers();
            } else {
                hideListDealers();
            }
        }

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(mContext).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            requestPermissions(permissionsRejected.
                                                    toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mProgressBarLoading != null && mProgressBarLoading.getVisibility() != View.INVISIBLE) {
            mProgressBarLoading.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setStepIndicator(Integer step) {

    }
}