package com.honda.connmc.Bluetooth.message;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.message.VehicleGwInfo.VehicleInfoMsgResolver;
import com.honda.connmc.Bluetooth.register.BleRegisterController;
import com.honda.connmc.Constants.message.MsgId;
import com.honda.connmc.Interfaces.listener.transition.IF_BluetoothTransferDataListener;
import com.honda.connmc.Model.message.common.VehicleMessage;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.Utils.MessageUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DataMessageManager.class, MessageUtils.class, UUID.class,
        LogUtils.class, BluetoothManager.class, VehicleInfoMsgResolver.class})
public class DataMessageManagerTest {

    @Mock
    DataMessageManager mMockDataMsgManager;

    @Mock
    IF_BluetoothTransferDataListener mMockListener;

    @Mock
    List<IF_BluetoothTransferDataListener> mMockListeners;

    @Mock
    BluetoothGatt mMockGatt;

    @Mock
    VehicleMessage mMockMsg;

    @Mock
    UUID mMockUUID;

    @Mock
    BluetoothGattService mMockGattService;

    @Mock
    BluetoothGattCharacteristic mMockGattCharas;

    @Mock
    BluetoothManager mMockBluetoothManager;

    @Mock
    BleRegisterController mMockBleRegisterController;

    @Mock
    VehicleInfoMsgResolver mMockVehicleInfoMsgResolver;


    private Answer voidAnswer = invocation -> null;


