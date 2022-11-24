package vn.co.honda.hondacrm.btu.Bluetooth.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import vn.co.honda.hondacrm.btu.Bluetooth.BluetoothManager;
import vn.co.honda.hondacrm.btu.Bluetooth.timer.IF_TimeScheduleListener;
import vn.co.honda.hondacrm.btu.Bluetooth.timer.TimeManager;
import vn.co.honda.hondacrm.btu.Bluetooth.utils.BluetoothUtils;
import vn.co.honda.hondacrm.btu.Constants.UuidConstants;
import vn.co.honda.hondacrm.btu.Interfaces.listener.scan.IF_VehicleScanListener;
import vn.co.honda.hondacrm.btu.Interfaces.method.scan.IF_Scan_Methods;
import vn.co.honda.hondacrm.btu.Manager.LocationManager;
import vn.co.honda.hondacrm.btu.Model.BluetoothScan;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;

import static vn.co.honda.hondacrm.btu.Constants.UuidConstants.UuidService.ASYNCHRONOUS_TRANSFER_SERVICE;
import static vn.co.honda.hondacrm.btu.Constants.UuidConstants.UuidService.BLE_PARAMETER_SERVICE;
import static vn.co.honda.hondacrm.btu.Constants.UuidConstants.UuidService.PSEUDO_SPP_SERVICE;
import static vn.co.honda.hondacrm.btu.Constants.UuidConstants.UuidService.TRANSMITTER_CONTROL_SERVICE;


public class BleScanController extends ScanCallback implements IF_Scan_Methods {
    private Context mContext;
    private IF_VehicleScanListener mVehicleScanListener;
    private boolean mIsScanning = false;
    private TimeManager mTimeManager;
    private List<BluetoothScan> mListDevices;
    private List<BluetoothScan> mListDevicesTemp;
    private final static int TIME_SCAN = 1000;
    private int mCount;
    private final int MAX_COUNT_TIME = 4;
    private boolean isLocationEnabled;
    private final String START_BTU_NAME = "BTU";
    private final Object LOCK_OBJECT = new Object();

    public enum UuidMode {
        MODE_NORMAL,
        MODE_SLEEP,
        MODE_OTHER,
        MODE_REGISTER
    }

    public BleScanController() {
        mTimeManager = new TimeManager();
        mListDevices = new ArrayList<>();
        mListDevicesTemp = new ArrayList<>();
    }

