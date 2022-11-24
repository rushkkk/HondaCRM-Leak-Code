package vn.co.honda.hondacrm.btu.Model.message.register.security;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParameterTypeUint8;

import static vn.co.honda.hondacrm.btu.Constants.message.ParamTag.SUCCESS;

public	class VehicleParameterTypeResultSuccess
		extends VehicleParameterTypeUint8 {
	public VehicleParameterTypeResultSuccess(int data) {
		super(SUCCESS, data);
	}

	
}
