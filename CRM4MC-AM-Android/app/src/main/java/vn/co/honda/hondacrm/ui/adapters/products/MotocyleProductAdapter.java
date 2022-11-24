package vn.co.honda.hondacrm.ui.adapters.products;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.model.products.MCProduct;

public class MotocyleProductAdapter  extends RecyclerView.Adapter<MotocyleProductAdapter.MCProductViewHolder> {

    private Context mContext;
    private List<MCProduct> mcProductList;

    public MotocyleProductAdapter(Context mContext, List<MCProduct> mcProductList) {
        this.mContext = mContext;
        this.mcProductList = mcProductList;
    }

    @NonNull
    @Override
    public MCProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_motocycle_product, viewGroup, false);
        return new MCProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MCProductViewHolder mcProductViewHolder, int i) {
            MCProduct mcProduct = mcProductList.get(i);
            mcProductViewHolder.tv_product_name.setText(mcProduct.getTitleMCProduct());
    }

    @Override
    public int getItemCount() {
        if(mcProductList != null){
            return mcProductList.size();
        }
        return 0;
    }

    public class MCProductViewHolder extends RecyclerView.ViewHolder {
        TextView tv_product_name;
        ImageView imgProduct;

        public MCProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(vn.co.honda.hondacrm.R.id.imgProduct);
            tv_product_name = itemView.findViewById(vn.co.honda.hondacrm.R.id.tv_product_name);
        }
    }
}
