package com.honda.connmc.Bluetooth.register;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.honda.connmc.Application.ConnMCApplication;
import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.connect.BleConnectController;
import com.honda.connmc.Bluetooth.message.DataMessageManager;
import com.honda.connmc.Bluetooth.timer.TimeManager;
import com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Constants.message.ParamTag;
import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.register.VehicleRegistrationType;
import com.honda.connmc.Interfaces.listener.register.IF_VehicleRegisterListener;
import com.honda.connmc.Model.message.common.VehicleParam;
import com.honda.connmc.Model.message.register.response.VehicleRegistrationResponseMessage;
import com.honda.connmc.NativeLib.NativeLibController;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.Utils.PermissionUtils;
import com.honda.connmc.Utils.StringUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_BTU_NAME_REGISTER_SUCCESS;
import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_REGISTER_SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        BleHandleRegistrationResult.class,
        BleRegisterController.class,
        IF_VehicleRegisterListener.class,
        LogUtils.class,
        VehicleRegistrationResponseMessage.class,
        TextUtils.class,
        BluetoothPrefUtils.class,
        BluetoothManager.class,
        String.class,
        ParamTag.class,
        NativeLibController.class,
        BleConnectController.class,
        ByteBuffer.class,
        StringUtils.class,
        PermissionUtils.class,
        TelephonyManager.class,
        ConnMCApplication.class,
        DataMessageManager.class,
        Object.class,
        Build.class

})
public class BleHandleRegistrationResultTest {
    @Mock
    private BleHandleRegistrationResult mBleHandleRegistrationResult;
    @Mock
    private BleRegisterController mBleRegisterController;
    @Mock
    private IF_VehicleRegisterListener mIF_VehicleRegisterListener;
    @Mock
    private VehicleRegistrationResponseMessage mVehicleRegistrationResponseMessage;
    @Mock
    private BluetoothPrefUtils mBluetoothPrefUtils;
    @Mock
    private BluetoothManager mBluetoothManager;
    @Mock
    private BluetoothDevice mBluetoothDevice;
    @Mock
    private DataMessageManager mDataMessageManager;
    @Mock
    private NativeLibController mNativeLibController;
    @Mock
    private BleConnectController mBleConnectController;
    @Mock
    private ByteBuffer mByteBuffer;
    @Mock
    private Object mObject;

    Answer voidAnswer = invocation -> null;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(BleConnectController.class);
        PowerMockito.mockStatic(BleRegisterController.class);
        PowerMockito.mockStatic(BluetoothDevice.class);
        PowerMockito.mockStatic(ByteBuffer.class);
        PowerMockito.mockStatic(BluetoothManager.EnumDeviceConnectStatus.class);
        PowerMockito.mockStatic(TimeManager.class);
        PowerMockito.mockStatic(ConnMCApplication.class);
        PowerMockito.mockStatic(TelephonyManager.class);
        PowerMockito.mockStatic(LogUtils.class);
        PowerMockito.mockStatic(BluetoothGattCharacteristic.class);
        PowerMockito.mockStatic(DataMessageManager.class);
        PowerMockito.mockStatic(BluetoothManager.class);
        PowerMockito.mockStatic(BluetoothGattService.class);
        PowerMockito.mockStatic(IF_VehicleRegisterListener.class);
        PowerMockito.mockStatic(BluetoothPrefUtils.class);
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(BluetoothUtils.class);
        PowerMockito.mockStatic(String.class);
        PowerMockito.mockStatic(Build.class);
        PowerMockito.mockStatic(ParamTag.class);
        PowerMockito.mockStatic(StringUtils.class);
        PowerMockito.mockStatic(Thread.class);
        PowerMockito.mockStatic(BleHandleRegistrationResult.class);
        PowerMockito.mockStatic(NativeLibController.class);
        PowerMockito.mockStatic(PermissionUtils.class);
        PowerMockito.mockStatic(Object.class);

