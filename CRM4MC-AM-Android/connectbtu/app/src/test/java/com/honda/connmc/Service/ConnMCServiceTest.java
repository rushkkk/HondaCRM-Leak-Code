package com.honda.connmc.Service;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Manager.PhoneStateManager;
import com.honda.connmc.Model.message.notification.VehicleNotificationMessage;
import com.honda.connmc.Utils.LogUtils;

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_BTU_NAME_REGISTER_SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ConnMCService.class,
        NotificationListenerService.class,
        PhoneStateManager.class,
        Build.class,
        Build.VERSION.class,
        Notification.class,
        LogUtils.class,
        StatusBarNotification.class,
        VehicleNotificationMessage.class,
        BluetoothManager.class,
        BluetoothPrefUtils.class,
        BluetoothUtils.class,
        Intent.class,
        BroadcastReceiver.class,
        TextUtils.class
})
public class ConnMCServiceTest extends NotificationListenerService {
    @Mock
    private ConnMCService mConnMCService;
    @Mock
    private PhoneStateManager mPhoneStateManager;
    @Mock
    private PhoneStateManager.OnReceiveMessageFromPhoneState mOnReceiveMessageFromPhoneState;
    @Mock
    private Notification mNotification;
    @Mock
    private BluetoothManager mBluetoothManager;
    @Mock
    private BroadcastReceiver mBroadcastReceiver;
    @Mock
    private BluetoothPrefUtils mBluetoothPrefUtils;
    @Mock
    private VehicleNotificationMessage message;
    @Mock
    private StatusBarNotification mStatusBarNotification;
    @Mock
    private Intent mIntent;
    @Mock
    private Context mContext;

    Answer voidAnswer = invocation -> null;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(PhoneStateManager.class);
        PowerMockito.mockStatic(ConnMCService.class);
        PowerMockito.mockStatic(Notification.class);
        PowerMockito.mockStatic(BluetoothManager.class);
        PowerMockito.mockStatic(VehicleNotificationMessage.class);
        PowerMockito.mockStatic(StatusBarNotification.class);
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(Build.VERSION.class);
        PowerMockito.mockStatic(LogUtils.class);
        PowerMockito.mockStatic(Context.class);
        PowerMockito.mockStatic(Intent.class);
        PowerMockito.mockStatic(BluetoothPrefUtils.class);
        PowerMockito.mockStatic(BroadcastReceiver.class);
        PowerMockito.mockStatic(BluetoothUtils.class);

