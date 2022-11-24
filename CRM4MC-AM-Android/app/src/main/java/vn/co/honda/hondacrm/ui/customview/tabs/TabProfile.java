package vn.co.honda.hondacrm.ui.customview.tabs;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.customview.HomeListener;
import vn.co.honda.hondacrm.ui.fragments.profile.FragmentProfileMyFollowEdit;

/**
 * CustomTabHome.
 */
public class TabProfile extends LinearLayout {

    int count=0;
    @BindViews({R.id.tvTabInfo, R.id.tvTabFollow, R.id.tvTabEvent})
    List<TextView> mTabHomes;

    public TabProfile(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.tab_profile, this);
        ButterKnife.bind(this);
    }

    public TabProfile(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.tab_profile, this);
        ButterKnife.bind(this);
    }

    public TabProfile(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.tab_profile, this);
        ButterKnife.bind(this);
    }


    /**
     * Interface define annotation select fragment and tab.
     */
    @IntDef({HomeStatus.HOME, HomeStatus.PRODUCTS, HomeStatus.VEHICLES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HomeStatus {
        int PRODUCTS = 0;
        int VEHICLES = 1;
        int HOME = 2;
    }

    public void setHomeListener(HomeListener homeListener) {
        this.homeListener = homeListener;
    }

    private HomeListener homeListener;

    @OnClick(R.id.tvTabInfo)
    void clickProducts() {
        updateTab(HomeStatus.PRODUCTS);
    }

    @OnClick(R.id.tvTabFollow)
    void clickVehicles() {
        updateTab(HomeStatus.VEHICLES);
    }

    @OnClick(R.id.tvTabEvent)
    void clickHome() {
        updateTab(HomeStatus.HOME);
    }

    public void updateTab(@HomeStatus int status) {
        if (status < 0 || status >= mTabHomes.size()) {
            return;
        }
        /*change background tab*/
        for (TextView tv : mTabHomes) {
            tv.setSelected(false);
        }
        mTabHomes.get(status).setSelected(true);
        /*change show fragment*/
        if (homeListener == null) {
            return;
        }
        homeListener.setTextHeader(status);
        switch (status) {
            case HomeStatus.HOME:
                homeListener.showHomeFragment();
                break;
            case HomeStatus.PRODUCTS:

                homeListener.showProductsFragment();
                break;
            case HomeStatus.VEHICLES:
                homeListener.showVehiclesFragment();
                break;
            default:
                homeListener.showHomeFragment();
                break;
        }
    }
}
