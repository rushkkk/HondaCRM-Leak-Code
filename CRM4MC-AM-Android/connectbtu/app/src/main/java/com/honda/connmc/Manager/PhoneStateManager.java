package com.honda.connmc.Manager;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.honda.connmc.Utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

public class PhoneStateManager {
    private Context mContext;
    private boolean isRing = false;
    private boolean isCallReceived = false;

    public PhoneStateManager(Context context, OnReceiveMessageFromPhoneState onReceiveMessageFromPhoneState) {
        this.mContext = context;
        this.mOnReceiveMessageFromPhoneState = onReceiveMessageFromPhoneState;
    }

    public interface OnReceiveMessageFromPhoneState {
        void onReceiveMessageFromPhoneState(List<String> message);
    }

    private OnReceiveMessageFromPhoneState mOnReceiveMessageFromPhoneState;
    private final PhoneStateListener phoneStateListener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (!TextUtils.isEmpty(incomingNumber)) {
                String contactName = getContactName(mContext, incomingNumber);
                if (TextUtils.isEmpty(contactName)) {
                    contactName = incomingNumber;
                }
                List<String> messages = new ArrayList<>();
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (isRing == true && isCallReceived == false) {
                            messages.add("MISSED CALL ");
                        }
                        isRing = false;
                        isCallReceived = false;
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        isRing = true;
                        messages.add("INCOMINGCALL");
                        messages.add(contactName);
                        messages.add("");
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        isCallReceived = true;
                        break;
                    default:
                        break;
                }
                if (!messages.isEmpty() && mOnReceiveMessageFromPhoneState != null) {
                    mOnReceiveMessageFromPhoneState.onReceiveMessageFromPhoneState(messages);
                }
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    };

    public String getContactName(Context context, String phoneNumber) {
        if (PermissionUtils.hasPermissions(context, Manifest.permission.READ_CONTACTS)) {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

            String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

            String contactName = "";
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    contactName = cursor.getString(0);
                }
                cursor.close();
            }

            return contactName;
        }
        return null;
    }

    public void listenPhoneState() {
        if (PermissionUtils.hasPermissions(mContext, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
            tm.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    public void stoplistenPhoneState() {
        if (PermissionUtils.hasPermissions(mContext, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
            tm.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
    }


}
