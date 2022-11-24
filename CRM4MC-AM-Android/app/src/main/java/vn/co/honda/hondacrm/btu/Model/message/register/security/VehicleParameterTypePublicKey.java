package vn.co.honda.hondacrm.btu.Model.message.register.security;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

import static vn.co.honda.hondacrm.btu.Constants.message.ParamTag.PUBLIC_KEY;

public	class	VehicleParameterTypePublicKey
		extends VehicleParam {
	public VehicleParameterTypePublicKey(byte[] data) {
		super(PUBLIC_KEY, data);
	}
}
