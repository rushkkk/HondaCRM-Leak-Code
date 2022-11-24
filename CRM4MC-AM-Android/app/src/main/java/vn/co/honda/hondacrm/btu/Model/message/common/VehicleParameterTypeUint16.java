package vn.co.honda.hondacrm.btu.Model.message.common;


import java.nio.ByteBuffer;

import vn.co.honda.hondacrm.btu.Constants.message.ParamTag;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;

public 	class 	VehicleParameterTypeUint16 
		extends VehicleParam {

	public VehicleParameterTypeUint16(short tag16, int data) {
		super(tag16);
		ByteBuffer buf	= ByteBuffer.allocate(2);
		buf.putShort((short) (data & ParamTag.END_OF_MESSAGE));
		mValue	= buf.array();
		LogUtils.d("VehicleParameterTypeUint16():");
	}

	public int getUint16() {
		LogUtils.d("getUint16():");
		ByteBuffer buf	= ByteBuffer.wrap(mValue);
		return buf.getShort() & ParamTag.END_OF_MESSAGE;
	}

}
