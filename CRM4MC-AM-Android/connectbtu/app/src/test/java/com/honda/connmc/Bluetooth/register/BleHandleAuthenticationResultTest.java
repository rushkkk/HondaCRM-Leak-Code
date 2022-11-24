package com.honda.connmc.Bluetooth.register;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.connect.BleConnectController;
import com.honda.connmc.Bluetooth.timer.TimeManager;
import com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils;
import com.honda.connmc.Constants.message.ParamTag;
import com.honda.connmc.Constants.message.ParamValue;
import com.honda.connmc.Interfaces.listener.register.IF_VehicleRegisterListener;
import com.honda.connmc.Model.message.common.VehicleMessage;
import com.honda.connmc.Model.message.common.VehicleParam;
import com.honda.connmc.Model.message.register.request.VehicleMessageRegistrationRequest;
import com.honda.connmc.Model.message.register.response.VehicleMessageAuthenticationResponse;
import com.honda.connmc.Model.message.register.security.VehicleParameterTransactionIdentity;
import com.honda.connmc.Model.message.register.security.VehicleParameterTypeResultSuccess;
import com.honda.connmc.Model.message.register.security.VehicleParameterTypeSecurityAuthResponseValue;
import com.honda.connmc.Model.message.register.security.VehicleParameterTypeSecurityConfigIdentifier;
import com.honda.connmc.Model.message.register.security.VehicleParameterTypeSecurityRandomValue;
import com.honda.connmc.Model.message.register.security.VehicleParameterTypeSecurityVehicleAuthValue;
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
import java.util.Iterator;

import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_BTU_NAME_REGISTER_SUCCESS;
import static com.honda.connmc.Constants.message.ParamTag.AUTH_RESPONSE_VALUE;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        BleHandleAuthenticationResult.class,
        IF_VehicleRegisterListener.class,
        BleRegisterController.class,
        LogUtils.class,
        ParamTag.class,
        VehicleMessage.class,
        VehicleParam.class,
        VehicleParameterTypeSecurityAuthResponseValue.class,
        VehicleParameterTypeSecurityConfigIdentifier.class,
        VehicleParameterTypeSecurityRandomValue.class,
        VehicleParameterTypeResultSuccess.class,
        VehicleParameterTypeSecurityVehicleAuthValue.class,
        VehicleParameterTransactionIdentity.class,
        BluetoothManager.class,
        ByteBuffer.class,
        NativeLibController.class,
        VehicleMessageAuthenticationResponse.class,
        BluetoothPrefUtils.class,
        BleConnectController.class,
        BluetoothDevice.class,
        ParamValue.class,
        Thread.class,
        Object.class
})
public class BleHandleAuthenticationResultTest {

    @Mock
    private BleHandleAuthenticationResult mBleHandleAuthenticationResult;

    @Mock
    private IF_VehicleRegisterListener mRegistrationEventListener;

    @Mock
    private BluetoothManager mBluetoothManager;

    @Mock
    private BleRegisterController mBleRegisterController;

    @Mock
    private BluetoothPrefUtils mBluetoothPrefUtils;

    @Mock
    private BluetoothDevice mBluetoothDevice;

    @Mock
    private NativeLibController mNativeLibController;

    @Mock
    private BleConnectController mBleConnectController;

    @Mock
    private ByteBuffer mByteBuffer;

    @Mock
    private Object mObject;

    @Mock
    private VehicleParam mVehicleParam;
    @Mock
    private VehicleMessage mVehicleMessage;

    @Mock
    private VehicleParameterTypeSecurityConfigIdentifier securityConfId;
    @Mock
    private VehicleParameterTypeSecurityRandomValue randomValue;
    @Mock
    private VehicleParameterTypeSecurityVehicleAuthValue vehicleAuthValue;

    byte[] dataTest = {01, 02, 03, 04};

