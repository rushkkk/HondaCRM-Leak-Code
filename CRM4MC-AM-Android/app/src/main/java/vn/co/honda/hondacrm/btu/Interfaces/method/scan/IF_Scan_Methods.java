package vn.co.honda.hondacrm.btu.Interfaces.method.scan;

import android.content.Context;

public interface IF_Scan_Methods {
    boolean startScan(Context activity);
    boolean startScan(Context activity, String name);
    boolean stopScan();
}
