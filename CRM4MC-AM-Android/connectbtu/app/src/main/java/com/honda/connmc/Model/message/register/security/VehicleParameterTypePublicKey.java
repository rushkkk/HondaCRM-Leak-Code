package com.honda.connmc.Model.message.register.security;

import com.honda.connmc.Model.message.common.VehicleParam;

import static com.honda.connmc.Constants.message.ParamTag.PUBLIC_KEY;

public	class	VehicleParameterTypePublicKey
		extends VehicleParam {
	public VehicleParameterTypePublicKey(byte[] data) {
		super(PUBLIC_KEY, data);
	}
}
