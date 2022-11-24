package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param;


import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Constants.message.TagNameRaw;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameDef;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameRaw;
import vn.co.honda.hondacrm.btu.Model.message.common.GwParam;
import vn.co.honda.hondacrm.btu.Model.message.common.SubValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class FrameMicu extends GwParam {

    public FrameMicu(VehicleParam param) {
        super(param);
        setRawTagName(TagNameRaw.FRAME_MICU);
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_IGNKEYSW, ValueNameDef.C_IGNKEYSW, ParamValue.VehicleGwInfo.FrameMicu.C_IGKEYSW));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_STOP, ValueNameDef.C_STOP, ParamValue.VehicleGwInfo.FrameMicu.C_STOP));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_HAZARD, ValueNameDef.C_HAZARD, ParamValue.VehicleGwInfo.FrameMicu.C_HAZARDSW));
    }
}
