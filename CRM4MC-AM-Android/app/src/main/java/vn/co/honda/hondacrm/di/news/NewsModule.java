package vn.co.honda.hondacrm.di.news;

import dagger.Module;
import dagger.Provides;
import vn.co.honda.hondacrm.interactors.news.INewsFragmentInteractor;
import vn.co.honda.hondacrm.interactors.news.NewsFragmentInteractor;
import vn.co.honda.hondacrm.net.usecase.news.GetNewsUseCase;
import vn.co.honda.hondacrm.repositories.news.INewsFragmentRepository;
import vn.co.honda.hondacrm.repositories.news.NewsFragmentRepository;
import vn.co.honda.hondacrm.ui.fragments.news.presenter.INewsFragmentPresenter;
import vn.co.honda.hondacrm.ui.fragments.news.presenter.NewsFragmentPresenter;

/**
 * @author CuongNV31
 */
@Module
public class NewsModule {

    @Provides
    @NewsScope
    INewsFragmentRepository provideINewsFragmentRepository(GetNewsUseCase getNewsUseCase) {
        return new NewsFragmentRepository(getNewsUseCase);
    }

    @Provides
    @NewsScope
    INewsFragmentInteractor provideINewsFragmentInteractor(INewsFragmentRepository repository) {
        return new NewsFragmentInteractor(repository);
    }

    @Provides
    @NewsScope
    INewsFragmentPresenter provideINewsPresenter(INewsFragmentInteractor interactor) {
        return new NewsFragmentPresenter(interactor);
    }

}
