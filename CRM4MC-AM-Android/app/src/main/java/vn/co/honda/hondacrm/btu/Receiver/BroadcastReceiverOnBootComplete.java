package vn.co.honda.hondacrm.btu.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothPrefUtils;
import vn.co.honda.hondacrm.btu.Service.ConnMCService;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;

import static vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_REGISTER_SUCCESS;


public class BroadcastReceiverOnBootComplete extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.startEndMethodLog(true);
        if (BluetoothPrefUtils.getInstance() == null) {
            LogUtils.e("BluetoothPrefUtils is null");
            return;
        }
        boolean isRegistered = BluetoothPrefUtils.getInstance().getBool(KEY_REGISTER_SUCCESS);
        if (isRegistered) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                Intent serviceIntent = new Intent(context, ConnMCService.class);
                context.startService(serviceIntent);
            }
        }
        LogUtils.startEndMethodLog(false);
    }
}
