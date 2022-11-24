package vn.co.honda.hondacrm.btu.Model.message.register.response;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleMessage;

import static vn.co.honda.hondacrm.btu.Constants.message.MsgId.AUTHENTICATION_RESPONSE;

public class VehicleMessageAuthenticationResponse
        extends VehicleMessage {

    public VehicleMessageAuthenticationResponse() {
        super(AUTHENTICATION_RESPONSE);
    }

}
