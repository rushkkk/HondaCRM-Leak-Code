package vn.co.honda.hondacrm.ui.adapters.connected;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.fragments.connected.models.DetailInformationItem;
import vn.co.honda.hondacrm.ui.fragments.connected.models.VehicleParameter;

/**
 * Created by TienTM13 on 25/07/2019.
 */

public class RcvConnectedDetailAdapter extends RecyclerView.Adapter<RcvConnectedDetailAdapter.RecyclerViewHolder> {
    private List<DetailInformationItem> alBaseData;
    private final VehicleParameter vp;
    private ArrayList<String> listKey = new ArrayList<>();

    public RcvConnectedDetailAdapter(List<DetailInformationItem> alBaseData) {
        this.alBaseData = alBaseData;
        this.vp = new VehicleParameter();
        setDataListKey();
    }

    private void setDataListKey() {
        listKey.add("C_ENGTEMP");
        listKey.add("C_ODO");
        listKey.add("ENG_BTU_VB_11");
        listKey.add("ENG_BTU_ECT_11");
        listKey.add("C_INTAKE_AIR_TEMP");
        listKey.add("C_ASP");
        listKey.add("ENG_BTU_THDEG_11");
        listKey.add("ENG_BTU_VO2_20");
    }

    public void updateListStatus(VehicleParameter vp2) {

        if (vp2 != null) {
            vp.setEngineTemperature(vp2.getEngineTemperature());
            vp.setDistanceOfMaintenance(vp2.getDistanceOfMaintenance());
            vp.setBatteryVoltage(vp2.getBatteryVoltage());
            vp.setWaterTemperature(vp2.getWaterTemperature());
            vp.setIntakeAirTemperature(vp2.getIntakeAirTemperature());
            vp.setGridOpeningDegree(vp2.getGridOpeningDegree());
            vp.setThrottleOpeningDegree(vp2.getThrottleOpeningDegree());
            vp.setO2Sensor(vp2.getO2Sensor());

        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_connected_vehicle_detail, parent, false);
        return new RecyclerViewHolder(view);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        TextView tvTitle;
        TextView tvContent;
        LinearLayout line;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_item_connected_detail_img);
            tvTitle = itemView.findViewById(R.id.tv_item_connected_detail_title);
            tvContent = itemView.findViewById(R.id.tv_item_connected_detail_content);
            line = itemView.findViewById(R.id.line);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int i) {

        Log.d("____________", "onBindViewHolder: temp " + vp.getEngineTemperature()
                + " distance " + vp.getDistanceOfMaintenance());

        holder.tvTitle.setText(alBaseData.get(i).getName());
        holder.ivImg.setImageResource(alBaseData.get(i).getImg());

        if (alBaseData.get(i).getId() == 0) {
            holder.tvContent.setText(resultText(vp.getEngineTemperature()));
        }
        if (alBaseData.get(i).getId() == 1) {
            holder.tvContent.setText(resultText(vp.getDistanceOfMaintenance()));
        }
        if (alBaseData.get(i).getId() == 2) {
            holder.tvContent.setText(resultText(vp.getBatteryVoltage()));
        }
        if (alBaseData.get(i).getId() == 3) {
            holder.tvContent.setText(resultText(vp.getWaterTemperature()));
        }
        if (alBaseData.get(i).getId() == 4) {
            holder.tvContent.setText(resultText(vp.getIntakeAirTemperature()));
        }
        if (alBaseData.get(i).getId() == 5) {
            holder.tvContent.setText(resultText(vp.getGridOpeningDegree()));
        }
        if (alBaseData.get(i).getId() == 6) {
            holder.tvContent.setText(resultText(vp.getThrottleOpeningDegree()));
        }
        if (alBaseData.get(i).getId() == 7) {
            holder.tvContent.setText(resultText(vp.getO2Sensor()));
        }
    }

    private String resultText(String value) {
        String str = "";
        if (value == null || value.equals("")) {
            str = "---";
        } else {
            str = value;
        }
        return str;
    }

    @Override
    public int getItemCount() {
        return alBaseData.size();
    }
}
