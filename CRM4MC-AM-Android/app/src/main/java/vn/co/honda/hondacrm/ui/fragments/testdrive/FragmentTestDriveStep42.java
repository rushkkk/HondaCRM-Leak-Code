package vn.co.honda.hondacrm.ui.fragments.testdrive;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.co.honda.hondacrm.R;


public class FragmentTestDriveStep42 extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_drive_step4_2, container, false);
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
