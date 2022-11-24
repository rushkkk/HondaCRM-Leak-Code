package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameSw extends GwParam {

    public FrameSw(VehicleParam param) {
        super(param);
        setRawTagName(TagNameRaw.FRAME_SW);
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_SW_UP, ValueNameDef.C_SW_UP, ParamValue.VehicleGwInfo.FrameSw.C_UP));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_SW_DOWN, ValueNameDef.C_SW_DOWN, ParamValue.VehicleGwInfo.FrameSw.C_DOWN));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_SW_ENT, ValueNameDef.C_SW_ENT, ParamValue.VehicleGwInfo.FrameSw.C_ENT));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_SW_BACK, ValueNameDef.C_SW_BACK, ParamValue.VehicleGwInfo.FrameSw.C_BACK));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_SW_RESERVE, ValueNameDef.C_SW_RESERVE, ParamValue.VehicleGwInfo.FrameSw.C_RESERVE));
    }
}
