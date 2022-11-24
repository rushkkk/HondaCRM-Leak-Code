package vn.co.honda.hondacrm.ui.fragments.testdrive.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.model.dealer.Dealers;
import vn.co.honda.hondacrm.ui.activities.Dealer.model.Dealer;

public class DealersAdapterFull extends RecyclerView.Adapter<DealersAdapterFull.ViewHolder> {
    private List<Dealer> mData;
    private List<Dealers> mDataList;
    private LayoutInflater mInflater;
    private Context context;
    private OnClick onClick;
    private int type = 0;
    private boolean typeList;

    public DealersAdapterFull(Context context, List<Dealers> mDataList, OnClick onClick, boolean typeList) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.onClick = onClick;
        this.mDataList = mDataList;
        this.typeList = typeList;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_dealer_full, parent, false);
        return new ViewHolder(view);
    }

    public void type(int type) {
        this.type = type;
        notifyDataSetChanged();
    }
    public void changCheckBoxFull(){

    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        Dealers dealer = mDataList.get(position);
        //Integer.parseInt(dealer.getDealerType());
        holder.txtAddress.setText(dealer.getAddress());
        holder.txtDealerName.setText(dealer.getDealerName());
        holder.txtTimeOpen.setText(" "+dealer.getWorkingStart()+" ");
        holder.txtTimeClose.setText(" "+dealer.getWorkingEnd());
        holder.txtDistance.setText("" + dealer.getDistance());
        holder.txtPhoneNumber.setText(dealer.getPhone());
        if(dealer.getRate()!=null && Float.parseFloat(dealer.getRate()) >3) {
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.ratingBar.setRating(Float.parseFloat(dealer.getRate()));
        }else {
            holder.ratingBar.setVisibility(View.GONE);
        }

//        if (position == 0)
//            holder.viewBorder.setVisibility(View.GONE);
        if (dealer.getDealerType().equals("1")) {
            holder.imgDealer.setImageResource(R.drawable.ic_dealer_am);
        } else {
            holder.imgDealer.setImageResource(R.drawable.img_moto);
        }
        holder.imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + dealer.getPhone()));
                context.startActivity(callIntent);
            }
        });
        holder.lnItemDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick != null) {
                    onClick.onClickGroup(position, dealer);
                }
            }
        });

        holder.tvBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick != null) {
                    onClick.onClickBooking(dealer);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDealer;
        TextView txtDealerName;
        TextView txtAddress;
        TextView txtPhoneNumber;
        TextView txtTimeClose;
        TextView txtTimeOpen;
        TextView txtDistance;
        View viewBorder;
        RatingBar ratingBar;
        LinearLayout imgPhone;
        LinearLayout lnItemDealer;
        TextView tvBooking;
        TextView tvNumberLike;

        ViewHolder(View itemView) {
            super(itemView);
            imgDealer = itemView.findViewById(R.id.img_dealer);
            txtAddress = itemView.findViewById(R.id.txt_dealer_address);
            txtPhoneNumber = itemView.findViewById(R.id.txt_phone_dealer);
            txtDistance = itemView.findViewById(R.id.txt_distance);
            txtTimeClose = itemView.findViewById(R.id.txt_time_close);
            txtTimeOpen = itemView.findViewById(R.id.txt_time_open);
            txtDealerName = itemView.findViewById(R.id.txt_dealer_name);
            ratingBar = itemView.findViewById(R.id.rb_dealer);
            viewBorder = itemView.findViewById(R.id.view_border);
            imgPhone = itemView.findViewById(R.id.ln_phone_dealer);
            lnItemDealer = itemView.findViewById(R.id.ln_item_dealer);
            tvBooking = itemView.findViewById(R.id.btn_book_a_service);
            ratingBar = itemView.findViewById(R.id.rb_dealer);
            tvNumberLike = itemView.findViewById(R.id.tvNumberLike);
        }

    }
}
