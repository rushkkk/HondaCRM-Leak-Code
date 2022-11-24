package vn.co.honda.hondacrm.btu.Bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import vn.co.honda.hondacrm.btu.Constants.UuidConstants;
import vn.co.honda.hondacrm.btu.Utils.LogUtils;

//Handle callback when perform read/ write / notify characteristic on gatt service.
//Same as BleDataCtrl
public class GattCallback extends BluetoothGattCallback {

    private ConnectListener mConnectListener;
    private DataListener mDataListener;
    private BluetoothGatt mGatt;
    private ArrayList<WriteDataQueue> mPendingDataQueues;

    public GattCallback() {
        mPendingDataQueues = new ArrayList<>();
    }

    public void setConnectListener(ConnectListener connectListener) {
        this.mConnectListener = connectListener;
    }

    public void setDataListener(DataListener dataListener) {
        this.mDataListener = dataListener;
    }


    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        LogUtils.startEndMethodLog(true);
        if (gatt == null) {
            LogUtils.e("onServicesDiscovered gatt param == null");
        }
        if (mConnectListener == null) {
            LogUtils.e("onServicesDiscovered listener = null");
            return;
        }
        if (status != BluetoothGatt.GATT_SUCCESS) {
            LogUtils.e("onServicesDiscovered status error");
            mConnectListener.onServiceDiscoveryFailed();
            return;
        }
        mConnectListener.onServiceCharacteristicDiscovered(mGatt.getServices());
        LogUtils.startEndMethodLog(false);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        LogUtils.d("newState: " + newState + " status: " + status);
        BluetoothManager.getInstance().stopScan();
        // reset encrypt when bluetooth status change
        BluetoothManager.getInstance().getDataMessageManager().setEnableEncrypt(false);
        if (status != BluetoothGatt.GATT_SUCCESS) {
            LogUtils.e("GATT INTERNAL error status = :" + status);
            mConnectListener.onDisconnected(gatt, status);
            return;
        }
        if (gatt != null) {
            if ((mGatt != null) && (mGatt != gatt)) {
                LogUtils.d("Another GATT was created. Try to close the previous one.");
                mGatt.disconnect();
                mGatt.close();
                mGatt = null;
            }
            mGatt = gatt;
        }
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            LogUtils.d("Connected to device " + gatt.getDevice().getAddress());
            mConnectListener.onConnected(gatt);
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            LogUtils.d("Disconnected from device");
            mConnectListener.onDisconnected(gatt, status);
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        LogUtils.startEndMethodLog(true);
        String uuid = characteristic.getUuid().toString();
        LogUtils.d("characteristic UUID: " + uuid);
        byte[] data = characteristic.getValue();

        switch (uuid) {
            case UuidConstants.UuidATS.ASYNCH_INPUT_DATA:
                mDataListener.onDataReceived(data);
                break;
            case UuidConstants.UuidATS.ASYNCH_OUTPUT_RESP:
                break;
            case UuidConstants.UuidBPS.ATT_MTU:
            case UuidConstants.UuidBPS.CONN_PARAM:
                mConnectListener.onBpsDataReceived(characteristic, data);
                break;
            default:
                break;
        }
        LogUtils.startEndMethodLog(false);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (gatt == null) {
            LogUtils.e("onCharacteristicWrite gatt param == null");
        }
        if (characteristic == null) {
            LogUtils.e("onCharacteristicWrite characteristic param == null");
        }
        LogUtils.d("onCharacteristicWrite, status: " + status + " (GATT_SUCCESS == 0)");
        if (status != BluetoothGatt.GATT_SUCCESS) {
            LogUtils.e("onCharacteristicWrite gatt error:" + status);
            if (mDataListener != null) {
                mDataListener.onWriteFailed(status);
            }
        }
        if (mPendingDataQueues == null) {
            LogUtils.e("onCharacteristicWrite already closed");
            return;
        }
        synchronized (mPendingDataQueues) {
            if (mPendingDataQueues.get(0) != null) {
                mPendingDataQueues.remove(0);
            }
            if (mPendingDataQueues.isEmpty() == false) {
                WriteDataQueue nextData = mPendingDataQueues.get(0);
                if (nextData != null) {
                    if (nextData.writeData() == false) {
                        LogUtils.e("onCharacteristicWrite failed to write next data");
                        if (mDataListener != null) {
                            mDataListener.onWriteFailed(0);
                        }
                    } else {
                        LogUtils.e("onCharacteristicWrite success to write next data");
                    }
                } else {
                    LogUtils.e("onCharacteristicWrite pending data queue broken");
                }
            } else {
                LogUtils.e("onCharacteristicWrite no pending data");
            }
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if (gatt == null) {
            LogUtils.e("onDescriptorWrite gatt param == null");
        }
        if (mConnectListener == null) {
            LogUtils.e("onDescriptorWrite listener = null");
            return;
        }
        if ((status != BluetoothGatt.GATT_SUCCESS) || (descriptor == null)) {
            LogUtils.e("onDescriptorWrite status error");
            mConnectListener.onCharacteristicNotifyEnableFailed(null, status);
            return;
        }
        mConnectListener.onCharacteristicNotifyEnabled(descriptor.getCharacteristic());
    }

