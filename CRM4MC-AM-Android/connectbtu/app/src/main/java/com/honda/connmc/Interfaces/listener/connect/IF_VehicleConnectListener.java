package com.honda.connmc.Interfaces.listener.connect;

import com.honda.connmc.Bluetooth.BluetoothManager;

public interface IF_VehicleConnectListener {
    void onConnectStateNotice(BluetoothManager.EnumDeviceConnectStatus connectState);
}
