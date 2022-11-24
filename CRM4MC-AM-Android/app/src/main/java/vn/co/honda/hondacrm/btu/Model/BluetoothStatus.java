package vn.co.honda.hondacrm.btu.Model;

public class BluetoothStatus {
    private boolean isHeader;
    private String mId;
    private String mName;
    private String mRawName;
    private String mValue;
    private int mRawValue;

    public BluetoothStatus(boolean isHeader, String mId, String mName, String mRawName, String mValue, int mRawValue) {
        this.isHeader = isHeader;
        this.mId = mId;
        this.mName = mName;
        this.mRawName = mRawName;
        this.mValue = mValue;
        this.mRawValue = mRawValue;
    }

    public BluetoothStatus() {
    }

    public BluetoothStatus(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getRawName() {
        return mRawName;
    }

    public void setRawName(String mRawName) {
        this.mRawName = mRawName;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String mValue) {
        this.mValue = mValue;
    }

    public int getRawValue() {
        return mRawValue;
    }

    public void setRawValue(int mRawValue) {
        this.mRawValue = mRawValue;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
