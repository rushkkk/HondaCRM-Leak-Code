package vn.co.honda.hondacrm.net.usecase.user;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.ui.fragments.users.models.UserModel;

/**
 * @author CuongNV31
 */
@Singleton
public class GetUserCase {

    private final ApiService mApiClient;

    private Single<List<UserModel>> userModels;

    @Inject
    GetUserCase(ApiService apiClient) {
        mApiClient = apiClient;
    }

    public Single<ArrayList<UserModel>> buildUseCaseObservable() {
        return mApiClient
                .getUserDetails();
    }

}