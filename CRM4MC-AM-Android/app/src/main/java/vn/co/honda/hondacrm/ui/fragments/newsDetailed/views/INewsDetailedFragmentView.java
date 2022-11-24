package vn.co.honda.hondacrm.ui.fragments.newsDetailed.views;

/**
 * @author CuongNV31
 */
public interface INewsDetailedFragmentView {

    void showAppBarBackArrow();

    void setAppBarTitle(String text);

    void setTitle(String text);

    void setBody(String text);

    void showProgressDialog();

    void hideProgressDialog();

    void showContentContainer();

    void hideContentContainer();

    void showNoContentContainer();

    void hideNoContentContainer();

}
