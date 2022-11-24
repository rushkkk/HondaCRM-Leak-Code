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


public class FragmentEventGoing extends Fragment {
    ArrayList<Events> listEvent= new ArrayList<>();
    ListView listView;
    EventAdapterRecicle mEventAdapterRecicle;
    RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Events event1= new Events("Happy aniversary","The Power of Dreams  manifests itself in ...","1/5/2019","3/5/2019","Số Thái Hà, Đống Đa, Hà Nội");
        Events event2= new Events("New model released!","Before you wonder, it’s a significantly updated...","1/5/2019","3/5/2019","Số Thái Hà, Đống Đa, Hà Nội");
        Events event3= new Events("New model released!","Distinctive headlights, taillights and sporty,bar...","1/5/2019","3/5/2019","Số Thái Hà, Đống Đa, Hà Nội");
        Events event4= new Events("Happy aniversary","The Power of Dreams  manifests itself in ...","1/5/2019","3/5/2019","Số Thái Hà, Đống Đa, Hà Nội");
        listEvent.add(event1);
        listEvent.add(event2);
        listEvent.add(event3);
        listEvent.add(event4);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mEventAdapterRecicle = new EventAdapterRecicle(getActivity(), R.layout.item_event,listEvent);
        View view = inflater.inflate(R.layout.my_event, container, false);
        recyclerView = view.findViewById(R.id.listEvent);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(mEventAdapterRecicle);
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
