package vn.co.honda.hondacrm.ui.activities.connected.view.base;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothUtils;
import vn.co.honda.hondacrm.btu.Utils.DialogCommon;
import vn.co.honda.hondacrm.btu.Utils.FragmentUtils;
import vn.co.honda.hondacrm.btu.Utils.KeyboardUtils;
import vn.co.honda.hondacrm.btu.Utils.ProgressDialogCommon;
import vn.co.honda.hondacrm.ui.fragments.connected.views.ScanVehicleFragment;

@SuppressWarnings("ResourceType")
public abstract class BaseFragment extends Fragment {
    protected ProgressDialogCommon mProgressDialog;
    protected Dialog mNotificationAccessDialog;
    private Dialog mDialogLostConnection;
    protected View mRootView;
    private long mLastClickTime = 0;
    private static final int TIME_DELAY_CLICK = 1000;
    protected boolean isNeedToShowDisconnectPopup;

    private static final String INTENT_ACTION_NOTIFICATION_ACCESS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    protected abstract int getIdLayout();

    protected abstract void initView();

    protected abstract void onBackPressedFragment();

    protected abstract void setActionForView();

    public boolean isNeedReceiveBluetoothState() {
        return false;
    }

    public void onBluetoothON() {
    }

    public void onBluetoothOFF() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getIdLayout(), container, false);
        }
        mRootView.setOnClickListener(v -> {
        });
        IntentFilter filterBluetooth = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(mBroadcastBluetoothState, filterBluetooth);
        initView();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActionForView();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findViewById(int viewId) {
        return (T) mRootView.findViewById(viewId);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDialogLostConnection != null && !mDialogLostConnection.isShowing()) {
            mDialogLostConnection.show();
        }
        if (mNotificationAccessDialog != null && !mNotificationAccessDialog.isShowing()) {
            mNotificationAccessDialog.show();
        }
        IntentFilter filterBluetooth = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(mBroadcastBluetoothState, filterBluetooth);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgressDialog();
        getActivity().unregisterReceiver(mBroadcastBluetoothState);
        KeyboardUtils.hideKeyboard(mRootView);
    }

    protected boolean isEnableClick() {
        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < TIME_DELAY_CLICK) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }

    public void showProgressDialog() {
        if (!isFragmentAlife()) {
            return;
        }
        getActivity().runOnUiThread(() -> {
            mProgressDialog = ProgressDialogCommon.newInstance();
            mProgressDialog.show(getFragmentManager());
        });
    }

    public void hideProgressDialog() {
        if (!isFragmentAlife()) {
            return;
        }
        getActivity().runOnUiThread(() -> {
            if (mProgressDialog != null) {
                mProgressDialog.dismissAllowingStateLoss();
                mProgressDialog = null;
            }
        });
    }

    protected boolean isFragmentAlife() {
        if (getActivity() == null || isDetached()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get resource id of an attribute reference.
     *
     * @param attr R.attr.
     * @return res id.
     */
    protected int getResId(int attr) {
        if (attr == 0 || getActivity() == null)
            return 0;
        final TypedValue typedvalueattr = new TypedValue();

        getActivity().getTheme().resolveAttribute(attr, typedvalueattr, true);
        return typedvalueattr.resourceId;
    }

    protected void processCheckNotificationAccess() {
        if (BluetoothManager.getInstance().getDeviceCurrentStatus() == BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE) {
            if (isNeedCheckNotificationAccess()) {
                if (!BluetoothUtils.checkNotificationAccsess()) {
                    showNotificationAccessDialog();
                } else {
                    dismissNotificationAccessPopup();
                }
            }
        } else {
            dismissNotificationAccessPopup();
        }
    }

    protected void dismissNotificationAccessPopup() {
        if (!isFragmentAlife()) {
            return;
        }
        if (mNotificationAccessDialog != null && mNotificationAccessDialog.isShowing()) {
            mNotificationAccessDialog.dismiss();
            mNotificationAccessDialog = null;
        }
    }

    public void showNotificationAccessDialog() {
        if (!isFragmentAlife()) {
            return;
        }
        getActivity().runOnUiThread(() -> {
            if (!getActivity().isDestroyed()) {
                if (mNotificationAccessDialog == null) {
                    mNotificationAccessDialog = DialogCommon.createPopup(getActivity(),
                            getString(R.string.text_button_dialog_cancel),
                            getString(R.string.text_button_dialog_ok),
                            getString(R.string.text_message_require_notification),
                            view -> mNotificationAccessDialog.dismiss(), view -> {
                                Intent i = new Intent(
                                        INTENT_ACTION_NOTIFICATION_ACCESS);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            });

                }
                if (isResumed() && !mNotificationAccessDialog.isShowing()) {
                    mNotificationAccessDialog.show();
                }
            }
        });
    }

    protected boolean isNeedCheckNotificationAccess() {
        return false;
    }

    protected void showRegistrationProblem() {
        Log.e("hoaiii", "showRegistrationProblem");
        if (!isFragmentAlife()) {
            return;
        }
        if (!BluetoothUtils.checkBluetooth()) {
            return;
        }
        if (isNeedToShowDisconnectPopup && !(this instanceof ScanVehicleFragment)) {
            getActivity().runOnUiThread(() -> {
                hideProgressDialog();
                if (mDialogLostConnection == null) {
                    mDialogLostConnection = DialogCommon.createPopup(getActivity(), getString(R.string.connection_problem_), getString(R.string.text_button_dialog_ok), view ->
                    {
                        goToDeviceListFragment(FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);
                        mDialogLostConnection = null;
                    });
                }
                if (isResumed() && !mDialogLostConnection.isShowing()) {
                    mDialogLostConnection.show();
                }
            });
        }
    }

    public void setNeedToShowDisconnectPopup(boolean needToShowDisconnectPopup) {
        isNeedToShowDisconnectPopup = needToShowDisconnectPopup;
    }

    protected void goToDeviceListFragment(FragmentUtils.FragmentAnimationType type) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentUtils.clearAllFragment(fragmentManager);
            FragmentUtils.replaceFragment(fragmentManager, new ScanVehicleFragment(), R.id.fragment_container, false, type);
        }
    }

    /**
     *
     */

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

}
