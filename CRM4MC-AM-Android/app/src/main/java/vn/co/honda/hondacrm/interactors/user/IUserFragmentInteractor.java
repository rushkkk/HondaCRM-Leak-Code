package vn.co.honda.hondacrm.interactors.user;

import java.util.ArrayList;

import rx.Single;
import vn.co.honda.hondacrm.ui.fragments.users.models.UserModel;

/**
 * @author CuongNV31
 */
public interface IUserFragmentInteractor {

    Single<ArrayList<UserModel>> getNews();
}
