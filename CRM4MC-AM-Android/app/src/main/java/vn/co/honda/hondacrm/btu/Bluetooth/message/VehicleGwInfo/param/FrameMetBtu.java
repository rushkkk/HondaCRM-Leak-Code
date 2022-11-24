package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param;


import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Constants.message.TagNameRaw;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameDef;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameRaw;
import vn.co.honda.hondacrm.btu.Model.message.common.GwParam;
import vn.co.honda.hondacrm.btu.Model.message.common.SubValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class FrameMetBtu extends GwParam {

    public FrameMetBtu(VehicleParam param) {
        super(param);
        setRawTagName(TagNameRaw.FRAME_MET_BTU);
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_REQ_REGMODE_MET, ValueNameDef.C_REQ_REGMODE_MET, ParamValue.VehicleGwInfo.FrameMetBtu.C_REQ_REGMODE_MET));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_REQ_CLEAR_MET, ValueNameDef.C_REQ_CLEAR_MET, ParamValue.VehicleGwInfo.FrameMetBtu.C_REQ_CLEAR_MET));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_WEATHER_INFO_REQ, ValueNameDef.C_WEATHER_INFO_REQ, ParamValue.VehicleGwInfo.FrameMetBtu.C_WEATHER_INFO_REQ));
        addSubValue(new SubValue(SubValue.Type.RES_REFTIME, ValueNameRaw.C_RES_RFTIME, ValueNameDef.C_RES_RFTIME, ParamValue.VehicleGwInfo.FrameMetBtu.C_RES_REFTIME));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_SAB_REQ_MET, ValueNameDef.C_SAB_REQ_MET, ParamValue.VehicleGwInfo.FrameMetBtu.C_SAB_REQ_MET));
    }
}
