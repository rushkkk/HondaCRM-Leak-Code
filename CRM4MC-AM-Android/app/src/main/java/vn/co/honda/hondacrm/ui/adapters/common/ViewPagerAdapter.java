package vn.co.honda.hondacrm.ui.adapters.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import vn.co.honda.hondacrm.ui.fragments.connected.views.ConnectedContainerFragment;
import vn.co.honda.hondacrm.ui.fragments.dealers.DealersFragment;
import vn.co.honda.hondacrm.ui.fragments.home.views.HomeFragment;
import vn.co.honda.hondacrm.ui.fragments.products.ProductsFragment;
import vn.co.honda.hondacrm.ui.fragments.vehicles.vehicles_infomation.views.VehiclesInformationFragment;
import vn.co.honda.hondacrm.utils.Constants;


/**
 * Created by CuongNV31.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    SparseArray<Fragment> registeredFragments = new SparseArray<>();


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Constants.TAB_PRODUCTS:
                return new ProductsFragment();
            case Constants.TAB_VEHICLES:
                return new VehiclesInformationFragment();
            case Constants.TAB_HOME:
                return new HomeFragment();
            case Constants.TAB_DEALERS:
                return new DealersFragment();
            case Constants.TAB_CONNECTED:
                return new ConnectedContainerFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return Constants.TAB_NUMBERS;
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getFragment(int position) {
        return registeredFragments.get(position);
    }
}
