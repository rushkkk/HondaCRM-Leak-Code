package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param;


import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Constants.message.TagNameRaw;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameDef;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameRaw;
import vn.co.honda.hondacrm.btu.Model.message.common.GwParam;
import vn.co.honda.hondacrm.btu.Model.message.common.SubValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class FrameVspne extends GwParam {

    public FrameVspne(VehicleParam param) {
        super(param);
        setRawTagName(TagNameRaw.FRAME_VSPNE);
        addSubValue(new SubValue(SubValue.Type.KM_H, ValueNameRaw.C_VSP, ValueNameDef.C_VSP, ParamValue.VehicleGwInfo.FrameVspne.C_VSP));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.C_NE, ValueNameDef.C_NE, ParamValue.VehicleGwInfo.FrameVspne.C_NE));
    }
}
