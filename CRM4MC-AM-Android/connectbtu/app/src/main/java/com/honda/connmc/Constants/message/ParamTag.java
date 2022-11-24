package com.honda.connmc.Constants.message;

import android.util.SparseIntArray;

/**
 * This class defines param's tag and its value's length.
 */
public class ParamTag {
    // Tag define (vehicle control)
    public static final short TRANSACTION_IDENTITY = 0x0000;
    public static final short MP_NAME_COMMAND = 0x00A0;
    public static final short MP_NAME_COMMAND_TEST = 0x0013;

    // Tag define (security)
    public static final short SECURITY_CONFIG_IDENTIFIER = 0x0201;
    public static final short RANDOM_VALUE = 0x0202;
    public static final short VEHICLE_AUTH_VALUE = 0x0203;
    public static final short AUTH_RESPONSE_VALUE = 0x0204;
    public static final short SECURITY_CONFIG_CONTAINER = 0x0206;
    public static final short REGISTRATION_TYPE = 0x0208;
    public static final short REGISTRATION_RESULT = 0x0209;
    public static final short RESULT_CAUSE = 0x020A;
    public static final short USER_NAME = 0x020B;
    public static final short PUBLIC_KEY = 0x020C;
    public static final short SECURITY_CONFIG_TYPE = 0x020D;
    public static final short REGISTRATION_STATUS = 0x0501;
    // Tag define (commu conn)
    public static final short SEQUENCE_NUMBER = 0x0301;

    // Tag define (error)
    public static final short SUCCESS = 0x7F00;
    public static final short FAILURE = 0x7F01;
    public static final short WARNING = 0x7F02;

    // Tag define (END_OF_MESSAGE)
    public static final int END_OF_MESSAGE = 0xFFFF;
    // Tag define (CHECK_SUM)
    public static final int CHECK_SUM_MESSAGE = 0xFF;

    //Tag define (Vehicle GW Info)
    public static final int FRAME_MICU = 0xF9E2;
    public static final int FRAME_VSPNE = 0xF9E3;
    public static final int FRAME_ENGTEMP = 0xF9E4;
    public static final int FRAME_SW = 0xF9E5;
    public static final int FRAME_MET_BTU = 0xF9E6;
    public static final int FRAME_TRICOM_PHEV = 0xF9E7;
    public static final int FRAME_ODO_TRIP = 0xF9E8;
    public static final int FRAME_TRICOM3 = 0xF9E9;
    public static final int FRAME_MAINTANCE = 0xF9EA;
    public static final int FRAME_TRICOM4 = 0xF9EB;
    public static final int FRAME_VEHICLE_DATA = 0xF9EC;
    public static final int FRAME_ECU_1 = 0xF9D0;
    public static final int FRAME_ECU_2 = 0xF9D1;
    public static final int FRAME_ECU_3 = 0xF9D2;
    public static final int FRAME_ECU_4 = 0xF9D3;
    public static final int FRAME_ECU_5 = 0xF9D4;
    public static final int FRAME_ECU_6 = 0xF9D5;
    public static final int FRAME_ECU_7 = 0xF9D6;
    public static final int FRAME_ECU_8 = 0xF9D7;
    public static final int FRAME_ECU_9 = 0xF9D8;
    public static final int FRAME_ECU_10 = 0xF9D9;
    public static final int FRAME_ECU_11 = 0xF9DA;
    public static final int FRAME_ECU_12 = 0xF9DB;


    //Tag define (Smartphone GW Info)
    public static final int FRAME_SMP_TIME = 0xF9DD;
    public static final int FRAME_AUDIO_GEN_INF = 0xF601;
    public static final int FRAME_SAB_REQ = 0xF9ED;
    public static final int FRAME_SAB_FLAG = 0xF9E2;
    public static final int FRAME_SAB_ARROW = 0xF9E3;
    public static final int FRAME_GPS_TIME = 0xF9E0;
    public static final int FRAME_GPS_LOCAL_TZONE = 0xF9E1;


    //-----------------------END-TAG-DEFINE-----------------------

    private static final SparseIntArray mTagLengthMap = new SparseIntArray();

