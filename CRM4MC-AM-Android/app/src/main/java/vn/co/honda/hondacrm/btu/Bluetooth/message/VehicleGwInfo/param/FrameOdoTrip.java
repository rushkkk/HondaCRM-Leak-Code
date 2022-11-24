package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param;


import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Constants.message.TagNameRaw;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameDef;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameRaw;
import vn.co.honda.hondacrm.btu.Model.message.common.GwParam;
import vn.co.honda.hondacrm.btu.Model.message.common.SubValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class FrameOdoTrip extends GwParam {
    public FrameOdoTrip(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_ODO_TRIP);
        addSubValue(new SubValue(SubValue.Type.DISPLAY_RANGE_UNIT, ValueNameRaw.C_DISPLAY_UNIT, ValueNameDef.C_DISPLAY_UNIT, ParamValue.VehicleGwInfo.FrameOdoTrip.C_DISPLAY_UNIT));
        addSubValue(new SubValue(SubValue.Type.DISPLAY_TRIP, ValueNameRaw.C_DISPLAY_TRIPA, ValueNameDef.C_DISPLAY_TRIPA, ParamValue.VehicleGwInfo.FrameOdoTrip.C_DISPLAY_TRIPA));
        addSubValue(new SubValue(SubValue.Type.DISPLAY_TRIP, ValueNameRaw.C_DISPLAY_TRIPB, ValueNameDef.C_DISPLAY_TRIPB, ParamValue.VehicleGwInfo.FrameOdoTrip.C_DISPLAY_TRIPB));
    }
}
