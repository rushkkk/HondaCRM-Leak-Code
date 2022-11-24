package vn.co.honda.hondacrm.di.newsDetail;

import dagger.Module;
import dagger.Provides;
import vn.co.honda.hondacrm.helpers.ImageHelper;
import vn.co.honda.hondacrm.interactors.newsDetailed.INewsDetailedFragmentInteractor;
import vn.co.honda.hondacrm.interactors.newsDetailed.NewsDetailedFragmentInteractor;
import vn.co.honda.hondacrm.repositories.newsDetailed.INewsDetailedFragmentRepository;
import vn.co.honda.hondacrm.repositories.newsDetailed.NewsDetailedFragmentRepository;
import vn.co.honda.hondacrm.ui.fragments.newsDetailed.presenter.INewsDetailedFragmentPresenter;
import vn.co.honda.hondacrm.ui.fragments.newsDetailed.presenter.NewsDetailedFragmentPresenter;

/**
 * @author CuongNV31
 */
@Module
public class NewsDetailedModule {

    @Provides
    @NewsDetailedScope
    INewsDetailedFragmentRepository provideINewsDetailedFragmentRepository() {
        return new NewsDetailedFragmentRepository();
    }

    @Provides
    @NewsDetailedScope
    INewsDetailedFragmentInteractor provideINewsDetailedFragmentInteractor(INewsDetailedFragmentRepository repository) {
        return new NewsDetailedFragmentInteractor(repository);
    }

    @Provides
    @NewsDetailedScope
    INewsDetailedFragmentPresenter provideINewsDetailedPresenter(INewsDetailedFragmentInteractor interactor,
                                                                 ImageHelper imageHelper) {
        return new NewsDetailedFragmentPresenter(interactor, imageHelper);
    }

}
