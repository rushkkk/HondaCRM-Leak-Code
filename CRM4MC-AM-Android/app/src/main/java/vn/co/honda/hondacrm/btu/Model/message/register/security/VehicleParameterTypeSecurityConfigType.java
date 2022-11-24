package vn.co.honda.hondacrm.btu.Model.message.register.security;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParameterTypeUint8;

import static vn.co.honda.hondacrm.btu.Constants.message.ParamTag.SECURITY_CONFIG_TYPE;

public	class	VehicleParameterTypeSecurityConfigType
		extends VehicleParameterTypeUint8 {
	public VehicleParameterTypeSecurityConfigType(int type) {
		super(SECURITY_CONFIG_TYPE, type);
	}
}
