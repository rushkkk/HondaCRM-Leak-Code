package com.honda.connmc.Model.message.register.security;

import com.honda.connmc.Model.message.common.VehicleParameterTypeUint8;

import static com.honda.connmc.Constants.message.ParamTag.SUCCESS;

public	class VehicleParameterTypeResultSuccess
		extends VehicleParameterTypeUint8 {
	public VehicleParameterTypeResultSuccess(int data) {
		super(SUCCESS, data);
	}

	
}
