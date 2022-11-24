package vn.co.honda.hondacrm.ui.fragments.dealers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import vn.co.honda.hondacrm.R;


public class FragmentVehicle extends Fragment {


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_vehicle_information, container, false);

        return view;
    }
}