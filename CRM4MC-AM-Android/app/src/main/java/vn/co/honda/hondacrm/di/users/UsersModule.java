package vn.co.honda.hondacrm.di.users;

import dagger.Module;
import dagger.Provides;
import vn.co.honda.hondacrm.interactors.user.IUserFragmentInteractor;
import vn.co.honda.hondacrm.interactors.user.UserFragmentInteractor;
import vn.co.honda.hondacrm.net.usecase.user.GetUserCase;
import vn.co.honda.hondacrm.repositories.users.IUserFragmentRepository;
import vn.co.honda.hondacrm.repositories.users.UsersFragmentRepository;
import vn.co.honda.hondacrm.ui.fragments.users.presenter.IUsersFragmentPresenter;
import vn.co.honda.hondacrm.ui.fragments.users.presenter.UsersFragmentPresenter;

/**
 * @author CuongNV31
 */
@Module
public class UsersModule {

    @Provides
    @UsersScope
    IUserFragmentRepository provideINewsFragmentRepository(GetUserCase getNewsUseCase) {
        return new UsersFragmentRepository(getNewsUseCase);
    }

    @Provides
    @UsersScope
    IUserFragmentInteractor provideINewsFragmentInteractor(IUserFragmentRepository repository) {
        return new UserFragmentInteractor(repository);
    }

    @Provides
    @UsersScope
    IUsersFragmentPresenter provideINewsPresenter(IUserFragmentInteractor interactor) {
        return new UsersFragmentPresenter(interactor);
    }

}
