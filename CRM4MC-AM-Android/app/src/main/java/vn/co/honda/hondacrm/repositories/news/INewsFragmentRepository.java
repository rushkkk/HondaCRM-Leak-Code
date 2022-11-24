package vn.co.honda.hondacrm.repositories.news;

import java.util.ArrayList;

import rx.Single;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;

/**
 * @author CuongNV31
 */
public interface INewsFragmentRepository {

    Single<ArrayList<NewsModel>> getNews();

}


