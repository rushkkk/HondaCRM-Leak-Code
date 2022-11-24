package vn.co.honda.hondacrm.ui.fragments.booking_service.views;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


import vn.co.honda.hondacrm.ui.activities.booking_service.BookingServiceActivity;
import vn.co.honda.hondacrm.ui.activities.booking_service.INumberStepCallBack;
import vn.co.honda.hondacrm.ui.adapters.bookingservice.BookingServiceStepTwoAdapter;
import vn.co.honda.hondacrm.ui.adapters.bookingservice.BookingServiceStepTwoAdapterFull;
import vn.co.honda.hondacrm.ui.fragments.booking_service.models.BookService;
import vn.co.honda.hondacrm.ui.fragments.dealers.adapter.SpinnerDistrictAdapter;
import vn.co.honda.hondacrm.ui.fragments.dealers.adapter.SpinnerProvinceAdapter;
import vn.co.honda.hondacrm.utils.PrefUtils;

import static android.content.Context.LOCATION_SERVICE;

//library

public class SelectDealerFragment extends Fragment implements OnMapReadyCallback, BookingServiceStepTwoAdapter.OnClick, LocationListener, BookingServiceStepTwoAdapterFull.OnClick{

    private GoogleMap mMap;

    //var
    private static final String MYTAG = "MYTAG";
    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    private Spinner spCity;
    private Spinner spDistrict;
    private LinearLayout imgHideDealer;
    private RecyclerView recDealer;
    private BookingServiceStepTwoAdapter dealerAdapter;
    private BookingServiceStepTwoAdapterFull dealerAdapterfull;
    private RecyclerView recAllDealer;
    ScrollView view_top3_dealer;
    boolean hidetopdealer = true;
    List<Dealers> dealerList;
    List<Dealers> dealerListTam;
    List<Dealers> dealerListTamMoto;
    List<Dealers> dealerListTamOto;
    List<Dealers> dealerListFull;
    List<Province> provinceList;
    List<District> districtResponses;
    CheckBox elect_dealer_oto, elect_dealer_moto;
    private static SpinnerProvinceAdapter spinnerProvinceAdapter;
    private static SpinnerDistrictAdapter spinnerDistrictAdapter;
    private int id_province1;
    private int dealerType;
    private String vin;
    View location_not_found;
    Boolean typeMap;
    View map;
    RelativeLayout topMenu;
    boolean checkChange=false;
    private INumberStepCallBack mNumberStepCallBack;
    private BookService bookService = new BookService();


    public static SelectDealerFragment newInstance() {
        SelectDealerFragment fragment = new SelectDealerFragment();
        return fragment;
    }