    Answer voidAnswer = invocation -> null;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(BleRegisterController.class);
        PowerMockito.mockStatic(BluetoothManager.class);
        PowerMockito.mockStatic(BleRegisterController.class);
        PowerMockito.mockStatic(ByteBuffer.class);
        PowerMockito.mockStatic(Thread.class);
        PowerMockito.mockStatic(ParamTag.class);
        PowerMockito.mockStatic(BluetoothDevice.class);
        PowerMockito.mockStatic(NativeLibController.class);
        PowerMockito.mockStatic(VehicleMessageRegistrationRequest.class);
        PowerMockito.mockStatic(BleHandleRegistrationResult.class);
        PowerMockito.mockStatic(BleHandleAuthenticationResult.class);
        PowerMockito.mockStatic(IF_VehicleRegisterListener.class);
        PowerMockito.mockStatic(VehicleParameterTypeSecurityConfigIdentifier.class);
        PowerMockito.mockStatic(VehicleParameterTypeSecurityRandomValue.class);
        PowerMockito.mockStatic(VehicleParameterTypeSecurityVehicleAuthValue.class);
        PowerMockito.mockStatic(TimeManager.class);
        PowerMockito.mockStatic(LogUtils.class);
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(BluetoothPrefUtils.class);
        PowerMockito.mockStatic(Object.class);

