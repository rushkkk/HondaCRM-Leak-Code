package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameEcu4 extends GwParam {
    public FrameEcu4(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_4);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TOUT_11, ValueNameDef.ENG_BTU_TOUT_11, ParamValue.VehicleGwInfo.FrameEcu4.ENG_BTU_TOUT_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_IG_11, ValueNameDef.ENG_BTU_IG_11, ParamValue.VehicleGwInfo.FrameEcu4.ENG_BTU_IG_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_PULCNT_11, ValueNameDef.ENG_BTU_PULCNT_11, ParamValue.VehicleGwInfo.FrameEcu4.ENG_BTU_PULCNT_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ICMD_11, ValueNameDef.ENG_BTU_ICMD_11, ParamValue.VehicleGwInfo.FrameEcu4.ENG_BTU_ICMD_11));
    }
}
