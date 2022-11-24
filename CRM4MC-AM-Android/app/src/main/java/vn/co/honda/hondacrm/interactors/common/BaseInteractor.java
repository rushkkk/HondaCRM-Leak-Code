package vn.co.honda.hondacrm.interactors.common;

import vn.co.honda.hondacrm.utils.LogUtils;

/**
 * @author CuongNV31
 */
public abstract class BaseInteractor implements IBaseInteractor {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void log(String log) {
        LogUtils.i(TAG, log);
    }

    @Override
    public void logError(String error) {
        LogUtils.e(TAG, error);
    }
}