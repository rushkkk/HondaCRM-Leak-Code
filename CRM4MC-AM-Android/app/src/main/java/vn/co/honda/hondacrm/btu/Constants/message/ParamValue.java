package vn.co.honda.hondacrm.btu.Constants.message;

//Defines range of value in message's param value.
public class ParamValue {

    public static class VehicleGwInfo {
        public static class FrameMicu {
            public static final String C_IGKEYSW = "0.5";
            public static final String C_STOP = "0.4";
            public static final String C_HAZARDSW = "2.5";
        }

        public static class FrameVspne {
            public static final String C_VSP = "0.7-0.0";
            public static final String C_NE = "1.7-2.0";
        }

        public static class FrameEngtemp {
            public static final String C_ENGTEMP = "0.7-0.0";
            public static final String C_ODO = "1.7-3.0";
        }

        public static class FrameSw {
            public static final String C_UP = "0.7-0.6";
            public static final String C_DOWN = "0.5-0.4";
            public static final String C_ENT = "0.3-0.2";
            public static final String C_BACK = "0.1-0.0";
            public static final String C_RESERVE = "1.7-1.6";
        }

        public static class FrameMetBtu {
            public static final String C_REQ_REGMODE_MET = "0.7-0.6";
            public static final String C_REQ_CLEAR_MET = "0.4-0.3";
            public static final String C_WEATHER_INFO_REQ = "0.1-0.0";
            public static final String C_RES_REFTIME = "1.7-1.6";
            public static final String C_SAB_REQ_MET = "1.4";
        }

        public static class FrameTricomPhev {
            public static final String C_RANGE_EV = "0.7-1.5";
            public static final String C_RANGE_TOTAL = "1.2-2.0";
        }

        public static class FrameOdoTrip {
            public static final String C_DISPLAY_UNIT = "0.7";
            public static final String C_DISPLAY_TRIPA = "0.1-2.1";
            public static final String C_DISPLAY_TRIPB = "2.0-4.0";
        }

        public static class FrameTricom3 {
            public static final String C_TRICOM_RANGE_UNIT = "0.7-0.7";
            public static final String C_TRICOM_RANGE = "0.2-1.0";
            public static final String C_TRICOM_OUTTEMP = "2.7-2.0";
            public static final String C_TRICOM_ELAP_TIME_HR = "3.7-3.0";
            public static final String C_TRICOM_ELAP_TIME_MIN = "4.7-4.0";
        }

        public static class FrameMaintance {
            public static final String C_MAINT_OIL_LIFE = "0.7-0.4";
            public static final String C_MAINT_INFO = "0.3-0.1";
            public static final String C_MAINT_DATA = "0.0-2.0";
            public static final String C_MAINT_UNIT = "3.7-3.6";
            public static final String C_MAINT_RESULT = "3.5-3.4";
            public static final String C_MAINT_OIL_IND = "3.3";
            public static final String C_MAINT_SUBMAINT_A = "3.2";
            public static final String C_MAINT_SUBMAINT_B = "3.1";
            public static final String C_MAINT_SUBMAINT_1 = "3.0";
            public static final String C_MAINT_SUBMAINT_2 = "4.7";
            public static final String C_MAINT_SUBMAINT_3 = "4.6";
            public static final String C_MAINT_SUBMAINT_4 = "4.5";
            public static final String C_MAINT_SUBMAINT_5 = "4.4";
            public static final String C_MAINT_SUBMAINT_6 = "4.3";
            public static final String C_MAINT_SUBMAINT_7 = "4.2-4.2";
            public static final String C_I_DISPLAY_CONDITION_MAINT = "5.7-5.4";
            public static final String C_I_CAR_CONDITION_MAINT = "5.3";
            public static final String C_EXCHANGE_OIL_DIST = "6.7-6.3";
            public static final String C_EXCHANGE_MAINT_DIST = "6.2-7.6";
        }

        public static class FrameVehicleData {
            public static final String C_ENG_OIL_TEMP = "0.7-0.0";
            public static final String C_ASP = "2.7-3.0";
            public static final String C_BRAKE_PRESSURE = "4.7-4.0";
            public static final String C_INTAKE_AIR_TEMP = "5.7-5.0";
        }

        public static class FrameTricom4 {
            public static final String C_TRICOM4_UNIT = "0.7-0.6";
            public static final String C_METER_ECO_INST_FUEL = "0.5-1.3";
            public static final String C_METER_ECO_AVG_FUEL = "1.2-2.0";
            public static final String C_METER_FUEL_CONSUMPTION = "3.7-3.0";
            public static final String C_METER_FUEL_REMAIN = "4.7-4.4";
        }

        public static class FrameEcu1 {
            public static final String ENG_BTU_ECUID1_00 = "0.7-0.0";
            public static final String ENG_BTU_ECUID2_00 = "1.7-1.0";
            public static final String ENG_BTU_ECUID3_00 = "2.7-2.0";
            public static final String ENG_BTU_ECUID4_00 = "3.7-3.0";
            public static final String ENG_BTU_ECUID5_00 = "4.7-4.0";
        }

