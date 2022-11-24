package com.honda.connmc.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.text.TextUtils;

import com.honda.connmc.Bluetooth.connect.BleConnectController;
import com.honda.connmc.Bluetooth.message.DataMessageManager;
import com.honda.connmc.Bluetooth.register.BleRegisterController;
import com.honda.connmc.Bluetooth.scan.BleScanController;
import com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Interfaces.listener.connect.IF_VehicleConnectListener;
import com.honda.connmc.Interfaces.listener.register.IF_VehicleRegisterListener;
import com.honda.connmc.Interfaces.listener.scan.IF_VehicleScanListener;
import com.honda.connmc.Interfaces.listener.transition.IF_BluetoothTransferDataListener;
import com.honda.connmc.Model.message.VehicleMessageStatusInquiry;
import com.honda.connmc.Model.message.common.VehicleMessage;
import com.honda.connmc.NativeLib.NativeLibController;
import com.honda.connmc.Utils.LogUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.nio.ByteBuffer;

import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_BTU_NAME_REGISTER_SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        BluetoothManager.class,
        BluetoothDevice.class,
        BleConnectController.class,
        BleRegisterController.class,
        BleScanController.class,
        DataMessageManager.class,
        GattCallback.class,
        BluetoothManager.EnumDeviceConnectStatus.class,
        String.class,
        ByteBuffer.class,
        Thread.class,
        NativeLibController.class,
        IF_VehicleScanListener.class,
        IF_VehicleConnectListener.class,
        IF_VehicleRegisterListener.class,
        IF_BluetoothTransferDataListener.class,
        Activity.class,
        BluetoothUtils.class,
        TextUtils.class,
        BluetoothPrefUtils.class,
        LogUtils.class,
        VehicleMessage.class,
        VehicleMessageStatusInquiry.class
})
public class BluetoothManagerTest {

    @Mock
    private BluetoothManager mBluetoothManager;
    @Mock
    private DataMessageManager mMsgManager;
    @Mock
    private BleConnectController mBleConnectController;
    @Mock
    private BleScanController mBleScanController;
    @Mock
    private BleRegisterController mBleRegisterController;
    @Mock
    private BluetoothDevice mBluetoothDevice;
    @Mock
    private GattCallback mGattCallback;
    @Mock
    private VehicleMessage mVehicleMessage;
    @Mock
    private BluetoothPrefUtils mBluetoothPrefUtils;
    @Mock
    private BluetoothManager.EnumDeviceConnectStatus mDeviceCurrentStatus;
    @Mock
    private int mTransactionIdentity;
    @Mock
    private IF_VehicleScanListener mIF_VehicleScanListener;
    @Mock
    private IF_VehicleConnectListener mIF_VehicleConnectListener;
    @Mock
    private IF_VehicleRegisterListener mIF_VehicleRegisterListener;
    @Mock
    private IF_BluetoothTransferDataListener mIF_BluetoothTransferDataListener;

    @Mock
    private Context mActivity;

    String BtuName = "BTU00001";

    Answer voidAnswer = invocation -> null;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(BluetoothDevice.class);
        PowerMockito.mockStatic(BluetoothManager.EnumDeviceConnectStatus.class);
        PowerMockito.mockStatic(BleScanController.class);
        PowerMockito.mockStatic(BleRegisterController.class);
        PowerMockito.mockStatic(BleConnectController.class);
        PowerMockito.mockStatic(LogUtils.class);
        PowerMockito.mockStatic(BluetoothGattCharacteristic.class);
        PowerMockito.mockStatic(DataMessageManager.class);
        PowerMockito.mockStatic(BluetoothManager.class);
        PowerMockito.mockStatic(IF_VehicleConnectListener.class);
        PowerMockito.mockStatic(IF_VehicleRegisterListener.class);
        PowerMockito.mockStatic(IF_BluetoothTransferDataListener.class);
        PowerMockito.mockStatic(IF_VehicleScanListener.class);
        PowerMockito.mockStatic(BluetoothPrefUtils.class);
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(BluetoothUtils.class);
        PowerMockito.mockStatic(Thread.class);
        PowerMockito.mockStatic(NativeLibController.class);
        PowerMockito.mockStatic(VehicleMessage.class);

