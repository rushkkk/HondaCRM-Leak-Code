package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameEcu8 extends GwParam {
    public FrameEcu8(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_8);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_NE_18, ValueNameDef.ENG_BTU_NE_18, ParamValue.VehicleGwInfo.FrameEcu8.ENG_BTU_NE_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TH1_18, ValueNameDef.ENG_BTU_TH1_18, ParamValue.VehicleGwInfo.FrameEcu8.ENG_BTU_TH1_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_THDEG1_18, ValueNameDef.ENG_BTU_THDEG1_18, ParamValue.VehicleGwInfo.FrameEcu8.ENG_BTU_THDEG1_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TH2_18, ValueNameDef.ENG_BTU_TH2_18, ParamValue.VehicleGwInfo.FrameEcu8.ENG_BTU_TH2_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_THDEG2_18, ValueNameDef.ENG_BTU_THDEG2_18, ParamValue.VehicleGwInfo.FrameEcu8.ENG_BTU_THDEG2_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TW_18, ValueNameDef.ENG_BTU_TW_18, ParamValue.VehicleGwInfo.FrameEcu8.ENG_BTU_TW_18));
    }
}
