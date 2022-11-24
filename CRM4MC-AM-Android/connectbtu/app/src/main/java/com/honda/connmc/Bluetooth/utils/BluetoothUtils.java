package com.honda.connmc.Bluetooth.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.provider.Settings;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Utils.LogUtils;

public class BluetoothUtils {
    public static boolean checkBluetooth() {
        LogUtils.startEndMethodLog(true);
        // Check if the device support BLE
        if (!BluetoothManager.getInstance().getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            LogUtils.d("BLE not supported");
            LogUtils.startEndMethodLog(false);
            BluetoothManager.getInstance().disconnectDevice();
            return false;
        }

        // If so, turn it on
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            LogUtils.d("BluetoothAdapter null");
            LogUtils.startEndMethodLog(false);
            BluetoothManager.getInstance().disconnectDevice();
            return false;
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                LogUtils.d("BluetoothAdapter isEnabled");
                LogUtils.startEndMethodLog(false);
                return true;
            } else {
                LogUtils.d("BluetoothAdapter disabled");
                LogUtils.startEndMethodLog(false);
                BluetoothManager.getInstance().disconnectDevice();
                return false;
            }
        }

    }

    public static boolean isBluetoothDeviceConnected() {
        BluetoothDevice bluetoothDevice = com.honda.connmc.Bluetooth.BluetoothManager.getInstance().getBleConnectController().getBluetoothDeviceConnect();
        if (bluetoothDevice == null) {
            LogUtils.e("BluetoothDevice is null");
            return false;
        }
        android.bluetooth.BluetoothManager bluetoothManager = (android.bluetooth.BluetoothManager)BluetoothManager.getInstance().getApplicationContext().getSystemService(Activity.BLUETOOTH_SERVICE);
        return bluetoothManager.getConnectionState(bluetoothDevice, BluetoothProfile.GATT) == BluetoothProfile.STATE_CONNECTED;
    }

    public static boolean checkNotificationAccsess() {
        ContentResolver contentResolver = BluetoothManager.getInstance().getApplicationContext().getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = BluetoothManager.getInstance().getApplicationContext().getPackageName();

        // check to see if the enabledNotificationListeners String contains our package name
        return !(enabledNotificationListeners == null || !enabledNotificationListeners.contains(packageName));
    }
}
