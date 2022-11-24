package com.honda.connmc.View.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honda.connmc.Model.BluetoothStatus;
import com.honda.connmc.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.DeviceViewHolder> {
    private final List<BluetoothStatus> mDeviceList;
    private final LinkedHashMap<String, BluetoothStatus> mHashMap;
    private int mSize;
    private boolean mIsDisplayRaw;

    public void updateListStatus(LinkedHashMap<String, BluetoothStatus> statuses) {
        mHashMap.putAll(statuses);
        mDeviceList.clear();
        mDeviceList.addAll(mHashMap.values());
        notifyDataSetChanged();
    }

    public void setDisplayRaw(boolean displayRaw) {
        this.mIsDisplayRaw = displayRaw;
        notifyDataSetChanged();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mValue;

        TextView mRawName;
        TextView mRawValue;
        TextView mRawValueHex;
        LinearLayout layoutRawValue;

        //View mSpaceView;

        DeviceViewHolder(View v) {
            super(v);
            mName = v.findViewById(R.id.tvName);
            mValue = v.findViewById(R.id.tvStatus);
            //mSpaceView = v.findViewById(R.id.space);

            mRawName = v.findViewById(R.id.tvTag);
            mRawValue = v.findViewById(R.id.tvDec);
            mRawValueHex = v.findViewById(R.id.tvHex);
            layoutRawValue = v.findViewById(R.id.layoutRawData);
        }
    }

    public StatusAdapter(boolean isDisplayRaw) {
        mDeviceList = new ArrayList<>();
        mHashMap = new LinkedHashMap<>();
        mIsDisplayRaw = isDisplayRaw;
    }

    @Override
    public StatusAdapter.DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_status, parent, false);
        DeviceViewHolder vh = new DeviceViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        BluetoothStatus bluetoothStatus = mDeviceList.get(holder.getAdapterPosition());
        if (bluetoothStatus == null) return;
        if (bluetoothStatus.isHeader()) {
            holder.mName.setTypeface(null, Typeface.BOLD);
            holder.mName.setText(mDeviceList.get(position).getName());
            holder.mValue.setText("");
        } else {
            holder.mName.setTypeface(null, Typeface.NORMAL);
            holder.mName.setText(mDeviceList.get(position).getName());
            holder.mValue.setText(mDeviceList.get(position).getValue());


            holder.mRawName.setText(mDeviceList.get(position).getRawName());
            holder.mRawValue.setText(String.valueOf(mDeviceList.get(position).getRawValue()));
            holder.mRawValueHex.setText(String.format("%X", mDeviceList.get(position).getRawValue()));

            if (mIsDisplayRaw) {
                holder.mRawName.setVisibility(View.VISIBLE);
                holder.layoutRawValue.setVisibility(View.VISIBLE);
                holder.mName.setVisibility(View.GONE);
                holder.mValue.setVisibility(View.GONE);
            } else {
                holder.mRawName.setVisibility(View.GONE);
                holder.layoutRawValue.setVisibility(View.GONE);
                holder.mName.setVisibility(View.VISIBLE);
                holder.mValue.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public int getItemCount() {
        mSize = mDeviceList.size();
        return mSize;
    }
}
