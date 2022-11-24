package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param;


import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Constants.message.TagNameRaw;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameDef;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameRaw;
import vn.co.honda.hondacrm.btu.Model.message.common.GwParam;
import vn.co.honda.hondacrm.btu.Model.message.common.SubValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

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
