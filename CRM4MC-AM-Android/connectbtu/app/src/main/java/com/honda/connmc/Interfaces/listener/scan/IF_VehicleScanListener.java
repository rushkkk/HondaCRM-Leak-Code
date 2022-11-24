package com.honda.connmc.Interfaces.listener.scan;

import com.honda.connmc.Model.BluetoothScan;

import java.util.List;

public interface IF_VehicleScanListener {
    void onScanDeviceResult(BluetoothScan device);
    void onScanDeviceResult(List<BluetoothScan> devices);
    void onScanFail(int errorCode);
}
