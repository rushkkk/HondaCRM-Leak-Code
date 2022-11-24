package vn.co.honda.hondacrm.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.Calendar;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.btu.Receiver.AlarmReceiver;

import static android.content.Context.ALARM_SERVICE;

public class NotificationChangeOil {
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void showNotificationFirst(Context context, String body, String vin, int notificationId, Intent intent, int numInDay) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_change_oil)
                .setContentTitle("Please change oil")
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());

        if (hour + 24 / numInDay < 24) {
            setReminder(context, AlarmReceiver.class, hour + (24 / numInDay), body, vin);
        } else {
            setReminder(context, AlarmReceiver.class, 0, body, vin);
        }


//        int min = calendar.get(Calendar.MINUTE);
//        if (min+60/numInDay<60){
//            setReminder(context, AlarmReceiver.class, min + (60 / numInDay), body, vin);
//        }else {
//            setReminder(context, AlarmReceiver.class, 0, body, vin);
//        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void showNotification(Context context, String body, String vin, int notificationId, Intent intent, int numInDay) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_change_oil)
                .setContentTitle("Please change oil")
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
        StateChangeOilPre.getInstance(context).saveCountChangeOil(vin, StateChangeOilPre.getInstance(context).getCountChangeOil(vin) - 1);
        if (hour + 24 / numInDay < 24) {
            setReminder(context, AlarmReceiver.class, hour + (24 / numInDay), body, vin);
        } else {
            setReminder(context, AlarmReceiver.class, 0, body, vin);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setReminder(Context context, Class<?> cls, int hour, String body, String vin) {
        Calendar calendar = Calendar.getInstance();
        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, 0);
        setcalendar.set(Calendar.SECOND, 0);
        // cancel already scheduled reminders
//        cancelReminder(context,cls);

        if (setcalendar.before(calendar))
            setcalendar.add(Calendar.DAY_OF_MONTH, 1);

        // Enable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        intent1.putExtra("VIN", vin);
        intent1.putExtra("BODY", body);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    public static void cancelReminder(Context context, Class<?> cls) {
        // Disable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }


}
