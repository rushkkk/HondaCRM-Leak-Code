package vn.co.honda.hondacrm.btu.Model.message.register.security;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

import static vn.co.honda.hondacrm.btu.Constants.message.ParamTag.SECURITY_CONFIG_CONTAINER;

public	class		VehicleParameterTypeSecurityConfigContainer
		extends VehicleParam {
	public VehicleParameterTypeSecurityConfigContainer(byte[] data) {
		super(SECURITY_CONFIG_CONTAINER, data);
	}

	
}
