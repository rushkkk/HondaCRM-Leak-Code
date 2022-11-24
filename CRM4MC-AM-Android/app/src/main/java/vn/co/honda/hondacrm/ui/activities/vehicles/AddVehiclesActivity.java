package vn.co.honda.hondacrm.ui.activities.vehicles;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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
import vn.co.honda.hondacrm.net.model.vehicle.Datum;
import vn.co.honda.hondacrm.net.model.vehicle.Vehicle;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleResponse;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleType;
import vn.co.honda.hondacrm.net.model.vin.VinResponse;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.vehicles.VerifyVehicle.VerifyVehicleActivity;
import vn.co.honda.hondacrm.ui.adapters.vehicles.spiner.SpinnerAdapter;
import vn.co.honda.hondacrm.ui.adapters.vehicles.spiner.SpinnerColorAdapter;
import vn.co.honda.hondacrm.ui.adapters.vehicles.spiner.SpinnerDatumAdapter;
import vn.co.honda.hondacrm.ui.fragments.connected.local.model.VehicleEntity;
import vn.co.honda.hondacrm.utils.BitmapUtil;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PermissionUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;
import vn.co.honda.hondacrm.utils.Utils;

import static vn.co.honda.hondacrm.utils.Constants.MAX_VIN_NUMBER;
import static vn.co.honda.hondacrm.utils.Constants.REQUEST_CAMERA_CODE;
import static vn.co.honda.hondacrm.utils.Constants.REQUEST_GALLERY_CODE;

