package vn.co.honda.hondacrm.btu.Model.message.register.security;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

import static vn.co.honda.hondacrm.btu.Constants.message.ParamTag.RANDOM_VALUE;

public class VehicleParameterTypeSecurityRandomValue
        extends VehicleParam {
    public VehicleParameterTypeSecurityRandomValue(byte[] data) {
        super(RANDOM_VALUE, data);
    }
}