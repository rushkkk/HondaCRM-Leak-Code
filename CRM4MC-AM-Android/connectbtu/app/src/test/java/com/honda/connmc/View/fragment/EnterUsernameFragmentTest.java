package com.honda.connmc.View.fragment;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.test.mock.MockContext;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.register.BleRegisterController;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Interfaces.listener.connect.IF_VehicleConnectListener;
import com.honda.connmc.R;
import com.honda.connmc.Utils.FragmentUtils;
import com.honda.connmc.Utils.KeyboardUtils;
import com.honda.connmc.Utils.LogUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static com.honda.connmc.Bluetooth.BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE;
import static com.honda.connmc.Bluetooth.BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        EnterUsernameFragment.class,
        BluetoothManager.class,
        KeyboardUtils.class,
        Resources.class,
        BleRegisterController.class,
        LayoutInflater.class,
        ViewGroup.class,
        IF_VehicleConnectListener.class,
        BluetoothUtils.class,
        FragmentUtils.class,
        FragmentManager.class,
        LogUtils.class,
        Fragment.class,
        TextUtils.class,
        FragmentActivity.class,
        Editable.Factory.class
})
public class EnterUsernameFragmentTest {
    @Mock
    private EnterUsernameFragment mEnterUsernameFragment;

    @Mock
    private BluetoothManager mBluetoothManager;

    @Mock
    private EditText mInput;

    @Mock
    private BleRegisterController mBleRegisterController;

    @Mock
    private Editable.Factory mEditableFactory;

    @Mock
    private Editable mEditable;

    @Mock
    private FragmentManager mFragmentManager;

    @Mock
    private LayoutInflater inflater;

    @Mock
    private ViewGroup mViewGroup;

    @Mock
    private View mMockView;
    @Mock
    private FragmentActivity activity;

    Answer voidAnswer = invocation -> null;

