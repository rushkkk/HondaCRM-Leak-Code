package vn.co.honda.hondacrm.btu.Bluetooth.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;


public class BluetoothPrefUtils {

    public static class RegisterKey {
        public static final String KEY_REGISTER_SUCCESS = "KEY_REGISTER_SUCCESS";
        public static final String KEY_BTU_NAME_REGISTER_SUCCESS = "key_btu_name_register_success";
        public static final String KEY_USER_NAME_REGISTER_SUCCESS = "key_user_name_register_success";
    }

    private static BluetoothPrefUtils mInstance;
    private SharedPreferences sSharedPreferences;

    private BluetoothPrefUtils() {
        if (sSharedPreferences == null) {
            sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(BluetoothManager.getInstance().getApplicationContext());
        }
    }

    public static BluetoothPrefUtils getInstance() {
        if (mInstance == null) {
            mInstance = new BluetoothPrefUtils();
        }
        return mInstance;
    }

    public String getUserName(){
        return getString(RegisterKey.KEY_USER_NAME_REGISTER_SUCCESS);
    }

    public void saveUserName(String userName){
        setString(RegisterKey.KEY_USER_NAME_REGISTER_SUCCESS, userName);
    }

    public String getString(String key) {
        return sSharedPreferences.getString(key, "");
    }

    public boolean setString(String key, String value) {
        Editor editor = sSharedPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public int getInt(String key) {
        return sSharedPreferences.getInt(key, -1);
    }

    public boolean setInt(String key, int value) {
        Editor editor = sSharedPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public boolean setLong(String key, long value) {
        Editor editor = sSharedPreferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public long getLong(String key) {
        return sSharedPreferences.getLong(key, 0L);
    }

    public long getLongStep(String key) {
        long value;
        try {
            value = sSharedPreferences.getLong(key, -1);
        } catch (ClassCastException e) {
            value = sSharedPreferences.getInt(key, -1);
            setLong(key, value);
        }
        return value;
    }

    public boolean getBool(String key, boolean defValue) {
        return sSharedPreferences.getBoolean(key, defValue);
    }

    public boolean getBool(String key) {
        return sSharedPreferences.getBoolean(key, false);
    }

    public boolean getBoolDefaultTrue(String key) {
        return sSharedPreferences.getBoolean(key, true);
    }

    public boolean setBool(String key, boolean value) {
        Editor editor = sSharedPreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean clearAllPreferences() {
        return sSharedPreferences.edit().clear().commit();
    }

    public boolean removeKeyPref(String key) {
        return sSharedPreferences.edit().remove(key).commit();
    }
}
