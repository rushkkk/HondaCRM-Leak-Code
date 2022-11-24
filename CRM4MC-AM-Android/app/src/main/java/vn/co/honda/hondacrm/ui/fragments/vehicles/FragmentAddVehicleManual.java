package vn.co.honda.hondacrm.ui.fragments.vehicles;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zing.zalo.devicetrackingsdk.Constant;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.vehicles.ActivityVertifyInformation;
import vn.co.honda.hondacrm.ui.activities.vehicles.AddVehiclesActivity;
import vn.co.honda.hondacrm.ui.activities.vehicles.models.Vehicle;
import vn.co.honda.hondacrm.utils.DialogUtils;

public class FragmentAddVehicleManual extends Fragment {
    private TextView txtSubmitVIN;
    private TextView txtErrorAddManual;
    private EditText editVinManual;
    private ImageView imgAddVINTutorial;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_vehicle_manual, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtSubmitVIN = view.findViewById(R.id.txt_submit_vin_manual);
        txtErrorAddManual = view.findViewById(R.id.txt_error_add_manual);
        editVinManual = view.findViewById(R.id.edit_vin_manual);
        txtSubmitVIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String VINNumber = String.valueOf(editVinManual.getText());
                if (!VINNumber.contains("vin")) {
                    txtErrorAddManual.setText(R.string.VIN_incorrect);
                    txtErrorAddManual.setVisibility(View.VISIBLE);
                } else {
                    DialogUtils.showDialogConfirm(view.getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, new DialogUtils.DialogListener() {
                        @Override
                        public void okButtonClick(Dialog dialog) {
                            final Intent data = new Intent();
                            Vehicle vehicle = new Vehicle();
                            vehicle.setName("Tật bôn");
                            vehicle.setConnected(true);
                            vehicle.setHaveConnectFunc(true);
                           // vehicle.setImgVehicle(R.drawable.motocyler);
                            vehicle.setLicense_plate("74L1 - 019.18");
                            vehicle.setModel("SH 300cc 2019");
                            vehicle.setWarning(true);
                            vehicle.setVIN("ABCDED");
                            vehicle.setNextPMDate("20/09/2019");

                            // Truyền data vào intent
                            data.putExtra("vehicle", vehicle);

                            // thể hiện đã thành công và có chứa kết quả trả về
                            getActivity().setResult(Activity.RESULT_OK, data);
                            getActivity().finish();
                        }

                        @Override
                        public void cancelButtonClick() {

                        }
                    });
                }
            }
        });
        imgAddVINTutorial = view.findViewById(R.id.ic_add_vin_tutorial);
        imgAddVINTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.showDialogTutorial(getContext(), R.layout.dialog_get_vin, 0.9f, 0.7f, new
                        DialogUtils.DialogListener() {
                            @Override
                            public void okButtonClick(Dialog dialog) {

                            }

                            @Override
                            public void cancelButtonClick() {

                            }
                        });
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