        public static class FrameEcu2 {
            public static final String ENG_BTU_NE_11 = "0.7-0.0";
            public static final String ENG_BTU_TH_11 = "2.7-2.0";
            public static final String ENG_BTU_THDEG_11 = "3.7-3.0";
            public static final String ENG_BTU_TW_11 = "4.7-4.0";
            public static final String ENG_BTU_ECT_11 = "5.7-5.0";
            public static final String ENG_BTU_TA_11 = "6.7-6.0";
        }

        public static class FrameEcu3 {
            public static final String ENG_BTU_IAT_11 = "0.7-0.0";
            public static final String ENG_BTU_PB_11 = "1.7-1.0";
            public static final String ENG_BTU_MAP_11 = "2.7-2.0";
            public static final String ENG_BTU_PA_11 = "3.7-3.0";
            public static final String ENG_BTU_BARO_11 = "4.7-4.0";
            public static final String ENG_BTU_VB_11 = "5.7-5.0";
            public static final String ENG_BTU_VSP_11 = "6.7-6.0";
        }

        public static class FrameEcu4 {
            public static final String ENG_BTU_TOUT_11 = "0.7-0.0";
            public static final String ENG_BTU_IG_11 = "1.7-1.0";
            public static final String ENG_BTU_PULCNT_11 = "2.7-2.0";
            public static final String ENG_BTU_ICMD_11 = "3.7-3.0";
        }

        public static class FrameEcu5 {
            public static final String ENG_BTU_VO2_20 = "0.7-0.0";
            public static final String ENG_BTU_KO2_20 = "1.7-1.0";
            public static final String ENG_BTU_O2HT_20 = "2.7-2.0";
        }

        public static class FrameEcu6 {
            public static final String ENG_BTU_NTRBCD_MILSTAT_D0 = "0.7-0.0";
            public static final String ENG_BTU_TIMB_D0 = "1.7-1.0";
            public static final String ENG_BTU_BA_D0 = "3.7-3.0";
            public static final String ENG_BTU_MAFREF_D0 = "4.7-4.0";
        }

        public static class FrameEcu7 {
            public static final String ENG_BTU_INBIT1_D1 = "0.7-0.0";
            public static final String ENG_BTU_INBIT2_D1 = "1.7-1.0";
            public static final String ENG_BTU_INBIT3_D1 = "2.7-2.0";
            public static final String ENG_BTU_INBIT4_D1 = "3.7-3.0";
            public static final String ENG_BTU_OUTBIT1_D1 = "4.7-4.0";
            public static final String ENG_BTU_OUTBIT2_D1 = "5.7-5.0";
            public static final String ENG_BTU_OUTBIT3_D1 = "6.7-6.0";
        }

        public static class FrameEcu8 {
            public static final String ENG_BTU_NE_18 = "0.7-0.0";
            public static final String ENG_BTU_TH1_18 = "2.7-2.0";
            public static final String ENG_BTU_THDEG1_18 = "3.7-3.0";
            public static final String ENG_BTU_TH2_18 = "4.7-4.0";
            public static final String ENG_BTU_THDEG2_18 = "5.7-5.0";
            public static final String ENG_BTU_TW_18 = "6.7-6.0";
        }

        public static class FrameEcu9 {
            public static final String ENG_BTU_ECT_18 = "0.7-0.0";
            public static final String ENG_BTU_TA_18 = "1.7-1.0";
            public static final String ENG_BTU_IAT_18 = "2.7-2.0";
            public static final String ENG_BTU_PB_18 = "3.7-3.0";
            public static final String ENG_BTU_MAP_18 = "4.7-4.0";
            public static final String ENG_BTU_PA_18 = "5.7-5.0";
            public static final String ENG_BTU_BARO_18 = "6.7-6.0";
        }

        public static class FrameEcu10 {
            public static final String ENG_BTU_VB_18 = "0.7-0.0";
            public static final String ENG_BTU_VSP_18 = "1.7-1.0";
            public static final String ENG_BTU_TOUT_18 = "2.7-2.0";
            public static final String ENG_BTU_IG_18 = "4.7-4.0";
            public static final String ENG_BTU_PULCNT_18 = "5.7-5.0";
            public static final String ENG_BTU_ICMD_HIGH_18 = "6.7-6.0";
        }

        public static class FrameEcu11 {
            public static final String ENG_BTU_ICMD_LOW_18 = "0.7-0.0";
            public static final String ENG_BTU_BATP_18 = "1.7-1.0";
            public static final String ENG_BTU_SUBVB_18 = "2.7-2.0";
            public static final String ENG_BTU_VRELEY_18 = "3.7-3.0";
            public static final String ENG_BTU_TRQDEMAND_18 = "4.7-4.0";
        }

        public static class FrameEcu12 {
            public static final String ENG_BTU_SOC_18 = "0.7-0.0";
            public static final String ENG_BTU_PACKV_18 = "1.7-1.0";
            public static final String ENG_BTU_PACKC_18 = "2.7-2.0";
            public static final String ENG_BTU_PACKT_MAX_18 = "3.7-3.0";
            public static final String ENG_BTU_PACKT_MIN_18 = "4.7-4.0";
        }

    }

