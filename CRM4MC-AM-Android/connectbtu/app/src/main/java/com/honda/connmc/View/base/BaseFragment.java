package com.honda.connmc.View.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.honda.connmc.Bluetooth.BluetoothManager;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.R;
import com.honda.connmc.Utils.FragmentUtils;
import com.honda.connmc.Utils.KeyboardUtils;
import com.honda.connmc.View.dialog.DialogCommon;
import com.honda.connmc.View.dialog.ProgressDialogCommon;
import com.honda.connmc.View.fragment.DeviceListFragment;

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

    protected boolean isNeedReceiveBluetoothState() {
        return false;
    }

    protected void onBluetoothON() {
    }

    protected void onBluetoothOFF() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getIdLayout(), container, false);
        }
        mRootView.setOnClickListener(v -> {
        });
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgressDialog();
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
        if (!isFragmentAlife()) {
            return;
        }
        if (!BluetoothUtils.checkBluetooth()) {
            return;
        }
        if (isNeedToShowDisconnectPopup && !(this instanceof DeviceListFragment)) {
            getActivity().runOnUiThread(() -> {
                hideProgressDialog();
                if (mDialogLostConnection == null) {
                    mDialogLostConnection = DialogCommon.createPopup(getActivity(), getString(R.string.text_button_dialog_ok), getString(R.string.connection_problem), view ->
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
            FragmentUtils.replaceFragment(fragmentManager, new DeviceListFragment(), R.id.fragment_container, false, type);
        }
    }
}
