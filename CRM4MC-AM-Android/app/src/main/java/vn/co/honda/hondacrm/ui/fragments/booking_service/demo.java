//package vn.co.honda.hondacrm.ui.fragments.booking_service;
//
//import android.Manifest;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.pm.PackageManager;
//import android.location.Criteria;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.CheckBox;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//import rx.SingleSubscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//import vn.co.honda.hondacrm.R;
//import vn.co.honda.hondacrm.net.ApiService;
//import vn.co.honda.hondacrm.net.core.ApiClient;
//import vn.co.honda.hondacrm.net.model.dealer.DealerResponse;
//import vn.co.honda.hondacrm.net.model.dealer.Dealers;
//import vn.co.honda.hondacrm.net.model.dealer.District;
//import vn.co.honda.hondacrm.net.model.dealer.DistrictResponse;
//import vn.co.honda.hondacrm.net.model.dealer.Province;
//import vn.co.honda.hondacrm.net.model.dealer.ProvinceResponse;
//import vn.co.honda.hondacrm.ui.adapters.bookingservice.BookingServiceStepTwoAdapter;
//import vn.co.honda.hondacrm.ui.adapters.dealers.DealersAdapterFull;
//import vn.co.honda.hondacrm.ui.fragments.dealers.adapter.SpinnerDistrictAdapter;
//import vn.co.honda.hondacrm.ui.fragments.dealers.adapter.SpinnerProvinceAdapter;
//import vn.co.honda.hondacrm.utils.DialogUtils;
//import vn.co.honda.hondacrm.utils.PrefUtils;
//
//import static android.content.Context.LOCATION_SERVICE;
//import static android.support.v4.content.ContextCompat.checkSelfPermission;
//import static android.support.v4.content.ContextCompat.getSystemService;
//
////library
//
//public class SelectDealerFragment extends Fragment implements OnMapReadyCallback, BookingServiceStepTwoAdapter.OnClick, LocationListener {
//
//    private GoogleMap mMap;
//
//    //var
//    private static final String MYTAG = "MYTAG";
//    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
//    private Spinner spCity;
//    private Spinner spDistrict;
//    private List<String> listCity;
//    private List<String> listDistrict;
//    private LinearLayout imgHideDealer;
//    private RecyclerView recDealer;
//    private BookingServiceStepTwoAdapter dealerAdapter;
//    private RecyclerView recAllDealer;
//    ScrollView view_top3_dealer;
//    boolean hidetopdealer = true;
//    List<Dealers> dealerList;
//    List<Province> provinceList;
//    List<District> districtResponses;
//    private static List<Province> listProvince;
//    private static SpinnerProvinceAdapter spinnerProvinceAdapter;
//    private static SpinnerDistrictAdapter spinnerDistrictAdapter;
//    Boolean typeMap;
//    private int id_province1;
//    List<Dealers> dealerListFull;
//    private DealersAdapterFull dealerAdapterfull;
//
//    public static SelectDealerFragment newInstance() {
//        SelectDealerFragment fragment = new SelectDealerFragment();
//        return fragment;
//    }
//
//    //api
//    ApiService apiService;
//    String mTypeFullAccessToken;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_booking_service_step_two, null, false);
//        apiService = ApiClient.getClient(getContext()).create(ApiService.class);
//
//        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(getContext());
//
//        dealerList = new ArrayList<>();
//        dealerListFull = new ArrayList<>();
//        districtResponses = new ArrayList<>();
//        provinceList = new ArrayList<>();
//
//        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//
//        permissionsToRequest = permissionsToRequest(permissions);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (permissionsToRequest.size() > 0) {
//                requestPermissions(permissionsToRequest.toArray(
//                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
//            }
//        }
//
//        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//
//        provinceList = new ArrayList<>();
//
//
//        requestDealerList(null, null);
//        requestProvinceList();
//
//        initComponent(view);
//        showProgressDialog();
//        customSpinner();
//        customSpinnerDistrict();
//
//        recDealer.setLayoutManager(new LinearLayoutManager(getContext()));
//        dealerAdapter = new BookingServiceStepTwoAdapter(getContext(), dealerList, this, false);
//        recDealer.setAdapter(dealerAdapter);
//        dealerAdapter.notifyDataSetChanged();
//
//        imgHideDealer.setOnClickListener(v -> {
//                    if (hidetopdealer) {
//                        view_top3_dealer.setVisibility(View.GONE);
//                        hidetopdealer = false;
//                    } else {
//                        view_top3_dealer.setVisibility(View.VISIBLE);
//                        hidetopdealer = true;
//                    }
//                }
//        );
//        hideListDealers();
//
//
//        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
//                if (position != 0) {
//                    typeMap = false;
//                    showListAllDealer();
//                    recAllDealer.setVisibility(View.VISIBLE);
//                    Province province = spinnerProvinceAdapter.getItem(position-1);
//                    districtResponses.clear();
//                    requestDistrictList(province.getId());
//                    id_province1 = province.getId();
//                    Log.d("onSuccess_dealer", "onSuccess: " + province.getId());
//                    dealerListFull.clear();
//                    spinnerDistrictAdapter.notifyDataSetChanged();
//                    requestDealerListFull(null, province.getId(),3);
//                    dealerAdapterfull.notifyDataSetChanged();
//                    Log.d("viewlist", "onSuccess: " + dealerListFull.size());
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position != 0) {
//                    dealerListFull.clear();
//                    District district = spinnerDistrictAdapter.getItem(position-1);
//                    requestDealerListFull(district.getId(), id_province1,3);
//                    dealerAdapterfull.notifyDataSetChanged();
//                    Log.d("viewlistfinal", "onSuccess: " + dealerListFull.size());
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        return view;
//    }
//
//    private void initComponent(View view) {
//        view_top3_dealer = view.findViewById(R.id.view_top3_dealer);
//        spCity = view.findViewById(R.id.sp_city);
//        spDistrict = view.findViewById(R.id.sp_district);
//        imgHideDealer = view.findViewById(R.id.ln_hide_dealer);
//        recDealer = view.findViewById(R.id.rec_three_dealer);
//        recAllDealer = view.findViewById(R.id.rec_all_dealer);
//
//    }
//
//    private void requestDealerListFull(Integer id_district, Integer id_province1,Integer type_Dealer) {
//        dealerListFull.clear();
//        apiService.getDealerList(
//                mTypeFullAccessToken,
//                id_district,
//                id_province1
//        )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleSubscriber<DealerResponse>() {
//                    @Override
//                    public void onSuccess(DealerResponse dealerResponse) {
//                        try {
//                            if (dealerResponse != null) {
//                                dealerResponse.getData().size();
//                                Location myLoca = new Location("");
//                                myLoca.setLatitude(21.038107);
//                                myLoca.setLongitude(105.800898);
//                                for (Dealers dealers : dealerResponse.getData()) {
//                                    float[] results = new float[1];
//                                    Location.distanceBetween(Double.parseDouble(dealers.getLatitude()), Double.parseDouble(dealers.getLongtitude()),
//                                            myLoca.getLatitude(), myLoca.getLongitude(), results);
//                                    dealers.setDistance(((float) Math.round(((results[0] / 1000) * 10)) / 10));
//                                    if ((type_Dealer<=2)&&(Integer.parseInt(dealers.getDealerType()) != type_Dealer)) {
//                                        continue;
//                                    }
//                                    dealerListFull.add(dealers);
//                                }
//                                Collections.sort(dealerListFull, new Comparator<Dealers>() {
//                                    @Override
//                                    public int compare(Dealers o1, Dealers o2) {
//                                        return Double.compare(o1.getDistance(), o2.getDistance());
//                                    }
//                                });
//                                dealerAdapterfull.notifyDataSetChanged();
//                                Log.d("onSuccess_dealer23232", "onSuccess: " + dealerListFull.size());
//                            } else {
//                                Log.d("onmullll_dealer11111", "onSuccess: ");
//                            }
//                        } catch (Exception e) {
//                            dealerListFull.clear();
//                            dealerAdapterfull.notifyDataSetChanged();
//                        }
//                        ;
//
//                    }
//
//                    @Override
//                    public void onError(Throwable error) {
//                        Log.d("error", "errordealer: " + error.getMessage());
//                    }
//                });
//    }
//
//    private void requestDealerList(Integer id_district, Integer id_province1) {
//        dealerList.clear();
//
//        apiService.getDealerList(
//                mTypeFullAccessToken,
//                id_district,
//                id_province1
//        )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleSubscriber<DealerResponse>() {
//                    @Override
//                    public void onSuccess(DealerResponse dealerResponse) {
//                        try {
//                            if (dealerResponse != null) {
//                                dealerResponse.getData().size();
//                                Location myLoca = getMyLocation();
//                                for (Dealers dealers : dealerResponse.getData()) {
//                                    float distance;
//                                    float[] results = new float[1];
//                                    Location.distanceBetween(Double.parseDouble(dealers.getLatitude()), Double.parseDouble(dealers.getLongtitude()),
//                                            myLoca.getLatitude(), myLoca.getLongitude(), results);
//                                    dealers.setDistance(((float) Math.round(((results[0] / 1000) * 10)) / 10));
//                                    dealerList.add(dealers);
//                                    if (dealerList.size() == 3) {
//                                        break;
//                                    }
//                                }
//                                Collections.sort(dealerList, new Comparator<Dealers>() {
//                                    @Override
//                                    public int compare(Dealers o1, Dealers o2) {
//                                        return Double.compare(o1.getDistance(), o2.getDistance());
//                                    }
//                                });
//                                for (Dealers dealers : dealerList) {
//                                    Log.d("ttt", dealers.getDistance() + "");
//                                }
//                                dealerAdapter.notifyDataSetChanged();
//                            } else {
//                                Log.d("onmullll_dealer11111", "onSuccess: ");
//                            }
//                        } catch (Exception e) {
//                            dealerList.clear();
//                            dealerAdapter.notifyDataSetChanged();
//                        }
//                        ;
//
//                    }
//
//                    @Override
//                    public void onError(Throwable error) {
//                        Log.d("error", "errordealer: " + error.getMessage());
//                    }
//                });
//    }
//
//    private void requestDistrictList(int id) {
//        districtResponses.clear();
//
//        apiService.getDistrictListId(
//                mTypeFullAccessToken,
//                id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleSubscriber<DistrictResponse>() {
//
//                    @Override
//                    public void onSuccess(DistrictResponse districtResponse) {
//                        if (districtResponse != null) {
//                            Log.d("ProvinceResponse", "onSuccess: " + districtResponse.getData().size());
//                            districtResponses.addAll(districtResponse.getData());
//                            spinnerDistrictAdapter.notifyDataSetChanged();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable error) {
//                    }
//                });
//    }
//
//
//    private void requestProvinceList() {
//        apiService.getProvinceList(
//                mTypeFullAccessToken)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleSubscriber<ProvinceResponse>() {
//                    @Override
//                    public void onSuccess(ProvinceResponse provinceResponse) {
//                        Log.d("ProvinceResponse", "onSuccess: " + provinceResponse.getData().size());
//                        provinceList.addAll(provinceResponse.getData());
//                        spinnerProvinceAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onError(Throwable error) {
//                    }
//                });
//    }
//
//
//
//    private void askPermissionsAndShowMyLocation() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            int accessCoarsePermission
//                    = checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
//            int accessFinePermission
//                    = checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
//
//
//            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
//                    || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
//                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION};
//                requestPermissions(permissions,
//                        REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
//            }
//        }
//
//
//    }
//
//
//
//    private void hideListDealers() {
//        if (checkGPS()) {
//
//        } else {
//            showProgressDialog();
//            showListAllDealer();
//            recAllDealer.setVisibility(View.VISIBLE);
//        }
//
//    }
//
//    private boolean checkGPS() {
//        LocationManager manager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
//        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        return statusOfGPS;
//    }
//
//    private void hideProgressDialog() {
//        DialogUtils.hideDialogLoadProgress();
//    }
//
//    private void showProgressDialog() {
//        DialogUtils.showDialogLoadProgress(getContext());
//    }
//
//    private void customSpinner() {
//        spinnerProvinceAdapter = new SpinnerProvinceAdapter(getContext(), provinceList);
//        spinnerProvinceAdapter.notifyDataSetChanged();
//        spCity.setAdapter(spinnerProvinceAdapter);
//
//    }
//
//    private void customSpinnerDistrict() {
//
//        spinnerDistrictAdapter = new SpinnerDistrictAdapter(getContext(), districtResponses);
//        spinnerDistrictAdapter.notifyDataSetChanged();
//        spDistrict.setAdapter(spinnerDistrictAdapter);
//    }
//
//
//    private void showListAllDealer() {
//        recAllDealer.setVisibility(View.VISIBLE);
//        recAllDealer.setLayoutManager(new LinearLayoutManager(getContext()));
//        dealerAdapter = new BookingServiceStepTwoAdapter(getContext(), dealerList, this, true);
//        Log.d("vvvv", ""+dealerList.size());
//        recAllDealer.setAdapter(dealerAdapter);
//        hideProgressDialog();
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
//            @Override
//            public void onMapLoaded() {
//                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                mMap.getUiSettings().setZoomControlsEnabled(true);
//                askPermissionsAndShowMyLocation();
//                if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                showSelectedDealer(dealerList.get(0));
//                mMap.setMyLocationEnabled(true);
//
//
//            }
//        });
//    }
//
//
//    //fuction give below
//    private String getEnabledLocationProvider() {
//        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String bestProvider = locationManager.getBestProvider(criteria, true);
//
//        boolean enabled = locationManager.isProviderEnabled(bestProvider);
//
//        if (!enabled) {
//            Toast.makeText(getContext(), "No location provider enabled!", Toast.LENGTH_LONG).show();
//            Log.i(MYTAG, "No location provider enabled!");
//            return null;
//        }
//        return bestProvider;
//    }
//
//    private Location getMyLocation() {
//
//
//        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
//
//        String locationProvider = this.getEnabledLocationProvider();
//
//        if (locationProvider == null) {
//
//        }
//
//        final long MIN_TIME_BW_UPDATES = 1000;
//
//        final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
//
//        Location myLocation = null;
//        try {
//
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
//                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//
//            myLocation = locationManager
//                    .getLastKnownLocation(locationProvider);
//            hideProgressDialog();
//        } catch (SecurityException e) {
//            Toast.makeText(getContext(), "Show My Location error                                                                                                                                                                                                                                           on Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//            Log.e(MYTAG, "Show My Location Error:" + e.getMessage());
//            e.printStackTrace();
//
//        }
//
//
//        return myLocation;
//    }
//
//
//    private void showSelectedDealer(Dealers dealer) {
//
//        if (getMyLocation() != null) {
//
//            LatLng latLng = new LatLng(Double.parseDouble(dealer.getLatitude()), Double.parseDouble(dealer.getLongtitude()));
//            LatLng latLngCamara = new LatLng(Double.parseDouble(dealer.getLatitude()), Double.parseDouble(dealer.getLongtitude()) + 0.011);
//
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(latLngCamara)
//                    .zoom(15)
//                    .bearing(90)
//                    .tilt(40)
//                    .build();
//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//                @Override
//                public View getInfoWindow(Marker marker) {
//                    View v = getLayoutInflater().inflate(R.layout.custom_location, null);
//                    TextView name = (TextView) v.findViewById(R.id.txt_dealer_name);
//                    TextView address = v.findViewById(R.id.txt_dealer_address);
//                    address.setText(dealer.getAddress());
//                    name.setText(dealer.getDealerName());
//                    name.setSelected(true);
//                    return v;
//
//                }
//
//                @Override
//                public View getInfoContents(Marker marker) {
//                    return null;
//                }
//            });
//
//            MarkerOptions option = new MarkerOptions();
//            option.icon(BitmapDescriptorFactory.fromResource(R.drawable.market));
//            option.position(latLng);
//            Marker currentMarker = mMap.addMarker(option);
//            currentMarker.showInfoWindow();
//        } else {
//            Toast.makeText(getContext(), "Location not found!", Toast.LENGTH_LONG).show();
//            Log.i(MYTAG, "Location not found");
//        }
//    }
//
//
//    @Override
//    public void onClickDealer(Dealers dealer) {
//        showSelectedDealer(dealer);
//    }
//
//    @Override
//    public void onClickSelectDealer(Float distance) {
//        Fragment fragment = new SelectTimeSlotFragment();
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_booking_service, fragment);
//        fragmentTransaction.commit();
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        Log.d("kiemtra", "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
//
//    }
//
//    @Override
//    public void onStatusChanged(String s, int i, Bundle bundle) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String s) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String s) {
//
//    }
//
//    private ArrayList<String> permissionsToRequest;
//    private ArrayList<String> permissionsRejected = new ArrayList<>();
//    private ArrayList<String> permissions = new ArrayList<>();
//    // integer for permissions results request
//    private static final int ALL_PERMISSIONS_RESULT = 1011;
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case ALL_PERMISSIONS_RESULT:
//                for (String perm : permissionsToRequest) {
//                    if (!hasPermission(perm)) {
//                        permissionsRejected.add(perm);
//                    }
//                }
//
//                if (permissionsRejected.size() > 0) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
//                            new AlertDialog.Builder(getActivity()).
//                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
//                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                requestPermissions(permissionsRejected.
//                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
//                                            }
//                                        }
//                                    }).setNegativeButton("Cancel", null).create().show();
//
//                            return;
//                        }
//                    }
//                } else {
//                }
//
//                break;
//        }
//    }
//
//    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
//        ArrayList<String> result = new ArrayList<>();
//
//        for (String perm : wantedPermissions) {
//            if (!hasPermission(perm)) {
//                result.add(perm);
//            }
//        }
//
//        return result;
//    }
//
//    private boolean hasPermission(String permission) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
//        }
//        return true;
//    }
//
//}