package vn.co.honda.hondacrm.ui.adapters.booking_service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.model.vehicle.Vehicle;

public class BookingVehicleAdapter extends RecyclerView.Adapter<BookingVehicleAdapter.VehicleHolder> {

    private Context context;
    private List<Vehicle> vehicleArrayList;
    private onClickItem mOnClickItem;

    public BookingVehicleAdapter(Context context, List<Vehicle> vehicleArrayList) {
        this.context = context;
        this.vehicleArrayList = vehicleArrayList;
    }

    @NonNull
    @Override
    public VehicleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_booking_vehicle, viewGroup, false);
        return new VehicleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleHolder adapterHolder, int i) {

        final Vehicle vehicle = vehicleArrayList.get(i);


        if (vehicle.getVehicleName() != null) {
            adapterHolder.txtVehicleName.setText(vehicle.getVehicleName());
        }
        if (vehicle.getLicensePlate() != null) {
            adapterHolder.txtLicensePlate.setText(vehicle.getLicensePlate());
        }
        if (vehicle.getVehicleModel() != null) {
            adapterHolder.txtVehicleModel.setText(vehicle.getVehicleModel());
            adapterHolder.txtVehicleModel.setVisibility(View.VISIBLE);
        } else {
            adapterHolder.txtVehicleModel.setVisibility(View.INVISIBLE);
        }
        if (vehicle.getVin() != null) {
            adapterHolder.txtVINCode.setText("***" + vehicle.getVin().substring(10, 17));
        }

        adapterHolder.txtNextPMDate.setText("09/2020    ");

        if (vehicle.getBodyStyle() != null) {
            adapterHolder.layoutNextPMDate.setVisibility(vehicle.getBodyStyle() == 1 ? View.VISIBLE : View.GONE);
        } else {
            adapterHolder.layoutNextPMDate.setVisibility(View.GONE);
        }

        if (vehicle.getIsConnected() != null && vehicle.getIsConnected() == 1) {
            adapterHolder.imgConnect.setVisibility(View.VISIBLE);
            if (vehicle.isConnecting()) {
                adapterHolder.imgConnect.setImageResource(R.drawable.ic_connected);
            } else {
                adapterHolder.imgConnect.setImageResource(R.drawable.ic_connect);
            }
        } else {
            adapterHolder.imgConnect.setVisibility(View.INVISIBLE);
        }
        adapterHolder.layoutItemVehicle.setForeground(context.getDrawable(R.drawable.item_vehicle_background));
        adapterHolder.layoutItemVehicle.setOnClickListener(view -> {
            if (mOnClickItem != null) {
                mOnClickItem.setOnClick(vehicle.getVehicleType(), vehicle.getVin());
            }
        });
        if (vehicle.getBodyStyle() != null && vehicle.getBodyStyle() == 2) {
            if (vehicle.getVehicleImage() == null) {
                adapterHolder.imgVehicle.setScaleType(ImageView.ScaleType.CENTER);
                adapterHolder.imgVehicle.setImageResource(R.drawable.ic_vehicle_fake);
            } else if (vehicle.getTempImage() == null) {
                Picasso.with(context)
                        .load(vehicle.getVehicleImage())
                        .error(R.drawable.ic_vehicle_fake)
                        .into(adapterHolder.imgVehicle, new Callback() {
                            @Override
                            public void onSuccess() {
                                adapterHolder.imgVehicle.setScaleType(ImageView.ScaleType.FIT_XY);
                            }

                            @Override
                            public void onError() {
                                adapterHolder.imgVehicle.setScaleType(ImageView.ScaleType.CENTER);
                            }
                        });
            } else {
                adapterHolder.imgVehicle.setImageBitmap(vehicle.getTempImage());
                adapterHolder.imgVehicle.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        } else if (vehicle.getBodyStyle() != null && vehicle.getBodyStyle() == 1) {
            if (vehicle.getVehicleImage() == null) {
                adapterHolder.imgVehicle.setScaleType(ImageView.ScaleType.CENTER);
                adapterHolder.imgVehicle.setImageResource(R.drawable.ic_moto);
            } else if (vehicle.getTempImage() == null) {
                Picasso.with(context)
                        .load(vehicle.getVehicleImage())
                        .error(R.drawable.ic_moto)
                        .into(adapterHolder.imgVehicle, new Callback() {
                            @Override
                            public void onSuccess() {
                                adapterHolder.imgVehicle.setScaleType(ImageView.ScaleType.FIT_XY);
                            }

                            @Override
                            public void onError() {
                                adapterHolder.imgVehicle.setScaleType(ImageView.ScaleType.CENTER);
                            }
                        });
            } else {
                adapterHolder.imgVehicle.setImageBitmap(vehicle.getTempImage());
                adapterHolder.imgVehicle.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    public void setOnClickItem(onClickItem clickItem) {
        this.mOnClickItem = clickItem;
    }

    @Override
    public int getItemCount() {
        if (vehicleArrayList == null) {
            return 0;
        }
        return vehicleArrayList.size();
    }

    public class VehicleHolder extends RecyclerView.ViewHolder {

        private TextView txtVehicleName, txtLicensePlate, txtVINCode, txtVehicleModel, txtNextPMDate;
        private ImageView imgVehicle, imgConnect;
        private LinearLayout layoutNextPMDate;
        private CardView layoutItemVehicle;

        public VehicleHolder(@NonNull View itemView) {
            super(itemView);
            txtVehicleName = itemView.findViewById(R.id.txtVehicleName);
            txtLicensePlate = itemView.findViewById(R.id.txtLicensePlate);
            txtVINCode = itemView.findViewById(R.id.txtVINCode);
            txtVehicleModel = itemView.findViewById(R.id.txtVehicleModel);
            txtNextPMDate = itemView.findViewById(R.id.txtNextPMDate);
            imgVehicle = itemView.findViewById(R.id.imgVehicle);
            imgConnect = itemView.findViewById(R.id.imgConnect);
            layoutItemVehicle = itemView.findViewById(R.id.layout_item_vehicle);
            layoutNextPMDate = itemView.findViewById(R.id.layout_next_pm_date);
        }
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        vehicleArrayList.clear();
        notifyDataSetChanged();
    }

    public interface onClickItem {
        void setOnClick(Integer VehicleType, String vin);
    }
}
