package vn.co.honda.hondacrm.btu.Model.message.register.security;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

import static vn.co.honda.hondacrm.btu.Constants.message.ParamTag.AUTH_RESPONSE_VALUE;

public class VehicleParameterTypeSecurityAuthResponseValue
        extends VehicleParam {
    public VehicleParameterTypeSecurityAuthResponseValue(byte[] data) {
        super(AUTH_RESPONSE_VALUE, data);
    }


}
