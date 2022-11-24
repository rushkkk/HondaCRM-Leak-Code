package vn.co.honda.hondacrm.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class StateConnectPre {
    private static StateConnectPre stateConnectPre;
    private SharedPreferences sharedPreferences;

    public static StateConnectPre getInstance(Context context) {
        if (stateConnectPre == null) {
            stateConnectPre = new StateConnectPre(context);
        }
        return stateConnectPre;
    }

    private StateConnectPre(Context context) {
        sharedPreferences = context.getSharedPreferences("DATASTATECONNECTED", Context.MODE_PRIVATE);
    }

    public void saveState(String key, int value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    public int getState(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, -1);
        }
        return -1;
    }
}