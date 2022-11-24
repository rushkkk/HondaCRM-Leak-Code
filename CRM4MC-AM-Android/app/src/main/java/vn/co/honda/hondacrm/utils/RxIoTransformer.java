package vn.co.honda.hondacrm.utils;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author CuongNV31
 */
public class RxIoTransformer<T> implements Single.Transformer<T, T> {

    @Override
    public Single<T> call(Single<T> single) {
        return single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
