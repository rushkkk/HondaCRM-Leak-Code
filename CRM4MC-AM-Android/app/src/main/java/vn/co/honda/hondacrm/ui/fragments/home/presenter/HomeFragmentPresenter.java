package vn.co.honda.hondacrm.ui.fragments.home.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import rx.Subscription;
import rx.subjects.PublishSubject;
import vn.co.honda.hondacrm.interactors.news.INewsFragmentInteractor;
import vn.co.honda.hondacrm.ui.fragments.common.BasePresenter;
import vn.co.honda.hondacrm.ui.fragments.home.views.IHomeFragmentView;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;
import vn.co.honda.hondacrm.utils.RxIoTransformer;

/**
 * @author CuongNV31
 */
public class HomeFragmentPresenter extends BasePresenter<IHomeFragmentView>
        implements IHomeFragmentPresenter {

    private final INewsFragmentInteractor mNewsFragmentInteractor;
    private IHomeFragmentView mHomeFragmentView;

    public HomeFragmentPresenter(INewsFragmentInteractor newsInteractor) {
        this.mNewsFragmentInteractor = newsInteractor;
    }

    @Override
    public void bindView(IHomeFragmentView homeFragmentView) {
        this.mHomeFragmentView = homeFragmentView;
    }

    @Override
    public void unbindView() {
        super.unbindView();
        mHomeFragmentView = null;
    }

    @Override
    public void initStartViewActions() {
        mHomeFragmentView.setAppBarTitle();
        mHomeFragmentView.initNewsAdapter();
    }

    @Override
    public void loadNews() {
        mHomeFragmentView.showProgressDialog();
        mHomeFragmentView.hideContentContainer();
        mHomeFragmentView.hideNoContentContainer();

        final Subscription loadNewsSubscription = mNewsFragmentInteractor
                .getNews()
                .compose(new RxIoTransformer<>())
                .subscribe(this::handleSuccessLoadNews, this::handleErrorLoadNews);
        mCompositeSubscription.add(loadNewsSubscription);
    }

    @Override
    public void observeNewsAdapterItemClick(PublishSubject<NewsModel> subject) {
        final Subscription subscription = subject
                .subscribe(newsModel -> {
                            log("newsModel: " + newsModel.toString());
                            mHomeFragmentView.openNewsDetailedFragment(newsModel);
                        },
                        throwable -> logError(throwable.getMessage()));

        mCompositeSubscription.add(subscription);
    }

    private void handleSuccessLoadNews(@NonNull ArrayList<NewsModel> newsModelArrayList) {
        mHomeFragmentView.setNewsAdapterData(newsModelArrayList);
        mHomeFragmentView.hideProgressDialog();
        mHomeFragmentView.showContentContainer();
    }

    private void handleErrorLoadNews(Throwable throwable) {
        logError(throwable.getMessage());
        mHomeFragmentView.hideProgressDialog();
        mHomeFragmentView.showNoContentContainer();
    }
}
