package com.honda.connmc.Bluetooth.message;

import android.bluetooth.BluetoothGattCharacteristic;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.GattCallback;
import com.honda.connmc.Bluetooth.message.VehicleGwInfo.VehicleInfoMsgResolver;
import com.honda.connmc.Constants.UuidConstants;
import com.honda.connmc.Constants.message.MsgId;
import com.honda.connmc.Interfaces.listener.transition.IF_BluetoothTransferDataListener;
import com.honda.connmc.Model.message.common.VehicleMessage;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.Utils.MessageUtils;
import com.honda.connmc.Utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle send / receive message to / from BTU.
 */
public class DataMessageManager implements GattCallback.DataListener {
    private GattCallback mGattCallback;
    private static final int MAX_DATA_LENGTH_WRITE = 18;
    private int mPacketDataLength = MAX_DATA_LENGTH_WRITE;
    private int mPacketDataMaxLength = MAX_DATA_LENGTH_WRITE;
    private static final int MAX_PACKET_NUM = 64;
    private static final int PACKET_ORDER_MASK = 0xC0;
    private static final int PACKET_DATA_MASK = 0x3F;
    private static final int PACKET_ORDER_TYPE_SINGLE = 3;
    private static final int PACKET_ORDER_TYPE_MULTI_BEGIN = 2;
    private static final int PACKET_ORDER_TYPE_MULTI_END = 1;
    private static final int PACKET_ORDER_TYPE_MULTI_MIDDLE = 0;
    private static final int PACKET_FLAG_BEGIN = 0x80;
    private static final int PACKET_FLAG_END = 0x40;
    private final Object LOCK_DATA_CTRL = new Object();

    private int mExpectedPacketNum = 0;
    private int mMessageOffset = 0;
    private byte[] mMessage = null;
    private boolean isEnableEcrypt;

    private List<IF_BluetoothTransferDataListener> mListeners;
//    private BluetoothGatt mGatt;

    public DataMessageManager(GattCallback gattCallback) {
        mGattCallback = gattCallback;
    }

    public void setupDataLength(int attSize){
        mPacketDataLength = attSize - 5;
        mPacketDataMaxLength = (mPacketDataLength > MAX_DATA_LENGTH_WRITE)
                ? MAX_DATA_LENGTH_WRITE : mPacketDataLength;
    }

