package vn.co.honda.hondacrm.btu.Model.message;


import vn.co.honda.hondacrm.btu.Model.message.common.VehicleMessage;

import static vn.co.honda.hondacrm.btu.Constants.message.MsgId.VEHICLE_STATUS_INQUIRY;

public class VehicleMessageStatusInquiry
        extends VehicleMessage {
    //GW_VEHICLE_INFO_INDICATION
    public VehicleMessageStatusInquiry() {
        super(VEHICLE_STATUS_INQUIRY);
    }

}
