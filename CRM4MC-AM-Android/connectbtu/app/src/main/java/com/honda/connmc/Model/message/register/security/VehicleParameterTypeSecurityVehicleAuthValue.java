package com.honda.connmc.Model.message.register.security;

import com.honda.connmc.Model.message.common.VehicleParam;

import static com.honda.connmc.Constants.message.ParamTag.VEHICLE_AUTH_VALUE;


public	class		VehicleParameterTypeSecurityVehicleAuthValue
		extends VehicleParam {
	public VehicleParameterTypeSecurityVehicleAuthValue(byte[] data) {
		super(VEHICLE_AUTH_VALUE, data);
	}
	
}
