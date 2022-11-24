package vn.co.honda.hondacrm.net.usecase.common;

import java.util.List;

import rx.Single;
import rx.subjects.PublishSubject;
import vn.co.honda.hondacrm.utils.LogUtils;

/**
 * @author CuongNV31
 */
public abstract class BaseUseCases<ParamType, ResultType, RetrievedDataType> implements IBaseUseCase {

    private final String TAG = getClass().getSimpleName();
    private PublishSubject<RetrievedDataType> mRetrievedData = PublishSubject.create();

    public abstract Single<List<ResultType>> buildUseCaseObservable(
            ParamType param,
            PublishSubject<RetrievedDataType> retrieveData);

    public Single<List<ResultType>> execute(ParamType param) {
        return buildUseCaseObservable(param, mRetrievedData);
    }

    @Override
    public PublishSubject<RetrievedDataType> observeRetrievedData() {
        return mRetrievedData;
    }

    @Override
    public void log(String log) {
        LogUtils.i(TAG, log);
    }

    @Override
    public void logError(String error) {
        LogUtils.e(TAG, error);
    }
}
