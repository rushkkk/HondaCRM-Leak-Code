package com.honda.connmc.Bluetooth.message.VehicleGwInfo.param;

import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.message.TagNameRaw;
import com.honda.connmc.Constants.message.ValueNameDef;
import com.honda.connmc.Constants.message.ValueNameRaw;
import com.honda.connmc.Model.message.common.GwParam;
import com.honda.connmc.Model.message.common.SubValue;
import com.honda.connmc.Model.message.common.VehicleParam;

public class FrameEcu6 extends GwParam {
    public FrameEcu6(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_6);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_NTRBCD_MILSTAT_D0, ValueNameDef.ENG_BTU_NTRBCD_MILSTAT_D0, ParamValue.VehicleGwInfo.FrameEcu6.ENG_BTU_NTRBCD_MILSTAT_D0));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TIMB_D0, ValueNameDef.ENG_BTU_TIMB_D0, ParamValue.VehicleGwInfo.FrameEcu6.ENG_BTU_TIMB_D0));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_BA_D0, ValueNameDef.ENG_BTU_BA_D0, ParamValue.VehicleGwInfo.FrameEcu6.ENG_BTU_BA_D0));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_MAFREF_D0, ValueNameDef.ENG_BTU_MAFREF_D0, ParamValue.VehicleGwInfo.FrameEcu6.ENG_BTU_MAFREF_D0));
    }
}
