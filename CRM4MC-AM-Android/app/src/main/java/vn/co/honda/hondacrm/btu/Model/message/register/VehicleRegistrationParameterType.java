package vn.co.honda.hondacrm.btu.Model.message.register;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParameterTypeUint8;

import static vn.co.honda.hondacrm.btu.Constants.message.ParamTag.REGISTRATION_TYPE;

public	class VehicleRegistrationParameterType
		extends VehicleParameterTypeUint8 {
	public VehicleRegistrationParameterType(int type) {
		super(REGISTRATION_TYPE, type);
	}
}
