package com.honda.connmc.Model.message.register.security;

import com.honda.connmc.Model.message.common.VehicleParam;

import static com.honda.connmc.Constants.message.ParamTag.USER_NAME;


public	class	VehicleParameterTypeUserName
		extends VehicleParam {
	public VehicleParameterTypeUserName(byte[] data) {
		super(USER_NAME, data);
	}
}
