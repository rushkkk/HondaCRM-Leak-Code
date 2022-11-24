package com.honda.connmc.View.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honda.connmc.Bluetooth.scan.BleScanController;
import com.honda.connmc.Model.BluetoothScan;
import com.honda.connmc.R;

import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder> {

    private final List<BluetoothScan> mDeviceList;

    private OnItemClickListener mListener;

    public DeviceListAdapter(List<BluetoothScan> devices) {
        mDeviceList = devices;
    }

    public void setOnItemClick(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public DeviceListAdapter.DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (parent == null || parent.getContext() == null) {
            return null;
        }
        View v = null;
        if (LayoutInflater.from(parent.getContext()) != null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        }

        DeviceListAdapter.DeviceViewHolder deviceViewHolder = null;
        if (v != null) {
            deviceViewHolder = new DeviceViewHolder(v);
        }

        return deviceViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        if (mDeviceList == null || mDeviceList.size() == 0) {
            return;
        }
        BluetoothScan bluetoothScan = mDeviceList.get(position);
        holder.mTextView.setText(bluetoothScan.getDevice().getName());
        holder.mTvStatus.setText(bluetoothScan.getUuidMode().toString());
        if (bluetoothScan.getUuidMode() == BleScanController.UuidMode.MODE_OTHER) {
            holder.mTvStatus.setVisibility(View.INVISIBLE);
        } else {
            holder.mTvStatus.setVisibility(View.VISIBLE);
        }
        if (position == mDeviceList.size() - 1) {
            holder.mSpaceView.setVisibility(View.GONE);
        } else {
            holder.mSpaceView.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(bluetoothScan);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    public interface OnItemClickListener {

        void onItemClick(BluetoothScan device);
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        TextView mTvStatus;

        View mSpaceView;

        DeviceViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.tv_device);
            mTvStatus = v.findViewById(R.id.tv_status);
            mSpaceView = v.findViewById(R.id.space);
        }
    }
}
