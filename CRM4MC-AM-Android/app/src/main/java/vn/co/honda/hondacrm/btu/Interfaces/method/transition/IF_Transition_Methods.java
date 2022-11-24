package vn.co.honda.hondacrm.btu.Interfaces.method.transition;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleMessage;

public interface IF_Transition_Methods {
    boolean sendMessage(VehicleMessage message);
    void onReceiveData(byte[] data);
}
