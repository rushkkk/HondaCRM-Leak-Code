package vn.co.honda.hondacrm.repositories.common;

import android.support.annotation.NonNull;

import vn.co.honda.hondacrm.utils.LogUtils;

/**
 * @author CuongNV31
 */
public abstract class BaseRepository implements IBaseRepository {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void log(@NonNull final String log) {
        LogUtils.i(TAG, log);
    }

    @Override
    public void logError(@NonNull final String error) {
        LogUtils.e(TAG, error);
    }
}