        Whitebox.setInternalState(mBleHandleRegistrationResult, "LOCK_OBJECT", mObject);
        PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "e", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "i", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "w", anyString()).thenAnswer(voidAnswer);
    }

    @Test
    public void testResetData() throws Exception {
        PowerMockito.when(mBleHandleRegistrationResult, "resetData").thenCallRealMethod();

        Whitebox.setInternalState(mBleHandleRegistrationResult, "isRequestPinFail", false);

        mBleHandleRegistrationResult.resetData();

        verify(mBleHandleRegistrationResult, atLeastOnce()).resetData();
    }

    @Test
    public void testSetRegistrationEventListener() throws Exception {
        PowerMockito.when(mBleHandleRegistrationResult, "setRegistrationEventListener", mIF_VehicleRegisterListener).thenCallRealMethod();

        Whitebox.setInternalState(mBleHandleRegistrationResult, "mRegistrationEventListener", mIF_VehicleRegisterListener);

        mBleHandleRegistrationResult.setRegistrationEventListener(mIF_VehicleRegisterListener);

        IF_VehicleRegisterListener actual = Whitebox.getInternalState(mBleHandleRegistrationResult, "mRegistrationEventListener");

        assertEquals(mIF_VehicleRegisterListener, actual);
    }

    @Test
    public void testExtractRegistrationResultMessage_Case01() throws Exception {
        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationResultMessage", mVehicleRegistrationResponseMessage).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        int result = ParamValue.Security.RegistrationResult.REDIRECT;
        int cause = ParamValue.Security.ResultCause.BTU_REGISTERED;
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResult()).thenReturn(result);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResultCause()).thenReturn(cause);
        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause).thenAnswer(voidAnswer);

        mBleHandleRegistrationResult.extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);

        verify(mBleHandleRegistrationResult, atLeastOnce()).extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);
    }

    @Test
    public void testExtractRegistrationResultMessage_Case02() throws Exception {
        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationResultMessage", mVehicleRegistrationResponseMessage).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        int result = ParamValue.Security.RegistrationResult.REJECT;
        int cause = ParamValue.Security.ResultCause.INVALID_PIN_CODE;
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResult()).thenReturn(result);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResultCause()).thenReturn(cause);

        Whitebox.setInternalState(mBleHandleRegistrationResult, "mRegistrationEventListener", mIF_VehicleRegisterListener);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        PowerMockito.when(mIF_VehicleRegisterListener, "onVerifyPinCodeFail").thenAnswer(voidAnswer);

        mBleHandleRegistrationResult.extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);

        verify(mBleHandleRegistrationResult, atLeastOnce()).extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);
    }

    @Test
    public void testExtractRegistrationResultMessage_Case03() throws Exception {
        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationResultMessage", mVehicleRegistrationResponseMessage).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        int result = ParamValue.Security.RegistrationResult.REJECT;
        int cause = ParamValue.Security.ResultCause.TOO_MANY_USERS;
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResult()).thenReturn(result);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResultCause()).thenReturn(cause);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);

        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);

        mBleHandleRegistrationResult.extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);

        verify(mBleHandleRegistrationResult, atLeastOnce()).extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);
    }

    @Test
    public void testExtractRegistrationResultMessage_Case04() throws Exception {
        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationResultMessage", mVehicleRegistrationResponseMessage).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        int result = ParamValue.Security.RegistrationResult.FAILURE;
        int cause = ParamValue.Security.ResultCause.INVALID_PIN_CODE;
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResult()).thenReturn(result);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResultCause()).thenReturn(cause);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);

        mBleHandleRegistrationResult.extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);

        verify(mBleHandleRegistrationResult, atLeastOnce()).extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);
    }

    @Test
    public void testExtractRegistrationResultMessage_Case05() throws Exception {
        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationResultMessage", mVehicleRegistrationResponseMessage).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        int result = ParamValue.Security.RegistrationResult.SUCCESS;
        int cause = ParamValue.Security.ResultCause.BTU_NOT_REGISTERD;
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResult()).thenReturn(result);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResultCause()).thenReturn(cause);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);

        mBleHandleRegistrationResult.extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);

        verify(mBleHandleRegistrationResult, atLeastOnce()).extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);
    }

    @Test
    public void testExtractRegistrationResultMessage_Case06() throws Exception {
        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationResultMessage", mVehicleRegistrationResponseMessage).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        int result = ParamValue.Security.RegistrationResult.SUCCESS;
        int cause = ParamValue.Security.ResultCause.UNSPECIFIED;
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResult()).thenReturn(result);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResultCause()).thenReturn(cause);

        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(mNativeLibController.deleteRegistrationKey()).thenAnswer(voidAnswer);

        mBleHandleRegistrationResult.extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);

        verify(mBleHandleRegistrationResult, atLeastOnce()).extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);
    }

    @Test
    public void testExtractRegistrationResultMessage_Case07() throws Exception {
        List<String> registeredList = mock(List.class);
        registeredList.add("BTU00001");
        String name = "BTU00001";
        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationResultMessage", mVehicleRegistrationResponseMessage).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        int result = ParamValue.Security.RegistrationResult.SUCCESS;
        int cause = ParamValue.Security.ResultCause.UNSPECIFIED;
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResult()).thenReturn(result);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResultCause()).thenReturn(cause);

        PowerMockito.when(mVehicleRegistrationResponseMessage.getUserNameList()).thenReturn(registeredList);
        PowerMockito.when(registeredList.size()).thenReturn(7);

        Iterator iterator = mock(Iterator.class);
        when(registeredList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(name);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mRegistrationEventListener", mIF_VehicleRegisterListener);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, ParamValue.Security.ResultCause.TOO_MANY_USERS).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);

        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        PowerMockito.when(mBluetoothPrefUtils.setBool(KEY_REGISTER_SUCCESS, true)).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
        PowerMockito.when(mBleConnectController.getBluetoothDeviceConnect()).thenReturn(mBluetoothDevice);
        PowerMockito.when(mBluetoothDevice.getName()).thenReturn(name);
        PowerMockito.when(mBluetoothManager, "setDeviceCurrentStatus", BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothManager.getDataMessageManager()).thenReturn(mDataMessageManager);
        PowerMockito.when(mDataMessageManager, "setEnableEncrypt", true).then(voidAnswer);
        PowerMockito.when(mBluetoothPrefUtils.setString(KEY_BTU_NAME_REGISTER_SUCCESS, name)).thenAnswer(voidAnswer);
        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE).thenAnswer(voidAnswer);

        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(mNativeLibController.deleteRegistrationKey()).thenAnswer(voidAnswer);

        mBleHandleRegistrationResult.extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);

        verify(mBleHandleRegistrationResult, atLeastOnce()).extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);
    }

    @Test
    public void testExtractRegistrationResultMessage_Case08() throws Exception {
        List<String> registeredList = mock(List.class);
        registeredList.add("BTU00001");
        String name = "BTU00001";
        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationResultMessage", mVehicleRegistrationResponseMessage).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        int result = ParamValue.Security.RegistrationResult.SUCCESS;
        int cause = ParamValue.Security.ResultCause.UNSPECIFIED;
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResult()).thenReturn(result);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResultCause()).thenReturn(cause);


        PowerMockito.when(mVehicleRegistrationResponseMessage.getUserNameList()).thenReturn(registeredList);
        PowerMockito.when(registeredList.size()).thenReturn(5);

        Iterator iterator = mock(Iterator.class);
        when(registeredList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(name);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mRegistrationEventListener", mIF_VehicleRegisterListener);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);

        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        PowerMockito.when(mBluetoothPrefUtils.setBool(KEY_REGISTER_SUCCESS, true)).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
        PowerMockito.when(mBleConnectController.getBluetoothDeviceConnect()).thenReturn(mBluetoothDevice);
        PowerMockito.when(mBluetoothDevice.getName()).thenReturn(name);
        PowerMockito.when(mBluetoothManager, "setDeviceCurrentStatus", BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothManager.getDataMessageManager()).thenReturn(mDataMessageManager);
        PowerMockito.when(mDataMessageManager, "setEnableEncrypt", true).then(voidAnswer);
        PowerMockito.when(mBluetoothPrefUtils.setString(KEY_BTU_NAME_REGISTER_SUCCESS, name)).thenAnswer(voidAnswer);
        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE).thenAnswer(voidAnswer);

        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(mNativeLibController.deleteRegistrationKey()).thenAnswer(voidAnswer);

        mBleHandleRegistrationResult.extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);

        verify(mBleHandleRegistrationResult, atLeastOnce()).extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);
    }

    @Test
    public void testExtractRegistrationResultMessage_Case09() throws Exception {
        List<String> registeredList = mock(List.class);
        registeredList.add("BTU00001");
        String name = "BTU00001";
        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationResultMessage", mVehicleRegistrationResponseMessage).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        int result = 0x05;
        int cause = ParamValue.Security.ResultCause.UNSPECIFIED;

        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResult()).thenReturn(result);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResultCause()).thenReturn(cause);

        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);


        mBleHandleRegistrationResult.extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);

        verify(mBleHandleRegistrationResult, atLeastOnce()).extractRegistrationResultMessage(mVehicleRegistrationResponseMessage);
    }

    @Test
    public void testProcessReceiveResultRedirect_Case01() throws Exception {
        int cause = ParamValue.Security.ResultCause.PUBLIC_KEY_REQUIRED;
        List<String> userNameList = mock(List.class);
        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getUserNameList()).thenReturn(userNameList);

        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mRegistrationEventListener", mIF_VehicleRegisterListener);
        PowerMockito.when(mBleRegisterController, "requestPublicKeyGeneration").thenAnswer(voidAnswer);

        Whitebox.invokeMethod(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause);
        PowerMockito.verifyPrivate(mBleHandleRegistrationResult, times(1)).invoke("processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause);
    }

    @Test
    public void testProcessReceiveResultRedirect_Case02() throws Exception {
        int cause = ParamValue.Security.ResultCause.INVALID_PIN_CODE;
        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mRegistrationEventListener", mIF_VehicleRegisterListener);
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        PowerMockito.when(mIF_VehicleRegisterListener, "onVerifyPinCodeFail").thenAnswer(voidAnswer);

        Whitebox.invokeMethod(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause);

        PowerMockito.verifyPrivate(mBleHandleRegistrationResult, times(1)).invoke("processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause);
    }

    @Test
    public void testProcessReceiveResultRedirect_Case03() throws Exception {
        int cause = ParamValue.Security.ResultCause.USER_NAME_REQUIRED;
        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleHandleRegistrationResult, "isRequestPinFail", false);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mRegistrationEventListener", mIF_VehicleRegisterListener);
        PowerMockito.when(mIF_VehicleRegisterListener, "onRequestUserName").thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);

        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause).thenAnswer(voidAnswer);

        PowerMockito.verifyPrivate(mBleHandleRegistrationResult, times(1)).invoke("processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause);
    }

    @Test
    public void testProcessReceiveResultRedirect_Case04() throws Exception {
        int cause = ParamValue.Security.ResultCause.USER_NAME_DUPLICATE;
        String name = "Device1";
        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mRegistrationEventListener", mIF_VehicleRegisterListener);
        PowerMockito.when(mIF_VehicleRegisterListener, "onRequestEditUserName", name).thenAnswer(voidAnswer);

        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause).thenAnswer(voidAnswer);

        PowerMockito.verifyPrivate(mBleHandleRegistrationResult, times(1)).invoke("processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause);
    }

    @Test
    public void testProcessReceiveResultRedirect_Case05() throws Exception {
        int cause = ParamValue.Security.ResultCause.TOO_MANY_USERS;
        List<String> registeredList = mock(List.class);
        registeredList.add("BTU00001");
        String name = "BTU00001";
        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        PowerMockito.when(mVehicleRegistrationResponseMessage.getUserNameList()).thenReturn(registeredList);
        PowerMockito.when(registeredList.size()).thenReturn(7);
        PowerMockito.when(registeredList.get(0)).thenReturn(name);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mRegistrationEventListener", mIF_VehicleRegisterListener);
        PowerMockito.when(mIF_VehicleRegisterListener, "onResultTooManyUser", name).thenAnswer(voidAnswer);

        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause).thenAnswer(voidAnswer);

        PowerMockito.verifyPrivate(mBleHandleRegistrationResult, times(1)).invoke("processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause);
    }

    @Test
    public void testProcessReceiveResultRedirect_Case06() throws Exception {
        int cause = ParamValue.Security.ResultCause.TOO_MANY_USERS;
        List<String> registeredList = mock(List.class);
        registeredList.add("BTU00001");
        String name = "BTU00001";
        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        PowerMockito.when(mVehicleRegistrationResponseMessage.getUserNameList()).thenReturn(null);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);

        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause).thenAnswer(voidAnswer);

        PowerMockito.verifyPrivate(mBleHandleRegistrationResult, times(1)).invoke("processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause);
    }

    @Test
    public void testProcessReceiveResultRedirect_Case07() throws Exception {
        int cause = ParamValue.Security.ResultCause.BTU_REGISTERED;
        List<String> registeredList = mock(List.class);
        registeredList.add("BTU00001");
        String name = "BTU00001";
        String userName = "BTU00002";
        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        PowerMockito.when(mVehicleRegistrationResponseMessage.getUserNameList()).thenReturn(registeredList);
        PowerMockito.when(registeredList.size()).thenReturn(7);
        PowerMockito.when(registeredList.get(0)).thenReturn(name);
        Iterator iterator = mock(Iterator.class);
        when(registeredList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(userName);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController.getUserName()).thenReturn(userName);
        PowerMockito.when(TextUtils.equals(name, userName)).thenReturn(false);

        PowerMockito.when(mBleRegisterController, "replaceUser", name).thenAnswer(voidAnswer);

        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, ParamValue.Security.ResultCause.TOO_MANY_USERS).thenAnswer(voidAnswer);

        Whitebox.invokeMethod(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause);

        PowerMockito.verifyPrivate(mBleHandleRegistrationResult, times(1)).invoke("processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause);
    }

    @Test
    public void testProcessReceiveResultRedirect_Case08() throws Exception {
        int cause = ParamValue.Security.ResultCause.BTU_REGISTERED;
        List<String> registeredList = mock(List.class);
        registeredList.add("BTU00001");
        String name = "BTU00001";
        PowerMockito.when(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        PowerMockito.when(mVehicleRegistrationResponseMessage.getUserNameList()).thenReturn(registeredList);
        PowerMockito.when(registeredList.size()).thenReturn(7);
        PowerMockito.when(registeredList.get(0)).thenReturn(name);
        Iterator iterator = mock(Iterator.class);
        when(registeredList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(name);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController.getUserName()).thenReturn(name);
        PowerMockito.when(TextUtils.equals(name, name)).thenReturn(true);

        PowerMockito.when(mBleRegisterController, "replaceUser", name).thenAnswer(voidAnswer);

        Whitebox.invokeMethod(mBleHandleRegistrationResult, "processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause);

        PowerMockito.verifyPrivate(mBleHandleRegistrationResult, times(1)).invoke("processReceiveResultRedirect", mVehicleRegistrationResponseMessage, cause);
    }

    @Test
    public void testSaveUserName_Case01() throws Exception {
        String userName = "BTU00002";
        PowerMockito.when(mBleHandleRegistrationResult, "saveUserInfo", (Object) null).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        mBleHandleRegistrationResult.saveUserInfo(null);

        int actual = mBleHandleRegistrationResult.saveUserInfo(null);
        assertEquals(ParamValue.Security.RegistrationResult.OTHER_ERROR, actual);

    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testSaveUserName() throws Exception {
        String userName = "BTU00002";
        PowerMockito.when(mBleHandleRegistrationResult, "saveUserInfo", userName).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(mBleHandleRegistrationResult, "saveUserInfo", userName).thenThrow(UnsupportedEncodingException.class);

        mBleHandleRegistrationResult.saveUserInfo(userName);

        int actual = mBleHandleRegistrationResult.saveUserInfo(userName);
        assertEquals(ParamValue.Security.RegistrationResult.OTHER_ERROR, actual);

    }

    @Test
    public void testSaveUserName_Case02() throws Exception {
        String userName = "BTU00002";
        PowerMockito.when(mBleHandleRegistrationResult, "saveUserInfo", userName).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(ParamTag.getLength(ParamTag.USER_NAME)).thenReturn(1);

        mBleHandleRegistrationResult.saveUserInfo(userName);

        int actual = mBleHandleRegistrationResult.saveUserInfo(userName);
        assertEquals(ParamValue.Security.RegistrationResult.OTHER_ERROR, actual);

    }

    @Test
    public void testSaveUserName_Case03() throws Exception {
        String userName = "BTU00002";
        PowerMockito.when(mBleHandleRegistrationResult, "saveUserInfo", userName).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(ParamTag.getLength(ParamTag.USER_NAME)).thenReturn(1);

        PowerMockito.when(NativeLibController.getInstance()).thenReturn(null);

        mBleHandleRegistrationResult.saveUserInfo(userName);

        int actual = mBleHandleRegistrationResult.saveUserInfo(userName);
        assertEquals(ParamValue.Security.RegistrationResult.OTHER_ERROR, actual);

    }

    @Test
    public void testSaveUserName_Case04() throws Exception {
        String userName = "BTU00002";
        String res = "BTU00001";
        byte[] convertName = {66, 84, 85, 48, 48, 48, 48, 50};
        PowerMockito.when(mBleHandleRegistrationResult, "saveUserInfo", userName).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(ParamTag.getLength(ParamTag.USER_NAME)).thenReturn(523);

        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(StringUtils.convertBytesToString(convertName, null)).thenReturn(userName);

        mBleHandleRegistrationResult.saveUserInfo(userName);

        int actual = mBleHandleRegistrationResult.saveUserInfo(userName);
        assertEquals(ParamValue.Security.RegistrationResult.OTHER_ERROR, actual);
    }

    @Test
    public void testSaveUserName_Case05() throws Exception {
        String userName = "BTU00002";
        String res = "BTU00001";
        byte[] convertName = {66, 84, 85, 48, 48, 48, 48, 50};
        byte[] tidBufArray = {66, 84, 85};
        PowerMockito.when(mBleHandleRegistrationResult, "saveUserInfo", userName).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(ParamTag.getLength(ParamTag.USER_NAME)).thenReturn(523);

        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBleHandleRegistrationResult, "getDeviceId").thenReturn(mByteBuffer);
        PowerMockito.when(mByteBuffer.array()).thenReturn(tidBufArray);
        PowerMockito.when(mNativeLibController.setUserInfo(convertName, tidBufArray)).thenReturn(NativeLibController.VEHICLE_CRYPTO_RESULT_SUCCESS);
        PowerMockito.when(StringUtils.convertBytesToString(convertName, null)).thenReturn(userName);

        mBleHandleRegistrationResult.saveUserInfo(userName);

        int actual = mBleHandleRegistrationResult.saveUserInfo(userName);
        assertEquals(NativeLibController.VEHICLE_CRYPTO_RESULT_SUCCESS, actual);
    }

    @Test
    public void testSaveUserName_Case06() throws Exception {
        String userName = "BTU00002";
        PowerMockito.when(mBleHandleRegistrationResult, "saveUserInfo", userName).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(ParamTag.getLength(ParamTag.USER_NAME)).thenReturn(523);

        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBleHandleRegistrationResult, "getDeviceId").thenReturn(null);
        mBleHandleRegistrationResult.saveUserInfo(userName);

        int actual = mBleHandleRegistrationResult.saveUserInfo(userName);
        assertEquals(ParamValue.Security.RegistrationResult.OTHER_ERROR, actual);
    }

    @Test
    public void testOnRegistrationResponseNotified_Case01() throws Exception {
        PowerMockito.when(mBleHandleRegistrationResult, "onRegistrationResponseNotified", (Object) null).thenCallRealMethod();

        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);
        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(mNativeLibController.deleteRegistrationKey()).thenAnswer(voidAnswer);

        mBleHandleRegistrationResult.onRegistrationResponseNotified(null);

        verify(mBleHandleRegistrationResult, atLeastOnce()).onRegistrationResponseNotified(null);
    }

    @Test
    public void testOnRegistrationResponseNotified_Case02() throws Exception {
        List<VehicleParam> paramsList = mock(List.class);
        PowerMockito.when(paramsList.size()).thenReturn(0);
        PowerMockito.when(mBleHandleRegistrationResult, "onRegistrationResponseNotified", paramsList).thenCallRealMethod();

        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);
        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(mNativeLibController.deleteRegistrationKey()).thenAnswer(voidAnswer);

        mBleHandleRegistrationResult.onRegistrationResponseNotified(paramsList);

        verify(mBleHandleRegistrationResult, atLeastOnce()).onRegistrationResponseNotified(paramsList);
    }

    @Test
    public void testOnRegistrationResponseNotified_Case03() throws Exception {
        List<VehicleParam> paramsList = mock(List.class);
        PowerMockito.when(paramsList.size()).thenReturn(1);
        VehicleRegistrationResponseMessage message = mock(VehicleRegistrationResponseMessage.class);

        PowerMockito.when(mBleHandleRegistrationResult, "onRegistrationResponseNotified", paramsList).thenCallRealMethod();
        PowerMockito.whenNew(VehicleRegistrationResponseMessage.class).withParameterTypes(List.class).withArguments(paramsList).thenReturn(message);
        PowerMockito.when(message.getRegistrationType()).thenReturn(VehicleRegistrationType.VEHICLE_REGISTRATION_TYPE_REGISTER);
        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationResultMessage", message).thenAnswer(voidAnswer);

        mBleHandleRegistrationResult.onRegistrationResponseNotified(paramsList);

        verify(mBleHandleRegistrationResult, atLeastOnce()).onRegistrationResponseNotified(paramsList);
    }

    @Test
    public void testOnRegistrationResponseNotified_Case04() throws Exception {
        List<VehicleParam> paramsList = mock(List.class);
        PowerMockito.when(paramsList.size()).thenReturn(1);
        VehicleRegistrationResponseMessage message = mock(VehicleRegistrationResponseMessage.class);

        PowerMockito.when(mBleHandleRegistrationResult, "onRegistrationResponseNotified", paramsList).thenCallRealMethod();
        PowerMockito.whenNew(VehicleRegistrationResponseMessage.class).withParameterTypes(List.class).withArguments(paramsList).thenReturn(message);
        PowerMockito.when(message.getRegistrationType()).thenReturn(VehicleRegistrationType.VEHICLE_REGISTRATION_TYPE_DEREGISTER);
        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationDeregisterResultMessage", message).thenAnswer(voidAnswer);

        mBleHandleRegistrationResult.onRegistrationResponseNotified(paramsList);

        verify(mBleHandleRegistrationResult, atLeastOnce()).onRegistrationResponseNotified(paramsList);
    }

    @Test
    public void testOnRegistrationResponseNotified_Case05() throws Exception {
        List<VehicleParam> paramsList = mock(List.class);
        PowerMockito.when(paramsList.size()).thenReturn(1);
        VehicleRegistrationResponseMessage message = mock(VehicleRegistrationResponseMessage.class);

        PowerMockito.when(mBleHandleRegistrationResult, "onRegistrationResponseNotified", paramsList).thenCallRealMethod();
        PowerMockito.whenNew(VehicleRegistrationResponseMessage.class).withParameterTypes(List.class).withArguments(paramsList).thenReturn(message);
        PowerMockito.when(message.getRegistrationType()).thenReturn(VehicleRegistrationType.VEHICLE_REGISTRATION_TYPE_RE_REGISTER);

        mBleHandleRegistrationResult.onRegistrationResponseNotified(paramsList);

        verify(mBleHandleRegistrationResult, atLeastOnce()).onRegistrationResponseNotified(paramsList);
    }

    @Test
    public void testOnRegistrationResponseNotified_Case06() throws Exception {
        List<VehicleParam> paramsList = mock(List.class);
        PowerMockito.when(paramsList.size()).thenReturn(1);
        VehicleRegistrationResponseMessage message = mock(VehicleRegistrationResponseMessage.class);

        PowerMockito.whenNew(VehicleRegistrationResponseMessage.class).withParameterTypes(List.class).withArguments(paramsList).thenReturn(message);
        PowerMockito.when(message.getRegistrationType()).thenReturn(0x04);
        Whitebox.setInternalState(mBleHandleRegistrationResult, "mBleRegisterController", mBleRegisterController);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);
        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(mNativeLibController.deleteRegistrationKey()).thenAnswer(voidAnswer);

        mBleHandleRegistrationResult.onRegistrationResponseNotified(paramsList);

        verify(mBleHandleRegistrationResult, atLeastOnce()).onRegistrationResponseNotified(paramsList);
    }

    @Test
    public void testExtractRegistrationDeregisterResultMessage_Case01() throws Exception {
        List<String> userNameList = mock(List.class);
        int result = ParamValue.Security.RegistrationResult.REDIRECT;
        int cause = ParamValue.Security.ResultCause.USER_NAME_UNKNOWN;

        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage).thenCallRealMethod();

        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResult()).thenReturn(result);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResultCause()).thenReturn(cause);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getUserNameList()).thenReturn(userNameList);

        Whitebox.invokeMethod(mBleHandleRegistrationResult, "extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage);

        PowerMockito.verifyPrivate(mBleHandleRegistrationResult, atLeastOnce()).invoke("extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage);

    }

    @Test
    public void testExtractRegistrationDeregisterResultMessage_Case02() throws Exception {
        int result = ParamValue.Security.RegistrationResult.SUCCESS;
        int cause = ParamValue.Security.ResultCause.UNSPECIFIED;

        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage).thenCallRealMethod();

        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResult()).thenReturn(result);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResultCause()).thenReturn(cause);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getUserNameList()).thenReturn(null);

        Whitebox.setInternalState(mBleHandleRegistrationResult, "mRegistrationEventListener", mIF_VehicleRegisterListener);
        PowerMockito.when(mIF_VehicleRegisterListener, "onUnRegisterResult").thenAnswer(voidAnswer);

        Whitebox.invokeMethod(mBleHandleRegistrationResult, "extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage);

        PowerMockito.verifyPrivate(mBleHandleRegistrationResult, atLeastOnce()).invoke("extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage);

    }

    @Test
    public void testExtractRegistrationDeregisterResultMessage_Case03() throws Exception {
        List<String> userNameList = mock(List.class);
        String userName = "BTU00001";
        String receivedName = "BTU00002";
        int result = ParamValue.Security.RegistrationResult.SUCCESS;
        int cause = ParamValue.Security.ResultCause.UNSPECIFIED;

        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage).thenCallRealMethod();

        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResult()).thenReturn(result);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResultCause()).thenReturn(cause);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getUserNameList()).thenReturn(userNameList);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);

        Iterator iterator = mock(Iterator.class);
        when(userNameList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(receivedName);
        PowerMockito.when(TextUtils.equals(receivedName, userName)).thenReturn(false);

        Whitebox.invokeMethod(mBleHandleRegistrationResult, "extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage);

        PowerMockito.verifyPrivate(mBleHandleRegistrationResult, atLeastOnce()).invoke("extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage);

    }

    @Test
    public void testExtractRegistrationDeregisterResultMessage_Case04() throws Exception {
        List<String> userNameList = mock(List.class);
        String userName = "BTU00001";
        String receivedName = "BTU00002";
        int result = ParamValue.Security.RegistrationResult.SUCCESS;
        int cause = ParamValue.Security.ResultCause.UNSPECIFIED;

        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage).thenCallRealMethod();

        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResult()).thenReturn(result);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResultCause()).thenReturn(cause);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getUserNameList()).thenReturn(userNameList);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);

        Iterator iterator = mock(Iterator.class);
        when(userNameList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(receivedName);
        PowerMockito.when(TextUtils.equals(receivedName, userName)).thenReturn(true);

        Whitebox.invokeMethod(mBleHandleRegistrationResult, "extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage);

        PowerMockito.verifyPrivate(mBleHandleRegistrationResult, atLeastOnce()).invoke("extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage);

    }

    @Test
    public void testExtractRegistrationDeregisterResultMessage_Case05() throws Exception {
        List<String> userNameList = mock(List.class);
        String userName = "BTU00001";
        String receivedName = "BTU00001";
        int result = ParamValue.Security.RegistrationResult.SUCCESS;
        int cause = ParamValue.Security.ResultCause.UNSPECIFIED;

        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage).thenCallRealMethod();

        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResult()).thenReturn(result);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getRegistrationResultCause()).thenReturn(cause);
        PowerMockito.when(mVehicleRegistrationResponseMessage.getUserNameList()).thenReturn(userNameList);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);

        Iterator iterator = mock(Iterator.class);
        when(userNameList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(receivedName);
        PowerMockito.when(TextUtils.equals(receivedName, userName)).thenReturn(true);

        Whitebox.invokeMethod(mBleHandleRegistrationResult, "extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage);

        PowerMockito.verifyPrivate(mBleHandleRegistrationResult, atLeastOnce()).invoke("extractRegistrationDeregisterResultMessage", mVehicleRegistrationResponseMessage);
    }
}
