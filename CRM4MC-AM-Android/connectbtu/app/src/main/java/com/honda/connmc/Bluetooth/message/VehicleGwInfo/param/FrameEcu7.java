package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameEcu7 extends GwParam {
    public FrameEcu7(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_7);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_INBIT1_D1, ValueNameDef.ENG_BTU_INBIT1_D1, ParamValue.VehicleGwInfo.FrameEcu7.ENG_BTU_INBIT1_D1));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_INBIT2_D1, ValueNameDef.ENG_BTU_INBIT2_D1, ParamValue.VehicleGwInfo.FrameEcu7.ENG_BTU_INBIT2_D1));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_INBIT3_D1, ValueNameDef.ENG_BTU_INBIT3_D1, ParamValue.VehicleGwInfo.FrameEcu7.ENG_BTU_INBIT3_D1));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_INBIT4_D1, ValueNameDef.ENG_BTU_INBIT4_D1, ParamValue.VehicleGwInfo.FrameEcu7.ENG_BTU_INBIT4_D1));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_OUTBIT1_D1, ValueNameDef.ENG_BTU_OUTBIT1_D1, ParamValue.VehicleGwInfo.FrameEcu7.ENG_BTU_OUTBIT1_D1));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_OUTBIT2_D1, ValueNameDef.ENG_BTU_OUTBIT2_D1, ParamValue.VehicleGwInfo.FrameEcu7.ENG_BTU_OUTBIT2_D1));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_OUTBIT3_D1, ValueNameDef.ENG_BTU_OUTBIT3_D1, ParamValue.VehicleGwInfo.FrameEcu7.ENG_BTU_OUTBIT3_D1));
    }
}
