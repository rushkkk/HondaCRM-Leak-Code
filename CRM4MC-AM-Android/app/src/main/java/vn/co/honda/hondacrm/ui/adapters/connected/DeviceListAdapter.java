package vn.co.honda.hondacrm.ui.adapters.connected;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.btu.Model.BluetoothScan;

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
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_connect, parent, false);
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
        holder.mTextView.setText(bluetoothScan.getDevice().getName() + "-" + bluetoothScan.getUuidMode());
        if (position == mDeviceList.size() - 1) {
            holder.mSpaceView.setVisibility(View.GONE);
        } else {
            holder.mSpaceView.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(bluetoothScan);
                new CountDownTimer(2000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        holder.imBluetooth.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.red));
                        holder.mTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.red));
                    }

                    @Override
                    public void onFinish() {
                        holder.mTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
                        holder.imBluetooth.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.black));
                    }
                }.start();

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
        View mSpaceView;
        ImageView imBluetooth;

        DeviceViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.tv_name_vehicle);
            mSpaceView = v.findViewById(R.id.space);
            imBluetooth = v.findViewById(R.id.imBluetooth);
        }
    }
}