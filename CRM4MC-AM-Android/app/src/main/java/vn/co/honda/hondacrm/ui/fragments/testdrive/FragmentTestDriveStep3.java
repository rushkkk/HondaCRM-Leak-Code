package vn.co.honda.hondacrm.ui.fragments.testdrive;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.model.base.User;
import vn.co.honda.hondacrm.net.model.user.UserProfile;
import vn.co.honda.hondacrm.ui.activities.testdrive.INumberStepCallBack;
import vn.co.honda.hondacrm.ui.activities.testdrive.TestDriveActivity;
import vn.co.honda.hondacrm.ui.fragments.testdrive.models.TestDrive;


public class FragmentTestDriveStep3 extends Fragment {
    private INumberStepCallBack mNumberStepCallBack;
    private ImageView btnNextStep5,btnBackStep5;
    private UserProfile user;
    private TestDrive testDrive;
    private Spinner edit_gender_test;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_drive_step3, container, false);
        user= new UserProfile();
        testDrive = new TestDrive();
        initComponent(view);
        customSpinner();
        btnNextStep5.setOnClickListener(v -> {
            if (mNumberStepCallBack != null) {
                testDrive.setUser(user);
                mNumberStepCallBack.setStepIndicator(4);
                Bundle bundle = new Bundle();
                bundle.putParcelable("testDrive", testDrive);
                Fragment toFragment = new FragmentTestDriveStep4();
                toFragment.setArguments(bundle);
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.frame_test_drive, toFragment).commit();
                }
            }
        });
        btnBackStep5.setOnClickListener(v -> {
            mNumberStepCallBack.setStepIndicator(2);
            Bundle bundle = new Bundle();
            bundle.putParcelable("testDrive", testDrive);
            Fragment toFragment = new FragmentTestDriveStep2();
            toFragment.setArguments(bundle);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction().replace(R.id.frame_test_drive, toFragment).commit();
            }
        });
        return view;
    }
    public void initComponent(View view){
        if(getArguments()!=null){
          testDrive=getArguments().getParcelable("testDrive");
      }
        btnBackStep5=view.findViewById(R.id.btnBackStep5);
        edit_gender_test= view.findViewById(R.id.edit_gender_test);
        btnNextStep5=view.findViewById(R.id.btnNextStep5);
    }
    public void customSpinner(){
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.Male));
        list.add(getString(R.string.Female));
        list.add(getString(R.string.Other));
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.spiner_item, list);
        adapter.setDropDownViewResource(R.layout.check_spiner_gener);
        edit_gender_test.setAdapter(adapter);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TestDriveActivity) {
            this.mNumberStepCallBack = (TestDriveActivity) context;
        }

    }

}
