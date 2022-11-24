package vn.co.honda.hondacrm.net.usecase.common;

import rx.subjects.PublishSubject;

/**
 * @author Aliaksandr Navumchyk
 */
public interface IBaseUseCase<RetrievedDataType> {

    PublishSubject<RetrievedDataType> observeRetrievedData();

    void log(String log);

    void logError(String error);
}
