package vn.co.honda.hondacrm.ui.activities.connected.view.base;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothUtils;
import vn.co.honda.hondacrm.utils.DialogUtils;


@SuppressWarnings("ResourceType")
public abstract class BaseActivity extends FragmentActivity {
    protected Dialog mBluetoothRequestDialog;

    private final BroadcastReceiver mBroadcastBluetoothState = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        onBluetoothOFF();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        onBluetoothON();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }

            }
        }
    };

    protected abstract int getIdLayout();

    protected abstract void initView();

    protected abstract void setActionForView();

    protected boolean isNeedCheckBlueToothWhenAppResume() {
        return false;
    }

    protected void onBluetoothON() {
        if (mBluetoothRequestDialog != null && mBluetoothRequestDialog.isShowing()) {
            mBluetoothRequestDialog.dismiss();
        }
        noticeBluetoothState(true);
    }

    protected void onBluetoothOFF() {
        if (!BluetoothUtils.checkBluetooth()) {
            showBluetoothRequestDialog();
        }
        noticeBluetoothState(false);
    }

    private void noticeBluetoothState(boolean stateON) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null && fragment instanceof BaseFragment) {
                if (((BaseFragment) fragment).isNeedReceiveBluetoothState()) {
                    if (stateON) {
                        ((BaseFragment) fragment).onBluetoothON();
                    } else {
                        ((BaseFragment) fragment).onBluetoothOFF();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getIdLayout());
        initView();
        setActionForView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedCheckBlueToothWhenAppResume()) {
            if (!BluetoothUtils.checkBluetooth()) {
                onBluetoothOFF();
            } else {
                onBluetoothON();
            }
        }
    }

    @Override
    protected void onStart() {
        IntentFilter filterBluetooth = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBroadcastBluetoothState, filterBluetooth);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcastBluetoothState);
    }

    private void showBluetoothRequestDialog() {
        runOnUiThread(() -> {
            if (!isDestroyed()) {
                if (mBluetoothRequestDialog == null) {
//                    mBluetoothRequestDialog = DialogCommon.createPopup(this,
//                            getString(R.string.text_button_dialog_quit),
//                            getString(R.string.text_button_dialog_ok),
//                            getString(R.string.text_message_require_bluetooth),
//                            view -> finishAffinity(), view -> {
//                                Intent intent =
//                                        new Intent(
//                                                Settings.ACTION_BLUETOOTH_SETTINGS);
//                                startActivity(intent);
//                            });

                    mBluetoothRequestDialog = DialogUtils.showDialogDefaultNew(this, R.layout.dialog_report, new DialogUtils.DialogListener() {
                        @Override
                        public void okButtonClick(Dialog dialog1) {
                            dialog1.dismiss();
                            Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                            startActivity(intent);
                        }

                        @Override
                        public void cancelButtonClick() {
                            mBluetoothRequestDialog.dismiss();
                        }
                    });
                    TextView tvContent = mBluetoothRequestDialog.findViewById(R.id.txt_title_logout);
                    tvContent.setText("This application requires bluetooth connection.\nPlease turn on bluetooth");


                }
                if (!mBluetoothRequestDialog.isShowing()) {
                    mBluetoothRequestDialog.show();
                }
                hideProgressDialog();
            }
        });
    }

    protected void hideProgressDialog() {
        Fragment f = getCurrentFragment();
        if (f == null) return;
        if (f instanceof BaseFragment) {
            ((BaseFragment) f).hideProgressDialog();
        }
    }

    @Nullable
    private Fragment getCurrentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm == null) {
            return null;
        }
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f == null) {
            return null;
        }
        return f;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm == null) {
            super.onBackPressed();
            return;
        }
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f == null) {
            super.onBackPressed();
            return;
        }
        if (f instanceof BaseFragment) {
            ((BaseFragment) f).onBackPressedFragment();
        } else {
            super.onBackPressed();
        }
    }
}
