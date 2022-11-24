package vn.co.honda.hondacrm.ui.activities.events;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.fragments.events.FragmentEventGoingNothing;
import vn.co.honda.hondacrm.ui.fragments.events.FragmentMyEvent;
import vn.co.honda.hondacrm.ui.fragments.profile.FragmentProfileMyEventEdit;
import vn.co.honda.hondacrm.ui.fragments.profile.FragmentProfileMyFollowEdit;

public class EventActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        setTitleHeader(getString(R.string.label_title_event));
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {
        FragmentMyEvent fragment_2 = new FragmentMyEvent();
        adapter.addFragment(fragment_2, getString(R.string.lb_event_tab_coming_title));
        adapter.addFragment(new FragmentEventGoingNothing(), getString(R.string.lb_event_tab_going_title));
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