public class AddVehiclesActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView webView;
    private RadioGroup radioGroup;
    private RadioButton radioAutomobile, radioMotocycler;
    private ImageView imgNFC, imgTutorial, imgChosePicture;
    private EditText editVIN;
    private TextView btnAddVehicle, txtErrorVIN;
    File fileUpload;
    ApiService apiService;
    String mTypeFullAccessToken;
    String mVin;
    Integer isConnected;
    Integer isHonDa;
    Integer typeVehicle;
    private static List<Datum> vehicleType1;
    private static List<Datum> vehicleBrand;
    private static SpinnerAdapter spinnerBrandAdapter;
    private static SpinnerDatumAdapter spinnerTypeAdapter;
    private EditText edtAddVehicleHondaName;
    private EditText edtAddVehicleHondaNumberPlate;
    String nameVeiclaHonda = "";
    String numberPlateHonda = "";
    String nameVeiclaOther = "";
    String numberPlateOther = "";
    EditText edNameOther;
    EditText edNumberPlateOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        apiService = ApiClient.getClient(AddVehiclesActivity.this.getApplicationContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(AddVehiclesActivity.this.getApplicationContext());
        initComponent();
        setEnableButton(false);
        imgNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editVIN.setText("VIN12345678910111213");
                editVIN.setSelection(editVIN.length());
            }
        });
        changeTypeVIN(radioGroup.getCheckedRadioButtonId());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeTypeVIN(checkedId);
            }
        });
        //get data list other vehicles


        btnAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVin = String.valueOf(editVIN.getText());
                if (TextUtils.isEmpty(mVin) || mVin.length() < MAX_VIN_NUMBER) {
                    txtErrorVIN.setVisibility(View.VISIBLE);
                    txtErrorVIN.setText(R.string.VIN_incorrect);
                } else {
                    txtErrorVIN.setVisibility(View.INVISIBLE);
                    requestVerifyVIN();
                }
            }
        });
        editVIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence.toString())) {
                    setEnableButton(false);
                } else {
                    setEnableButton(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void changeTypeVIN(int checkedId) {
        if (checkedId == radioAutomobile.getId()) {
            imgNFC.setVisibility(View.VISIBLE);
            imgTutorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    txtErrorVIN.setVisibility(View.INVISIBLE);
                    requestGuideLine();
                }
            });
        } else {
            imgNFC.setVisibility(View.GONE);
            imgTutorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    txtErrorVIN.setVisibility(View.INVISIBLE);
                    requestGuideGetVin();
                }
            });
        }
    }

    public void setEnableButton(boolean b) {
        btnAddVehicle.setEnabled(b);
        if (b) {
            btnAddVehicle.setBackground(getDrawable(R.drawable.selector_red_button));
        } else {
            btnAddVehicle.setBackground(getDrawable(R.drawable.btn_signup_disable));
        }
    }

    private void requestGuideLine() {
        showProgressDialog();
        apiService.getGuideline()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Guide>() {
                    @Override
                    public void onSuccess(Guide response) {
                        hideProgressDialog();
                        DialogUtils.showDialogGuideGetVin(response.getData().get(0).getContentEn(), getString(R.string.lb_get_vin_nfc_title), AddVehiclesActivity.this, R.layout.dialog_guideline, 0.9f, 0.7f, new DialogUtils.DialogListener() {
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
                        hideProgressDialog();
                        DialogUtils.showDialogConfirmLogin(AddVehiclesActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                    }
                });
    }

    private void requestGuideGetVin() {
        showProgressDialog();
        apiService.getGuideline()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Guide>() {
                    @Override
                    public void onSuccess(Guide response) {
                        hideProgressDialog();
                        DialogUtils.showDialogGuideGetVin(response.getData().get(0).getContentEn(), getString(R.string.lv_get_vin_title), AddVehiclesActivity.this, R.layout.dialog_guideline, 0.9f, 0.7f, new DialogUtils.DialogListener() {
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
                        hideProgressDialog();
                        DialogUtils.showDialogConfirmLogin(AddVehiclesActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                    }
                });
    }

    private void hideProgressDialog() {
        DialogUtils.hideDialogLoadProgress();
    }

    private void showProgressDialog() {
        DialogUtils.showDialogLoadProgress(AddVehiclesActivity.this);
    }

    private void initComponent() {
        setTitleHeader(getString(R.string.lb_add_vehicle_title));
        radioGroup = findViewById(R.id.gr_radio_select_vehicle);
        radioAutomobile = findViewById(R.id.radioAutomobile);
        radioMotocycler = findViewById(R.id.radioMotocycler);
        imgTutorial = findViewById(R.id.img_add_vin_tutorial);
        btnAddVehicle = findViewById(R.id.txtAddVIN);
        txtErrorVIN = findViewById(R.id.txtErrorVIN);
        editVIN = findViewById(R.id.editVINNumber);
        imgNFC = findViewById(R.id.imgNFC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        imgChosePicture.setScaleType(ImageView.ScaleType.FIT_XY);
                        imgChosePicture.setImageBitmap(photo);
                        fileUpload = BitmapUtil.getFileFromBitmap(AddVehiclesActivity.this, photo);
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        //Display an error
                        return;
                    }
                    try {
                        if (data.getData() != null) {
                            InputStream inputStream = getBaseContext().getContentResolver().openInputStream(data.getData());
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imgChosePicture.setScaleType(ImageView.ScaleType.FIT_XY);
                            imgChosePicture.setImageBitmap(bitmap);
                            fileUpload = BitmapUtil.getFileFromBitmap(AddVehiclesActivity.this, bitmap);
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

        }
    }


    private void isHondaVehicle(boolean isHondaVehicle, boolean isMoto) {
        Vehicle vehicle = new Vehicle();
        if (isHondaVehicle) {
            showDialogAddVehicleHonda(isMoto, R.layout.dialog_add_vehicle_of_honda, vehicle);
        } else {
            if (isMoto) {
                typeVehicle = 1;
            } else {
                typeVehicle = 2;
            }
            showDialogVehicleAnotherBrand(isMoto, R.layout.dialog_add_vehicle_another_brand, typeVehicle);
        }
    }

    private void requestAddVehicle(Vehicle vehicle) {
        showProgressDialog();
        RequestBody requestFile;
        MultipartBody.Part body = null;
        if (fileUpload != null) {
            requestFile = RequestBody.create(MediaType.parse("image/png"), fileUpload);
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("vehicle_image", fileUpload.getName(), requestFile);
        }

        RequestBody vehicleVIN = RequestBody.create(
                MediaType.parse("text/plain"),
                mVin);

        RequestBody vehicleName = RequestBody.create(
                MediaType.parse("text/plain"),
                vehicle.getVehicleName());

        RequestBody vehiclePlate = RequestBody.create(
                MediaType.parse("text/plain"),
                vehicle.getLicensePlate());
        apiService.registerVehicleInfoNew(
                mTypeFullAccessToken,
                vehicleVIN,
                vehicleName,
                vehiclePlate,
                vehicle.getVehicleType(),
                vehicle.getIsConnected(),
                vehicle.getIsHonda(),
                vehicle.getBodyStyle(),
                body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VehicleResponse>() {
                    @Override
                    public void onSuccess(VehicleResponse response) {
                        hideProgressDialog();
                        Intent data = new Intent();
                        VehicleEntity v = new VehicleEntity(vehicle.getVin(), vehicle.getVehicleName(), vehicle.getLicensePlate(),
                                vehicle.getVehicleType() + "", vehicle.getIsConnected() == 1, false, false, "", "", "2000", "3", "2", "0");
                        Gson gson = new Gson();
                        String jsonData = gson.toJson(v);
                        data.putExtra("vehicleReturn", vehicle.getIsConnected());
                        data.putExtra("vehicleData", jsonData);
                        setResult(Activity.RESULT_OK, data);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressDialog();
                        DialogUtils.showDialogConfirmLogin(AddVehiclesActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                    }
                });
    }

    private void requestAddVehicleOtherBrand(Vehicle vehicle) {
        showProgressDialog();
        RequestBody requestFile;
        MultipartBody.Part body = null;
        if (fileUpload != null) {
            requestFile = RequestBody.create(MediaType.parse("image/png"), fileUpload);
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("vehicle_image", fileUpload.getName(), requestFile);
        }

        RequestBody vehicleVIN = RequestBody.create(
                MediaType.parse("text/plain"),
                mVin);

        RequestBody vehicleName = RequestBody.create(
                MediaType.parse("text/plain"),
                vehicle.getVehicleName());

        RequestBody vehiclePlate = RequestBody.create(
                MediaType.parse("text/plain"),
                vehicle.getLicensePlate());

        RequestBody vehicleModel = RequestBody.create(
                MediaType.parse("text/plain"),
                vehicle.getVehicleModel());

        RequestBody vehicleColor = RequestBody.create(
                MediaType.parse("text/plain"),
                vehicle.getVehicleColor());
        apiService.registerVehicleInfoOtherBrand(
                mTypeFullAccessToken,
                vehicleVIN,
                vehicleName,
                vehiclePlate,
                vehicle.getVehicleType(),
                vehicle.getIsConnected(),
                vehicle.getIsHonda(),
                vehicle.getVehicleBrand(),
                vehicleModel,
                vehicleColor,
                vehicle.getBodyStyle(),
                body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VehicleResponse>() {
                    @Override
                    public void onSuccess(VehicleResponse response) {
                        hideProgressDialog();
                        Intent data = new Intent();
                        data.putExtra("vehicleReturn", vehicle.getIsConnected());
                        data.putExtra("key_vehicle", vehicle);
                        setResult(Activity.RESULT_OK, data);
                        finish();

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressDialog();
                        DialogUtils.showDialogConfirmLogin(AddVehiclesActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                    }
                });
    }

    private void requestVerifyVIN() {
        showProgressDialog();
        apiService
                .verifyVin(mTypeFullAccessToken, mVin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VinResponse>() {
                    @Override
                    public void onSuccess(VinResponse response) {
                        if (response != null && response.getCode() != null && response.getCode() == 0) {
                            DialogUtils.showDialogDefault(AddVehiclesActivity.this, R.layout.dialog_verify_vin, 0.9f, 0.5f, new DialogUtils.DialogListener() {
                                @Override
                                public void okButtonClick(Dialog dialog) {
                                    Intent intent = new Intent(AddVehiclesActivity.this, VerifyVehicleActivity.class);
                                    intent.putExtra("VIN", String.valueOf(editVIN.getText()));
                                    startActivity(intent);
                                    dialog.dismiss();
                                }

                                @Override
                                public void cancelButtonClick() {

                                }
                            });
                        } else if (response != null && response.getCode() != null && response.getCode() == 1) {
                            isConnected = convertBooleanToInteger(response.getVinType().getIsConnected());
                            isHonDa = convertBooleanToInteger(response.getVinType().getIsConnected());
                            boolean isMoto;
                            if (radioGroup.getCheckedRadioButtonId() == radioAutomobile.getId()) {
                                isMoto = false;
                            } else {
                                isMoto = true;
                            }

                            isHondaVehicle(response.getVinType().getIsHonda(), isMoto);
                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressDialog();
                        DialogUtils.showDialogConfirmLogin(AddVehiclesActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                    }
                });
    }

    private int convertBooleanToInteger(boolean mBoolean) {
        if (!mBoolean) {
            return 0;
        } else {
            return 1;
        }
    }


    public void showDialogAddVehicleHonda(boolean isMoto, int layoutRes, Vehicle vehicle) {


        Dialog dialog = getDialogDefault(layoutRes);
        imgChosePicture = dialog.findViewById(R.id.img_choose_picture);
        TextView txtOK = dialog.findViewById(R.id.btn_ok);
        TextView txtCancel = dialog.findViewById(R.id.btn_cancel);
        edtAddVehicleHondaName = dialog.findViewById(R.id.edt_add_vehicle_honda_name);
        edtAddVehicleHondaNumberPlate = dialog.findViewById(R.id.edt_add_vehicle_honda_number_plate);
        setEnableButton(txtOK, false);
        if (isMoto) {
            imgChosePicture.setImageResource(R.drawable.ic_edit_moto);
        } else {
            imgChosePicture.setImageResource(R.drawable.ic_edit_oto);
        }
        imgChosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showDialogChooseImage(AddVehiclesActivity.this, R.layout.dialog_capture, new DialogUtils.DialogChooseImageListener() {
                    @Override
                    public void onCameraClick() {
                        if (ContextCompat.checkSelfPermission(AddVehiclesActivity.this, Manifest.permission.CAMERA) == PackageManager
                                .PERMISSION_GRANTED) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            AddVehiclesActivity.this.startActivityForResult(takePicture, REQUEST_CAMERA_CODE);
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (PermissionUtils.neverAskAgainSelected(AddVehiclesActivity.this, Manifest.permission.CAMERA)) {
                                    PermissionUtils.displayNeverAskAgainDialog(AddVehiclesActivity.this);
                                } else {
                                    PermissionUtils.requestPermission(AddVehiclesActivity.this);
                                }
                            }
                        }
                    }

                    @Override
                    public void onGalleryClick() {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        AddVehiclesActivity.this.startActivityForResult(intent, REQUEST_GALLERY_CODE);
                    }

                    @Override
                    public void cancelButtonClick() {

                    }
                });

            }
        });
        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imgChoose = dialog.findViewById(R.id.img_choose_picture);
                TextView txtErrorName = dialog.findViewById(R.id.txtErrorVehicleName);
                TextView txtErrorPlate = dialog.findViewById(R.id.txtErrorVehiclePlate);
                String vehicleHondaName = String.valueOf(edtAddVehicleHondaName.getText()).trim();
                String vehicleHondaNumberPlate = String.valueOf(edtAddVehicleHondaNumberPlate.getText()).trim();
                boolean check = true;
                if (TextUtils.isEmpty(vehicleHondaName)) {
                    txtErrorName.setText(getText(R.string.label_error_empty));
                    txtErrorName.setVisibility(View.VISIBLE);
                    check = false;
                } else {
                    txtErrorName.setVisibility(View.INVISIBLE);
                    if (!TextUtils.isEmpty(vehicleHondaNumberPlate)) {
                        check = true;
                    }
                }
                if (TextUtils.isEmpty(vehicleHondaNumberPlate)) {
                    txtErrorPlate.setText(getText(R.string.label_error_empty));
                    txtErrorPlate.setVisibility(View.VISIBLE);
                    check = false;
                } else {
                    txtErrorPlate.setVisibility(View.INVISIBLE);
                    if (!TextUtils.isEmpty(vehicleHondaName)) {
                        check = true;
                    }
                }

                if (check) {
                    vehicle.setVin(mVin);
                    vehicle.setVehicleName(vehicleHondaName);
                    vehicle.setLicensePlate(vehicleHondaNumberPlate);
                    vehicle.setIsHonda(isHonDa);
                    vehicle.setIsConnected(isHonDa);
                    vehicle.setNextPm("09/2019");
                    if (radioGroup.getCheckedRadioButtonId() == radioAutomobile.getId()) {
                        vehicle.setVehicleType(2);
                    } else {
                        vehicle.setVehicleType(1);
                    }
                    vehicle.setBodyStyle(1);
                    vehicle.setVehicleBrand(1);
                    vehicle.setVehicleModel("Innova");
                    if (!imgChoose.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_edit_oto).getConstantState())) {
                        Bitmap vehiclePicture = ((BitmapDrawable) imgChoose.getDrawable()).getBitmap();
                        vehicle.setTempImage(vehiclePicture);
                    }
                    requestAddVehicle(vehicle);
                    dialog.dismiss();
                }
            }
        });
        edtAddVehicleHondaName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameVeiclaHonda = charSequence.toString();
                if (TextUtils.isEmpty(nameVeiclaHonda) || TextUtils.isEmpty(numberPlateHonda)) {
                    setEnableButton(txtOK, false);
                } else {
                    setEnableButton(txtOK, true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtAddVehicleHondaNumberPlate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                numberPlateHonda = charSequence.toString();
                if (TextUtils.isEmpty(nameVeiclaHonda) || TextUtils.isEmpty(numberPlateHonda)) {
                    setEnableButton(txtOK, false);
                } else {
                    setEnableButton(txtOK, true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void setEnableButton(TextView btn, boolean b) {
        btn.setEnabled(b);
        if (b) {
            btn.setBackground(getDrawable(R.drawable.selector_red_button));
        } else {
            btn.setBackground(getDrawable(R.drawable.btn_signup_disable));
        }
    }

    public void showDialogVehicleAnotherBrand(Boolean isMoto, int layoutRes, Integer type) {
        Dialog dialog = getDialogDefault(layoutRes);
        Vehicle vehicle = new Vehicle();
        TextView txtOK = dialog.findViewById(R.id.btn_ok);
        TextView txtCancel = dialog.findViewById(R.id.btn_cancel);
        edNameOther = dialog.findViewById(R.id.edt_vehicle_name);
        edNumberPlateOther = dialog.findViewById(R.id.edt_number_plate);
        setEnableButton(txtOK, false);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        imgChosePicture = dialog.findViewById(R.id.img_picture);
        if (isMoto) {
            imgChosePicture.setImageResource(R.drawable.ic_edit_moto);
        } else {
            imgChosePicture.setImageResource(R.drawable.ic_edit_oto);
        }

        Spinner spBrand = dialog.findViewById(R.id.sp_brand);
        Spinner spType = dialog.findViewById(R.id.sp_type);
        Spinner spColor = dialog.findViewById(R.id.sp_color);

        requestBodyStype(type);
        requestgetVehicleBrand(type);
        vehicleType1 = new ArrayList<>();
        vehicleBrand = new ArrayList<>();
        spinnerBrandAdapter = new SpinnerAdapter(this, vehicleBrand);
        spBrand.setAdapter(spinnerBrandAdapter);
        spinnerTypeAdapter = new SpinnerDatumAdapter(this, vehicleType1);
        spType.setAdapter(spinnerTypeAdapter);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> color = new ArrayList<String>();
        color.add(this.getResources().getString(R.string.txt_red));
        color.add(this.getResources().getString(R.string.txt_orange));
        color.add(this.getResources().getString(R.string.txt_yellow));
        color.add(this.getResources().getString(R.string.txt_green));
        color.add(this.getResources().getString(R.string.txt_blue));
        color.add(this.getResources().getString(R.string.txt_indigo));
        color.add(this.getResources().getString(R.string.txt_violet));
        SpinnerColorAdapter adapterSpinnerColor = new SpinnerColorAdapter(this, color);
        spColor.setAdapter(adapterSpinnerColor);
        spColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imgChosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showDialogChooseImage(AddVehiclesActivity.this, R.layout.dialog_capture, new DialogUtils.DialogChooseImageListener() {
                    @Override
                    public void onCameraClick() {
                        if (ContextCompat.checkSelfPermission(AddVehiclesActivity.this, Manifest.permission.CAMERA) == PackageManager
                                .PERMISSION_GRANTED) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            AddVehiclesActivity.this.startActivityForResult(takePicture, REQUEST_CAMERA_CODE);
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (PermissionUtils.neverAskAgainSelected(AddVehiclesActivity.this, Manifest.permission.CAMERA)) {
                                    PermissionUtils.displayNeverAskAgainDialog(AddVehiclesActivity.this);
                                } else {
                                    PermissionUtils.requestPermission(AddVehiclesActivity.this);
                                }
                            }
                        }
                    }

                    @Override
                    public void onGalleryClick() {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_GALLERY_CODE);
                    }

                    @Override
                    public void cancelButtonClick() {

                    }
                });
            }
        });
        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edModel = dialog.findViewById(R.id.edt_model);
                TextView txtErrorName = dialog.findViewById(R.id.txtErrorVehicleName);
                TextView txtErrorPlate = dialog.findViewById(R.id.txtErrorVehiclePlate);
                TextView txtErrorModel = dialog.findViewById(R.id.txtErrorVehicleModel);
                Spinner spType = dialog.findViewById(R.id.sp_type);
                Spinner spColor = dialog.findViewById(R.id.sp_color);
                Spinner spBrand = dialog.findViewById(R.id.sp_brand);
                ImageView imgChoose = dialog.findViewById(R.id.img_picture);
                String vehicleName = edNameOther.getText().toString().trim();
                String vehicleNumber = edNumberPlateOther.getText().toString().trim();
                String model = edModel.getText().toString();
                String color = spColor.getSelectedItem().toString();
                String type = spType.getSelectedItem().toString();
                String brand = spBrand.getSelectedItem().toString();
                boolean check = true;
                if (TextUtils.isEmpty(vehicleName)) {
                    txtErrorName.setText(getText(R.string.label_error_empty));
                    txtErrorName.setVisibility(View.VISIBLE);
                    check = false;
                } else {
                    txtErrorName.setVisibility(View.INVISIBLE);
                    if (!TextUtils.isEmpty(vehicleNumber) && TextUtils.isEmpty(model)) {
                        check = true;
                    }
                }
                if (TextUtils.isEmpty(vehicleNumber)) {
                    txtErrorPlate.setText(getText(R.string.label_error_empty));
                    txtErrorPlate.setVisibility(View.VISIBLE);
                    check = false;
                } else {
                    txtErrorPlate.setVisibility(View.INVISIBLE);
                    if (!TextUtils.isEmpty(vehicleName) && TextUtils.isEmpty(model)) {
                        check = true;
                    }
                }

                if (check) {
                    vehicle.setVin(mVin);
                    vehicle.setVehicleName(vehicleName);
                    vehicle.setLicensePlate(vehicleNumber);
                    vehicle.setIsHonda(isHonDa);
                    vehicle.setIsConnected(isHonDa);
                    Datum vehicleType = (Datum) spType.getSelectedItem();
                    vehicle.setBodyStyle(vehicleType.getId());
                    vehicle.setNextPm("09/09/2019");
                    vehicle.setVehicleType(typeVehicle);
                    Datum vehicleBrand = (Datum) spBrand.getSelectedItem();
                    vehicle.setVehicleBrand(vehicleBrand.getId());
                    vehicle.setVehicleColor(spColor.getSelectedItem().toString());
                    vehicle.setVehicleModel(String.valueOf(edModel.getText()));
                    if (!imgChoose.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_edit_oto).getConstantState())) {
                        Bitmap vehiclePicture = ((BitmapDrawable) imgChoose.getDrawable()).getBitmap();
                        vehicle.setTempImage(vehiclePicture);
                    }
                    requestAddVehicleOtherBrand(vehicle);
                    dialog.dismiss();
                }
            }
        });
        edNameOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameVeiclaOther = charSequence.toString();
                if (TextUtils.isEmpty(nameVeiclaOther) || TextUtils.isEmpty(numberPlateOther)) {
                    setEnableButton(txtOK, false);
                } else {
                    setEnableButton(txtOK, true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edNumberPlateOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                numberPlateOther = charSequence.toString();
                if (TextUtils.isEmpty(nameVeiclaOther) || TextUtils.isEmpty(numberPlateOther)) {
                    setEnableButton(txtOK, false);
                } else {
                    setEnableButton(txtOK, true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        spBrand.setOnTouchListener(mOnTouchListener);
        spType.setOnTouchListener(mOnTouchListener);
        spColor.setOnTouchListener(mOnTouchListener);
    }

    /**
     * Handle close keyboard when touch on spinner.
     */
    private final View.OnTouchListener mOnTouchListener = (v, event) -> {
        Utils.hideKeyboardFrom(AddVehiclesActivity.this, v);
        v.performClick();
        return false;
    };

    private Dialog getDialogDefault(int layoutRes) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(layoutRes);
        Window window = dialog.getWindow();
        int widthLayout = (int) (this.getResources().getDisplayMetrics().widthPixels * 0.9f);
        if (window != null) {
            window.setLayout(widthLayout, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(R.drawable.border_dialog);
        }
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();
        return dialog;
    }


    //get type
    private void requestBodyStype(Integer type) {
        apiService.getVehicleBodyStype(mTypeFullAccessToken, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VehicleType>() {
                    @Override
                    public void onSuccess(VehicleType vehicleType) {
                        if (vehicleType != null && vehicleType.getData() != null) {
                            vehicleType1.addAll(vehicleType.getData());
                            spinnerTypeAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        DialogUtils.showDialogConfirmLogin(AddVehiclesActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                    }
                });
    }

    //get brand
    private void requestgetVehicleBrand(Integer type) {
        apiService.getVehicleBrand(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VehicleType>() {
                    @Override
                    public void onSuccess(VehicleType vehicleType) {
                        if (vehicleType != null && vehicleType.getData() != null) {
                            vehicleBrand.addAll(vehicleType.getData());
                            spinnerBrandAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        DialogUtils.showDialogConfirmLogin(AddVehiclesActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case  Constants.PERMISSION_REQUEST_CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, REQUEST_CAMERA_CODE);
                } else {
                    PermissionUtils.setShouldShowStatus(this, Manifest.permission.CAMERA);
                }
                break;
        }
    }
}