        mActivity = mock(Context.class);
        PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "e", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "i", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "w", anyString()).thenAnswer(voidAnswer);
    }

    @Test
    public void testSetDeviceCurrentStatus() throws Exception {
        PowerMockito.when(mBluetoothManager, "setDeviceCurrentStatus", mDeviceCurrentStatus).thenCallRealMethod();

        mBluetoothManager.setDeviceCurrentStatus(mDeviceCurrentStatus);

        BluetoothManager.EnumDeviceConnectStatus actual = Whitebox.getInternalState(mBluetoothManager, "mDeviceCurrentStatus");

        assertEquals(mDeviceCurrentStatus, actual);
    }

    @Test
    public void testSetScanListener() throws Exception {
        PowerMockito.when(mBluetoothManager, "setScanListener", mIF_VehicleScanListener).thenCallRealMethod();
        Whitebox.setInternalState(mBluetoothManager, "mScanController", mBleScanController);

        PowerMockito.when(mBleScanController, "setVehicleScanListener", mIF_VehicleScanListener).thenAnswer(voidAnswer);

        mBluetoothManager.setScanListener(mIF_VehicleScanListener);

        verify(mBluetoothManager, atLeastOnce()).setScanListener(mIF_VehicleScanListener);
    }

    @Test
    public void testSetConnectListener() throws Exception {
        PowerMockito.when(mBluetoothManager, "setConnectListener", mIF_VehicleConnectListener).thenCallRealMethod();
        Whitebox.setInternalState(mBluetoothManager, "mBleConnectController", mBleConnectController);

        PowerMockito.when(mBleConnectController, "setVehicleConnectListener", mIF_VehicleConnectListener).thenAnswer(voidAnswer);

        mBluetoothManager.setConnectListener(mIF_VehicleConnectListener);

        verify(mBluetoothManager, atLeastOnce()).setConnectListener(mIF_VehicleConnectListener);
    }

    @Test
    public void testSetRegisterListener() throws Exception {
        PowerMockito.when(mBluetoothManager, "setRegisterListener", mIF_VehicleRegisterListener).thenCallRealMethod();
        Whitebox.setInternalState(mBluetoothManager, "mBleRegisterController", mBleRegisterController);

        PowerMockito.when(mBleRegisterController, "setRegistrationEventListener", mIF_VehicleRegisterListener).thenAnswer(voidAnswer);

        mBluetoothManager.setRegisterListener(mIF_VehicleRegisterListener);

        verify(mBluetoothManager, atLeastOnce()).setRegisterListener(mIF_VehicleRegisterListener);
    }

    @Test
    public void testSetMessageListener() throws Exception {
        PowerMockito.when(mBluetoothManager, "setMessageListener", mIF_BluetoothTransferDataListener).thenCallRealMethod();
        Whitebox.setInternalState(mBluetoothManager, "mMsgManager", mMsgManager);

        PowerMockito.when(mMsgManager, "setListener", mIF_BluetoothTransferDataListener).thenAnswer(voidAnswer);

        mBluetoothManager.setMessageListener(mIF_BluetoothTransferDataListener);

        verify(mBluetoothManager, atLeastOnce()).setMessageListener(mIF_BluetoothTransferDataListener);
    }

    @Test
    public void testUnRegisterMessageListener() throws Exception {
        PowerMockito.when(mBluetoothManager, "unRegisterMessageListener", mIF_BluetoothTransferDataListener).thenCallRealMethod();
        Whitebox.setInternalState(mBluetoothManager, "mMsgManager", mMsgManager);

        PowerMockito.when(mMsgManager, "unRegisterListener", mIF_BluetoothTransferDataListener).thenAnswer(voidAnswer);

        mBluetoothManager.unRegisterMessageListener(mIF_BluetoothTransferDataListener);

        verify(mBluetoothManager, atLeastOnce()).unRegisterMessageListener(mIF_BluetoothTransferDataListener);
    }

    @Test
    public void testUnRegisterDevice() throws Exception {
        PowerMockito.when(mBluetoothManager, "unRegisterDevice").thenCallRealMethod();

        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.disconnectDevice()).thenReturn(true);
        PowerMockito.when(mBluetoothManager, "resetData").thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBluetoothManager, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController, "unRegister").thenReturn(true);

        mBluetoothManager.unRegisterDevice();

        verify(mBluetoothManager, atLeastOnce()).unRegisterDevice();
    }

    @Test
    public void testStartScan_Case01() throws Exception {
        PowerMockito.when(mBluetoothManager.startScan(mActivity)).thenCallRealMethod();

        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(true);
        Whitebox.setInternalState(mBluetoothManager, "mScanController", mBleScanController);
        PowerMockito.when(mBleScanController.startScan(mActivity)).thenReturn(true);

        boolean actual = mBluetoothManager.startScan(mActivity);

        assertTrue(actual);
    }

    @Test
    public void testStartScan_Case02() {
        PowerMockito.when(mBluetoothManager.startScan(mActivity)).thenCallRealMethod();

        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(false);

        boolean actual = mBluetoothManager.startScan(mActivity);

        assertFalse(actual);
    }

    @Test
    public void testStartScanWithDeviceName() {
        PowerMockito.when(mBluetoothManager.startScan(mActivity, "DeviceName")).thenCallRealMethod();

        Whitebox.setInternalState(mBluetoothManager, "mScanController", mBleScanController);
        PowerMockito.when(mBleScanController.startScan(mActivity, "DeviceName")).thenReturn(true);

        boolean actual = mBluetoothManager.startScan(mActivity, "DeviceName");

        assertTrue(actual);
    }

    @Test
    public void testStopScan() {
        PowerMockito.when(mBluetoothManager.stopScan()).thenCallRealMethod();

        Whitebox.setInternalState(mBluetoothManager, "mScanController", mBleScanController);
        PowerMockito.when(mBleScanController.stopScan()).thenReturn(true);

        boolean actual = mBluetoothManager.stopScan();

        assertTrue(actual);
    }

    @Test
    public void testStartConnect() {
        PowerMockito.when(mBluetoothManager.startConnect(mBluetoothDevice)).thenCallRealMethod();

        Whitebox.setInternalState(mBluetoothManager, "mBleConnectController", mBleConnectController);
        PowerMockito.when(mBleConnectController.startConnect(mBluetoothDevice)).thenReturn(true);

        boolean actual = mBluetoothManager.startConnect(mBluetoothDevice);

        assertTrue(actual);
    }

    @Test
    public void testDisconnectDevice() {
        PowerMockito.when(mBluetoothManager.disconnectDevice()).thenCallRealMethod();

        Whitebox.setInternalState(mBluetoothManager, "mBleConnectController", mBleConnectController);
        PowerMockito.when(mBleConnectController.disconnectDevice()).thenReturn(true);

        boolean actual = mBluetoothManager.disconnectDevice();

        assertTrue(actual);
    }

    @Test
    public void testReConnect_Case01() {
        PowerMockito.when(mBluetoothManager.reConnect(mActivity)).thenCallRealMethod();

        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        PowerMockito.when(mBluetoothPrefUtils.getString(KEY_BTU_NAME_REGISTER_SUCCESS)).thenReturn(BtuName);
        PowerMockito.when(TextUtils.isEmpty(BtuName)).thenReturn(false);
        Whitebox.setInternalState(mBluetoothManager, "mBleConnectController", mBleConnectController);
        PowerMockito.when(mBleConnectController.reConnect(mActivity)).thenReturn(true);

        boolean actual = mBluetoothManager.reConnect(mActivity);

        assertTrue(actual);
    }

    @Test
    public void testReConnect_Case02() {
        PowerMockito.when(mBluetoothManager.reConnect(mActivity)).thenCallRealMethod();
        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        PowerMockito.when(mBluetoothPrefUtils.getString(KEY_BTU_NAME_REGISTER_SUCCESS)).thenReturn(BtuName);
        PowerMockito.when(TextUtils.isEmpty(BtuName)).thenReturn(true);

        boolean actual = mBluetoothManager.reConnect(mActivity);

        assertFalse(actual);
    }

    @Test
    public void testSendMessage() throws Exception {
        PowerMockito.when(mBluetoothManager, "sendMessage", mVehicleMessage).thenCallRealMethod();

        Whitebox.setInternalState(mBluetoothManager, "mMsgManager", mMsgManager);
        PowerMockito.when(mMsgManager, "sendMessage", mVehicleMessage).thenAnswer(voidAnswer);

        mBluetoothManager.sendMessage(mVehicleMessage);

        verify(mBluetoothManager, atLeastOnce()).sendMessage(mVehicleMessage);
    }

    @Test
    public void testGetBleConnectController() throws Exception {
        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenCallRealMethod();

        Whitebox.setInternalState(mBluetoothManager, "mBleConnectController", mBleConnectController);

        BleConnectController actual = mBluetoothManager.getBleConnectController();

        assertEquals(mBleConnectController, actual);
    }

    @Test
    public void testGetDataMessageManager() {
        PowerMockito.when(mBluetoothManager.getDataMessageManager()).thenCallRealMethod();

        Whitebox.setInternalState(mBluetoothManager, "mMsgManager", mMsgManager);

        DataMessageManager actual = mBluetoothManager.getDataMessageManager();

        assertEquals(mMsgManager, actual);
    }

    @Test
    public void testGetBleRegisterController() {
        PowerMockito.when(mBluetoothManager.getBleRegisterController()).thenCallRealMethod();

        Whitebox.setInternalState(mBluetoothManager, "mBleRegisterController", mBleRegisterController);

        BleRegisterController actual = mBluetoothManager.getBleRegisterController();

        assertEquals(mBleRegisterController, actual);
    }

}
