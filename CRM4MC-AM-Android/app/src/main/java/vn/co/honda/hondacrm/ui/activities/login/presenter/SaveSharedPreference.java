package vn.co.honda.hondacrm.ui.activities.login.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import vn.co.honda.hondacrm.ui.activities.login.model.User;

public class SaveSharedPreference {
    public static final String PREF_NUMBER_PHONE = "numberphone";
    public static final String PREF_PASSWORD= "password";
    public static final String PREF_FULLNAME= "fullname";
    public static final String PREF_AVATAR= "avatar";
    public static final String PREF_GENDER= "gender";
    public static final String PREF_BIRTDAY= "birtday";
    public static final String PREF_EMAIL= "email";
    public static final String PREF_FIRST_LOGIN= "firstlogin";
    public static final String PREF_COUNT_LOGIN = "countlogin";




    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUser(Context ctx, User user)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_NUMBER_PHONE, user.getNumberPhone());
        editor.putString(PREF_PASSWORD, user.getPassword());

        editor.apply();
    }
    public static void setInforUser(Context ctx , User user){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_FULLNAME, user.getFullName());
        editor.putString(PREF_AVATAR, user.getAvatar());
        editor.putString(PREF_GENDER,user.getGender());
        editor.putString(PREF_BIRTDAY,user.getBirtDay());
        editor.putString(PREF_EMAIL,user.getEmail());

        editor.apply();
    }
    public static void setFirstLogin(Context ctx, boolean b){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_FIRST_LOGIN, b);
        editor.apply();
    }
    public static void setCountLogin(Context ctx, int count){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_COUNT_LOGIN, count);
        editor.apply();
    }
    public static String getGender(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_GENDER,"");
    }
    public static String getBirtday(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_BIRTDAY,"");
    }


    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_NUMBER_PHONE, "");
    }
    public static String getPassword(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_PASSWORD, "");
    }
    public static String getFullname(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_FULLNAME, "");
    }
    public static String getAvatar(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_AVATAR, "");
    }
    public static String getEmail(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_EMAIL, "");
    }

    public static boolean getFirstLogin(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_FIRST_LOGIN, true);
    }
    public static int getCountLogin(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(PREF_COUNT_LOGIN, 0);
    }
}
