package vn.co.honda.hondacrm.ui.adapters.vehicles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.model.vehicle.Vehicle;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleHolder> {

    private Context context;
    private List<Vehicle> vehicleArrayList;
    private boolean isEdit = false;
    private IVehiclesCallBack mIVehiclesCallBack;
    private static String EMPTY = "";
    private ApiService apiService;
    private String mTypeFullAccessToken;
    private IVehicleListener mIVehicleListener;

    public VehicleAdapter(Context context, List<Vehicle> vehicleArrayList, String mTypeFullAccessToken, ApiService apiService) {
        this.context = context;
        this.vehicleArrayList = vehicleArrayList;
        this.mTypeFullAccessToken = mTypeFullAccessToken;
        this.apiService = apiService;
    }

    public VehicleAdapter() {
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public void setVehiclesCallBack(IVehiclesCallBack iVehiclesCallBack) {
        mIVehiclesCallBack = iVehiclesCallBack;
    }

    public void setVehicleListener(IVehicleListener iVehicleListener) {
        mIVehicleListener = iVehicleListener;
    }


    @NonNull
    @Override
    public VehicleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vehicle, viewGroup, false);
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
//        if (vehicle.getNextPm() != null) {
//            adapterHolder.txtNextPMDate.setText(vehicle.getNextPm());
//        }
        if (vehicle.getVin() != null) {
            Integer vinLength = vehicle.getVin().length();
            adapterHolder.txtVINCode.setText("***" + vehicle.getVin().substring(vinLength-6, vinLength));
        }

        adapterHolder.imgEditVehicle.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        adapterHolder.imgDeleteVehicle.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        adapterHolder.imgMoveVehicle.setVisibility(isEdit ? View.VISIBLE : View.GONE);

        if (vehicle.getVehicleType() != null && vehicle.getNextPm() != null && TextUtils.isEmpty(vehicle.getNextPm())) {
               adapterHolder.layoutNextPMDate.setVisibility(vehicle.getVehicleType() == 1 ? View.VISIBLE : View.GONE);
                adapterHolder.txtNextPMDate.setText(vehicle.getNextPm());
        } else {
            adapterHolder.layoutNextPMDate.setVisibility(View.GONE);
        }

        adapterHolder.imgDeleteVehicle.setOnClickListener(v -> {
            if (mIVehicleListener != null) {
                mIVehicleListener.deleteVehicle(vehicle);
            }
        });

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

        adapterHolder.imgEditVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIVehicleListener != null) {
                    mIVehicleListener.editVehicle(vehicle);
                }
            }
        });

//        if (vehicle.isWarning()) {
//            adapterHolder.txtVehicleName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.warning, 0);
//            adapterHolder.txtVehicleName.setCompoundDrawablePadding(10);
//        } else {
//            adapterHolder.txtVehicleName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//        }

        if (!isEdit) {
            adapterHolder.layoutItemVehicle.setForeground(context.getDrawable(R.drawable.item_vehicle_background));
            adapterHolder.layoutItemVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIVehicleListener != null) {
                        mIVehicleListener.onItemClick(vehicle.getVin());
                    }
                }
            });
        } else {
            adapterHolder.layoutItemVehicle.setForeground(context.getDrawable(R.drawable.transparent));
            adapterHolder.itemView.setOnClickListener(null);
            adapterHolder.layoutItemVehicle.setOnClickListener(null);
        }