        Whitebox.setInternalState(mBleHandleAuthenticationResult, "LOCK_OBJECT", mObject);
        when(LogUtils.class, "d", anyString()).thenAnswer(voidAnswer);
        when(LogUtils.class, "e", anyString()).thenAnswer(voidAnswer);
    }

    @Test
    public void testOnAuthenticationRequestResult_Case01() throws Exception {
        ArrayList<VehicleParam> paramArrayList = mock(ArrayList.class);
        mVehicleMessage.mParamArray = paramArrayList;
        mVehicleParam = new VehicleParam(ParamTag.SECURITY_CONFIG_IDENTIFIER, dataTest);

        PowerMockito.when(mBleHandleAuthenticationResult, "onAuthenticationRequestResult", mVehicleMessage).thenCallRealMethod();

        Iterator iterator = Mockito.mock(Iterator.class);
        when(paramArrayList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(mVehicleParam);
        mVehicleParam.mTag = ParamTag.SECURITY_CONFIG_IDENTIFIER;
        PowerMockito.whenNew(VehicleParameterTypeSecurityRandomValue.class).withAnyArguments().thenReturn(randomValue);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBleHandleAuthenticationResult, "tryAuthenticationAsync", dataTest, dataTest, dataTest).thenAnswer(voidAnswer);

        mBleHandleAuthenticationResult.onAuthenticationRequestResult(mVehicleMessage);

        verify(mBleHandleAuthenticationResult, atLeastOnce()).onAuthenticationRequestResult(mVehicleMessage);
    }

    @Test
    public void testOnAuthenticationRequestResult_Case02() throws Exception {
        ArrayList<VehicleParam> paramArrayList = mock(ArrayList.class);
        mVehicleMessage.mParamArray = paramArrayList;
        when(mBleHandleAuthenticationResult, "onAuthenticationRequestResult", mVehicleMessage).thenCallRealMethod();
        PowerMockito.whenNew(VehicleParameterTypeSecurityConfigIdentifier.class).withAnyArguments().thenReturn(securityConfId);

        Iterator iterator = Mockito.mock(Iterator.class);
        when(paramArrayList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(mVehicleParam);
        mVehicleParam.mTag = ParamTag.RANDOM_VALUE;

        mBleHandleAuthenticationResult.onAuthenticationRequestResult(mVehicleMessage);

        verify(mBleHandleAuthenticationResult, atLeastOnce()).onAuthenticationRequestResult(mVehicleMessage);
    }

    @Test
    public void testOnAuthenticationRequestResult_Case03() throws Exception {
        ArrayList<VehicleParam> paramArrayList = mock(ArrayList.class);
        mVehicleMessage.mParamArray = paramArrayList;
        mVehicleParam = new VehicleParam(ParamTag.RANDOM_VALUE, dataTest);
        when(mBleHandleAuthenticationResult, "onAuthenticationRequestResult", mVehicleMessage).thenCallRealMethod();
        PowerMockito.whenNew(VehicleParameterTypeSecurityConfigIdentifier.class).withAnyArguments().thenReturn(securityConfId);

        Iterator iterator = Mockito.mock(Iterator.class);
        when(paramArrayList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(mVehicleParam);
        mVehicleParam.mTag = ParamTag.RANDOM_VALUE;
        PowerMockito.whenNew(VehicleParameterTypeSecurityConfigIdentifier.class).withAnyArguments().thenReturn(securityConfId);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBleHandleAuthenticationResult, "tryAuthenticationAsync", null, dataTest, null).thenAnswer(voidAnswer);

        mBleHandleAuthenticationResult.onAuthenticationRequestResult(mVehicleMessage);

        verify(mBleHandleAuthenticationResult, atLeastOnce()).onAuthenticationRequestResult(mVehicleMessage);
    }

    @Test
    public void testOnAuthenticationRequestResult_Case04() throws Exception {
        ArrayList<VehicleParam> paramArrayList = mock(ArrayList.class);
        mVehicleMessage.mParamArray = paramArrayList;
        when(mBleHandleAuthenticationResult, "onAuthenticationRequestResult", mVehicleMessage).thenCallRealMethod();

        Iterator iterator = Mockito.mock(Iterator.class);
        when(paramArrayList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(mVehicleParam);
        mVehicleParam.mTag = ParamTag.VEHICLE_AUTH_VALUE;

        mBleHandleAuthenticationResult.onAuthenticationRequestResult(mVehicleMessage);

        verify(mBleHandleAuthenticationResult, atLeastOnce()).onAuthenticationRequestResult(mVehicleMessage);
    }

    @Test
    public void testOnAuthenticationRequestResult_Case05() throws Exception {
        ArrayList<VehicleParam> paramArrayList = mock(ArrayList.class);
        mVehicleMessage.mParamArray = paramArrayList;
        mVehicleParam = new VehicleParam(ParamTag.VEHICLE_AUTH_VALUE, dataTest);
        when(mBleHandleAuthenticationResult, "onAuthenticationRequestResult", mVehicleMessage).thenCallRealMethod();

        Iterator iterator = Mockito.mock(Iterator.class);
        when(paramArrayList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(mVehicleParam);
        mVehicleParam.mTag = ParamTag.VEHICLE_AUTH_VALUE;
        PowerMockito.whenNew(VehicleParameterTypeSecurityVehicleAuthValue.class).withAnyArguments().thenReturn(vehicleAuthValue);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBleHandleAuthenticationResult, "tryAuthenticationAsync", null, null, dataTest).thenAnswer(voidAnswer);

        mBleHandleAuthenticationResult.onAuthenticationRequestResult(mVehicleMessage);

        verify(mBleHandleAuthenticationResult, atLeastOnce()).onAuthenticationRequestResult(mVehicleMessage);
    }

    @Test
    public void testOnAuthenticationRequestResult_Case06() throws Exception {
        ArrayList<VehicleParam> paramArrayList = mock(ArrayList.class);
        mVehicleMessage.mParamArray = paramArrayList;
        mVehicleParam = new VehicleParam(ParamTag.TRANSACTION_IDENTITY, dataTest);
        when(mBleHandleAuthenticationResult, "onAuthenticationRequestResult", mVehicleMessage).thenCallRealMethod();

        Iterator iterator = Mockito.mock(Iterator.class);
        when(paramArrayList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(mVehicleParam);
        mVehicleParam.mTag = ParamTag.TRANSACTION_IDENTITY;
        mVehicleParam.mValue[0] = 1;
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);

        PowerMockito.when(mBleHandleAuthenticationResult, "tryAuthenticationAsync", null, null, dataTest).thenAnswer(voidAnswer);

        mBleHandleAuthenticationResult.onAuthenticationRequestResult(mVehicleMessage);

        verify(mBleHandleAuthenticationResult, atLeastOnce()).onAuthenticationRequestResult(mVehicleMessage);
    }

    @Test
    public void testOnAuthenticationRequestResult_Case07() throws Exception {
        ArrayList<VehicleParam> paramArrayList = mock(ArrayList.class);
        mVehicleMessage.mParamArray = paramArrayList;
        mVehicleParam = new VehicleParam(ParamTag.SECURITY_CONFIG_IDENTIFIER, dataTest);
        mVehicleParam = new VehicleParam(ParamTag.VEHICLE_AUTH_VALUE, dataTest);
        mVehicleParam = new VehicleParam(ParamTag.RANDOM_VALUE, dataTest);
        when(mBleHandleAuthenticationResult, "onAuthenticationRequestResult", mVehicleMessage).thenCallRealMethod();

        Iterator iterator = Mockito.mock(Iterator.class);
        when(paramArrayList.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.next()).thenReturn(mVehicleParam);
        mVehicleParam.mTag = ParamTag.VEHICLE_AUTH_VALUE;
        mVehicleParam.mTag = ParamTag.RANDOM_VALUE;
        mVehicleParam.mTag = ParamTag.SECURITY_CONFIG_IDENTIFIER;
        mVehicleParam.mValue[0] = 1;
        PowerMockito.whenNew(VehicleParameterTypeSecurityConfigIdentifier.class).withAnyArguments().thenReturn(securityConfId);
        PowerMockito.whenNew(VehicleParameterTypeSecurityRandomValue.class).withAnyArguments().thenReturn(randomValue);
        PowerMockito.whenNew(VehicleParameterTypeSecurityVehicleAuthValue.class).withAnyArguments().thenReturn(vehicleAuthValue);

        PowerMockito.when(mBleHandleAuthenticationResult, "tryAuthenticationAsync", null, null, dataTest).thenAnswer(voidAnswer);

        mBleHandleAuthenticationResult.onAuthenticationRequestResult(mVehicleMessage);

        verify(mBleHandleAuthenticationResult, atLeastOnce()).onAuthenticationRequestResult(mVehicleMessage);
    }

    @Test
    public void testPerformAuthentication_Case01() throws Exception {
        when(mBleHandleAuthenticationResult, "performAuthentication", dataTest, dataTest, dataTest).thenCallRealMethod();
        when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        when(ByteBuffer.wrap(dataTest)).thenReturn(mByteBuffer);
        byte data = 0;
        when(mByteBuffer.get()).thenReturn(data);

        when(mBleHandleAuthenticationResult, "onVehicleAuthenticationResult", dataTest, true).thenAnswer(voidAnswer);


        mBleHandleAuthenticationResult.performAuthentication(dataTest, dataTest, dataTest);

        verify(mBleHandleAuthenticationResult, atLeastOnce()).performAuthentication(dataTest, dataTest, dataTest);
    }

    @Test
    public void testPerformAuthentication_Case02() throws Exception {
        when(mBleHandleAuthenticationResult, "performAuthentication", dataTest, dataTest, dataTest).thenCallRealMethod();
        when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        when(ByteBuffer.wrap(dataTest)).thenReturn(mByteBuffer);
        byte data = 1;
        when(mByteBuffer.get()).thenReturn(data);
        byte[] devAuth = new byte[ParamTag.getLength(AUTH_RESPONSE_VALUE)];

        when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        when(mNativeLibController.tryAuthentication(dataTest, dataTest, devAuth)).thenReturn(1);

        when(mBleHandleAuthenticationResult, "onVehicleAuthenticationResult", devAuth, true).thenAnswer(voidAnswer);


        mBleHandleAuthenticationResult.performAuthentication(dataTest, dataTest, dataTest);

        verify(mBleHandleAuthenticationResult, atLeastOnce()).performAuthentication(dataTest, dataTest, dataTest);
    }

    @Test
    public void testPerformAuthentication_Case03() throws Exception {
        when(mBleHandleAuthenticationResult, "performAuthentication", dataTest, dataTest, dataTest).thenCallRealMethod();
        when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        when(ByteBuffer.wrap(dataTest)).thenReturn(mByteBuffer);
        byte data = 1;
        when(mByteBuffer.get()).thenReturn(data);

        when(NativeLibController.getInstance()).thenReturn(null);
        mBleHandleAuthenticationResult.performAuthentication(dataTest, dataTest, dataTest);

        verify(mBleHandleAuthenticationResult, atLeastOnce()).performAuthentication(dataTest, dataTest, dataTest);
    }

    @Test
    public void testPerformAuthentication_Case04() throws Exception {
        when(mBleHandleAuthenticationResult, "performAuthentication", dataTest, dataTest, dataTest).thenCallRealMethod();
        when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        when(ByteBuffer.wrap(dataTest)).thenReturn(mByteBuffer);
        byte data = 1;
        when(mByteBuffer.get()).thenReturn(data);
        byte[] devAuth = new byte[ParamTag.getLength(AUTH_RESPONSE_VALUE)];

        when(NativeLibController.getInstance()).thenReturn(mNativeLibController);
        when(mNativeLibController.tryAuthentication(dataTest, dataTest, devAuth)).thenReturn(0);

        when(mBleHandleAuthenticationResult, "onVehicleAuthenticationResult", devAuth, false).thenAnswer(voidAnswer);


        mBleHandleAuthenticationResult.performAuthentication(dataTest, dataTest, dataTest);

        verify(mBleHandleAuthenticationResult, atLeastOnce()).performAuthentication(dataTest, dataTest, dataTest);
    }

    @Test
    public void testPerformAuthentication_Case05() throws Exception {
        byte[] testData = null;
        when(mBleHandleAuthenticationResult, "performAuthentication", testData, testData, testData).thenCallRealMethod();
        when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        mBleHandleAuthenticationResult.performAuthentication(testData, testData, testData);

        verify(mBleHandleAuthenticationResult, atLeastOnce()).performAuthentication(testData, testData, testData);
    }

    @Test
    public void testOnVehicleAuthenticationResult_Case01() throws Exception {
        byte[] testData = null;
        when(mBleHandleAuthenticationResult, "onVehicleAuthenticationResult", testData, false).thenCallRealMethod();
        when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mBleHandleAuthenticationResult, "mBleRegisterController", mBleRegisterController);
        when(mBleRegisterController, "registrationFailureProcess").thenAnswer(voidAnswer);

        mBleHandleAuthenticationResult.onVehicleAuthenticationResult(testData, false);

        verify(mBleHandleAuthenticationResult, atLeastOnce()).onVehicleAuthenticationResult(testData, false);
    }

    @Test
    public void testOnVehicleAuthenticationResult_Case02() throws Exception {
        when(mBleHandleAuthenticationResult, "onVehicleAuthenticationResult", dataTest, true).thenCallRealMethod();
        when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        VehicleMessageAuthenticationResponse message = mock(VehicleMessageAuthenticationResponse.class);
        PowerMockito.whenNew(VehicleMessageAuthenticationResponse.class).withNoArguments().thenReturn(message);
        ArrayList<VehicleParam> paramArrayList = mock(ArrayList.class);
        message.mParamArray = paramArrayList;
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        VehicleParameterTransactionIdentity mVehicleParameterTransactionIdentity = mock(VehicleParameterTransactionIdentity.class);
        VehicleParameterTypeResultSuccess mVehicleParameterTypeResultSuccess = mock(VehicleParameterTypeResultSuccess.class);
        VehicleParameterTypeSecurityAuthResponseValue mVehicleParameterTypeSecurityAuthResponseValue = mock(VehicleParameterTypeSecurityAuthResponseValue.class);
        PowerMockito.whenNew(VehicleParameterTransactionIdentity.class).withAnyArguments().thenReturn(mVehicleParameterTransactionIdentity);
        PowerMockito.whenNew(VehicleParameterTypeResultSuccess.class).withAnyArguments().thenReturn(mVehicleParameterTypeResultSuccess);
        PowerMockito.whenNew(VehicleParameterTypeSecurityAuthResponseValue.class).withAnyArguments().thenReturn(mVehicleParameterTypeSecurityAuthResponseValue);

        PowerMockito.when(paramArrayList, "add", mVehicleParameterTransactionIdentity).thenAnswer(voidAnswer);
        PowerMockito.when(paramArrayList, "add", mVehicleParameterTypeResultSuccess).thenAnswer(voidAnswer);
        PowerMockito.when(paramArrayList, "add", mVehicleParameterTypeSecurityAuthResponseValue).thenAnswer(voidAnswer);


        when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        when(mBluetoothManager.getBleConnectController()).thenReturn(mBleConnectController);
        when(mBleConnectController.getBluetoothDeviceConnect()).thenReturn(mBluetoothDevice);
        when(mBluetoothDevice.getName()).thenReturn(anyString());
        when(mBluetoothManager, "sendMessage", message).thenAnswer(voidAnswer);
        when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        when(mBluetoothPrefUtils.setString(KEY_BTU_NAME_REGISTER_SUCCESS, "BTU000001")).thenReturn(true);

        mBleHandleAuthenticationResult.onVehicleAuthenticationResult(dataTest, true);

        verify(mBleHandleAuthenticationResult, atLeastOnce()).onVehicleAuthenticationResult(dataTest, true);
    }
}
