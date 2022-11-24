package com.honda.connmc.Bluetooth.scan;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.text.TextUtils;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.connect.BleConnectController;
import com.honda.connmc.Bluetooth.timer.TimeManager;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Interfaces.listener.scan.IF_VehicleScanListener;
import com.honda.connmc.Manager.LocationManager;
import com.honda.connmc.Model.BluetoothScan;
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

import java.util.List;
import java.util.UUID;

import static com.honda.connmc.Constants.UuidConstants.UuidService.BLE_PARAMETER_SERVICE;
import static com.honda.connmc.Constants.UuidConstants.UuidService.LOW_POWER_SLEEP_SERVICE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BleScanController.class,
        IF_VehicleScanListener.class,
        BluetoothLeScanner.class,
        BluetoothAdapter.class,
        TimeManager.class,
        BluetoothScan.class,
        ScanCallback.class,
        IntentFilter.class,
        BleConnectController.class,
        BluetoothDevice.class,
        ScanRecord.class,
        BluetoothManager.class,
        ScanResult.class,
        BluetoothUtils.class,
        LogUtils.class,
        LocationManager.class,
        BroadcastReceiver.class,
        UUID.class,
        TextUtils.class,
        Object.class,
        ScanRecord.class,
        Context.class
})
public class BleScanControllerTest {
    @Mock
    private BluetoothAdapter mBluetoothAdapter;

    @Mock
    private BluetoothLeScanner mBluetoothLeScanner;

    @Mock
    private TimeManager mTimeManager;

    @Mock
    private IF_VehicleScanListener If_vehicleScanListener;

    @Mock
    private ScanSettings.Builder mMockBuilder;

    @Mock
    private ScanSettings mScanSettings;

    @Mock
    private BleScanController bleScanController;

    @Mock
    private BluetoothManager mBluetoothManager;

    @Mock
    private Context mContext;

    @Mock
    private ScanFilter mMockScanFilter;

    @Mock
    private BleConnectController mBleConnectController;

    @Mock
    private ScanFilter.Builder scanFilterBuilder;

    @Mock
    private ScanResult mScanResult;

    @Mock
    private ScanCallback mScanCallBack;

    @Mock
    private BluetoothDevice mBluetoothDevice;

    @Mock
    private ScanRecord mScanRecord;

    @Mock
    private BluetoothScan mBluetoothScan;

    @Mock
    private Object mObject;

    @Mock
    private BroadcastReceiver mBluetoothReceiver;
    @Mock
    private BluetoothUtils mBluetoothUtils;

    @Mock
    private UUID uuid;

    @Mock
    private List<ParcelUuid> listParcel_Uuids;

    @Mock
    private List<BluetoothScan> mListDevices;
    @Mock

    private Intent mIntent;

    @Mock
    private ParcelUuid parcel_Uuids;

    Answer voidAnswer = invocation -> null;

    public BleScanControllerTest() {
    }


    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(LocationManager.class);
        PowerMockito.mockStatic(BluetoothAdapter.class);
        PowerMockito.mockStatic(LogUtils.class);
        PowerMockito.mockStatic(BluetoothLeScanner.class);
        PowerMockito.mockStatic(ScanFilter.class);
        PowerMockito.mockStatic(ScanSettings.class);
        PowerMockito.mockStatic(ScanResult.class);
        PowerMockito.mockStatic(BleScanController.class);
        PowerMockito.mockStatic(BluetoothManager.class);
        PowerMockito.mockStatic(BluetoothUtils.class);
        PowerMockito.mockStatic(ScanFilter.Builder.class);
        PowerMockito.mockStatic(BluetoothDevice.class);
        PowerMockito.mockStatic(ScanCallback.class);
        PowerMockito.mockStatic(UUID.class);
        PowerMockito.mockStatic(BroadcastReceiver.class);
        PowerMockito.mockStatic(Object.class);

        mContext = mock(Context.class);
        Whitebox.setInternalState(bleScanController, "LOCK_OBJECT", mObject);

        PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "e", anyString()).thenAnswer(voidAnswer);
    }

    @Test
    public void testNotNull() {
        assertNotNull(mBluetoothAdapter);
        assertNotNull(mBluetoothLeScanner);
    }

    @Test
    public void testStartScanOneParams_Case01() {
        when(bleScanController.startScan(mContext)).thenCallRealMethod();

        Whitebox.setInternalState(bleScanController, "mIsScanning", true);

        boolean actual = bleScanController.startScan(mContext);

        assertFalse(actual);
    }

    @Test
    public void testStartScanOneParams_Case02() throws Exception {
        when(bleScanController.startScan(mContext)).thenCallRealMethod();

        Whitebox.setInternalState(bleScanController, "mIsScanning", false);
        Whitebox.setInternalState(bleScanController, "mCount", 1);
        PowerMockito.when(bleScanController, "startScan", mContext, (Object) null).thenReturn(true);

        boolean actual = bleScanController.startScan(mContext);

        assertTrue(actual);
    }

    @Test
    public void testSetVehicleScanListener() throws Exception {
        PowerMockito.when(bleScanController, "setVehicleScanListener", If_vehicleScanListener).thenCallRealMethod();

        bleScanController.setVehicleScanListener(If_vehicleScanListener);

        IF_VehicleScanListener actual = Whitebox.getInternalState(bleScanController, "mVehicleScanListener");

        assertEquals(If_vehicleScanListener, actual);
    }

    @Test
    public void testStartScan_Case01() throws Exception {
        when(bleScanController.startScan(mContext, "")).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        when(LocationManager.isLocationEnabled(mContext)).thenReturn(true);
        Whitebox.setInternalState(bleScanController, "mIsScanning", true);

        boolean actual = bleScanController.startScan(mContext, "");
        assertFalse(actual);
    }

    @Test
    public void testStartScan_Case02() throws Exception {
        when(bleScanController.startScan(mContext, "")).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        when(BluetoothUtils.checkBluetooth()).thenReturn(false);

        boolean actual = bleScanController.startScan(mContext, "");
        assertFalse(actual);
    }

    @Test
    public void testStartScan_Case03() throws Exception {
        String device = "BTU0001";
        when(bleScanController.startScan(mContext, device)).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "mIsScanning", false);
        when(BluetoothUtils.checkBluetooth()).thenReturn(true);

        PowerMockito.when(BluetoothAdapter.getDefaultAdapter()).thenReturn(mBluetoothAdapter);
        assertNotNull(mBluetoothAdapter);

        when(mBluetoothAdapter.isDiscovering()).thenReturn(true);

        PowerMockito.when(mBluetoothAdapter.cancelDiscovery()).thenReturn(true);

        PowerMockito.when(mBluetoothAdapter.getBluetoothLeScanner()).thenReturn(null);

        boolean actual = bleScanController.startScan(mContext, device);
        assertFalse(actual);
    }

    @Test
    public void testStartScan_Case04() throws Exception {
        when(bleScanController.startScan(mContext, "")).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "mIsScanning", false);
        when(BluetoothUtils.checkBluetooth()).thenReturn(true);

        PowerMockito.when(BluetoothAdapter.getDefaultAdapter()).thenReturn(mBluetoothAdapter);
        assertNotNull(mBluetoothAdapter);

        when(mBluetoothAdapter.isDiscovering()).thenReturn(true);

        when(mBluetoothAdapter.cancelDiscovery()).thenReturn(true);

        PowerMockito.when(mBluetoothAdapter.getBluetoothLeScanner()).thenReturn(mBluetoothLeScanner);

        PowerMockito.when(LocationManager.isLocationEnabled(mContext)).thenReturn(true);
        Whitebox.setInternalState(bleScanController, "mListDevices", mListDevices);
        PowerMockito.when(mListDevices, "clear").thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "isLocationEnabled", true);
        PowerMockito.when(bleScanController, "stopScan").thenAnswer(voidAnswer);

        List<ScanFilter> filters = mock(List.class);
        PowerMockito.when(TextUtils.isEmpty("")).thenReturn(false);
        PowerMockito.whenNew(ScanFilter.Builder.class).withNoArguments().thenReturn(scanFilterBuilder);
        when(scanFilterBuilder.setDeviceName("")).thenReturn(scanFilterBuilder);
        when(scanFilterBuilder.build()).thenReturn(mMockScanFilter);
        when(filters.add(mMockScanFilter)).thenReturn(true);

        PowerMockito.whenNew(ScanSettings.Builder.class).withAnyArguments().thenReturn(mMockBuilder);
        PowerMockito.when(mMockBuilder, "setScanMode", ScanSettings.SCAN_MODE_LOW_LATENCY).thenAnswer(voidAnswer);
        PowerMockito.when(mMockBuilder.build()).thenReturn(mScanSettings);
        PowerMockito.when(mBluetoothLeScanner, "startScan", filters, mScanSettings, mScanCallBack).thenAnswer(voidAnswer);


        boolean actual = bleScanController.startScan(mContext, "");
        assertTrue(actual);
    }

    @Test
    public void testStartScan_Case05() throws Exception {
        when(bleScanController.startScan(mContext, "BTUDD0F5")).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "mIsScanning", false);
        when(BluetoothUtils.checkBluetooth()).thenReturn(true);

        PowerMockito.when(BluetoothAdapter.getDefaultAdapter()).thenReturn(mBluetoothAdapter);
        assertNotNull(mBluetoothAdapter);

        when(mBluetoothAdapter.isDiscovering()).thenReturn(true);

        when(mBluetoothAdapter.cancelDiscovery()).thenReturn(true);

        PowerMockito.when(mBluetoothAdapter.getBluetoothLeScanner()).thenReturn(mBluetoothLeScanner);

        PowerMockito.when(LocationManager.isLocationEnabled(mContext)).thenReturn(true);
        Whitebox.setInternalState(bleScanController, "mListDevices", mListDevices);
        PowerMockito.when(mListDevices, "clear").thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "isLocationEnabled", true);
        PowerMockito.when(bleScanController, "stopScan").thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("BTUDD0F5")).thenReturn(true);

        List<ScanFilter> filters = mock(List.class);
        PowerMockito.whenNew(ScanSettings.Builder.class).withAnyArguments().thenReturn(mMockBuilder);
        PowerMockito.when(mMockBuilder, "setScanMode", ScanSettings.SCAN_MODE_LOW_LATENCY).thenAnswer(voidAnswer);
        PowerMockito.when(mMockBuilder.build()).thenReturn(mScanSettings);
        PowerMockito.when(mBluetoothLeScanner, "startScan", filters, mScanSettings, mScanCallBack).thenAnswer(voidAnswer);

        boolean actual = bleScanController.startScan(mContext, "BTUDD0F5");
        assertTrue(actual);
    }

    @Test
    public void testStartScan_Case06() throws Exception {
        when(bleScanController.startScan(mContext, "BTUDD0F5")).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "mIsScanning", false);
        when(BluetoothUtils.checkBluetooth()).thenReturn(true);

        PowerMockito.when(BluetoothAdapter.getDefaultAdapter()).thenReturn(mBluetoothAdapter);
        assertNotNull(mBluetoothAdapter);

        when(mBluetoothAdapter.isDiscovering()).thenReturn(true);

        when(mBluetoothAdapter.cancelDiscovery()).thenReturn(true);
        PowerMockito.when(mBluetoothAdapter.getBluetoothLeScanner()).thenReturn(mBluetoothLeScanner);

        PowerMockito.when(LocationManager.isLocationEnabled(mContext)).thenReturn(false);
        Whitebox.setInternalState(bleScanController, "mListDevices", mListDevices);
        PowerMockito.when(mListDevices, "clear").thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "isLocationEnabled", true);
        PowerMockito.when(bleScanController, "stopScan").thenAnswer(voidAnswer);
        IntentFilter filter = mock(IntentFilter.class);
        PowerMockito.when(bleScanController, "stopScan").thenAnswer(voidAnswer);

        PowerMockito.whenNew(IntentFilter.class).withArguments(BluetoothDevice.ACTION_FOUND).thenReturn(filter);
        PowerMockito.when(filter, "addAction", BluetoothAdapter.ACTION_DISCOVERY_FINISHED).thenAnswer(voidAnswer);
        PowerMockito.when(mContext.registerReceiver(mBluetoothReceiver, filter)).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothAdapter.startDiscovery()).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothAdapter.getDefaultAdapter()).thenReturn(mBluetoothAdapter);
        PowerMockito.when(mBluetoothAdapter.startDiscovery()).thenReturn(true);

        Whitebox.setInternalState(bleScanController, "mTimeManager", mTimeManager);

        boolean actual = bleScanController.startScan(mContext, "BTUDD0F5");
        assertTrue(actual);
    }

    @Test
    public void testStopScan_Case01() throws Exception {
        when(bleScanController.stopScan()).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "mIsScanning", true);
        Whitebox.setInternalState(bleScanController, "mTimeManager", mTimeManager);
        PowerMockito.when(mTimeManager, "stopTimeSchedule").thenAnswer(voidAnswer);
        when(BluetoothAdapter.getDefaultAdapter()).thenReturn(mBluetoothAdapter);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
