package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameEngtemp extends GwParam {

    public FrameEngtemp(VehicleParam param) {
        super(param);
        setRawTagName(TagNameRaw.FRAME_ENGTEMP);
        addSubValue(new SubValue(SubValue.Type.ENGTEMP, ValueNameRaw.C_ENGTEMP, ValueNameDef.C_ENGTEMP, ParamValue.VehicleGwInfo.FrameEngtemp.C_ENGTEMP));
        addSubValue(new SubValue(SubValue.Type.KM, ValueNameRaw.C_ODO, ValueNameDef.C_ODO, ParamValue.VehicleGwInfo.FrameEngtemp.C_ODO));
    }
}
