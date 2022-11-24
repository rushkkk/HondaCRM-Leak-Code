package vn.co.honda.hondacrm.ui.fragments.testdrive;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.testdrive.INumberStepCallBack;
import vn.co.honda.hondacrm.ui.activities.testdrive.TestDriveActivity;
import vn.co.honda.hondacrm.ui.fragments.testdrive.models.TestDrive;


public class FragmentTestDriveStep4 extends Fragment {
    private INumberStepCallBack mNumberStepCallBack;
    TextView btn_ok;
    private ImageView btnBackStep5;
    private TestDrive testDrive = new TestDrive();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_drive_step4, container, false);
        initComponent(view);
        btn_ok.setOnClickListener(v -> {
            if (mNumberStepCallBack != null) {
                //request api test drive
                mNumberStepCallBack.setStepIndicator(5);
                Fragment toFragment = new FragmentTestDriveStep42();
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.frame_test_drive, toFragment).commit();
                }
            }
        });
        btnBackStep5.setOnClickListener(v -> {
            mNumberStepCallBack.setStepIndicator(3);
            Bundle bundle = new Bundle();
            bundle.putParcelable("testDrive", testDrive);
            Fragment toFragment = new FragmentTestDriveStep3();
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
        btn_ok=view.findViewById(R.id.btn_ok);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TestDriveActivity) {
            this.mNumberStepCallBack = (TestDriveActivity) context;
        }

    }

}
