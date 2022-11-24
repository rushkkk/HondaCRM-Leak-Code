package com.honda.connmc.Service;

import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Telephony;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Manager.PhoneStateManager;
import com.honda.connmc.Model.message.notification.VehicleNotificationMessage;
import com.honda.connmc.Utils.LogUtils;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_BTU_NAME_REGISTER_SUCCESS;
import static com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_REGISTER_SUCCESS;

public class ConnMCService extends NotificationListenerService implements PhoneStateManager.OnReceiveMessageFromPhoneState {

    private String mPackageNameSms;

    public static final String PACKAGE_NAME_HANGOUT = "com.google.android.talk";
    public static final String PACKAGE_NAME_HANGOUT_CHAT = "com.google.android.apps.dynamite";
    public static final String PACKAGE_NAME_GMAIL = "com.google.android.gm";
    public static final String PACKAGE_NAME_ZALO = "com.zing.zalo";
    public static final String PACKAGE_NAME_VIBER = "com.viber.voip";
    public static final String PACKAGE_NAME_LINE = "jp.naver.line.android";
    public static final String PACKAGE_NAME_LINE_LITE = "com.linecorp.linelite";
    public static final String PACKAGE_NAME_WHATSTAPP = "com.whatsapp";
    public static final String PACKAGE_NAME_WHATSTAPP_BUSSINESS = "com.whatsapp.w4b";
    public static final String PACKAGE_NAME_FACEBOOK = "com.facebook.katana";
    public static final String PACKAGE_NAME_FACEBOOK_WORK = "com.facebook.work";
    public static final String PACKAGE_NAME_FACEBOOK_MESSENGER = "com.facebook.orca";
    public static final String PACKAGE_NAME_FACEBOOK_MESSENGER_WORK = "com.facebook.workchat";
    private final int TIME_DELAY_MESSAGE = 1000;
    private HashMap<String, Short> mNotificationList;

    private final BroadcastReceiver mBroadcastBluetoothState = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        BluetoothManager.getInstance().disconnectDevice();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        reConnect();
                        break;
                    default:
                        break;
                }

            }
        }
    };

    private PhoneStateManager mPhoneStateManager;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.startEndMethodLog(true);
