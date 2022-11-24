package vn.co.honda.hondacrm.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;

public class PermissionUtils {

    public static void requestPermission(Activity context) {
        ActivityCompat.requestPermissions(context,
                new String[]{Manifest.permission.CAMERA},
                Constants.PERMISSION_REQUEST_CAMERA_CODE);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean neverAskAgainSelected(final Activity activity, final String permission) {
        final boolean prevShouldShowStatus = getRatinaleDisplayStatus(activity,permission);
        final boolean currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission);
        return prevShouldShowStatus != currShouldShowStatus;
    }

    public static void setShouldShowStatus(final Context context, final String permission) {
        SharedPreferences genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = genPrefs.edit();
        editor.putBoolean(permission, true);
        editor.commit();
    }


    public static void setShouldHideStatus(final Context context, final String permission) {
        SharedPreferences genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = genPrefs.edit();
        editor.putBoolean(permission, false);
        editor.commit();
    }

    public static boolean getRatinaleDisplayStatus(final Context context, final String permission) {
        SharedPreferences genPrefs =  context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        return genPrefs.getBoolean(permission, false);
    }

    public static void displayNeverAskAgainDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("We need to capture Picture for performing necessary task. Please permit the permission through "
                + "Settings screen.\n\nSelect Permissions -> Enable permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
