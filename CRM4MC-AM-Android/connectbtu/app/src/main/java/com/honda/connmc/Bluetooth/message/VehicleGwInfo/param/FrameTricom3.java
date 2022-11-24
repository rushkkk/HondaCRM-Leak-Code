package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameTricom3 extends GwParam {
    public FrameTricom3(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_TRICOM3);
        addSubValue(new SubValue(SubValue.Type.DISPLAY_RANGE_UNIT, ValueNameRaw.C_TRICOM_RANGE_UNIT, ValueNameDef.C_TRICOM_RANGE_UNIT, ParamValue.VehicleGwInfo.FrameTricom3.C_TRICOM_RANGE_UNIT));
        addSubValue(new SubValue(SubValue.Type.TRICOM_RANGE, ValueNameRaw.C_TRICOM_RANGE, ValueNameDef.C_TRICOM_RANGE, ParamValue.VehicleGwInfo.FrameTricom3.C_TRICOM_RANGE));
        addSubValue(new SubValue(SubValue.Type.OUTTEMP, ValueNameRaw.C_TRICOM_OUTTEMP, ValueNameDef.C_TRICOM_OUTTEMP, ParamValue.VehicleGwInfo.FrameTricom3.C_TRICOM_OUTTEMP));
        addSubValue(new SubValue(SubValue.Type.HOUR, ValueNameRaw.C_TRICOM_ELAP_TIME_HR, ValueNameDef.C_TRICOM_ELAP_TIME_HR, ParamValue.VehicleGwInfo.FrameTricom3.C_TRICOM_ELAP_TIME_HR));
        addSubValue(new SubValue(SubValue.Type.MINUTE, ValueNameRaw.C_TRICOM_ELAP_TIME_MIN, ValueNameDef.C_TRICOM_ELAP_TIME_MIN, ParamValue.VehicleGwInfo.FrameTricom3.C_TRICOM_ELAP_TIME_MIN));
    }
}
