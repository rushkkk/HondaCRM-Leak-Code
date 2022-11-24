//package vn.co.honda.hondacrm.ui.activities.Dealer;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.location.Criteria;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Build;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.LinearLayout;
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
//import java.util.List;
//
//import vn.co.honda.hondacrm.R;
//import vn.co.honda.hondacrm.ui.activities.Dealer.model.Dealer;
//import vn.co.honda.hondacrm.ui.adapters.dealers.DealersAdapter;
//import vn.co.honda.hondacrm.utils.DialogUtils;
//
//public class DealerFragment extends AppCompatActivity implements LocationListener, DealersAdapter.OnClick {
//  private GoogleMap myMap;
//  private static final String MYTAG = "MYTAG";
//  public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
//  private Spinner spCity;
//  private Spinner spDistrict;
//  private List<String> listCity;
//  private List<String> listDistrict;
//  private LinearLayout imgHideDealer;
//  private RecyclerView recDealer;
//  private DealersAdapter dealerAdapter;
//  private RecyclerView recAllDealer;
//
//  @Override
//  protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setContentView(R.layout.fragment_booking_service_step_two);
//    initComponent();
//    showProgressDialog();
//    customSpinner();
//
//    SupportMapFragment mapFragment
//        = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
//
//    mapFragment.getMapAsync(googleMap -> onMyMapReady(googleMap));
//
//    recDealer.setLayoutManager(new LinearLayoutManager(this));
//    dealerAdapter = new DealersAdapter(this, listDealer(), this);
//    recDealer.setAdapter(dealerAdapter);
//
//    imgHideDealer.setOnClickListener(v -> recDealer.setVisibility(View.GONE));
//
//    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//      @Override
//      public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
//        if(position != 0){
//         // hideProgressDialog();
//          showListAllDealer();
//          recAllDealer.setVisibility(View.VISIBLE);
//        }
//
//      }
//
//      @Override
//      public void onNothingSelected(AdapterView<?> adapterView) {
//
//      }
//    });
//  }
//
//  private void showProgressDialog() {
//  }
//
//  private void customSpinner() {
//    listCity = new ArrayList<>();
//    listCity.add("City");
//    listCity.add("Ha Noi");
//    listCity.add("Ho Chi Minh");
//    listCity.add("Da Nang");
//    listCity.add("Hue");
//
//    listDistrict = new ArrayList<>();
//
//    listDistrict.add("District");
//    listDistrict.add("Phu Loc");
//    listDistrict.add("Hoa Mai");
//    listDistrict.add("Binh Minh");
//
//    ArrayAdapter adapterCity = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listCity);
//    adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//    spCity.setAdapter(adapterCity);
//
//    ArrayAdapter adapterDistrict = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listDistrict);
//    adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//    spDistrict.setAdapter(adapterDistrict);
//
//  }
//
//  private void initComponent() {
//    spCity = findViewById(R.id.sp_city);
//    spDistrict = findViewById(R.id.sp_district);
//    imgHideDealer = findViewById(R.id.ln_hide_dealer);
//    recDealer = findViewById(R.id.rec_three_dealer);
//    recAllDealer = findViewById(R.id.rec_all_dealer);
//
//  }
//
//  private List<Dealer> listDealer() {
//    List<Dealer> list = new ArrayList<>();
//    list.add(new Dealer("Mỹ Đình", "0965845544", 100, "17:00", "08:00", 100, 0, "4 Hoàng Diệu", 21.038107, 105.800898));
//    list.add(new Dealer("Giải Phóng", "0965845544", 100, "17:00", "08:00", 100, 0, "8 Lê Quang Đạo",20.994818, 105.790189));
//    list.add(new Dealer("Mỹ Đình", "0965845544", 100, "17:00", "08:00", 100, 0, "104 Châu Thị Vĩnh Tế", 21.011396, 105.837379));
//    return list;
//  }
//
//  private void showListAllDealer(){
//    List<Dealer> listAllDealer = new ArrayList<>();
//    listAllDealer.add(new Dealer("Mỹ Đình", "0965845544", 100, "17:00", "08:00", 100, 0, "4 Hoàng Diệu", 21.038107, 105.800898));
//    listAllDealer.add(new Dealer("Giải Phóng", "0965845544", 100, "17:00", "08:00", 100, 0, "8 Lê Quang Đạo",20.994818, 105.790189));
//    listAllDealer.add(new Dealer("Mỹ Đình", "0965845544", 100, "17:00", "08:00", 100, 0, "104 Châu Thị Vĩnh Tế", 21.011396, 105.837379));
//    listAllDealer.add(new Dealer("Mỹ Đình", "0965845544", 100, "17:00", "08:00", 100, 0, "4 Hoàng Diệu", 21.038107, 105.800898));
//    listAllDealer.add(new Dealer("Giải Phóng", "0965845544", 100, "17:00", "08:00", 100, 0, "8 Lê Quang Đạo",20.994818, 105.790189));
//    listAllDealer.add(new Dealer("Mỹ Đình", "0965845544", 100, "17:00", "08:00", 100, 0, "104 Châu Thị Vĩnh Tế", 21.011396, 105.837379));
//
//    recAllDealer.setLayoutManager(new LinearLayoutManager(this));
//    dealerAdapter = new DealersAdapter(this, listAllDealer, this);
//    recAllDealer.setAdapter(dealerAdapter);
//  }
////  private void hideProgressDialog() {
////    DialogUtils.hideDialogLoadProgress();
////  }
////
////  private void showProgressDialog() {
////    DialogUtils.showDialogLoadProgress(DealerFragment.this);
////  }
//
//  private void onMyMapReady(GoogleMap googleMap) {
//    myMap = googleMap;
//    myMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
//
//      @Override
//      public void onMapLoaded() {
//       // hideProgressDialog();
//        askPermissionsAndShowMyLocation();
//      }
//    });
//    myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//    myMap.getUiSettings().setZoomControlsEnabled(true);
//    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//      return;
//    }
//    myMap.setMyLocationEnabled(true);
//  }
//
//  private void askPermissionsAndShowMyLocation() {
//    if (Build.VERSION.SDK_INT >= 23) {
//      int accessCoarsePermission
//          = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
//      int accessFinePermission
//          = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//
//
//      if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
//          || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
//        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION};
//        ActivityCompat.requestPermissions(this, permissions,
//            REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
//
//        return;
//      }
//    }
//    Dealer dealer = new Dealer("Mỹ Đình", "0965845544", 100, "17:00", "08:00", 100, 0, "4 Hoàng Diệu", 21.038107, 105.800898);
//    this.showMyLocation(dealer);
//  }
//
//  @Override
//  public void onRequestPermissionsResult(int requestCode,
//                                         String permissions[], int[] grantResults) {
//
//    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    //
//    switch (requestCode) {
//      case REQUEST_ID_ACCESS_COURSE_FINE_LOCATION: {
//        if (grantResults.length > 1
//            && grantResults[0] == PackageManager.PERMISSION_GRANTED
//            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//
//          Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();
//          Dealer dealer = new Dealer("Mỹ Đình", "0965845544", 100, "17:00", "08:00", 100, 0, "4 Hoàng Diệu", 21.038107, 105.800898);
//          this.showMyLocation(dealer);
//        } else {
//          Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
//        }
//        break;
//      }
//    }
//  }
//
//  private String getEnabledLocationProvider() {
//    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//    Criteria criteria = new Criteria();
//    String bestProvider = locationManager.getBestProvider(criteria, true);
//
//    boolean enabled = locationManager.isProviderEnabled(bestProvider);
//
//    if (!enabled) {
//      Toast.makeText(this, "No location provider enabled!", Toast.LENGTH_LONG).show();
//      Log.i(MYTAG, "No location provider enabled!");
//      return null;
//    }
//    return bestProvider;
//  }
//
//  private void showMyLocation(Dealer dealer) {
//
//    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//    String locationProvider = this.getEnabledLocationProvider();
//
//    if (locationProvider == null) {
//      return;
//    }
//
//    final long MIN_TIME_BW_UPDATES = 1000;
//
//    final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
//
//    Location myLocation = null;
//    try {
//
//      locationManager.requestLocationUpdates(
//          locationProvider,
//          MIN_TIME_BW_UPDATES,
//          MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
//      myLocation = locationManager
//          .getLastKnownLocation(locationProvider);
//    } catch (SecurityException e) {
//      Toast.makeText(this, "Show My Location Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//      Log.e(MYTAG, "Show My Location Error:" + e.getMessage());
//      e.printStackTrace();
//      return;
//    }
//
//    if (myLocation != null) {
////      LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
//      LatLng latLng = new LatLng(dealer.getLatitudep(),  dealer.getLongitude());
//      LatLng latLngCamara = new LatLng(dealer.getLatitudep(),  dealer.getLongitude() + 0.012);
//      Log.d("location1", myLocation.getLatitude() + "");
//      myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
//      CameraPosition cameraPosition = new CameraPosition.Builder()
//          .target(latLngCamara)
//          .zoom(15)
//          .bearing(90)
//          .tilt(40)
//          .build();
//      myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//      myMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//        @Override
//        public View getInfoWindow(Marker marker) {
//          View v = getLayoutInflater().inflate(R.layout.custom_location, null);
//          TextView textView = (TextView) v.findViewById(R.id.txt_dealer_name);
//          textView.setSelected(true);
//          return v;
//
//        }
//
//        @Override
//        public View getInfoContents(Marker marker) {
//          return null;
//        }
//      });
//
//      MarkerOptions option = new MarkerOptions();
//      option.icon(BitmapDescriptorFactory.fromResource(R.drawable.market));
//      option.position(latLng);
//      Marker currentMarker = myMap.addMarker(option);
//      currentMarker.showInfoWindow();
//    } else {
//      Toast.makeText(this, "Location not found!", Toast.LENGTH_LONG).show();
//      Log.i(MYTAG, "Location not found");
//    }
//
//
//  }
//
//  @Override
//  public void onLocationChanged(Location location) {
//
//  }
//
//  @Override
//  public void onStatusChanged(String provider, int status, Bundle extras) {
//
//  }
//
//  @Override
//  public void onProviderEnabled(String provider) {
//
//  }
//
//  @Override
//  public void onProviderDisabled(String provider) {
//
//  }
//
//  @Override
//  public void onMapReady(GoogleMap googleMap) {
//
//  }
//
//  @Override
//  public void onClickGroup(int position, Dealer dealer) {
//    showMyLocation(dealer);
//  }
//}
