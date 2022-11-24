package vn.co.honda.hondacrm;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.zing.zalo.zalosdk.oauth.ZaloSDKApplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.NativeLib.NativeLibController;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;
import vn.co.honda.hondacrm.di.application.AppComponent;
import vn.co.honda.hondacrm.di.application.AppModule;
import vn.co.honda.hondacrm.di.application.DaggerAppComponent;
import vn.co.honda.hondacrm.di.application.NetworkModule;

/**
 * @author CuongNV31
 */
public class App extends Application {

    private static App sApp;
    private AppComponent mAppComponent;

    public static App getApplication() {
        return sApp;
    }


    //
    private final boolean DEBUG_MODE_OFF = false;
    private final boolean DEBUG_MODE_ON = true;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        Log.d("TAGGG", getApplicationHashKey(this));
        initAppComponent();
        registerInternetBroadcastReceiver();


        //
        LogUtils.startEndMethodLog(true);
        LogUtils.setDebugMode(DEBUG_MODE_ON);
        new Thread(() -> NativeLibController.createInstance()).start();
        BluetoothManager.getInstance().init(this);
        LogUtils.startEndMethodLog(false);
    }

    private void registerInternetBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    private void initAppComponent() {

        FacebookSdk.sdkInitialize(this, new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {

            }
        });

        ZaloSDKApplication.wrap(this);

        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(this))
                .build();
        mAppComponent.inject(this);
    }

    public static String getApplicationHashKey(Context ctx) {
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sig = Base64.encodeToString(md.digest(), Base64.DEFAULT).trim();
                if (sig.trim().length() > 0) {
                    return sig;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
