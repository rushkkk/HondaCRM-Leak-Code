package vn.co.honda.hondacrm.btu.Model.message.register.security;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

import static vn.co.honda.hondacrm.btu.Constants.message.ParamTag.USER_NAME;

public	class	VehicleParameterTypeUserName
		extends VehicleParam {
	public VehicleParameterTypeUserName(byte[] data) {
		super(USER_NAME, data);
	}
}
