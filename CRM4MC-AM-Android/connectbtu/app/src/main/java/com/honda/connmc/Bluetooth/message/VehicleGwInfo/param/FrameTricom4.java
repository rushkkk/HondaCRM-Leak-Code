package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameTricom4 extends GwParam {
    public FrameTricom4(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_TRICOM4);
        addSubValue(new SubValue(SubValue.Type.TRICOM4_UNIT, ValueNameRaw.C_TRICOM_UNIT, ValueNameDef.C_TRICOM_UNIT, ParamValue.VehicleGwInfo.FrameTricom4.C_TRICOM4_UNIT));
        addSubValue(new SubValue(SubValue.Type.METER_ECO_FUEL, ValueNameRaw.C_METER_ECO_INST_FUEL, ValueNameDef.C_METER_ECO_INST_FUEL, ParamValue.VehicleGwInfo.FrameTricom4.C_METER_ECO_INST_FUEL));
        addSubValue(new SubValue(SubValue.Type.METER_ECO_FUEL, ValueNameRaw.C_METER_ECO_AVG_FUEL, ValueNameDef.C_METER_ECO_AVG_FUEL, ParamValue.VehicleGwInfo.FrameTricom4.C_METER_ECO_AVG_FUEL));
        addSubValue(new SubValue(SubValue.Type.METER_ECO_FUEL, ValueNameRaw.C_METER_FUEL_CONSUMPTION, ValueNameDef.C_METER_FUEL_CONSUMPTION, ParamValue.VehicleGwInfo.FrameTricom4.C_METER_FUEL_CONSUMPTION));
        addSubValue(new SubValue(SubValue.Type.METER_FUEL_REMAIN, ValueNameRaw.C_METER_FUEL_REMAIN, ValueNameDef.C_METER_FUEL_REMAIN, ParamValue.VehicleGwInfo.FrameTricom4.C_METER_FUEL_REMAIN));
    }
}
