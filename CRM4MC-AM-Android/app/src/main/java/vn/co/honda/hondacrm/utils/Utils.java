package vn.co.honda.hondacrm.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.text.TextUtils;
public class Utils {

    // show keyboard
    public static void showKeyBoard(final Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }

    //ẩn bàn phím
    public static void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //an bàn phím
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public String convertNullObject(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        return content;
    }

    public static int convertGender(String gender) {
        if (TextUtils.isEmpty(gender)) {
            return 0;
        }
        if (gender.toLowerCase().equals("male")) {
            return 0;
        } else if (gender.toLowerCase().equals("female")) {
            return 1;
        }
        return 2;
    }
}