    public static class SmartphoneGwInfo {
        public static class FrameSmpTime {
            public static final String C_SMP_LOCAL_YEAR = "3.7-3.1";
            public static final String C_SMP_LOCAL_MONTH = "3.0-4.5";
            public static final String C_SMP_LOCAL_DAY = "4.4-4.0";
            public static final String C_SMP_LOCAL_HOUR = "5.7-5.3";
            public static final String C_SMP_LOCAL_MINUTE = "5.2-6.5";
            public static final String C_SMP_LOCAL_SECOND = "6.4-7.7";
        }

        public static class FrameAudioGenInf {
            public static final String C_AUDIO_POWER = "0.7";
            public static final String C_AUDIO_HITEMP = "0.6";
            public static final String C_AUDIO_DIAG = "0.5";
            public static final String C_USB_CONNECT = "0.3";
            public static final String C_SMP_CONNECT = "0.1";
            public static final String C_HED_CONNECT = "0.0";
            public static final String C_AUDIO_VOL = "1.7-1.0";
            public static final String C_AUDIO_MUTE = "2.7";
            public static final String C_SVC_MT_MODE = "2.6";
            public static final String C_AUDIO_SVC_MT = "2.5";
            public static final String C_SVC_MODE = "2.3-2.0";
            public static final String C_AUDIO_SRC = "3.7-3.4";
            public static final String C_AUDIO_SINK = "3.3-3.0";
            public static final String C_AUDIO_ST = "4.7-4.0";
            public static final String C_VSP_MODE = "5.7-5.4";
        }

        public static class FrameSabReq {
            public static final String C_SAB_REQ_MET = "0.0";
        }

        public static class FrameSabFlag {
            public static final String C_SAB_FLAG = "0.0";
        }

        public static class FrameSaArrow {
            public static final String C_SAB_ARROW = "0.1-0.0";
        }

        public static class FrameGpsTime {
            public static final String C_GPS_TIME_STATUS = "0.7-0.6";
            public static final String C_GPS_TIME_MODE = "0.5-0.5";
            public static final String C_GPS_MONTH_0PADDING = "0.4-0.4";
            public static final String C_GPS_DATE_0PADDING = "0.3-0.3";
            public static final String C_GPS_HOUR_0PADDING = "0.2-0.2";
            public static final String C_GPS_MINUTE_0PADDING = "0.1-0.1";
            public static final String C_GPS_TIME_COLON_BLINK = "0.0-0.0";
            public static final String C_GPS_LOCAL_SERIAL = "1.7-2.1";
            public static final String C_GPS_LOCAL_YEAR = "3.7-3.1";
            public static final String C_GPS_LOCAL_MONTH = "3.0-4.5";
            public static final String C_GPS_LOCAL_DAY = "4.4-4.0";
            public static final String C_GPS_LOCAL_HOUR = "5.7-5.3";
            public static final String C_GPS_LOCAL_MINUTE = "5.2-6.5";
            public static final String C_GPS_LOCAL_SECOND = "6.4-7.7";
            public static final String C_GPS_LOCAL_WEEK = "7.6-7.4";
            public static final String C_GPS_DISP_FORMAT = "7.2-7.0";
        }

        public static class FrameGpsLocalTzone {
            public static final String C_GPS_LOCAL_TZONE = "7.7-7.1";
        }

    }

    public static class VehicleControl {
        public static class MpNameCommand {
            public static final int CODE_UNKNOWN = 0x00;
            public static final int CODE_ASCII = 0x01;
            public static final int CODE_ISO88591 = 0x02;
            public static final int CODE_UTF8 = 0x03;
            public static final int CODE_UTF16 = 0x04;
            public static final int CODE_SPECIAL_CHARS = 0x05;
        }
    }

    public static class Security {
        public static class RegistrationType {
            public static final int REGISTER_ANY_FOB = 0x01;
            public static final int DEREGISTER = 0x02;
            public static final int RE_REGISTER = 0x03;
        }

        public static class RegistrationResult {
            public static final int OTHER_ERROR = -0x01;
            public static final int SUCCESS = 0x00;
            public static final int FAILURE = 0x01;
            public static final int REJECT = 0x02;
            public static final int REDIRECT = 0x03;
        }

        public static class ResultCause {
            public static final int UNSPECIFIED = 0x00;
            public static final int PUBLIC_KEY_REQUIRED = 0x01;
            public static final int TOO_MANY_USERS = 0x02;
            public static final int INVALID_PIN_CODE = 0x03;
            public static final int USER_NAME_UNKNOWN = 0x04;
            public static final int USER_NAME_DUPLICATE = 0x05;
            public static final int USER_NAME_REQUIRED = 0x06;
            public static final int BTU_REGISTERED = 0x07;
            public static final int BTU_NOT_REGISTERD = 0x08;
        }

        public static class SecurityConfigType {
            public static final int VERIFY_PIN = 0x01;
            public static final int ID_EXCHANGE = 0x02;
        }
    }

    public static final int END_OF_MESSAGE = 0x00;
}
