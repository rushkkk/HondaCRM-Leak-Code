package vn.co.honda.hondacrm.btu.Interfaces.listener.connect;


import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;

public interface IF_VehicleConnectListener {
    void onConnectStateNotice(BluetoothManager.EnumDeviceConnectStatus connectState);
}
