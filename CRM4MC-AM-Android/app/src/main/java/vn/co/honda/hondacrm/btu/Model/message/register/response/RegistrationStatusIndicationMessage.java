package vn.co.honda.hondacrm.btu.Model.message.register.response;


import java.nio.ByteBuffer;
import java.util.List;

import vn.co.honda.hondacrm.btu.Constants.message.ParamTag;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

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
