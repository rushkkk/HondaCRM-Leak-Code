package com.honda.connmc.Constants.message;

/**
 * This class defines Message Ids.
 */
public class MsgId {
        public static final short VEHICLE_TRANSACTION_REQUEST = 0x0001;         //Phone => Vehicle
        public static final short VEHICLE_TRANSACTION_RESPONSE = 0x0002;        //Phone <= Vehicle
        public static final short VEHICLE_STATUS_INQUIRY = 0x0003;              //Phone <= Vehicle
        //public static final short VEHICLE_STAUS_INDICATION = 0x0004;
        public static final short AUTHENTICATION_REQUEST = 0x0005;              //Phone <= Vehicle
        public static final short AUTHENTICATION_RESPONSE = 0x0006;             //Phone => Vehicle
        public static final short SECURITY_CONFIG_REQUEST = 0x000C;             //Phone <= Vehicle
        public static final short SECURITY_CONFIG_RESPONSE = 0x000D;            //Phone => Vehicle
        public static final short VEHICLE_WARNING_INDICATION = 0x0012;          //Phone <= Vehicle
        public static final short REGISTRATION_REQUEST = 0x0014;                //Phone => Vehicle
        public static final short REGISTRATION_RESPONSE = 0x0015;               //Phone <= Vehicle
        public static final short REGISTRATION_STATUS_INDICATION = 0x0016;      //Phone <= Vehicle
        public static final short GW_VEHICLE_INFO_INDICATION = 0x0020;          //Phone <= Vehicle
        public static final short GW_SMARTPHONE_INFO_INDICATION = 0x0021;       //Phone => Vehicle
}
