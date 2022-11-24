package vn.co.honda.hondacrm.ui.adapters.vehicles;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

import vn.co.honda.hondacrm.R;

public class ImageVerifyVehicleInforAdapter extends RecyclerView.Adapter<ImageVerifyVehicleInforAdapter.viewHolder>{

    private List<Bitmap> images;
    private LayoutInflater layoutInflater;
    IClickRemoveImage clickRemoveImage;

    public ImageVerifyVehicleInforAdapter(List<Bitmap> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.item_image_verify_vehicle_infor, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        Bitmap imageVehicle = images.get(i);
        viewHolder.imageVehicle.setImageBitmap(imageVehicle);
        viewHolder.btnRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRemoveImage.clickRemoveButton(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        public ImageView imageVehicle;
        public ImageButton btnRemoveImage;

        public viewHolder(View view) {
            super(view);
            imageVehicle = view.findViewById(R.id.img_image_vehicle);
            btnRemoveImage = view.findViewById(R.id.btn_remove_image_vehicle);
        }
    }

    public interface IClickRemoveImage {
        void clickRemoveButton(int possion);
    }
}