//        BluetoothManager.getInstance().getBleConnectController().setVehicleConnectListener(this);
        boolean isRegistered = BluetoothPrefUtils.getInstance().getBool(KEY_REGISTER_SUCCESS);
        if (isRegistered) {
            mPackageNameSms = Telephony.Sms.getDefaultSmsPackage(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String CHANNEL_ID = "connmc_id";
                int notificationId = 99;
                Notification notification = new Notification.Builder(this, CHANNEL_ID).setAutoCancel(true).build();
                startForeground(notificationId, notification);
            }
            IntentFilter filterBluetooth = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastBluetoothState, filterBluetooth);
            reConnect();
        }
        LogUtils.startEndMethodLog(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.startEndMethodLog(true);
//        BluetoothManager.getInstance().getBleConnectController().unRegisterListener(this);
        boolean isRegistered = BluetoothPrefUtils.getInstance().getBool(KEY_REGISTER_SUCCESS);
        if (isRegistered) {
            unregisterReceiver(mBroadcastBluetoothState);
        }
        LogUtils.startEndMethodLog(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.startEndMethodLog(true);
        mNotificationList = new HashMap<>();
        if (BluetoothPrefUtils.getInstance() == null) {
            return START_STICKY;
        }
        boolean isRegistered = BluetoothPrefUtils.getInstance().getBool(KEY_REGISTER_SUCCESS);
        if (isRegistered) {
            if (mPhoneStateManager == null) {
                LogUtils.d("init PhoneStateManager");
                mPhoneStateManager = new PhoneStateManager(this, this);
            }
            mPhoneStateManager.listenPhoneState();
        }
        LogUtils.startEndMethodLog(false);
        return START_STICKY;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        LogUtils.startEndMethodLog(true);
        handlerNotification(sbn, true);
        LogUtils.startEndMethodLog(false);
    }

    private void handlerNotification(StatusBarNotification sbn, boolean isNotificationPosted) {
        if (BluetoothPrefUtils.getInstance() == null) {
            return;
        }
        boolean isRegistered = BluetoothPrefUtils.getInstance().getBool(KEY_REGISTER_SUCCESS);
        if (isRegistered) {
            short count;
            String message = null;
            String packetName = sbn.getPackageName();
            if (TextUtils.isEmpty(packetName)) {
                LogUtils.e("Packet name is null");
                return;
            }
            Short countObj = null;
            if (mNotificationList != null && mNotificationList.size() > 0) {
                countObj = mNotificationList.get(packetName);
            }
            if (countObj == null) {
                count = 0;
            } else {
                count = countObj.shortValue();
            }
            if (isNotificationPosted) {
                count++;
            } else {
                count = 0;
            }
            LogUtils.d("PacketName : " + packetName);
            if (TextUtils.isEmpty(mPackageNameSms)) {
                mPackageNameSms = Telephony.Sms.getDefaultSmsPackage(this);
            }
            if (TextUtils.equals(packetName, mPackageNameSms)) {
                mNotificationList.put(packetName, count);
                message = "SMS      " + String.format("%03d", count);
            } else {
                switch (sbn.getPackageName()) {

//            case mPackageNameSms:
//            case ConnMCService.PACKAGE_NAME_SMS2:
//            case ConnMCService.PACKAGE_NAME_SMS_SONY:
//
//                break;
                    case ConnMCService.PACKAGE_NAME_FACEBOOK:
                        mNotificationList.put(packetName, count);
                        message = "FACEBOOK " + String.format("%03d", count);
                        break;
                    case ConnMCService.PACKAGE_NAME_FACEBOOK_WORK:
                        mNotificationList.put(packetName, count);
                        message = "FACEBOOK " + String.format("%03d", count);
                        break;
                    case ConnMCService.PACKAGE_NAME_FACEBOOK_MESSENGER:
                        mNotificationList.put(packetName, count);
                        message = "FACEBOOK " + String.format("%03d", count);
                        break;
                    case ConnMCService.PACKAGE_NAME_FACEBOOK_MESSENGER_WORK:
                        mNotificationList.put(packetName, count);
                        message = "FACEBOOK " + String.format("%03d", count);
                        break;
                    case ConnMCService.PACKAGE_NAME_WHATSTAPP:
                    case ConnMCService.PACKAGE_NAME_WHATSTAPP_BUSSINESS:
                        mNotificationList.put(packetName, count);
                        message = "WHATSTAPP" + String.format("%03d", count);
                        break;
                    case ConnMCService.PACKAGE_NAME_ZALO:
                        mNotificationList.put(packetName, count);
                        message = "ZALO     " + String.format("%03d", count);
                        break;
                    case ConnMCService.PACKAGE_NAME_GMAIL:
                        mNotificationList.put(packetName, count);
                        message = "GMAIL    " + String.format("%03d", count);
                        break;
                    case ConnMCService.PACKAGE_NAME_LINE:
                    case ConnMCService.PACKAGE_NAME_LINE_LITE:
                        mNotificationList.put(packetName, count);
                        message = "LINE     " + String.format("%03d", count);
                        break;
                    case ConnMCService.PACKAGE_NAME_VIBER:
                        mNotificationList.put(packetName, count);
                        message = "VIBER    " + String.format("%03d", count);
                        break;
                    case ConnMCService.PACKAGE_NAME_HANGOUT:
                        mNotificationList.put(packetName, count);
                        message = "HANGOUT  " + String.format("%03d", count);
                        break;
                    case ConnMCService.PACKAGE_NAME_HANGOUT_CHAT:
                        mNotificationList.put(packetName, count);
                        message = "HANGOUT  " + String.format("%03d", count);
                        break;
                    default:
                        break;
                }

            }
            if (TextUtils.isEmpty(message)) {
                LogUtils.e("unknown notice!");
            } else {
                if (count == 0) {
                    message = "";
                }
                LogUtils.d(message);
                resolveNoticeContent(message);
            }
        }
    }

    private void resolveNoticeContent(String message) {
        LogUtils.startEndMethodLog(true);
        if (BluetoothPrefUtils.getInstance() == null) {
            return;
        }
        message = deAccent(message);
        boolean isRegistered = BluetoothPrefUtils.getInstance().getBool(KEY_REGISTER_SUCCESS);
        if (isRegistered) {
            //Create Vehicle Transaction Request Message.
            VehicleNotificationMessage msg = new VehicleNotificationMessage(message);
            //Send message.
            BluetoothManager.getInstance().sendMessage(msg);
        }
        LogUtils.startEndMethodLog(false);
    }

    private String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        LogUtils.startEndMethodLog(true);
        handlerNotification(sbn, false);
        LogUtils.startEndMethodLog(false);
    }

    private void reConnect() {
        LogUtils.startEndMethodLog(true);
        if (BluetoothUtils.isBluetoothDeviceConnected()) {
            LogUtils.e("Bluetooth is connected => don't need reconnect!");
            return;
        }
        String btuName = BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS);
        if (TextUtils.isEmpty(btuName)) {
            LogUtils.e("BTU name is empty!");
            return;
        }
        LogUtils.d("Reconnect with BTU : " + btuName);
        BluetoothManager.getInstance().setDeviceCurrentStatus(BluetoothManager.EnumDeviceConnectStatus.INITIAL);
        BluetoothManager.getInstance().reConnect(this);
        LogUtils.startEndMethodLog(false);
    }

    @Override
    public void onReceiveMessageFromPhoneState(List<String> messages) {
        if (BluetoothPrefUtils.getInstance() == null) {
            return;
        }
        boolean isRegistered = BluetoothPrefUtils.getInstance().getBool(KEY_REGISTER_SUCCESS);
        if (isRegistered) {
            for (int i = 0; i < messages.size(); i++) {
                try {
                    Thread.sleep(i * TIME_DELAY_MESSAGE);
                } catch (InterruptedException e) {
                    LogUtils.e(e.toString());
                }
                resolveNoticeContent(messages.get(i));
            }
        }
    }

}
