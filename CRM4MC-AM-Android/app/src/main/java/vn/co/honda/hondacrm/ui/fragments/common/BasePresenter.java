package vn.co.honda.hondacrm.ui.fragments.common;

import rx.subscriptions.CompositeSubscription;
import vn.co.honda.hondacrm.utils.LogUtils;

/**
 * @author CuongNV31
 */
public abstract class BasePresenter<TypeView> implements IBasePresenter<TypeView> {

    protected CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    private final String TAG = getClass().getSimpleName();

    @Override
    public void unbindView() {
        mCompositeSubscription.clear();
    }

    protected void log(final String log) {
        LogUtils.i(TAG, log);
    }

    protected void logError(final String error) {
        LogUtils.e(TAG, error);
    }
}