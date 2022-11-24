package vn.co.honda.hondacrm.ui.activities.vehicles.VerifyVehicle;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.base.Guide;
import vn.co.honda.hondacrm.net.model.base.Response;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.vehicles.ListVehicleActivity;
import vn.co.honda.hondacrm.ui.adapters.vehicles.ImageVerifyVehicleAdapter;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PermissionUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

import static vn.co.honda.hondacrm.utils.Constants.REQUEST_CAMERA_CODE;

public class VerifyVehicleActivity extends BaseActivity {
    private List<File> fileList = new ArrayList<>();
    private RecyclerView rvImageVerifyVehicle;
    private ImageVerifyVehicleAdapter imageVerifyVehicleAdapter;
    private TextView btnImageCapture, btnSendRequestVerify;
    private File fileUpload;
    private String VIN;
    private EditText editVerifyNote;
    private ApiService apiService;
    private String mTypeFullAccessToken;
    private ImageView btnGuildCaptureImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getStringExtra("VIN") != null) {
            VIN = getIntent().getStringExtra("VIN");

        }
        setContentView(R.layout.activity_verify_vehicle_infor);
        setTitleHeader(getString(R.string.verify_vehicle_information));
        initView();
    }

//    private void fakeData() {
//        for(int i = 0; i < 5;i++){
//            ImageVerifyVehicle imageVerifyVehicle = new ImageVerifyVehicle();
//            imageVerifyVehicleList.add(File);
//        }
//    }



    private void initView() {
        apiService = ApiClient.getClient(VerifyVehicleActivity.this.getApplicationContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(VerifyVehicleActivity.this.getApplicationContext());
        rvImageVerifyVehicle = findViewById(R.id.rvImageVerifyVehicle);
        imageVerifyVehicleAdapter = new ImageVerifyVehicleAdapter(this, fileList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImageVerifyVehicle.setLayoutManager(linearLayoutManager);
        rvImageVerifyVehicle.setAdapter(imageVerifyVehicleAdapter);
        imageVerifyVehicleAdapter.setSizeChange(mOnSizeChange);
        btnImageCapture = findViewById(R.id.btnCaptureImage);
        btnSendRequestVerify = findViewById(R.id.btnSendRequestVerify);
        editVerifyNote = findViewById(R.id.editVerifyNote);
        btnGuildCaptureImage = findViewById(R.id.btnGuildCaptureImage);
        setEnableButtonSend(false);
        btnImageCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(VerifyVehicleActivity.this, Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED) {
                            startActivityForResult(new Intent(VerifyVehicleActivity.this, CameraCustom.class), REQUEST_CAMERA_CODE);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (PermissionUtils.neverAskAgainSelected(VerifyVehicleActivity.this, Manifest.permission.CAMERA)) {
                                PermissionUtils.displayNeverAskAgainDialog(VerifyVehicleActivity.this);
                            } else {
                                PermissionUtils.requestPermission(VerifyVehicleActivity.this);
                            }
                        }
                    }
            }
        });


        btnSendRequestVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    requestVerifyVehicle();
            }
        });

        btnGuildCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestGuideGetVin();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getExtras() != null && data.getStringExtra("image") != null) {
                        fileUpload = new File(data.getStringExtra("image"));
                        fileList.add(fileUpload);
                        imageVerifyVehicleAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    private void requestVerifyVehicle() {
        DialogUtils.showDialogLoadProgress(this);
        RequestBody requestFile;
        MultipartBody.Part body = null;
        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[fileList.size()];

//        for (int index = 0; index < fileList.size(); index++) {
//            requestFile = RequestBody.create(MediaType.parse("image/png"), fileList.get(index));
//            fileList.get(index).getPath();
//            surveyImagesParts[index] =  MultipartBody.Part.createFormData("image", fileList.get(index).getName() , requestFile);
//        }

        for (int i = 0; i < fileList.size(); i++) {
            File file = new File(fileList.get(i).getPath());
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
            //Setting the file name as an empty string here causes the same issue, which is sending the request successfully without saving the files in the backend, so don't neglect the file name parameter.
            surveyImagesParts[i] = MultipartBody.Part.createFormData(String.format(Locale.ENGLISH, "image[%d]", i), file.getName(), fileBody);
        }


//            requestFile = RequestBody.create(MediaType.parse("image/*"), fileUpload);
//            // MultipartBody.Part is used to send also the actual file name
//            body = MultipartBody.Part.createFormData("image[]", fileUpload.getName(), requestFile);


        RequestBody vehicleVIN = RequestBody.create(
                MediaType.parse("text/plain"),
                VIN);

        RequestBody vehicleNote = RequestBody.create(
                MediaType.parse("text/plain"),
               String.valueOf(editVerifyNote.getText()));

        apiService.requestVerifyVehicle(
                mTypeFullAccessToken,
                vehicleVIN,
                vehicleNote,
                1,
                surveyImagesParts).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Response>() {
                    @Override
                    public void onSuccess(Response response) {
                        DialogUtils.hideDialogLoadProgress();
                        if (response != null && response.getCode() != null && response.getCode() == 1) {
                            DialogUtils.showDialogDefault(VerifyVehicleActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.request_successfull), new DialogUtils.DialogListener() {
                                @Override
                                public void okButtonClick(Dialog dialog) {
                                    startActivity(VerifyVehicleActivity.this, ListVehicleActivity.class, true);
                                }

                                @Override
                                public void cancelButtonClick() {

                                }
                            });
                        } else if (response != null) {
                            DialogUtils.showDialogConfirmLogin(VerifyVehicleActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, response.getMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtils.hideDialogLoadProgress();
                        DialogUtils.showDialogConfirmLogin(VerifyVehicleActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                    }
                });
    }

    private void requestGuideGetVin() {
        DialogUtils.showDialogLoadProgress(this);
        apiService.getGuideline()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Guide>() {
                    @Override
                    public void onSuccess(Guide response) {
                        DialogUtils.hideDialogLoadProgress();
                        DialogUtils.showDialogGuideGetVin(response.getData().get(0).getContentEn(), getString(R.string.lb_guide_take_photo), VerifyVehicleActivity.this, R.layout.dialog_guideline, 0.9f, 0.7f, new DialogUtils.DialogListener() {
                            @Override
                            public void okButtonClick(Dialog dialog) {

                            }

                            @Override
                            public void cancelButtonClick() {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtils.hideDialogLoadProgress();
                        DialogUtils.showDialogConfirmLogin(VerifyVehicleActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case  Constants.PERMISSION_REQUEST_CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (fileList != null && fileList.size() == 10) {
                        DialogUtils.showDialogConfirmLogin(VerifyVehicleActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, "The maximum number of photos is 10");
                    } else {
//                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(takePicture, REQUEST_CAMERA_CODE);
                        startActivityForResult(new Intent(VerifyVehicleActivity.this, CameraCustom.class), REQUEST_CAMERA_CODE);
                    }
                } else {
                    PermissionUtils.setShouldShowStatus(this, Manifest.permission.CAMERA);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        if (PermissionUtils.neverAskAgainSelected(VerifyVehicleActivity.this, Manifest.permission.CAMERA)) {
//                            PermissionUtils.displayNeverAskAgainDialog(VerifyVehicleActivity.this);
//                        }
//                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                                != PackageManager.PERMISSION_GRANTED) {
//
////                            else {
////                                showMessageOKCancel("You need to allow access permissions",
////                                        new DialogInterface.OnClickListener() {
////                                            @Override
////                                            public void onClick(DialogInterface dialog, int which) {
////                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                                                    requestPermission();
////                                                }
////                                            }
////                                        });
////                            }
//                        }
//                    }
                }
                break;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void setEnableButtonSend(boolean b) {
        btnSendRequestVerify.setEnabled(b);
    }

    public void setEnableButtonCapture(boolean b) {
        btnImageCapture.setEnabled(b);
    }

    /**
     *
     */
    private final ImageVerifyVehicleAdapter.OnSizeChange mOnSizeChange = size -> {
        // todo something
        if(size == 0){
            setEnableButtonSend(false);
        } else {
            setEnableButtonSend(true);
        }
        if(size == 10){
            setEnableButtonCapture(false);
        } else {
            setEnableButtonCapture(true);
        }
    };

}
