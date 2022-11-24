package vn.co.honda.hondacrm.di.home;

import dagger.Module;
import dagger.Provides;
import vn.co.honda.hondacrm.interactors.news.INewsFragmentInteractor;
import vn.co.honda.hondacrm.interactors.news.NewsFragmentInteractor;
import vn.co.honda.hondacrm.net.usecase.news.GetNewsUseCase;
import vn.co.honda.hondacrm.repositories.news.INewsFragmentRepository;
import vn.co.honda.hondacrm.repositories.news.NewsFragmentRepository;
import vn.co.honda.hondacrm.ui.fragments.home.presenter.HomeFragmentPresenter;
import vn.co.honda.hondacrm.ui.fragments.home.presenter.IHomeFragmentPresenter;

/**
 * @author CuongNV31
 */
@Module
public class HomeModule {

    @Provides
    @HomeScope
    INewsFragmentRepository provideINewsFragmentRepository(GetNewsUseCase getNewsUseCase) {
        return new NewsFragmentRepository(getNewsUseCase);
    }

    @Provides
    @HomeScope
    INewsFragmentInteractor provideINewsFragmentInteractor(INewsFragmentRepository repository) {
        return new NewsFragmentInteractor(repository);
    }

    @Provides
    @HomeScope
    IHomeFragmentPresenter provideINewsPresenter(INewsFragmentInteractor interactor) {
        return new HomeFragmentPresenter(interactor);
    }

}
