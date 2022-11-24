package vn.co.honda.hondacrm.ui.adapters.vehicles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.model.vehicle.Item;

public class ServiceFullHistoryAdapter extends RecyclerView.Adapter<ServiceFullHistoryAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Item> serviceItemsArrayList;
    private IItemListener mIItemListener;

    public ServiceFullHistoryAdapter(Context mContext, ArrayList<Item> serviceItemsArrayList) {
        this.mContext = mContext;
        this.serviceItemsArrayList = serviceItemsArrayList;
    }

    public void setListener(IItemListener iItemListener) {
        mIItemListener = iItemListener;
    }

    @NonNull
    @Override
    public ServiceFullHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_service_history_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceFullHistoryAdapter.ViewHolder viewHolder, int position) {
        Item item = serviceItemsArrayList.get(position);
        viewHolder.tv_address.setText(item.getAddress());
        viewHolder.tv_km.setText(item.getOdometer());
        if(item.getTotalAmount() == null) {
            viewHolder.tv_price.setVisibility(View.GONE);
        }else{
            viewHolder.tv_price.setVisibility(View.VISIBLE);
            viewHolder.tv_price.setText(item.getTotalAmount() + " vnd");
        }
        viewHolder.tv_price.setText(item.getTotalAmount() + " vnd");
        viewHolder.tv_date.setText(item.getRepairDate());
        if (item.getRating() == null || item.getRating() < 4) {
            viewHolder.ratingBar.setVisibility(View.GONE);
        } else {
            viewHolder.ratingBar.setVisibility(View.VISIBLE);
            viewHolder.ratingBar.setRating(item.getRating());
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIItemListener != null) {
                    mIItemListener.onItemClick(item.getId());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if(serviceItemsArrayList == null){
            return  0;
        }
        return serviceItemsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_address, tv_price, tv_km, tv_date;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_address = itemView.findViewById(R.id.tv_numAgency);
            tv_price = itemView.findViewById(R.id.tv_numPrice);
            tv_km = itemView.findViewById(R.id.tv_numKm);
            tv_date = itemView.findViewById(R.id.tv_numDate);
            ratingBar = itemView.findViewById(R.id.ratingBarService);
        }
    }


}
