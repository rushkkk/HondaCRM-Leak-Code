package vn.co.honda.hondacrm.ui.adapters.vehicles;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation.Item;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation.ServiceHistory;
import vn.co.honda.hondacrm.ui.activities.vehicles.DetailCarServiceHistoryActivity;
import vn.co.honda.hondacrm.ui.activities.vehicles.models.ServiceItems;
import vn.co.honda.hondacrm.utils.Constants;

public class ServiceHistoryAdapter extends RecyclerView.Adapter<ServiceHistoryAdapter.ViewHolder> {

    private Context mContext;
    private List<ServiceHistory> serviceItemsArrayList;
    String vehicleName;

    public ServiceHistoryAdapter(Context mContext, List<ServiceHistory> serviceItemsArrayList, String vehicleName) {
        this.mContext = mContext;
        this.serviceItemsArrayList = serviceItemsArrayList;
        this.vehicleName = vehicleName;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_service_history_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ServiceHistory serviceItems = serviceItemsArrayList.get(position);
        if (serviceItems.getAmount() == null) {
            viewHolder.tv_price.setText("0 vnd");
        } else {
            viewHolder.tv_price.setText(serviceItems.getAmount() + " vnd");
        }
        if (serviceItems.getDistance() != null) {
            viewHolder.tv_km.setText(serviceItems.getDistance() + " km");
        } else {
            viewHolder.tv_km.setText(Constants.EMPTY);
        }
        if (serviceItems.getTime() != null) {
            viewHolder.tv_date.setText(serviceItems.getTime());
        } else {
            viewHolder.tv_date.setText(Constants.EMPTY);
        }

        if (serviceItems.getServiceName() != null) {
            viewHolder.tv_maintain.setText(serviceItems.getServiceName());
        } else {
            viewHolder.tv_maintain.setText(Constants.EMPTY);
        }
        if (serviceItems.getDealerName() != null) {
            viewHolder.tv_numAgency.setText(serviceItems.getDealerName());
        } else {
            viewHolder.tv_numAgency.setText(Constants.EMPTY);
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailCarServiceHistoryActivity.class);
                intent.putExtra("id_service_history", serviceItems.getServiceId());
                intent.putExtra("vehicle_name", vehicleName);
                mContext.startActivity(intent);
            }
        });

        if (serviceItems.getRating() != null) {
            if (serviceItems.getRating() <= 3) {
                viewHolder.ratingBarService.setVisibility(View.GONE);
            } else {
                viewHolder.ratingBarService.setRating(serviceItems.getRating());
            }
        } else {
            viewHolder.ratingBarService.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (serviceItemsArrayList == null) {
            return 0;
        }
        if (serviceItemsArrayList.size() > 3) {
            return 3;
        }
        return serviceItemsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_address, tv_price, tv_titleKm, tv_km, tv_date, tv_maintain, tv_vehicleName, tv_numAgency;
        RatingBar ratingBarService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_address = itemView.findViewById(vn.co.honda.hondacrm.R.id.tv_numAgency);
            tv_price = itemView.findViewById(vn.co.honda.hondacrm.R.id.tv_numPrice);
            tv_titleKm = itemView.findViewById(vn.co.honda.hondacrm.R.id.tv_km);
            tv_km = itemView.findViewById(vn.co.honda.hondacrm.R.id.tv_numKm);
            tv_date = itemView.findViewById(vn.co.honda.hondacrm.R.id.tv_numDate);
            tv_maintain = itemView.findViewById(vn.co.honda.hondacrm.R.id.tv_maintain);
            tv_vehicleName = itemView.findViewById(R.id.tv_titleVehicle);
            ratingBarService = itemView.findViewById(R.id.ratingBarService);
            tv_numAgency = itemView.findViewById(R.id.tv_numAgency);
        }
    }

}
