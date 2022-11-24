package vn.co.honda.hondacrm.ui.activities.vehicles.models;

import android.graphics.Bitmap;

import java.io.File;

public class ImageVerifyVehicle {
    private Bitmap imgBitmap;
    private File fileUpload;

    public ImageVerifyVehicle() {
    }

    public ImageVerifyVehicle(Bitmap imgBitmap, File fileUpload) {
        this.imgBitmap = imgBitmap;
        this.fileUpload = fileUpload;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public File getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(File fileUpload) {
        this.fileUpload = fileUpload;
    }
}
