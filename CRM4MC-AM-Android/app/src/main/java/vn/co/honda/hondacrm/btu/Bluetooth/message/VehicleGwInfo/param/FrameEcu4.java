package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param;


import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Constants.message.TagNameRaw;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameDef;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameRaw;
import vn.co.honda.hondacrm.btu.Model.message.common.GwParam;
import vn.co.honda.hondacrm.btu.Model.message.common.SubValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class FrameEcu4 extends GwParam {
    public FrameEcu4(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_4);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TOUT_11, ValueNameDef.ENG_BTU_TOUT_11, ParamValue.VehicleGwInfo.FrameEcu4.ENG_BTU_TOUT_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_IG_11, ValueNameDef.ENG_BTU_IG_11, ParamValue.VehicleGwInfo.FrameEcu4.ENG_BTU_IG_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_PULCNT_11, ValueNameDef.ENG_BTU_PULCNT_11, ParamValue.VehicleGwInfo.FrameEcu4.ENG_BTU_PULCNT_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ICMD_11, ValueNameDef.ENG_BTU_ICMD_11, ParamValue.VehicleGwInfo.FrameEcu4.ENG_BTU_ICMD_11));
    }
}
