package vn.co.honda.hondacrm.repositories.news;

import java.util.ArrayList;

import rx.Single;
import vn.co.honda.hondacrm.net.usecase.news.GetNewsUseCase;
import vn.co.honda.hondacrm.repositories.common.BaseRepository;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;

/**
 * @author CuongNV31
 */
public class NewsFragmentRepository extends BaseRepository implements INewsFragmentRepository {

    private final GetNewsUseCase mGetNewsUseCase;

    public NewsFragmentRepository(GetNewsUseCase getNewsUseCase) {
        mGetNewsUseCase = getNewsUseCase;
    }

    @Override
    public Single<ArrayList<NewsModel>> getNews() {
        return mGetNewsUseCase.execute(null);
    }
}