//        if(vehicle.getTempImage().sameAs(((BitmapDrawable) context.getDrawable(R.drawable.ic_moto)).getBitmap())){
//            adapterHolder.imgVehicle.setScaleType(ImageView.ScaleType.CENTER);
//        } else {
//            adapterHolder.imgVehicle.setScaleType(ImageView.ScaleType.FIT_XY);
//        }
        adapterHolder.imgVehicle.setImageResource(R.drawable.circle_holder);
        if (vehicle.getVehicleType() != null && vehicle.getVehicleType() == 2) {
            if (vehicle.getVehicleImage() == null) {
                adapterHolder.containerHolder.setVisibility(View.VISIBLE);
                adapterHolder.circleVehicle.setVisibility(View.INVISIBLE);
                adapterHolder.imgVehicle.setImageResource(R.drawable.ic_vehicle_fake);
            } else if (vehicle.getTempImage() == null) {
                Picasso.with(context)
                        .load(vehicle.getVehicleImage())
                        .placeholder(R.drawable.circle_holder)
                        .error(R.drawable.circle_holder)
                        .into(adapterHolder.circleVehicle, new Callback() {
                            @Override
                            public void onSuccess() {
                                adapterHolder.containerHolder.setVisibility(View.INVISIBLE);
                                adapterHolder.circleVehicle.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {
                                adapterHolder.containerHolder.setVisibility(View.VISIBLE);
                                adapterHolder.circleVehicle.setVisibility(View.INVISIBLE);
                                adapterHolder.imgVehicle.setImageResource(R.drawable.ic_vehicle_fake);
                            }
                        });
            } else {
                adapterHolder.containerHolder.setVisibility(View.INVISIBLE);
                adapterHolder.circleVehicle.setVisibility(View.VISIBLE);
                adapterHolder.circleVehicle.setImageBitmap(vehicle.getTempImage());
            }
        } else if (vehicle.getVehicleType() != null && vehicle.getVehicleType() == 1) {
            if (vehicle.getVehicleImage() == null) {
                adapterHolder.containerHolder.setVisibility(View.VISIBLE);
                adapterHolder.circleVehicle.setVisibility(View.INVISIBLE);
                adapterHolder.imgVehicle.setImageResource(R.drawable.ic_moto);
            } else if (vehicle.getTempImage() == null) {
                Picasso.with(context)
                        .load(vehicle.getVehicleImage())
                        .placeholder(R.drawable.circle_holder)
                        .error(R.drawable.circle_holder)
                        .into(adapterHolder.circleVehicle, new Callback() {
                            @Override
                            public void onSuccess() {
                                adapterHolder.containerHolder.setVisibility(View.INVISIBLE);
                                adapterHolder.circleVehicle.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {
                                adapterHolder.circleVehicle.setVisibility(View.INVISIBLE);
                                adapterHolder.containerHolder.setVisibility(View.VISIBLE);
                                adapterHolder.imgVehicle.setImageResource(R.drawable.ic_moto);
                            }
                        });
            } else {
                adapterHolder.containerHolder.setVisibility(View.INVISIBLE);
                adapterHolder.circleVehicle.setVisibility(View.VISIBLE);
                adapterHolder.circleVehicle.setImageBitmap(vehicle.getTempImage());
            }
        }

    }

    @Override
    public int getItemCount() {
        if (vehicleArrayList == null) {
            return 0;
        }
        mIVehiclesCallBack.update(vehicleArrayList.size());
        return vehicleArrayList.size();
    }

    public class VehicleHolder extends RecyclerView.ViewHolder {

        private TextView txtVehicleName, txtLicensePlate, txtVINCode, txtVehicleModel, txtNextPMDate;
        private ImageView imgVehicle, imgConnect, imgEditVehicle, imgDeleteVehicle, imgMoveVehicle;
        private LinearLayout layoutNextPMDate;
        private CardView layoutItemVehicle;
        private View containerHolder;
        private CircleImageView circleVehicle;

        public VehicleHolder(@NonNull View itemView) {
            super(itemView);
            txtVehicleName = itemView.findViewById(R.id.txtVehicleName);
            txtLicensePlate = itemView.findViewById(R.id.txtLicensePlate);
            txtVINCode = itemView.findViewById(R.id.txtVINCode);
            txtVehicleModel = itemView.findViewById(R.id.txtVehicleModel);
            txtNextPMDate = itemView.findViewById(R.id.txtNextPMDate);
            imgVehicle = itemView.findViewById(R.id.imgVehicle);
            imgConnect = itemView.findViewById(R.id.imgConnect);
            imgEditVehicle = itemView.findViewById(R.id.imgEditVehicle);
            imgDeleteVehicle = itemView.findViewById(R.id.imgDeleteVehicle);
            imgMoveVehicle = itemView.findViewById(R.id.img_vehicle_move);
            layoutItemVehicle = itemView.findViewById(R.id.layout_item_vehicle);
            layoutNextPMDate = itemView.findViewById(R.id.layout_next_pm_date);
            containerHolder = itemView.findViewById(R.id.frContainerHolder);
            circleVehicle = itemView.findViewById(R.id.circleVehicle);
        }
    }

    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(vehicleArrayList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(vehicleArrayList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        vehicleArrayList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Vehicle> list) {
        vehicleArrayList.addAll(list);
        notifyDataSetChanged();
    }
}
