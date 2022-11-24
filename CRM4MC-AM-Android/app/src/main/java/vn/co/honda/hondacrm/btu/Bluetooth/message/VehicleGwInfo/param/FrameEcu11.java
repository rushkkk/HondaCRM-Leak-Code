package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param;


import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Constants.message.TagNameRaw;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameDef;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameRaw;
import vn.co.honda.hondacrm.btu.Model.message.common.GwParam;
import vn.co.honda.hondacrm.btu.Model.message.common.SubValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class FrameEcu11 extends GwParam {
    public FrameEcu11(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ECU_11);
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_ICMD_LOW_18, ValueNameDef.ENG_BTU_ICMD_LOW_18, ParamValue.VehicleGwInfo.FrameEcu11.ENG_BTU_ICMD_LOW_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_BATP_18, ValueNameDef.ENG_BTU_BATP_18, ParamValue.VehicleGwInfo.FrameEcu11.ENG_BTU_BATP_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_SUBVB_18, ValueNameDef.ENG_BTU_SUBVB_18, ParamValue.VehicleGwInfo.FrameEcu11.ENG_BTU_SUBVB_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_VRELEY_18, ValueNameDef.ENG_BTU_VRELEY_18, ParamValue.VehicleGwInfo.FrameEcu11.ENG_BTU_VRELEY_18));
        addSubValue(new SubValue(SubValue.Type.RAW_VALUE, ValueNameRaw.ENG_BTU_TRQDEMAND_18, ValueNameDef.ENG_BTU_TRQDEMAND_18, ParamValue.VehicleGwInfo.FrameEcu11.ENG_BTU_TRQDEMAND_18));
    }
}
