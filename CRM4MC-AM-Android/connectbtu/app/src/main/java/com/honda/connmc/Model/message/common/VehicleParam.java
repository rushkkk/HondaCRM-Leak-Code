package com.honda.connmc.Model.message.common;


/**
 * Param of a message.
 */
public class VehicleParam {
    public int mTag;
    public byte[] mValue;

    public VehicleParam(int mTag) {
        this.mTag = mTag;
    }

    public VehicleParam(int mTag, byte[] value) {
        this.mTag = mTag;
        if (value != null) {
            this.mValue = value.clone();
        }
    }

    public VehicleParam(VehicleParam vehicleParam) {
        this.mTag = vehicleParam.getTag();
        this.mValue = vehicleParam.getValue();
    }

    /**
     * Get size of param.
     *
     * @return int size of tag + value.
     */
    public int getSize() {
        return 2 + mValue.length;
    }

    public int getTag() {
        return mTag;
    }

    public void setTag(int mTag) {
        this.mTag = mTag;
    }

    public byte[] getValue() {
        if (mValue != null) {
            return mValue.clone();
        }
        return null;
    }

    public void setValue(byte[] value) {
        if (value != null) {
            this.mValue = value.clone();
        }
    }
}