    //api
    ApiService apiService;
    String mTypeFullAccessToken;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookingServiceActivity) {
            this.mNumberStepCallBack = (BookingServiceActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_service_step_two, container, false);

        if (getArguments() != null){
            dealerType = getArguments().getInt("VehicleType");
            vin = getArguments().getString("vin");
        }
        bookService.setVin(vin);
        apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(getContext());
        dealerList = new ArrayList<>();
        dealerListTamMoto= new ArrayList<>();
        dealerListTamOto= new ArrayList<>();
        dealerListTam= new ArrayList<>();
        dealerListFull = new ArrayList<>();
        districtResponses = new ArrayList<>();

        provinceList = new ArrayList<>();
        requestDealerList(null, null, dealerType);
        requestProvinceList();
        initComponent(view);
        showProgressDialog();
        customSpinner();
        customSpinnerDistrick();
        hideListDealers();


        //map
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //set onclick item
        recDealer.setLayoutManager(new LinearLayoutManager(getContext()));
        dealerAdapter = new BookingServiceStepTwoAdapter(getContext(), dealerList, this, false);
        recDealer.setAdapter(dealerAdapter);
        dealerAdapter.notifyDataSetChanged();

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
                    map.setVisibility(View.INVISIBLE);
                    showListAllDealer();
                    recAllDealer.setVisibility(View.VISIBLE);
                    Province province = spinnerProvinceAdapter.getItem(position);
                    districtResponses.clear();
                    requestDistrictList(province.getId());
                    id_province1 = province.getId();
                    dealerListFull.clear();
                    District district1= new District("District");
                    spinnerDistrictAdapter.insert(district1,0);
                    spinnerDistrictAdapter.notifyDataSetChanged();
                    requestDealerListFull(null, province.getId(),3);
                    dealerAdapterfull.notifyDataSetChanged();

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
                    map.setVisibility(View.INVISIBLE);
                    dealerListFull.clear();
                    District district = spinnerDistrictAdapter.getItem(position);
                    requestDealerListFull(district.getId(), id_province1,dealerType);
                    dealerAdapterfull.notifyDataSetChanged();
                    Log.d("viewlistfinal", "onSuccess: " + dealerListFull.size());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }


    private void requestDealerList(Integer id_district, Integer id_province1 ,Integer type_Dealer) {
        dealerList.clear();
        showProgressDialog();
        apiService.getDealerList(
                mTypeFullAccessToken,
                id_district,
                id_province1
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<DealerResponse>() {
                    @Override
                    public void onSuccess(DealerResponse dealerResponse) {
                        try {
                            if (dealerResponse != null) {
                                dealerResponse.getData().size();
                                Location myLoca = new Location("");
                                myLoca.setLatitude(21.038107);
                                myLoca.setLongitude(105.800898);
                                for (Dealers dealers : dealerResponse.getData()) {
                                    float distance;
                                    float[] results = new float[1];
                                    Location.distanceBetween(dealers.getLatitude(), dealers.getLongtitude(),
                                            myLoca.getLatitude(), myLoca.getLongitude(), results);
                                    dealers.setDistance(((float) Math.round(((results[0] / 1000) * 10)) / 10));
                                    if ((type_Dealer<=2)&&(Integer.parseInt(dealers.getDealerType()) != type_Dealer)) {
                                        continue;
                                    }
                                    dealerList.add(dealers);
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
                                for (Dealers dealers : dealerList) {
                                    Log.d("ttt", dealers.getDistance() + "");
                                }
                                dealerAdapter.notifyDataSetChanged();
                            } else {
                                Log.d("onmullll_dealer11111", "onSuccess: ");
                            }
                        } catch (Exception e) {
                            dealerList.clear();
                            dealerAdapter.notifyDataSetChanged();
                        }
                        ;

                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.d("error", "errordealer: " + error.getMessage());
                    }
                });
        hideProgressDialog();
    }

    private void requestDealerListFull(Integer id_district, Integer id_province1,Integer type_Dealer) {
        showProgressDialog();
        dealerListFull.clear();
        apiService.getDealerList(
                mTypeFullAccessToken,
                id_district,
                id_province1
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<DealerResponse>() {
                    @Override
                    public void onSuccess(DealerResponse dealerResponse) {
                        try {
                            if (dealerResponse != null) {
                                dealerResponse.getData().size();
                                Location myLoca = new Location("");
                                myLoca.setLatitude(21.038107);
                                myLoca.setLongitude(105.800898);
                                for (Dealers dealers : dealerResponse.getData()) {
                                    float[] results = new float[1];
                                    Location.distanceBetween(dealers.getLatitude(), dealers.getLongtitude(),
                                            myLoca.getLatitude(), myLoca.getLongitude(), results);
                                    dealers.setDistance(((float) Math.round(((results[0] / 1000) * 10)) / 10));
                                    if ((type_Dealer<=2)&&(Integer.parseInt(dealers.getDealerType()) != type_Dealer)) {
                                        continue;
                                    }
                                    dealerListFull.add(dealers);
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
                                Log.d("onmullll_dealer11111", "onSuccess: ");
                            }
                        } catch (Exception e) {
                            dealerListFull.clear();
                            dealerAdapterfull.notifyDataSetChanged();
                        }
                        ;

                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.d("error", "errordealer: " + error.getMessage());
                    }
                });
        hideProgressDialog();
    }

    private void requestProvinceList() {
        showProgressDialog();
        provinceList.clear();
        apiService.getProvinceList(
                mTypeFullAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ProvinceResponse>() {
                    @Override
                    public void onSuccess(ProvinceResponse provinceResponse) {
                        Log.d("ProvinceResponse", "onSuccess: " + provinceResponse.getData().size());
                        provinceList.addAll(provinceResponse.getData());
                        spinnerProvinceAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable error) {
                    }
                });
        hideProgressDialog();
    }

    private void requestDistrictList(int id) {
        showProgressDialog();
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
                    }
                });
        hideProgressDialog();
    }


    private void askPermissionsAndShowMyLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarsePermission
                    = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFinePermission
                    = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);


            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                    || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions,
                        REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);

                return;
            }
        }


    }

    private void hideListDealers() {
        if (checkGPS()) {
            typeMap = true;
            location_not_found.setVisibility(View.INVISIBLE);
            topMenu.setVisibility(View.VISIBLE);
            map.setVisibility(View.VISIBLE);
        } else {
            typeMap = false;
            hideProgressDialog();
            location_not_found.setVisibility(View.VISIBLE);
            topMenu.setVisibility(View.GONE);
            map.setVisibility(View.INVISIBLE);
        }

    }

    private boolean checkGPS() {
        LocationManager manager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return statusOfGPS;
    }

    private void showProgressDialog() {
    }

    private void hideProgressDialog() {
    }

    private void initComponent(View view) {
        map=view.findViewById(R.id.map);
        location_not_found=view.findViewById(R.id.location_not_found);
        elect_dealer_oto = view.findViewById(R.id.elect_dealer_oto);
        elect_dealer_moto = view.findViewById(R.id.elect_dealer_moto);
        view_top3_dealer = view.findViewById(R.id.view_top3_dealer);
        spCity = view.findViewById(R.id.sp_city);
        spDistrict = view.findViewById(R.id.sp_district);
        imgHideDealer = view.findViewById(R.id.ln_hide_dealer);
        recDealer = view.findViewById(R.id.rec_three_dealer);
        recAllDealer = view.findViewById(R.id.rec_all_dealer);
        topMenu = view.findViewById(R.id.ln_top_menu);

    }

    private void customSpinner() {
        spinnerProvinceAdapter = new SpinnerProvinceAdapter(getContext(), provinceList);
        Province province= new Province("City");
        spinnerProvinceAdapter.insert(province,0);
        spinnerProvinceAdapter.notifyDataSetChanged();
        spCity.setAdapter(spinnerProvinceAdapter);

    }

    private void customSpinnerDistrick() {

        spinnerDistrictAdapter = new SpinnerDistrictAdapter(getContext(), districtResponses);
        District district= new District("District");
        spinnerDistrictAdapter.insert(district,0);
        spinnerDistrictAdapter.notifyDataSetChanged();
        spDistrict.setAdapter(spinnerDistrictAdapter);
    }


    private void showListAllDealer() {
        dealerListFull.clear();
        recAllDealer.setLayoutManager(new LinearLayoutManager(getContext()));
        dealerAdapterfull = new BookingServiceStepTwoAdapterFull(getContext(), dealerListFull, this, true);
        recAllDealer.setAdapter(dealerAdapterfull);
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
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if(dealerList != null && !dealerList.isEmpty()){
                    showSelectedDealer(dealerList.get(0));
                }
                else {
                    Toast.makeText(getContext(), "Dealer not found", Toast.LENGTH_LONG).show();
                }

                mMap.setMyLocationEnabled(true);


            }
        });
    }

    @Override
    public void onClickDealerThreeItem(Dealers dealer) {
        showSelectedDealer(dealer);
    }


    @Override
    public void onClickDealer(Dealers dealers) {
        if (mNumberStepCallBack != null) {
            bookService.setDealer(dealers);
            mNumberStepCallBack.setStepIndicator(3);
            Bundle bundle = new Bundle();
             bundle.putSerializable("bookService", bookService);
            Fragment toFragment = new SelectTimeSlotFragment();
            toFragment.setArguments(bundle);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().replace(R.id.frame_booking_service, toFragment).commit();
            }
        }
    }

    @Override
    public void onClickSelectDealer(Dealers dealers) {
        if (mNumberStepCallBack != null) {
            bookService.setDealer(dealers);
            mNumberStepCallBack.setStepIndicator(3);
            Bundle bundle = new Bundle();
            bundle.putSerializable("bookService", bookService);
            Fragment toFragment = new SelectTimeSlotFragment();
            toFragment.setArguments(bundle);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().replace(R.id.frame_booking_service, toFragment).commit();
            }
        }

    }

    //fuction give below
    private String getEnabledLocationProvider() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);

        boolean enabled = locationManager.isProviderEnabled(bestProvider);

        if (!enabled) {
            Toast.makeText(getContext(), "No location provider enabled!", Toast.LENGTH_LONG).show();
            Log.i(MYTAG, "No location provider enabled!");
            return null;
        }
        return bestProvider;
    }

    private Location getMyLocation() {


        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        String locationProvider = this.getEnabledLocationProvider();

        if (locationProvider == null) {

        }

        final long MIN_TIME_BW_UPDATES = 1000;

        final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

        Location myLocation = null;
        try {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            myLocation = locationManager
                    .getLastKnownLocation(locationProvider);
            hideProgressDialog();
        } catch (SecurityException e) {
            Toast.makeText(getContext(), "Show My Location error                                                                                                                                                                                                                                           on Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(MYTAG, "Show My Location Error:" + e.getMessage());
            e.printStackTrace();

        }


        return myLocation;
    }

    private void showSelectedDealer(Dealers dealer) {



        LatLng latLng = new LatLng(dealer.getLatitude(),dealer.getLongtitude());
        LatLng latLngCamara = new LatLng(dealer.getLatitude(), dealer.getLongtitude() + 0.009);

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
                TextView name = (TextView) v.findViewById(R.id.txt_dealer_name);
                TextView address = v.findViewById(R.id.txt_dealer_address);
                address.setText(dealer.getAddress());
                name.setText(dealer.getDealerName());
                name.setSelected(true);
                return v;

            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        MarkerOptions option = new MarkerOptions();
        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.market));
        option.position(latLng);
        Marker currentMarker = mMap.addMarker(option);
        currentMarker.showInfoWindow();

    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}