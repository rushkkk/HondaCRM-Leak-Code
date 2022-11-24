package vn.co.honda.hondacrm.utils;

import android.app.Activity;
import android.content.Intent;

import vn.co.honda.hondacrm.R;

/**
 * Created by FPT.
 */
public class ActivityUtils {

    /**
     * Prevent instance creation.
     */
    private ActivityUtils() {
        // Do not instantiate.
    }

    /**
     * This method using for clear all previous activity when start new activity
     *
     * @param intent intent
     */
    public static void removeAllPreviousActivity(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    /**
     * This method using to start activity
     *
     * @param activity           Activity
     * @param intent             Intent
     * @param direction          Animation Direction
     * @param finishLastActivity true/false, finish activity or not
     */
    public static void startActivity(Activity activity, Intent intent, int direction, boolean finishLastActivity) {
        if (activity == null) return;
        activity.startActivity(intent);
        if (finishLastActivity) {
            activity.finish();
        }
        overridePendingTransition(activity, direction);
    }

    /**
     * This method using to start for result activity
     *
     * @param activity           Activity
     * @param intent             Intent
     * @param backCode           Key Code
     * @param direction          Animation Direction
     * @param finishLastActivity true/false, finish activity or not
     */
    public static void startActivityForResult(Activity activity, Intent intent, int backCode, int direction, boolean finishLastActivity) {
        if (activity == null) return;
        activity.startActivityForResult(intent, backCode);
        if (finishLastActivity) activity.finish();
        overridePendingTransition(activity, direction);
    }

    /**
     * This method using to BACK activity
     *
     * @param activity Activity
     */
    public static void back(Activity activity) {
        activity.finish();
        overridePendingTransition(activity, Constants.ANIMATE_BACK);
    }

    /**
     * This method using to set animation for transition activity
     *
     * @param activity  Activity
     * @param direction type animation
     */
    private static void overridePendingTransition(Activity activity, int direction) {
        if (direction == Constants.ANIMATE_FORWARD) {
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (direction == Constants.ANIMATE_BACK) {
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (direction == Constants.ANIMATE_EASE_IN_OUT) {
            activity.overridePendingTransition(R.anim.ease_in, R.anim.ease_out);
        } else if (direction == Constants.ANIMATE_SLIDE_TOP_FROM_BOTTOM) {
            activity.overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_none_medium_time);
        } else if (direction == Constants.ANIMATE_SLIDE_BOTTOM_FROM_TOP) {
            activity.overridePendingTransition(R.anim.slide_none_medium_time, R.anim.slide_top_to_bottom);
        } else if (direction == Constants.ANIMATE_SCALE_IN) {
            activity.overridePendingTransition(R.anim.popup_scale_in, R.anim.slide_none);
        } else if (direction == Constants.ANIMATE_SCALE_OUT) {
            activity.overridePendingTransition(R.anim.slide_none, R.anim.popup_scale_out);
        } else if (direction == Constants.ANIMATE_NONE) {
            //do nothing
            activity.overridePendingTransition(R.anim.slide_none, R.anim.slide_none);
        } else {
            activity.overridePendingTransition(R.anim.zoom_fade_in, R.anim.fade_out);
        }
    }
}
