package com.honda.connmc.Constants;

public interface UuidConstants {
    int SPLASH_DISPLAY_LENGTH = 1500;
    int DELAY_CONNECTING_MILLIS = 5000;

    String KEY_DEVICE_LIST = "KEY_DEVICE_LIST";

    int SCAN_TIME = 0;

    int REQUEST_ENABLE_BT = 1;
    String UUID_NOTIFICATION[] = {};

    interface UuidService {
        String PSEUDO_SPP_SERVICE = "00000000-1111-4489-0000-ffa28cde82ab";
        String TRANSMITTER_CONTROL_SERVICE = "00000000-2222-4489-0000-ffa28cde82ab";
        String BLE_PARAMETER_SERVICE = "00000000-0000-4489-0000-ffa28cde82ab";
        String ASYNCHRONOUS_TRANSFER_SERVICE = "00000000-3333-4489-0000-ffa28cde82ab";
        String LOW_POWER_SLEEP_SERVICE = "00000000-ffff-4489-0000-ffa28cde82ab";
        String REGISTER_SERVICE = "000000ff-3333-4489-0000-ffa28cde82ab";
    }

    interface UuidPSS {
        String INPUT_DATA = "11111111-1111-4489-0000-ffa28cde82ab";
        String INPUT_DATA_RESP = "22222222-1111-4489-0000-ffa28cde82ab";
        String OUTPUT_DATA = "33333333-1111-4489-0000-ffa28cde82ab";
        String OUTPUT_DATA_RESP = "44444444-1111-4489-0000-ffa28cde82ab";
    }

    interface UuidTCS {
        String CONTROL_IND = "11111111-2222-4489-ffff-ffa28cde82ab";
        String CONTROL_IND_RESP = "22222222-2222-4489-ffff-ffa28cde82ab";
        String CONTROL_REQ = "33333333-2222-4489-ffff-ffa28cde82ab";
        String CONTROL_REQ_RESP = "44444444-2222-4489-ffff-ffa28cde82ab";
    }

    interface UuidBPS {
        String CONN_PARAM = "11111111-0000-4489-0000-ffa28cde82ab";
        String ATT_MTU = "22222222-0000-4489-0000-ffa28cde82ab";
    }

    interface UuidATS {
        //UUID read data.
        String ASYNCH_INPUT_DATA = "11111111-3333-4489-0000-ffa28cde82ab";
        String ASYNCH_INPUT_RESP = "22222222-3333-4489-0000-ffa28cde82ab";
        //UUID write data.
        String ASYNCH_OUTPUT_DATA = "33333333-3333-4489-0000-ffa28cde82ab";
        String ASYNCH_OUTPUT_RESP = "44444444-3333-4489-0000-ffa28cde82ab";

        String DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";
    }

    interface UuidLPS {
        String SLEEP = "00000000-ffff-4489-0000-ffa28cde82ab";
    }


}