package vn.co.honda.hondacrm.ui.adapters.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import vn.co.honda.hondacrm.utils.LogUtils;

/**
 * @author CuongNV31
 */
public abstract class BaseHolder <ClickData> extends RecyclerView.ViewHolder
        implements IBaseHolder {

    private final String TAG = getClass().getSimpleName();
    protected PublishSubject <ClickData> mClick;
    protected CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    protected BaseHolder(View v, PublishSubject <ClickData>  click) {
        super(v);
        mClick = click;
        ButterKnife.bind(this, v);
    }

    @Override
    public void unbindView() {
        unsubscribeSubscription(mCompositeSubscription);
    }

    private void unsubscribeSubscription(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public void log(final String message) {
        LogUtils.i(TAG, message);
    }

    public void logError(final String message) {
        LogUtils.e(TAG, message);
    }
}
