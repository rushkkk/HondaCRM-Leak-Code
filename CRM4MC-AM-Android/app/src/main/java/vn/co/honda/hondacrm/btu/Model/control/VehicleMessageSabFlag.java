package vn.co.honda.hondacrm.btu.Model.control;


import vn.co.honda.hondacrm.btu.Constants.message.MsgId;
import vn.co.honda.hondacrm.btu.Constants.message.ParamTag;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleMessage;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParameterTypeUint8;

public class VehicleMessageSabFlag
        extends VehicleMessage {
    public final static byte SAB_FLAG_ON = 1;
    public final static byte SAB_FLAG_OFF = 0;
    public VehicleMessageSabFlag(byte sabFlag) {
        super(MsgId.GW_SMARTPHONE_INFO_INDICATION);
        addParams(new VehicleParameterTypeUint8((short) ParamTag.FRAME_SAB_FLAG, sabFlag));
    }

}
