package com.honda.connmc.View.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honda.connmc.Model.ItemRawData;
import com.honda.connmc.R;

import java.util.ArrayList;
import java.util.List;

public class RawDataAdapter extends RecyclerView.Adapter<RawDataAdapter.Holder> {
    private final List<ItemRawData> mListRawData;
    private int mOldPosition;
    public interface OnItemClickListener {

        void onItemClick(String itemRawData);
    }
    private OnItemClickListener mListener;
    private Context mContext;
    public void setOnItemClick(OnItemClickListener listener) {
        mListener = listener;
    }

    public void addData(ItemRawData data) {
        mListRawData.add(data);
        notifyDataSetChanged();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView tvContent;
        LinearLayout linearItem;
        Holder(View v) {
            super(v);
            tvContent = v.findViewById(R.id.tvContent);
            linearItem = v.findViewById(R.id.linearRawItem);
        }
    }

    public RawDataAdapter(Context context) {
        this.mContext = context;
        mListRawData = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_raw_data, parent, false);
        Holder vh = new Holder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.tvContent.setText(mListRawData.get(holder.getAdapterPosition()).getRawData());
        if(mListRawData.get(holder.getAdapterPosition()).isSelected()) {
            holder.linearItem.setBackground(mContext.getResources().getDrawable(R.drawable.background_raw_item));
        } else {
            holder.linearItem.setBackground(null);
        }
        holder.linearItem.setOnClickListener(v -> {
            mListRawData.get(mOldPosition).setSelected(false);
            mOldPosition = holder.getAdapterPosition();
            mListener.onItemClick(mListRawData.get(holder.getAdapterPosition()).getRawData());
            boolean isSelect = mListRawData.get(holder.getAdapterPosition()).isSelected();
            mListRawData.get(holder.getAdapterPosition()).setSelected(!isSelect);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return mListRawData.size();
    }
}
