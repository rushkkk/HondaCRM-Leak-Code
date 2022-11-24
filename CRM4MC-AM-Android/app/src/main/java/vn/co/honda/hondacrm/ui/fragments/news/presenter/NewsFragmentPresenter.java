package vn.co.honda.hondacrm.ui.fragments.news.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import rx.Subscription;
import rx.subjects.PublishSubject;
import vn.co.honda.hondacrm.interactors.news.INewsFragmentInteractor;
import vn.co.honda.hondacrm.ui.fragments.common.BasePresenter;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModel;
import vn.co.honda.hondacrm.ui.fragments.news.views.INewsFragmentView;
import vn.co.honda.hondacrm.utils.RxIoTransformer;

/**
 * @author CuongNV31
 */
public class NewsFragmentPresenter extends BasePresenter<INewsFragmentView>
        implements INewsFragmentPresenter {

    private final INewsFragmentInteractor mNewsFragmentInteractor;
    private INewsFragmentView mNewsFragmentView;

    public NewsFragmentPresenter(INewsFragmentInteractor newsInteractor) {
        this.mNewsFragmentInteractor = newsInteractor;
    }

    @Override
    public void bindView(INewsFragmentView newsFragmentView) {
        this.mNewsFragmentView = newsFragmentView;
    }

    @Override
    public void unbindView() {
        super.unbindView();
        mNewsFragmentView = null;
    }

    @Override
    public void initStartViewActions() {
        mNewsFragmentView.setAppBarTitle();
        mNewsFragmentView.initNewsAdapter();
    }

    @Override
    public void loadNews() {
        mNewsFragmentView.showProgressDialog();
        mNewsFragmentView.hideContentContainer();
        mNewsFragmentView.hideNoContentContainer();

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
                               mNewsFragmentView.openNewsDetailedFragment(newsModel);
                           },
                           throwable -> logError(throwable.getMessage()));

        mCompositeSubscription.add(subscription);
    }

    private void handleSuccessLoadNews(@NonNull ArrayList<NewsModel> newsModelArrayList) {
        mNewsFragmentView.setNewsAdapterData(newsModelArrayList);
        mNewsFragmentView.hideProgressDialog();
        mNewsFragmentView.showContentContainer();
    }

    private void handleErrorLoadNews(Throwable throwable) {
        logError(throwable.getMessage());
        mNewsFragmentView.hideProgressDialog();
        mNewsFragmentView.showNoContentContainer();
    }
}
