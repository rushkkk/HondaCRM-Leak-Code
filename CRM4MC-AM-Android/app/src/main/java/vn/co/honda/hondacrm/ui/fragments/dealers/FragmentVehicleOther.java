package vn.co.honda.hondacrm.ui.fragments.dealers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import vn.co.honda.hondacrm.R;


public class FragmentVehicleOther extends Fragment {


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_other, container, false);

        return view;
    }
}