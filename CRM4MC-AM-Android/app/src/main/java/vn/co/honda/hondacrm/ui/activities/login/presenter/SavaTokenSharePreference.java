package vn.co.honda.hondacrm.ui.activities.login.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import vn.co.honda.hondacrm.ui.activities.login.model.User;

public class SavaTokenSharePreference {

    public static final String PREF_TOKEN_FACEBOOK = "facebook";
    public static final String PREF_TOKEN_ZALO = "zalo";
    public static final String PREF_TOKEN_GOOGLE = "google";
    public static final String PREF_TOKEN_SIGNUP = "signup";


    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setTokenFacebook(Context ctx, String token)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_TOKEN_FACEBOOK, token);
        editor.commit();
    }
    public static String getTokenFacebook(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_TOKEN_FACEBOOK,"");
    }
    public static void setTokenZalo(Context ctx, String token)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_TOKEN_ZALO, token);
        editor.commit();
    }
    public static String getTokenZalo(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_TOKEN_ZALO,"");
    }
    public static void setTokenGoogle(Context ctx,String token)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_TOKEN_GOOGLE,token);
        editor.commit();
    }
    public static String getTokenGoogle(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_TOKEN_GOOGLE,"");
    }
    public static void setTokenSignup(Context ctx,String token)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_TOKEN_SIGNUP,token);
        editor.commit();
    }
    public static String getTokenSignup(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_TOKEN_SIGNUP,"");
    }

}
