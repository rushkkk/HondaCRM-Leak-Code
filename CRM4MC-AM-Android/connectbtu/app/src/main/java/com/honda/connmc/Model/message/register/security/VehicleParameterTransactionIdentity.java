package com.honda.connmc.Model.message.register.security;

import com.honda.connmc.Model.message.common.VehicleParameterTypeUint8;

import static com.honda.connmc.Constants.message.ParamTag.TRANSACTION_IDENTITY;

public	class VehicleParameterTransactionIdentity
		extends VehicleParameterTypeUint8 {
	public VehicleParameterTransactionIdentity(int data) {
		super(TRANSACTION_IDENTITY, data);
	}
}
