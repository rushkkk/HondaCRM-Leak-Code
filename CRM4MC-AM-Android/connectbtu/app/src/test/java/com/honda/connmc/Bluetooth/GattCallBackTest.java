package com.honda.connmc.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import com.honda.connmc.Bluetooth.message.DataMessageManager;
import com.honda.connmc.Constants.UuidConstants;
import com.honda.connmc.Interfaces.listener.scan.IF_VehicleScanListener;
import com.honda.connmc.Utils.LogUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        GattCallback.class,
        BluetoothGattCallback.class,
        GattCallback.ConnectListener.class,
        GattCallback.DataListener.class,
        BluetoothGattDescriptor.class,
        BluetoothGattCharacteristic.class,
        BluetoothDevice.class,
        BluetoothManager.class,
        DataMessageManager.class,
        UUID.class,
        LogUtils.class,
        BluetoothGatt.class,
})
public class GattCallBackTest {

    @InjectMocks
    private GattCallback mGattCallBack;

    @Mock
    private GattCallback.DataListener mDataListener;

    @Mock
    private BluetoothGattCharacteristic characteristic;

    @Mock
    private BluetoothGattDescriptor mBluetoothGattDescriptor;

    @Mock
    private BluetoothGatt mGatt;

    @Mock
    private BluetoothGatt mBleGatt;
    @Mock
    private DataMessageManager mDataMessageManager;

    @Mock
    private BluetoothManager mBluetoothManager;

    @Mock
    private UUID mUuid;

    @Mock
    private BluetoothDevice mBluetoothDevice;

    @Mock
    private GattCallback.ConnectListener mConnectListener;

    private Answer voidAnswer = invocation -> null;


    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(BluetoothGatt.class);
        PowerMockito.mockStatic(LogUtils.class);
        PowerMockito.mockStatic(GattCallback.DataListener.class);
        PowerMockito.mockStatic(GattCallback.ConnectListener.class);
        PowerMockito.mockStatic(BluetoothGattDescriptor.class);
        PowerMockito.mockStatic(BluetoothGattCharacteristic.class);
        PowerMockito.mockStatic(BluetoothDevice.class);
        PowerMockito.mockStatic(DataMessageManager.class);
        PowerMockito.mockStatic(BluetoothManager.class);

