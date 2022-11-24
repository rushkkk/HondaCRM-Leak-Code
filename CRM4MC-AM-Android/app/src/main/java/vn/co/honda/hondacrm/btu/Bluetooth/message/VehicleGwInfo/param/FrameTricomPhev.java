package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param;


import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Constants.message.TagNameRaw;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameDef;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameRaw;
import vn.co.honda.hondacrm.btu.Model.message.common.GwParam;
import vn.co.honda.hondacrm.btu.Model.message.common.SubValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class FrameTricomPhev extends GwParam {
    public FrameTricomPhev(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_TRICOM_PHEV);
        addSubValue(new SubValue(SubValue.Type.TRICOM_RANGE, ValueNameRaw.C_RANGE_EV, ValueNameDef.C_RANGE_EV, ParamValue.VehicleGwInfo.FrameTricomPhev.C_RANGE_EV));
        addSubValue(new SubValue(SubValue.Type.TRICOM_RANGE, ValueNameRaw.C_RANGE_TOTAL, ValueNameDef.C_RANGE_TOTAL, ParamValue.VehicleGwInfo.FrameTricomPhev.C_RANGE_TOTAL));
    }
}
