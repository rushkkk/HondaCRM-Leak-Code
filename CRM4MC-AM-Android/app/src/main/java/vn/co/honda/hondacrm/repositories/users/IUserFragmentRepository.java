package vn.co.honda.hondacrm.repositories.users;

import java.util.ArrayList;

import rx.Single;
import vn.co.honda.hondacrm.ui.fragments.users.models.UserModel;

/**
 * @author CuongNV31
 */
public interface IUserFragmentRepository {

    Single<ArrayList<UserModel>> getUsers();

}


