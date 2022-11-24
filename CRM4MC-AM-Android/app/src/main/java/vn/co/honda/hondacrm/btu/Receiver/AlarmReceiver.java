package vn.co.honda.hondacrm.btu.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import vn.co.honda.hondacrm.utils.NotificationChangeOil;
import vn.co.honda.hondacrm.utils.StateChangeOilPre;

public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        String body = intent.getStringExtra("BODY");
        String vin = intent.getStringExtra("VIN");
        if (StateChangeOilPre.getInstance(context).getCountChangeOil(vin) > 0) {
            NotificationChangeOil.showNotification(context, body, vin, 1, intent, 2);
        } else {
            if (StateChangeOilPre.getInstance(context).getCountChangeOil("vin") == 0) {
                StateChangeOilPre.getInstance(context).saveCountChangeOil("vin", -1);
                Intent i = new Intent();
                i.setAction("UPDATECHNAGEOIL");
                i.putExtra("VINCHANGEOIL", vin);
                i.putExtra("ISCHANGEOIL", true);
                context.sendBroadcast(i);
            }
        }
    }
}