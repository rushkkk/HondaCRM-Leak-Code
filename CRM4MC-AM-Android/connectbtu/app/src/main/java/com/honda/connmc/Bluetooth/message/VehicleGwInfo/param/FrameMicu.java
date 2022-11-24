package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameMicu extends GwParam {

    public FrameMicu(VehicleParam param) {
        super(param);
        setRawTagName(TagNameRaw.FRAME_MICU);
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_IGNKEYSW, ValueNameDef.C_IGNKEYSW, ParamValue.VehicleGwInfo.FrameMicu.C_IGKEYSW));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_STOP, ValueNameDef.C_STOP, ParamValue.VehicleGwInfo.FrameMicu.C_STOP));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_HAZARD, ValueNameDef.C_HAZARD, ParamValue.VehicleGwInfo.FrameMicu.C_HAZARDSW));
    }
}
