package com.honda.connmc.Model.message.register.security;

import com.honda.connmc.Model.message.common.VehicleParam;

import static com.honda.connmc.Constants.message.ParamTag.RANDOM_VALUE;


public	class		VehicleParameterTypeSecurityRandomValue
		extends VehicleParam {
	public VehicleParameterTypeSecurityRandomValue(byte[] data) {
		super(RANDOM_VALUE, data);
	}
}