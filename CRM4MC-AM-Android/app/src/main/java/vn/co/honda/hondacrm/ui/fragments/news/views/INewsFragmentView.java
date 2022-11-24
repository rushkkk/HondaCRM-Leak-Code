package vn.co.honda.hondacrm.ui.fragments.news.views;

import java.util.ArrayList;

import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;

/**
 * @author CuongNV31
 */
public interface INewsFragmentView {

    void setAppBarTitle();

    void initNewsAdapter();

    void setNewsAdapterData(ArrayList<NewsModel> data);

    void showProgressDialog();

    void hideProgressDialog();

    void showContentContainer();

    void hideContentContainer();

    void showNoContentContainer();

    void hideNoContentContainer();

    void openNewsDetailedFragment(NewsModel newsModel);
}
