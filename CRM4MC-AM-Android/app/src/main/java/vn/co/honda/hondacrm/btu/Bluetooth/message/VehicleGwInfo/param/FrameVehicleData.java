package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param;


import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Constants.message.TagNameRaw;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameDef;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameRaw;
import vn.co.honda.hondacrm.btu.Model.message.common.GwParam;
import vn.co.honda.hondacrm.btu.Model.message.common.SubValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class FrameVehicleData extends GwParam {
    public FrameVehicleData(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_VEHICLE_DATA);
        addSubValue(new SubValue(SubValue.Type.VEH_DATA_TEMP, ValueNameRaw.C_ENG_OIL_TEMP, ValueNameDef.C_ENG_OIL_TEMP, ParamValue.VehicleGwInfo.FrameVehicleData.C_ENG_OIL_TEMP));
        addSubValue(new SubValue(SubValue.Type.ASP, ValueNameRaw.C_ASP, ValueNameDef.C_ASP, ParamValue.VehicleGwInfo.FrameVehicleData.C_ASP));
        addSubValue(new SubValue(SubValue.Type.BRAKE_PRESSURE, ValueNameRaw.C_BRAKE_PRESSURE, ValueNameDef.C_BRAKE_PRESSURE, ParamValue.VehicleGwInfo.FrameVehicleData.C_BRAKE_PRESSURE));
        addSubValue(new SubValue(SubValue.Type.VEH_DATA_TEMP, ValueNameRaw.C_INTAKE_AIR_TEMP, ValueNameDef.C_INTAKE_AIR_TEMP, ParamValue.VehicleGwInfo.FrameVehicleData.C_INTAKE_AIR_TEMP));
    }
}
