package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameOdoTrip extends GwParam {
    public FrameOdoTrip(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ODO_TRIP);
        addSubValue(new SubValue(SubValue.Type.DISPLAY_RANGE_UNIT, ValueNameRaw.C_DISPLAY_UNIT, ValueNameDef.C_DISPLAY_UNIT, ParamValue.VehicleGwInfo.FrameOdoTrip.C_DISPLAY_UNIT));
        addSubValue(new SubValue(SubValue.Type.DISPLAY_TRIP, ValueNameRaw.C_DISPLAY_TRIPA, ValueNameDef.C_DISPLAY_TRIPA, ParamValue.VehicleGwInfo.FrameOdoTrip.C_DISPLAY_TRIPA));
        addSubValue(new SubValue(SubValue.Type.DISPLAY_TRIP, ValueNameRaw.C_DISPLAY_TRIPB, ValueNameDef.C_DISPLAY_TRIPB, ParamValue.VehicleGwInfo.FrameOdoTrip.C_DISPLAY_TRIPB));
    }
}
