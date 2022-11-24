package vn.co.honda.hondacrm.net.usecase.news;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;
import rx.subjects.PublishSubject;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.usecase.common.BaseUseCase;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModelRSS;

/**
 * @author CuongNV31
 */
@Singleton
public class GetNewsUseCase extends BaseUseCase<Void, ArrayList<NewsModel>, Void> {

    private final ApiService mApiClient;

    @Inject
    GetNewsUseCase(ApiService apiClient) {
        mApiClient = apiClient;
    }

    @Override
    public Single<ArrayList<NewsModel>> buildUseCaseObservable(Void param,
                                                               PublishSubject retrievedData) {
        return mApiClient
                .getNews()
                .map(NewsModelRSS::getNewsList);
    }
}