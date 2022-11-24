package com.honda.connmc.Bluetooth.connect;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.GattCallback;
import com.honda.connmc.Bluetooth.message.DataMessageManager;
import com.honda.connmc.Bluetooth.timer.IF_TimeOutListener;
import com.honda.connmc.Bluetooth.timer.IF_TimeScheduleListener;
import com.honda.connmc.Bluetooth.timer.TimeManager;
import com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Constants.UuidConstants;
import com.honda.connmc.Interfaces.listener.connect.IF_VehicleConnectListener;
import com.honda.connmc.Interfaces.listener.scan.IF_VehicleScanListener;
import com.honda.connmc.Model.BluetoothScan;
import com.honda.connmc.NativeLib.NativeLibController;
import com.honda.connmc.Utils.LogUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.honda.connmc.Bluetooth.BluetoothManager.EnumDeviceConnectStatus.CONNECTED;
import static com.honda.connmc.Bluetooth.BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED;
import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_BTU_NAME_REGISTER_SUCCESS;
import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_REGISTER_SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BleConnectController.class,
        TimeManager.class,
        IF_TimeOutListener.class,
        IF_TimeScheduleListener.class,
        GattCallback.class,
        GattCallback.ConnectListener.class,
        BluetoothGattService.class,
        BluetoothGattCharacteristic.class,
        IF_VehicleConnectListener.class,
        BluetoothDevice.class,
        BluetoothScan.class,
        BluetoothManager.EnumDeviceConnectStatus.class,
        DataMessageManager.class,
        BluetoothManager.class,
        UuidConstants.class,
        LogUtils.class,
        Thread.class,
        NativeLibController.class,
        Object.class,
        RemoteException.class,
        IF_VehicleScanListener.class,
        UUID.class,
        TextUtils.class,
        BluetoothPrefUtils.class,
        Application.class,
        BluetoothUtils.class,
        Activity.class,
        BluetoothGatt.class})
public class BleConnectControllerTest {
    @Mock
    private BleConnectController mBleConnectController;

    @Mock
    private TimeManager mTimeManager;

    @Mock
    private IF_TimeOutListener if_timeOutListener;

    @Mock
    private IF_TimeScheduleListener if_timeScheduleListener;

    @Mock
    private BluetoothManager.EnumDeviceConnectStatus mBleConnectState;

    @Mock
    private BluetoothGatt mBluetoothGatt;

    @Mock
    private Application mApplication;

    @Mock
    private GattCallback mGatCallBack;

    @Mock
    private Object mObject;

    @Mock
    private NativeLibController mNativeLibController;

    @Mock
    private BluetoothScan mBluetoothScan;
    @Mock
    private BluetoothGattCharacteristic mBleGattCharacteristic;

    @Mock
    private BluetoothDevice mBluetoothDevice;

    @Mock
    private BluetoothManager mBluetoothManager;

    @Mock
    private DataMessageManager mDataMessageManager;

    @Mock
    private UUID mUuid;

    @Mock
    private BluetoothPrefUtils prefUtils;

    @Mock
    private IF_VehicleScanListener if_vehicleScanListener;

    @Mock
    private IF_VehicleConnectListener if_vehicleConnectListener;

    @Mock
    private List<BluetoothGattService> mListBluetoothGattService;

    @Mock
    private List<BluetoothGattCharacteristic> mListBluetoothGattCharacteristic;

    @Mock
    private BluetoothGattService mBluetoothGattService;

    @Mock
    private Map<String, BluetoothGattCharacteristic> mCharacteristics;

    @Mock
    private Map<String, BluetoothGattService> mServices = null;

    @Mock
    private BluetoothPrefUtils mBluetoothPrefUtils;

    @Mock
    private Activity mActivity;
    private List<IF_VehicleConnectListener> mListeners;

    @Mock
    private Context mContext;

    private byte[] data = {0x00};

    Answer voidAnswer = invocation -> null;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(BleConnectController.class);
        PowerMockito.mockStatic(BluetoothDevice.class);
        PowerMockito.mockStatic(BluetoothGatt.class);
        PowerMockito.mockStatic(BluetoothManager.EnumDeviceConnectStatus.class);
        PowerMockito.mockStatic(TimeManager.class);
        PowerMockito.mockStatic(LogUtils.class);
        PowerMockito.mockStatic(BluetoothGattCharacteristic.class);
        PowerMockito.mockStatic(DataMessageManager.class);
        PowerMockito.mockStatic(BluetoothManager.class);
        PowerMockito.mockStatic(BluetoothGattService.class);
        PowerMockito.mockStatic(IF_TimeScheduleListener.class);
        PowerMockito.mockStatic(BluetoothPrefUtils.class);
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(BluetoothUtils.class);
        PowerMockito.mockStatic(Thread.class);
        PowerMockito.mockStatic(NativeLibController.class);
        PowerMockito.mockStatic(Object.class);
        PowerMockito.mockStatic(Application.class);

        mListeners = mock(List.class);
        mContext = mock(Context.class);
        Whitebox.setInternalState(mBleConnectController, "LOCK_OBJECT", mObject);
        PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "e", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "i", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "w", anyString()).thenAnswer(voidAnswer);
    }

    @Test
    public void testSetVehicleConnectListener_Case01() throws Exception {
        List<IF_VehicleConnectListener> mListeners = mock(List.class);
        PowerMockito.when(mBleConnectController, "setVehicleConnectListener", if_vehicleConnectListener).thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mListeners", mListeners);

        PowerMockito.when(mListeners, "contains", if_vehicleConnectListener).thenReturn(false);
        PowerMockito.when(mListeners, "add", if_vehicleConnectListener).thenAnswer(voidAnswer);


        mBleConnectController.setVehicleConnectListener(if_vehicleConnectListener);

        verify(mBleConnectController, atLeastOnce()).setVehicleConnectListener(if_vehicleConnectListener);
    }

    @Test
    public void testSetVehicleConnectListener_Case02() throws Exception {
        PowerMockito.when(mBleConnectController, "setVehicleConnectListener", (Object) null).thenCallRealMethod();

//
//        PowerMockito.when(mListeners, "contains", if_vehicleConnectListener).thenReturn(false);
//        PowerMockito.when(mListeners, "add", if_vehicleConnectListener).thenAnswer(voidAnswer);


        mBleConnectController.setVehicleConnectListener(null);

        verify(mBleConnectController, atLeastOnce()).setVehicleConnectListener(null);
    }

