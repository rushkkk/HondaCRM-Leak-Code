package com.honda.connmc.Model.message.register.security;

import com.honda.connmc.Model.message.common.VehicleParameterTypeUint8;

import static com.honda.connmc.Constants.message.ParamTag.SECURITY_CONFIG_TYPE;


public	class	VehicleParameterTypeSecurityConfigType
		extends VehicleParameterTypeUint8 {
	public VehicleParameterTypeSecurityConfigType(int type) {
		super(SECURITY_CONFIG_TYPE, type);
	}
}
