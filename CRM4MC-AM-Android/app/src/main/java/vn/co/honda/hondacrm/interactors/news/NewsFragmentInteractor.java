package vn.co.honda.hondacrm.interactors.news;

import java.util.ArrayList;

import rx.Single;
import vn.co.honda.hondacrm.interactors.common.BaseInteractor;
import vn.co.honda.hondacrm.repositories.news.INewsFragmentRepository;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;

/**
 * @author CuongNV31
 */
public class NewsFragmentInteractor extends BaseInteractor implements INewsFragmentInteractor {

    private final INewsFragmentRepository mNewsFragmentRepository;

    public NewsFragmentInteractor(INewsFragmentRepository mNewsFragmentRepository) {
        this.mNewsFragmentRepository = mNewsFragmentRepository;
    }

    @Override
    public Single<ArrayList<NewsModel>> getNews() {
        return mNewsFragmentRepository
                .getNews()
                .onErrorReturn(throwable -> {
                    logError(throwable.getMessage());
                    return getMockNewsModelList();
                });
    }


    private ArrayList<NewsModel> getMockNewsModelList() {

        final ArrayList<NewsModel> newsModels = new ArrayList<>();

        newsModels.add(new NewsModel("#1 Title",
                                     "#1 Body"));
        newsModels.add(new NewsModel("#2 Title",
                                     "#2 Body"));
        newsModels.add(new NewsModel("#3 Title",
                                     "#3 Body"));

        return newsModels;
    }
}
