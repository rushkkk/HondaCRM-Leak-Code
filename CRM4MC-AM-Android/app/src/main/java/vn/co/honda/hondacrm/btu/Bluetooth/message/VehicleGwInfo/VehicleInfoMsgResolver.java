package vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo;


import java.util.ArrayList;
import java.util.LinkedHashMap;

import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameEcu1;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameEcu10;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameEcu11;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameEcu12;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameEcu2;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameEcu3;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameEcu4;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameEcu5;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameEcu6;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameEcu7;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameEcu8;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameEcu9;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameEngtemp;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameMaintance;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameMetBtu;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameMicu;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameOdoTrip;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameSw;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameTricom3;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameTricom4;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameTricomPhev;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameVehicleData;
import vn.co.honda.hondacrm.btu.Bluetooth.message.VehicleGwInfo.param.FrameVspne;
import vn.co.honda.hondacrm.btu.Constants.message.ParamTag;
import vn.co.honda.hondacrm.btu.Model.BluetoothStatus;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleMessage;
import vn.co.honda.hondacrm.btu.Model.message.common.VehicleParam;

public class VehicleInfoMsgResolver {
    private static VehicleInfoMsgResolver sInstance;
    private VehicleInfoListener mListener;
    private VehicleMessage mMsg;

    public static VehicleInfoMsgResolver getInstance() {
        if (sInstance == null) {
            sInstance = new VehicleInfoMsgResolver();
        }
        return sInstance;
    }

    private VehicleInfoMsgResolver() {
    }

    public interface VehicleInfoListener {
        void onResolvedVehicleInfo(LinkedHashMap<String, BluetoothStatus> statuses);
    }

    public void registerListener(VehicleInfoListener listener) {
        this.mListener = listener;
    }

    public void unRegisterListener(VehicleInfoListener listener) {
        if (this.mListener == listener) {
            this.mListener = null;
        }
    }

    /**
     * Extract data from message then notify listener.
     *
     * @param message VehicleGwInfoMessage.
     */
    public void resolveMsg(VehicleMessage message) {
        mMsg = message;
        ArrayList<VehicleParam> params = mMsg.getParams();
        LinkedHashMap<String, BluetoothStatus> statuses = new LinkedHashMap<>();
        for (VehicleParam param : params) {
            LinkedHashMap<String, BluetoothStatus> bluetoothStatus = getDataFromParam(param);
            if (bluetoothStatus != null && !bluetoothStatus.isEmpty()) {
                statuses.putAll(bluetoothStatus);
            }
        }
        if (mListener != null) {
            mListener.onResolvedVehicleInfo(statuses);
        }
    }

    /**
     * Get all data from a param.
     *
     * @param param VehicleParam of VehicleGwInfoMsg.
     * @return list of bluetooth status.
     */
    private LinkedHashMap<String, BluetoothStatus> getDataFromParam(VehicleParam param) {
        switch (param.getTag()) {
            case ParamTag.FRAME_MICU:
                return new FrameMicu(param).getAllStatus();
            case ParamTag.FRAME_VSPNE:
                return new FrameVspne(param).getAllStatus();
            case ParamTag.FRAME_ENGTEMP:
                return new FrameEngtemp(param).getAllStatus();
            case ParamTag.FRAME_SW:
                return new FrameSw(param).getAllStatus();
            case ParamTag.FRAME_MET_BTU:
                return new FrameMetBtu(param).getAllStatus();
            case ParamTag.FRAME_TRICOM_PHEV:
                return new FrameTricomPhev(param).getAllStatus();
            case ParamTag.FRAME_ODO_TRIP:
                return new FrameOdoTrip(param).getAllStatus();
            case ParamTag.FRAME_TRICOM3:
                return new FrameTricom3(param).getAllStatus();
            case ParamTag.FRAME_MAINTANCE:
                return new FrameMaintance(param).getAllStatus();
            case ParamTag.FRAME_TRICOM4:
                return new FrameTricom4(param).getAllStatus();
            case ParamTag.FRAME_VEHICLE_DATA:
                return new FrameVehicleData(param).getAllStatus();
            case ParamTag.FRAME_ECU_1:
                return new FrameEcu1(param).getAllStatus();
            case ParamTag.FRAME_ECU_2:
                return new FrameEcu2(param).getAllStatus();
            case ParamTag.FRAME_ECU_3:
                return new FrameEcu3(param).getAllStatus();
            case ParamTag.FRAME_ECU_4:
                return new FrameEcu4(param).getAllStatus();
            case ParamTag.FRAME_ECU_5:
                return new FrameEcu5(param).getAllStatus();
            case ParamTag.FRAME_ECU_6:
                return new FrameEcu6(param).getAllStatus();
            case ParamTag.FRAME_ECU_7:
                return new FrameEcu7(param).getAllStatus();
            case ParamTag.FRAME_ECU_8:
                return new FrameEcu8(param).getAllStatus();
            case ParamTag.FRAME_ECU_9:
                return new FrameEcu9(param).getAllStatus();
            case ParamTag.FRAME_ECU_10:
                return new FrameEcu10(param).getAllStatus();
            case ParamTag.FRAME_ECU_11:
                return new FrameEcu11(param).getAllStatus();
            case ParamTag.FRAME_ECU_12:
                return new FrameEcu12(param).getAllStatus();

            default:
                return null;
        }
    }


}