        PowerMockito.when(LogUtils.class, "d", any()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "e", anyString()).thenAnswer(voidAnswer);
    }

    @Test
    public void testSetConnectListener() throws Exception {
        Whitebox.setInternalState(mGattCallBack, "mConnectListener", mConnectListener);

        mGattCallBack.setConnectListener(mConnectListener);

        GattCallback.ConnectListener actual = Whitebox.getInternalState(mGattCallBack, "mConnectListener");

        assertEquals(mConnectListener, actual);
    }

    @Test
    public void testSetDataListener() throws Exception {
        Whitebox.setInternalState(mGattCallBack, "mDataListener", mDataListener);
        mGattCallBack.setDataListener(mDataListener);

        GattCallback.DataListener actual = Whitebox.getInternalState(mGattCallBack, "mDataListener");

        assertEquals(mDataListener, actual);
    }

    @Test
    public void testOnServicesDiscovered_Case01() throws Exception {
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        mGattCallBack.onServicesDiscovered(null, 0);
        PowerMockito.verifyPrivate(LogUtils.class, times(1)).invoke("e", "onServicesDiscovered listener = null");
        PowerMockito.verifyPrivate(LogUtils.class, times(1)).invoke("e", "onServicesDiscovered gatt param == null");
    }

    @Test
    public void testOnServicesDiscovered_Case02() throws Exception {
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mGattCallBack, "mConnectListener", mConnectListener);

        PowerMockito.when(mConnectListener, "onServiceDiscoveryFailed").thenAnswer(voidAnswer);
        mGattCallBack.onServicesDiscovered(mGatt, 1);
        PowerMockito.verifyPrivate(LogUtils.class, times(1)).invoke("e", "onServicesDiscovered status error");
    }

    @Test
    public void testOnServicesDiscovered_Case03() throws Exception {
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mGattCallBack, "mConnectListener", mConnectListener);
        Whitebox.setInternalState(mGattCallBack, "mGatt", mGatt);

        List<BluetoothGattService> list = mock(List.class);
        PowerMockito.when(mGatt.getServices()).thenReturn(list);

        PowerMockito.when(mConnectListener, "onServiceCharacteristicDiscovered", list).thenAnswer(voidAnswer);

        mGattCallBack.onServicesDiscovered(mGatt, 0);
    }

    @Test
    public void testOnConnectionStateChange_Case01() throws Exception {

        Whitebox.setInternalState(mGattCallBack, "mConnectListener", mConnectListener);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "stopScan").thenReturn(true);
        PowerMockito.when(mBluetoothManager.getDataMessageManager()).thenReturn(mDataMessageManager);
        PowerMockito.when(mDataMessageManager, "setEnableEncrypt", false).thenAnswer(voidAnswer);

        PowerMockito.when(mConnectListener, "onDisconnected", mGatt, BluetoothGatt.GATT_FAILURE).thenAnswer(voidAnswer);

        mGattCallBack.onConnectionStateChange(mGatt, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_DISCONNECTED);

    }

    @Test
    public void testOnConnectionStateChange_Case02() throws Exception {
        Whitebox.setInternalState(mGattCallBack, "mConnectListener", mConnectListener);

        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "stopScan").thenReturn(true);
        PowerMockito.when(mBluetoothManager.getDataMessageManager()).thenReturn(mDataMessageManager);
        PowerMockito.when(mDataMessageManager, "setEnableEncrypt", false).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mGattCallBack, "mGatt", mBleGatt);
        PowerMockito.doNothing().when(mGatt, "close");

        PowerMockito.when(mGatt.getDevice()).thenReturn(mBluetoothDevice);
        PowerMockito.when(mBluetoothDevice.getAddress()).thenReturn("192.168.1.1");
        PowerMockito.when(mConnectListener, "onConnected", mGatt).thenAnswer(voidAnswer);

        mGattCallBack.onConnectionStateChange(mGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
    }

    @Test
    public void testOnConnectionStateChange_Case03() throws Exception {
        Whitebox.setInternalState(mGattCallBack, "mConnectListener", mConnectListener);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "stopScan").thenReturn(true);
        PowerMockito.when(mBluetoothManager.getDataMessageManager()).thenReturn(mDataMessageManager);
        PowerMockito.when(mDataMessageManager, "setEnableEncrypt", false).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mGattCallBack, "mGatt", mBleGatt);
        PowerMockito.doNothing().when(mGatt, "close");

        PowerMockito.when(mGatt.getDevice()).thenReturn(mBluetoothDevice);
        PowerMockito.when(mBluetoothDevice.getAddress()).thenReturn("192.168.1.1");
        PowerMockito.when(mConnectListener, "onDisconnected", mGatt, BluetoothGatt.GATT_SUCCESS).thenAnswer(voidAnswer);

        mGattCallBack.onConnectionStateChange(mGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);
    }

    @Test
    public void testOnCharacteristicChanged_Case01() throws Exception {
        byte[] data = {0, 1};
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(characteristic.getUuid()).thenReturn(mUuid);
        PowerMockito.when(mUuid.toString()).thenReturn(UuidConstants.UuidATS.ASYNCH_INPUT_DATA);
        PowerMockito.when(characteristic.getValue()).thenReturn(data);
        Whitebox.setInternalState(mGattCallBack, "mDataListener", mDataListener);
        PowerMockito.when(mDataListener, "onDataReceived", data).thenAnswer(voidAnswer);

        mGattCallBack.onCharacteristicChanged(mGatt, characteristic);
    }

    @Test
    public void testOnCharacteristicChanged_Case02() throws Exception {
        byte[] data = {0, 1};
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(characteristic.getUuid()).thenReturn(mUuid);
        PowerMockito.when(mUuid.toString()).thenReturn(UuidConstants.UuidATS.ASYNCH_OUTPUT_RESP);
        PowerMockito.when(characteristic.getValue()).thenReturn(data);

        mGattCallBack.onCharacteristicChanged(mGatt, characteristic);
    }

    @Test
    public void testOnCharacteristicChanged_Case03() throws Exception {
        byte[] data = {0, 1};
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(characteristic.getUuid()).thenReturn(mUuid);
        PowerMockito.when(mUuid.toString()).thenReturn(UuidConstants.UuidBPS.ATT_MTU);
        PowerMockito.when(characteristic.getValue()).thenReturn(data);
        Whitebox.setInternalState(mGattCallBack, "mConnectListener", mConnectListener);
        PowerMockito.when(mConnectListener, "onBpsDataReceived", characteristic, data).thenAnswer(voidAnswer);

        mGattCallBack.onCharacteristicChanged(mGatt, characteristic);
    }

    @Test
    public void testOnCharacteristicChanged_Case04() throws Exception {
        byte[] data = {0, 1};
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(characteristic.getUuid()).thenReturn(mUuid);
        PowerMockito.when(mUuid.toString()).thenReturn(UuidConstants.UuidBPS.CONN_PARAM);
        PowerMockito.when(characteristic.getValue()).thenReturn(data);
        Whitebox.setInternalState(mGattCallBack, "mConnectListener", mConnectListener);
        PowerMockito.when(mConnectListener, "onBpsDataReceived", characteristic, data).thenAnswer(voidAnswer);

        mGattCallBack.onCharacteristicChanged(mGatt, characteristic);
    }

    @Test
    public void testOnCharacteristicWrite_Case01() throws Exception {

    }

    @Test
    public void testOnDescriptorWrite_Case01() throws Exception {

        mGattCallBack.onDescriptorWrite(null, mBluetoothGattDescriptor, BluetoothGatt.GATT_SUCCESS);

        PowerMockito.verifyPrivate(LogUtils.class, times(1)).invoke("e", "onDescriptorWrite gatt param == null");
        PowerMockito.verifyPrivate(LogUtils.class, times(1)).invoke("e", "onDescriptorWrite listener = null");
    }

    @Test
    public void testOnDescriptorWrite_Case02() throws Exception {
        Whitebox.setInternalState(mGattCallBack, "mConnectListener", mConnectListener);

        PowerMockito.when(mConnectListener, "onCharacteristicNotifyEnableFailed", null, BluetoothGatt.GATT_SUCCESS).thenAnswer(voidAnswer);
        mGattCallBack.onDescriptorWrite(mGatt, null, BluetoothGatt.GATT_SUCCESS);

        PowerMockito.verifyPrivate(LogUtils.class, times(1)).invoke("e", "onDescriptorWrite status error");

    }

    @Test
    public void testOnDescriptorWrite_Case03() throws Exception {
        Whitebox.setInternalState(mGattCallBack, "mConnectListener", mConnectListener);

        PowerMockito.when(mConnectListener, "onCharacteristicNotifyEnableFailed", null, BluetoothGatt.GATT_SUCCESS).thenAnswer(voidAnswer);

        mGattCallBack.onDescriptorWrite(mGatt, mBluetoothGattDescriptor, BluetoothGatt.GATT_FAILURE);
        PowerMockito.verifyPrivate(LogUtils.class, times(1)).invoke("e", "onDescriptorWrite status error");
    }

    @Test
    public void testOnDescriptorWrite_Case04() throws Exception {
        Whitebox.setInternalState(mGattCallBack, "mConnectListener", mConnectListener);

        PowerMockito.when(mBluetoothGattDescriptor.getCharacteristic()).thenReturn(characteristic);
        PowerMockito.when(mConnectListener, "onCharacteristicNotifyEnabled", characteristic).thenAnswer(voidAnswer);

        mGattCallBack.onDescriptorWrite(mGatt, mBluetoothGattDescriptor, BluetoothGatt.GATT_SUCCESS);
    }

    @Test
    public void testEnableCharacteristicNotification_Case01() throws Exception {
        PowerMockito.when(mGattCallBack.enableCharacteristicNotification(null)).thenCallRealMethod();

        assertFalse(mGattCallBack.enableCharacteristicNotification(null));
    }

    @Test
    public void testEnableCharacteristicNotification_Case02() throws Exception {
        PowerMockito.when(mGattCallBack.enableCharacteristicNotification(characteristic)).thenCallRealMethod();

        assertFalse(mGattCallBack.enableCharacteristicNotification(characteristic));
    }

    @Test
    public void testEnableCharacteristicNotification_Case03() throws Exception {
        Whitebox.setInternalState(mGattCallBack, "mGatt", mGatt);
        PowerMockito.when(characteristic.getDescriptor(any(UUID.class))).thenReturn(null);

        assertFalse(mGattCallBack.enableCharacteristicNotification(characteristic));
    }

    @Test
    public void testEnableCharacteristicNotification_Case04() throws Exception {
        PowerMockito.when(mGattCallBack.enableCharacteristicNotification(null)).thenCallRealMethod();
        Whitebox.setInternalState(mGattCallBack, "mGatt", mGatt);
        PowerMockito.when(characteristic.getDescriptor(any(UUID.class))).thenReturn(mBluetoothGattDescriptor);
        int properties = 0;
        PowerMockito.when(characteristic.getProperties()).thenReturn(properties);

        assertFalse(mGattCallBack.enableCharacteristicNotification(characteristic));
    }

    @Test
    public void testEnableCharacteristicNotification_Case05() throws Exception {
        Whitebox.setInternalState(mGattCallBack, "mGatt", mGatt);
        PowerMockito.when(characteristic.getDescriptor(any(UUID.class))).thenReturn(mBluetoothGattDescriptor);
        int properties = BluetoothGattCharacteristic.PROPERTY_NOTIFY;
        PowerMockito.when(characteristic.getProperties()).thenReturn(properties);

        assertFalse(mGattCallBack.enableCharacteristicNotification(characteristic));
    }

    @Test
    public void testEnableCharacteristicNotification_Case06() throws Exception {
        Whitebox.setInternalState(mGattCallBack, "mGatt", mGatt);
        PowerMockito.when(characteristic.getDescriptor(any(UUID.class))).thenReturn(mBluetoothGattDescriptor);
        int properties = BluetoothGattCharacteristic.PROPERTY_NOTIFY;
        PowerMockito.when(characteristic.getProperties()).thenReturn(properties);

        PowerMockito.when(mGatt.setCharacteristicNotification(characteristic, true)).thenReturn(true);
        byte[] data = {0, 1};
        PowerMockito.when(mBluetoothGattDescriptor, "setValue", data).thenReturn(true);
        PowerMockito.when(mGatt.writeDescriptor(mBluetoothGattDescriptor)).thenReturn(true);
        assertFalse(mGattCallBack.enableCharacteristicNotification(characteristic));
    }
}
