package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameEcu3 extends GwParam {
    public FrameEcu3(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_3);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_IAT_11, ValueNameDef.ENG_BTU_IAT_11, ParamValue.VehicleGwInfo.FrameEcu3.ENG_BTU_IAT_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_PB_11, ValueNameDef.ENG_BTU_PB_11, ParamValue.VehicleGwInfo.FrameEcu3.ENG_BTU_PB_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_MAP_11, ValueNameDef.ENG_BTU_MAP_11, ParamValue.VehicleGwInfo.FrameEcu3.ENG_BTU_MAP_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_PA_11, ValueNameDef.ENG_BTU_PA_11, ParamValue.VehicleGwInfo.FrameEcu3.ENG_BTU_PA_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_BARO_11, ValueNameDef.ENG_BTU_BARO_11, ParamValue.VehicleGwInfo.FrameEcu3.ENG_BTU_BARO_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_VB_11, ValueNameDef.ENG_BTU_VB_11, ParamValue.VehicleGwInfo.FrameEcu3.ENG_BTU_VB_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_VSP_11, ValueNameDef.ENG_BTU_VSP_11, ParamValue.VehicleGwInfo.FrameEcu3.ENG_BTU_VSP_11));
    }
}
