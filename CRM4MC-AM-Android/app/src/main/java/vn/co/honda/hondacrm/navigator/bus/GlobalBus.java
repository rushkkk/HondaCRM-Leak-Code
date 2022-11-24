package vn.co.honda.hondacrm.navigator.bus;

import com.squareup.otto.Bus;

/**
 * Define global otto bus.
 *
 * @author CuongNV31
 */
public class GlobalBus {
    private static Bus sBus;

    public static Bus getBus() {
        if (sBus == null)
            sBus = new Bus();
        return sBus;
    }
}
