package vn.co.honda.hondacrm.btu.Model.message.register.security;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

import static vn.co.honda.hondacrm.btu.Constants.message.ParamTag.VEHICLE_AUTH_VALUE;

public class VehicleParameterTypeSecurityVehicleAuthValue
        extends VehicleParam {
    public VehicleParameterTypeSecurityVehicleAuthValue(byte[] data) {
        super(VEHICLE_AUTH_VALUE, data);
    }

}
