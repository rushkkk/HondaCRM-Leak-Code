package vn.co.honda.hondacrm.btu.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import vn.co.honda.hondacrm.utils.PrefUtils;

import static vn.co.honda.hondacrm.utils.Constants.ACTION_UPDATE_API;

public class PushDataFromBTUService extends Service {

    private final String TIME_TO_UPDATE_VEHICLE_DETAIL = "TIME_TO_UPDATE_VEHICLE_DETAIL";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (PrefUtils.getStringPref(this, TIME_TO_UPDATE_VEHICLE_DETAIL).length() > 0) {
            handler.postDelayed(runnable, Integer.parseInt(PrefUtils.getStringPref(this, TIME_TO_UPDATE_VEHICLE_DETAIL)) * 1000);
        } else {
            handler.postDelayed(runnable, 5 * 1000);
        }
    }




    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(ACTION_UPDATE_API);
            sendBroadcast(broadcastIntent);
            if (PrefUtils.getStringPref(getApplicationContext(), TIME_TO_UPDATE_VEHICLE_DETAIL).length() > 0) {
                handler.postDelayed(runnable, Integer.parseInt(PrefUtils.getStringPref(getApplicationContext(), TIME_TO_UPDATE_VEHICLE_DETAIL)) * 60 * 1000);
            } else {
                handler.postDelayed(runnable, 5 * 60 * 1000);
            }
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
