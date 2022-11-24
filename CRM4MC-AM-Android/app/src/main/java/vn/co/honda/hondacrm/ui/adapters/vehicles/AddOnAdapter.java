package vn.co.honda.hondacrm.ui.adapters.vehicles;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.vehicles.models.AddOn;

public class AddOnAdapter extends RecyclerView.Adapter<AddOnAdapter.ViewHolder> {


    private ArrayList<AddOn> list = new ArrayList<>();

    private Context context;

    public AddOnAdapter(ArrayList<AddOn> list, Context context) {

        this.list = list;

        this.context = context;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_cell_items, parent, false);
                return new ViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_grid_cell_items, parent, false);
                return new ViewHolder(view);
        }
        return null;
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int position) {
        AddOn addOn = list.get(position);
        holder.tv_job.setText(addOn.getJob());
        holder.tv_price.setText(addOn.getPrice());

        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#05000000"));
        } else if (position == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#1A000000"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#0D000000"));
        }
    }

    @Override

    public int getItemCount() {
        if(list == null){
            return 0;
        }
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_job, tv_price;

        public ViewHolder(View itemView) {

            super(itemView);

            tv_job = itemView.findViewById(R.id.tv_job);
            tv_price = itemView.findViewById(R.id.tv_jobPrice);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list != null) {
            AddOn object = list.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }
}