    private String USERNAME = "Device1";

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(BluetoothManager.class);
        PowerMockito.mockStatic(Editable.Factory.class);
        PowerMockito.mockStatic(TextView.class);
        PowerMockito.mockStatic(BluetoothUtils.class);
        PowerMockito.mockStatic(LogUtils.class);
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Resources.class);
        PowerMockito.mockStatic(FragmentActivity.class);
        PowerMockito.mockStatic(Fragment.class);
        PowerMockito.mockStatic(KeyboardUtils.class);
        PowerMockito.mockStatic(FragmentUtils.class);
        PowerMockito.mockStatic(FragmentManager.class);
        PowerMockito.mockStatic(IF_VehicleConnectListener.class);

        PowerMockito.when(mEnterUsernameFragment.getActivity()).thenReturn(activity);

        mInput = mock(EditText.class);
        PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "e", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(inflater, "inflate", R.layout.input_view, mViewGroup, false).thenReturn(mMockView);

    }

    @Test
    public void testHandleUsername_Case01() throws Exception {
        PowerMockito.when(mEnterUsernameFragment, "handleUsername").thenCallRealMethod();

        Whitebox.setInternalState(mEnterUsernameFragment, "mRootView", mMockView);
        PowerMockito.doReturn(mInput).when(mEnterUsernameFragment).findViewById(R.id.edtInput);
        Whitebox.setInternalState(mEnterUsernameFragment, "mInput", mInput);
        PowerMockito.when(mEnterUsernameFragment, "setNeedToShowDisconnectPopup", true).thenAnswer(voidAnswer);
        PowerMockito.when(KeyboardUtils.class, "hideKeyboard", mMockView).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothUtils.isBluetoothDeviceConnected()).thenReturn(true);

        PowerMockito.when(mEnterUsernameFragment, "showProgressDialog").thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(Editable.Factory.getInstance()).thenReturn(mEditableFactory);
        PowerMockito.when(mInput.getText()).thenReturn(mEditable);
        PowerMockito.when(mEditable.toString().trim()).thenReturn(USERNAME);
        assertEquals(USERNAME, mEditable.toString().trim());
        PowerMockito.when(mBluetoothManager.getBleRegisterController()).thenReturn(mBleRegisterController);
        PowerMockito.when(mBleRegisterController, "registerUserName", USERNAME).thenAnswer(voidAnswer);

        Whitebox.invokeMethod(mEnterUsernameFragment, "handleUsername");

        PowerMockito.verifyPrivate(mEnterUsernameFragment, atLeastOnce()).invoke("handleUsername");
    }

    @Test
    public void testHandleUsername_Case02() throws Exception {
        PowerMockito.when(mEnterUsernameFragment, "handleUsername").thenCallRealMethod();

        Whitebox.setInternalState(mEnterUsernameFragment, "mRootView", mMockView);
        PowerMockito.doReturn(mInput).when(mEnterUsernameFragment).findViewById(R.id.edtInput);
        Whitebox.setInternalState(mEnterUsernameFragment, "mInput", mInput);
        PowerMockito.when(mEnterUsernameFragment, "setNeedToShowDisconnectPopup", true).thenAnswer(voidAnswer);
        PowerMockito.when(KeyboardUtils.class, "hideKeyboard", mMockView).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothUtils.isBluetoothDeviceConnected()).thenReturn(true);
        PowerMockito.when(Editable.Factory.getInstance()).thenReturn(mEditableFactory);
        PowerMockito.when(mInput.getText()).thenReturn(mEditable);
        PowerMockito.when(mEditable.toString().trim()).thenReturn("");
        PowerMockito.when(TextUtils.isEmpty("")).thenReturn(true);

        Whitebox.invokeMethod(mEnterUsernameFragment, "handleUsername");

        PowerMockito.verifyPrivate(mEnterUsernameFragment, atLeastOnce()).invoke("handleUsername");
    }

    @Test
    public void testHandleUsername_Case03() throws Exception {
        PowerMockito.when(mEnterUsernameFragment, "handleUsername").thenCallRealMethod();

        Whitebox.setInternalState(mEnterUsernameFragment, "mRootView", mMockView);
        PowerMockito.doReturn(mInput).when(mEnterUsernameFragment).findViewById(R.id.edtInput);
        PowerMockito.when(mEnterUsernameFragment, "setNeedToShowDisconnectPopup", true).thenAnswer(voidAnswer);
        Whitebox.setInternalState(mEnterUsernameFragment, "mInput", mInput);

        PowerMockito.when(KeyboardUtils.class, "hideKeyboard", mMockView).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothUtils.isBluetoothDeviceConnected()).thenReturn(false);
        PowerMockito.when(mEnterUsernameFragment, "showRegistrationProblem").thenAnswer(voidAnswer);

        Whitebox.invokeMethod(mEnterUsernameFragment, "handleUsername");

        PowerMockito.verifyPrivate(mEnterUsernameFragment, atLeastOnce()).invoke("handleUsername");
    }

    @Test
    public void testOnclick_Case01() throws Exception {
        ImageView imageView = PowerMockito.mock(ImageView.class);
        doReturn(imageView).when(mEnterUsernameFragment).findViewById(R.id.btnBack);
        PowerMockito.doCallRealMethod().when(mEnterUsernameFragment).onClick(imageView);

        PowerMockito.when(mEnterUsernameFragment, "isEnableClick").thenReturn(true);

        PowerMockito.when(imageView, "getId").thenReturn(R.id.btnBack);

        PowerMockito.when(mEnterUsernameFragment, "goToDeviceListFragment", FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK).thenAnswer(voidAnswer);

        mEnterUsernameFragment.onClick(imageView);
    }

    @Test
    public void testOnclick_Case02() throws Exception {
        ImageView imageView = PowerMockito.mock(ImageView.class);
        doReturn(imageView).when(mEnterUsernameFragment).findViewById(R.id.btnBack);
        PowerMockito.doCallRealMethod().when(mEnterUsernameFragment).onClick(imageView);
        PowerMockito.when(imageView, "getId").thenReturn(R.id.btnBack);

        PowerMockito.when(mEnterUsernameFragment, "isEnableClick").thenReturn(false);
        mEnterUsernameFragment.onClick(imageView);
    }

    @Test
    public void testGoToDeviceListFragment_Case01() throws Exception {
        PowerMockito.when(mEnterUsernameFragment, "goToDeviceListFragment", FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK).thenCallRealMethod();

        PowerMockito.when(activity.getSupportFragmentManager()).thenReturn(mFragmentManager);

        PowerMockito.when(FragmentUtils.class, "clearAllFragment", mFragmentManager).thenAnswer(voidAnswer);
        PowerMockito.when(FragmentUtils.class, "replaceFragment", mFragmentManager, new DeviceListFragment(), R.id.fragment_container, false, FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK).thenAnswer(voidAnswer);


        Whitebox.invokeMethod(mEnterUsernameFragment, "goToDeviceListFragment", FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);

        PowerMockito.verifyPrivate(mEnterUsernameFragment, atLeastOnce()).invoke("goToDeviceListFragment", FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);

    }

    @Test
    public void testGoToDeviceListFragment_Case02() throws Exception {
        PowerMockito.when(mEnterUsernameFragment, "goToDeviceListFragment", FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK).thenCallRealMethod();

        PowerMockito.when(activity.getSupportFragmentManager()).thenReturn(null);

        Whitebox.invokeMethod(mEnterUsernameFragment, "goToDeviceListFragment", FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);

        PowerMockito.verifyPrivate(mEnterUsernameFragment, atLeastOnce()).invoke("goToDeviceListFragment", FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);

    }

    @Test
    public void testOnConnectStateNotice_Case01() throws Exception {
        PowerMockito.when(mEnterUsernameFragment, "onConnectStateNotice", DISCONNECTED).thenCallRealMethod();

        PowerMockito.when(mEnterUsernameFragment, "isFragmentAlife").thenReturn(false);

        mEnterUsernameFragment.onConnectStateNotice(DISCONNECTED);

        verify(mEnterUsernameFragment, atLeastOnce()).onConnectStateNotice(DISCONNECTED);
    }

    @Test
    public void testOnConnectStateNotice_Case02() throws Exception {
        PowerMockito.when(mEnterUsernameFragment, "onConnectStateNotice", DISCONNECTED).thenCallRealMethod();

        PowerMockito.when(mEnterUsernameFragment, "isFragmentAlife").thenReturn(true);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(mEnterUsernameFragment, "showRegistrationProblem").thenAnswer(voidAnswer);

        mEnterUsernameFragment.onConnectStateNotice(DISCONNECTED);

        verify(mEnterUsernameFragment, atLeastOnce()).onConnectStateNotice(DISCONNECTED);
    }

    @Test
    public void testOnConnectStateNotice_Case03() throws Exception {
        Fragment fragment = mock(Fragment.class);
        PowerMockito.when(mEnterUsernameFragment, "onConnectStateNotice", BTU_ACTIVE).thenCallRealMethod();

        PowerMockito.when(mEnterUsernameFragment, "isFragmentAlife").thenReturn(true);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        activity.runOnUiThread(() -> {
            try {
                PowerMockito.whenNew(VehicleStatusFragment.class).withNoArguments().thenReturn((VehicleStatusFragment) fragment);
                PowerMockito.when(activity.getSupportFragmentManager()).thenReturn(mFragmentManager);
                PowerMockito.when(FragmentUtils.class, "clearAllFragment", mFragmentManager).thenAnswer(voidAnswer);
                PowerMockito.when(FragmentUtils.class, "replaceFragment", mFragmentManager, new DeviceListFragment(), R.id.fragment_container, false, FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_GO).thenAnswer(voidAnswer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mEnterUsernameFragment.onConnectStateNotice(BTU_ACTIVE);

        verify(mEnterUsernameFragment, atLeastOnce()).onConnectStateNotice(BTU_ACTIVE);
    }
}
