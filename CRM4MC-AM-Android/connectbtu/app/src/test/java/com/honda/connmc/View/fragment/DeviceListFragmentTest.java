package com.honda.connmc.View.fragment;


import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.connect.BleConnectController;
import com.honda.connmc.Bluetooth.timer.IF_TimeScheduleListener;
import com.honda.connmc.Bluetooth.timer.TimeManager;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Interfaces.listener.connect.IF_VehicleConnectListener;
import com.honda.connmc.Interfaces.listener.register.IF_VehicleRegisterListener;
import com.honda.connmc.Interfaces.listener.scan.IF_VehicleScanListener;
import com.honda.connmc.Model.BluetoothScan;
import com.honda.connmc.R;
import com.honda.connmc.Utils.FragmentUtils;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.View.activity.MainActivity;
import com.honda.connmc.View.adapter.DeviceListAdapter;
import com.honda.connmc.View.dialog.DialogCommon;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static com.honda.connmc.Bluetooth.BluetoothManager.EnumDeviceConnectStatus.CONNECTED;
import static com.honda.connmc.Bluetooth.BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DeviceListFragment.class,
        RecyclerView.class,
        IF_VehicleScanListener.class,
        IF_TimeScheduleListener.class,
        IF_VehicleConnectListener.class,
        IF_TimeScheduleListener.class,
        IF_VehicleRegisterListener.class,
        TimeManager.class,
        DeviceListAdapter.class,
        BluetoothScan.class,
        Dialog.class,
        FragmentManager.class,
        FragmentUtils.class,
        DialogCommon.class,
        LogUtils.class,
        BluetoothUtils.class,
        BluetoothManager.class,
        BluetoothDevice.class,
        BleConnectController.class,
        BluetoothManager.EnumDeviceConnectStatus.class,
        View.OnClickListener.class,
        View.class,
        Runnable.class,
        ProgressBar.class,
        MainActivity.class

})
public class DeviceListFragmentTest {
    @Mock
    private DeviceListFragment mDeviceListFragment;

    @Mock
    private FragmentManager mFragmentManager;

    @Mock
    private BluetoothManager mBluetoothManager;

    @Mock
    private BluetoothScan mBluetoothDevice;

    @Mock
    private IF_VehicleScanListener mScanListener;

    @Mock
    private IF_VehicleConnectListener mVehicleConnectListener;
    @Mock
    private ProgressBar mProgressBar;

    @Mock
    private FragmentActivity activity;

    Answer voidAnswer = invocation -> null;

    @Before
    public void setUp() throws Exception {
        mock(IF_VehicleScanListener.class);
        PowerMockito.mockStatic(BluetoothManager.class);
        PowerMockito.mockStatic(LogUtils.class);
        PowerMockito.mock(TimeManager.class);
        PowerMockito.mockStatic(BluetoothDevice.class);
        PowerMockito.mockStatic(BluetoothScan.class);
        PowerMockito.mockStatic(FragmentManager.class);
        PowerMockito.mockStatic(IF_TimeScheduleListener.class);
        PowerMockito.mockStatic(IF_VehicleConnectListener.class);
        PowerMockito.mockStatic(IF_VehicleRegisterListener.class);
        PowerMockito.mockStatic(Dialog.class);
        PowerMockito.mockStatic(DialogCommon.class);
        PowerMockito.mockStatic(FragmentUtils.class);
        PowerMockito.mockStatic(Runnable.class);
        PowerMockito.mockStatic(MainActivity.class);
        mProgressBar = mock(ProgressBar.class);
        PowerMockito.when(mDeviceListFragment.getActivity()).thenReturn(activity);

        PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "e", anyString()).thenAnswer(voidAnswer);
    }

    @Test
    public void testStartScan_Case01() throws Exception {
        PowerMockito.when(mDeviceListFragment, "startScan").thenCallRealMethod();

        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "setScanListener", mScanListener).thenAnswer(voidAnswer);

        when(mBluetoothManager.startScan(activity)).thenReturn(true);

        //Call method
        Whitebox.invokeMethod(mDeviceListFragment, "startScan");
        PowerMockito.verifyPrivate(mDeviceListFragment, atLeastOnce()).invoke("startScan");
    }

    @Test
    public void testStopScan_Case01() throws Exception {
        PowerMockito.when(mDeviceListFragment, "stopScan").thenCallRealMethod();

        Whitebox.setInternalState(mDeviceListFragment, "mProgressbar", mProgressBar);

        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "stopScan").thenAnswer(voidAnswer);


        //Call method
        Whitebox.invokeMethod(mDeviceListFragment, "stopScan");
        PowerMockito.verifyPrivate(mDeviceListFragment, atLeastOnce()).invoke("stopScan");
    }

