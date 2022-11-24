package com.honda.connmc.Model.message;

import com.honda.connmc.Model.message.common.VehicleMessage;

import static com.honda.connmc.Constants.message.MsgId.VEHICLE_STATUS_INQUIRY;

public class VehicleMessageStatusInquiry
        extends VehicleMessage {
    //GW_VEHICLE_INFO_INDICATION
    public VehicleMessageStatusInquiry() {
        super(VEHICLE_STATUS_INQUIRY);
    }

}
