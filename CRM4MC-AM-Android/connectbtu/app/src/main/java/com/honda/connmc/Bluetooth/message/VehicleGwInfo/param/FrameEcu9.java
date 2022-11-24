package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameEcu9 extends GwParam {
    public FrameEcu9(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_9);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ECT_18, ValueNameDef.ENG_BTU_ECT_18, ParamValue.VehicleGwInfo.FrameEcu9.ENG_BTU_ECT_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TA_18, ValueNameDef.ENG_BTU_TA_18, ParamValue.VehicleGwInfo.FrameEcu9.ENG_BTU_TA_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_IAT_18, ValueNameDef.ENG_BTU_IAT_18, ParamValue.VehicleGwInfo.FrameEcu9.ENG_BTU_IAT_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_PB_18, ValueNameDef.ENG_BTU_PB_18, ParamValue.VehicleGwInfo.FrameEcu9.ENG_BTU_PB_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_MAP_18, ValueNameDef.ENG_BTU_MAP_18, ParamValue.VehicleGwInfo.FrameEcu9.ENG_BTU_MAP_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_PA_18, ValueNameDef.ENG_BTU_PA_18, ParamValue.VehicleGwInfo.FrameEcu9.ENG_BTU_PA_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_BARO_18, ValueNameDef.ENG_BTU_BARO_18, ParamValue.VehicleGwInfo.FrameEcu9.ENG_BTU_BARO_18));
    }
}
