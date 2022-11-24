package vn.co.honda.hondacrm.ui.customview;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Interface define annotation select fragment and tab.
 */
@IntDef({HomeStatus.HOME, HomeStatus.PRODUCTS, HomeStatus.CONNECTED, HomeStatus.VEHICLES, HomeStatus.DEALERS})
@Retention(RetentionPolicy.SOURCE)
public @interface HomeStatus {
    int PRODUCTS = 0;
    int VEHICLES = 1;
    int HOME = 2;
    int DEALERS = 3;
    int CONNECTED = 4;
}
    