package vn.co.honda.hondacrm.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by CuongNV31.
 */

public class PrefUtils {
    /**
     * Storing API Key in shared preferences to
     * add it in header part of every retrofit request
     */
    public PrefUtils() {
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
    }

    public static void storeAccessTokenKey(Context context, String accessToken) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("ACCESS_TOKEN", accessToken);
        editor.commit();
    }

    public static String getAccessTokenKey(Context context) {
        return getSharedPreferences(context).getString("ACCESS_TOKEN", null);
    }

    public static void storeTokenTypeKey(Context context, String accessToken) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("ACCESS_TOKEN_TYPE", accessToken);
        editor.commit();
    }

    public static String getTokenTypeKey(Context context) {
        return getSharedPreferences(context).getString("ACCESS_TOKEN_TYPE", null);
    }

    public static String getFullTypeAccessToken(Context context) {
        return getSharedPreferences(context).getString("ACCESS_TOKEN", null);
    }

    public static boolean setStringPref(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static boolean setIntPref(Context context, String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static String getStringPref(Context context, String key) {
        return getSharedPreferences(context).getString(key, "");
    }

}
