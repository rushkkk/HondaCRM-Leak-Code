package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameVspne extends GwParam {

    public FrameVspne(VehicleParam param) {
        super(param);
        setRawTagName(TagNameRaw.FRAME_VSPNE);
        addSubValue(new SubValue(SubValue.Type.KM_H, ValueNameRaw.C_VSP, ValueNameDef.C_VSP, ParamValue.VehicleGwInfo.FrameVspne.C_VSP));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.C_NE, ValueNameDef.C_NE, ParamValue.VehicleGwInfo.FrameVspne.C_NE));
    }
}
