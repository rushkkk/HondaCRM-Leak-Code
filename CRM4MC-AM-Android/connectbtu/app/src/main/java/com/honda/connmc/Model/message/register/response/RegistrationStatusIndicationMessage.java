package com.honda.connmc.Model.message.register.response;

import com.honda.connmc.Constants.message.ParamTag;
import com.honda.connmc.Model.message.common.VehicleParam;

import java.nio.ByteBuffer;
import java.util.List;

public class RegistrationStatusIndicationMessage {

	private int mRegistrationStatusValue;

	public RegistrationStatusIndicationMessage(List<VehicleParam> paramList) {
		ByteBuffer buf;
		for (VehicleParam param : paramList) {
			if (param == null) {
				continue;
			}
			switch (param.mTag) {
			case ParamTag.REGISTRATION_STATUS:
				buf = ByteBuffer.wrap(param.mValue);
				mRegistrationStatusValue = buf.get();
				break;

			default:
				break;
			}
		}
	}

	public int getRegistrationStatusValue() {
		return mRegistrationStatusValue;
	}
	
}
