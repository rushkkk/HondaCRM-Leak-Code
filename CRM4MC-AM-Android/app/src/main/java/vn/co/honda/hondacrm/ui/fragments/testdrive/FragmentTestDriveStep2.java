package vn.co.honda.hondacrm.ui.fragments.testdrive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.model.vehicle.Vehicle;
import vn.co.honda.hondacrm.ui.activities.testdrive.INumberStepCallBack;
import vn.co.honda.hondacrm.ui.activities.testdrive.TestDriveActivity;
import vn.co.honda.hondacrm.ui.fragments.testdrive.adapter.SpinnerVehicleAdapter;
import vn.co.honda.hondacrm.ui.fragments.testdrive.models.TestDrive;


public class FragmentTestDriveStep2 extends Fragment {
    private ImageView btnNextStep5,img_vehicle_test,btnBackStep5;
    private INumberStepCallBack mNumberStepCallBack;
    private Context mContext;
    private TestDrive testDrive ;
    private Spinner spn_selecte_type_service;
    private SpinnerVehicleAdapter spinnerVehicleAdapter;
    private Vehicle vehicleSelected;
    private List<Vehicle> listVehicle;
    private TextView txt_name_vehicle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_drive_step2, container, false);
        vehicleSelected= new Vehicle();
        listVehicle= new ArrayList<>();
        testDrive = new TestDrive();
        fakeListVehicle();
        initComponent(view);
        cusTomSpinner();
        btnNextStep5.setOnClickListener(v -> {
            if (mNumberStepCallBack != null) {
                testDrive.setVehicle(vehicleSelected);
                mNumberStepCallBack.setStepIndicator(3);
                Bundle bundle = new Bundle();
                bundle.putParcelable("testDrive", testDrive);
                Fragment toFragment = new FragmentTestDriveStep3();
                toFragment.setArguments(bundle);
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.frame_test_drive, toFragment).commit();
                }
            }
        });
        btnBackStep5.setOnClickListener(v -> {
            mNumberStepCallBack.setStepIndicator(1);
            Bundle bundle = new Bundle();
            bundle.putParcelable("testDrive", testDrive);
            Fragment toFragment = new FragmentTestDriveStep1();
            toFragment.setArguments(bundle);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().replace(R.id.frame_test_drive, toFragment).commit();
            }
        });
        txt_name_vehicle.setText(listVehicle.get(0).getVehicleName());
        spn_selecte_type_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Vehicle vehicle=spinnerVehicleAdapter.getItem(position);
                if(vehicle.getId()==1){
                    img_vehicle_test.setImageResource(R.drawable.img_test_oto);
                    vehicleSelected=vehicle;
                }
                if(vehicle.getId()==2){
                    img_vehicle_test.setImageResource(R.drawable.img_event_detail);
                    vehicleSelected=vehicle;
                }
                txt_name_vehicle.setText(vehicle.getVehicleName());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
    public void initComponent(View view){
        vehicleSelected= listVehicle.get(0);
        if(getArguments()!=null){
            testDrive=getArguments().getParcelable("testDrive");
        }
        btnBackStep5=view.findViewById(R.id.btnBackStep5);
        txt_name_vehicle=view.findViewById(R.id.txt_name_vehicle);
        img_vehicle_test=view.findViewById(R.id.img_vehicle_test);
        spn_selecte_type_service=view.findViewById(R.id.spn_selecte_type_service);
        btnNextStep5=view.findViewById(R.id.btnNextStep5);
    }
    public void cusTomSpinner(){
        spinnerVehicleAdapter= new SpinnerVehicleAdapter(getContext(),listVehicle);
        spn_selecte_type_service.setAdapter(spinnerVehicleAdapter);
        spinnerVehicleAdapter.notifyDataSetChanged();

    }
    public void fakeListVehicle(){
        Vehicle vehicle1= new Vehicle();
        vehicle1.setId(1);
        vehicle1.setVehicleName("Honda CIVIC");
        Vehicle vehicle2= new Vehicle();
        vehicle2.setId(2);
        vehicle2.setVehicleName("Honda X 150");
        listVehicle.add(vehicle1);
        listVehicle.add(vehicle2);
        listVehicle.add(vehicle1);
        listVehicle.add(vehicle2);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof TestDriveActivity) {
            this.mNumberStepCallBack = (TestDriveActivity) context;
        }
    }


}