//    @Test
//    public void testOnStartScan_Case1() throws Exception {
//        PowerMockito.when(mDeviceListFragment, "onStartScan").thenCallRealMethod();
//
//        Whitebox.setInternalState(mDeviceListFragment, "mBluetoothDevices", mListBluetoothDevices);
//        PowerMockito.when(mListBluetoothDevices, "clear").thenAnswer(voidAnswer);
//
//        //Call method
//        Whitebox.invokeMethod(mDeviceListFragment, "stopScan");
//    }

//    @Test
//    public void testOnScanDeviceResult_Case01() throws Exception {
//        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onScanDeviceResult(mBluetoothScan);
//        PowerMockito.when(mDeviceListFragment, "isFragmentAlife").thenReturn(true);
//        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
//
//        PowerMockito.when(mDeviceListFragment, "getDeviceAddToList", mBluetoothScan).thenReturn(1);
//
//        Whitebox.setInternalState(mDeviceListFragment, "mBluetoothDevices", mListBluetoothDevices);
//
//        PowerMockito.when(mListBluetoothDevices, "remove", anyInt()).thenAnswer(voidAnswer);
//
//        PowerMockito.when(mListBluetoothDevices, "add", anyInt(), any(BluetoothScan.class)).thenAnswer(voidAnswer);
//
//        PowerMockito.when(mDeviceListFragment, "dataChangeAndUpdateUi").thenAnswer(voidAnswer);
//
//        mDeviceListFragment.onScanDeviceResult(mBluetoothScan);
//    }
//
//    @Test
//    public void testOnScanDeviceResult_Case02() throws Exception {
//        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onScanDeviceResult(mBluetoothScan);
//        PowerMockito.when(mDeviceListFragment, "isFragmentAlife").thenReturn(false);
//        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
//
//        PowerMockito.when(mDeviceListFragment, "getDeviceAddToList", mBluetoothScan).thenReturn(1);
//
//        Whitebox.setInternalState(mDeviceListFragment, "mBluetoothDevices", mListBluetoothDevices);
//
//        PowerMockito.when(mListBluetoothDevices, "remove", anyInt()).thenAnswer(voidAnswer);
//
//        PowerMockito.when(mListBluetoothDevices, "add", anyInt(), any(BluetoothScan.class)).thenAnswer(voidAnswer);
//
//        PowerMockito.when(mDeviceListFragment, "dataChangeAndUpdateUi").thenAnswer(voidAnswer);
//
//        mDeviceListFragment.onScanDeviceResult(mBluetoothScan);
//    }
//
//    @Test
//    public void testOnScanDeviceResult_Case03() throws Exception {
//        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onScanDeviceResult(mBluetoothScan);
//        PowerMockito.when(mDeviceListFragment, "isFragmentAlife").thenReturn(true);
//        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
//
//        PowerMockito.when(mDeviceListFragment, "getDeviceAddToList", mBluetoothScan).thenReturn(-1);
//
//        Whitebox.setInternalState(mDeviceListFragment, "mBluetoothDevices", mListBluetoothDevices);
//
//        PowerMockito.when(mListBluetoothDevices, "add", any(BluetoothScan.class)).thenAnswer(voidAnswer);
//
//        PowerMockito.when(mDeviceListFragment, "dataChangeAndUpdateUi").thenAnswer(voidAnswer);
//
//        mDeviceListFragment.onScanDeviceResult(mBluetoothScan);
//    }
//
//    @Test
//    public void testOnScanDeviceResult_Case04() throws Exception {
//        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onScanDeviceResult(mBluetoothScan);
//
//        PowerMockito.when(mDeviceListFragment, "isFragmentAlife").thenReturn(true);
//        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
//
//        PowerMockito.when(mDeviceListFragment, "getDeviceAddToList", mBluetoothScan).thenReturn(-2);
//
//        mDeviceListFragment.onScanDeviceResult(mBluetoothScan);
//    }