    public boolean enableCharacteristicNotification(BluetoothGattCharacteristic charas) {
        if ((mGatt == null) || (charas == null)) {
            LogUtils.e("enableCharacteristicNotification error mGatt is null");
            return false;
        }
        UUID descriptorConfUUID = UUID.fromString(UuidConstants.UuidATS.DESCRIPTOR);
        BluetoothGattDescriptor descriptor = charas.getDescriptor(descriptorConfUUID);
        if (descriptor == null) {
            LogUtils.e("enableCharacteristicNotification error descriptor invalid");
            return false;
        }
        int properties = charas.getProperties();
        boolean enableNotify;
        if ((BluetoothGattCharacteristic.PROPERTY_NOTIFY & properties) != 0) {
            enableNotify = true;
        } else if ((BluetoothGattCharacteristic.PROPERTY_INDICATE & properties) != 0) {
            enableNotify = false;
        } else {
            LogUtils.e("enableCharacteristicNotification error descriptor property invalid");
            return false;
        }
        if (mGatt.setCharacteristicNotification(charas, true) == false) {
            LogUtils.e("enableCharacteristicNotification failed to setCharacteristicNoficitaion");
            return false;
        }
        byte[] data = (enableNotify == true) ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.ENABLE_INDICATION_VALUE;
        if (descriptor.setValue(data) == false) {
            LogUtils.e("descriptor setValue error");
            return false;
        }
        return mGatt.writeDescriptor(descriptor);
    }

    public boolean write(BluetoothGattCharacteristic characteristic, byte[] data) {
        if ((mGatt == null) || (characteristic == null) || (mPendingDataQueues == null)) {
            LogUtils.e("write parameter error");
            return false;
        }
        synchronized (mPendingDataQueues) {
            boolean isNeedToSend = mPendingDataQueues.isEmpty();
            WriteDataQueue queue = new WriteDataQueue(characteristic, data);
            mPendingDataQueues.add(queue);
            if (isNeedToSend == true) {
                LogUtils.d("write now");
                if (queue.writeData()) {
                    LogUtils.d("success to write data");
                    return true;
                } else {
                    LogUtils.e("failed to write data");
                    return false;
                }
            }
            LogUtils.d("write pending");
            return true;
        }
    }

    //Same as IF_BleDataCtrlStandbyListener
    public interface DataListener {
        void onDataReceived(byte[] data);

        void onWriteFailed(int errCode);
    }

    public interface ConnectListener {
        void onServiceCharacteristicDiscovered(List<BluetoothGattService> services);

        void onServiceDiscoveryFailed();

        void onCharacteristicNotifyEnabled(BluetoothGattCharacteristic characteristic);

        void onCharacteristicNotifyEnableFailed(BluetoothGattCharacteristic characteristic, int error);

        void onBpsDataReceived(BluetoothGattCharacteristic characteristic, byte[] data);

        void onConnected(BluetoothGatt gatt);

        void onDisconnected(BluetoothGatt gatt, int error);
    }

    private class WriteDataQueue {
        private BluetoothGattCharacteristic writeCharacteristic;
        private byte[] writenData;

        public WriteDataQueue(BluetoothGattCharacteristic characteristic, byte[] data) {
            writeCharacteristic = characteristic;
            writenData = data;
        }

        boolean writeData() {
            if ((mGatt == null) || (writeCharacteristic == null)) {
                LogUtils.e("writeData mGatt == null or writeCharacteristic == null");
                return false;
            }
            writeCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            if (writeCharacteristic.setValue(writenData) == false) {
                LogUtils.e("writeData failed to setValue");
                return false;
            }
            return mGatt.writeCharacteristic(writeCharacteristic);
        }
    }
}