//    @Test
//    public void testUnRegisterListener() throws Exception {
//        PowerMockito.when(mBleConnectController, "unRegisterListener", if_vehicleConnectListener).thenCallRealMethod();
//
//        Whitebox.setInternalState(mBleConnectController, "mListeners", mListeners);
//
//        PowerMockito.when(mListeners.size()).thenReturn(5);
//        PowerMockito.when(mListeners, "add", if_vehicleConnectListener).thenAnswer(voidAnswer);
//
//        PowerMockito.when(mListeners, "remove", if_vehicleConnectListener).thenAnswer(voidAnswer);
//
////        boolean actual = mBleConnectController.unRegisterListener(if_vehicleConnectListener);
//
////        assertFalse(actual);
//    }

    @Test
    public void testConnectDevice() throws Exception {
        PowerMockito.when(mBleConnectController, "connectDevice", mBluetoothDevice).thenCallRealMethod();
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getApplicationContext()).thenReturn(mApplication);
        Whitebox.setInternalState(mBleConnectController, "mBluetoothDeviceConnect", mBluetoothDevice);
        PowerMockito.when(mBluetoothDevice.connectGatt(mApplication, false, mGatCallBack)).thenReturn(mBluetoothGatt);
        boolean actual = Whitebox.invokeMethod(mBleConnectController, "connectDevice", mBluetoothDevice);

        assertFalse(actual);
    }

    @Test
    public void testStartConnect_Case01() throws Exception {
        when(mBleConnectController.startConnect(mBluetoothDevice)).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleConnectController, "mIsConnecting", true);

        boolean actual = mBleConnectController.startConnect(mBluetoothDevice);

        assertFalse(actual);
    }

    @Test
    public void testStartConnect_Case02() throws Exception {
        when(mBleConnectController.startConnect(mBluetoothDevice)).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleConnectController, "mIsConnecting", false);

        Whitebox.setInternalState(mBleConnectController, "mTimeManager", mTimeManager);
        PowerMockito.doNothing().when(mTimeManager, "startTimer", if_timeOutListener, 30000L);
        PowerMockito.when(mBleConnectController, "connectDevice", mBluetoothDevice).thenReturn(true);

        mBleConnectController.startConnect(mBluetoothDevice);

        boolean actual = mBleConnectController.startConnect(mBluetoothDevice);

        assertFalse(actual);
    }

    @Test
    public void testDisconnectDevice_Case01() throws Exception {
        when(mBleConnectController.disconnectDevice()).thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mIsConnecting", false);

        PowerMockito.when(mBleConnectController, "removeServiceCharacteristicList").thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleConnectController, "mGatt", (Object) null);

        PowerMockito.doNothing().when(mBluetoothGatt, "disconnect");

        PowerMockito.when(mBluetoothGatt, "close").thenAnswer(voidAnswer);

        boolean actual = mBleConnectController.disconnectDevice();

        assertFalse(actual);
    }

    @Test
    public void testDisconnectDevice_Case02() throws Exception {
        when(mBleConnectController.disconnectDevice()).thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mIsConnecting", false);

        PowerMockito.when(mBleConnectController, "removeServiceCharacteristicList").thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleConnectController, "mGatt", mBluetoothGatt);

        PowerMockito.doNothing().when(mBluetoothGatt, "disconnect");

        PowerMockito.when(mBluetoothGatt, "close").thenAnswer(voidAnswer);

        boolean actual = mBleConnectController.disconnectDevice();

        assertTrue(actual);
    }

    @Test
    public void testReConnect_Case01() throws Exception {
        PowerMockito.when(mBleConnectController, "reConnect", mContext).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleConnectController, "mIsConnecting", true);
        boolean actual = mBleConnectController.reConnect(mContext);

        assertFalse(actual);

    }

    @Test
    public void testReConnect_Case02() throws Exception {
        String BtuName = "BTU00001";
        PowerMockito.when(mBleConnectController, "reConnect", mContext).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        doAnswer(voidAnswer).when(mBluetoothManager).setScanListener(if_vehicleScanListener);

        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(prefUtils);
        PowerMockito.when(prefUtils.getString(KEY_BTU_NAME_REGISTER_SUCCESS)).thenReturn(BtuName);
        Whitebox.setInternalState(mBleConnectController, "mBluetoothDeviceConnect", mBluetoothDevice);
        PowerMockito.when(mBluetoothDevice.getName()).thenReturn(BtuName);

        PowerMockito.doAnswer(voidAnswer).when(mBluetoothManager).startScan(mContext, BtuName);

        boolean actual = mBleConnectController.reConnect(mContext);

        assertTrue(actual);

    }

    @Test
    public void testReConnect_Case03() throws Exception {
        PowerMockito.when(mBleConnectController, "reConnect", mActivity).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        doAnswer(voidAnswer).when(mBluetoothManager).setScanListener(if_vehicleScanListener);

        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(prefUtils);
        PowerMockito.when(prefUtils.getString(KEY_BTU_NAME_REGISTER_SUCCESS)).thenReturn("BTU01");

        PowerMockito.doAnswer(voidAnswer).when(mBluetoothManager).startScan(mActivity, "BTU01");

        boolean actual = mBleConnectController.reConnect(mActivity);

        assertTrue(actual);

    }

    @Test
    public void testAutoReConnect_Case01() throws Exception {
        PowerMockito.when(mBleConnectController, "autoReConnect", "Name").thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleConnectController, "mIsConnecting", true);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "autoReConnect", "Name");

        assertFalse(actual);

    }

    @Test
    public void testAutoReConnect_Case02() throws Exception {
        String btuName = "BTU00001";
        PowerMockito.when(mBleConnectController, "autoReConnect", "Name").thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleConnectController, "mIsConnecting", false);
        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(false);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "autoReConnect", "Name");

        assertFalse(actual);

    }

    @Test
    public void testAutoReConnect_Case03() throws Exception {
        String btuName = "";
        PowerMockito.when(mBleConnectController, "autoReConnect", btuName).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleConnectController, "mIsConnecting", false);
        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(true);
        PowerMockito.when(TextUtils.isEmpty(btuName)).thenReturn(true);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "autoReConnect", btuName);

        assertFalse(actual);

    }

    @Test
    public void testAutoReConnect_Case04() throws Exception {
        String btuName = "BTU00001";
        PowerMockito.when(mBleConnectController, "autoReConnect", "Name").thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleConnectController, "mIsConnecting", false);
        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(true);
        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        PowerMockito.when(mBluetoothPrefUtils.getString(KEY_BTU_NAME_REGISTER_SUCCESS)).thenReturn(btuName);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getDeviceCurrentStatus()).thenReturn(BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE);
        PowerMockito.when(mBluetoothManager, "setScanListener", if_vehicleScanListener).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothManager, "startScan", null, btuName).thenAnswer(voidAnswer);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "autoReConnect", "Name");

        assertTrue(actual);

    }

    @Test
    public void testAutoReConnect_Case05() throws Exception {
        String btuName = "BTU00001";
        PowerMockito.when(mBleConnectController, "autoReConnect", "Name").thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleConnectController, "mIsConnecting", false);
        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(true);
        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        PowerMockito.when(mBluetoothPrefUtils.getString(KEY_BTU_NAME_REGISTER_SUCCESS)).thenReturn(btuName);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getDeviceCurrentStatus()).thenReturn(BluetoothManager.EnumDeviceConnectStatus.INITIAL);
        PowerMockito.when(mBluetoothManager, "setScanListener", if_vehicleScanListener).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothManager, "startScan", null, btuName).thenAnswer(voidAnswer);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "autoReConnect", "Name");

        assertTrue(actual);

    }

    @Test
    public void testAutoReConnect_Case06() throws Exception {
        String btuName = "BTU00001";
        PowerMockito.when(mBleConnectController, "autoReConnect", "Name").thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleConnectController, "mIsConnecting", false);
        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(true);
        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        PowerMockito.when(mBluetoothPrefUtils.getString(KEY_BTU_NAME_REGISTER_SUCCESS)).thenReturn(btuName);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getDeviceCurrentStatus()).thenReturn(BluetoothManager.EnumDeviceConnectStatus.BTU_INACTIVE);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "autoReConnect", "Name");

        assertTrue(actual);

    }

    @Test
    public void testOnServiceCharacteristicDiscovered_Case01() throws Exception {
        PowerMockito.when(mBleConnectController, "onServiceCharacteristicDiscovered", (Object) null).thenCallRealMethod();

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", DISCONNECTED);

        mBleConnectController.onServiceCharacteristicDiscovered(null);

    }

    @Test
    public void testOnServiceCharacteristicDiscovered_Case02() throws Exception {
        PowerMockito.when(mBleConnectController, "onServiceCharacteristicDiscovered", mListBluetoothGattService).thenCallRealMethod();

        PowerMockito.when(mListBluetoothGattService.size()).thenReturn(0);

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onServiceCharacteristicDiscovered(mListBluetoothGattService);

    }

    @Test
    public void testOnServiceCharacteristicDiscovered_Case03() throws Exception {
        PowerMockito.when(mBleConnectController, "onServiceCharacteristicDiscovered", mListBluetoothGattService).thenCallRealMethod();

        PowerMockito.when(mListBluetoothGattService.size()).thenReturn(5);

        mBleConnectController.onServiceCharacteristicDiscovered(mListBluetoothGattService);

        PowerMockito.when(mBleConnectController, "createServiceCharacteristicList", mListBluetoothGattService).then(voidAnswer);

        PowerMockito.when(mBleConnectController, "isAtsSupport").thenReturn(false);

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onServiceCharacteristicDiscovered(mListBluetoothGattService);

    }

    @Test
    public void testOnServiceCharacteristicDiscovered_Case04() throws Exception {
        PowerMockito.when(mBleConnectController, "onServiceCharacteristicDiscovered", mListBluetoothGattService).thenCallRealMethod();

        PowerMockito.when(mListBluetoothGattService.size()).thenReturn(5);

        mBleConnectController.onServiceCharacteristicDiscovered(mListBluetoothGattService);

        PowerMockito.when(mBleConnectController, "createServiceCharacteristicList", mListBluetoothGattService).then(voidAnswer);

        PowerMockito.when(mBleConnectController, "isAtsSupport").thenReturn(true);

        PowerMockito.when(mBleConnectController, "isAtsSupport").thenReturn(true);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidBPS.ATT_MTU)).thenReturn(mBleGattCharacteristic);

        boolean ret = true;
        Whitebox.setInternalState(mBleConnectController, "mGattCallback", mGatCallBack);

        PowerMockito.when(mGatCallBack.enableCharacteristicNotification(mBleGattCharacteristic)).thenReturn(ret);

        assertTrue(ret);

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onServiceCharacteristicDiscovered(mListBluetoothGattService);

    }

    @Test
    public void testOnServiceCharacteristicDiscovered_Case05() throws Exception {
        PowerMockito.when(mBleConnectController, "onServiceCharacteristicDiscovered", mListBluetoothGattService).thenCallRealMethod();

        PowerMockito.when(mListBluetoothGattService.size()).thenReturn(5);

        mBleConnectController.onServiceCharacteristicDiscovered(mListBluetoothGattService);

        PowerMockito.when(mBleConnectController, "createServiceCharacteristicList", mListBluetoothGattService).then(voidAnswer);

        PowerMockito.when(mBleConnectController, "isAtsSupport").thenReturn(true);

        PowerMockito.when(mBleConnectController, "isBpsServiceSupport").thenReturn(true);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidBPS.ATT_MTU)).thenReturn(mBleGattCharacteristic);

        boolean ret = false;
        Whitebox.setInternalState(mBleConnectController, "mGattCallback", mGatCallBack);

        PowerMockito.when(mGatCallBack.enableCharacteristicNotification(mBleGattCharacteristic)).thenReturn(ret);

        assertFalse(ret);

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onServiceCharacteristicDiscovered(mListBluetoothGattService);

    }

    @Test
    public void testOnServiceCharacteristicDiscovered_Case06() throws Exception {
        PowerMockito.when(mBleConnectController, "onServiceCharacteristicDiscovered", mListBluetoothGattService).thenCallRealMethod();

        PowerMockito.when(mListBluetoothGattService.size()).thenReturn(5);

        mBleConnectController.onServiceCharacteristicDiscovered(mListBluetoothGattService);

        PowerMockito.when(mBleConnectController, "createServiceCharacteristicList", mListBluetoothGattService).then(voidAnswer);

        PowerMockito.when(mBleConnectController, "isAtsSupport").thenReturn(true);

        PowerMockito.when(mBleConnectController, "isBpsServiceSupport").thenReturn(false);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidATS.ASYNCH_INPUT_DATA)).thenReturn(mBleGattCharacteristic);

        boolean ret = true;
        Whitebox.setInternalState(mBleConnectController, "mGattCallback", mGatCallBack);

        PowerMockito.when(mGatCallBack.enableCharacteristicNotification(mBleGattCharacteristic)).thenReturn(ret);

        assertTrue(ret);

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onServiceCharacteristicDiscovered(mListBluetoothGattService);

    }

    @Test
    public void testOnServiceCharacteristicDiscovered_Case07() throws Exception {
        PowerMockito.when(mBleConnectController, "onServiceCharacteristicDiscovered", mListBluetoothGattService).thenCallRealMethod();

        PowerMockito.when(mListBluetoothGattService.size()).thenReturn(5);

        mBleConnectController.onServiceCharacteristicDiscovered(mListBluetoothGattService);

        PowerMockito.when(mBleConnectController, "createServiceCharacteristicList", mListBluetoothGattService).then(voidAnswer);

        PowerMockito.when(mBleConnectController, "isAtsSupport").thenReturn(true);

        PowerMockito.when(mBleConnectController, "isBpsServiceSupport").thenReturn(false);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidATS.ASYNCH_INPUT_DATA)).thenReturn(mBleGattCharacteristic);

        boolean ret = false;
        Whitebox.setInternalState(mBleConnectController, "mGattCallback", mGatCallBack);

        PowerMockito.when(mGatCallBack.enableCharacteristicNotification(mBleGattCharacteristic)).thenReturn(ret);

        assertFalse(ret);

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onServiceCharacteristicDiscovered(mListBluetoothGattService);

    }

    @Test
    public void testOnServiceDiscoveryFailed() throws Exception {
        PowerMockito.when(mBleConnectController, "onServiceDiscoveryFailed").thenCallRealMethod();

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onServiceDiscoveryFailed();
    }


    @Test
    public void testOnCharacteristicNotifyEnabled_Case01() throws Exception {
        PowerMockito.when(mBleConnectController, "onCharacteristicNotifyEnabled", (Object) null).thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "LOCK_OBJECT", mObject);
        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onCharacteristicNotifyEnabled(null);

    }

    @Test
    public void testOnCharacteristicNotifyEnabled_Case02() throws Exception {
        PowerMockito.when(mBleConnectController, "onCharacteristicNotifyEnabled", mBleGattCharacteristic).thenCallRealMethod();

        PowerMockito.when(mBleGattCharacteristic.getUuid()).thenReturn(mUuid);

        PowerMockito.when(mUuid.toString()).thenReturn("22222222-0000-4489-0000-ffa28cde82ab");

        assertEquals(UuidConstants.UuidBPS.ATT_MTU, "22222222-0000-4489-0000-ffa28cde82ab");

        Whitebox.setInternalState(mBleConnectController, "mGattCallback", mGatCallBack);
        boolean ret = false;
        byte[] data = {0x00};
        PowerMockito.when(mGatCallBack.write(mBleGattCharacteristic, data)).thenReturn(ret);

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onCharacteristicNotifyEnabled(mBleGattCharacteristic);

    }

    @Test
    public void testOnCharacteristicNotifyEnabled_Case03() throws Exception {
        PowerMockito.when(mBleConnectController, "onCharacteristicNotifyEnabled", mBleGattCharacteristic).thenCallRealMethod();

        PowerMockito.when(mBleGattCharacteristic.getUuid()).thenReturn(mUuid);

        PowerMockito.when(mUuid.toString()).thenReturn("22222222-0000-4489-0000-ffa28cde82ab");

        assertEquals(UuidConstants.UuidBPS.ATT_MTU, "22222222-0000-4489-0000-ffa28cde82ab");

        Whitebox.setInternalState(mBleConnectController, "mGattCallback", mGatCallBack);
        boolean ret = true;
        byte[] data = {0x00};
        PowerMockito.when(mGatCallBack.write(mBleGattCharacteristic, data)).thenReturn(ret);

        mBleConnectController.onCharacteristicNotifyEnabled(mBleGattCharacteristic);
    }

    @Test
    public void testOnCharacteristicNotifyEnabled_Case04() throws Exception {
        PowerMockito.when(mBleConnectController, "onCharacteristicNotifyEnabled", mBleGattCharacteristic).thenCallRealMethod();

        PowerMockito.when(mBleGattCharacteristic.getUuid()).thenReturn(mUuid);

        PowerMockito.when(mUuid.toString()).thenReturn("11111111-0000-4489-0000-ffa28cde82ab");

        assertEquals(UuidConstants.UuidBPS.CONN_PARAM, "11111111-0000-4489-0000-ffa28cde82ab");

        Whitebox.setInternalState(mBleConnectController, "mGattCallback", mGatCallBack);
        boolean ret = false;
        byte[] data = {0x00};
        PowerMockito.when(mGatCallBack.write(mBleGattCharacteristic, data)).thenReturn(ret);

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onCharacteristicNotifyEnabled(mBleGattCharacteristic);

    }

    @Test
    public void testOnCharacteristicNotifyEnabled_Case05() throws Exception {
        PowerMockito.when(mBleConnectController, "onCharacteristicNotifyEnabled", mBleGattCharacteristic).thenCallRealMethod();

        PowerMockito.when(mBleGattCharacteristic.getUuid()).thenReturn(mUuid);

        PowerMockito.when(mUuid.toString()).thenReturn("11111111-0000-4489-0000-ffa28cde82ab");

        assertEquals(UuidConstants.UuidBPS.CONN_PARAM, "11111111-0000-4489-0000-ffa28cde82ab");

        Whitebox.setInternalState(mBleConnectController, "mGattCallback", mGatCallBack);
        boolean ret = true;
        byte[] data = {0x00};
        PowerMockito.when(mGatCallBack.write(mBleGattCharacteristic, data)).thenReturn(ret);

        mBleConnectController.onCharacteristicNotifyEnabled(mBleGattCharacteristic);
    }

    @Test
    public void testOnCharacteristicNotifyEnabled_Case06() throws Exception {
        PowerMockito.when(mBleConnectController, "onCharacteristicNotifyEnabled", mBleGattCharacteristic).thenCallRealMethod();

        PowerMockito.when(mBleGattCharacteristic.getUuid()).thenReturn(mUuid);

        PowerMockito.when(mUuid.toString()).thenReturn("11111111-3333-4489-0000-ffa28cde82ab");

        assertEquals(UuidConstants.UuidATS.ASYNCH_INPUT_DATA, "11111111-3333-4489-0000-ffa28cde82ab");

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", CONNECTED);

        mBleConnectController.onCharacteristicNotifyEnabled(mBleGattCharacteristic);
    }

    @Test
    public void testOnCharacteristicNotifyEnableFailed_Case01() throws Exception {
        PowerMockito.when(mBleConnectController, "onCharacteristicNotifyEnableFailed", mBleGattCharacteristic, 1).thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mIsConnecting", true);

        mBleConnectController.onCharacteristicNotifyEnableFailed(mBleGattCharacteristic, 1);
    }

    @Test
    public void testOnCharacteristicNotifyEnableFailed_Case02() throws Exception {
        PowerMockito.when(mBleConnectController, "onCharacteristicNotifyEnableFailed", mBleGattCharacteristic, 1).thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mIsConnecting", false);

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onCharacteristicNotifyEnableFailed(mBleGattCharacteristic, 1);
    }

    @Test
    public void testOnBpsDataReceived_Case01() throws Exception {
        PowerMockito.when(mBleConnectController, "onBpsDataReceived", null, data).thenCallRealMethod();

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onBpsDataReceived(null, data);
    }

    @Test
    public void testOnBpsDataReceived_Case02() throws Exception {
        PowerMockito.when(mBleConnectController, "onBpsDataReceived", mBleGattCharacteristic, data).thenCallRealMethod();

        PowerMockito.when(mBleGattCharacteristic.getUuid()).thenReturn(mUuid);

        PowerMockito.when(mUuid.toString()).thenReturn("11111111-0000-4489-0000-ffa28cde82ab");

        assertEquals(UuidConstants.UuidBPS.CONN_PARAM, "11111111-0000-4489-0000-ffa28cde82ab");

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidBPS.ATT_MTU)).thenReturn(mBleGattCharacteristic);

        boolean ret = false;
        Whitebox.setInternalState(mBleConnectController, "mGattCallback", mGatCallBack);

        PowerMockito.when(mGatCallBack.enableCharacteristicNotification(mBleGattCharacteristic)).thenReturn(ret);

        assertFalse(ret);

        mBleConnectController.onBpsDataReceived(mBleGattCharacteristic, data);
    }

    @Test
    public void testOnBpsDataReceived_Case03() throws Exception {
        PowerMockito.when(mBleConnectController, "onBpsDataReceived", mBleGattCharacteristic, data).thenCallRealMethod();

        PowerMockito.when(mBleGattCharacteristic.getUuid()).thenReturn(mUuid);

        PowerMockito.when(mUuid.toString()).thenReturn("11111111-0000-4489-0000-ffa28cde82ab");

        assertEquals(UuidConstants.UuidBPS.CONN_PARAM, "11111111-0000-4489-0000-ffa28cde82ab");

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidBPS.ATT_MTU)).thenReturn(mBleGattCharacteristic);

        boolean ret = true;
        Whitebox.setInternalState(mBleConnectController, "mGattCallback", mGatCallBack);

        PowerMockito.when(mGatCallBack.enableCharacteristicNotification(mBleGattCharacteristic)).thenReturn(ret);

        assertTrue(ret);

        mBleConnectController.onBpsDataReceived(mBleGattCharacteristic, data);
    }

    @Test
    public void testOnBpsDataReceived_Case04() throws Exception {
        PowerMockito.when(mBleConnectController, "onBpsDataReceived", mBleGattCharacteristic, data).thenCallRealMethod();

        PowerMockito.when(mBleGattCharacteristic.getUuid()).thenReturn(mUuid);

        PowerMockito.when(mUuid.toString()).thenReturn("22222222-0000-4489-0000-ffa28cde82ab");

        assertEquals(UuidConstants.UuidBPS.ATT_MTU, "22222222-0000-4489-0000-ffa28cde82ab");

        Whitebox.setInternalState(mBleConnectController, "mAttSize", 5);
        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getDataMessageManager()).thenReturn(mDataMessageManager);
        PowerMockito.when(mDataMessageManager, "setupDataLength", 5).thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidATS.ASYNCH_INPUT_DATA)).thenReturn(mBleGattCharacteristic);

        boolean ret = true;
        Whitebox.setInternalState(mBleConnectController, "mGattCallback", mGatCallBack);

        PowerMockito.when(mGatCallBack.enableCharacteristicNotification(mBleGattCharacteristic)).thenReturn(ret);

        assertTrue(ret);

        mBleConnectController.onBpsDataReceived(mBleGattCharacteristic, data);
    }

    @Test
    public void testOnBpsDataReceived_Case05() throws Exception {
        byte[] data = {1, 0};
        PowerMockito.when(mBleConnectController, "onBpsDataReceived", mBleGattCharacteristic, data).thenCallRealMethod();

        PowerMockito.when(mBleGattCharacteristic.getUuid()).thenReturn(mUuid);

        PowerMockito.when(mUuid.toString()).thenReturn("22222222-0000-4489-0000-ffa28cde82ab");

        assertEquals(UuidConstants.UuidBPS.ATT_MTU, "22222222-0000-4489-0000-ffa28cde82ab");

        Whitebox.setInternalState(mBleConnectController, "mAttSize", 5);
        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getDataMessageManager()).thenReturn(mDataMessageManager);
        PowerMockito.when(mDataMessageManager, "setupDataLength", 5).thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidATS.ASYNCH_INPUT_DATA)).thenReturn(mBleGattCharacteristic);

        boolean ret = true;
        Whitebox.setInternalState(mBleConnectController, "mGattCallback", mGatCallBack);

        PowerMockito.when(mGatCallBack.enableCharacteristicNotification(mBleGattCharacteristic)).thenReturn(ret);

        assertTrue(ret);

        mBleConnectController.onBpsDataReceived(mBleGattCharacteristic, data);
    }

    @Test
    public void testOnConnected_Case01() throws Exception {
        BluetoothDevice mBluetoothDeviceCurrent = mock(BluetoothDevice.class);
        PowerMockito.when(mBleConnectController, "onConnected", mBluetoothGatt).thenCallRealMethod();

        PowerMockito.when(mBluetoothGatt.getDevice()).thenReturn(mBluetoothDevice);
        PowerMockito.when(mBluetoothDevice.getAddress()).thenReturn("192.168.1.1");
        PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(voidAnswer);

        PowerMockito.when(mBluetoothGatt.discoverServices()).thenReturn(false);
        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
        Whitebox.setInternalState(mBleConnectController, "mBluetoothDeviceConnect", mBluetoothDeviceCurrent);
        PowerMockito.when(mBluetoothDeviceCurrent.getName()).thenReturn("BTU00001");
        PowerMockito.when(mBleConnectController, "autoReConnect", "BTU00001").thenReturn(true);

        mBleConnectController.onConnected(mBluetoothGatt);

        verify(mBleConnectController, atLeastOnce()).onConnected(mBluetoothGatt);
    }

    @Test
    public void testOnConnected_Case02() throws Exception {
        PowerMockito.when(mBleConnectController, "onConnected", mBluetoothGatt).thenCallRealMethod();

        PowerMockito.when(mBluetoothGatt.getDevice()).thenReturn(mBluetoothDevice);
        PowerMockito.when(mBluetoothDevice.getAddress()).thenReturn("192.168.1.1");
        PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(voidAnswer);

        PowerMockito.when(mBluetoothGatt.discoverServices()).thenReturn(true);

        mBleConnectController.onConnected(mBluetoothGatt);
        verify(mBleConnectController, atLeastOnce()).onConnected(mBluetoothGatt);

    }

    @Test
    public void testOnDisconnected_Case01() throws Exception {
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        Whitebox.setInternalState(mBleConnectController, "mTimeManager", mTimeManager);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "setTransactionIdentity", 0).thenAnswer(voidAnswer);
        PowerMockito.when(mBleConnectController, "onDisconnected", mBluetoothGatt, 0x13).thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mTimeManager", mTimeManager);

        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        PowerMockito.when(mBluetoothPrefUtils.getString(KEY_BTU_NAME_REGISTER_SUCCESS)).thenReturn("BTU");
        PowerMockito.when(TextUtils.isEmpty("BTU")).thenReturn(true);

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onDisconnected(mBluetoothGatt, 0x13);

        verify(mBleConnectController, atLeastOnce()).onDisconnected(mBluetoothGatt, 0x13);
    }

    @Test
    public void testOnDisconnected_Case02() throws Exception {
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        Whitebox.setInternalState(mBleConnectController, "mTimeManager", mTimeManager);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "setTransactionIdentity", 0).thenAnswer(voidAnswer);
        PowerMockito.when(mBleConnectController, "onDisconnected", mBluetoothGatt, 0x08).thenCallRealMethod();
        Whitebox.setInternalState(mBleConnectController, "mTimeManager", mTimeManager);

        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        PowerMockito.when(mBluetoothPrefUtils.getString(KEY_BTU_NAME_REGISTER_SUCCESS)).thenReturn("BTU");
        PowerMockito.when(TextUtils.isEmpty("BTU")).thenReturn(true);

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onDisconnected(mBluetoothGatt, 0x08);

        verify(mBleConnectController, atLeastOnce()).onDisconnected(mBluetoothGatt, 0x08);
    }

    @Test
    public void testOnDisconnected_Case03() throws Exception {
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        Whitebox.setInternalState(mBleConnectController, "mTimeManager", mTimeManager);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "setTransactionIdentity", 0).thenAnswer(voidAnswer);
        PowerMockito.when(mBleConnectController, "onDisconnected", mBluetoothGatt, 0x09).thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mTimeManager", mTimeManager);
        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(false);
        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        PowerMockito.when(mBluetoothPrefUtils.getString(KEY_BTU_NAME_REGISTER_SUCCESS)).thenReturn("BTU");
        PowerMockito.when(TextUtils.isEmpty("BTU")).thenReturn(true);

        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onDisconnected(mBluetoothGatt, 0x09);

        verify(mBleConnectController, atLeastOnce()).onDisconnected(mBluetoothGatt, 0x09);
    }

    @Test
    public void testOnDisconnected_Case04() throws Exception {
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        Whitebox.setInternalState(mBleConnectController, "mTimeManager", mTimeManager);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "setTransactionIdentity", 0).thenAnswer(voidAnswer);
        PowerMockito.when(mBleConnectController, "onDisconnected", mBluetoothGatt, 0x0001).thenCallRealMethod();
        Whitebox.setInternalState(mBleConnectController, "mTimeManager", mTimeManager);
        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(true);

        Whitebox.setInternalState(mBleConnectController, "mBluetoothDeviceConnect", (Object) null);
        PowerMockito.doNothing().when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);

        mBleConnectController.onDisconnected(mBluetoothGatt, 0x0001);

        verify(mBleConnectController, atLeastOnce()).onDisconnected(mBluetoothGatt, 0x0001);
    }

    @Test
    public void testStopTimer() throws Exception {
        PowerMockito.when(mBleConnectController, "stopTimer").thenCallRealMethod();
        Whitebox.setInternalState(mBleConnectController, "mTimeManager", mTimeManager);
        PowerMockito.when(mTimeManager, "stopAllTimer").thenAnswer(voidAnswer);

        Whitebox.invokeMethod(mBleConnectController, "stopTimer");

        PowerMockito.verifyPrivate(mBleConnectController, atLeastOnce()).invoke("stopTimer");
    }

    @Test
    public void testNotifyConnectStatusChange_Case01() throws Exception {
        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED).thenCallRealMethod();

        PowerMockito.doNothing().when(mBleConnectController, "stopTimer");
        PowerMockito.when(mBleConnectController, "disconnectDevice").thenReturn(true);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.disconnectDevice()).thenReturn(true);

        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED).thenAnswer(voidAnswer);

        verify(mBleConnectController, atLeastOnce()).notifyConnectStatusChange(DISCONNECTED);
    }

    @Test
    public void testNotifyConnectStatusChange_Case02() throws Exception {
        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED).thenCallRealMethod();

        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.doNothing().when(mBleConnectController, "stopTimer");
        PowerMockito.when(mBleConnectController, "disconnectDevice").thenReturn(true);
        PowerMockito.when(mBluetoothManager.disconnectDevice()).thenReturn(true);
        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(false);
        Whitebox.setInternalState(mBleConnectController, "mListeners", mListeners);

        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED).thenAnswer(voidAnswer);

        verify(mBleConnectController, atLeastOnce()).notifyConnectStatusChange(DISCONNECTED);
    }

    @Test
    public void testNotifyConnectStatusChange_Case03() throws Exception {
        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", CONNECTED).thenCallRealMethod();

        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(false);
        Whitebox.setInternalState(mBleConnectController, "mListeners", mListeners);
        PowerMockito.when(if_vehicleConnectListener, "onConnectStateNotice", CONNECTED).thenAnswer(voidAnswer);

        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", CONNECTED).thenAnswer(voidAnswer);

        verify(mBleConnectController, atLeastOnce()).notifyConnectStatusChange(CONNECTED);

    }

    @Test
    public void testNotifyConnectStatusChange_Case04() throws Exception {
        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", CONNECTED).thenCallRealMethod();

        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(false);
        Whitebox.setInternalState(mBleConnectController, "mListeners", mListeners);

        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", CONNECTED).thenAnswer(voidAnswer);

        verify(mBleConnectController, atLeastOnce()).notifyConnectStatusChange(CONNECTED);

    }

    @Test
    public void testNotifyConnectStatusChange_Case05() throws Exception {
        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", DISCONNECTED).thenCallRealMethod();

        PowerMockito.doNothing().when(mBleConnectController, "stopTimer");
        PowerMockito.when(mBleConnectController, "disconnectDevice").thenReturn(true);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.disconnectDevice()).thenReturn(true);
        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(false);
        Whitebox.setInternalState(mBleConnectController, "mListeners", mListeners);

        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", DISCONNECTED).thenAnswer(voidAnswer);

        verify(mBleConnectController, atLeastOnce()).notifyConnectStatusChange(DISCONNECTED);

    }

    @Test
    public void testOnTimeout_Case01() throws Exception {
        PowerMockito.when(mBleConnectController, "onTimeout").thenCallRealMethod();

        PowerMockito.when(mBleConnectController, "isConnecting").thenReturn(false);
        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED).thenAnswer(voidAnswer);
        mBleConnectController.onTimeout();

        verify(mBleConnectController, atLeastOnce()).onTimeout();
    }

    @Test
    public void testOnTimeout_Case02() throws Exception {
        PowerMockito.when(mBleConnectController, "onTimeout").thenCallRealMethod();

        PowerMockito.when(mBleConnectController, "isConnecting").thenReturn(true);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.stopScan()).thenReturn(true);
        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED).thenAnswer(voidAnswer);

        mBleConnectController.onTimeout();

        verify(mBleConnectController, atLeastOnce()).onTimeout();
    }

    @Test
    public void testCreateServiceCharacteristicList_Case01() throws Exception {
        PowerMockito.when(mBleConnectController, "createServiceCharacteristicList", mListBluetoothGattService).thenCallRealMethod();

        PowerMockito.when(mBleConnectController, "removeServiceCharacteristicList").thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleConnectController, "mServices", mServices);
        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);

        when(mListBluetoothGattService.size()).thenReturn(2);

        Iterator iterator = mock(Iterator.class);
        when(mListBluetoothGattService.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(mBluetoothGattService);

        PowerMockito.when(mBluetoothGattService.getUuid()).thenReturn(mUuid);

        PowerMockito.when(mServices.put(mUuid.toString(), mBluetoothGattService)).thenReturn(mBluetoothGattService);

        PowerMockito.when(mBluetoothGattService.getCharacteristics()).thenReturn(mListBluetoothGattCharacteristic);

        when(mListBluetoothGattCharacteristic.size()).thenReturn(5);

        Iterator ite = mock(Iterator.class);
        when(mListBluetoothGattCharacteristic.iterator()).thenReturn(ite);
        when(ite.hasNext()).thenReturn(true, false);
        when(ite.next()).thenReturn(mBleGattCharacteristic);

        when(mBleGattCharacteristic.getUuid()).thenReturn(mUuid);

        PowerMockito.when(mCharacteristics.put(mUuid.toString(), mBleGattCharacteristic)).thenAnswer(voidAnswer);

        Whitebox.invokeMethod(mBleConnectController, "createServiceCharacteristicList", mListBluetoothGattService);

        PowerMockito.verifyPrivate(mBleConnectController, atLeastOnce()).invoke("createServiceCharacteristicList", mListBluetoothGattService);
    }

    @Test
    public void testCreateServiceCharacteristicList_Case02() throws Exception {
        PowerMockito.when(mBleConnectController, "createServiceCharacteristicList", mListBluetoothGattService).thenCallRealMethod();

        PowerMockito.when(mBleConnectController, "removeServiceCharacteristicList").thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleConnectController, "mServices", mServices);
        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);

        when(mListBluetoothGattService.size()).thenReturn(2);

        Iterator iterator = mock(Iterator.class);
        when(mListBluetoothGattService.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(mBluetoothGattService);

        PowerMockito.when(mBluetoothGattService.getUuid()).thenReturn(mUuid);

        PowerMockito.when(mServices.put(mUuid.toString(), mBluetoothGattService)).thenReturn(mBluetoothGattService);

        PowerMockito.when(mBluetoothGattService.getCharacteristics()).thenReturn(mListBluetoothGattCharacteristic);

        when(mListBluetoothGattCharacteristic.size()).thenReturn(0);

        Iterator ite = mock(Iterator.class);
        when(mListBluetoothGattCharacteristic.iterator()).thenReturn(ite);
        when(ite.hasNext()).thenReturn(true, false);
        when(ite.next()).thenReturn(mBleGattCharacteristic);

        when(mBleGattCharacteristic.getUuid()).thenReturn(mUuid);

        PowerMockito.when(mCharacteristics.put(mUuid.toString(), mBleGattCharacteristic)).thenAnswer(voidAnswer);

        Whitebox.invokeMethod(mBleConnectController, "createServiceCharacteristicList", mListBluetoothGattService);

        PowerMockito.verifyPrivate(mBleConnectController, atLeastOnce()).invoke("createServiceCharacteristicList", mListBluetoothGattService);
    }

    @Test
    public void testRemoveServiceCharacteristicList() throws Exception {
        PowerMockito.when(mBleConnectController, "removeServiceCharacteristicList").thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mServices", mServices);
        PowerMockito.when(mServices, "clear").thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);
        PowerMockito.when(mCharacteristics, "clear").thenAnswer(voidAnswer);

        Whitebox.invokeMethod(mBleConnectController, "removeServiceCharacteristicList");

        PowerMockito.verifyPrivate(mBleConnectController, atLeastOnce()).invoke("removeServiceCharacteristicList");
    }

    @Test
    public void testIsAtsSupport_Case01() throws Exception {
        PowerMockito.when(mBleConnectController, "isAtsSupport").thenCallRealMethod();
        Whitebox.setInternalState(mBleConnectController, "mServices", (Object) null);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "isAtsSupport");

        assertFalse(actual);
    }

    @Test
    public void testIsAtsSupport_Case02() throws Exception {
        PowerMockito.when(mBleConnectController, "isAtsSupport").thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mServices", mServices);
        when(mServices.size()).thenReturn(1);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", (Object) null);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "isAtsSupport");

        assertFalse(actual);
    }

    @Test
    public void testIsAtsSupport_Case03() throws Exception {
        PowerMockito.when(mBleConnectController, "isAtsSupport").thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mServices", mServices);
        when(mServices.size()).thenReturn(1);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);
        when(mCharacteristics.size()).thenReturn(1);
        PowerMockito.when(mServices.get(UuidConstants.UuidService.ASYNCHRONOUS_TRANSFER_SERVICE)).thenReturn(null);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "isAtsSupport");

        assertFalse(actual);
    }

    @Test
    public void testIsAtsSupport_Case04() throws Exception {
        PowerMockito.when(mBleConnectController, "isAtsSupport").thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mServices", mServices);
        when(mServices.size()).thenReturn(1);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);
        when(mCharacteristics.size()).thenReturn(1);
        PowerMockito.when(mServices.get(UuidConstants.UuidService.ASYNCHRONOUS_TRANSFER_SERVICE)).thenReturn(mBluetoothGattService);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidATS.ASYNCH_INPUT_DATA)).thenReturn(null);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "isAtsSupport");

        assertFalse(actual);
    }

    @Test
    public void testIsAtsSupport_Case05() throws Exception {
        PowerMockito.when(mBleConnectController, "isAtsSupport").thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mServices", mServices);
        when(mServices.size()).thenReturn(1);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);
        when(mCharacteristics.size()).thenReturn(1);
        PowerMockito.when(mServices.get(UuidConstants.UuidService.ASYNCHRONOUS_TRANSFER_SERVICE)).thenReturn(mBluetoothGattService);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidATS.ASYNCH_INPUT_DATA)).thenReturn(mBleGattCharacteristic);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidATS.ASYNCH_OUTPUT_DATA)).thenReturn(null);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "isAtsSupport");

        assertFalse(actual);
    }

    @Test
    public void testIsAtsSupport_Case06() throws Exception {
        PowerMockito.when(mBleConnectController, "isAtsSupport").thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mServices", mServices);
        when(mServices.size()).thenReturn(1);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);
        when(mCharacteristics.size()).thenReturn(1);
        PowerMockito.when(mServices.get(UuidConstants.UuidService.ASYNCHRONOUS_TRANSFER_SERVICE)).thenReturn(mBluetoothGattService);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidATS.ASYNCH_INPUT_DATA)).thenReturn(mBleGattCharacteristic);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidATS.ASYNCH_OUTPUT_DATA)).thenReturn(mBleGattCharacteristic);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "isAtsSupport");

        assertTrue(actual);
    }

    @Test
    public void testIsBpsSupport_Case01() throws Exception {
        PowerMockito.when(mBleConnectController, "isBpsServiceSupport").thenCallRealMethod();
        Whitebox.setInternalState(mBleConnectController, "mServices", (Object) null);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "isBpsServiceSupport");

        assertFalse(actual);
    }

    @Test
    public void testIsBpsSupport_Case02() throws Exception {
        PowerMockito.when(mBleConnectController, "isBpsServiceSupport").thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mServices", mServices);
        when(mServices.size()).thenReturn(1);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", (Object) null);

        PowerMockito.when(mBleConnectController, "isBpsServiceSupport").thenAnswer(voidAnswer);
    }

    @Test
    public void testIsBpsSupport_Case03() throws Exception {
        PowerMockito.when(mBleConnectController, "isBpsServiceSupport").thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mServices", mServices);
        when(mServices.size()).thenReturn(1);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);
        when(mCharacteristics.size()).thenReturn(1);
        PowerMockito.when(mServices.get(UuidConstants.UuidService.BLE_PARAMETER_SERVICE)).thenReturn(null);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "isBpsServiceSupport");

        assertFalse(actual);
    }

    @Test
    public void testIsBpsSupport_Case04() throws Exception {
        PowerMockito.when(mBleConnectController, "isBpsServiceSupport").thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mServices", mServices);
        when(mServices.size()).thenReturn(1);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);
        when(mCharacteristics.size()).thenReturn(1);
        PowerMockito.when(mServices.get(UuidConstants.UuidService.BLE_PARAMETER_SERVICE)).thenReturn(mBluetoothGattService);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidBPS.ATT_MTU)).thenReturn(null);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "isBpsServiceSupport");

        assertFalse(actual);
    }

    @Test
    public void testIsBpsSupport_Case05() throws Exception {
        PowerMockito.when(mBleConnectController, "isBpsServiceSupport").thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mServices", mServices);
        when(mServices.size()).thenReturn(1);

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);
        when(mCharacteristics.size()).thenReturn(1);
        PowerMockito.when(mServices.get(UuidConstants.UuidService.BLE_PARAMETER_SERVICE)).thenReturn(mBluetoothGattService);

        PowerMockito.when(mCharacteristics.get(UuidConstants.UuidBPS.ATT_MTU)).thenReturn(mBleGattCharacteristic);

        boolean actual = Whitebox.invokeMethod(mBleConnectController, "isBpsServiceSupport");

        assertTrue(actual);
    }

    @Test
    public void testGetCharacteristic_Case01() throws Exception {
        PowerMockito.when(mBleConnectController, "getCharacteristic", anyString()).thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mCharacteristics", mCharacteristics);
        BluetoothGattCharacteristic actual = mBleConnectController.getCharacteristic(anyString());

        assertEquals(any(BluetoothGattCharacteristic.class), actual);
    }

    @Test
    public void testGetCharacteristic_Case02() throws Exception {
        PowerMockito.when(mBleConnectController, "getCharacteristic", anyString()).thenCallRealMethod();

        BluetoothGattCharacteristic actual = mBleConnectController.getCharacteristic(anyString());

        assertNull(actual);
    }


    @Test
    public void testGetBluetoothDeviceConnect() throws Exception {
        PowerMockito.when(mBleConnectController, "getBluetoothDeviceConnect").thenCallRealMethod();

        Whitebox.setInternalState(mBleConnectController, "mBluetoothDeviceConnect", mBluetoothDevice);
        BluetoothDevice actual = mBleConnectController.getBluetoothDeviceConnect();

        assertEquals(mBluetoothDevice, actual);
    }

    @Test
    public void testOnScanDeviceResult_Case01() throws Exception {
        PowerMockito.doCallRealMethod().when(mBleConnectController).onScanDeviceResult(mBluetoothScan);

        PowerMockito.when(mBluetoothScan.getDevice()).thenReturn(mBluetoothDevice);

        PowerMockito.when(TextUtils.isEmpty(mBluetoothDevice.getName())).thenReturn(true);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        PowerMockito.when(mBluetoothPrefUtils.getBool(KEY_REGISTER_SUCCESS)).thenReturn(false);

        PowerMockito.when(mBluetoothManager, "stopScan").thenAnswer(voidAnswer);

        PowerMockito.when(mBleConnectController, "connectDevice", mBluetoothDevice).thenAnswer(voidAnswer);

        mBleConnectController.onScanDeviceResult(mBluetoothScan);
        verify(mBleConnectController, atLeastOnce()).onScanDeviceResult(mBluetoothScan);
    }

    @Test
    public void testOnScanDeviceResult_Case02() throws Exception {
        PowerMockito.doCallRealMethod().when(mBleConnectController).onScanDeviceResult(mBluetoothScan);

        PowerMockito.when(mBluetoothScan.getDevice()).thenReturn(mBluetoothDevice);

        PowerMockito.when(TextUtils.isEmpty(mBluetoothDevice.getName())).thenReturn(false);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        PowerMockito.when(mBluetoothPrefUtils.getBool(KEY_REGISTER_SUCCESS)).thenReturn(false);

        PowerMockito.when(mBluetoothManager, "stopScan").thenAnswer(voidAnswer);

        PowerMockito.when(mBleConnectController, "connectDevice", mBluetoothDevice).thenAnswer(voidAnswer);

        mBleConnectController.onScanDeviceResult(mBluetoothScan);
        verify(mBleConnectController, atLeastOnce()).onScanDeviceResult(mBluetoothScan);
    }


    @Test
    public void testOnScanDeviceResult_Case03() throws Exception {
        PowerMockito.doCallRealMethod().when(mBleConnectController).onScanDeviceResult(mBluetoothScan);

        PowerMockito.when(mBluetoothScan.getDevice()).thenReturn(null);
        mBleConnectController.onScanDeviceResult(mBluetoothScan);
    }

}
