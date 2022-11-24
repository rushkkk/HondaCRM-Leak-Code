package vn.co.honda.hondacrm.ui.fragments.home.presenter;

import rx.subjects.PublishSubject;
import vn.co.honda.hondacrm.ui.fragments.common.IBasePresenter;
import vn.co.honda.hondacrm.ui.fragments.home.views.IHomeFragmentView;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;

/**
 * @author CuongNV31
 */
public interface IHomeFragmentPresenter extends IBasePresenter<IHomeFragmentView> {

    void observeNewsAdapterItemClick(PublishSubject<NewsModel> subject);

    void loadNews();
}
