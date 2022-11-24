package vn.co.honda.hondacrm.ui.fragments.vehicles;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.utils.DialogUtils;

public class FragmentAddVehicleNFC extends Fragment {

    private ImageView imgAddVINTutorial;
    private TextView txtOpenNFC;
    private EditText editVINNumberNFC;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_vehicle_nfc, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgAddVINTutorial = view.findViewById(R.id.ic_add_vin_tutorial);
        editVINNumberNFC = view.findViewById(R.id.edit_vin_number_nfc);
        imgAddVINTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.showDialogTutorial(getContext(), R.layout.dialog_get_vin_nfc, 0.9f, 0.7f, new DialogUtils.DialogListener() {
                    @Override
                    public void okButtonClick(Dialog dialog) {

                    }

                    @Override
                    public void cancelButtonClick() {

                    }
                });
            }
        });
        txtOpenNFC = view.findViewById(R.id.txt_open_nfc);
        txtOpenNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editVINNumberNFC.setText("VIN123456789101112");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
