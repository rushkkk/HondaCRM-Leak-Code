package vn.co.honda.hondacrm.ui.activities.vehicles.VerifyVehicle;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.utils.BitmapUtil;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PermissionUtils;

public class CameraCustom extends BaseActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;
    private ImageView btn_Capture, btn_close;
    private Context myContext;
    private FrameLayout cameraPreview;
    public static Bitmap bitmap;
    private ImageView imageView;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        imageView = findViewById(R.id.shape);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;
        cameraPreview = findViewById(R.id.cPreview1);
        btn_Capture = findViewById(R.id.btn_Capture);
        btn_Capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showDialogLoadProgress(myContext);
                mCamera.takePicture(null, null, mPicture);
            }
        });
        btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(CameraCustom.this, Manifest.permission.CAMERA) == PackageManager
                .PERMISSION_GRANTED) {
            if (mCamera == null) {
                mCamera = Camera.open();
                mCamera.setDisplayOrientation(90);
                mPreview = new CameraPreview(myContext, mCamera);
                cameraPreview.addView(mPreview);
                Camera.Parameters params = mCamera.getParameters();
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                mCamera.setParameters(params);
                mPicture = getPictureCallback();
                mCamera.startPreview();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PermissionUtils.neverAskAgainSelected(CameraCustom.this, Manifest.permission.CAMERA)) {
                    PermissionUtils.displayNeverAskAgainDialog(CameraCustom.this);
                } else {
                    requestPermission();
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //when on Pause, release camera in order to be used from other applications
        releaseCamera();
    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.PictureCallback getPictureCallback() {
        Camera.PictureCallback picture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                mCamera.stopPreview();
                bitmap = RotateBitmap(data);
                File fileUpload = BitmapUtil.getFileFromBitmap(myContext, bitmap);
                bitmap.recycle();
                Intent intent = getIntent();
                intent.putExtra("image", fileUpload.getAbsolutePath());
                setResult(Activity.RESULT_OK, intent);
                DialogUtils.hideDialogLoadProgress();
                finish();
            }
        };
        return picture;
    }


    public static Bitmap RotateBitmap(byte[] source) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap bitmap = BitmapFactory.decodeByteArray(source, 0, source.length);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mCamera == null) {
                        mCamera = Camera.open();
                        mCamera.setDisplayOrientation(90);
                        mPreview = new CameraPreview(myContext, mCamera);
                        cameraPreview.addView(mPreview);
                        Camera.Parameters params = mCamera.getParameters();
                        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                        mCamera.setParameters(params);
                        mPicture = getPictureCallback();
                        mCamera.startPreview();
                    }
                } else {
                    PermissionUtils.setShouldShowStatus(this, Manifest.permission.CAMERA);
                    finish();
                }
                break;
        }
    }

}
