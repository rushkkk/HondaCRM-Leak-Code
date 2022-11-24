package vn.co.honda.hondacrm.btu.Model.message.register.response;


import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import vn.co.honda.hondacrm.btu.Constants.message.ParamTag;
import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;
import vn.co.honda.hondacrm.btu.Model.message.register.security.VehicleParameterTypeSecurityConfigContainer;
import vn.co.honda.hondacrm.btu.Model.message.register.security.VehicleParameterTypeSecurityConfigType;

public	class		VehicleSecurityConfigMessage {
	private int mSecurityConfigType;
	private byte[] mSecurityConfigContainer;
	private static final int RESPONSE_CONTAINER_BYTE_SIZE_ID_EXCHANGE_ = 64;
	private static final int RESPONSE_CONTAINER_BYTE_SIZE_VERIFY_PIN = 256;
	private static final int RESPONSE_CONTAINER_BLOCK_NUM_ID_EXCHANGE = 2;
	private static final int RESPONSE_CONTAINER_BLOCK_NUM_VERIFY_PIN = 8;
	
	public VehicleSecurityConfigMessage(int configType, byte[] container) {
		mSecurityConfigType = configType;
		if (container != null) {
			mSecurityConfigContainer = container.clone();
		}
	}
	
	public VehicleSecurityConfigMessage(List<VehicleParam> paramList) {
		ByteBuffer containerBuf = null;
		int copyContainerBlock = 0;
		int copyContainerBlockMax = 0;
		for (VehicleParam param : paramList) {
			switch (param.mTag) {
			case ParamTag.SECURITY_CONFIG_TYPE:
				ByteBuffer buf = ByteBuffer.wrap(param.mValue);
				mSecurityConfigType = buf.get();
				if (containerBuf != null) {
					break;
				}
				if (mSecurityConfigType == ParamValue.Security.SecurityConfigType.ID_EXCHANGE) {
					containerBuf = ByteBuffer.allocate(RESPONSE_CONTAINER_BYTE_SIZE_ID_EXCHANGE_);
					copyContainerBlockMax = RESPONSE_CONTAINER_BLOCK_NUM_ID_EXCHANGE;
				} else if (mSecurityConfigType == ParamValue.Security.SecurityConfigType.VERIFY_PIN){
					containerBuf = ByteBuffer.allocate(RESPONSE_CONTAINER_BYTE_SIZE_VERIFY_PIN);
					copyContainerBlockMax = RESPONSE_CONTAINER_BLOCK_NUM_VERIFY_PIN;
				} else {
					break;
				}
				break;
			case ParamTag.SECURITY_CONFIG_CONTAINER:
				if (containerBuf == null) {
					break;
				}
				if (copyContainerBlock >= copyContainerBlockMax) {
					break;
				}
				containerBuf.put(param.mValue);
				copyContainerBlock++;
				break;
			default:
				break;
			}
		}
		if ((containerBuf != null) && (copyContainerBlock == copyContainerBlockMax)) {
			mSecurityConfigContainer = containerBuf.array();
		}
	}

	public int getSecurityConfigType() {
		return mSecurityConfigType;
	}

	public byte[] getSecurityConfigContainer() {
		if (mSecurityConfigContainer != null) {
			return mSecurityConfigContainer.clone();
		}
		return null;
	}

	public List<VehicleParam> getVehicleParameters() {
		if (mSecurityConfigContainer == null) {
			return null;
		}
		List<VehicleParam> paramList = new ArrayList<>();
		VehicleParameterTypeSecurityConfigType type = new VehicleParameterTypeSecurityConfigType(mSecurityConfigType);
		VehicleParameterTypeSecurityConfigContainer container = new VehicleParameterTypeSecurityConfigContainer(mSecurityConfigContainer);
		paramList.add(type);
		paramList.add(container);
		return paramList;
	}
}
