package vn.co.honda.hondacrm.ui.customview;

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

/**
 * CustomTabHome.
 */
public class TabBottom extends LinearLayout {

    @BindViews({R.id.tvTabProducts, R.id.tvTabVehicles, R.id.tvTabHome, R.id.tvTabDealers, R.id.tvTabConnect})
    List<TextView> mTabHomes;

    public TabBottom(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.tab_bottom, this);
        ButterKnife.bind(this);
    }

    public TabBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.tab_bottom, this);
        ButterKnife.bind(this);
    }

    public TabBottom(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.tab_bottom, this);
        ButterKnife.bind(this);
    }

    /**
     * Interface define annotation select fragment and tab.
     */
    @IntDef({HomeStatus.HOME, HomeStatus.PRODUCTS, HomeStatus.CONNECTED, HomeStatus.VEHICLES, HomeStatus.DEALERS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HomeStatus {
        int PRODUCTS = 0;
        int VEHICLES = 1;
        int HOME = 2;
        int DEALERS = 3;
        int CONNECTED = 4;
    }

    public void setHomeListener(HomeListener homeListener) {
        this.homeListener = homeListener;
    }

    private HomeListener homeListener;

    @OnClick(R.id.tvTabProducts)
    void clickProducts() {
        updateTab(HomeStatus.PRODUCTS);
    }

    @OnClick(R.id.tvTabVehicles)
    void clickVehicles() {
        updateTab(HomeStatus.VEHICLES);
    }

    @OnClick(R.id.tvTabHome)
    void clickHome() {
        updateTab(HomeStatus.HOME);
    }

    @OnClick(R.id.tvTabDealers)
    void clickDealers() {
        updateTab(HomeStatus.DEALERS);
    }

    @OnClick(R.id.tvTabConnect)
    void clickConnected() {
        updateTab(HomeStatus.CONNECTED);
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
            case HomeStatus.CONNECTED:
                homeListener.showConnectedFragment();
                break;
            case HomeStatus.VEHICLES:
                homeListener.showVehiclesFragment();
                break;
            case HomeStatus.DEALERS:
                homeListener.showDealers();
                break;
            default:
                homeListener.showHomeFragment();
                break;
        }
    }
}
