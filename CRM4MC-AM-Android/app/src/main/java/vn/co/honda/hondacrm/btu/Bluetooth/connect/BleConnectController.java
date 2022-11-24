package vn.co.honda.hondacrm.btu.Bluetooth.connect;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.Bluetooth.GattCallback;
import vn.co.honda.hondacrm.btu.Bluetooth.scan.BleScanController;
import vn.co.honda.hondacrm.btu.Bluetooth.timer.IF_TimeOutListener;
import vn.co.honda.hondacrm.btu.Bluetooth.timer.IF_TimeScheduleListener;
import vn.co.honda.hondacrm.btu.Bluetooth.timer.TimeManager;
import vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothPrefUtils;
import vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothUtils;
import vn.co.honda.hondacrm.btu.Constants.TimeConstants;
import vn.co.honda.hondacrm.btu.Constants.UuidConstants;
import vn.co.honda.hondacrm.btu.Interfaces.listener.connect.IF_VehicleConnectListener;
import vn.co.honda.hondacrm.btu.Interfaces.listener.scan.IF_VehicleScanListener;
import vn.co.honda.hondacrm.btu.Model.BluetoothScan;
import vn.co.honda.hondacrm.btu.NativeLib.NativeLibController;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;
import vn.co.honda.hondacrm.btu.Utils.PrefUtils;

import static vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_BTU_NAME_REGISTER_SUCCESS;
import static vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothPrefUtils.RegisterKey.KEY_REGISTER_SUCCESS;


public class BleConnectController implements GattCallback.ConnectListener, IF_TimeOutListener, IF_VehicleScanListener {

    private final TimeManager mTimeManager;
    private BluetoothGatt mGatt;
    private Map<String, BluetoothGattService> mServices = null;
    private Map<String, BluetoothGattCharacteristic> mCharacteristics = null;
    private static final byte[] DUMMY_DATA = {0x00};
    private static final int MIN_ATT_SIZE = 6;
    private static final int TIME_RECONNECT = 500;
    private static final int ATT_MTU_SUPPORTED_DATASIZE = 2;
    public static final int DEFAULT_ATTMTU = 23;
    private int mAttSize = DEFAULT_ATTMTU;
    private List<IF_VehicleConnectListener> mListeners;
    //    private IF_VehicleConnectListener mListener;
    private GattCallback mGattCallback;
    private boolean mIsConnecting = false;
    private BluetoothDevice mBluetoothDeviceConnect;
    private final Object LOCK_OBJECT = new Object();

    public BleConnectController(GattCallback gattCallback) {
        mGattCallback = gattCallback;
        mListeners = new ArrayList<>();
        mTimeManager = new TimeManager();
    }

    /**
     * Set listener for bluetooth connection changes.
     *
     * @param vehicleConnectListener IF_VehicleConnectListener.
     */
    public void setVehicleConnectListener(IF_VehicleConnectListener vehicleConnectListener) {
        if (vehicleConnectListener == null) {
            LogUtils.e("Listener connect : null!");
            return;
        }
        LogUtils.d("Listener connect : " + vehicleConnectListener.getClass().getCanonicalName());
//        this.mListener = vehicleConnectListener;
        if (!mListeners.contains(vehicleConnectListener)) {
            mListeners.add(vehicleConnectListener);
        }
        LogUtils.d("SIZE listener: " + mListeners.size());
    }

    /**
     * Unregister Connect listener
     *
     * @param vehicleConnectListener IF_VehicleConnectListener
     * @return true if removed successfully.
     */
    public void unRegisterListener(IF_VehicleConnectListener vehicleConnectListener) {
        if (mListeners.contains(vehicleConnectListener)) {
            mListeners.remove(vehicleConnectListener);
        }
    }

