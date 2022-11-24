//package vn.co.honda.hondacrm.ui.fragments.connected.views;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//
//import java.util.ArrayList;
//
//import vn.co.honda.hondacrm.R;
//import vn.co.honda.hondacrm.ui.adapters.connected.VpgConnectedNoVehicleAdapter;
//import vn.co.honda.hondacrm.ui.fragments.connected.models.VehicleIntroduce;
//
///**
// * Created by TienTM13 on 24/06/2019.
// */
//
//public class ConnectedVehicleIntroductionFragment extends Fragment {
//
//    private View mRootView;
//    private ViewPager vpgContent;
//    private TabLayout tlSelector;
//    private ArrayList<VehicleIntroduce> alIntroduceVehicle;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mRootView = inflater.inflate(R.layout.fragment_connected_no_vehicle, container, false);
//        initView();
//        return mRootView;
//    }
//
//    private void initView() {
//        vpgContent = mRootView.findViewById(R.id.vpg_content);
//        tlSelector = mRootView.findViewById(R.id.tl_selector);
//
//        fakeData();
//        VpgConnectedNoVehicleAdapter mPagerAdapter = new VpgConnectedNoVehicleAdapter(alIntroduceVehicle);
//        vpgContent.setAdapter(mPagerAdapter);
//        vpgContent.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        tlSelector.setupWithViewPager(vpgContent, true);
//
//    }
//
//    private void fakeData() {
//        alIntroduceVehicle = new ArrayList<>();
//        String str = "Congratulation , We recognize the amount of contribution that you put into your job and we assure that your efforts are appreciated. These awards represent our appreciation and will serve as an ongoing reminder of your achievements.";
//
//        for (int i = 0; i < 3; i++) {
//            VehicleIntroduce v = new VehicleIntroduce("Honda " + i, R.drawable.img_motor_3, str);
//            alIntroduceVehicle.add(v);
//        }
//    }
//}
