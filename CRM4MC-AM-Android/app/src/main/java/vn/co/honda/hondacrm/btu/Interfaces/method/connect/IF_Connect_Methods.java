package vn.co.honda.hondacrm.btu.Interfaces.method.connect;

import android.bluetooth.BluetoothDevice;

public interface IF_Connect_Methods {
    boolean startConnect(BluetoothDevice bluetoothDevice);
    boolean reConnect();
    boolean disconnectDevice();
}
