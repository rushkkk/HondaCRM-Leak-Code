package com.honda.connmc.Bluetooth;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;

import com.honda.connmc.Bluetooth.connect.BleConnectController;
import com.honda.connmc.Bluetooth.message.DataMessageManager;
import com.honda.connmc.Bluetooth.register.BleRegisterController;
import com.honda.connmc.Bluetooth.scan.BleScanController;
import com.honda.connmc.Bluetooth.utils.BluetoothPrefUtils;
import com.honda.connmc.Bluetooth.utils.BluetoothUtils;
import com.honda.connmc.Interfaces.listener.connect.IF_VehicleConnectListener;
import com.honda.connmc.Interfaces.listener.register.IF_VehicleRegisterListener;
import com.honda.connmc.Interfaces.listener.scan.IF_VehicleScanListener;
import com.honda.connmc.Interfaces.listener.transition.IF_BluetoothTransferDataListener;
import com.honda.connmc.Model.message.common.VehicleMessage;
import com.honda.connmc.Utils.LogUtils;

public class BluetoothManager {
    private static BluetoothManager sInstance;
    private DataMessageManager mMsgManager;
    private BleConnectController mBleConnectController;
    private BleScanController mScanController;
    private BleRegisterController mBleRegisterController;
    private GattCallback mGattCallback;
    private EnumDeviceConnectStatus mDeviceCurrentStatus;
    private int mTransactionIdentity;
    private Application mApplicationContext;
    public enum EnumDeviceConnectStatus {
        INITIAL,
        CONNECTED,
        DISCONNECTED,
        BTU_INACTIVE,
        BTU_INACTIVE_SECOND_TIME,
        BTU_ACTIVE,
        BTU_UNREGISTERED
    }

    private BluetoothManager() {
    }

    public int getTransactionIdentity() {
        return mTransactionIdentity;
    }

    public void setTransactionIdentity(int mTransactionIdentity) {
        this.mTransactionIdentity = mTransactionIdentity;
    }

    public void init(Application application) {
        this.mApplicationContext = application;
        mTransactionIdentity = 0;
        mDeviceCurrentStatus = EnumDeviceConnectStatus.INITIAL;
        mGattCallback = new GattCallback();
        mBleConnectController = new BleConnectController(mGattCallback);
        mScanController = new BleScanController();
        mBleRegisterController = new BleRegisterController();
        mMsgManager = new DataMessageManager(mGattCallback);

        mGattCallback.setConnectListener(mBleConnectController);
        mGattCallback.setDataListener(mMsgManager);
    }

    public void resetData() {
        mTransactionIdentity = 0;
        mDeviceCurrentStatus = EnumDeviceConnectStatus.INITIAL;
        mBleConnectController.disconnectDevice();
        mBleRegisterController.resetData();
        mScanController.resetData();
    }

    public static BluetoothManager getInstance() {
        if (sInstance == null) {
            sInstance = new BluetoothManager();
        }
        return sInstance;
    }

    public EnumDeviceConnectStatus getDeviceCurrentStatus() {
        return mDeviceCurrentStatus;
    }

    public void setDeviceCurrentStatus(EnumDeviceConnectStatus mDeviceCurrentStatus) {
        this.mDeviceCurrentStatus = mDeviceCurrentStatus;
    }

    public void setScanListener(IF_VehicleScanListener scanListener) {
        if (mScanController != null) {
            mScanController.setVehicleScanListener(scanListener);
        }
    }

    public void setConnectListener(IF_VehicleConnectListener connectListener) {
        if (mBleConnectController != null) {
            mBleConnectController.setVehicleConnectListener(connectListener);
        }
    }

    public void setRegisterListener(IF_VehicleRegisterListener registerListener) {
        if (mBleRegisterController != null) {
            mBleRegisterController.setRegistrationEventListener(registerListener);
        }
    }

    public void setMessageListener(IF_BluetoothTransferDataListener msgListener) {
        if (mMsgManager != null) {
            mMsgManager.setListener(msgListener);
        }
    }

    public void unRegisterMessageListener(IF_BluetoothTransferDataListener msgListener){
        if (mMsgManager != null) {
            mMsgManager.unRegisterListener(msgListener);
        }
    }

    public void unRegisterDevice(){
        disconnectDevice();
        resetData();
        mBleRegisterController.unRegister();
    }

    public boolean startScan(Context context) {
        if (BluetoothUtils.checkBluetooth()) {
            return mScanController != null && mScanController.startScan(context);
        }
        return false;
    }

    public boolean startScan(Context context, String deviceName) {
        return mScanController != null && mScanController.startScan(context, deviceName);
    }

    public boolean stopScan() {
        boolean canStopScan = mScanController != null && mScanController.stopScan();
        if (canStopScan) {
            mScanController.resetData();
        }
        return canStopScan;
    }

    public boolean startConnect(BluetoothDevice bluetoothDevice) {
        return mBleConnectController != null && mBleConnectController.startConnect(bluetoothDevice);
    }

    public boolean disconnectDevice() {
        boolean disconnect = mBleConnectController != null && mBleConnectController.disconnectDevice();
        return disconnect;
    }

    public boolean reConnect(Context context) {
        String btuName = BluetoothPrefUtils.getInstance().getString(BluetoothPrefUtils.RegisterKey.KEY_BTU_NAME_REGISTER_SUCCESS);
        if (TextUtils.isEmpty(btuName)) {
            LogUtils.e("BTU name is empty!");
            return false;
        }
        return mBleConnectController != null && mBleConnectController.reConnect(context);
    }

    public BleConnectController getBleConnectController() {
        return mBleConnectController;
    }

    public void sendMessage(VehicleMessage msg) {
        mMsgManager.sendMessage(msg);
    }

    public DataMessageManager getDataMessageManager() {
        return mMsgManager;
    }

    public BleRegisterController getBleRegisterController() {
        return mBleRegisterController;
    }

    public Application getApplicationContext() {
        return mApplicationContext;
    }
}
