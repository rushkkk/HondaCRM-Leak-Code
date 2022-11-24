package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param;


import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Constants.message.TagNameRaw;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameDef;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameRaw;
import vn.co.honda.hondacrm.btu.Model.message.common.GwParam;
import vn.co.honda.hondacrm.btu.Model.message.common.SubValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class FrameEngtemp extends GwParam {

    public FrameEngtemp(VehicleParam param) {
        super(param);
        setRawTagName(TagNameRaw.FRAME_ENGTEMP);
        addSubValue(new SubValue(SubValue.Type.ENGTEMP, ValueNameRaw.C_ENGTEMP, ValueNameDef.C_ENGTEMP, ParamValue.VehicleGwInfo.FrameEngtemp.C_ENGTEMP));
        addSubValue(new SubValue(SubValue.Type.KM, ValueNameRaw.C_ODO, ValueNameDef.C_ODO, ParamValue.VehicleGwInfo.FrameEngtemp.C_ODO));
    }
}
