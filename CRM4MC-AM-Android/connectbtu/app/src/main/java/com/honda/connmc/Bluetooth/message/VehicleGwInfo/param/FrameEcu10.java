package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameEcu10 extends GwParam {
    public FrameEcu10(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_10);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_VB_18, ValueNameDef.ENG_BTU_VB_18, ParamValue.VehicleGwInfo.FrameEcu10.ENG_BTU_VB_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_VSP_18, ValueNameDef.ENG_BTU_VSP_18, ParamValue.VehicleGwInfo.FrameEcu10.ENG_BTU_VSP_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TOUT_18, ValueNameDef.ENG_BTU_TOUT_18, ParamValue.VehicleGwInfo.FrameEcu10.ENG_BTU_TOUT_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_IG_18, ValueNameDef.ENG_BTU_IG_18, ParamValue.VehicleGwInfo.FrameEcu10.ENG_BTU_IG_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_PULCNT_18, ValueNameDef.ENG_BTU_PULCNT_18, ParamValue.VehicleGwInfo.FrameEcu10.ENG_BTU_PULCNT_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ICMD_HIGH_18, ValueNameDef.ENG_BTU_ICMD_HIGH_18, ParamValue.VehicleGwInfo.FrameEcu10.ENG_BTU_ICMD_HIGH_18));
    }
}
