package vn.co.honda.hondacrm.ui.fragments.vehicles.vehicles_infomation.presenter;

import rx.subjects.PublishSubject;
import vn.co.honda.hondacrm.ui.fragments.common.IBasePresenter;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;
import vn.co.honda.hondacrm.ui.fragments.news.views.INewsFragmentView;

/**
 * @author CuongNV31
 */
public interface IVehiclesInformationFragmentPresenter extends IBasePresenter<INewsFragmentView> {

    void observeNewsAdapterItemClick(PublishSubject<NewsModel> subject);

    void loadNews();
}
