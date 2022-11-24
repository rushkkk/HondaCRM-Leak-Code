package vn.co.honda.hondacrm.ui.fragments.connected.views;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.connected.view.base.BaseFragment;
import vn.co.honda.hondacrm.ui.adapters.connected.VpgIntroduceNewProductAdapter;
import vn.co.honda.hondacrm.ui.fragments.connected.models.VehicleIntroduce;

/**
 * Created by TienTM13 on 15/07/2019.
 */

public class IntroduceNewProductFragment extends BaseFragment {

    private ViewPager vpgContent;
    private TabLayout tlSelector;
    private VpgIntroduceNewProductAdapter mPagerAdapter;
    private ArrayList<VehicleIntroduce> alIntroduceVehicle;

    @Override
    protected int getIdLayout() {
        return R.layout.fragment_introduce_new_product;
    }

    @Override
    protected void initView() {
        fakeData();
        vpgContent = findViewById(R.id.vpg_intro_container);
        tlSelector = findViewById(R.id.tl_selector);
        mPagerAdapter = new VpgIntroduceNewProductAdapter(alIntroduceVehicle);
    }

    @Override
    protected void onBackPressedFragment() {
        getActivity().finish();
    }

    @Override
    protected void setActionForView() {
        vpgContent.setAdapter(mPagerAdapter);
        vpgContent.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tlSelector.setupWithViewPager(vpgContent, true);

    }

    private void fakeData() {
        alIntroduceVehicle = new ArrayList<>();
        String strTitle = "Honda New Model";
        String strContent = "are equipped with excellent features to connect and send  parameters to your phone. You can easily control the status of your motorbike also with many other interesting utilities by your phone.";
        for (int i = 0; i < 3; i++) {
            VehicleIntroduce v = new VehicleIntroduce(strTitle, R.drawable.img_motor, strContent);
            alIntroduceVehicle.add(v);
        }
    }
}