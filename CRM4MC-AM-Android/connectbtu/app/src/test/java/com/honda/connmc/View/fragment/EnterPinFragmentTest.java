package com.honda.connmc.View.fragment;

import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.test.mock.MockContext;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.register.BleRegisterController;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Interfaces.listener.connect.IF_VehicleConnectListener;
import com.honda.connmc.R;
import com.honda.connmc.Utils.KeyboardUtils;
import com.honda.connmc.Utils.LogUtils;
import com.honda.connmc.View.activity.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static com.honda.connmc.Bluetooth.BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BluetoothManager.class,
        EnterPinFragment.class,
        KeyboardUtils.class,
        BleRegisterController.class,
        Resources.class,
        IF_VehicleConnectListener.class,
        Editable.Factory.class,
        BluetoothUtils.class,
        LogUtils.class,
        MainActivity.class
})
public class EnterPinFragmentTest {

    @Mock
    private EnterPinFragment mEnterPinFragment;

    @Mock
    private BluetoothManager mBluetoothManager;

    @Mock
    private EditText mInput;

    @Mock
    private TextView mTitle;
    @Mock
    private IF_VehicleConnectListener mIF_VehicleConnectListener;

    @Mock
    private Editable.Factory mEditableFactory;

    @Mock
    private BleRegisterController mBleRegisterController;

    @Mock
    private Editable mEditable;

    @Mock
    private Button mButtonSubmit;

    @Mock
    private MockContext mockContext;

    @Mock
    private Resources resources;

    @Mock
    private FragmentActivity activity;

    Answer voidAnswer = invocation -> null;

    private String SECURITY_CODE = "5NqWW58S";

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(BluetoothManager.class);
        PowerMockito.mockStatic(Editable.Factory.class);
        PowerMockito.mockStatic(EditText.class);
        PowerMockito.mockStatic(TextView.class);
        PowerMockito.mockStatic(MainActivity.class);
        PowerMockito.mockStatic(Resources.class);
        PowerMockito.mockStatic(FragmentActivity.class);
        PowerMockito.mockStatic(BluetoothUtils.class);
        PowerMockito.mockStatic(LogUtils.class);
        activity = Mockito.mock(FragmentActivity.class);

        PowerMockito.when(mEnterPinFragment.getActivity()).thenReturn(activity);

        PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "e", anyString()).thenAnswer(voidAnswer);
    }

//    @Test
//    public void testInitView() throws Exception {
//        PowerMockito.doCallRealMethod().when(mEnterPinFragment).initView();
//        PowerMockito.doReturn(mInput).when(mEnterPinFragment).findViewById(R.id.edtInput);
//        PowerMockito.doReturn(mButtonSubmit).when(mEnterPinFragment).findViewById(R.id.submit);
//        PowerMockito.doReturn(mTitle).when(mEnterPinFragment).findViewById(R.id.title);
//
//        Whitebox.setInternalState(mEnterPinFragment, "mInput", mInput);
//        Whitebox.setInternalState(mEnterPinFragment, "mTitle", mTitle);
//        Whitebox.setInternalState(mEnterPinFragment, "mBtnSubmit", mButtonSubmit);
//
//        mEnterPinFragment.initView();
//    }

    @Test
    public void testRegisterPin_Case01() throws Exception {
        PowerMockito.doReturn(mInput).when(mEnterPinFragment).findViewById(R.id.edtInput);
        Whitebox.setInternalState(mEnterPinFragment, "mInput", mInput);

        PowerMockito.when(mEnterPinFragment, "registerPin").thenCallRealMethod();

        PowerMockito.when(mEnterPinFragment, "isEnableClick").thenReturn(true);
        PowerMockito.when(mEnterPinFragment, "setNeedToShowDisconnectPopup", true).thenAnswer(voidAnswer);
        PowerMockito.when(KeyboardUtils.class, "hideKeyboard", mInput).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothUtils.isBluetoothDeviceConnected()).thenReturn(true);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mEnterPinFragment, "showProgressDialog").thenAnswer(voidAnswer);
        PowerMockito.when(Editable.Factory.getInstance()).thenReturn(mEditableFactory);
        PowerMockito.when(mInput.getText()).thenReturn(mEditable);
        PowerMockito.when(mEditable.toString()).thenReturn(SECURITY_CODE);
        assertEquals(SECURITY_CODE, mEditable.toString());
        PowerMockito.when(mBluetoothManager.getBleRegisterController()).thenReturn(mBleRegisterController);
        PowerMockito.when(mBleRegisterController.registerPinCode(SECURITY_CODE)).thenReturn(false);

        Whitebox.invokeMethod(mEnterPinFragment, "registerPin");

        verifyPrivate(mEnterPinFragment, atLeastOnce()).invoke("registerPin");
    }

    @Test
    public void testRegisterPin_Case02() throws Exception {
        PowerMockito.doReturn(mInput).when(mEnterPinFragment).findViewById(R.id.edtInput);
        Whitebox.setInternalState(mEnterPinFragment, "mInput", mInput);
        PowerMockito.when(mEnterPinFragment, "registerPin").thenCallRealMethod();

        PowerMockito.when(mEnterPinFragment, "isEnableClick").thenReturn(false);

        Whitebox.invokeMethod(mEnterPinFragment, "registerPin");

        verifyPrivate(mEnterPinFragment, atLeastOnce()).invoke("registerPin");
    }

    @Test
    public void testRegisterPin_Case03() throws Exception {
        PowerMockito.doReturn(mInput).when(mEnterPinFragment).findViewById(R.id.edtInput);
        Whitebox.setInternalState(mEnterPinFragment, "mInput", mInput);

        PowerMockito.when(mEnterPinFragment, "registerPin").thenCallRealMethod();

        PowerMockito.when(mEnterPinFragment, "isEnableClick").thenReturn(true);
        PowerMockito.when(mEnterPinFragment, "setNeedToShowDisconnectPopup", true).thenAnswer(voidAnswer);
        PowerMockito.when(KeyboardUtils.class, "hideKeyboard", mInput).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothUtils.isBluetoothDeviceConnected()).thenReturn(false);
        PowerMockito.when(mEnterPinFragment, "showRegistrationProblem").thenAnswer(voidAnswer);

        Whitebox.invokeMethod(mEnterPinFragment, "registerPin");

        verifyPrivate(mEnterPinFragment, atLeastOnce()).invoke("registerPin");
    }

