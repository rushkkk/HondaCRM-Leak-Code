package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameEcu1 extends GwParam {
    public FrameEcu1(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_1);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ECUID1_00, ValueNameDef.ENG_BTU_ECUID1_00, ParamValue.VehicleGwInfo.FrameEcu1.ENG_BTU_ECUID1_00));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ECUID2_00, ValueNameDef.ENG_BTU_ECUID2_00, ParamValue.VehicleGwInfo.FrameEcu1.ENG_BTU_ECUID2_00));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ECUID3_00, ValueNameDef.ENG_BTU_ECUID3_00, ParamValue.VehicleGwInfo.FrameEcu1.ENG_BTU_ECUID3_00));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ECUID4_00, ValueNameDef.ENG_BTU_ECUID4_00, ParamValue.VehicleGwInfo.FrameEcu1.ENG_BTU_ECUID4_00));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ECUID5_00, ValueNameDef.ENG_BTU_ECUID5_00, ParamValue.VehicleGwInfo.FrameEcu1.ENG_BTU_ECUID5_00));
    }
}
