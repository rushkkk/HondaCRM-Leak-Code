package vn.co.honda.hondacrm.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

    public static File getFileFromBitmap(final Context context, final Bitmap bitmap) {
        //create a file to write bitmap data
        String genFile = System.currentTimeMillis() + ".jpg";
        File fileUpload = new File(context.getCacheDir(), genFile);
        try {
            fileUpload.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileUpload);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception ex) {

        }
        return fileUpload;
    }

    public static String getRealPathFromURIT(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public static String getRealPathFromURI(Context context, Uri contentURI) {
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
    public static String getNameFile(File file) {
        return file.getName();
    }

    public static String getPathFile(File file) {
        return file.getAbsolutePath();
    }

    public static String genFileNameAuto(String formatImage) {
        return System.currentTimeMillis() + formatImage;
    }

    public static Bitmap convertFileToBitmap(File file){
        String filePath = file.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        return bitmap;
    }
}