    private final BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (!isLocationEnabled) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device == null || TextUtils.isEmpty(device.getName()) || !device.getName().startsWith(START_BTU_NAME)) {
                        return;
                    }
                    LogUtils.d("Found device: " + device.getName());
                    if (mVehicleScanListener != null) {
                        BluetoothScan bluetoothScan = new BluetoothScan(device, UuidMode.MODE_OTHER);
                        mVehicleScanListener.onScanDeviceResult(bluetoothScan);
                        detectDeviceAddToList(bluetoothScan);
                    }
                } else {
                    if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                        LogUtils.d("ACTION_DISCOVERY_FINISHED ==> startDiscovery() again");
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter != null) {
                            BluetoothAdapter.getDefaultAdapter().startDiscovery();
                        }
                    }

                }
            }
        }
    };

    public void setVehicleScanListener(IF_VehicleScanListener vehicleScanListener) {
        mVehicleScanListener = vehicleScanListener;
    }

    public boolean startScan(Context context) {
        synchronized (LOCK_OBJECT) {
            if (mIsScanning) {
                LogUtils.e("Start scan fail, have task Scan is running!");
                return false;
            }
            if (mCount > 0) {
                mIsScanning = true;
            }
            mCount++;
            LogUtils.d("mCount = " + mCount);
            return startScan(context, null);
        }
    }

    public boolean startScan(Context context, String deviceName) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            if (!BluetoothUtils.checkBluetooth()) {
                LogUtils.e("mBluetoothAdapter is null!");
                return false;
            }
            if (BluetoothAdapter.getDefaultAdapter().isDiscovering()) {
                LogUtils.d("mBluetoothAdapter.isDiscovering() is true, cancel discovery to make sure scan can success");
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            }
            if (BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner() == null) {
                LogUtils.e("mBluetoothLeScanner is null!");
                return false;
            }
            if (context != null) {
                this.mContext = context;
            }
            List<ScanFilter> filters = new ArrayList<>();
            boolean checkLocation = LocationManager.isLocationEnabled(mContext);
            if (checkLocation || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (isLocationEnabled != checkLocation || !mIsScanning) {
                    mListDevices.clear();
                    if (isLocationEnabled != checkLocation) {
                        isLocationEnabled = checkLocation;
                        stopScan();
                    }
                    mIsScanning = true;
                    if (!TextUtils.isEmpty(deviceName)) {
                        ScanFilter.Builder scanBuilder = new ScanFilter.Builder().setDeviceName(deviceName);
                        if (scanBuilder != null) {
                            ScanFilter scanFilter = scanBuilder.build();
                            filters.add(scanFilter);
                        }
                    }
                    ScanSettings.Builder setBuilder = new ScanSettings.Builder();
                    setBuilder.setScanMode(ScanSettings.SCAN_MODE_BALANCED);
                    ScanSettings setting = setBuilder.build();
                    BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().startScan(filters, setting, this);
                    LogUtils.d("Start new scan progress.");
                }
            } else {
                if (isLocationEnabled != checkLocation || !mIsScanning) {
                    mListDevices.clear();
                    if (isLocationEnabled != checkLocation) {
                        isLocationEnabled = checkLocation;
                        stopScan();
                    }
                    mIsScanning = true;
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    mContext.registerReceiver(mBluetoothReceiver, filter);
                    BluetoothAdapter.getDefaultAdapter().startDiscovery();
                    LogUtils.d("Start new scan progress.");
                }
            }

            if (mTimeManager != null) {
                mTimeManager.startTimer((IF_TimeScheduleListener) () -> {
                    if (mListDevices.size() == 0 || mListDevicesTemp.size() > mListDevices.size()) {
                        // wait 5s for update list device if have BTU not found
                        if (mCount >= MAX_COUNT_TIME) {
                            mListDevicesTemp.clear();
                            mListDevicesTemp.addAll(mListDevices);
                            mVehicleScanListener.onScanDeviceResult(mListDevicesTemp);
                        }
                    } else {
                        // update list when found BTU
                        mListDevicesTemp.clear();
                        mListDevicesTemp.addAll(mListDevices);
                        mVehicleScanListener.onScanDeviceResult(mListDevicesTemp);
                    }
                    if (mCount >= MAX_COUNT_TIME) {
                        if (isLocationEnabled) {
                            // Stop scan to start scan again
                            stopScan();
                        }
                        mListDevices.clear();
                        mCount = 0;
                    }
                    mIsScanning = false;
                    startScan(mContext);
                }, TIME_SCAN);
            }

            LogUtils.startEndMethodLog(false);
        }
        return true;
    }

    public boolean stopScan() {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            if (mTimeManager != null) {
                mTimeManager.stopTimeSchedule();
            }
            if (mIsScanning) {
                mIsScanning = false;
                if (BluetoothManager.getInstance().getBleConnectController().isConnecting()) {
                    BluetoothManager.getInstance().getBleConnectController().disconnectDevice();
                }
                if (BluetoothAdapter.getDefaultAdapter() != null && BluetoothAdapter.getDefaultAdapter().isDiscovering()) {
                    try {
                        mContext.unregisterReceiver(mBluetoothReceiver);
                    } catch (IllegalArgumentException e) {
                        LogUtils.e(e.toString());
                    }
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                }

                try {
                    if (BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner() != null) {
                        BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().stopScan(this);
                    }
                } catch (IllegalStateException e) {
                    LogUtils.e(Log.getStackTraceString(e));
                }
            } else {
                //Scan stop already
                return false;
            }
            LogUtils.startEndMethodLog(false);
        }
        return true;
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        synchronized (LOCK_OBJECT) {
            if (result == null) {
                LogUtils.e("ScanResult null");
                return;
            }
            BluetoothDevice device = result.getDevice();
            ScanRecord scanRecord = result.getScanRecord();
            processScanResult(device, scanRecord);
        }
    }

    private void processScanResult(BluetoothDevice device, ScanRecord scanRecord) {
        if (!mIsScanning) {
            LogUtils.e("scan stopped already");
            return;
        }
        if (mVehicleScanListener == null) {
            LogUtils.e("mVehicleScanListener null");
            return;
        }
        if (device == null || TextUtils.isEmpty(device.getName())) {
            return;
        }
        LogUtils.d("Found device: " + device.getName());
        UuidMode uuidMode = UuidMode.MODE_OTHER;
        if (Objects.requireNonNull(scanRecord).getServiceUuids() != null) {
            UUID uuid = Objects.requireNonNull(scanRecord).getServiceUuids().get(0).getUuid();
            uuidMode = parseUuid(uuid);
        }

        if (mVehicleScanListener != null && uuidMode != null && !uuidMode.equals(UuidMode.MODE_OTHER)) {
            BluetoothScan bluetoothScan = new BluetoothScan(device, uuidMode);
            mVehicleScanListener.onScanDeviceResult(bluetoothScan);
            detectDeviceAddToList(bluetoothScan);
        }
    }

    private int getStateDeviceAddToList(BluetoothScan bluetoothScan) {
        for (int pos = 0; pos < mListDevices.size(); pos++) {
            BluetoothScan device = mListDevices.get(pos);
            if (bluetoothScan.getDevice().getName().equals(device.getDevice().getName())) {
                if (bluetoothScan.getDevice().getAddress().equals(device.getDevice().getAddress())) {
                    if(bluetoothScan.getUuidMode() == device.getUuidMode()) {
                        return -2;
                    } else {
                        return pos;
                    }
                } else {
                    return pos;
                }

            }
        }
        return -1;
    }

    private void detectDeviceAddToList(BluetoothScan bluetoothScan) {
        int pos = getStateDeviceAddToList(bluetoothScan);
        if (pos == -1) {
            mListDevices.add(bluetoothScan);
        } else {
            if (pos != -2 && mListDevices.size() > pos) {
                mListDevices.remove(pos);
                mListDevices.add(pos,bluetoothScan);
            }
        }
    }

    private UuidMode parseUuid(UUID uuid) {
        if (uuid == null) {
            return UuidMode.MODE_OTHER;
        }
        if (uuid.toString().equals(UuidConstants.UuidService.LOW_POWER_SLEEP_SERVICE)) {
            LogUtils.d("UUID... is sleep mod");
            return UuidMode.MODE_SLEEP;
        } else if (uuid.toString().equals(BLE_PARAMETER_SERVICE)
                || uuid.toString().equals(PSEUDO_SPP_SERVICE)
                || uuid.toString().equals(TRANSMITTER_CONTROL_SERVICE)
                || uuid.toString().equals(ASYNCHRONOUS_TRANSFER_SERVICE)) {
            LogUtils.d("UUID... is normal mod");
            return UuidMode.MODE_NORMAL;
        } else if (uuid.toString().equals(UuidConstants.UuidService.REGISTER_SERVICE)) {
            LogUtils.d("UUID... is register mod");
            return UuidMode.MODE_REGISTER;

        } else {
            LogUtils.d("UUID... is other mod");
            return UuidMode.MODE_OTHER;
        }
    }

    @Override
    public void onScanFailed(int errorCode) {
        synchronized (LOCK_OBJECT) {
            LogUtils.startEndMethodLog(true);
            LogUtils.d(errorCode + "");
//            stopScan();
//            startScan(mContext);
            if (mVehicleScanListener != null) {
                mVehicleScanListener.onScanFail(errorCode);
            }
            LogUtils.startEndMethodLog(false);
        }
    }

    public synchronized void resetData() {
        mIsScanning = false;
        mCount = 0;
        mListDevices.clear();
        mListDevicesTemp.clear();
        isLocationEnabled = false;
    }

}