    public void setListener(IF_BluetoothTransferDataListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void setEnableEncrypt(boolean enableEncrypt) {
        isEnableEcrypt = enableEncrypt;
    }

    /**
     * Unregister listener.
     *
     * @param listener IF_BluetoothTransferDataListener to remove.
     * @return true if removed successfully.
     */
    public boolean unRegisterListener(IF_BluetoothTransferDataListener listener) {
        return mListeners.remove(listener);
    }

    /**
     * Send message to BTU
     *
     * @param message VehicleMessage.
     */
    public void sendMessage(VehicleMessage message) {
        LogUtils.startEndMethodLog(true);
        //Encode and Encrypt message.
        byte[] encodedMsg = MessageUtils.encode(message);
        byte[] encryptedMsg;
        if (isEnableEcrypt) {
            encryptedMsg = MessageUtils.encrypt(encodedMsg);
        } else {
            encryptedMsg = encodedMsg;
        }
        LogUtils.d("encodedMsg = " + StringUtils.byteArrToStringHex(encodedMsg)
                + " encryptedMsg = " + StringUtils.byteArrToStringHex(encryptedMsg));
        splitMsgAndSend(encryptedMsg);

        LogUtils.startEndMethodLog(false);
    }

    /**
     * Split encrypted msg into packages then send to BTU.
     *
     * @param data byte[] encrypted msg.
     * @return true if sent successfully.
     */
    private boolean splitMsgAndSend(byte[] data) {
        synchronized (LOCK_DATA_CTRL) {
            BluetoothGattCharacteristic charWriteData = BluetoothManager.getInstance().getBleConnectController().getCharacteristic(UuidConstants.UuidATS.ASYNCH_OUTPUT_DATA);

            if (charWriteData == null) {
                LogUtils.e("write error Write Characteristic == null");
                return false;
            }
            if ((data.length > mPacketDataMaxLength * MAX_PACKET_NUM)
                    || (data.length <= 0)) {
                return false;
            }
            // check length and packet
            int packet = (data.length + (mPacketDataMaxLength - 1)) / mPacketDataMaxLength;
            LogUtils.d("write dataLength:" + data.length);
            LogUtils.d("write packet:" + packet);
            int offset = 0;
            // need to add header and footer to packet
            for (int i = 0; i < packet; i++) {
                // packet size
                int dataSize = data.length - offset;
                if (dataSize > mPacketDataMaxLength) {
                    dataSize = mPacketDataMaxLength;
                }
                LogUtils.d("write dataSize:" + dataSize);
                byte[] message = new byte[dataSize + 2];
                LogUtils.d("makeHeader packetNo:" + (i + 1));
                byte header = makeHeader(i + 1, packet, dataSize);
                LogUtils.d("write header" + Byte.toString(header));

                byte footer = 0x00;
                footer ^= header;
                int dst = 0;
                message[dst++] = header;

                for (int count = 0; count < dataSize; count++) {
                    footer ^= data[offset];
                    message[dst++] = data[offset++];
                }
                message[dst] = footer;
                LogUtils.d("TX(BLE):" + StringUtils.byteArrToStringHex(message));
                boolean isSuccessful = mGattCallback.write(charWriteData, message);
                if (!isSuccessful) {
                    LogUtils.e("Write data into bluetooth gatt error");
                    notifySendMsgFailed();
                    return false;
                }
            }
            LogUtils.d("write offset" + offset);
            if (offset == data.length) {
                return true;
            }
            return false;
        }
    }

    @Override
    public void onWriteFailed(int errCode) {
        LogUtils.e("Write characteristic failed with err code: " + errCode);
    }

    /**
     * Handle a package of data received. Join packages then send to listeners.
     *
     * @param data package of data. Include SeqNum, Data, Chksum.
     */
    @Override
    public void onDataReceived(byte[] data) {
        LogUtils.startEndMethodLog(true);
        if ((data == null) || (data.length < 3)|| (data.length > mPacketDataLength + 2)) {
            LogUtils.e("onDataReceived error invalid data");
            resetInputMessage();
            return;
        }
        LogUtils.d("RX(BLE):" + StringUtils.byteArrToStringHex(data));
        if (!isCheckSumValid(data)) {
            LogUtils.e("onDataReceived error invalid checkSum");
            resetInputMessage();
            return;
        }

        boolean isNeedtoSend = checkHeader(data[0], data.length);
        if (mMessage == null) {
            LogUtils.e("onDataReceived error invalid header");
            resetInputMessage();
            return;
        }
        // copy data
        LogUtils.d("onDataReceived mMessageOffset:" + mMessageOffset);
        LogUtils.d("onDataReceived length:" + mMessage.length);
        for (int i = 1; i < data.length - 1; i++) {
            if (mMessageOffset < mMessage.length) {
                mMessage[mMessageOffset++] = data[i];
            } else {
                LogUtils.e("onDataReceived offset error mMessageOffset:" + mMessageOffset);
                resetInputMessage();
                return;
            }
        }
        // notify data.
        if (isNeedtoSend) {
            int exactSize = mMessage.length;
            if (mMessageOffset < mMessage.length) {
                exactSize = mMessageOffset;
            }
            LogUtils.d("onDataReceived exactSize:" + exactSize);
            byte joinedData[] = new byte[exactSize];
            for (int src = 0; src < exactSize; src++) {
                joinedData[src] = mMessage[src];
            }
            fullDataReceived(joinedData);
            resetInputMessage();
        }
        LogUtils.startEndMethodLog(false);
    }

    /**
     * Full data received, after joining data packages.
     *
     * @param joinedData Application SDU data joined from multiple data packages.
     */
    private void fullDataReceived(byte[] joinedData) {
        byte[] decryptedMsg;
        if (isEnableEcrypt) {
            decryptedMsg = MessageUtils.decrypt(joinedData);
        } else {
            decryptedMsg = joinedData;
        }
        notifyRawDataReceived(decryptedMsg);

        VehicleMessage msg = MessageUtils.decode(decryptedMsg);
        short msgId = msg.getMsgId();
        switch (msgId) {
            case MsgId.AUTHENTICATION_REQUEST:
                BluetoothManager.getInstance().getBleRegisterController().onReceiveDataAuthenticationRequest(msg);
                break;
            case MsgId.SECURITY_CONFIG_REQUEST:
                BluetoothManager.getInstance().getBleRegisterController().onSecurityConfigRequestNotified(msg);
                break;
            case MsgId.REGISTRATION_RESPONSE:
                BluetoothManager.getInstance().getBleRegisterController().onReceiveDataRegistrationResponse(msg);
                break;
            case MsgId.REGISTRATION_STATUS_INDICATION:
                BluetoothManager.getInstance().getBleRegisterController().onRegistrationStatusIndicationNotified(msg);
                break;
            case MsgId.GW_VEHICLE_INFO_INDICATION:
                VehicleInfoMsgResolver.getInstance().resolveMsg(msg);
                break;
            default:
                break;
        }


        notifyMsgReceived(msg);
//        LogUtils.d("MsgId received."
//                + "decryptedMsg = " + decryptedMsg);
    }


    private void notifySendMsgFailed() {
        if (mListeners == null || mListeners.isEmpty()) return;

        for (IF_BluetoothTransferDataListener listener : mListeners) {
            listener.onSendMessageFail();
        }
    }

    private void notifyMsgReceived(VehicleMessage msg) {
        if (mListeners == null || mListeners.isEmpty()) return;

        for (IF_BluetoothTransferDataListener listener : mListeners) {
            listener.onReceiveDataFromBTU(msg);
        }
    }

    private void notifyRawDataReceived(byte[] rawData) {
        if (mListeners == null || mListeners.isEmpty()) return;
        for (IF_BluetoothTransferDataListener listener : mListeners) {
            listener.onReceivedRawData(StringUtils.byteArrToStringHex(rawData));
        }
    }

    private void resetInputMessage() {
        mExpectedPacketNum = 1;
        mMessageOffset = 0;
        mMessage = null;
    }

    private boolean isCheckSumValid(byte[] input) {
        byte footer = input[input.length - 1];
        LogUtils.d("isCheckSumValid raw footer:" + Byte.toString(footer));
        byte checksum = 0x00;
        for (int count = 0; count < input.length - 1; count++) {
            checksum ^= input[count];
        }
        LogUtils.d("isCheckSumValid caliculated footer:" + Byte.toString(checksum));
        return footer == checksum;
    }

    private boolean checkHeader(byte header, int packetDataSize) {
        int type = (header & PACKET_ORDER_MASK) >> 6;
        byte data = (byte) (header & PACKET_DATA_MASK);
        LogUtils.d("onDataReceived headerType:" + type);

        switch (type) {
            case PACKET_ORDER_TYPE_MULTI_END:
                return checkPacketOrder((byte) 0x00);
            case PACKET_ORDER_TYPE_MULTI_MIDDLE:
                return checkPacketOrder(data);
            case PACKET_ORDER_TYPE_MULTI_BEGIN:
                return allocateBufferFromHeader(false, data, packetDataSize);
            case PACKET_ORDER_TYPE_SINGLE:
                return allocateBufferFromHeader(true, data, packetDataSize);
            default:
                LogUtils.e("type no is invalid");
                resetInputMessage();
                return false;
        }
    }

    private boolean checkPacketOrder(byte data) {
        if (mMessage == null) {
            LogUtils.e("middle or last data is received when buffer is empty");
            resetInputMessage();
            return false;
        }
        if (data != mExpectedPacketNum - 1) {
            LogUtils.e("packet order is wrong");
            resetInputMessage();
            return false;
        }
        if (data != 0) {
            mExpectedPacketNum = data;
            return false;
        } else {
            return true;
        }
    }

    private boolean allocateBufferFromHeader(boolean isSingleData, byte data, int packetDataSize) {
        boolean isReset = ((mMessage == null) || (mExpectedPacketNum == 1)) ? true : false;
        resetInputMessage();
        if (!isReset) {
            LogUtils.e("onDataReceived begin message is received when buffer is allocated");
            return false;
        }
        int messageSize;
        if (!isSingleData) {
            mExpectedPacketNum = data;
            messageSize = mPacketDataLength * (mExpectedPacketNum + 1);
        } else {
            if (packetDataSize != data + 2) {
                LogUtils.d("onDataReceived header is little wrong");
            }
            messageSize = packetDataSize;
        }
        LogUtils.d("onDataReceived expected messageSize:" + messageSize);
        mMessage = new byte[messageSize];
        return isSingleData;
    }

    private byte makeHeader(int packetNo, int packetSum, int length) {
        int header = 0x00;
        if (packetNo == 1) {
            header |= PACKET_FLAG_BEGIN;
        }
        if (packetNo == packetSum) {
            header |= PACKET_FLAG_END;
            header |= length;
        } else {
            header |= packetSum - packetNo;
        }

        byte data = (byte) header;
        return data;
    }
}