    @Before
    public void prepareForTest() throws Exception {
        mockStatic(MessageUtils.class, LogUtils.class,
                UUID.class, BluetoothManager.class,
                VehicleInfoMsgResolver.class);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "e", anyString()).thenAnswer(voidAnswer);
        when(BluetoothManager.getInstance()).thenReturn(mMockBluetoothManager);
        when(VehicleInfoMsgResolver.getInstance()).thenReturn(mMockVehicleInfoMsgResolver);
        when(mMockBluetoothManager.getBleRegisterController()).thenReturn(mMockBleRegisterController);
        PowerMockito.when(mMockBleRegisterController, "onReceiveDataAuthenticationRequest", any()).thenAnswer(voidAnswer);
        PowerMockito.when(mMockBleRegisterController, "onSecurityConfigRequestNotified", any()).thenAnswer(voidAnswer);
        PowerMockito.when(mMockBleRegisterController, "onReceiveDataRegistrationResponse", any()).thenAnswer(voidAnswer);
        PowerMockito.when(mMockBleRegisterController, "onRegistrationStatusIndicationNotified", any()).thenAnswer(voidAnswer);
        PowerMockito.when(mMockVehicleInfoMsgResolver, "resolveMsg", any()).thenAnswer(voidAnswer);
    }

    /**
     * Test method for setupDataLength case 1.
     * Test condition: attsize -5 < MAX_DATA_LENGTH_WRITE
     */
    @Test
    public void setupDataLengthCase1() throws Exception {
        int MAX_DATA_LENGTH_WRITE = 18;
        int attsize = 22;
        PowerMockito.when(mMockDataMsgManager, "setupDataLength", attsize).thenCallRealMethod();

        mMockDataMsgManager.setupDataLength(attsize);
        int actual = Whitebox.getInternalState(mMockDataMsgManager, "mPacketDataMaxLength");
        assertEquals(attsize - 5, actual);
    }

    /**
     * Test method for setupDataLength case 2.
     * Test condition: attsize -5 >= MAX_DATA_LENGTH_WRITE
     */
    @Test
    public void setupDataLengthCase2() throws Exception {
        int MAX_DATA_LENGTH_WRITE = 18;
        int attsize = 25;
        PowerMockito.when(mMockDataMsgManager, "setupDataLength", attsize).thenCallRealMethod();

        mMockDataMsgManager.setupDataLength(attsize);
        int actual = Whitebox.getInternalState(mMockDataMsgManager, "mPacketDataMaxLength");
        assertEquals(MAX_DATA_LENGTH_WRITE, actual);
    }


    /**
     * Test method for setListener.
     * Test condition: mListeners = null, mListeners != contain listener.
     * Expected result: Run method test success. Pass method setListener.
     */
    @Test
    public void setListenerTest() {
        try {
            //Setup mock.
            PowerMockito.when(mMockDataMsgManager, "setListener", mMockListener).thenCallRealMethod();

            //Call real method.
            mMockDataMsgManager.setListener(mMockListener);

            //Verify result.
            List<IF_BluetoothTransferDataListener> mListeners = Whitebox.getInternalState(mMockDataMsgManager, "mListeners");
            assertEquals(mListeners.size(), 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Test method for unRegisterListener.
     * Test condition: No condition.
     * Expected result: Run method test success. Pass method unRegisterListener.
     */
    @Test
    public void unRegisterListenerTest() {
        try {
            //Setup mock.
            PowerMockito.when(mMockDataMsgManager, "unRegisterListener", mMockListener).thenCallRealMethod();
            mMockListeners.add(mMockListener);
            Whitebox.setInternalState(mMockDataMsgManager, "mListeners", mMockListeners);

            //Call real method.
            mMockDataMsgManager.unRegisterListener(mMockListener);

            //Verify result.
            List<IF_BluetoothTransferDataListener> mListeners = Whitebox.getInternalState(mMockDataMsgManager, "mListeners");
            assertEquals(mListeners.size(), 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Test method for sendMessage in Case01.
     * Test condition: isEnableEcrypt = true
     * Expected result: Run method test success. Branch true was executed.
     */
    @Test
    public void sendMessageCase01() {
        try {
            byte[] data = null;

            //Setup mock.
            PowerMockito.when(mMockDataMsgManager, "sendMessage", mMockMsg).thenCallRealMethod();
            Whitebox.setInternalState(mMockDataMsgManager, "isEnableEcrypt", true);
            when(MessageUtils.encode(mMockMsg)).thenReturn(null);

            //Call real method.
            mMockDataMsgManager.sendMessage(mMockMsg);
            PowerMockito.verifyPrivate(MessageUtils.class, times(1)).invoke("encrypt", (Object) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for sendMessage in Case02.
     * Test condition:isEnableEcrypt = false
     * Expected result: Run method test success. Branch false was executed.
     */
    @Test
    public void sendMessageCase02() throws Exception {
        byte[] data = null;
        //Setup mock.
        PowerMockito.when(mMockDataMsgManager, "sendMessage", mMockMsg).thenCallRealMethod();
        Whitebox.setInternalState(mMockDataMsgManager, "isEnableEcrypt", false);
        when(MessageUtils.encode(mMockMsg)).thenReturn(null);

        //Call real method.
        mMockDataMsgManager.sendMessage(mMockMsg);
        PowerMockito.verifyPrivate(MessageUtils.class, times(0)).invoke("encrypt", (Object) null);
    }


    /**
     * Test method for onDataReceived.
     * Test condition: DataLength <3
     */
    @Test
    public void onDataReceivedCase1() throws Exception {
        //Setup mock.
        byte[] data = {1, 2};
        PowerMockito.when(mMockDataMsgManager, "onDataReceived", any()).thenCallRealMethod();

        //Call real method.
        mMockDataMsgManager.onDataReceived(data);

        PowerMockito.verifyPrivate(LogUtils.class, times(1))
                .invoke("e", "onDataReceived error invalid data");
    }

    /**
     * Test method for onDataReceived.
     * Test condition: DataLength > 3, isCheckSumValid = false.
     */
    @Test
    public void onDataReceivedCase2() throws Exception {
        //Setup mock.
        byte[] data = {1, 2, 3};
        Whitebox.setInternalState(mMockDataMsgManager, "mPacketDataLength", 3);
        PowerMockito.when(mMockDataMsgManager, "onDataReceived", any()).thenCallRealMethod();
        PowerMockito.when(mMockDataMsgManager, "isCheckSumValid", any()).thenReturn(false);

        //Call real method.
        mMockDataMsgManager.onDataReceived(data);

        PowerMockito.verifyPrivate(LogUtils.class, times(1))
                .invoke("e", "onDataReceived error invalid checkSum");
    }

    /**
     * Test method for onDataReceived.
     * Test condition: DataLength > 3, isCheckSumValid = true, mMessage = null.
     */
    @Test
    public void onDataReceivedCase3() throws Exception {
        //Setup mock.
        byte[] data = {1, 2, 3};
        Whitebox.setInternalState(mMockDataMsgManager, "mPacketDataLength", 3);
        PowerMockito.when(mMockDataMsgManager, "onDataReceived", any()).thenCallRealMethod();
        PowerMockito.when(mMockDataMsgManager, "isCheckSumValid", any()).thenReturn(true);

        //Call real method.
        mMockDataMsgManager.onDataReceived(data);

        PowerMockito.verifyPrivate(LogUtils.class, times(1))
                .invoke("e", "onDataReceived error invalid header");
    }

    /**
     * Test method for onDataReceived.
     * Test condition: DataLength > 3, isCheckSumValid = true, mMessage != null.
     * msgOffset < mMessage.length.
     */
    @Test
    public void onDataReceivedCase4() throws Exception {
        //Setup mock.
        byte[] data = {1, 2, 3};
        byte[] mMessage = {1, 2, 3};
        Whitebox.setInternalState(mMockDataMsgManager, "mPacketDataLength", 3);
        PowerMockito.when(mMockDataMsgManager, "onDataReceived", any()).thenCallRealMethod();
        PowerMockito.when(mMockDataMsgManager, "isCheckSumValid", any()).thenReturn(true);
        Whitebox.setInternalState(mMockDataMsgManager, "mMessage", mMessage);
        Whitebox.setInternalState(mMockDataMsgManager, "mMessageOffset", 0);
        //Call real method.
        mMockDataMsgManager.onDataReceived(data);

    }

    /**
     * Test method for onDataReceived.
     * Test condition: DataLength > 3, isCheckSumValid = true, mMessage != null.
     * msgOffset > mMessage.length.
     */
    @Test
    public void onDataReceivedCase5() throws Exception {
        //Setup mock.
        byte[] data = {1, 2, 3};
        byte[] mMessage = {1, 2, 3};
        Whitebox.setInternalState(mMockDataMsgManager, "mPacketDataLength", 3);
        PowerMockito.when(mMockDataMsgManager, "onDataReceived", any()).thenCallRealMethod();
        PowerMockito.when(mMockDataMsgManager, "isCheckSumValid", any()).thenReturn(true);
        Whitebox.setInternalState(mMockDataMsgManager, "mMessage", mMessage);
        Whitebox.setInternalState(mMockDataMsgManager, "mMessageOffset", 4);
        //Call real method.
        mMockDataMsgManager.onDataReceived(data);
        PowerMockito.verifyPrivate(LogUtils.class, times(1))
                .invoke("e", "onDataReceived offset error mMessageOffset:" + 4);
    }


    /**
     * Test method for onDataReceived.
     * Test condition: DataLength > 3, isCheckSumValid = true, mMessage != null.
     * msgOffset < mMessage.length, isNeedtoSend = true.
     */
    @Test
    public void onDataReceivedCase6() throws Exception {
        //Setup mock.
        byte[] data = {1, 2, 3};
        byte[] mMessage = {1, 2, 3};
        Whitebox.setInternalState(mMockDataMsgManager, "mPacketDataLength", 3);
        PowerMockito.when(mMockDataMsgManager, "onDataReceived", any()).thenCallRealMethod();
        PowerMockito.when(mMockDataMsgManager, "isCheckSumValid", any()).thenReturn(true);
        Whitebox.setInternalState(mMockDataMsgManager, "mMessage", mMessage);
        Whitebox.setInternalState(mMockDataMsgManager, "mMessageOffset", 0);
        PowerMockito.when(mMockDataMsgManager, "checkHeader", data[0], data.length).thenReturn(true);

        //Call real method.
        mMockDataMsgManager.onDataReceived(data);
        PowerMockito.verifyPrivate(mMockDataMsgManager, times(1))
                .invoke("fullDataReceived", any());
    }

    /**
     * Test method for notifySendMsgFailed.
     * Test condition: No condition.
     * Expected result: Run method test success. Pass method notifySendMsgFailed.
     */
    @Test
    public void onWriteFailedTest() {
        try {
            //Setup mock.
            PowerMockito.when(mMockDataMsgManager, "onWriteFailed", anyInt()).thenCallRealMethod();

            //Call real method.
            mMockDataMsgManager.onWriteFailed(69);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for notifySendMsgFailed.
     * Test condition: No condition.
     * Expected result: Run method test success. Pass method notifySendMsgFailed.
     */
    @Test
    public void notifySendMsgFailedTest() {
        try {
            //Setup mock.
            List<IF_BluetoothTransferDataListener> list = new ArrayList<>();
            list.add(mMockListener);
            PowerMockito.when(mMockDataMsgManager, "notifySendMsgFailed").thenCallRealMethod();
            Whitebox.setInternalState(mMockDataMsgManager, "mListeners", list);

            //Call real method.
            Whitebox.invokeMethod(mMockDataMsgManager, "notifySendMsgFailed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for notifyMsgReceived.
     * Test condition: No condition.
     * Expected result: Run method test success. Pass method notifyMsgReceived.
     */
    @Test
    public void notifyMsgReceivedTest() {
        try {
            //Setup mock.
            List<IF_BluetoothTransferDataListener> list = new ArrayList<>();
            list.add(mMockListener);
            PowerMockito.when(mMockDataMsgManager, "notifyMsgReceived", mMockMsg).thenCallRealMethod();
            Whitebox.setInternalState(mMockDataMsgManager, "mListeners", list);

            //Call real method.
            Whitebox.invokeMethod(mMockDataMsgManager, "notifyMsgReceived", mMockMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for resetInputMessage.
     */
    @Test
    public void resetInputMessageTest() throws Exception {
        PowerMockito.when(mMockDataMsgManager, "resetInputMessage").thenCallRealMethod();

        //Call real method.
        Whitebox.invokeMethod(mMockDataMsgManager, "resetInputMessage");
    }

    /**
     * Test method for fullDataReceived.
     * Test condition: isEnableEcrypt = true, msg = AUTHENTICATION_REQUEST.
     */
    @Test
    public void fullDataReceivedCase1() throws Exception {
        //Setup mock.
        byte[] data = {1, 2, 3};
        Whitebox.setInternalState(mMockDataMsgManager, "isEnableEcrypt", true);
        PowerMockito.when(mMockDataMsgManager, "fullDataReceived", data).thenCallRealMethod();
        when(MessageUtils.decode(any())).thenReturn(mMockMsg);
        when(mMockMsg.getMsgId()).thenReturn(MsgId.AUTHENTICATION_REQUEST);
        //Call real method.
        Whitebox.invokeMethod(mMockDataMsgManager, "fullDataReceived", data);
    }

    /**
     * Test method for fullDataReceived.
     * Test condition: isEnableEcrypt = true, msg = SECURITY_CONFIG_REQUEST.
     */
    @Test
    public void fullDataReceivedCase2() throws Exception {
        //Setup mock.
        byte[] data = {1, 2, 3};
        Whitebox.setInternalState(mMockDataMsgManager, "isEnableEcrypt", true);
        PowerMockito.when(mMockDataMsgManager, "fullDataReceived", data).thenCallRealMethod();
        when(MessageUtils.decode(any())).thenReturn(mMockMsg);
        when(mMockMsg.getMsgId()).thenReturn(MsgId.SECURITY_CONFIG_REQUEST);
        //Call real method.
        Whitebox.invokeMethod(mMockDataMsgManager, "fullDataReceived", data);
    }

    /**
     * Test method for fullDataReceived.
     * Test condition: isEnableEcrypt = true, msg = REGISTRATION_RESPONSE.
     */
    @Test
    public void fullDataReceivedCase3() throws Exception {
        //Setup mock.
        byte[] data = {1, 2, 3};
        Whitebox.setInternalState(mMockDataMsgManager, "isEnableEcrypt", true);
        PowerMockito.when(mMockDataMsgManager, "fullDataReceived", data).thenCallRealMethod();
        when(MessageUtils.decode(any())).thenReturn(mMockMsg);
        when(mMockMsg.getMsgId()).thenReturn(MsgId.REGISTRATION_RESPONSE);
        //Call real method.
        Whitebox.invokeMethod(mMockDataMsgManager, "fullDataReceived", data);
    }

    /**
     * Test method for fullDataReceived.
     * Test condition: isEnableEcrypt = true, msg = REGISTRATION_STATUS_INDICATION.
     */
    @Test
    public void fullDataReceivedCase4() throws Exception {
        //Setup mock.
        byte[] data = {1, 2, 3};
        Whitebox.setInternalState(mMockDataMsgManager, "isEnableEcrypt", true);
        PowerMockito.when(mMockDataMsgManager, "fullDataReceived", data).thenCallRealMethod();
        when(MessageUtils.decode(any())).thenReturn(mMockMsg);
        when(mMockMsg.getMsgId()).thenReturn(MsgId.REGISTRATION_STATUS_INDICATION);
        //Call real method.
        Whitebox.invokeMethod(mMockDataMsgManager, "fullDataReceived", data);
    }

    /**
     * Test method for fullDataReceived.
     * Test condition: isEnableEcrypt = false, msg = GW_VEHICLE_INFO_INDICATION.
     */
    @Test
    public void fullDataReceivedCase5() throws Exception {
        //Setup mock.
        byte[] data = {1, 2, 3};
        Whitebox.setInternalState(mMockDataMsgManager, "isEnableEcrypt", false);
        PowerMockito.when(mMockDataMsgManager, "fullDataReceived", data).thenCallRealMethod();
        when(MessageUtils.decode(any())).thenReturn(mMockMsg);
        when(mMockMsg.getMsgId()).thenReturn(MsgId.GW_VEHICLE_INFO_INDICATION);
        //Call real method.
        Whitebox.invokeMethod(mMockDataMsgManager, "fullDataReceived", data);
    }


    @Test
    public void isCheckSumValidTest() throws Exception {
        byte[] data = {1, 2, 3};
        PowerMockito.when(mMockDataMsgManager, "isCheckSumValid", data).thenCallRealMethod();
        //Call real method.
        Whitebox.invokeMethod(mMockDataMsgManager, "isCheckSumValid", data);
    }

    /**
     * Test method for checkHeader.
     * Test condition: header type = PACKET_ORDER_TYPE_MULTI_END
     */
    @Test
    public void checkHeaderCase1() throws Exception {
        byte header = 126;
        int packetDataSize = 1;
        PowerMockito.when(mMockDataMsgManager, "checkHeader", header, packetDataSize).thenCallRealMethod();
        Whitebox.invokeMethod(mMockDataMsgManager, "checkHeader", header, packetDataSize);
    }

    /**
     * Test method for checkHeader.
     * Test condition: header type = PACKET_ORDER_TYPE_MULTI_MIDDLE
     */
    @Test
    public void checkHeaderCase2() throws Exception {
        byte header = 63;
        int packetDataSize = 1;
        PowerMockito.when(mMockDataMsgManager, "checkHeader", header, packetDataSize).thenCallRealMethod();
        Whitebox.invokeMethod(mMockDataMsgManager, "checkHeader", header, packetDataSize);
    }

    /**
     * Test method for checkHeader.
     * Test condition: header type = PACKET_ORDER_TYPE_MULTI_BEGIN
     */
    @Test
    public void checkHeaderCase3() throws Exception {
        byte header = (byte) 191;
        int packetDataSize = 1;
        PowerMockito.when(mMockDataMsgManager, "checkHeader", header, packetDataSize).thenCallRealMethod();
        Whitebox.invokeMethod(mMockDataMsgManager, "checkHeader", header, packetDataSize);
    }

    /**
     * Test method for checkHeader.
     * Test condition: header type = PACKET_ORDER_TYPE_SINGLE
     */
    @Test
    public void checkHeaderCase4() throws Exception {
        byte header = (byte) 245;
        int packetDataSize = 1;
        PowerMockito.when(mMockDataMsgManager, "checkHeader", header, packetDataSize).thenCallRealMethod();
         Whitebox.invokeMethod(mMockDataMsgManager, "checkHeader", header, packetDataSize);
    }

    /**
     * Test method for checkPacketOrder.
     * Test condition: mMessage = null.
     */
    @Test
    public void checkPacketOrderCase1() throws Exception {
        byte data = 1;
        PowerMockito.when(mMockDataMsgManager, "checkPacketOrder", data).thenCallRealMethod();
        Whitebox.invokeMethod(mMockDataMsgManager, "checkPacketOrder", data);
        PowerMockito.verifyPrivate(LogUtils.class, times(1))
                .invoke("e", "middle or last data is received when buffer is empty");
    }

    /**
     * Test method for checkPacketOrder.
     * Test condition: mMessage != null, data != mExpectedPacketNum - 1.
     */
    @Test
    public void checkPacketOrderCase2() throws Exception {
        byte data = 1;
        byte expectedPacketNum = 1;
        byte[] mMsg = {1, 2, 3};
        PowerMockito.when(mMockDataMsgManager, "checkPacketOrder", data).thenCallRealMethod();
        Whitebox.setInternalState(mMockDataMsgManager, "mMessage", mMsg);
        Whitebox.setInternalState(mMockDataMsgManager, "mExpectedPacketNum", expectedPacketNum);

        Whitebox.invokeMethod(mMockDataMsgManager, "checkPacketOrder", data);
        PowerMockito.verifyPrivate(LogUtils.class, times(1))
                .invoke("e", "packet order is wrong");
    }

    /**
     * Test method for checkPacketOrder.
     * Test condition: mMessage != null, data = mExpectedPacketNum - 1, data !=0.
     */
    @Test
    public void checkPacketOrderCase3() throws Exception {
        byte data = 1;
        byte expectedPacketNum = 2;
        byte[] mMsg = {1, 2, 3};
        PowerMockito.when(mMockDataMsgManager, "checkPacketOrder", data).thenCallRealMethod();
        Whitebox.setInternalState(mMockDataMsgManager, "mMessage", mMsg);
        Whitebox.setInternalState(mMockDataMsgManager, "mExpectedPacketNum", expectedPacketNum);

        boolean actual = Whitebox.invokeMethod(mMockDataMsgManager, "checkPacketOrder", data);
        assertFalse(actual);
    }

    /**
     * Test method for checkPacketOrder.
     * Test condition: mMessage != null, data = mExpectedPacketNum - 1, data =0.
     */
    @Test
    public void checkPacketOrderCase4() throws Exception {
        byte data = 0;
        byte expectedPacketNum = 1;
        byte[] mMsg = {1, 2, 3};
        PowerMockito.when(mMockDataMsgManager, "checkPacketOrder", data).thenCallRealMethod();
        Whitebox.setInternalState(mMockDataMsgManager, "mMessage", mMsg);
        Whitebox.setInternalState(mMockDataMsgManager, "mExpectedPacketNum", expectedPacketNum);

        boolean actual = Whitebox.invokeMethod(mMockDataMsgManager, "checkPacketOrder", data);
        assertTrue(actual);
    }

    /**
     * Test method for allocateBufferFromHeader.
     * Test condition: isReset = false.
     */
    @Test
    public void allocateBufferFromHeaderCase1() throws Exception {
        byte[] mMsg = {1, 2, 3};
        boolean isSingleData = false;
        byte data = 1;
        int packageSize = 1;

        PowerMockito.when(mMockDataMsgManager, "allocateBufferFromHeader", isSingleData, data, packageSize).thenCallRealMethod();
        Whitebox.setInternalState(mMockDataMsgManager, "mMessage", mMsg);

        Whitebox.invokeMethod(mMockDataMsgManager, "allocateBufferFromHeader", isSingleData, data, packageSize);
        PowerMockito.verifyPrivate(LogUtils.class, times(1))
                .invoke("e", "onDataReceived begin message is received when buffer is allocated");
    }

    /**
     * Test method for allocateBufferFromHeader.
     * Test condition: isReset = true, isSingleData = false.
     */
    @Test
    public void allocateBufferFromHeaderCase2() throws Exception {
        boolean isSingleData = false;
        byte data = 1;
        int packageSize = 1;

        PowerMockito.when(mMockDataMsgManager, "allocateBufferFromHeader", isSingleData, data, packageSize).thenCallRealMethod();

        Whitebox.invokeMethod(mMockDataMsgManager, "allocateBufferFromHeader", isSingleData, data, packageSize);
    }

    /**
     * Test method for allocateBufferFromHeader.
     * Test condition: isReset = true, isSingleData = true, packageSize != data + 2.
     */
    @Test
    public void allocateBufferFromHeaderCase3() throws Exception {
        boolean isSingleData = true;
        byte data = 1;
        int packageSize = 1;

        PowerMockito.when(mMockDataMsgManager, "allocateBufferFromHeader", isSingleData, data, packageSize).thenCallRealMethod();

        Whitebox.invokeMethod(mMockDataMsgManager, "allocateBufferFromHeader", isSingleData, data, packageSize);
    }

    /**
     * Test method for makeHeader.
     * Test condition: packetNo = 1.
     */
    @Test
    public void makeHeaderCase1() throws Exception {
        int packetNo = 1;
        int packetSum = 2;
        int length = 3;
        PowerMockito.when(mMockDataMsgManager, "makeHeader", packetNo, packetSum, length).thenCallRealMethod();

        byte actual = Whitebox.invokeMethod(mMockDataMsgManager, "makeHeader", packetNo, packetSum, length);
        assertEquals(-127, actual);
    }

    /**
     * Test method for makeHeader.
     * Test condition: packetNo != 1, packetNo = packetSum.
     */
    @Test
    public void makeHeaderCase2() throws Exception {
        int packetNo = 2;
        int packetSum = 2;
        int length = 3;
        PowerMockito.when(mMockDataMsgManager, "makeHeader", packetNo, packetSum, length).thenCallRealMethod();

        byte actual = Whitebox.invokeMethod(mMockDataMsgManager, "makeHeader", packetNo, packetSum, length);
        assertEquals(67, actual);
    }


    /**
     * Test method for makeHeader.
     * Test condition: packetNo != 1, packetNo != packetSum.
     */
    @Test
    public void makeHeaderCase3() throws Exception {
        int packetNo = 2;
        int packetSum = 3;
        int length = 3;
        PowerMockito.when(mMockDataMsgManager, "makeHeader", packetNo, packetSum, length).thenCallRealMethod();

        byte actual = Whitebox.invokeMethod(mMockDataMsgManager, "makeHeader", packetNo, packetSum, length);
        assertEquals(1, actual);
    }

}
