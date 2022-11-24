package vn.co.honda.hondacrm.ui.activities.connected.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import butterknife.ButterKnife;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.base.Response;
import vn.co.honda.hondacrm.net.model.connected.OilSettingResponse;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.fragments.connected.models.Vehicle;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;
import vn.co.honda.hondacrm.utils.StateChangeOilPre;

import static vn.co.honda.hondacrm.utils.Constants.KM_CHANGE_OIL;
import static vn.co.honda.hondacrm.utils.Constants.NUM_DAY_NOTIFICATION;
import static vn.co.honda.hondacrm.utils.Constants.NUM_IN_DAY_NOTIFICATION;

public class FuelStatusActivity extends BaseActivity {
    private IndicatorSeekBar idcSeekbar;
    private TextView tvFuelStart, tvFuelEnd;
    private TextView tvNameVehicle, tvVin, tvLicensePlates;
    private TextView tvChangeOil, tvNumDay, tvNumInDay;
    private Button btnReCount;
    private RelativeLayout rlSetting;
    private Vehicle mVehicle;

    private int fuelStart = 0, distance = 0, changeOil = 0, numDay = 0, numInDay = 0;

    //dialog
    private EditText edKmChange, edNumDay, edNumIndDay;

    ApiService apiService;
    String mTypeFullAccessToken;
    private String vin = "VIN12345678910cb1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_status);
        ButterKnife.bind(this);
        setTitleHeader(getString(R.string.lb_fuel_status));
        mVehicle = (Vehicle) getIntent().getSerializableExtra(Constants.VEHICLE_OIL);
        vin = mVehicle.getVIN();
        distance = getIntent().getIntExtra(Constants.DISTANCE_OIL, 0);
        apiService = ApiClient.getClient(FuelStatusActivity.this.getApplicationContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(FuelStatusActivity.this.getApplicationContext());
        initView();
        idcSeekbar.setOnTouchListener((v, event) -> true);
        getCurrentSetting();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentSetting();
    }

