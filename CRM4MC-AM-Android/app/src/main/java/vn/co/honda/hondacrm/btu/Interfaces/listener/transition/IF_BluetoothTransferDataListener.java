package vn.co.honda.hondacrm.btu.Interfaces.listener.transition;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleMessage;

public interface IF_BluetoothTransferDataListener {
    void onReceiveDataFromBTU(VehicleMessage message);

    void onReceivedRawData(String rawData);

    void onSendMessageFail();
}
