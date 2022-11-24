package vn.co.honda.hondacrm.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.Date;

/**
 * @author CuongNV31
 */
public final class LogUtils {



    private LogUtils() {
        // for preventing new LogUtils()
    }

    private static final String LOG_TAG = "HonDa";

    static {
        Logger.init(LOG_TAG)
                .methodCount(0)
                .hideThreadInfo();
    }

    public static void d(final String tag, final String message) {
        Logger.d("%s: %s", tag, message);
    }

    public static void i(final String tag, final String message) {
        Logger.i("%s: %s", tag, message);
    }

    public static void v(final String tag, final String message) {
        Logger.v("%s: %s", tag, message);
    }

    public static void e(final String tag, final String message) {
        Logger.e("%s: %s", tag, message);
    }
}
