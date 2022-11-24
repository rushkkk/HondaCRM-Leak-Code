package com.honda.connmc.Model.message.common;

import com.honda.connmc.Model.BluetoothStatus;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.Utils.MessageUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Same as VehicleParam, but have a list of sub-param index.
 */
public class GwParam extends VehicleParam {
    private String mRawTagName;
    private List<SubValue> mListSubValue = new ArrayList<>();

    public GwParam(VehicleParam vehicleParam) {
        super(vehicleParam);
    }

    public GwParam(short mTag) {
        super(mTag);
    }

    public GwParam(short mTag, byte[] mValue) {
        super(mTag, mValue);
    }

    public LinkedHashMap<String, BluetoothStatus> getAllStatus() {
        if (mListSubValue == null) return null;
        LinkedHashMap<String, BluetoothStatus> statuses = new LinkedHashMap<>();
        BluetoothStatus statusHeader = new BluetoothStatus(true);
        statusHeader.setName(mRawTagName);
        statuses.put(mRawTagName, statusHeader);
        for (SubValue subValue : mListSubValue) {
            subValue.setValue(MessageUtils.getValueFromByteArr(mValue, subValue.getBitLayout()));

            BluetoothStatus status = new BluetoothStatus(false);
            status.setId(getRawTagName() + "_" + subValue.getRawName());
            status.setName(subValue.getName());
            status.setRawName(subValue.getRawName());
            status.setRawValue(subValue.getValue());
            status.setValue(subValue.getReadableValue());

            statuses.put(status.getId(), status);
            LogUtils.d("Subvalue: name = " + subValue.getName() + " \t\t value = " + subValue.getValue());
        }

        LogUtils.d("List status size = " + statuses.size());
        return statuses;
    }

    public void addSubValue(SubValue subValue) {
        mListSubValue.add(subValue);
    }

    public List<SubValue> getListSubValue() {
        return mListSubValue;
    }

    public String getRawTagName() {
        return mRawTagName;
    }

    public void setRawTagName(String rawTagName) {
        this.mRawTagName = rawTagName;
    }
}
