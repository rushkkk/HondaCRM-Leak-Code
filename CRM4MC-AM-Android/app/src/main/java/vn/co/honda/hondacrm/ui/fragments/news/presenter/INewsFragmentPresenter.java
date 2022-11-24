package vn.co.honda.hondacrm.ui.fragments.news.presenter;

import rx.subjects.PublishSubject;
import vn.co.honda.hondacrm.ui.fragments.common.IBasePresenter;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;
import vn.co.honda.hondacrm.ui.fragments.news.views.INewsFragmentView;

/**
 * @author CuongNV31
 */
public interface INewsFragmentPresenter extends IBasePresenter<INewsFragmentView> {

    void observeNewsAdapterItemClick(PublishSubject<NewsModel> subject);

    void loadNews();
}
