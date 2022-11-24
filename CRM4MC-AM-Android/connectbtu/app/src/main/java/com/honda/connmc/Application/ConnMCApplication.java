package com.honda.connmc.Application;

import android.app.Application;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.NativeLib.NativeLibController;
import com.honda.connmc.Utils.LogUtils;

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
