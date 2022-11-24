package vn.co.honda.hondacrm.btu.Application;

import android.app.Application;

import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.NativeLib.NativeLibController;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;

public class ConnMCApplication extends Application {
    private final boolean DEBUG_MODE_OFF = false;
    private final boolean DEBUG_MODE_ON = true;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.startEndMethodLog(true);
        LogUtils.setDebugMode(DEBUG_MODE_ON);
        new Thread(() -> {
            NativeLibController.createInstance();
        }).start();
        BluetoothManager.getInstance().init(this);
        LogUtils.startEndMethodLog(false);
    }
}
