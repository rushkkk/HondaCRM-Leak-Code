package vn.co.honda.hondacrm.ui.fragments.users.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import rx.Subscription;
import rx.subjects.PublishSubject;
import vn.co.honda.hondacrm.interactors.user.IUserFragmentInteractor;
import vn.co.honda.hondacrm.ui.fragments.common.BasePresenter;
import vn.co.honda.hondacrm.ui.fragments.users.models.UserModel;
import vn.co.honda.hondacrm.ui.fragments.users.views.IUsersFragmentView;
import vn.co.honda.hondacrm.utils.RxIoTransformer;

/**
 * @author CuongNV31
 */
public class UsersFragmentPresenter extends BasePresenter<IUsersFragmentView>
        implements IUsersFragmentPresenter {

    private final IUserFragmentInteractor mNewsFragmentInteractor;
    private IUsersFragmentView mNewsFragmentView;

    public UsersFragmentPresenter(IUserFragmentInteractor newsInteractor) {
        this.mNewsFragmentInteractor = newsInteractor;
    }

    @Override
    public void bindView(IUsersFragmentView newsFragmentView) {
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

//        Single<Response<List<UserModel>>> testObservable = ApiClient.call().getUserDetails();
//        testObservable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleSubscriber<Response<List<UserModel>>>() {
//                    @Override
//                    public void onSuccess(Response<List<UserModel>> listResponse) {
//                        Log.d("TAGGG", listResponse.body().size() + "");
////                        Toast.makeText("Size" + listResponse.body().size()).show();
//                    }
//
//                    @Override
//                    public void onError(Throwable error) {
//
//                    }
//                });
//                .subscribe(new SingleObserver<Response<List<UserModel>>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//                    @Override
//                    public void onSuccess(Response<List<UserModel>> listResponse) {
//                    }
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//                });
//        mCompositeSubscription.add();

    }

    @Override
    public void observeNewsAdapterItemClick(PublishSubject<UserModel> subject) {
        final Subscription subscription = subject
                .subscribe(newsModel -> {
                            log("newsModel: " + newsModel.toString());
                            mNewsFragmentView.openNewsDetailedFragment(newsModel);
                        },
                        throwable -> logError(throwable.getMessage()));

        mCompositeSubscription.add(subscription);
    }

    private void handleSuccessLoadNews(@NonNull ArrayList<UserModel> newsModelArrayList) {
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
