package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameEcu11 extends GwParam {
    public FrameEcu11(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_11);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ICMD_LOW_18, ValueNameDef.ENG_BTU_ICMD_LOW_18, ParamValue.VehicleGwInfo.FrameEcu11.ENG_BTU_ICMD_LOW_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_BATP_18, ValueNameDef.ENG_BTU_BATP_18, ParamValue.VehicleGwInfo.FrameEcu11.ENG_BTU_BATP_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_SUBVB_18, ValueNameDef.ENG_BTU_SUBVB_18, ParamValue.VehicleGwInfo.FrameEcu11.ENG_BTU_SUBVB_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_VRELEY_18, ValueNameDef.ENG_BTU_VRELEY_18, ParamValue.VehicleGwInfo.FrameEcu11.ENG_BTU_VRELEY_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TRQDEMAND_18, ValueNameDef.ENG_BTU_TRQDEMAND_18, ParamValue.VehicleGwInfo.FrameEcu11.ENG_BTU_TRQDEMAND_18));
    }
}
