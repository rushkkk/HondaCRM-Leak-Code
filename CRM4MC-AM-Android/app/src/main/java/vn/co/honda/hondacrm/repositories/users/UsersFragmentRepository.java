package vn.co.honda.hondacrm.repositories.users;

import java.util.ArrayList;

import rx.Single;
import vn.co.honda.hondacrm.net.usecase.user.GetUserCase;
import vn.co.honda.hondacrm.repositories.common.BaseRepository;
import vn.co.honda.hondacrm.ui.fragments.users.models.UserModel;

/**
 * @author CuongNV31
 */
public class UsersFragmentRepository extends BaseRepository implements IUserFragmentRepository {

    private final GetUserCase mGetNewsUseCase;

    public UsersFragmentRepository(GetUserCase getNewsUseCase) {
        mGetNewsUseCase = getNewsUseCase;
    }

    @Override
    public Single<ArrayList<UserModel>> getUsers() {
        return mGetNewsUseCase.buildUseCaseObservable();
    }
}
