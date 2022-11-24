package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param;


import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Constants.message.TagNameRaw;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameDef;
import vn.co.honda.hondacrm.btu.Constants.message.ValueNameRaw;
import vn.co.honda.hondacrm.btu.Model.message.common.GwParam;
import vn.co.honda.hondacrm.btu.Model.message.common.SubValue;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class FrameMaintance extends GwParam {
    public FrameMaintance(VehicleParam vehicleParam) {
        super(vehicleParam);
        setRawTagName(TagNameRaw.FRAME_MAINTANCE);
        addSubValue(new SubValue(SubValue.Type.OIL_LIFE, ValueNameRaw.C_MAINT_OIL_LIFE, ValueNameDef.C_MAINT_OIL_LIFE, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_OIL_LIFE));
        addSubValue(new SubValue(SubValue.Type.MAINT_INFO, ValueNameRaw.C_MAINT_INFO, ValueNameDef.C_MAINT_INFO, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_INFO));
        addSubValue(new SubValue(SubValue.Type.MAINT_DATA, ValueNameRaw.C_MAINT_DATA, ValueNameDef.C_MAINT_DATA, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_DATA));
        addSubValue(new SubValue(SubValue.Type.MAINT_UNIT, ValueNameRaw.C_MAINT_UNIT, ValueNameDef.C_MAINT_UNIT, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_UNIT));
        addSubValue(new SubValue(SubValue.Type.MAINT_RESULT, ValueNameRaw.C_MAINT_RESULT, ValueNameDef.C_MAINT_RESULT, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_RESULT));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_MAINT_OIL_IND, ValueNameDef.C_MAINT_OIL_IND, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_OIL_IND));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_MAINT_SUBMAINT_A, ValueNameDef.C_MAINT_SUBMAINT_A, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_SUBMAINT_A));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_MAINT_SUBMAINT_B, ValueNameDef.C_MAINT_SUBMAINT_B, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_SUBMAINT_B));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_MAINT_SUBMAINT_1, ValueNameDef.C_MAINT_SUBMAINT_1, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_SUBMAINT_1));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_MAINT_SUBMAINT_2, ValueNameDef.C_MAINT_SUBMAINT_2, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_SUBMAINT_2));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_MAINT_SUBMAINT_3, ValueNameDef.C_MAINT_SUBMAINT_3, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_SUBMAINT_3));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_MAINT_SUBMAINT_4, ValueNameDef.C_MAINT_SUBMAINT_4, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_SUBMAINT_4));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_MAINT_SUBMAINT_5, ValueNameDef.C_MAINT_SUBMAINT_5, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_SUBMAINT_5));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_MAINT_SUBMAINT_6, ValueNameDef.C_MAINT_SUBMAINT_6, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_SUBMAINT_6));
        addSubValue(new SubValue(SubValue.Type.ON_OFF, ValueNameRaw.C_MAINT_SUBMAINT_7, ValueNameDef.C_MAINT_SUBMAINT_7, ParamValue.VehicleGwInfo.FrameMaintance.C_MAINT_SUBMAINT_7));
        addSubValue(new SubValue(SubValue.Type.DISPLAY_CONDITION_MAINT, ValueNameRaw.C_I_DISPLAY_CONDITION_MAINT, ValueNameDef.C_I_DISPLAY_CONDITION_MAINT, ParamValue.VehicleGwInfo.FrameMaintance.C_I_DISPLAY_CONDITION_MAINT));
        addSubValue(new SubValue(SubValue.Type.CAR_CONDITION_MAINT, ValueNameRaw.C_I_CAR_CONDITION_MAINT, ValueNameDef.C_I_CAR_CONDITION_MAINT, ParamValue.VehicleGwInfo.FrameMaintance.C_I_CAR_CONDITION_MAINT));
        addSubValue(new SubValue(SubValue.Type.EXCHANGE_DIST, ValueNameRaw.C_EXCHANGE_OIL_DEST, ValueNameDef.C_EXCHANGE_OIL_DEST, ParamValue.VehicleGwInfo.FrameMaintance.C_EXCHANGE_OIL_DIST));
        addSubValue(new SubValue(SubValue.Type.EXCHANGE_DIST, ValueNameRaw.C_EXCHANGE_MAINT_DEST, ValueNameDef.C_EXCHANGE_MAINT_DEST, ParamValue.VehicleGwInfo.FrameMaintance.C_EXCHANGE_MAINT_DIST));
    }
}