    private void setUpdataSeekbar() {
        idcSeekbar.setMin(fuelStart);
//        fakeDistance();
        if (distance > (fuelStart + changeOil)) {
            idcSeekbar.setProgress(fuelStart + changeOil);
        } else {
            idcSeekbar.setProgress(distance);
        }

        idcSeekbar.setMax(fuelStart + changeOil);
        if (idcSeekbar.getProgress() >= idcSeekbar.getMax()) {
            idcSeekbar.setIndicatorTextFormat("Đã đến thời hạn thay dầu");
            idcSeekbar.setmIndicatorTopContentView(2);
        } else {
            idcSeekbar.setIndicatorTextFormat("Số km còn lại đến lần\n thay dầu tiếp theo:" + (int) (idcSeekbar.getMax() - idcSeekbar.getProgress()));
            idcSeekbar.setmIndicatorTopContentView(1);
        }

        idcSeekbar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                if (seekParams.progressFloat >= idcSeekbar.getMax()) {
                    idcSeekbar.setIndicatorTextFormat("Đã đến thời hạn thay dầu");
                    idcSeekbar.setmIndicatorTopContentView(2);
                } else {
                    idcSeekbar.setIndicatorTextFormat("Số km còn lại đến lần\n thay dầu tiếp theo:" + (int) (idcSeekbar.getMax() - seekParams.progressFloat));
                    idcSeekbar.setmIndicatorTopContentView(1);
                }
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });

    }

    private void initDataTextview() {
        tvNameVehicle.setText(mVehicle.getName());
        tvVin.setText(mVehicle.getVIN());
        tvLicensePlates.setText(mVehicle.getLicense_plate());
        tvFuelStart.setText(fuelStart + " km");
        tvFuelEnd.setText((fuelStart + changeOil) + " km");
        tvChangeOil.setText(changeOil + "km");
        tvNumDay.setText(numDay + " ngày");
        tvNumInDay.setText(numInDay + " lần");
    }

    private void initView() {
        idcSeekbar = findViewById(R.id.idc_seekbar);
        tvVin = findViewById(R.id.tv_vin);
        tvNameVehicle = findViewById(R.id.tv_name_vehicle);
        tvChangeOil = findViewById(R.id.tv_change_oil);
        tvNumDay = findViewById(R.id.tv_num_day);
        tvNumInDay = findViewById(R.id.tv_num_in_day);
        tvLicensePlates = findViewById(R.id.tv_license_plates);
        btnReCount = findViewById(R.id.btn_recount);
        rlSetting = findViewById(R.id.rl_setting);
        tvFuelStart = findViewById(R.id.tv_fuel_start);
        tvFuelEnd = findViewById(R.id.tv_fuel_end);

        rlSetting.setOnClickListener(lsClick);
        btnReCount.setOnClickListener(lsClick);
    }


    private void fomartDataAPI() {
        if (changeOil == 0) {
            changeOil = KM_CHANGE_OIL;
        }
        if (numDay == 0) {
            numDay = NUM_DAY_NOTIFICATION;
        }
        if (numInDay == 0) {
            numInDay = NUM_IN_DAY_NOTIFICATION;
        }
    }

    private DialogUtils.DialogListener dialogListener = new DialogUtils.DialogListener() {
        @Override
        public void okButtonClick(Dialog dialog) {
            if (!TextUtils.equals(edKmChange.getText(), Constants.EMPTY)) {
                changeOil = Integer.parseInt(edKmChange.getText().toString());
            }

            if (!TextUtils.equals(edNumDay.getText(), Constants.EMPTY)) {
                numDay = Integer.parseInt(edNumDay.getText().toString());
            }

            if (!TextUtils.equals(edKmChange.getText(), Constants.EMPTY)) {
                numInDay = Integer.parseInt(edNumIndDay.getText().toString());
            }


            setUpdataSeekbar();
            initDataTextview();
            configConsumptionSetting(vin, String.valueOf(numDay), String.valueOf(numInDay), String.valueOf(changeOil));
            dialog.dismiss();
        }

        @Override
        public void cancelButtonClick() {

        }
    };

    private View.OnClickListener lsClick = v -> {
        switch (v.getId()) {
            case R.id.rl_setting:
                Dialog dialog = DialogUtils.showDialogDefaultNew(this, R.layout.dialog_change_oil, dialogListener);
                edKmChange = dialog.findViewById(R.id.edt_km_change);
                edNumDay = dialog.findViewById(R.id.edt_num_day);
                edNumIndDay = dialog.findViewById(R.id.edt_num_in_day);

                edKmChange.setText(changeOil + "");
                edNumDay.setText(numDay + "");
                edNumIndDay.setText(numInDay + "");

                break;
            case R.id.btn_recount:
                fuelStart = distance;
                setOilChange(String.valueOf(distance), vin);
                initDataTextview();
                setUpdataSeekbar();
                StateChangeOilPre.getInstance(this).saveCountChangeOil(mVehicle.getVIN(), -1);
                Intent i = new Intent();
                i.setAction("UPDATECHNAGEOIL");
                i.putExtra("VINCHANGEOIL", vin);
                i.putExtra("ISCHANGEOIL", false);
                sendBroadcast(i);
                break;
        }
    };


    private void setOilChange(String distance, String vin) {
        showProgressDialog();
        apiService.setOilChange(
                mTypeFullAccessToken,
                distance,
                vin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Response>() {
                    @Override
                    public void onSuccess(Response response) {
                        Toast.makeText(FuelStatusActivity.this, "onSuccess", Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(FuelStatusActivity.this, "onError - " + e.getMessage(), Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }
                });
    }


    private void getCurrentSetting() {
//        showProgressDialog();
        apiService.getConsumptionSetting(
                mTypeFullAccessToken,
                vin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<OilSettingResponse>() {
                    @Override
                    public void onSuccess(OilSettingResponse response) {
//                        hideProgressDialog();
                        if (String.valueOf(response.getMessage()).equals("VehicleInformation not owned")) {
                            Toast.makeText(FuelStatusActivity.this, "VehicleInformation not owned.", Toast.LENGTH_LONG).show();
                        } else {
                            if (String.valueOf(response.getMessage()).equals("Không tìm thấy thông tin.")) {
//                                Toast.makeText(FuelStatusActivity.this, "Không tìm thấy thông tin.", Toast.LENGTH_LONG).show();
                            } else {
                                if (response.getOilSetting() != null) {
                                    if (response.getOilSetting().getLastDistance() != null) {
                                        fuelStart = Integer.parseInt(response.getOilSetting().getLastDistance());
                                    }
                                    if (response.getOilSetting().getDateReminder() != null) {
                                        numDay = Integer.parseInt(response.getOilSetting().getDateReminder());
                                    }
                                    if (response.getOilSetting().getReminderCount() != null) {
                                        numInDay = Integer.parseInt(response.getOilSetting().getReminderCount());
                                    }
                                    if (response.getOilSetting().getDistanceOfMaintenance() != null) {
                                        changeOil = Integer.parseInt(response.getOilSetting().getDistanceOfMaintenance());
                                    }
                                }


                            }
                            fomartDataAPI();
//                            fakeDistance();
                            setUpdataSeekbar();
                            initDataTextview();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("hoaiii", e.getMessage());
//                        hideProgressDialog();
                    }
                });
    }

    private void configConsumptionSetting(String vin, String numDay, String numInDay, String changeOil) {
        Log.e("hoaii", "vin = " + vin + " numday = " + numDay + "numinday = " + numInDay + "changeoil" + changeOil);
        showProgressDialog();
        apiService.configConsumptionSetting(
                mTypeFullAccessToken,
                vin, changeOil, numDay, numInDay, "", vin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<OilSettingResponse>() {
                    @Override
                    public void onSuccess(OilSettingResponse response) {
                        Toast.makeText(FuelStatusActivity.this, "onSuccess", Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("hoaiii", "onError ---" + e.getMessage());
                        hideProgressDialog();
                    }
                });
    }

    private void hideProgressDialog() {
        DialogUtils.hideDialogLoadProgress();
    }

    private void showProgressDialog() {
        DialogUtils.showDialogLoadProgress(FuelStatusActivity.this);
    }

}