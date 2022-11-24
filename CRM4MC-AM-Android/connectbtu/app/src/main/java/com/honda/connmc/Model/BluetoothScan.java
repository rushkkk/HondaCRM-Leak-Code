package com.honda.connmc.Model;

import android.bluetooth.BluetoothDevice;

import com.honda.connmc.Bluetooth.scan.BleScanController;

public class BluetoothScan {
    private BluetoothDevice device;
    private BleScanController.UuidMode uuidMode;

    public BluetoothScan(BluetoothDevice device, BleScanController.UuidMode uuidMode) {
        this.device = device;
        this.uuidMode = uuidMode;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public BleScanController.UuidMode getUuidMode() {
        return uuidMode;
    }

    public void setUuidMode(BleScanController.UuidMode uuidMode) {
        this.uuidMode = uuidMode;
    }
}