    public boolean startConnect(BluetoothDevice bluetoothDevice) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            if (mIsConnecting) {
                LogUtils.e("connect progress is running!");
                return false;
            }
            startTimer();
            mIsConnecting = true;
            LogUtils.startEndMethodLog(false);
            return connectDevice(bluetoothDevice);
        }
    }

    private void startTimer() {
        LogUtils.d("Start timer!");
        if (mTimeManager != null) {
            mTimeManager.startTimer(this, TimeConstants.TIME_IN_PROCESS);
        }
    }

    private boolean connectDevice(BluetoothDevice bluetoothDevice) {
        this.mBluetoothDeviceConnect = bluetoothDevice;
        mGatt = bluetoothDevice.connectGatt(BluetoothManager.getInstance().getApplicationContext(), false, mGattCallback);
        return mGatt != null;
    }

    public boolean disconnectDevice() {
        synchronized (LOCK_OBJECT) {
            mIsConnecting = false;
            mBluetoothDeviceConnect = null;
            removeServiceCharacteristicList();
            if (mGatt != null) {
                mGatt.disconnect();
                mGatt.close();
                mGatt = null;
                return true;
            }
            return false;
        }
    }

    public boolean reConnect(Context context) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            if (mIsConnecting) {
                LogUtils.e("Connection in progress!");
                return false;
            }
            String btuName = BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS);
            if (mBluetoothDeviceConnect != null) {
                btuName = mBluetoothDeviceConnect.getName();
            }
            mIsConnecting = true;
            BluetoothManager.getInstance().setScanListener(BleConnectController.this);
            BluetoothManager.getInstance().startScan(context, btuName);
            LogUtils.startEndMethodLog(false);
            return true;
        }
    }

    public boolean reConnect(Context context, String btuName) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            if (mIsConnecting) {
                LogUtils.e("Connection in progress!");
                return false;
            }
