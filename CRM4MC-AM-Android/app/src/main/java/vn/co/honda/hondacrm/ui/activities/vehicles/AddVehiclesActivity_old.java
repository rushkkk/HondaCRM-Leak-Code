//package vn.co.honda.hondacrm.ui.activities.vehicles;
//
//import android.os.Bundle;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import vn.co.honda.hondacrm.R;
//import vn.co.honda.hondacrm.ui.activities.BaseActivity;
//import vn.co.honda.hondacrm.ui.fragments.vehicles.FragmentAddVehicleManual;
//import vn.co.honda.hondacrm.ui.fragments.vehicles.FragmentAddVehicleNFC;
//
//public class AddVehiclesActivity_old extends BaseActivity {
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
//    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_vehicle);
//        setTitleHeader(getString(R.string.lb_add_vehicle_title));
//        setupViewPager();
//    }
//
//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }
//
//    private void setupViewPager() {
//        viewPager = findViewById(R.id.viewpager);
//        tabLayout = findViewById(R.id.tabs);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        tabLayout.setupWithViewPager(viewPager);
//        FragmentAddVehicleNFC fragmentAddVehicleNFC = new FragmentAddVehicleNFC();
//        adapter.addFragment(fragmentAddVehicleNFC, "Using NFC");
//        adapter.addFragment(new FragmentAddVehicleManual(), "Input Manual");
//        viewPager.setAdapter(adapter);
//    }
//}