//    @Test
//    public void testHandlePinWrong_Case01() throws Exception {
//        PowerMockito.when(mEnterPinFragment, "handlePinWrong").thenCallRealMethod();
//
//
//        Whitebox.setInternalState(mEnterPinFragment, "mTitle", mTitle);
//        PowerMockito.when(mEnterPinFragment, "hideProgressDialog").thenAnswer(voidAnswer);
//        PowerMockito.when(mTitle, "setText", R.string.pin_wrong).thenAnswer(voidAnswer);
//        when(activity.getResources()).thenReturn(resources);
//        when(resources.getColor(R.color.darkRed)).thenReturn(0x7f05002e);
//        PowerMockito.when(mTitle, "setTextColor", 0x7f05002e).thenAnswer(voidAnswer);
//
//        mEnterPinFragment.handlePinWrong();
//    }
//
//    @Test
//    public void testHandlePinWrong_Case02() throws Exception {
//        PowerMockito.when(mEnterPinFragment, "handlePinWrong").thenCallRealMethod();
//
//        Whitebox.setInternalState(mEnterPinFragment, "mCountInputPinWrong", 3);
//        PowerMockito.when(mEnterPinFragment, "hideProgressDialog").thenAnswer(voidAnswer);
//        PowerMockito.when(mTitle, "setText", "Pin not correct. Please try again.").thenAnswer(voidAnswer);
//        PowerMockito.when(mTitle, "setTextColor", "#901617").thenAnswer(voidAnswer);
//
//        int actual = Character.getNumericValue(Whitebox.getInternalState(mEnterPinFragment, "mCountInputPinWrong"));
//
//        assertEquals(3, actual);
//
//        PowerMockito.when(mEnterPinFragment, "showDialogPinWrong").thenAnswer(voidAnswer);
//
//        mEnterPinFragment.handlePinWrong();
//    }

    @Test
    public void testOnclick_Case01() throws Exception {
        ImageView imageView = mock(ImageView.class);
        doReturn(imageView).when(mEnterPinFragment).findViewById(R.id.btnBack);
        PowerMockito.doCallRealMethod().when(mEnterPinFragment).onClick(imageView);

        PowerMockito.when(mEnterPinFragment, "isEnableClick").thenReturn(true);

        PowerMockito.when(imageView, "getId").thenReturn(R.id.btnBack);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.disconnectDevice()).thenAnswer(voidAnswer);

        PowerMockito.when(mEnterPinFragment, "onBackPressedFragment").thenAnswer(voidAnswer);

        mEnterPinFragment.onClick(imageView);
    }

    @Test
    public void testOnclick_Case02() throws Exception {
        ImageView imageView = mock(ImageView.class);
        doReturn(imageView).when(mEnterPinFragment).findViewById(R.id.btnBack);
        PowerMockito.doCallRealMethod().when(mEnterPinFragment).onClick(imageView);
        PowerMockito.when(imageView, "getId").thenReturn(R.id.btnBack);

        PowerMockito.when(mEnterPinFragment, "isEnableClick").thenReturn(false);
        mEnterPinFragment.onClick(imageView);
    }

    @Test
    public void testOnConnectStateNotice_Case01() throws Exception {
        PowerMockito.when(mEnterPinFragment, "onConnectStateNotice", DISCONNECTED).thenCallRealMethod();

        PowerMockito.when(mEnterPinFragment, "isFragmentAlife").thenReturn(false);

        mEnterPinFragment.onConnectStateNotice(DISCONNECTED);

        verify(mEnterPinFragment, atLeastOnce()).onConnectStateNotice(DISCONNECTED);
    }

    @Test
    public void testOnConnectStateNotice_Case02() throws Exception {
        PowerMockito.when(mEnterPinFragment, "onConnectStateNotice", DISCONNECTED).thenCallRealMethod();

        PowerMockito.when(mEnterPinFragment, "isFragmentAlife").thenReturn(false);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        PowerMockito.when(mEnterPinFragment, "showRegistrationProblem").thenAnswer(voidAnswer);

        mEnterPinFragment.onConnectStateNotice(DISCONNECTED);

        verify(mEnterPinFragment, atLeastOnce()).onConnectStateNotice(DISCONNECTED);
    }
}
