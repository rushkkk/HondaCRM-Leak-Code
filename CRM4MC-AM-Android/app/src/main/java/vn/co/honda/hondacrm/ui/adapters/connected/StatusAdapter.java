package vn.co.honda.hondacrm.ui.adapters.connected;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.btu.Model.BluetoothStatus;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.DeviceViewHolder> {
    private final List<BluetoothStatus> mDeviceList;
    private final LinkedHashMap<String, BluetoothStatus> mHashMap;
    private int mSize;
    private boolean mIsDisplayRaw;

    public void updateListStatus(LinkedHashMap<String, BluetoothStatus> statuses) {
//        mHashMap.putAll(statuses);
//        mDeviceList.clear();
//        for (String key : mHashMap.keySet()) {
//            BluetoothStatus value = mHashMap.get(key);
//            if (value.getRawName() != null) {
//                if (value.getRawName().equals("C_ENGTEMP")
//                        || value.getRawName().equals("C_ODO")
//                        || value.getRawName().equals("ENG_BTU_VB_11")
//                        || value.getRawName().equals("ENG_BTU_ECT_11")
//                        || value.getRawName().equals("C_INTAKE_AIR_TEMP")
//                        || value.getRawName().equals("C_ASP")
//                        || value.getRawName().equals("ENG_BTU_THDEG_11")
//                        || value.getRawName().equals("ENG_BTU_VO2_20")
//                ) {
//                    mDeviceList.add(value);
//                }
//            }
//        }
//        notifyDataSetChanged();

        mHashMap.putAll(statuses);
        mDeviceList.clear();
        mDeviceList.addAll(mHashMap.values());
        notifyDataSetChanged();
    }

    public void setUpStatus(List<BluetoothStatus> list) {
//        mDeviceList.addAll(list);
//        setDataListKey();
//        for (BluetoothStatus bu : list) {
//            if (bu.getRawName().equals(listKey.get(0))) {
//                mDeviceList.add(0, bu);
//            }
//            if (bu.getRawName().equals(listKey.get(1))) {
//                mDeviceList.add(1, bu);
//            }
//            if (bu.getRawName().equals(listKey.get(2))) {
//                mDeviceList.add(2, bu);
//            }
//            if (bu.getRawName().equals(listKey.get(3))) {
//                mDeviceList.add(3, bu);
//            }
//            if (bu.getRawName().equals(listKey.get(4))) {
//                mDeviceList.add(4, bu);
//            }
//            if (bu.getRawName().equals(listKey.get(5))) {
//                mDeviceList.add(5, bu);
//            }
//            if (bu.getRawName().equals(listKey.get(6))) {
//                mDeviceList.add(6, bu);
//            }
//            if (bu.getRawName().equals(listKey.get(7))) {
//                mDeviceList.add(7, bu);
//            }
//        }

        notifyDataSetChanged();

    }

    private ArrayList<String> listKey = new ArrayList<>();

    private void setDataListKey() {
        listKey.add("C_ENGTEMP ");
        listKey.add("C_ODO");
        listKey.add("ENG_BTU_VB_11");
        listKey.add("ENG_BTU_ECT_11");
        listKey.add("C_INTAKE_AIR_TEMP");
        listKey.add("C_ASP");
        listKey.add("ENG_BTU_THDEG_11");
        listKey.add("ENG_BTU_VO2_20");
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

        Log.d("_____________", "pos: " + position + " id: " + mDeviceList.get(position).getId() +
                " /\n Name: " + mDeviceList.get(position).getName() +
                " /\nRawName: " + mDeviceList.get(position).getRawName() +
                " /\n Value: " + mDeviceList.get(position).getValue() +
                " /\n RawValue: " + mDeviceList.get(position).getRawValue());

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
