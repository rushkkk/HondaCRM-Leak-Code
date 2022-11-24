package vn.co.honda.hondacrm.utils;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseUtil {
    public static final String EVENT_LOGIN_PHONE = "phone";
    public static final String EVENT_SINGUP_PHONE = "phone";
    public static final String EVENT_SINGUP_GOOGLE = "google";
    public static final String EVENT_SINGUP_ZALO = "zalo";
    public static final String EVENT_SINGUP_FACEBOOK = "facebook";

    /**
     * Handle tracking event Login.
     *
     * @param firebaseAnalytics firebaseAnalytics
     * @param params            params
     */
    public static void logEventLogin(FirebaseAnalytics firebaseAnalytics, String params) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, params);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

        //Sets whether analytics collection is enabled for this app on this device.
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
    }

    /**
     * Handle tracking event Sing up.
     *
     * @param firebaseAnalytics firebaseAnalytics
     * @param params            params
     */
    public static void logEventSignup(FirebaseAnalytics firebaseAnalytics, String params) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, params);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);
    }
}
