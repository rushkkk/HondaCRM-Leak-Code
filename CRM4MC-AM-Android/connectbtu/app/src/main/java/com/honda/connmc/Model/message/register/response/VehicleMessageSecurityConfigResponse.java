package com.honda.connmc.Model.message.register.response;


import com.honda.connmc.Model.message.common.VehicleMessage;

import static com.honda.connmc.Constants.message.MsgId.SECURITY_CONFIG_RESPONSE;

public	class VehicleMessageSecurityConfigResponse
		extends VehicleMessage {
	public VehicleMessageSecurityConfigResponse() {
		super(SECURITY_CONFIG_RESPONSE);
	}
}
