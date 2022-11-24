package com.honda.connmc.Model.message.register.security;

import com.honda.connmc.Model.message.common.VehicleParam;

import static com.honda.connmc.Constants.message.ParamTag.SECURITY_CONFIG_CONTAINER;

public	class		VehicleParameterTypeSecurityConfigContainer
		extends VehicleParam {
	public VehicleParameterTypeSecurityConfigContainer(byte[] data) {
		super(SECURITY_CONFIG_CONTAINER, data);
	}

	
}
