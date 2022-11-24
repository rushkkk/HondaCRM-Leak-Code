package vn.co.honda.hondacrm.ui.adapters.connected;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.customview.ExpandableTextView;
import vn.co.honda.hondacrm.ui.fragments.connected.models.DetailInformationItem;
import vn.co.honda.hondacrm.ui.fragments.connected.models.VehicleParameter;

public class RcvConnectedDetailListAdapter extends RecyclerView.Adapter<RcvConnectedDetailListAdapter.ViewHolder> {
    private List<DetailInformationItem> alBaseData;
    private final VehicleParameter vp;
    private Context context;

    public RcvConnectedDetailListAdapter(List<DetailInformationItem> alBaseData, Context context) {
        this.alBaseData = alBaseData;
        this.vp = new VehicleParameter();
        this.context = context;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_connected_detail_llc, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.postItem = i;
        holder.tvTitle.setText(alBaseData.get(i).getName());
        holder.imInfo.setImageResource(alBaseData.get(i).getImg());
        holder.tvDetail.setText(alBaseData.get(i).getDetail());
        if (alBaseData.get(i).getId() == 0) {
            holder.tvData.setText(resultText(vp.getEngineTemperature()));
        }
        if (alBaseData.get(i).getId() == 1) {
            holder.tvData.setText(resultText(vp.getDistanceOfMaintenance()));
        }
        if (alBaseData.get(i).getId() == 2) {
            holder.tvData.setText(resultText(vp.getBatteryVoltage()));
        }
        if (alBaseData.get(i).getId() == 3) {
            holder.tvData.setText(resultText(vp.getWaterTemperature()));
        }
        if (alBaseData.get(i).getId() == 4) {
            holder.tvData.setText(resultText(vp.getIntakeAirTemperature()));
        }
        if (alBaseData.get(i).getId() == 5) {
            holder.tvData.setText(resultText(vp.getGridOpeningDegree()));
        }
        if (alBaseData.get(i).getId() == 6) {
            holder.tvData.setText(resultText(vp.getThrottleOpeningDegree()));
        }
        if (alBaseData.get(i).getId() == 7) {
            holder.tvData.setText(resultText(vp.getO2Sensor()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvDetail.setOnClick();
                if (holder.tvDetail.getTrim()) {
                    holder.rlDetail.setVisibility(View.GONE);
                    changeState(false, holder.tvData, holder.imInfo, holder.tvTitle, holder.llInfo, holder.pbInfo);
                } else {
                    Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
                    holder.rlDetail.setVisibility(View.VISIBLE);
                    holder.rlDetail.startAnimation(slideDown);
                    changeState(true, holder.tvData, holder.imInfo, holder.tvTitle, holder.llInfo, holder.pbInfo);
                }
            }
        });
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

    private void changeState(
            Boolean state, TextView tvData,
            ImageView imgInfo,
            TextView tvTitle,
            LinearLayout llInfo,
            ProgressBar pbInfo) {
        Resources res = context.getResources();
        if (state) {
            tvData.setTextColor(Color.WHITE);
            tvTitle.setTextColor(Color.WHITE);
            imgInfo.setColorFilter(Color.WHITE);
            llInfo.setBackgroundColor(ResourcesCompat.getColor(res, R.color.red, null));
            pbInfo.setProgressDrawable(ResourcesCompat.getDrawable(res, R.drawable.custom_progressbar_state, null));
        } else {
            tvData.setTextColor(Color.BLACK);
            tvTitle.setTextColor(Color.BLACK);
            imgInfo.setColorFilter(Color.GRAY);
            llInfo.setBackgroundColor(ResourcesCompat.getColor(res, R.color.white, null));
            pbInfo.setProgressDrawable(ResourcesCompat.getDrawable(res, R.drawable.custom_progressbar_gray, null));
        }
    }


    private OnItemDetailClick onItemDetailClick;

    public void setOnItemDetailClick(OnItemDetailClick onItemDetailClick) {
        this.onItemDetailClick = onItemDetailClick;
    }

    public interface OnItemDetailClick {
        void onClickItem(int position, String name);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        int postItem = 0;
        ImageView imInfo;
        TextView tvTitle, tvData;
        ProgressBar pbInfo;
        ExpandableTextView tvDetail;
        LinearLayout llInfo;
        RelativeLayout rlDetail;

        public ViewHolder(@NonNull View view) {
            super(view);
            imInfo = view.findViewById(R.id.iv_info);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvData = view.findViewById(R.id.tv_data);
            pbInfo = view.findViewById(R.id.pbInfo);
            tvDetail = view.findViewById(R.id.tv_detail);
            llInfo = view.findViewById(R.id.ll_info);
            rlDetail = view.findViewById(R.id.rl_detail);
        }

    }
}
