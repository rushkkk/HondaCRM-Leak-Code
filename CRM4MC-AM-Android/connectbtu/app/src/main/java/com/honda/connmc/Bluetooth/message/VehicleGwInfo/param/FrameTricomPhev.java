package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameTricomPhev extends GwParam {
    public FrameTricomPhev(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_TRICOM_PHEV);
        addSubValue(new SubValue(SubValue.Type.TRICOM_RANGE, ValueNameRaw.C_RANGE_EV, ValueNameDef.C_RANGE_EV, ParamValue.VehicleGwInfo.FrameTricomPhev.C_RANGE_EV));
        addSubValue(new SubValue(SubValue.Type.TRICOM_RANGE, ValueNameRaw.C_RANGE_TOTAL, ValueNameDef.C_RANGE_TOTAL, ParamValue.VehicleGwInfo.FrameTricomPhev.C_RANGE_TOTAL));
    }
}