        PowerMockito.when(LogUtils.class, "d", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "e", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "i", anyString()).thenAnswer(voidAnswer);
        PowerMockito.when(LogUtils.class, "w", anyString()).thenAnswer(voidAnswer);
    }


    @Test
    public void testOnStartCommand_Case01() throws Exception {
        PowerMockito.when(mConnMCService.onStartCommand(mIntent, 1, 1)).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        PowerMockito.whenNew(PhoneStateManager.class).withAnyArguments().thenReturn(mPhoneStateManager);

        PowerMockito.when(mPhoneStateManager, "listenPhoneState").thenAnswer(voidAnswer);

        int actual = mConnMCService.onStartCommand(mIntent, 1, 1);

        assertEquals(START_STICKY, actual);
    }

    @Test
    public void testOnStartCommand_Case02() throws Exception {
        PowerMockito.when(mConnMCService.onStartCommand(mIntent, 1, 1)).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        Whitebox.setInternalState(mConnMCService, "mPhoneStateManager", mPhoneStateManager);

        PowerMockito.when(mPhoneStateManager, "listenPhoneState").thenAnswer(voidAnswer);

        int actual = mConnMCService.onStartCommand(mIntent, 1, 1);

        assertEquals(START_STICKY, actual);
    }

    @Test
    public void testOnNotificationPosted_Case01() throws Exception {
        PowerMockito.when(mConnMCService, "onNotificationPosted", mStatusBarNotification).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenAnswer(voidAnswer);

        mConnMCService.onNotificationPosted(mStatusBarNotification);

        verify(mConnMCService, atLeastOnce()).onNotificationPosted(mStatusBarNotification);
    }

    @Test
    public void testHandlerNotification_Case00() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn("");
        PowerMockito.when(TextUtils.isEmpty("")).thenReturn(true);

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }


    @Test
    public void testHandlerNotification_Case01() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(ConnMCService.PACKAGE_NAME_FACEBOOK);
        PowerMockito.when(mNotificationList, "put", ConnMCService.PACKAGE_NAME_FACEBOOK, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("FACEBOOK")).thenReturn(false);
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "FACEBOOK").thenCallRealMethod();

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }

    @Test
    public void testHandlerNotification_Case02() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(ConnMCService.PACKAGE_NAME_FACEBOOK_WORK);
        PowerMockito.when(mNotificationList, "put", ConnMCService.PACKAGE_NAME_FACEBOOK_WORK, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("FACEBOOK AT WORK")).thenReturn(false);
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "FACEBOOK AT WORK").thenCallRealMethod();

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }

    @Test
    public void testHandlerNotification_Case03() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(ConnMCService.PACKAGE_NAME_FACEBOOK_MESSENGER);
        PowerMockito.when(mNotificationList, "put", ConnMCService.PACKAGE_NAME_FACEBOOK_MESSENGER, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("FACEBOOK MESSENGER")).thenReturn(false);
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "FACEBOOK MESSENGER").thenCallRealMethod();

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }

    @Test
    public void testHandlerNotification_Case04() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(ConnMCService.PACKAGE_NAME_FACEBOOK_MESSENGER_WORK);
        PowerMockito.when(mNotificationList, "put", ConnMCService.PACKAGE_NAME_FACEBOOK_MESSENGER_WORK, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("FACEBOOK MESSENGER AT WORK")).thenReturn(false);
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "FACEBOOK MESSENGER AT WORK").thenCallRealMethod();

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }

    @Test
    public void testHandlerNotification_Case05() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(ConnMCService.PACKAGE_NAME_WHATSTAPP);
        PowerMockito.when(mNotificationList, "put", ConnMCService.PACKAGE_NAME_WHATSTAPP, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("WHATSTAPP")).thenReturn(false);
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "WHATSTAPP").thenCallRealMethod();

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }

    @Test
    public void testHandlerNotification_Case06() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(ConnMCService.PACKAGE_NAME_WHATSTAPP_BUSSINESS);
        PowerMockito.when(mNotificationList, "put", ConnMCService.PACKAGE_NAME_WHATSTAPP_BUSSINESS, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("WHATSTAPP")).thenReturn(false);
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "WHATSTAPP").thenCallRealMethod();

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }

    @Test
    public void testHandlerNotification_Case07() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(ConnMCService.PACKAGE_NAME_ZALO);
        PowerMockito.when(mNotificationList, "put", ConnMCService.PACKAGE_NAME_ZALO, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("ZALO")).thenReturn(false);
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "ZALO").thenCallRealMethod();

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }

    @Test
    public void testHandlerNotification_Case08() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(ConnMCService.PACKAGE_NAME_GMAIL);
        PowerMockito.when(mNotificationList, "put", ConnMCService.PACKAGE_NAME_GMAIL, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("GMAIL")).thenReturn(false);
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "GMAIL").thenCallRealMethod();

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }

    @Test
    public void testHandlerNotification_Case09() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(ConnMCService.PACKAGE_NAME_LINE);
        PowerMockito.when(mNotificationList, "put", ConnMCService.PACKAGE_NAME_LINE, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("LINE")).thenReturn(false);
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "LINE").thenCallRealMethod();

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }

    @Test
    public void testHandlerNotification_Case010() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(ConnMCService.PACKAGE_NAME_LINE_LITE);
        PowerMockito.when(mNotificationList, "put", ConnMCService.PACKAGE_NAME_LINE_LITE, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("LINE")).thenReturn(false);
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "LINE").thenCallRealMethod();

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }

    @Test
    public void testHandlerNotification_Case11() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(ConnMCService.PACKAGE_NAME_VIBER);
        PowerMockito.when(mNotificationList, "put", ConnMCService.PACKAGE_NAME_VIBER, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("VIBER")).thenReturn(false);
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "VIBER").thenCallRealMethod();

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }

    @Test
    public void testHandlerNotification_Case12() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(ConnMCService.PACKAGE_NAME_HANGOUT);
        PowerMockito.when(mNotificationList, "put", ConnMCService.PACKAGE_NAME_HANGOUT, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("HANGOUT")).thenReturn(false);
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "HANGOUT").thenCallRealMethod();

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }

    @Test
    public void testHandlerNotification_Case13() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(ConnMCService.PACKAGE_NAME_HANGOUT_CHAT);
        PowerMockito.when(mNotificationList, "put", ConnMCService.PACKAGE_NAME_HANGOUT_CHAT, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("HANGOUT CHAT")).thenReturn(false);
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "HANGOUT CHAT").thenCallRealMethod();

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }

    @Test
    public void testHandlerNotification_Case14() throws Exception {
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(ConnMCService.PACKAGE_NAME_FACEBOOK);
        PowerMockito.when(mNotificationList, "put", ConnMCService.PACKAGE_NAME_FACEBOOK, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("")).thenReturn(true);

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }

    @Test
    public void testHandlerNotification_Case15() throws Exception {
        String mPackageNameSms = "com.google.android.apps.messaging";
        Whitebox.setInternalState(mConnMCService, "mPackageNameSms", mPackageNameSms);
        HashMap<String, Short> mNotificationList = mock(HashMap.class);
        PowerMockito.when(mConnMCService, "handlerNotification", mStatusBarNotification, true).thenCallRealMethod();
        Whitebox.setInternalState(mConnMCService, "mNotificationList", mNotificationList);
        PowerMockito.when(mStatusBarNotification.getPackageName()).thenReturn(mPackageNameSms);
        PowerMockito.when(TextUtils.class, "equals", mPackageNameSms, mPackageNameSms).thenReturn(true);
        PowerMockito.when(mNotificationList, "put", mPackageNameSms, 0).thenAnswer(voidAnswer);
        PowerMockito.when(TextUtils.isEmpty("SMS")).thenReturn(false);
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "SMS").thenCallRealMethod();

        Whitebox.invokeMethod(mConnMCService, "handlerNotification", mStatusBarNotification, true);

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("handlerNotification", mStatusBarNotification, true);
    }


    @Test
    public void testResolveNoticeContent() throws Exception {
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "Have notice from SMS center").thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        PowerMockito.whenNew(VehicleNotificationMessage.class).withAnyArguments().thenReturn(message);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager, "sendMessage", message).thenAnswer(voidAnswer);

        Whitebox.invokeMethod(mConnMCService, "resolveNoticeContent", "Have notice from SMS center");

        PowerMockito.verifyPrivate(mConnMCService).invoke("resolveNoticeContent", "Have notice from SMS center");
    }

    @Test
    public void testOnNotificationRemoved() throws Exception {
        PowerMockito.when(mConnMCService, "onNotificationRemoved", mStatusBarNotification).thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);

        mConnMCService.onNotificationRemoved(mStatusBarNotification);

        verify(mConnMCService, atLeastOnce()).onNotificationRemoved(mStatusBarNotification);
    }

    @Test
    public void testOnReceiveMessageFromPhoneState() throws Exception {
        List<String> listMessage = mock(List.class);
        listMessage.add("Message1");
        listMessage.add("Message2");
        listMessage.add("Message3");
        PowerMockito.when(listMessage.size()).thenReturn(3);
        PowerMockito.when(mConnMCService, "onReceiveMessageFromPhoneState", listMessage).thenCallRealMethod();

        Iterator iterator = mock(Iterator.class);
        when(listMessage.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("Message1");
        PowerMockito.spy(Thread.class);

        // These two lines are tightly bound.
        PowerMockito.doThrow(new InterruptedException()).when(Thread.class);
        Thread.sleep(Mockito.anyLong());
        PowerMockito.when(mConnMCService, "resolveNoticeContent", "Message1").thenAnswer(voidAnswer);

        mConnMCService.onReceiveMessageFromPhoneState(listMessage);

        verify(mConnMCService, atLeastOnce()).onReceiveMessageFromPhoneState(listMessage);
    }

    @Test
    public void testReConnect_Case00() throws Exception {
        String name = "BTU00001";
        PowerMockito.when(mConnMCService, "reConnect").thenCallRealMethod();

        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothUtils.isBluetoothDeviceConnected()).thenReturn(true);

        Whitebox.invokeMethod(mConnMCService, "reConnect");

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("reConnect");
    }


    @Test
    public void testReConnect_Case01() throws Exception {
        String name = "BTU00001";
        PowerMockito.when(mConnMCService, "reConnect").thenCallRealMethod();
        PowerMockito.when(BluetoothUtils.isBluetoothDeviceConnected()).thenReturn(false);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        PowerMockito.when(mBluetoothPrefUtils.getString(KEY_BTU_NAME_REGISTER_SUCCESS)).thenReturn(name);
        PowerMockito.when(TextUtils.isEmpty(name)).thenReturn(false);
        PowerMockito.when(BluetoothManager.getInstance()).thenReturn(mBluetoothManager);
        PowerMockito.when(mBluetoothManager.reConnect(mContext)).thenReturn(true);

        Whitebox.invokeMethod(mConnMCService, "reConnect");

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("reConnect");
    }

    @Test
    public void testReConnect_Case02() throws Exception {
        String name = "BTU00001";
        PowerMockito.when(mConnMCService, "reConnect").thenCallRealMethod();
        PowerMockito.when(BluetoothUtils.isBluetoothDeviceConnected()).thenReturn(false);
        PowerMockito.when(LogUtils.class, "startEndMethodLog", anyBoolean()).thenAnswer(voidAnswer);
        PowerMockito.when(BluetoothPrefUtils.getInstance()).thenReturn(mBluetoothPrefUtils);
        PowerMockito.when(mBluetoothPrefUtils.getString(KEY_BTU_NAME_REGISTER_SUCCESS)).thenReturn(name);
        PowerMockito.when(TextUtils.isEmpty(name)).thenReturn(true);

        Whitebox.invokeMethod(mConnMCService, "reConnect");

        PowerMockito.verifyPrivate(mConnMCService, atLeastOnce()).invoke("reConnect");
    }
}
