package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameEcu12 extends GwParam {
    public FrameEcu12(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_12);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_SOC_18, ValueNameDef.ENG_BTU_SOC_18, ParamValue.VehicleGwInfo.FrameEcu12.ENG_BTU_SOC_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_PACKV_18, ValueNameDef.ENG_BTU_PACKV_18, ParamValue.VehicleGwInfo.FrameEcu12.ENG_BTU_PACKV_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_PACKC_18, ValueNameDef.ENG_BTU_PACKC_18, ParamValue.VehicleGwInfo.FrameEcu12.ENG_BTU_PACKC_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_PACKT_MAX_18, ValueNameDef.ENG_BTU_PACKT_MAX_18, ParamValue.VehicleGwInfo.FrameEcu12.ENG_BTU_PACKT_MAX_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_PACKT_MIN_18, ValueNameDef.ENG_BTU_PACKT_MIN_18, ParamValue.VehicleGwInfo.FrameEcu12.ENG_BTU_PACKT_MIN_18));
    }
}