    static {
        // Tag length define (vehicle control)
        mTagLengthMap.put(ParamTag.TRANSACTION_IDENTITY, 1);
        mTagLengthMap.put(ParamTag.MP_NAME_COMMAND, 484);
        // Tag length define (security)
        mTagLengthMap.put(ParamTag.SECURITY_CONFIG_IDENTIFIER, 1);
        mTagLengthMap.put(ParamTag.RANDOM_VALUE, 32);
        mTagLengthMap.put(ParamTag.VEHICLE_AUTH_VALUE, 32);
        mTagLengthMap.put(ParamTag.AUTH_RESPONSE_VALUE, 32);
        mTagLengthMap.put(ParamTag.SECURITY_CONFIG_CONTAINER, 32);
        mTagLengthMap.put(ParamTag.REGISTRATION_TYPE, 1);
        mTagLengthMap.put(ParamTag.REGISTRATION_RESULT, 1);
        mTagLengthMap.put(ParamTag.REGISTRATION_STATUS, 1);
        mTagLengthMap.put(ParamTag.RESULT_CAUSE, 1);
        mTagLengthMap.put(ParamTag.USER_NAME, 32);
        mTagLengthMap.put(ParamTag.PUBLIC_KEY, 321);
        mTagLengthMap.put(ParamTag.SECURITY_CONFIG_TYPE, 1);

        // Tag length define (commu conn)
        mTagLengthMap.put(ParamTag.SEQUENCE_NUMBER, 1);

        // Tag length define (error)
        mTagLengthMap.put(ParamTag.SUCCESS, 1);
        mTagLengthMap.put(ParamTag.FAILURE, 1);
        mTagLengthMap.put(ParamTag.WARNING, 1);
        mTagLengthMap.put(ParamTag.END_OF_MESSAGE, 1);

        //Tag length define (Vehicle GW Info)
        mTagLengthMap.put(ParamTag.FRAME_MICU, 5);
        mTagLengthMap.put(ParamTag.FRAME_VSPNE, 6);
        mTagLengthMap.put(ParamTag.FRAME_ENGTEMP, 4);
        mTagLengthMap.put(ParamTag.FRAME_SW, 3);
        mTagLengthMap.put(ParamTag.FRAME_MET_BTU, 3);
        mTagLengthMap.put(ParamTag.FRAME_TRICOM_PHEV, 8);
        mTagLengthMap.put(ParamTag.FRAME_ODO_TRIP, 5);
        mTagLengthMap.put(ParamTag.FRAME_TRICOM3, 7);
        mTagLengthMap.put(ParamTag.FRAME_MAINTANCE, 7);
        mTagLengthMap.put(ParamTag.FRAME_TRICOM4, 8);
        mTagLengthMap.put(ParamTag.FRAME_VEHICLE_DATA, 8);
        mTagLengthMap.put(ParamTag.FRAME_ECU_1, 5);
        mTagLengthMap.put(ParamTag.FRAME_ECU_2, 7);
        mTagLengthMap.put(ParamTag.FRAME_ECU_3, 7);
        mTagLengthMap.put(ParamTag.FRAME_ECU_4, 4);
        mTagLengthMap.put(ParamTag.FRAME_ECU_5, 3);
        mTagLengthMap.put(ParamTag.FRAME_ECU_6, 5);
        mTagLengthMap.put(ParamTag.FRAME_ECU_7, 7);
        mTagLengthMap.put(ParamTag.FRAME_ECU_8, 7);
        mTagLengthMap.put(ParamTag.FRAME_ECU_9, 7);
        mTagLengthMap.put(ParamTag.FRAME_ECU_10, 7);
        mTagLengthMap.put(ParamTag.FRAME_ECU_11, 5);
        mTagLengthMap.put(ParamTag.FRAME_ECU_12, 5);

        //Tag length define (Smartphone GW Info)
        mTagLengthMap.put(ParamTag.FRAME_SMP_TIME, 8);
        mTagLengthMap.put(ParamTag.FRAME_AUDIO_GEN_INF, 6);
        mTagLengthMap.put(ParamTag.FRAME_SAB_REQ, 1);
       // mTagLengthMap.put(ParamTag.FRAME_SAB_FLAG, 1);
       // mTagLengthMap.put(ParamTag.FRAME_SAB_ARROW, 1);
        mTagLengthMap.put(ParamTag.FRAME_GPS_TIME, 8);
        mTagLengthMap.put(ParamTag.FRAME_GPS_LOCAL_TZONE, 1);
    }

    //Get size of value associated with this tag.
    public static int getLength(int tag) {
        return mTagLengthMap.get(tag);
    }
}