//    @Test
//    public void testGetDeviceAddToList_Case01() throws Exception {
//        PowerMockito.doCallRealMethod().when(mDeviceListFragment, "getDeviceAddToList", mBluetoothScan);
//
//        Whitebox.setInternalState(mDeviceListFragment, "mBluetoothDevices", mListBluetoothDevices);
//
//        when(mListBluetoothDevices.size()).thenReturn(5);
//
//        when(mListBluetoothDevices.get(anyInt())).thenReturn(mBluetoothScanFromList);
//
//        when(mBluetoothScan.getDevice()).thenReturn(mBluetoothDevice);
//        when(mBluetoothScanFromList.getDevice()).thenReturn(mBluetoothDeviceFromList);
//
//        when(mBluetoothDevice.getName()).thenReturn("String");
//        when(mBluetoothDeviceFromList.getName()).thenReturn("String");
//
//        assertEquals(mBluetoothDeviceFromList.getName(), mBluetoothDevice.getName());
//
//        when(mBluetoothDevice.getAddress()).thenReturn("192.168.1.1");
//        when(mBluetoothDeviceFromList.getAddress()).thenReturn("192.168.1.1");
//        assertEquals(mBluetoothDeviceFromList.getAddress(), mBluetoothDevice.getAddress());
//
//        when(mBluetoothScan.getUuidMode()).thenReturn(BleScanController.UuidMode.MODE_OTHER);
//        when(mBluetoothScanFromList.getUuidMode()).thenReturn(BleScanController.UuidMode.MODE_OTHER);
//
//        assertEquals(mBluetoothScan.getUuidMode(), mBluetoothScanFromList.getUuidMode());
//
//        int actual = Whitebox.invokeMethod(mDeviceListFragment, "getDeviceAddToList", mBluetoothScan);
//
//        assertEquals(-2, actual);
//    }
//
//    @Test
//    public void testGetDeviceAddToList_Case02() throws Exception {
//        PowerMockito.doCallRealMethod().when(mDeviceListFragment, "getDeviceAddToList", mBluetoothScan);
//
//        Whitebox.setInternalState(mDeviceListFragment, "mBluetoothDevices", mListBluetoothDevices);
//
//        when(mListBluetoothDevices.size()).thenReturn(5);
//
//        when(mListBluetoothDevices.get(anyInt())).thenReturn(mBluetoothScanFromList);
//
//        when(mBluetoothScan.getDevice()).thenReturn(mBluetoothDevice);
//        when(mBluetoothScanFromList.getDevice()).thenReturn(mBluetoothDeviceFromList);
//
//        when(mBluetoothDevice.getName()).thenReturn("String");
//        when(mBluetoothDeviceFromList.getName()).thenReturn("Integer");
//
//
//        when(mBluetoothDevice.getAddress()).thenReturn("192.168.1.1");
//        when(mBluetoothDeviceFromList.getAddress()).thenReturn("192.168.1.1");
//
//        when(mBluetoothScan.getUuidMode()).thenReturn(BleScanController.UuidMode.MODE_OTHER);
//        when(mBluetoothScanFromList.getUuidMode()).thenReturn(BleScanController.UuidMode.MODE_OTHER);
//
//
//        int actual = Whitebox.invokeMethod(mDeviceListFragment, "getDeviceAddToList", mBluetoothScan);
//
//        assertEquals(-1, actual);
//    }
//
//    @Test
//    public void testGetDeviceAddToList_Case03() throws Exception {
//        PowerMockito.doCallRealMethod().when(mDeviceListFragment, "getDeviceAddToList", mBluetoothScan);
//
//        Whitebox.setInternalState(mDeviceListFragment, "mBluetoothDevices", mListBluetoothDevices);
//
//        when(mListBluetoothDevices.size()).thenReturn(5);
//
//        when(mListBluetoothDevices.get(anyInt())).thenReturn(mBluetoothScanFromList);
//
//        when(mBluetoothScan.getDevice()).thenReturn(mBluetoothDevice);
//        when(mBluetoothScanFromList.getDevice()).thenReturn(mBluetoothDeviceFromList);
//
//        when(mBluetoothDevice.getName()).thenReturn("String");
//        when(mBluetoothDeviceFromList.getName()).thenReturn("String");
//
//
//        when(mBluetoothDevice.getAddress()).thenReturn("192.168.1.1");
//        when(mBluetoothDeviceFromList.getAddress()).thenReturn("192.168.1.2");
//
//        when(mBluetoothScan.getUuidMode()).thenReturn(BleScanController.UuidMode.MODE_OTHER);
//        when(mBluetoothScanFromList.getUuidMode()).thenReturn(BleScanController.UuidMode.MODE_OTHER);
//
//
//        int actual = Whitebox.invokeMethod(mDeviceListFragment, "getDeviceAddToList", mBluetoothScan);
//
//        assertEquals(anyInt(), actual);
//    }
//
//    @Test
//    public void testGetDeviceAddToList_Case04() throws Exception {
//        PowerMockito.doCallRealMethod().when(mDeviceListFragment, "getDeviceAddToList", mBluetoothScan);
//
//        Whitebox.setInternalState(mDeviceListFragment, "mBluetoothDevices", mListBluetoothDevices);
//
//        when(mListBluetoothDevices.size()).thenReturn(5);
//
//        when(mListBluetoothDevices.get(anyInt())).thenReturn(mBluetoothScanFromList);
//
//        when(mBluetoothScan.getDevice()).thenReturn(mBluetoothDevice);
//        when(mBluetoothScanFromList.getDevice()).thenReturn(mBluetoothDeviceFromList);
//
//        when(mBluetoothDevice.getName()).thenReturn("String");
//        when(mBluetoothDeviceFromList.getName()).thenReturn("String");
//
//
//        when(mBluetoothDevice.getAddress()).thenReturn("192.168.1.1");
//        when(mBluetoothDeviceFromList.getAddress()).thenReturn("192.168.1.1");
//
//        when(mBluetoothScan.getUuidMode()).thenReturn(BleScanController.UuidMode.MODE_OTHER);
//        when(mBluetoothScanFromList.getUuidMode()).thenReturn(BleScanController.UuidMode.MODE_IDEA);
//
//
//        int actual = Whitebox.invokeMethod(mDeviceListFragment, "getDeviceAddToList", mBluetoothScan);
//
//        assertEquals(anyInt(), actual);
//    }

    @Test
    public void testOnScanFail_Case01() throws Exception {
        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onScanFail(ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED);
        PowerMockito.when(mDeviceListFragment, "isFragmentAlife").thenReturn(true);

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        mDeviceListFragment.onScanFail(ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED);

        verify(mDeviceListFragment, atLeastOnce()).onScanFail(ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED);
    }

    @Test
    public void testOnScanFail_Case02() throws Exception {
        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onScanFail(ScanCallback.SCAN_FAILED_INTERNAL_ERROR);
        PowerMockito.when(mDeviceListFragment, "isFragmentAlife").thenReturn(true);

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        mDeviceListFragment.onScanFail(ScanCallback.SCAN_FAILED_INTERNAL_ERROR);

        verify(mDeviceListFragment, atLeastOnce()).onScanFail(ScanCallback.SCAN_FAILED_INTERNAL_ERROR);
    }

    @Test
    public void testOnScanFail_Case03() throws Exception {
        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onScanFail(ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED);
        PowerMockito.when(mDeviceListFragment, "isFragmentAlife").thenReturn(true);

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        mDeviceListFragment.onScanFail(ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED);

        verify(mDeviceListFragment, atLeastOnce()).onScanFail(ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED);
    }

    @Test
    public void testOnScanFail_Case04() throws Exception {
        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onScanFail(ScanCallback.SCAN_FAILED_ALREADY_STARTED);
        PowerMockito.when(mDeviceListFragment, "isFragmentAlife").thenReturn(true);

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        mDeviceListFragment.onScanFail(ScanCallback.SCAN_FAILED_ALREADY_STARTED);

        verify(mDeviceListFragment, atLeastOnce()).onScanFail(ScanCallback.SCAN_FAILED_ALREADY_STARTED);
    }

    @Test
    public void testOnScanFail_Case05() throws Exception {
        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onScanFail(ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED);
        PowerMockito.when(mDeviceListFragment, "isFragmentAlife").thenReturn(false);

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        mDeviceListFragment.onScanFail(ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED);

        verify(mDeviceListFragment, atLeastOnce()).onScanFail(ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED);
    }

    @Test
    public void testOnScanFail_Case06() throws Exception {
        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onScanFail(ScanCallback.SCAN_FAILED_ALREADY_STARTED);
        PowerMockito.when(mDeviceListFragment, "isFragmentAlife").thenReturn(false);

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        mDeviceListFragment.onScanFail(ScanCallback.SCAN_FAILED_ALREADY_STARTED);

        verify(mDeviceListFragment, atLeastOnce()).onScanFail(ScanCallback.SCAN_FAILED_ALREADY_STARTED);
    }

    @Test
    public void testOnScanFail_Case07() throws Exception {
        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onScanFail(ScanCallback.SCAN_FAILED_INTERNAL_ERROR);
        PowerMockito.when(mDeviceListFragment, "isFragmentAlife").thenReturn(false);

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        mDeviceListFragment.onScanFail(ScanCallback.SCAN_FAILED_INTERNAL_ERROR);

        verify(mDeviceListFragment, atLeastOnce()).onScanFail(ScanCallback.SCAN_FAILED_INTERNAL_ERROR);
    }

    @Test
    public void testOnScanFail_Case08() throws Exception {
        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onScanFail(ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED);
        PowerMockito.when(mDeviceListFragment, "isFragmentAlife").thenReturn(false);

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        mDeviceListFragment.onScanFail(ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED);

        verify(mDeviceListFragment, atLeastOnce()).onScanFail(ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED);
    }

    @Test
    public void testConnectDevice_Case01() throws Exception {
        PowerMockito.doCallRealMethod().when(mDeviceListFragment, "connectDevice", mBluetoothDevice);

        PowerMockito.when(mDeviceListFragment, "stopScan").thenAnswer(voidAnswer);

        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "setConnectListener", mVehicleConnectListener).thenAnswer(voidAnswer);

        boolean actual = Whitebox.invokeMethod(mDeviceListFragment, "connectDevice", mBluetoothDevice);

        assertFalse(actual);
    }

    @Test
    public void testOnConnectStateNotice_Case01() throws Exception {
        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onConnectStateNotice(CONNECTED);
        PowerMockito.when(mDeviceListFragment, "isFragmentAlife").thenReturn(false);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        mDeviceListFragment.onConnectStateNotice(CONNECTED);

        verify(mDeviceListFragment, atLeastOnce()).onConnectStateNotice(CONNECTED);
    }

    @Test
    public void testOnConnectStateNotice_Case02() throws Exception {
        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onConnectStateNotice(DISCONNECTED);
        PowerMockito.when(mDeviceListFragment, "isFragmentAlife").thenReturn(true);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(mDeviceListFragment, "hideProgressDialog").thenAnswer(voidAnswer);

        mDeviceListFragment.onConnectStateNotice(DISCONNECTED);

        verify(mDeviceListFragment, atLeastOnce()).onConnectStateNotice(DISCONNECTED);
    }

    @Test
    public void testOnItemClick_Case01() throws Exception {
        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onItemClick(mBluetoothDevice);
        PowerMockito.when(mDeviceListFragment, "isEnableClick").thenReturn(false);

        mDeviceListFragment.onItemClick(mBluetoothDevice);

        verify(mDeviceListFragment, atLeastOnce()).onItemClick(mBluetoothDevice);
    }

    @Test
    public void testOnItemClick_Case02() throws Exception {
        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onItemClick(mBluetoothDevice);
        PowerMockito.when(mDeviceListFragment, "isEnableClick").thenReturn(true);
        PowerMockito.when(mDeviceListFragment, "showProgressDialog").thenAnswer(voidAnswer);
        mDeviceListFragment.onItemClick(mBluetoothDevice);
        verify(mDeviceListFragment, atLeastOnce()).onItemClick(mBluetoothDevice);
    }

    @Test
    public void testOnclick_Case01() throws Exception {
        ImageView imageView = PowerMockito.mock(ImageView.class);
        doReturn(imageView).when(mDeviceListFragment).findViewById(R.id.btnBack);
        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onClick(imageView);
        PowerMockito.when(mDeviceListFragment, "isEnableClick").thenReturn(true);
        PowerMockito.when(imageView, "getId").thenReturn(R.id.btnBack);

        PowerMockito.when(mDeviceListFragment, "stopScan").thenAnswer(voidAnswer);

        DeviceNotRegisterFragment fragment = mock(DeviceNotRegisterFragment.class);
        PowerMockito.whenNew(DeviceNotRegisterFragment.class).withAnyArguments().thenReturn(fragment);
        PowerMockito.when(activity.getSupportFragmentManager()).thenReturn(mFragmentManager);

        PowerMockito.when(FragmentUtils.class, "clearAllFragment", mFragmentManager).thenAnswer(voidAnswer);
        PowerMockito.when(FragmentUtils.class,
                "replaceFragment",
                mFragmentManager,
                fragment,
                R.id.fragment_container,
                false,
                FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK).thenAnswer(voidAnswer);


        mDeviceListFragment.onClick(imageView);

        verify(mDeviceListFragment, atLeastOnce()).onClick(imageView);
    }

    @Test
    public void testOnclick_Case02() throws Exception {
        ImageView imageView = PowerMockito.mock(ImageView.class);
        doReturn(imageView).when(mDeviceListFragment).findViewById(R.id.btnBack);
        PowerMockito.doCallRealMethod().when(mDeviceListFragment).onClick(imageView);

        PowerMockito.when(mDeviceListFragment, "isEnableClick").thenReturn(false);

        mDeviceListFragment.onClick(imageView);

        verify(mDeviceListFragment, atLeastOnce()).onClick(imageView);
    }
}
