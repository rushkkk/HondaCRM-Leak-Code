package vn.co.honda.hondacrm.ui.fragments.vehicles.vehicles_infomation.views;

import java.util.ArrayList;

import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;

/**
 * @author CuongNV31
 */
public interface IVehiclesInfomationFragmentView {

    void initNewsAdapter();

    void setNewsAdapterData(ArrayList<NewsModel> data);

    void setupViewPager();
}
