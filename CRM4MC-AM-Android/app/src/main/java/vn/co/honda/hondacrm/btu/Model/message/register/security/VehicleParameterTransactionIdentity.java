package vn.co.honda.hondacrm.btu.Model.message.register.security;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParameterTypeUint8;

import static vn.co.honda.hondacrm.btu.Constants.message.ParamTag.TRANSACTION_IDENTITY;

public	class VehicleParameterTransactionIdentity
		extends VehicleParameterTypeUint8 {
	public VehicleParameterTransactionIdentity(int data) {
		super(TRANSACTION_IDENTITY, data);
	}
}
