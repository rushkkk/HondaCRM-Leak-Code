package vn.co.honda.hondacrm.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class StateChangeOilPre {
    private static StateChangeOilPre stateConnectPre;
    private SharedPreferences sharedPreferences;

    public static StateChangeOilPre getInstance(Context context) {
        if (stateConnectPre == null) {
            stateConnectPre = new StateChangeOilPre(context);
        }
        return stateConnectPre;
    }

    private StateChangeOilPre(Context context) {
        sharedPreferences = context.getSharedPreferences("DATACHANGEOIL", Context.MODE_PRIVATE);
    }

    public void saveCountChangeOil(String key, int value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    public int getCountChangeOil(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, -1);
        }
        return -1;
    }
}