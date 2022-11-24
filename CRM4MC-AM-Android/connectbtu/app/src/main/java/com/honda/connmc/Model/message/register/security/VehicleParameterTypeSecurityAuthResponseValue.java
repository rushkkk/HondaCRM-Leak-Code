package com.honda.connmc.Model.message.register.security;


import com.honda.connmc.Model.message.common.VehicleParam;

import static com.honda.connmc.Constants.message.ParamTag.AUTH_RESPONSE_VALUE;

public	class		VehicleParameterTypeSecurityAuthResponseValue
		extends VehicleParam {
	public VehicleParameterTypeSecurityAuthResponseValue(byte[] data) {
		super(AUTH_RESPONSE_VALUE, data);
	}

	
}
