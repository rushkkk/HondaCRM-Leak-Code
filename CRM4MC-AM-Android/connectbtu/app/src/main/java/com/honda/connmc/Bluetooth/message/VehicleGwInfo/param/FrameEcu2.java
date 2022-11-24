package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameEcu2 extends GwParam {
    public FrameEcu2(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_2);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_NE_11, ValueNameDef.ENG_BTU_NE_11, ParamValue.VehicleGwInfo.FrameEcu2.ENG_BTU_NE_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TH_11, ValueNameDef.ENG_BTU_TH_11, ParamValue.VehicleGwInfo.FrameEcu2.ENG_BTU_TH_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_THDEG_11, ValueNameDef.ENG_BTU_THDEG_11, ParamValue.VehicleGwInfo.FrameEcu2.ENG_BTU_THDEG_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TW_11, ValueNameDef.ENG_BTU_TW_11, ParamValue.VehicleGwInfo.FrameEcu2.ENG_BTU_TW_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ECT_11, ValueNameDef.ENG_BTU_ECT_11, ParamValue.VehicleGwInfo.FrameEcu2.ENG_BTU_ECT_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TA_11, ValueNameDef.ENG_BTU_TA_11, ParamValue.VehicleGwInfo.FrameEcu2.ENG_BTU_TA_11));
    }
}
