package vn.co.honda.hondacrm.ui.fragments.users.views;

import java.util.ArrayList;

import vn.co.honda.hondacrm.ui.fragments.users.models.UserModel;

/**
 * @author CuongNV31
 */
public interface IUsersFragmentView {

    void setAppBarTitle();

    void initNewsAdapter();

    void setNewsAdapterData(ArrayList<UserModel> data);

    void showProgressDialog();

    void hideProgressDialog();

    void showContentContainer();

    void hideContentContainer();

    void showNoContentContainer();

    void hideNoContentContainer();

    void openNewsDetailedFragment(UserModel newsModel);
}
