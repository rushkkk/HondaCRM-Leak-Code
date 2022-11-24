package vn.co.honda.hondacrm.ui.adapters.vehicles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.utils.BitmapUtil;
import vn.co.honda.hondacrm.utils.Constants;

public class ImageVerifyVehicleAdapter extends RecyclerView.Adapter<ImageVerifyVehicleAdapter.ImageVerifyVehicleHolder> {

    private Context context;
    private List<File> fileList;
    private OnSizeChange mOnSizeChange;

    public ImageVerifyVehicleAdapter(Context context, List<File> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    public ImageVerifyVehicleAdapter() {
    }

    public void setSizeChange(OnSizeChange onSizeChange) {
        mOnSizeChange = onSizeChange;
    }

    @NonNull
    @Override
    public ImageVerifyVehicleAdapter.ImageVerifyVehicleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_verify_vehicle, viewGroup, false);
        return new ImageVerifyVehicleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageVerifyVehicleHolder adapterHolder, int i) {
        File file = fileList.get(i);
            adapterHolder.imgContent.setImageBitmap(BitmapUtil.convertFileToBitmap(file));
            adapterHolder.imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileList.remove(i);
                    notifyDataSetChanged();
                }
            });
    }

    @Override
    public int getItemCount() {
        if (fileList == null) {
            mOnSizeChange.getSizeChange(Constants.ZERO);
            return Constants.ZERO;
        }
        mOnSizeChange.getSizeChange(fileList.size());
        return fileList.size();
    }

    public class ImageVerifyVehicleHolder extends RecyclerView.ViewHolder {
        private ImageView imgContent, imgRemove;
        public ImageVerifyVehicleHolder(@NonNull View itemView) {
            super(itemView);
            imgContent = itemView.findViewById(R.id.imgVerifyVehicle);
            imgRemove = itemView.findViewById(R.id.imgRemoveVerifyVehicle);
        }
    }

    public interface OnSizeChange {
        void getSizeChange(int size);
    }

}
