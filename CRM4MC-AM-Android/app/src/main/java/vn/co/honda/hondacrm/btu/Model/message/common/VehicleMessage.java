package vn.co.honda.hondacrm.btu.Model.message.common;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Hold data of a message.
 */
public class VehicleMessage {
	public short mMsgId;
	public ArrayList<VehicleParam> mParamArray;

	/**
	 * Default constructor.
	 */
	public VehicleMessage() {
		mParamArray = new ArrayList<>();
	}

	/**
	 * Constructor with msg id.
	 *
	 * @param mMsgId from Msg.Id.
	 */
	public VehicleMessage(short mMsgId) {
		mParamArray = new ArrayList<>();
		this.mMsgId = mMsgId;
	}

	/**
	 * Constructor with msg id and param(s).
	 *
	 * @param mMsgId from Msg.Id.
	 * @param params VehicleParam.
	 */
	public VehicleMessage(short mMsgId, VehicleParam... params) {
		mParamArray = new ArrayList<>();
		this.mMsgId = mMsgId;
		mParamArray.addAll(Arrays.asList(params));
	}

	/**
	 * Constructor with msg id and ArrayList of params.
	 *
	 * @param mMsgId      from Msg.Id.
	 * @param mParamArray ArrayList<VehicleParam>.
	 */
	public VehicleMessage(short mMsgId, ArrayList<VehicleParam> mParamArray) {
		this.mMsgId = mMsgId;
		this.mParamArray = mParamArray;
	}


	public short getMsgId() {
		return mMsgId;
	}

	public void setMsgId(short mMsgId) {
		this.mMsgId = mMsgId;
	}

	public ArrayList<VehicleParam> getParams() {
		return mParamArray;
	}

	public void setParams(ArrayList<VehicleParam> mParamArray) {
		this.mParamArray = mParamArray;
	}

	/**
	 * Add params into message.
	 *
	 * @param params VehicleParam.
	 */
	public void addParams(VehicleParam... params) {
		if (mParamArray == null) {
			mParamArray = new ArrayList<>();
		}
		mParamArray.addAll(Arrays.asList(params));
	}

	/**
	 * Get size of message (Msg Id + params size + End of message size)
	 *
	 * @return size of VehicleMessage.
	 */
	public int getMsgSize() {
		int sizeOfParams = 0;
		if (mParamArray != null) {
			for (VehicleParam param : mParamArray) {
				sizeOfParams += param.getSize();
			}
		}
		// 5 = MsgId + E_O_M tag + value.
		return 5 + sizeOfParams;
	}
}

