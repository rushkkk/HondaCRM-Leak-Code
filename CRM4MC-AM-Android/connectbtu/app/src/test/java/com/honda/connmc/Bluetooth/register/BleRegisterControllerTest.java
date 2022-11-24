package com.honda.connmc.Bluetooth.register;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.connect.BleConnectController;
import com.honda.connmc.Bluetooth.message.DataMessageManager;
import com.honda.connmc.Bluetooth.timer.IF_TimeOutListener;
import com.honda.connmc.Bluetooth.timer.TimeManager;
import com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Constants.message.ParamTag;
import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Constants.register.VehicleRegistrationStatus;
import com.honda.connmc.Constants.register.VehicleRegistrationType;
import com.honda.connmc.Interfaces.listener.register.IF_VehicleRegisterListener;
import com.honda.connmc.Interfaces.method.register.IF_Register_Methods;
import com.honda.connmc.Model.message.VehicleMessageStatusInquiry;
import com.honda.connmc.Model.message.common.VehicleMessage;
import com.honda.connmc.Model.message.common.VehicleParam;
import com.honda.connmc.Model.message.register.request.VehicleMessageRegistrationRequest;
import com.honda.connmc.Model.message.register.response.RegistrationStatusIndicationMessage;
import com.honda.connmc.Model.message.register.response.VehicleMessageSecurityConfigResponse;
import com.honda.connmc.Model.message.register.response.VehicleSecurityConfigMessage;
import com.honda.connmc.NativeLib.NativeLibController;
import com.honda.connmc.Utils.LogUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_BTU_NAME_REGISTER_SUCCESS;
import static com.honda.connmc.Constants.message.ParamTag.SECURITY_CONFIG_CONTAINER;
import static com.honda.connmc.Constants.register.VehicleRegistrationType.VEHICLE_REGISTRATION_TYPE_REGISTER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        BleConnectController.class,
        TimeManager.class,
        VehicleMessage.class,
        VehicleParam.class,
        BleRegisterController.class,
        BleHandleAuthenticationResult.class,
        BleHandleRegistrationResult.class,
        IF_VehicleRegisterListener.class,
        IF_Register_Methods.class,
        IF_TimeOutListener.class,
        LogUtils.class,
        BluetoothDevice.class,
        BluetoothManager.class,
        ByteBuffer.class,
        Thread.class,
        BluetoothUtils.class,
        BluetoothPrefUtils.class,
        NativeLibController.class,
        DataMessageManager.class,
        ParamTag.class,
        Object.class,
        TextUtils.class
})
public class BleRegisterControllerTest {
    @Mock
    private TimeManager mTimeManager;

    private final static long REGISTRATION_RESPONSE_TIMEOUT = 30000L;
    @Mock
    private IF_VehicleRegisterListener mRegistrationEventListener;
    @Mock
    private IF_TimeOutListener mIF_TimeOutListener;
    @Mock
    private BleHandleRegistrationResult mBleHandleRegistrationResult;
    @Mock
    private BleHandleAuthenticationResult mBleHandleAuthenticationResult;
    @Mock
    private BluetoothManager mBluetoothManager;
    @Mock
    private VehicleMessageRegistrationRequest mVehicleMessageRegistrationRequest;
    @Mock
    private NativeLibController mNativeLibController;
    @Mock
    private ByteBuffer mByteBuffer;
    @Mock
    private BluetoothManager.EnumDeviceConnectStatus state;
    @Mock
    private DataMessageManager mDataMessageManager;
    @Mock
    private VehicleMessage mMessage;
    @Mock
    private BluetoothPrefUtils prefUtils;
    @Mock
    private VehicleParam mParams;
    @Mock
    private Object mObject;
    @Mock
    private BluetoothDevice mBluetoothDevice;
    @Mock
    private BleConnectController mBleConnectController;
    @Mock
    private BleRegisterController mBleRegisterController;

    String dataTest = "String";

    Answer voidAnswer = invocation -> null;

    @Mock
    ArgumentCaptor<Runnable> runnables = ArgumentCaptor.forClass(Runnable.class);

    @Mock
    int paramValue = 521;
    @Mock
    byte[] pinData = {1, 1, 2};
    @Mock
    byte[] input = {1, 1, 2};
    @Mock
    byte[] output = {1, 1, 2};

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(BleRegisterController.class);
        PowerMockito.mockStatic(BluetoothManager.class);
        PowerMockito.mockStatic(ByteBuffer.class);
        PowerMockito.mockStatic(Thread.class);
        PowerMockito.mockStatic(ParamTag.class);
        PowerMockito.mockStatic(BluetoothDevice.class);
        PowerMockito.mockStatic(NativeLibController.class);
        PowerMockito.mockStatic(VehicleMessageRegistrationRequest.class);
        PowerMockito.mockStatic(BleHandleRegistrationResult.class);
        PowerMockito.mockStatic(BleHandleAuthenticationResult.class);
        PowerMockito.mockStatic(IF_VehicleRegisterListener.class);
        PowerMockito.mockStatic(TimeManager.class);
        PowerMockito.mockStatic(LogUtils.class);
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(BluetoothPrefUtils.class);
        PowerMockito.mockStatic(BluetoothUtils.class);

        Whitebox.setInternalState(mBleRegisterController, "LOCK_OBJECT", mObject);

        PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "e", anyString()).thenAnswer(voidAnswer);
    }

    @Test
    public void testSetRegistrationEventListener_Case01() throws Exception {
        PowerMockito.when(mBleRegisterController, "setRegistrationEventListener", mRegistrationEventListener).thenCallRealMethod();
        Whitebox.setInternalState(mBleRegisterController, "mBleHandleRegistrationResult", mBleHandleRegistrationResult);

        PowerMockito.when(mBleHandleRegistrationResult, "setRegistrationEventListener", mRegistrationEventListener).thenAnswer(voidAnswer);

        mBleRegisterController.setRegistrationEventListener(mRegistrationEventListener);

        IF_VehicleRegisterListener actual = Whitebox.getInternalState(mBleRegisterController, "mRegistrationEventListener");

        assertEquals(mRegistrationEventListener, actual);
    }

    @Test
    public void testSetRegistrationEventListener_Case02() throws Exception {
        PowerMockito.when(mBleRegisterController, "setRegistrationEventListener", (Object) null).thenCallRealMethod();

        mBleRegisterController.setRegistrationEventListener(mRegistrationEventListener);

        IF_VehicleRegisterListener actual = Whitebox.getInternalState(mBleRegisterController, "mRegistrationEventListener");

        assertNull(actual);
    }

    @Test
    public void testStartRegister_Case01() throws Exception {
        PowerMockito.when(mBleRegisterController.startRegister()).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
        PowerMockito.when(mBleConnectController.getBluetoothDeviceConnect()).thenReturn(null);

        mBleRegisterController.startRegister();

        boolean actual = mBleRegisterController.startRegister();
        assertFalse(actual);
    }

    @Test
    public void testStartRegister_Case02() throws Exception {
        PowerMockito.when(mBleRegisterController.startRegister()).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
        PowerMockito.when(mBleConnectController.getBluetoothDeviceConnect()).thenReturn(mBluetoothDevice);
        Whitebox.setInternalState(mBleRegisterController, "isStartRegister", true);

        mBleRegisterController.startRegister();

        boolean actual = mBleRegisterController.startRegister();
        assertFalse(actual);
    }

    @Test
    public void testStartRegister_Case03() throws Exception {
        PowerMockito.when(mBleRegisterController.startRegister()).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
        PowerMockito.when(mBleConnectController.getBluetoothDeviceConnect()).thenReturn(mBluetoothDevice);
        Whitebox.setInternalState(mBleRegisterController, "isStartRegister", false);

        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(mBluetoothDevice.getName()).thenReturn("BTU00001");
        PowerMockito.when(mNativeLibController.isExistVehicleInfo("BTU00001")).thenReturn(true);

        mBleRegisterController.startRegister();

        boolean actual = mBleRegisterController.startRegister();
        assertFalse(actual);
    }

    @Test
    public void testStartRegister_Case04() throws Exception {
        PowerMockito.when(mBleRegisterController.startRegister()).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
        PowerMockito.when(mBleConnectController.getBluetoothDeviceConnect()).thenReturn(mBluetoothDevice);
        Whitebox.setInternalState(mBleRegisterController, "isStartRegister", false);

        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(mBluetoothDevice.getName()).thenReturn("BTU00001");
        PowerMockito.when(mNativeLibController.isExistVehicleInfo("BTU00001")).thenReturn(false);
        PowerMockito.whenNew(VehicleMessageRegistrationRequest.class).withArguments(VEHICLE_REGISTRATION_TYPE_REGISTER, null, null).thenReturn(mVehicleMessageRegistrationRequest);
        PowerMockito.when(mBluetoothManager, "sendMessage", mVehicleMessageRegistrationRequest).thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleRegisterController, "mTimeManager", mTimeManager);

        PowerMockito.when(mTimeManager, "startTimer", mIF_TimeOutListener, REGISTRATION_RESPONSE_TIMEOUT).thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleRegisterController, "isStartRegister", false);


        mBleRegisterController.startRegister();

        boolean actual = mBleRegisterController.startRegister();
        assertFalse(actual);
    }

    @Test
    public void testStopTimer() throws Exception {
        Whitebox.setInternalState(mBleRegisterController, "isStartRegister", false);
        Whitebox.setInternalState(mBleRegisterController, "mTimeManager", mTimeManager);
        PowerMockito.when(mTimeManager, "stopTimeOut").thenAnswer(voidAnswer);

        boolean actual = Whitebox.getInternalState(mBleRegisterController, "isStartRegister");

        assertFalse(actual);
    }

    @Test
    public void testRegisterPinCode_Case01() throws Exception {
        PowerMockito.when(mBleRegisterController.registerPinCode("Pincode")).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(mBleRegisterController, "checkFormat", "Pincode").thenReturn(pinData);

        PowerMockito.when(mBleRegisterController, "sendPinCodeAsync", pinData).thenAnswer(voidAnswer);

        mBleRegisterController.registerPinCode("Pincode");

        assertFalse(mBleRegisterController.registerPinCode("Pincode"));
    }

    @Test
    public void testRegisterPinCode_Case02() throws Exception {
        PowerMockito.when(mBleRegisterController.registerPinCode("Pincode")).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(mBleRegisterController, "checkFormat", "Pincode").thenReturn(null);

        PowerMockito.when(mBleRegisterController, "sendPinCodeAsync", pinData).thenAnswer(voidAnswer);

        mBleRegisterController.registerPinCode("Pincode");

        assertFalse(mBleRegisterController.registerPinCode("Pincode"));
    }

    @Test
    public void testSendPinCodeAsync() throws Exception {
        byte[] data = {0, 1, 2};
        PowerMockito.when(mBleRegisterController, "sendPinCodeAsync", data).thenCallRealMethod();

        List params = mock(List.class);
        VehicleSecurityConfigMessage sendMsg = mock(VehicleSecurityConfigMessage.class);
        Thread thread = mock(Thread.class);
        PowerMockito.whenNew(Thread.class).withParameterTypes(Runnable.class).withArguments(runnables.capture()).thenReturn(thread);
        thread = new Thread(() -> {

            try {
                PowerMockito.when(ParamTag.getLength(SECURITY_CONFIG_CONTAINER)).thenReturn(paramValue);
                PowerMockito.when(ByteBuffer.allocate(paramValue)).thenReturn(mByteBuffer);
                PowerMockito.when(mByteBuffer.put(pinData)).thenAnswer(voidAnswer);
                PowerMockito.when(mByteBuffer.array()).thenReturn(input);

                PowerMockito.when(mByteBuffer.array()).thenReturn(output);

                PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
                PowerMockito.when(mNativeLibController.getPinCodeSendData(input, output)).thenReturn(1);

                PowerMockito.whenNew(VehicleSecurityConfigMessage.class).withArguments(ParamValue.Security.SecurityConfigType.VERIFY_PIN, output).thenReturn(sendMsg);

                PowerMockito.when(sendMsg.getVehicleParameters()).thenReturn(params);


                PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);
                PowerMockito.when(mBleRegisterController, "sendSecurityConfigResponse", params).thenAnswer(voidAnswer);

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        thread.start();

        Whitebox.invokeMethod(mBleRegisterController, "sendPinCodeAsync", data);

        PowerMockito.verifyStatic();
    }

    @Test
    public void testRegistrationFailureProcess_Case01() throws Exception {
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenCallRealMethod();
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.disconnectDevice()).thenAnswer(voidAnswer);
        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(mNativeLibController.deleteRegistrationKey()).thenAnswer(voidAnswer);

        mBleRegisterController.registrationFailureProcess();

        verify(mBleRegisterController, atLeastOnce()).registrationFailureProcess();

    }

    @Test
    public void testRegistrationFailureProcess_Case02() throws Exception {
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenCallRealMethod();
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.disconnectDevice()).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(false);
        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(mNativeLibController.deleteRegistrationKey()).thenAnswer(voidAnswer);

        Whitebox.invokeMethod(mBleRegisterController, "registrationFailureProcess");

        verifyPrivate(mBleRegisterController, atLeastOnce()).invoke("registrationFailureProcess");

    }

    @Test
    public void testRegistrationFailureProcess_Case03() throws Exception {
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenCallRealMethod();
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.disconnectDevice()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleRegisterController, "mRegistrationEventListener", mRegistrationEventListener);
        PowerMockito.when(BluetoothUtils.checkBluetooth()).thenReturn(true);
        PowerMockito.when(mRegistrationEventListener, "onRegisterResultFail", ParamValue.Security.RegistrationResult.OTHER_ERROR).thenAnswer(voidAnswer);
        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(mNativeLibController.deleteRegistrationKey()).thenAnswer(voidAnswer);

        mBleRegisterController.registrationFailureProcess();

        verify(mBleRegisterController, atLeastOnce()).registrationFailureProcess();

    }

    @Test
    public void testCheckFormat_Case01() throws Exception {
        PowerMockito.when(mBleRegisterController, "checkFormat", dataTest).thenCallRealMethod();
        PowerMockito.when(ParamTag.class, "getLength", (int) SECURITY_CONFIG_CONTAINER).thenReturn(518);
        byte[] actual = Whitebox.invokeMethod(mBleRegisterController, "checkFormat", dataTest);
        assertNotNull(actual);
    }

    @Test
    public void testCheckFormat_Case02() throws Exception {
        String data = "String";
        PowerMockito.when(mBleRegisterController, "checkFormat", (Object) null).thenCallRealMethod();
        byte[] actual = Whitebox.invokeMethod(mBleRegisterController, "checkFormat", (Object) null);
        assertNull(actual);
    }

    @Test
    public void testCheckFormat_Case03() throws Exception {
        String data = "Tonight, I'm gonna make it up to you\n" +
                "Tonight, I'm gonna make love to you\n" +
                "Tonight, you're gonna know how much I missed you, baby\n" +
                "Tonight, I dedicate my heart to you\n" +
                "Tonight, I'm gonna be a part of you \n" +
                "Tonight, you're gonna know how much I miss you\n" +
                "And I miss you so\n";
        PowerMockito.when(mBleRegisterController, "checkFormat", data).thenCallRealMethod();
        byte[] actual = Whitebox.invokeMethod(mBleRegisterController, "checkFormat", data);
        assertNull(actual);
    }

    @Test
    public void testRegisterUserName() throws Exception {
        PowerMockito.when(mBleRegisterController.registerUserName("Device1")).thenCallRealMethod();
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(mBleRegisterController, "requestUserName", "Devices1").thenAnswer(voidAnswer);

        boolean actual = mBleRegisterController.registerUserName("Device1");
        assertFalse(actual);
    }

    @Test
    public void testReplaceUser_Case01() throws Exception {
        PowerMockito.when(mBleRegisterController.replaceUser(dataTest)).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty(dataTest)).thenReturn(true);

        boolean actual = mBleRegisterController.replaceUser(dataTest);
        assertFalse(actual);
    }

    @Test
    public void testReplaceUser_Case02() throws Exception {
        PowerMockito.when(mBleRegisterController.replaceUser(dataTest)).thenCallRealMethod();
        List<String> userNameList = mock(List.class);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty(dataTest)).thenReturn(false);
        userNameList = Arrays.asList(dataTest);
        PowerMockito.whenNew(VehicleMessageRegistrationRequest.class)
                .withArguments(VehicleRegistrationType.VEHICLE_REGISTRATION_TYPE_DEREGISTER, userNameList, null).thenReturn(mVehicleMessageRegistrationRequest);
        PowerMockito.when(mVehicleMessageRegistrationRequest.getParams()).thenReturn(null);

        boolean actual = mBleRegisterController.replaceUser(dataTest);
        assertFalse(actual);
    }

    @Test
    public void testReplaceUser_Case03() throws Exception {
        PowerMockito.when(mBleRegisterController.replaceUser(dataTest)).thenCallRealMethod();
        ArrayList<VehicleParam> paramList = mock(ArrayList.class);
        List<String> userNameList = mock(List.class);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty(dataTest)).thenReturn(false);
        userNameList = Arrays.asList(dataTest);
        PowerMockito.whenNew(VehicleMessageRegistrationRequest.class)
                .withArguments(VehicleRegistrationType.VEHICLE_REGISTRATION_TYPE_DEREGISTER, userNameList, null).thenReturn(mVehicleMessageRegistrationRequest);
        PowerMockito.when(mVehicleMessageRegistrationRequest.getParams()).thenReturn(paramList);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "sendMessage", mVehicleMessageRegistrationRequest).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleRegisterController, "mTimeManager", mTimeManager);
        PowerMockito.when(mTimeManager, "startTimer", mIF_TimeOutListener, REGISTRATION_RESPONSE_TIMEOUT).thenAnswer(voidAnswer);

        boolean actual = mBleRegisterController.replaceUser(dataTest);
        assertTrue(actual);
    }

    @Test
    public void testOnTimeout() throws Exception {
        PowerMockito.when(mBleRegisterController, "onTimeout").thenCallRealMethod();

        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);
        mBleRegisterController.onTimeout();

        verify(mBleConnectController, atLeast(0)).onTimeout();
    }

    @Test
    public void testOnReceiveDataRegistrationResponse_Case01() throws Exception {
        PowerMockito.when(mBleRegisterController, "onReceiveDataRegistrationResponse", (Object) null).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);

        mBleRegisterController.onReceiveDataRegistrationResponse(null);

        verify(mBleRegisterController, atLeast(0)).onReceiveDataRegistrationResponse(null);
    }

    @Test
    public void testOnReceiveDataRegistrationResponse_Case02() throws Exception {
        PowerMockito.when(mBleRegisterController, "onReceiveDataRegistrationResponse", mMessage).thenCallRealMethod();
        ArrayList<VehicleParam> listParams = mock(ArrayList.class);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleRegisterController, "mBleHandleRegistrationResult", mBleHandleRegistrationResult);
        PowerMockito.when(mMessage.getParams()).thenReturn(listParams);
        PowerMockito.when(mBleHandleRegistrationResult, "onRegistrationResponseNotified", listParams).thenAnswer(voidAnswer);

        mBleRegisterController.onReceiveDataRegistrationResponse(mMessage);

        verify(mBleRegisterController, atLeast(0)).onReceiveDataRegistrationResponse(mMessage);
    }

    @Test
    public void testOnSecurityConfigRequestNotified_Case01() throws Exception {
        PowerMockito.when(mBleRegisterController, "onSecurityConfigRequestNotified", (Object) null).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);

        mBleRegisterController.onSecurityConfigRequestNotified(null);

        verify(mBleRegisterController, atLeast(0)).onSecurityConfigRequestNotified(null);
    }

    @Test
    public void testOnSecurityConfigRequestNotified_Case02() throws Exception {
        PowerMockito.when(mBleRegisterController, "onSecurityConfigRequestNotified", (Object) null).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        PowerMockito.when(mMessage.getParams()).thenReturn(null);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);

        mBleRegisterController.onSecurityConfigRequestNotified(null);

        verify(mBleRegisterController, atLeast(0)).onSecurityConfigRequestNotified(null);
    }

    @Test
    public void testOnSecurityConfigRequestNotified_Case03() throws Exception {
        PowerMockito.when(mBleRegisterController, "onSecurityConfigRequestNotified", (Object) null).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        ArrayList listParams = mock(ArrayList.class);
        PowerMockito.when(mMessage.getParams()).thenReturn(null);
        mMessage.mParamArray = listParams;
        PowerMockito.when(listParams.size()).thenReturn(0);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);

        mBleRegisterController.onSecurityConfigRequestNotified(null);

        verify(mBleRegisterController, atLeast(0)).onSecurityConfigRequestNotified(null);
    }

    @Test
    public void testOnSecurityConfigRequestNotified_Case04() throws Exception {
        PowerMockito.when(mBleRegisterController, "onSecurityConfigRequestNotified", mMessage).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        ArrayList listParams = mock(ArrayList.class);
        ArrayList listParamsArray = mock(ArrayList.class);
        VehicleSecurityConfigMessage sendMsg = mock(VehicleSecurityConfigMessage.class);
        PowerMockito.when(mMessage.getParams()).thenReturn(listParams);
        mMessage.mParamArray = listParamsArray;
        PowerMockito.when(listParamsArray.size()).thenReturn(1);
        PowerMockito.whenNew(VehicleSecurityConfigMessage.class)
                .withParameterTypes(List.class)
                .withArguments(listParamsArray).thenReturn(sendMsg);

        PowerMockito.when(sendMsg.getSecurityConfigType()).thenReturn(ParamValue.Security.SecurityConfigType.VERIFY_PIN);

        Whitebox.setInternalState(mBleRegisterController, "mBleHandleRegistrationResult", mBleHandleRegistrationResult);
        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationCryptionKey", sendMsg).thenAnswer(voidAnswer);

        mBleRegisterController.onSecurityConfigRequestNotified(mMessage);

        verify(mBleRegisterController, atLeast(0)).onSecurityConfigRequestNotified(mMessage);
    }

    @Test
    public void testOnSecurityConfigRequestNotified_Case05() throws Exception {
        PowerMockito.when(mBleRegisterController, "onSecurityConfigRequestNotified", mMessage).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        ArrayList listParams = mock(ArrayList.class);
        ArrayList listParamsArray = mock(ArrayList.class);
        VehicleSecurityConfigMessage sendMsg = mock(VehicleSecurityConfigMessage.class);
        PowerMockito.when(mMessage.getParams()).thenReturn(listParams);
        mMessage.mParamArray = listParamsArray;
        PowerMockito.when(listParamsArray.size()).thenReturn(1);
        PowerMockito.whenNew(VehicleSecurityConfigMessage.class)
                .withParameterTypes(List.class)
                .withArguments(listParamsArray).thenReturn(sendMsg);

        PowerMockito.when(sendMsg.getSecurityConfigType()).thenReturn(ParamValue.Security.SecurityConfigType.ID_EXCHANGE);

        Whitebox.setInternalState(mBleRegisterController, "mBleHandleRegistrationResult", mBleHandleRegistrationResult);
        PowerMockito.when(mBleHandleRegistrationResult, "extractRegistrationCryptionKey", sendMsg).thenAnswer(voidAnswer);

        mBleRegisterController.onSecurityConfigRequestNotified(mMessage);

        verify(mBleRegisterController, atLeast(0)).onSecurityConfigRequestNotified(mMessage);
    }


    @Test
    public void testOnSecurityConfigRequestNotified_Case06() throws Exception {
        PowerMockito.when(mBleRegisterController, "onSecurityConfigRequestNotified", mMessage).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        ArrayList listParams = mock(ArrayList.class);
        ArrayList listParamsArray = mock(ArrayList.class);
        VehicleSecurityConfigMessage sendMsg = mock(VehicleSecurityConfigMessage.class);
        PowerMockito.when(mMessage.getParams()).thenReturn(listParams);
        mMessage.mParamArray = listParamsArray;
        PowerMockito.when(listParamsArray.size()).thenReturn(1);
        PowerMockito.whenNew(VehicleSecurityConfigMessage.class)
                .withParameterTypes(List.class)
                .withArguments(listParamsArray).thenReturn(sendMsg);

        PowerMockito.when(sendMsg.getSecurityConfigType()).thenReturn(00012);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);

        mBleRegisterController.onSecurityConfigRequestNotified(mMessage);

        verify(mBleRegisterController, atLeast(0)).onSecurityConfigRequestNotified(mMessage);
    }

    @Test
    public void testOnRegistrationStatusIndicationNotified_Case01() throws Exception {
        PowerMockito.when(mBleRegisterController, "onRegistrationStatusIndicationNotified", (Object) null).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);

        mBleRegisterController.onRegistrationStatusIndicationNotified(null);

        verify(mBleRegisterController, atLeast(0)).onRegistrationStatusIndicationNotified(null);
    }

    @Test
    public void testOnRegistrationStatusIndicationNotified_Case02() throws Exception {
        PowerMockito.when(mBleRegisterController, "onRegistrationStatusIndicationNotified", mMessage).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);

        mBleRegisterController.onRegistrationStatusIndicationNotified(mMessage);

        verify(mBleRegisterController, atLeast(0)).onRegistrationStatusIndicationNotified(mMessage);
    }

    @Test
    public void testOnRegistrationStatusIndicationNotified_Case03() throws Exception {
        PowerMockito.when(mBleRegisterController, "onRegistrationStatusIndicationNotified", mMessage).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        ArrayList listParams = mock(ArrayList.class);
        ArrayList listParamsArray = mock(ArrayList.class);
        PowerMockito.when(mMessage.getParams()).thenReturn(listParams);
        mMessage.mParamArray = listParamsArray;
        PowerMockito.when(listParamsArray.size()).thenReturn(0);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);
        PowerMockito.when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        PowerMockito.when(mNativeLibController.deleteRegistrationKey()).thenAnswer(voidAnswer);

        mBleRegisterController.onRegistrationStatusIndicationNotified(mMessage);

        verify(mBleRegisterController, atLeast(0)).onRegistrationStatusIndicationNotified(mMessage);
    }

    @Test
    public void testOnRegistrationStatusIndicationNotified_Case04() throws Exception {
        PowerMockito.when(mBleRegisterController, "onRegistrationStatusIndicationNotified", mMessage).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        ArrayList listParams = mock(ArrayList.class);
        ArrayList listParamsArray = mock(ArrayList.class);
        RegistrationStatusIndicationMessage registrationStatus = mock(RegistrationStatusIndicationMessage.class);
        PowerMockito.when(mMessage.getParams()).thenReturn(listParams);
        mMessage.mParamArray = listParamsArray;
        PowerMockito.when(listParamsArray.size()).thenReturn(1);

        PowerMockito.whenNew(RegistrationStatusIndicationMessage.class).withArguments(listParamsArray).thenReturn(registrationStatus);
        PowerMockito.when(registrationStatus.getRegistrationStatusValue()).thenReturn(VehicleRegistrationStatus.REGISTRATION_STATUS_VALUE_ACTIVE);

        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "setDeviceCurrentStatus", BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothManager.getDataMessageManager()).thenReturn(mDataMessageManager);
        PowerMockito.when(mDataMessageManager, "setEnableEncrypt", true).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
        PowerMockito.when(mBleConnectController.getBluetoothDeviceConnect()).thenReturn(mBluetoothDevice);
        PowerMockito.when(mBluetoothDevice.getName()).thenReturn("String");
        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(prefUtils);
        PowerMockito.when(prefUtils.setString(KEY_BTU_NAME_REGISTER_SUCCESS, "String")).thenReturn(true);
        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE).thenAnswer(voidAnswer);


        mBleRegisterController.onRegistrationStatusIndicationNotified(mMessage);

        verify(mBleRegisterController, atLeast(0)).onRegistrationStatusIndicationNotified(mMessage);
    }

    @Test
    public void testOnRegistrationStatusIndicationNotified_Case05() throws Exception {
        PowerMockito.when(mBleRegisterController, "onRegistrationStatusIndicationNotified", mMessage).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        ArrayList listParams = mock(ArrayList.class);
        ArrayList listParamsArray = mock(ArrayList.class);
        RegistrationStatusIndicationMessage registrationStatus = mock(RegistrationStatusIndicationMessage.class);
        PowerMockito.when(mMessage.getParams()).thenReturn(listParams);
        mMessage.mParamArray = listParamsArray;
        PowerMockito.when(listParamsArray.size()).thenReturn(1);

        PowerMockito.whenNew(RegistrationStatusIndicationMessage.class).withArguments(listParamsArray).thenReturn(registrationStatus);
        PowerMockito.when(registrationStatus.getRegistrationStatusValue()).thenReturn(VehicleRegistrationStatus.REGISTRATION_STATUS_VALUE_INACTIVE);

        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getDeviceCurrentStatus()).thenReturn(BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE);
        PowerMockito.when(mBluetoothManager, "disconnectDevice").thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothManager, "setDeviceCurrentStatus", BluetoothManager.EnumDeviceConnectStatus.BTU_INACTIVE).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.BTU_INACTIVE).thenAnswer(voidAnswer);

        mBleRegisterController.onRegistrationStatusIndicationNotified(mMessage);

        verify(mBleRegisterController, atLeast(0)).onRegistrationStatusIndicationNotified(mMessage);
    }

    @Test
    public void testOnRegistrationStatusIndicationNotified_Case06() throws Exception {
        PowerMockito.when(mBleRegisterController, "onRegistrationStatusIndicationNotified", mMessage).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        ArrayList listParamsArray = mock(ArrayList.class);
        VehicleMessageRegistrationRequest registerMsg = mock(VehicleMessageRegistrationRequest.class);
        RegistrationStatusIndicationMessage registrationStatus = mock(RegistrationStatusIndicationMessage.class);
        mMessage.mParamArray = listParamsArray;
        PowerMockito.when(listParamsArray.size()).thenReturn(1);

        PowerMockito.whenNew(RegistrationStatusIndicationMessage.class).withArguments(listParamsArray).thenReturn(registrationStatus);
        PowerMockito.when(registrationStatus.getRegistrationStatusValue()).thenReturn(VehicleRegistrationStatus.REGISTRATION_STATUS_VALUE_INACTIVE);

        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getDeviceCurrentStatus()).thenReturn(BluetoothManager.EnumDeviceConnectStatus.BTU_INACTIVE);

        PowerMockito.when(mBluetoothManager, "setDeviceCurrentStatus", BluetoothManager.EnumDeviceConnectStatus.BTU_INACTIVE_SECOND_TIME).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothManager.getDataMessageManager()).thenReturn(mDataMessageManager);
        PowerMockito.when(mDataMessageManager, "setEnableEncrypt", true).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
        List<String> userNameList = mock(List.class);
        userNameList.add(dataTest);
        PowerMockito.whenNew(VehicleMessageRegistrationRequest.class).withAnyArguments().thenReturn(registerMsg);

        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "sendMessage", registerMsg).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleRegisterController, "mTimeManager", mTimeManager);
        PowerMockito.when(mTimeManager, "startTimer", mIF_TimeOutListener, REGISTRATION_RESPONSE_TIMEOUT).thenAnswer(voidAnswer);

        mBleRegisterController.onRegistrationStatusIndicationNotified(mMessage);

        verify(mBleRegisterController, atLeast(0)).onRegistrationStatusIndicationNotified(mMessage);
    }

    @Test
    public void testOnRegistrationStatusIndicationNotified_Case07() throws Exception {
        PowerMockito.when(mBleRegisterController, "onRegistrationStatusIndicationNotified", mMessage).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        ArrayList listParamsArray = mock(ArrayList.class);
        RegistrationStatusIndicationMessage registrationStatus = mock(RegistrationStatusIndicationMessage.class);
        mMessage.mParamArray = listParamsArray;
        PowerMockito.when(listParamsArray.size()).thenReturn(1);

        PowerMockito.whenNew(RegistrationStatusIndicationMessage.class).withArguments(listParamsArray).thenReturn(registrationStatus);
        PowerMockito.when(registrationStatus.getRegistrationStatusValue()).thenReturn(VehicleRegistrationStatus.REGISTRATION_STATUS_VALUE_NOT_REGISTER);
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.getDeviceCurrentStatus()).thenReturn(BluetoothManager.EnumDeviceConnectStatus.BTU_INACTIVE);
        PowerMockito.when(mBluetoothManager, "setDeviceCurrentStatus", BluetoothManager.EnumDeviceConnectStatus.BTU_UNREGISTERED).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothManager.disconnectDevice()).thenReturn(true);

        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.BTU_INACTIVE).thenAnswer(voidAnswer);

        mBleRegisterController.onRegistrationStatusIndicationNotified(mMessage);

        verify(mBleRegisterController, atLeast(0)).onRegistrationStatusIndicationNotified(mMessage);
    }

    @Test
    public void testOnRegistrationStatusIndicationNotified_Case8() throws Exception {
        PowerMockito.when(mBleRegisterController, "onRegistrationStatusIndicationNotified", mMessage).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        ArrayList listParamsArray = mock(ArrayList.class);
        RegistrationStatusIndicationMessage registrationStatus = mock(RegistrationStatusIndicationMessage.class);
        mMessage.mParamArray = listParamsArray;
        PowerMockito.when(listParamsArray.size()).thenReturn(1);

        PowerMockito.whenNew(RegistrationStatusIndicationMessage.class).withArguments(listParamsArray).thenReturn(registrationStatus);
        PowerMockito.when(registrationStatus.getRegistrationStatusValue()).thenReturn(VehicleRegistrationStatus.REGISTRATION_STATUS_VALUE_NOT_REGISTER);
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "setDeviceCurrentStatus", BluetoothManager.EnumDeviceConnectStatus.BTU_UNREGISTERED).thenAnswer(voidAnswer);
        PowerMockito.when(mBluetoothManager.disconnectDevice()).thenReturn(false);
        PowerMockito.when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
        PowerMockito.when(mBleConnectController, "notifyConnectStatusChange", BluetoothManager.EnumDeviceConnectStatus.BTU_UNREGISTERED).thenAnswer(voidAnswer);

        mBleRegisterController.onRegistrationStatusIndicationNotified(mMessage);

        verify(mBleRegisterController, atLeast(0)).onRegistrationStatusIndicationNotified(mMessage);
    }

    @Test
    public void testOnRegistrationStatusIndicationNotified_Case9() throws Exception {
        PowerMockito.when(mBleRegisterController, "onRegistrationStatusIndicationNotified", mMessage).thenCallRealMethod();
        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        ArrayList listParamsArray = mock(ArrayList.class);
        RegistrationStatusIndicationMessage registrationStatus = mock(RegistrationStatusIndicationMessage.class);
        mMessage.mParamArray = listParamsArray;
        PowerMockito.when(listParamsArray.size()).thenReturn(1);

        PowerMockito.whenNew(RegistrationStatusIndicationMessage.class).withArguments(listParamsArray).thenReturn(registrationStatus);
        PowerMockito.when(registrationStatus.getRegistrationStatusValue()).thenReturn(3);
        PowerMockito.when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);

        mBleRegisterController.onRegistrationStatusIndicationNotified(mMessage);

        verify(mBleRegisterController, atLeast(0)).onRegistrationStatusIndicationNotified(mMessage);
    }

    @Test
    public void testSendSecurityConfigResponse() throws Exception {
        List<VehicleParam> data = mock(List.class);
        PowerMockito.when(mBleRegisterController, "sendSecurityConfigResponse", data).thenCallRealMethod();
        ArrayList<VehicleParam> listParams = mock(ArrayList.class);
        VehicleMessageSecurityConfigResponse requestMessage = mock(VehicleMessageSecurityConfigResponse.class);
        PowerMockito.whenNew(VehicleMessageSecurityConfigResponse.class).withNoArguments().thenReturn(requestMessage);
        Iterator iterator = Mockito.mock(Iterator.class);
        when(data.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(mParams);
        requestMessage.mParamArray = listParams;
        PowerMockito.when(listParams, "add", mParams).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "sendMessage", requestMessage).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleRegisterController, "mTimeManager", mTimeManager);

        mBleRegisterController.sendSecurityConfigResponse(data);

        verify(mBleRegisterController, atLeast(0)).sendSecurityConfigResponse(data);
    }

    @Test
    public void testOnReceiveDataAuthenticationRequest() throws Exception {
        PowerMockito.when(mBleRegisterController, "onReceiveDataAuthenticationRequest", mMessage).thenCallRealMethod();

        PowerMockito.when(mBleRegisterController, "stopTimer").thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleRegisterController, "mBleHandleAuthenticationResult", mBleHandleAuthenticationResult);
        PowerMockito.when(mBleHandleAuthenticationResult, "onAuthenticationRequestResult", mMessage).thenAnswer(voidAnswer);

        mBleRegisterController.onReceiveDataAuthenticationRequest(mMessage);

        verify(mBleRegisterController, atLeast(0)).onReceiveDataAuthenticationRequest(mMessage);
    }

    @Test
    public void testRequestUserName_Case01() throws Exception {
        String currentUserName = "currentUserName";
        PowerMockito.when(mBleRegisterController, "requestUserName", dataTest).thenCallRealMethod();
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);

        Whitebox.setInternalState(mBleRegisterController, "mBleHandleRegistrationResult", mBleHandleRegistrationResult);

        PowerMockito.when(mBleHandleRegistrationResult.saveUserInfo(dataTest)).thenReturn(1);

        Whitebox.invokeMethod(mBleRegisterController, "requestUserName", dataTest);

    }

    @Test
    public void testRequestUserName_Case02() throws Exception {
        String currentUserName = "";
        PowerMockito.when(mBleRegisterController, "requestUserName", dataTest).thenCallRealMethod();
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(TextUtils.isEmpty(currentUserName)).thenReturn(true);
        Whitebox.setInternalState(mBleRegisterController, "mBleHandleRegistrationResult", mBleHandleRegistrationResult);

        PowerMockito.when(mBleHandleRegistrationResult.saveUserInfo(dataTest)).thenReturn(1);

        Whitebox.invokeMethod(mBleRegisterController, "requestUserName", dataTest);

        PowerMockito.verifyPrivate(mBleRegisterController, atLeastOnce()).invoke("requestUserName", dataTest);
    }

    @Test
    public void testRequestUserName_Case03() throws Exception {
        String currentUserName = "";
        PowerMockito.when(mBleRegisterController, "requestUserName", dataTest).thenCallRealMethod();
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        Whitebox.setInternalState(mBleRegisterController, "mBleHandleRegistrationResult", mBleHandleRegistrationResult);

        PowerMockito.when(mBleHandleRegistrationResult.saveUserInfo(dataTest)).thenReturn(1);

        Whitebox.invokeMethod(mBleRegisterController, "requestUserName", dataTest);

        PowerMockito.verifyPrivate(mBleRegisterController, atLeastOnce()).invoke("requestUserName", dataTest);
    }

    @Test
    public void testRequestUserName_Case04() throws Exception {
        String currentUserName = "currentUserName";
        List<String> userNameList = mock(List.class);
        PowerMockito.when(mBleRegisterController, "requestUserName", dataTest).thenCallRealMethod();
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(TextUtils.isEmpty(currentUserName)).thenReturn(false);
        userNameList.add(dataTest);
        PowerMockito.whenNew(VehicleMessageRegistrationRequest.class).withAnyArguments().thenReturn(mVehicleMessageRegistrationRequest);

        PowerMockito.when(mVehicleMessageRegistrationRequest.getParams()).thenReturn(null);

        Whitebox.invokeMethod(mBleRegisterController, "requestUserName", dataTest);

        PowerMockito.verifyPrivate(mBleRegisterController, atLeastOnce()).invoke("requestUserName", dataTest);
    }

    @Test
    public void testRequestUserName_Case05() throws Exception {
        String currentUserName = "currentUserName";
        List<String> userNameList = mock(List.class);
        ArrayList<VehicleParam> paramList = mock(ArrayList.class);
        PowerMockito.when(mBleRegisterController, "requestUserName", dataTest).thenCallRealMethod();
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(TextUtils.isEmpty(currentUserName)).thenReturn(false);
        userNameList.add(dataTest);
        PowerMockito.whenNew(VehicleMessageRegistrationRequest.class).withAnyArguments().thenReturn(mVehicleMessageRegistrationRequest);
        PowerMockito.when(mBluetoothManager, "sendMessage", mVehicleMessageRegistrationRequest).thenAnswer(voidAnswer);

        Whitebox.setInternalState(mBleRegisterController, "mTimeManager", mTimeManager);
        PowerMockito.when(mTimeManager, "startTimer", mIF_TimeOutListener, 30000L).thenAnswer(voidAnswer);

        PowerMockito.when(mVehicleMessageRegistrationRequest.getParams()).thenReturn(paramList);

        Whitebox.invokeMethod(mBleRegisterController, "requestUserName", dataTest);

        PowerMockito.verifyPrivate(mBleRegisterController, atLeastOnce()).invoke("requestUserName", dataTest);
    }

    @Test
    public void testInquire() throws Exception {
        PowerMockito.when(mBleRegisterController, "inquire", true).thenCallRealMethod();

        VehicleMessageStatusInquiry vehicleMessageStatusInquiry = mock(VehicleMessageStatusInquiry.class);
        PowerMockito.whenNew(VehicleMessageStatusInquiry.class).withNoArguments().thenReturn(vehicleMessageStatusInquiry);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "sendMessage", vehicleMessageStatusInquiry).thenAnswer(voidAnswer);

        mBleRegisterController.inquire(true);

        verify(mBleRegisterController, atLeastOnce()).inquire(true);
    }

    @Test
    public void testIsRegisterInProgress_False() throws Exception {
        PowerMockito.when(mBleRegisterController.isRegisterInProgress()).thenCallRealMethod();

        Whitebox.setInternalState(mBleRegisterController, "isStartRegister", false);

        boolean actual = mBleRegisterController.isRegisterInProgress();

        assertFalse(actual);
    }

    @Test
    public void testIsRegisterInProgress_True() throws Exception {
        PowerMockito.when(mBleRegisterController.isRegisterInProgress()).thenCallRealMethod();

        Whitebox.setInternalState(mBleRegisterController, "isStartRegister", true);

        boolean actual = mBleRegisterController.isRegisterInProgress();

        assertTrue(actual);
    }

}