//            String btuName = BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS);
            if (mBluetoothDeviceConnect != null) {
                btuName = mBluetoothDeviceConnect.getName();
            }
            mIsConnecting = true;
            BluetoothManager.getInstance().setScanListener(BleConnectController.this);
            BluetoothManager.getInstance().startScan(context, btuName);
            LogUtils.startEndMethodLog(false);
            return true;
        }
    }

    public void stopReconnect() {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            mIsConnecting = false;
            BluetoothManager.getInstance().stopScan();
            LogUtils.startEndMethodLog(false);
        }
    }

    private boolean autoReConnect(String btuName) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            if (mIsConnecting) {
                LogUtils.e("Connection in progress!");
                return false;
            }
            if (!BluetoothUtils.checkBluetooth()) {
                LogUtils.e("Bluetooth is OFF");
                return false;
            }
            if (TextUtils.isEmpty(btuName)) {
                LogUtils.e("btuName is empty!");
                return false;
            }
            BluetoothManager.EnumDeviceConnectStatus status = BluetoothManager.getInstance().getDeviceCurrentStatus();
            if (status == BluetoothManager.EnumDeviceConnectStatus.INITIAL
                    || status == BluetoothManager.EnumDeviceConnectStatus.BTU_ACTIVE || status == BluetoothManager.EnumDeviceConnectStatus.BTU_INACTIVE_SECOND_TIME) {
                mIsConnecting = true;
                BluetoothManager.getInstance().setScanListener(BleConnectController.this);
                BluetoothManager.getInstance().startScan(null, btuName);
            } else {
                LogUtils.e("Device status is " + status + " ==> not autoReconnect!");
            }
            LogUtils.startEndMethodLog(false);
            return true;
        }
    }

    @Override
    public void onServiceCharacteristicDiscovered(List<BluetoothGattService> services) {
        synchronized (LOCK_OBJECT) {
            if ((services == null) || (services.size() == 0)) {
                // need to disconnect, close and recover
                notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
                LogUtils.e("onServiceCharacteristicDiscovered error with invalid parameter");
                return;
            }

            createServiceCharacteristicList(services);
            if (!isAtsSupport()) {
                LogUtils.e("onServiceCharacteristicDiscovered does not support Port UUID");
                // need to disconnect, close and recover
                notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
                return;
            }
            if (isBpsServiceSupport()) {
                LogUtils.d("onServiceCharacteristicDiscovered dues not conn Param but support ATT MTU");
                BluetoothGattCharacteristic attMtuCharas = mCharacteristics.get(UuidConstants.UuidBPS.ATT_MTU);

                boolean ret = mGattCallback.enableCharacteristicNotification(attMtuCharas);
                LogUtils.d("enableCharacteristicNotification mGatt.writeDescriptor return " + ret);
                if (false == ret) {
                    // need to disconnect, close and recover
                    notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
                    return;
                }

            } else {
                LogUtils.d("onServiceCharacteristicDiscovered does not support BPS service but support port service");
                BluetoothGattCharacteristic readCharas = mCharacteristics.get(UuidConstants.UuidATS.ASYNCH_INPUT_DATA);
                boolean ret = mGattCallback.enableCharacteristicNotification(readCharas);
                LogUtils.d("enableCharacteristicNotification mGatt.writeDescriptor return " + ret);
                if (false == ret) {
                    // need to disconnect, close and recover
                    notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
                    return;
                }

            }
        }
    }

    @Override
    public void onServiceDiscoveryFailed() {
        synchronized (LOCK_OBJECT) {
            notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
        }
    }

    @Override
    public void onCharacteristicNotifyEnabled(BluetoothGattCharacteristic characteristic) {
        synchronized (LOCK_OBJECT) {
            if (characteristic == null) {
                notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
                LogUtils.e("onCharacteristicNotifyEnabled with parameter error");
                return;
            }
            String uuid = characteristic.getUuid().toString();
            if (UuidConstants.UuidBPS.ATT_MTU.equals(uuid) ||
                    UuidConstants.UuidBPS.CONN_PARAM.equals(uuid)) {
                LogUtils.d("onCharacteristicNotifyEnabled enabled BPS UUID:" + uuid);
                boolean ret = mGattCallback.write(characteristic, DUMMY_DATA);
                if (!ret) {
                    notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
                    LogUtils.e("onCharacteristicNotifyEnabled write fail");
                    return;
                }


            } else if (UuidConstants.UuidATS.ASYNCH_INPUT_DATA.equals(uuid)) {
                // connect BTU completed
                LogUtils.d("onCharacteristicNotifyEnabled enabled port UUID " + uuid);
                checkApplicationAlreadyRegisteredAsync();
                notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.CONNECTED);

            }
        }
    }

    private void checkApplicationAlreadyRegisteredAsync() {
        Log.e("checkApplication", "vaoday");
        Thread t = new Thread(() -> {
            synchronized (LOCK_OBJECT) {
                String deviceName = BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS);
                deviceName = PrefUtils.getString("NAMEBTU");
                Log.e("checkApplication", deviceName);
                boolean isAlreadyRegistered = NativeLibController.getInstance().isExistVehicleInfo(deviceName);
                if (isAlreadyRegistered) {
                    Log.e("checkApplication", "isAlreadyRegistered");
                    BluetoothManager.getInstance().getBleRegisterController().inquire(true);
                } else {
                    Log.e("checkApplication", "else");
                    BluetoothManager.getInstance().getBleRegisterController().stopTimer();
                    BluetoothManager.getInstance().getBleRegisterController().startRegister();
                }
            }
        });
        t.start();
    }

    @Override
    public void onCharacteristicNotifyEnableFailed(BluetoothGattCharacteristic characteristic, int error) {
        synchronized (LOCK_OBJECT) {
            // We will not notify the listener if we are going to retry
            // This is to prevent write fail event to trigger connection stop
            if (mIsConnecting) {
                return;
            }
            notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
        }
    }

    @Override
    public void onBpsDataReceived(BluetoothGattCharacteristic characteristic, byte[] data) {
        synchronized (LOCK_OBJECT) {
            if (characteristic == null) {
                notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
                LogUtils.e("onBpsDataReceived error parameter invalid");
                return;
            }
            String uuid = characteristic.getUuid().toString();

            if (UuidConstants.UuidBPS.CONN_PARAM.equals(uuid) == true) {

                LogUtils.d("onBpsDataReceived Conn uuid and enable notification of AttMtu");
                BluetoothGattCharacteristic attCharacteristic = mCharacteristics.get(UuidConstants.UuidBPS.ATT_MTU);
                boolean ret = mGattCallback.enableCharacteristicNotification(attCharacteristic);
                LogUtils.d("enableCharacteristicNotification mGatt.writeDescriptor return " + ret);
            } else if (UuidConstants.UuidBPS.ATT_MTU.equals(uuid) == true) {
                if (data.length == ATT_MTU_SUPPORTED_DATASIZE) {
                    mAttSize = (0x00FF & data[1]);
                    mAttSize *= 256;
                    mAttSize += (0x00FF & data[0]);
                    if (mAttSize < MIN_ATT_SIZE) {
                        LogUtils.e("onBpsDataReceived invalid ATTSIZE");
                        notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
                        return;
                    }
                    LogUtils.d(uuid + "| mAttSize = " + mAttSize);
                }
                BluetoothManager.getInstance().getDataMessageManager().setupDataLength(mAttSize);
                LogUtils.d("onBpsDataReceived ATT MTU data:" + mAttSize);
                BluetoothGattCharacteristic readCharacteristic = mCharacteristics.get(UuidConstants.UuidATS.ASYNCH_INPUT_DATA);
                boolean ret = mGattCallback.enableCharacteristicNotification(readCharacteristic);
                LogUtils.d("enableCharacteristicNotification mGatt.writeDescriptor return " + ret);
            }
        }
    }

    @Override
    public void onConnected(BluetoothGatt gatt) {
        synchronized (LOCK_OBJECT) {
            LogUtils.d("Connected to device " + gatt.getDevice().getAddress());
            if (!gatt.discoverServices()) {
                LogUtils.e("gatt.discoverServices fail");
                notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
                autoReConnect(mBluetoothDeviceConnect.getName());
                return;
            }
        }
    }

    @Override
    public void onDisconnected(BluetoothGatt gatt, int statusError) {
        synchronized (LOCK_OBJECT) {
            LogUtils.d("Disconnected from device, current status device = " + BluetoothManager.getInstance().getDeviceCurrentStatus());
            boolean isNeedToReconnect;
            mIsConnecting = false;
            BluetoothManager.getInstance().setTransactionIdentity(0);
            if (mGatt != null) {
                mGatt.disconnect();
                mGatt.close();
                mGatt = null;
            }
            LogUtils.e("Connect Failed with error code = " + statusError);
            boolean bluetoothEnable = BluetoothUtils.checkBluetooth();
            // We will not retry if BTU initiated the disconnection
            int DISCONNECT_FROM_BTU = 0x13;
            int DISCONNECT_TIME_OUT = 0x08;
            if (!bluetoothEnable || statusError == DISCONNECT_FROM_BTU || statusError == DISCONNECT_TIME_OUT) {
                isNeedToReconnect = false;
                LogUtils.w("BTU initiated the disconnection or timeout connect, check reconnect to authentication!");
            } else {
                isNeedToReconnect = true;
            }
            final boolean isReAuthentication = isNeedToReconnect;
            mTimeManager.startTimer((IF_TimeScheduleListener) () -> {
                if (isReAuthentication) {
                    if (mBluetoothDeviceConnect != null) {
                        String deviceName = mBluetoothDeviceConnect.getName();
                        autoReConnect(deviceName);
                    }
                } else {
                    String deviceName = BluetoothPrefUtils.getInstance().getString(KEY_BTU_NAME_REGISTER_SUCCESS);
                    if (!TextUtils.isEmpty(deviceName)) {
                        LogUtils.d("Reconnect to authentication with BTU : " + deviceName);
                        autoReConnect(deviceName);
                    } else {
                        BluetoothManager.getInstance().getBleRegisterController().resetData();
                    }
                    notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
                }
            }, TIME_RECONNECT);
        }
    }

    public void stopTimer() {
        synchronized (LOCK_OBJECT) {
            LogUtils.d("Stop timer!");
            if (mTimeManager != null) {
                mTimeManager.stopAllTimer();
            }
        }
    }


    /**
     * Notify bluetooth connection status change for listeners.
     *
     * @param state ConnectState.
     */
    public void notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus state) {
        LogUtils.d("notify connect status : " + state);
        mIsConnecting = false;
        if (state == BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED) {
            stopTimer();
            disconnectDevice();
        }
        if (BluetoothUtils.checkBluetooth()) {
            for (IF_VehicleConnectListener listener : mListeners) {
                if (listener != null) {
                    listener.onConnectStateNotice(state);
                }
            }
        }
    }

    @Override
    public void onTimeout() {
        synchronized (LOCK_OBJECT) {
            if (isConnecting()) {
                BluetoothManager.getInstance().stopScan();
            }
            mIsConnecting = false;
            notifyConnectStatusChange(BluetoothManager.EnumDeviceConnectStatus.DISCONNECTED);
        }
    }


    private void createServiceCharacteristicList(List<BluetoothGattService> services) {
        removeServiceCharacteristicList();
        mServices = new HashMap<>();
        mCharacteristics = new HashMap<>();

        for (BluetoothGattService service : services) {
            UUID uuidService = service.getUuid();
            LogUtils.d("onServiceCharacteristicDiscovered service:" + uuidService.toString());
            mServices.put(uuidService.toString(), service);
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            if ((characteristics != null) && (characteristics.size() > 0)) {
                for (BluetoothGattCharacteristic characteristic : characteristics) {
                    UUID uuidChars = characteristic.getUuid();
                    LogUtils.d("onServiceCharacteristicDiscovered serviceCharacteristic:"
                            + uuidChars.toString());
                    mCharacteristics.put(uuidChars.toString(), characteristic);
                }
            }
        }
    }

    private void removeServiceCharacteristicList() {
        if (mServices != null) {
            mServices.clear();
            mServices = null;
        }
        if (mCharacteristics != null) {
            mCharacteristics.clear();
            mCharacteristics = null;
        }
    }

    private boolean isAtsSupport() {
        if ((mServices == null) || (mServices.size() == 0)) {
            LogUtils.e("isAtsSupport service error");
            return false;
        }
        if ((mCharacteristics == null) || (mCharacteristics.size() == 0)) {
            LogUtils.e("isAtsSupport characteristic error");
            return false;
        }
        if (mServices.get(UuidConstants.UuidService.ASYNCHRONOUS_TRANSFER_SERVICE) == null) {
            LogUtils.e("isAtsSupport (ATS) unmatch service");
            return false;
        }
        if (mCharacteristics.get(UuidConstants.UuidATS.ASYNCH_INPUT_DATA) == null) {
            LogUtils.e("isAtsSupport unmatch readData UUID");
            return false;
        }
        if (mCharacteristics.get(UuidConstants.UuidATS.ASYNCH_OUTPUT_DATA) == null) {
            LogUtils.e("isAtsSupport unmatch write UUID");
            return false;
        }
        return true;
    }

    private boolean isBpsServiceSupport() {
        if ((mServices == null) || (mServices.size() == 0)) {
            LogUtils.e("Services null or empty error!");
            return false;
        }
        if ((mCharacteristics == null) || (mCharacteristics.size() == 0)) {
            LogUtils.e("isBpsServiceSupport characteristics error");
            return false;
        }
        if (mServices.get(UuidConstants.UuidService.BLE_PARAMETER_SERVICE) == null) {
            LogUtils.e("isBpsServiceSupport unmatch service");
            return false;
        }
        if (mCharacteristics.get(UuidConstants.UuidBPS.ATT_MTU) == null) {
            LogUtils.e("isBpsServiceSupport unmatch att mtu uuid");
            return false;
        }
        return true;
    }

    public BluetoothGattCharacteristic getCharacteristic(String uuid) {
        if (mCharacteristics != null) {
            return mCharacteristics.get(uuid);
        }
        return null;
    }

    public BluetoothDevice getBluetoothDeviceConnect() {
        return mBluetoothDeviceConnect;
    }

    @Override
    public void onScanDeviceResult(BluetoothScan device) {
        synchronized (LOCK_OBJECT) {
            LogUtils.d("Found device to reconnect!");
            // Found device to reconnect
            if (device != null && device.getDevice() != null && !TextUtils.isEmpty(device.getDevice().getName())) {
                boolean isRegistered = BluetoothPrefUtils.getInstance().getBool(KEY_REGISTER_SUCCESS);
                if (!(device.getUuidMode() == BleScanController.UuidMode.MODE_REGISTER && isRegistered)) {
                    BluetoothManager.getInstance().stopScan();
                    connectDevice(device.getDevice());
                    return;
                }
                LogUtils.e("Don't autoReconnect because is MODE_REGISTER");
            }
        }
    }

    @Override
    public void onScanDeviceResult(List<BluetoothScan> devices) {

    }

    @Override
    public void onScanFail(int errorCode) {
        LogUtils.e("scan reconnect fail with error: " + errorCode);
//        BluetoothManager.getInstance().stopScan();
//        mIsConnecting = false;
//        if (mContext != null) {
//            reConnect(mContext);
//        }
    }

    public boolean isConnecting() {
        return mIsConnecting;
    }

}