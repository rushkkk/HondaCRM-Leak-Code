package vn.co.honda.hondacrm.interactors.news;

import java.util.ArrayList;

import rx.Single;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;

/**
 * @author CuongNV31
 */
public interface INewsFragmentInteractor {

    Single<ArrayList<NewsModel>> getNews();
}
