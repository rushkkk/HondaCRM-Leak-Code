package com.honda.connmc.Interfaces.method.transition;

import com.honda.connmc.Model.message.common.VehicleMessage;

public interface IF_Transition_Methods {
    boolean sendMessage(VehicleMessage message);
    void onReceiveData(byte[] data);
}
