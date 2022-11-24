package vn.co.honda.hondacrm.btu.Model.message.register.security;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParameterTypeUint8;

import static vn.co.honda.hondacrm.btu.Constants.message.ParamTag.SECURITY_CONFIG_IDENTIFIER;

public	class		VehicleParameterTypeSecurityConfigIdentifier
		extends VehicleParameterTypeUint8 {
	public VehicleParameterTypeSecurityConfigIdentifier(int data) {
		super(SECURITY_CONFIG_IDENTIFIER, data);
	}

	
}
