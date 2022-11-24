package vn.co.honda.hondacrm.ui.adapters.drawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.utils.Constants;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {
    //Dữ liệu hiện thị là danh sách sinh viên
    private ArrayList menuList;
    // Lưu Context để dễ dàng truy cập
    private Context mContext;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MenuRecyclerViewAdapter(ArrayList _menuList, Context mContext) {
        this.menuList = _menuList;
        this.mContext = mContext;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Nạp layout cho View biểu diễn phần tử
        View studentView = inflater.inflate(R.layout.row_listview, parent, false);

        return new ViewHolder(studentView);

    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        Menu menu = (Menu) menuList.get(position);
        if (position == Constants.ZERO) {
            holder.viewHeader.setVisibility(View.VISIBLE);
        } else {
            holder.viewHeader.setVisibility(View.GONE);
        }
        holder.itemTilte.setText(menu.getItemName());
        holder.itemIcon.setImageResource(menu.getItemIcon());

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    /**
     * Lớp nắm giữ cấu trúc view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTilte;
        public ImageView itemIcon;
        public View viewHeader;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemTilte = itemView.findViewById(R.id.tvItemName);
            itemIcon = itemView.findViewById(R.id.imgIcon);
            viewHeader = itemView.findViewById(R.id.viewHeader);
            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(itemView, getLayoutPosition());
            });

        }

    }


}