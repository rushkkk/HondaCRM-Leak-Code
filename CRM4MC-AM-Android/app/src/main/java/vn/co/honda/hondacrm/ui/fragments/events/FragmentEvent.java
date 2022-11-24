package vn.co.honda.hondacrm.ui.fragments.events;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.user.Event;
import vn.co.honda.hondacrm.net.model.user.MyEventResponse;
import vn.co.honda.hondacrm.ui.fragments.events.model.EventAdapterRecicle;
import vn.co.honda.hondacrm.ui.fragments.events.model.Events;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;


public class FragmentEvent extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    ArrayList<Events> listEvent = new ArrayList<>();
    EventAdapterRecicle mEventAdapterRecicle;
    RecyclerView recyclerView;
    View viewContainerViewEdit;
    SwipeRefreshLayout mSwipeRefreshLayout;
    //api
    ApiService apiService;
    String mTypeFullAccessToken;
    //page load
    private int sCurrentPageEvent = 1;
    public ProgressBar progressBar;

    List<Integer> ids;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get api
        apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(getContext());
        ids= new ArrayList<>();
        // Inflate the layout for this fragment
        mEventAdapterRecicle = new EventAdapterRecicle(getActivity(), R.layout.item_event, listEvent);
        View view = inflater.inflate(R.layout.fragment_profile_my_event_edit, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        mEventAdapterRecicle.notifyDataSetChanged();
        // mEventAdapterRecicle.notifyDataSetChanged();
        recyclerView = view.findViewById(R.id.listEvent);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //get view
        viewContainerViewEdit = view.findViewById(R.id.llEditEvent);
        recyclerView.setAdapter(mEventAdapterRecicle);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
            mEventAdapterRecicle.notifyDataSetChanged();
            // Fetching data from server
        });
        //LOAD MORE
       // initScrollListener();
        return view;
    }

    //request list my Event from api



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onRefresh() {
    }

}
