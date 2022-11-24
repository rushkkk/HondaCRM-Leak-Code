package com.honda.connmc.Model.message.register.request;


import com.honda.connmc.Constants.message.MsgId;
import com.honda.connmc.Constants.message.ParamTag;
import com.honda.connmc.Model.message.common.VehicleMessage;
import com.honda.connmc.Model.message.register.VehicleRegistrationParameterType;
import com.honda.connmc.Model.message.register.security.VehicleParameterTypePublicKey;
import com.honda.connmc.Model.message.register.security.VehicleParameterTypeUserName;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import static com.honda.connmc.Constants.message.ParamTag.PUBLIC_KEY;
import static com.honda.connmc.Constants.message.ParamTag.USER_NAME;

public	class VehicleMessageRegistrationRequest
		extends VehicleMessage {
	private int mRegistrationType;
	private List<String> mUserNameList;
	private byte[] mPublicKey;

	private static final int VEHICLE_REGISTRATION_REQUEST_PUBLICKEY_PADSIZE = 0x6c;
	public VehicleMessageRegistrationRequest(int registrationType, List<String> userNameList, byte[] publicKey) {
		super(MsgId.REGISTRATION_REQUEST);
		mRegistrationType = registrationType;
		mUserNameList = userNameList;
		if (publicKey != null) {
			mPublicKey = publicKey.clone();
		}
		createVehicleParameters();
	}

	private void createVehicleParameters() {
		VehicleRegistrationParameterType type = new VehicleRegistrationParameterType(mRegistrationType);
		// add type param (VehicleRegistrationType)
		addParams(type);
		// add user name param
		if (mUserNameList != null && !mUserNameList.isEmpty()) {
			for (String name : mUserNameList) {
				byte[] byteName = convertUserNameToBytes(name);
				if (byteName != null) {
					VehicleParameterTypeUserName paramUser = new VehicleParameterTypeUserName(byteName);
					addParams(paramUser);
				}
			}
		} else {
			ByteBuffer emptyUserNameBuf = ByteBuffer.allocate(ParamTag.getLength(USER_NAME));
			byte[] emptyUserName = emptyUserNameBuf.array();
			Arrays.fill(emptyUserName, (byte)0);
			VehicleParameterTypeUserName paramUser = new VehicleParameterTypeUserName(emptyUserName);
			addParams(paramUser);
		}
		// add public key param
		if ((mPublicKey != null) && (mPublicKey.length <= PUBLIC_KEY)) {
			ByteBuffer keyData = ByteBuffer.allocate(ParamTag.getLength(PUBLIC_KEY));
			keyData.put(mPublicKey);
			byte padSizeByte = (byte)(VEHICLE_REGISTRATION_REQUEST_PUBLICKEY_PADSIZE & ParamTag.CHECK_SUM_MESSAGE);
			keyData.put(padSizeByte);
			VehicleParameterTypePublicKey paramKey = new VehicleParameterTypePublicKey(keyData.array());
			addParams(paramKey);
		}
	}

	private byte[] convertUserNameToBytes(String target) {
		byte[] convertName;
		try {
			convertName = target.getBytes("Shift-JIS");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		ByteBuffer sizedBuf = ByteBuffer.allocate(ParamTag.getLength(USER_NAME));
		if (convertName.length > ParamTag.getLength(USER_NAME)) {
			return null;
		}
		sizedBuf.put(convertName);
		return sizedBuf.array();
	}
}
