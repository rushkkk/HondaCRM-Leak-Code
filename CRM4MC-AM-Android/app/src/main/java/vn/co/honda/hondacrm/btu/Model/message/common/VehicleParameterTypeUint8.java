package vn.co.honda.hondacrm.btu.Model.message.common;


import vn.co.honda.hondacrm.btu.Constants.message.ParamTag;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;

public 	class 	VehicleParameterTypeUint8
		extends VehicleParam {

	public VehicleParameterTypeUint8(short tag16, int data) {
		super(tag16);
		mValue		= new byte[1];
		mValue[0]	= (byte) (data & ParamTag.CHECK_SUM_MESSAGE);
		LogUtils.d("VehicleParameterTypeUint8():");
	}

	public int getUint8() {
		LogUtils.d("getUint8():");
		return	mValue[0] & ParamTag.CHECK_SUM_MESSAGE;
	}

}
