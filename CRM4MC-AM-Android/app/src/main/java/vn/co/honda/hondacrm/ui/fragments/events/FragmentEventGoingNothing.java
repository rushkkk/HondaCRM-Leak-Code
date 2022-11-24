package vn.co.honda.hondacrm.ui.fragments.events;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.fragments.events.model.EventAdapterRecicle;
import vn.co.honda.hondacrm.ui.fragments.events.model.Events;


public class FragmentEventGoingNothing extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.event_nothing, container, false);
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
