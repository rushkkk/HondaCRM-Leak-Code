package vn.co.honda.hondacrm.btu.Model.message.register.response;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleMessage;

import static vn.co.honda.hondacrm.btu.Constants.message.MsgId.SECURITY_CONFIG_RESPONSE;

public	class VehicleMessageSecurityConfigResponse
		extends VehicleMessage {
	public VehicleMessageSecurityConfigResponse() {
		super(SECURITY_CONFIG_RESPONSE);
	}
}
