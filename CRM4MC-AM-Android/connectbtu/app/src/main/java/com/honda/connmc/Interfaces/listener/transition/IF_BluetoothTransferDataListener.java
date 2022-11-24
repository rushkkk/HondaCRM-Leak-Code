package com.honda.connmc.Interfaces.listener.transition;

import com.honda.connmc.Model.message.common.VehicleMessage;

public interface IF_BluetoothTransferDataListener {
    void onReceiveDataFromBTU(VehicleMessage message);

    void onReceivedRawData(String rawData);

    void onSendMessageFail();
}
