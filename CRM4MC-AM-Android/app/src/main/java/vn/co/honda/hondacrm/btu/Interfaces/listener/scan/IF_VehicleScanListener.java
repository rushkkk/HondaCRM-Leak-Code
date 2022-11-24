package vn.co.honda.hondacrm.btu.Interfaces.listener.scan;


import java.util.List;

import vn.co.honda.hondacrm.btu.Model.BluetoothScan;

public interface IF_VehicleScanListener {
    void onScanDeviceResult(BluetoothScan device);
    void onScanDeviceResult(List<BluetoothScan> devices);
    void onScanFail(int errorCode);
}
