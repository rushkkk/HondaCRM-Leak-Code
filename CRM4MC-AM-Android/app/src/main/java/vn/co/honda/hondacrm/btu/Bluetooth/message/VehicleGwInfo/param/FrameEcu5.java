package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param;


import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Constants.message.TagNameRaw;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameDef;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameRaw;
import vn.co.honda.hondacrm.btu.Model.message.common.GwParam;
import vn.co.honda.hondacrm.btu.Model.message.common.SubValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class FrameEcu5 extends GwParam {
    public FrameEcu5(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_5);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_VO2_20, ValueNameDef.ENG_BTU_VO2_20, ParamValue.VehicleGwInfo.FrameEcu5.ENG_BTU_VO2_20));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_KO2_20, ValueNameDef.ENG_BTU_KO2_20, ParamValue.VehicleGwInfo.FrameEcu5.ENG_BTU_KO2_20));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_O2HT_20, ValueNameDef.ENG_BTU_O2HT_20, ParamValue.VehicleGwInfo.FrameEcu5.ENG_BTU_O2HT_20));
    }
}
