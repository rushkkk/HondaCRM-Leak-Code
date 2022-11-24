package com.honda.connmc.Model.message.register.response;

import com.honda.connmc.Model.message.common.VehicleMessage;

import static com.honda.connmc.Constants.message.MsgId.AUTHENTICATION_RESPONSE;

public class VehicleMessageAuthenticationResponse
        extends VehicleMessage {

    public VehicleMessageAuthenticationResponse() {
        super(AUTHENTICATION_RESPONSE);
    }

}
