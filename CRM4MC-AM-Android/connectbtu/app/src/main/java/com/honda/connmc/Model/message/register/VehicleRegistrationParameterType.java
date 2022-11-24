package com.honda.connmc.Model.message.register;


import com.honda.connmc.Model.message.common.VehicleParameterTypeUint8;

import static com.honda.connmc.Constants.message.ParamTag.REGISTRATION_TYPE;


public	class VehicleRegistrationParameterType
		extends VehicleParameterTypeUint8 {
	public VehicleRegistrationParameterType(int type) {
		super(REGISTRATION_TYPE, type);
	}
}
