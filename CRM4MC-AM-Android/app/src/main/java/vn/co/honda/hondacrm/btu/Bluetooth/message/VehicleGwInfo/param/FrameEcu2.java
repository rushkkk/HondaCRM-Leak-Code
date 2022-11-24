package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param;


import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Constants.message.TagNameRaw;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameDef;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameRaw;
import vn.co.honda.hondacrm.btu.Model.message.common.GwParam;
import vn.co.honda.hondacrm.btu.Model.message.common.SubValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class FrameEcu2 extends GwParam {
    public FrameEcu2(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_2);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_NE_11, ValueNameDef.ENG_BTU_NE_11, ParamValue.VehicleGwInfo.FrameEcu2.ENG_BTU_NE_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TH_11, ValueNameDef.ENG_BTU_TH_11, ParamValue.VehicleGwInfo.FrameEcu2.ENG_BTU_TH_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_THDEG_11, ValueNameDef.ENG_BTU_THDEG_11, ParamValue.VehicleGwInfo.FrameEcu2.ENG_BTU_THDEG_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TW_11, ValueNameDef.ENG_BTU_TW_11, ParamValue.VehicleGwInfo.FrameEcu2.ENG_BTU_TW_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ECT_11, ValueNameDef.ENG_BTU_ECT_11, ParamValue.VehicleGwInfo.FrameEcu2.ENG_BTU_ECT_11));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TA_11, ValueNameDef.ENG_BTU_TA_11, ParamValue.VehicleGwInfo.FrameEcu2.ENG_BTU_TA_11));
    }
}
