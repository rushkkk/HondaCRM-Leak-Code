package vn.co.honda.hondacrm.btu.Utils;


import vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothPrefUtils;

public class PrefUtils {

    public static class Settings {
        public static final String CURRENT_THEME = "current_theme";
        public static final String THEME_BLACK = "theme_black";
        public static final String THEME_RED = "theme_red";
        public static final String THEME_BLACK_TEXTURE = "theme_black_texture";
        public static final String THEME_RED_TEXTURE = "theme_red_texture";
    }

//    private static PrefUtils mInstance;
//    private SharedPreferences sSharedPreferences;

//    private PrefUtils() {
//        if (sSharedPreferences == null) {
//            sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(BluetoothManager.getInstance().getApplicationContext());
//        }
//    }
//
//    public static PrefUtils getInstance() {
//        if (mInstance == null) {
//            mInstance = new PrefUtils();
//        }
//        return mInstance;
//    }


    public static String getString(String key) {
        return BluetoothPrefUtils.getInstance().getString(key);
    }

    public static boolean setString(String key, String value) {
        return BluetoothPrefUtils.getInstance().setString(key, value);
    }

    public static int getInt(String key) {
        return BluetoothPrefUtils.getInstance().getInt(key);
    }

    public static boolean setInt(String key, int value) {
        return BluetoothPrefUtils.getInstance().setInt(key, value);
    }

    public static boolean setLong(String key, long value) {
        return BluetoothPrefUtils.getInstance().setLong(key, value);
    }

    public static long getLong(String key) {
        return BluetoothPrefUtils.getInstance().getLong(key);
    }


    public static boolean getBool(String key, boolean defValue) {
        return BluetoothPrefUtils.getInstance().getBool(key, defValue);
    }

    public static boolean getBool(String key) {
        return BluetoothPrefUtils.getInstance().getBool(key);
    }


    public static boolean setBool(String key, boolean value) {
        return BluetoothPrefUtils.getInstance().setBool(key, value);
    }

    public static boolean clearAllPreferences() {
        return BluetoothPrefUtils.getInstance().clearAllPreferences();
    }

    public static boolean removeKeyPref(String key) {
        return BluetoothPrefUtils.getInstance().removeKeyPref(key);
    }
}