//        PowerMockito.when(mBleConnectController.isIsConnecting()).thenReturn(true);
        PowerMockito.when(mBleConnectController, "disconnectDevice").thenAnswer(voidAnswer);
        when(mBluetoothAdapter.isDiscovering()).thenReturn(true);
        ContextWrapper contextWrapper = mock(ContextWrapper.class);
        Whitebox.setInternalState(bleScanController, "mBluetoothReceiver", mBluetoothReceiver);
        Whitebox.setInternalState(bleScanController, "mContext", mContext);
        PowerMockito.when(contextWrapper, "unregisterReceiver", mBluetoothReceiver).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothAdapter.cancelDiscovery()).thenReturn(true);

        when(mBluetoothAdapter.getBluetoothLeScanner()).thenReturn(mBluetoothLeScanner);
        PowerMockito.when(mBluetoothLeScanner, "stopScan", mScanCallBack).thenAnswer(voidAnswer);

        boolean actual = bleScanController.stopScan();

        assertTrue(actual);
    }

    @Test
    public void testStopScan_Case02() throws Exception {
        when(bleScanController.stopScan()).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "mIsScanning", true);
        Whitebox.setInternalState(bleScanController, "mTimeManager", mTimeManager);
        PowerMockito.when(mTimeManager, "stopTimeSchedule").thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
        PowerMockito.when(mBleConnectController.isConnecting()).thenReturn(false);
        when(BluetoothAdapter.getDefaultAdapter()).thenReturn(null);


        when(BluetoothAdapter.getDefaultAdapter()).thenReturn(mBluetoothAdapter);
        when(mBluetoothAdapter.getBluetoothLeScanner()).thenReturn(mBluetoothLeScanner);
        PowerMockito.when(mBluetoothLeScanner, "stopScan", mScanCallBack).thenAnswer(voidAnswer);

        boolean actual = bleScanController.stopScan();

        assertTrue(actual);
    }

    @Test
    public void testStopScan_Case03() throws Exception {
        when(bleScanController.stopScan()).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "mIsScanning", true);
        Whitebox.setInternalState(bleScanController, "mTimeManager", mTimeManager);
        PowerMockito.when(mTimeManager, "stopTimeSchedule").thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
        PowerMockito.when(mBleConnectController.isConnecting()).thenReturn(false);

        when(BluetoothAdapter.getDefaultAdapter()).thenReturn(mBluetoothAdapter);
        when(mBluetoothAdapter.startDiscovery()).thenReturn(false);

        when(mBluetoothAdapter.getBluetoothLeScanner()).thenReturn(mBluetoothLeScanner);
        PowerMockito.when(mBluetoothLeScanner, "stopScan", mScanCallBack).thenAnswer(voidAnswer);

        boolean actual = bleScanController.stopScan();

        assertTrue(actual);
    }

    @Test
    public void testStopScan_Case04() throws Exception {
        when(bleScanController.stopScan()).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "mIsScanning", false);
        Whitebox.setInternalState(bleScanController, "mTimeManager", mTimeManager);
        PowerMockito.when(mTimeManager, "stopTimeSchedule").thenAnswer(voidAnswer);

        boolean actual = bleScanController.stopScan();

        assertFalse(actual);
    }

    @Test
    public void testStopScan_Case05() throws Exception {
        when(bleScanController.stopScan()).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "mIsScanning", false);

        boolean actual = bleScanController.stopScan();

        assertFalse(actual);
    }

    @Test
    public void testOnScanResult_Case01() throws Exception {
        PowerMockito.when(bleScanController, "onScanResult", ScanSettings.CALLBACK_TYPE_ALL_MATCHES, null).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        bleScanController.onScanResult(ScanSettings.CALLBACK_TYPE_ALL_MATCHES, null);

        verify(bleScanController, atLeastOnce()).onScanResult(ScanSettings.CALLBACK_TYPE_ALL_MATCHES, null);

    }

    @Test
    public void testOnScanResult_Case02() throws Exception {
        doCallRealMethod().when(bleScanController).onScanResult(ScanSettings.CALLBACK_TYPE_ALL_MATCHES, mScanResult);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(mScanResult.getDevice()).thenReturn(mBluetoothDevice);
        PowerMockito.when(mScanResult.getScanRecord()).thenReturn(mScanRecord);
        PowerMockito.when(bleScanController, "processScanResult", mBluetoothDevice, mScanRecord).thenAnswer(voidAnswer);

        bleScanController.onScanResult(ScanSettings.CALLBACK_TYPE_ALL_MATCHES, mScanResult);

        verify(bleScanController, atLeastOnce()).onScanResult(ScanSettings.CALLBACK_TYPE_ALL_MATCHES, mScanResult);
    }

    @Test
    public void testProcessScanResult_Case01() throws Exception {
        PowerMockito.when(bleScanController, "processScanResult", mBluetoothDevice, mScanRecord).thenCallRealMethod();
        Whitebox.setInternalState(bleScanController, "mIsScanning", false);

        Whitebox.invokeMethod(bleScanController, "processScanResult", mBluetoothDevice, mScanRecord);

        PowerMockito.verifyPrivate(bleScanController, times(1)).invoke("processScanResult", mBluetoothDevice, mScanRecord);
    }

    @Test
    public void testProcessScanResult_Case02() throws Exception {
        PowerMockito.when(bleScanController, "processScanResult", mBluetoothDevice, mScanRecord).thenCallRealMethod();
        Whitebox.setInternalState(bleScanController, "mIsScanning", true);

        Whitebox.invokeMethod(bleScanController, "processScanResult", mBluetoothDevice, mScanRecord);

        PowerMockito.verifyPrivate(bleScanController, times(1)).invoke("processScanResult", mBluetoothDevice, mScanRecord);
    }

    @Test
    public void testProcessScanResult_Case03() throws Exception {
        PowerMockito.when(bleScanController, "processScanResult", (Object) null, mScanRecord).thenCallRealMethod();
        Whitebox.setInternalState(bleScanController, "mIsScanning", true);
        Whitebox.setInternalState(bleScanController, "mVehicleScanListener", If_vehicleScanListener);

        Whitebox.invokeMethod(bleScanController, "processScanResult", (Object) null, mScanRecord);

        PowerMockito.verifyPrivate(bleScanController, times(1)).invoke("processScanResult", (Object) null, mScanRecord);
    }

    @Test
    public void testProcessScanResult_Case04() throws Exception {
        PowerMockito.when(bleScanController, "processScanResult", mBluetoothDevice, mScanRecord).thenCallRealMethod();
        Whitebox.setInternalState(bleScanController, "mIsScanning", true);
        Whitebox.setInternalState(bleScanController, "mVehicleScanListener", If_vehicleScanListener);
        PowerMockito.when(TextUtils.isEmpty(mBluetoothDevice.getName())).thenReturn(true);

        Whitebox.invokeMethod(bleScanController, "processScanResult", mBluetoothDevice, mScanRecord);

        PowerMockito.verifyPrivate(bleScanController, times(1)).invoke("processScanResult", mBluetoothDevice, mScanRecord);
    }

    @Test
    public void testProcessScanResult_Case05() throws Exception {
        PowerMockito.when(bleScanController, "processScanResult", mBluetoothDevice, mScanRecord).thenCallRealMethod();
        Whitebox.setInternalState(bleScanController, "mIsScanning", true);
        Whitebox.setInternalState(bleScanController, "mVehicleScanListener", If_vehicleScanListener);
        PowerMockito.when(TextUtils.isEmpty(mBluetoothDevice.getName())).thenReturn(false);
        List<ParcelUuid> parcelUuidList = mock(List.class);
        BleScanController.UuidMode uuidMode = BleScanController.UuidMode.MODE_OTHER;
        PowerMockito.when(mScanRecord.getServiceUuids()).thenReturn(parcelUuidList);
        PowerMockito.when(parcelUuidList.size()).thenReturn(1);
        PowerMockito.when(parcelUuidList.get(0)).thenReturn(parcel_Uuids);
        PowerMockito.when(parcel_Uuids.getUuid()).thenReturn(uuid);


        PowerMockito.when(bleScanController, "parseUuid", uuid).thenReturn(uuidMode);

        PowerMockito.whenNew(BluetoothScan.class).withAnyArguments().thenReturn(mBluetoothScan);

        PowerMockito.when(If_vehicleScanListener, "onScanDeviceResult", mBluetoothScan).thenAnswer(voidAnswer);

        Whitebox.invokeMethod(bleScanController, "processScanResult", mBluetoothDevice, mScanRecord);

        PowerMockito.verifyPrivate(bleScanController, times(1)).invoke("processScanResult", mBluetoothDevice, mScanRecord);
    }

    @Test
    public void testProcessScanResult_Case06() throws Exception {
        PowerMockito.when(bleScanController, "processScanResult", mBluetoothDevice, mScanRecord).thenCallRealMethod();
        Whitebox.setInternalState(bleScanController, "mIsScanning", true);
        Whitebox.setInternalState(bleScanController, "mVehicleScanListener", If_vehicleScanListener);
        PowerMockito.when(TextUtils.isEmpty(mBluetoothDevice.getName())).thenReturn(false);

        PowerMockito.when(mScanRecord.getServiceUuids()).thenReturn(null);

        Whitebox.invokeMethod(bleScanController, "processScanResult", mBluetoothDevice, mScanRecord);

        PowerMockito.verifyPrivate(bleScanController, times(1)).invoke("processScanResult", mBluetoothDevice, mScanRecord);
    }

    @Test
    public void testProcessScanResult_Case07() throws Exception {
        PowerMockito.when(bleScanController, "processScanResult", mBluetoothDevice, mScanRecord).thenCallRealMethod();
        Whitebox.setInternalState(bleScanController, "mIsScanning", true);
        Whitebox.setInternalState(bleScanController, "mVehicleScanListener", If_vehicleScanListener);
        PowerMockito.when(TextUtils.isEmpty(mBluetoothDevice.getName())).thenReturn(false);
        List<ParcelUuid> parcelUuidList = mock(List.class);
        PowerMockito.when(mScanRecord.getServiceUuids()).thenReturn(parcelUuidList);
        PowerMockito.when(parcelUuidList.size()).thenReturn(1);
        PowerMockito.when(parcelUuidList.get(0)).thenReturn(parcel_Uuids);
        PowerMockito.when(parcel_Uuids.getUuid()).thenReturn(uuid);


        PowerMockito.when(bleScanController, "parseUuid", uuid).thenReturn(BleScanController.UuidMode.MODE_REGISTER);

        PowerMockito.whenNew(BluetoothScan.class).withAnyArguments().thenReturn(mBluetoothScan);

        PowerMockito.when(If_vehicleScanListener, "onScanDeviceResult", mBluetoothScan).thenAnswer(voidAnswer);

        Whitebox.invokeMethod(bleScanController, "processScanResult", mBluetoothDevice, mScanRecord);

        PowerMockito.verifyPrivate(bleScanController, times(1)).invoke("processScanResult", mBluetoothDevice, mScanRecord);
    }


    @Test
    public void testOnScanFailed_Case01() throws Exception {
        PowerMockito.doCallRealMethod().when(bleScanController).onScanFailed(ScanCallback.SCAN_FAILED_ALREADY_STARTED);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "mIsScanning", false);
        Whitebox.setInternalState(bleScanController, "mVehicleScanListener", If_vehicleScanListener);

        bleScanController.onScanFailed(ScanCallback.SCAN_FAILED_ALREADY_STARTED);
    }

    @Test
    public void testOnScanFailed_Case02() throws Exception {
        PowerMockito.doCallRealMethod().when(bleScanController).onScanFailed(ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "mIsScanning", false);
        Whitebox.setInternalState(bleScanController, "mVehicleScanListener", If_vehicleScanListener);

        bleScanController.onScanFailed(ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED);
    }

    @Test
    public void testOnScanFailed_Case03() throws Exception {
        PowerMockito.doCallRealMethod().when(bleScanController).onScanFailed(ScanCallback.SCAN_FAILED_INTERNAL_ERROR);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "mIsScanning", false);
        Whitebox.setInternalState(bleScanController, "mVehicleScanListener", If_vehicleScanListener);

        bleScanController.onScanFailed(ScanCallback.SCAN_FAILED_INTERNAL_ERROR);
    }

    @Test
    public void testOnScanFailed_Case04() throws Exception {
        PowerMockito.doCallRealMethod().when(bleScanController).onScanFailed(ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(bleScanController, "mIsScanning", false);
        Whitebox.setInternalState(bleScanController, "mVehicleScanListener", If_vehicleScanListener);

        bleScanController.onScanFailed(ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED);
    }

    @Test
    public void testParseUuid_Case01() throws Exception {
        PowerMockito.when(bleScanController, "parseUuid", (Object) null).thenCallRealMethod();

        BleScanController.UuidMode actual = Whitebox.invokeMethod(bleScanController, "parseUuid", (Object) null);

        assertEquals(BleScanController.UuidMode.MODE_OTHER, actual);
    }

    @Test
    public void testParseUuid_Case02() throws Exception {
        when(uuid.toString()).thenReturn(BLE_PARAMETER_SERVICE);

        PowerMockito.when(bleScanController, "parseUuid", uuid).thenCallRealMethod();
        BleScanController.UuidMode actual = Whitebox.invokeMethod(bleScanController, "parseUuid", uuid);

        assertEquals(BleScanController.UuidMode.MODE_NORMAL, actual);
    }

    @Test
    public void testParseUuid_Case03() throws Exception {
        when(uuid.toString()).thenReturn(LOW_POWER_SLEEP_SERVICE);
        PowerMockito.when(bleScanController, "parseUuid", uuid).thenCallRealMethod();

        BleScanController.UuidMode actual = Whitebox.invokeMethod(bleScanController, "parseUuid", uuid);

        assertEquals(BleScanController.UuidMode.MODE_SLEEP, actual);
    }

    @Test
    public void testParseUuid_Case04() throws Exception {
        when(uuid.toString()).thenReturn("00000000-0000-4489-0000-ffa28cde82ab");

        PowerMockito.when(bleScanController, "parseUuid", uuid).thenCallRealMethod();

        BleScanController.UuidMode actual = Whitebox.invokeMethod(bleScanController, "parseUuid", uuid);

        assertEquals(BleScanController.UuidMode.MODE_NORMAL, actual);
    }

    @Test
    public void testParseUuid_Case05() throws Exception {
        when(uuid.toString()).thenReturn("00000000-2222-4489-0000-ffa28cde82ab");

        PowerMockito.when(bleScanController, "parseUuid", uuid).thenCallRealMethod();

        BleScanController.UuidMode actual = Whitebox.invokeMethod(bleScanController, "parseUuid", uuid);

        assertEquals(BleScanController.UuidMode.MODE_NORMAL, actual);
    }

    @Test
    public void testParseUuid_Case06() throws Exception {
        when(uuid.toString()).thenReturn("00000000-1111-4489-0000-ffa28cde82ab");

        PowerMockito.when(bleScanController, "parseUuid", uuid).thenCallRealMethod();

        BleScanController.UuidMode actual = Whitebox.invokeMethod(bleScanController, "parseUuid", uuid);

        assertEquals(BleScanController.UuidMode.MODE_NORMAL, actual);
    }

    @Test
    public void testParseUuid_Case07() throws Exception {
        when(uuid.toString()).thenReturn("000000ff-3333-4489-0000-ffa28cde82ab");

        PowerMockito.when(bleScanController, "parseUuid", uuid).thenCallRealMethod();

        BleScanController.UuidMode actual = Whitebox.invokeMethod(bleScanController, "parseUuid", uuid);

        assertEquals(BleScanController.UuidMode.MODE_REGISTER, actual);
    }

    @Test
    public void testParseUuid_Case08() throws Exception {
        when(uuid.toString()).thenReturn("00000011-3333-4489-0000-ffa28cde82ab");

        PowerMockito.when(bleScanController, "parseUuid", uuid).thenCallRealMethod();

        BleScanController.UuidMode actual = Whitebox.invokeMethod(bleScanController, "parseUuid", uuid);

        assertEquals(BleScanController.UuidMode.MODE_OTHER, actual);
    }
}
