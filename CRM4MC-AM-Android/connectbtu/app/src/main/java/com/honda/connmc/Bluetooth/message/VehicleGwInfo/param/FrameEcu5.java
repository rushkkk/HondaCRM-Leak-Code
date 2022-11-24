package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameEcu5 extends GwParam {
    public FrameEcu5(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_5);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_VO2_20, ValueNameDef.ENG_BTU_VO2_20, ParamValue.VehicleGwInfo.FrameEcu5.ENG_BTU_VO2_20));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_KO2_20, ValueNameDef.ENG_BTU_KO2_20, ParamValue.VehicleGwInfo.FrameEcu5.ENG_BTU_KO2_20));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_O2HT_20, ValueNameDef.ENG_BTU_O2HT_20, ParamValue.VehicleGwInfo.FrameEcu5.ENG_BTU_O2HT_20));
    }
}
