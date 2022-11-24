package vn.co.honda.hondacrm.interactors.user;

import java.util.ArrayList;

import rx.Single;
import vn.co.honda.hondacrm.interactors.common.BaseInteractor;
import vn.co.honda.hondacrm.repositories.users.IUserFragmentRepository;
import vn.co.honda.hondacrm.ui.fragments.users.models.UserModel;

/**
 * @author CuongNV31
 */
public class UserFragmentInteractor extends BaseInteractor implements IUserFragmentInteractor {

    private final IUserFragmentRepository mNewsFragmentRepository;

    public UserFragmentInteractor(IUserFragmentRepository mNewsFragmentRepository) {
        this.mNewsFragmentRepository = mNewsFragmentRepository;
    }

    @Override
    public Single<ArrayList<UserModel>> getNews() {
        return mNewsFragmentRepository
                .getUsers()
                .onErrorReturn(throwable -> {
                    logError(throwable.getMessage());
                    return getMockNewsModelList();
                });
    }


    private ArrayList<UserModel> getMockNewsModelList() {

//        final ArrayList<NewsModel> newsModels = new ArrayList<>();
//
//        newsModels.add(new NewsModel("#1 Title",
//                                     "#1 Body"));
//        newsModels.add(new NewsModel("#2 Title",
//                                     "#2 Body"));
//        newsModels.add(new NewsModel("#3 Title",
//                                     "#3 Body"));

        return null;
    }
}
