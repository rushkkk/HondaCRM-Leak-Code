package vn.co.honda.hondacrm.btu.Utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.co.honda.hondacrm.btu.Constants.message.MsgId;
import vn.co.honda.hondacrm.btu.Constants.message.ParamTag;
import vn.co.honda.hondacrm.btu.Constants.message.ParamValue;
import vn.co.honda.hondacrm.btu.Model.BluetoothStatus;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleMessage;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class DumyData {
    public static final String PIN = "123456";
    public static final String USERNAME = "hvn";
    public static final String PASSWORD = "hvn2018";

    public static List<BluetoothStatus> getAllStatus() {
        List<BluetoothStatus> statusList = new ArrayList<>();
      /*  statusList.add(new BluetoothStatus("Engine status:", "OFF"));
        statusList.add(new BluetoothStatus("Lamp head status:", "Small On"));
        statusList.add(new BluetoothStatus("Lamp turn status:", "Right blink"));
        statusList.add(new BluetoothStatus("Distance to emlpy:", "283 km"));*/
        return statusList;
    }

    public static VehicleMessage createDummyVehicleGwMsg() {
        VehicleMessage vehGwMsg = new VehicleMessage(MsgId.GW_VEHICLE_INFO_INDICATION);
        vehGwMsg.addParams(createFrameMicu());
        vehGwMsg.addParams(createFrameVspne());
        vehGwMsg.addParams(createFrameEngtemp());

        return vehGwMsg;
    }

    private static VehicleParam createFrameMicu() {
        Map<String, Integer> micu = new HashMap<>();
        micu.put(ParamValue.VehicleGwInfo.FrameMicu.C_IGKEYSW, 1);
        micu.put(ParamValue.VehicleGwInfo.FrameMicu.C_STOP, 0);
        micu.put(ParamValue.VehicleGwInfo.FrameMicu.C_HAZARDSW, 1);
        byte[] param = MessageUtils.createPhoneGwMsgParam(ParamTag.getLength(ParamTag.FRAME_MICU), micu);
        LogUtils.w("ParamTag.getLength(ParamTag.FRAME_MICU) = " + ParamTag.getLength(ParamTag.FRAME_MICU));
        return new VehicleParam(ParamTag.FRAME_MICU, param);
    }

    private static VehicleParam createFrameVspne() {
        Map<String, Integer> subVal = new HashMap<>();
        subVal.put(ParamValue.VehicleGwInfo.FrameVspne.C_VSP, 0xF9);
        subVal.put(ParamValue.VehicleGwInfo.FrameVspne.C_NE, 0xFFFE);
        byte[] param = MessageUtils.createPhoneGwMsgParam(ParamTag.getLength(ParamTag.FRAME_VSPNE), subVal);
        LogUtils.w("ParamTag.getLength(ParamTag.FRAME_VSPNE) = " + ParamTag.getLength(ParamTag.FRAME_VSPNE));
        return new VehicleParam(ParamTag.FRAME_VSPNE, param);
    }

    private static VehicleParam createFrameEngtemp() {
        Map<String, Integer> subVal = new HashMap<>();
        subVal.put(ParamValue.VehicleGwInfo.FrameEngtemp.C_ENGTEMP, 0xBE);
        subVal.put(ParamValue.VehicleGwInfo.FrameEngtemp.C_ODO, 6969);
        byte[] param = MessageUtils.createPhoneGwMsgParam(ParamTag.getLength(ParamTag.FRAME_ENGTEMP), subVal);
        LogUtils.w("ParamTag.getLength(ParamTag.FRAME_ENGTEMP) = " + ParamTag.getLength(ParamTag.FRAME_ENGTEMP));
        return new VehicleParam(ParamTag.FRAME_ENGTEMP, param);
    }

}
