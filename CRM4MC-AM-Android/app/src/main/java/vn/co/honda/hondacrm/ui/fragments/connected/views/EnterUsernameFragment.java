package vn.co.honda.hondacrm.ui.fragments.connected.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothPrefUtils;
import vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothUtils;
import vn.co.honda.hondacrm.btu.Interfaces.listener.connect.IF_VehicleConnectListener;
import vn.co.honda.hondacrm.btu.Utils.FragmentUtils;
import vn.co.honda.hondacrm.btu.Utils.KeyboardUtils;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;
import vn.co.honda.hondacrm.ui.activities.connected.view.base.BaseFragment;
import vn.co.honda.hondacrm.ui.activities.vehicles.ListVehicleActivity;
import vn.co.honda.hondacrm.ui.fragments.connected.local.model.VehicleEntity;
import vn.co.honda.hondacrm.ui.fragments.connected.local.roomDatabase.VehicleRoomDb;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.PrefUtils;

import static vn.co.honda.hondacrm.utils.Constants.KEY_BTU_NAME_REGISTER_SUCCESS;
import static vn.co.honda.hondacrm.utils.Constants.MY_LOG_TAG;
import static vn.co.honda.hondacrm.utils.Constants.TAB_CONNECTED;
import static vn.co.honda.hondacrm.utils.Constants.TYPE_VIEWPAGE;
import static vn.co.honda.hondacrm.utils.Constants.UPDATE_CURRENT_VIEWPAGER;


public class EnterUsernameFragment extends BaseFragment implements View.OnClickListener, IF_VehicleConnectListener {
    private VehicleRoomDb mDb;
    private VehicleEntity mVehicle;

    public EnterUsernameFragment() {
    }

    @Override
    protected int getIdLayout() {
        return R.layout.input_view;
    }

    @Override
    protected void initView() {
        String mJsonObjVehicle = PrefUtils.getStringPref(getContext(), "LAST_ADDED_VEHICLE");
        Gson gson = new Gson();
        mVehicle = new VehicleEntity();
        mVehicle = gson.fromJson(mJsonObjVehicle, VehicleEntity.class);
        mDb = VehicleRoomDb.getInstance(getContext());
    }

    @Override
    protected void onBackPressedFragment() {
        goToDeviceListFragment(FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);
    }

    @Override
    protected void setActionForView() {
        BluetoothManager.getInstance().setConnectListener(this);
        handleUsername();
    }

    private void handleUsername() {
        KeyboardUtils.hideKeyboard(mRootView);
        setNeedToShowDisconnectPopup(true);
        if (BluetoothUtils.isBluetoothDeviceConnected()) {
            showProgressDialog();
            Log.d(MY_LOG_TAG, "handleUsername true enter");
            BluetoothManager.getInstance().getBleRegisterController().registerUserName(mVehicle.getVinId());
        } else {
            Log.d(MY_LOG_TAG, "handleUsername false enter");
            showRegistrationProblem();
        }
    }

//    public void handlerTooManyUser(String userNameDelete) {
//        setNeedToShowDisconnectPopup(false);
//        mDialogTooManyUser = DialogCommon.createPopup(getActivity()
//                , getString(R.string.text_button_yes)
//                , getString(R.string.text_button_no)
//                , getString(R.string.msg_too_many_user)
//                , v -> {
//                    mDialogTooManyUser.dismiss();
//                    setNeedToShowDisconnectPopup(true);
//                    if (BluetoothUtils.isBluetoothDeviceConnected()) {
//                        BluetoothManager.getInstance().getBleRegisterController().replaceUser(userNameDelete);
//                    } else {
//                        showRegistrationProblem();
//                    }
//                }
//                , v -> {
//                    mDialogTooManyUser.dismiss();
//                    goToDeviceNotRegisterFragment();
//                });
//        mDialogTooManyUser.show();
//    }

    private void goToDeviceNotRegisterFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentUtils.clearAllFragment(fragmentManager);
            FragmentUtils.replaceFragment(fragmentManager, new ScanVehicleFragment(), R.id.fragment_container, false, FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);
        }
    }

    @Override
    public void onClick(View v) {
        if (!isEnableClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.btnBack:
                goToDeviceListFragment(FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_BACK);
                break;
            default:
                break;
        }
    }

    @Override
    public void onConnectStateNotice(BluetoothManager.EnumDeviceConnectStatus connectState) {
        if (!isFragmentAlife()) {
            return;
        }
        LogUtils.startEndMethodLog(true);
        LogUtils.d(connectState.name());
        hideProgressDialog();
        switch (connectState) {
            case DISCONNECTED:
                Log.d(MY_LOG_TAG, "DISCONNECTED enter");
                showRegistrationProblem();
                break;
            case BTU_ACTIVE:
                getActivity().runOnUiThread(() -> {
                    Log.d(MY_LOG_TAG, "BTU_ACTIVE enter");

                    if (PrefUtils.getStringPref(getContext(), Constants.VIN_OF_CONNECTED_VEHICLE_NOW) != "") {
                        BluetoothManager.getInstance().getBleConnectController().disconnectDevice();
                    }
                    // luu lai xe nao dang connected
                    PrefUtils.setStringPref(getContext(), Constants.VIN_OF_CONNECTED_VEHICLE_NOW, mVehicle.getVinId());

//                    String btuName = BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS);
//                    mVehicle.setBtuName(btuName);

                    try {
                        String btuName = BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS);
                        mVehicle.setBtuName(btuName);
                    } catch (Exception e) {
                        LogUtils.e("BTU name is empty!");
                    }
                    saveVehicleToRoom(mVehicle);
//
////                    Fragment fragment = new ConnectedContainerFragment();
////                    FragmentUtils.clearAllFragment(getFragmentManager());
////                    Bundle bundle = new Bundle();
////                    bundle.putBoolean(VehicleStatusFragment.KEY_WAITING_BTU_DISCONNECT, true);
////                    fragment.setArguments(bundle);
////                    FragmentUtils.replaceFragment(getActivity().getSupportFragmentManager(), fragment, R.id.fragment_container, false, FragmentUtils.FragmentAnimationType.TYPE_ANIMATION_GO);
//
                    Intent intent = new Intent();
                    intent.setAction(UPDATE_CURRENT_VIEWPAGER);
                    intent.putExtra(TYPE_VIEWPAGE, TAB_CONNECTED);
                    intent.putExtra(VehicleStatusFragment.KEY_WAITING_BTU_DISCONNECT, true);
                    getActivity().sendBroadcast(intent);
                    if (ListVehicleActivity.fa != null) {
                        ListVehicleActivity.fa.finish();
                    }


                    getActivity().finish();


//                    SaveDataAsyncTask s = new SaveDataAsyncTask();
//                    s.execute();

                });
                break;
            default:
                break;
        }
        LogUtils.startEndMethodLog(false);
    }

    @SuppressLint("StaticFieldLeak")
    public class SaveDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(MY_LOG_TAG, "doInBackground: 3 " + PrefUtils.getStringPref(getContext(), Constants.VIN_OF_CONNECTED_VEHICLE_NOW));
            String btuName = BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS);
            mVehicle.setBtuName(btuName);
            saveVehicleToRoom(mVehicle);
            Intent intent = new Intent();
            intent.setAction(UPDATE_CURRENT_VIEWPAGER);
            intent.putExtra(TYPE_VIEWPAGE, TAB_CONNECTED);
            intent.putExtra(VehicleStatusFragment.KEY_WAITING_BTU_DISCONNECT, true);
            getActivity().sendBroadcast(intent);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressDialog();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void saveVehicleToRoom(VehicleEntity v) {
        try {
            mDb.daoAccess().insertOnlySingleVehicle(v);
            Log.d(MY_LOG_TAG, " apiService 2 :saveVehicleToRoom");
        } catch (Exception e) {
            Log.d(MY_LOG_TAG, " Can't insert !");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BluetoothManager.getInstance().getBleConnectController().unRegisterListener(this);
    }
}
