package com.honda.connmc.Model.message.register.security;


import com.honda.connmc.Model.message.common.VehicleParameterTypeUint8;

import static com.honda.connmc.Constants.message.ParamTag.SECURITY_CONFIG_IDENTIFIER;


public	class		VehicleParameterTypeSecurityConfigIdentifier
		extends VehicleParameterTypeUint8 {
	public VehicleParameterTypeSecurityConfigIdentifier(int data) {
		super(SECURITY_CONFIG_IDENTIFIER, data);
	}

	
}
