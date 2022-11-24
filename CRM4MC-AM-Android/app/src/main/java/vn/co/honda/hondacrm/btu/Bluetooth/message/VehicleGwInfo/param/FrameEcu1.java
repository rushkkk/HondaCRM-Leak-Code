package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param;


import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Constants.message.TagNameRaw;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameDef;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameRaw;
import vn.co.honda.hondacrm.btu.Model.message.common.GwParam;
import vn.co.honda.hondacrm.btu.Model.message.common.SubValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class FrameEcu1 extends GwParam {
    public FrameEcu1(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_1);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ECUID1_00, ValueNameDef.ENG_BTU_ECUID1_00, ParamValue.VehicleGwInfo.FrameEcu1.ENG_BTU_ECUID1_00));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ECUID2_00, ValueNameDef.ENG_BTU_ECUID2_00, ParamValue.VehicleGwInfo.FrameEcu1.ENG_BTU_ECUID2_00));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ECUID3_00, ValueNameDef.ENG_BTU_ECUID3_00, ParamValue.VehicleGwInfo.FrameEcu1.ENG_BTU_ECUID3_00));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ECUID4_00, ValueNameDef.ENG_BTU_ECUID4_00, ParamValue.VehicleGwInfo.FrameEcu1.ENG_BTU_ECUID4_00));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ECUID5_00, ValueNameDef.ENG_BTU_ECUID5_00, ParamValue.VehicleGwInfo.FrameEcu1.ENG_BTU_ECUID5_00));
    }
}
