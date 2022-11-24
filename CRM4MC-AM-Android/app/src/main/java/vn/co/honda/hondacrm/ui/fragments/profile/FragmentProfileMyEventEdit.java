package vn.co.honda.hondacrm.ui.fragments.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.navigator.bus.GlobalBus;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.user.Event;
import vn.co.honda.hondacrm.net.model.user.Follow;
import vn.co.honda.hondacrm.net.model.user.MyEventResponse;
import vn.co.honda.hondacrm.net.model.user.MyFollowResponse;
import vn.co.honda.hondacrm.ui.fragments.profile.models.Events;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;


public class FragmentProfileMyEventEdit extends Fragment implements EventEditAdapterRecicle.ClickCheckbox, SwipeRefreshLayout.OnRefreshListener, EventEditAdapterRecicle.ClickCheckboxAllEvent {
    List<Event> listEvent;
    List<Event> listEvent2;
    ListView listView;
    EventEditAdapterRecicle mEventAdapterRecicle;
    RecyclerView recyclerView;
    View viewContainerViewEdit;
    CheckBox checkbox_follow;
    List<Integer> listRemove;
    View llDelete, btn_delete_all;
    SwipeRefreshLayout mSwipeRefreshLayout;
    changeCountItemEvent changeCountItemEvent;
    Boolean checkBoxAll = false;
    //api
    ApiService apiService;
    String mTypeFullAccessToken;
    //page load
    private int sCurrentPageEvent = 1;
    public ProgressBar progressBar;
    private int visibleItemCount;
    private int pastVisibleItems;
    int lastVisibleItem, totalItemCount;
    boolean isLoading;
    private  int totalProductEvent;

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
        listEvent = new ArrayList<>();
        listEvent2 = new ArrayList<>();
        listRemove= new ArrayList<>();
        requestListMyEvent(4, 1);
        // Inflate the layout for this fragment
        mEventAdapterRecicle = new EventEditAdapterRecicle(getActivity(), R.layout.item_profile_event_edit, listEvent, this, this);
        View view = inflater.inflate(R.layout.fragment_profile_my_event_edit, container, false);
        checkbox_follow = (CheckBox) view.findViewById(R.id.checkbox_select_all);
        progressBar = view.findViewById(R.id.progressBar);
        btn_delete_all = view.findViewById(R.id.btn_delete_all);
        mEventAdapterRecicle.notifyDataSetChanged();
        // mEventAdapterRecicle.notifyDataSetChanged();
        //select all
        checkbox_follow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                for (Event event : listEvent) {
                    event.setChose(true);
                }
                mEventAdapterRecicle.notifyDataSetChanged();
            }
            if (!isChecked) {
                try{
                    for (Event event : listEvent) {
                        event.setChose(false);
                    }
                    mEventAdapterRecicle.setChoseAllEvent(true);
                    mEventAdapterRecicle.notifyDataSetChanged();
                }catch (Exception e){
                    Log.d("", "onCreateView: loi"+e.getMessage());
                }
            }
        });
        btn_delete_all.setOnClickListener(v -> showDialogCormfirm());

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
            passDataCount(listEvent.size());
            mSwipeRefreshLayout.setRefreshing(false);
            mEventAdapterRecicle.notifyDataSetChanged();
            // Fetching data from server
        });
        //LOAD MORE
        initScrollListener();
        return view;
    }

    //request list my Event from api
    private void requestListMyEvent(Integer size, Integer page) {
        apiService.getListMyEvent(
                mTypeFullAccessToken,
                size,
                page
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<MyEventResponse>() {
                    @Override
                    public void onSuccess(MyEventResponse myEventResponse) {
                        if (myEventResponse != null && myEventResponse.getData() != null
                                && myEventResponse.getData().getItems() != null && myEventResponse.getData().getTotal()!=null) {
                            if (!listEvent.isEmpty()) {
                                listEvent.clear();
                            }
                            listEvent.addAll(myEventResponse.getData().getItems());
                            passDataCount(myEventResponse.getData().getTotal());
                            totalProductEvent = myEventResponse.getData().getTotal();
                            mEventAdapterRecicle.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));
                    }
                });
    }

    public void showDialogCormfirm(){
        DialogUtils.showDialogLogout(getContext(), R.layout.diaglog_confirm_delete, 0.9f, 0.3f, new DialogUtils.DialogListener() {
            @Override
            public void okButtonClick(Dialog dialog) {
                if (checkbox_follow.isChecked()) {
                    for (Event event:listEvent) {
                        ids.add(event.getId());
                    }
                    requestUnFollow(ids);
                    passDataCount(totalProductEvent-ids.size());
                    listEvent.clear();
                } else {
                    listRemove.size();
                    listEvent2.clear();
                    for (Event event : listEvent) {
                        listEvent2.add(event);
                    }
                    if (listRemove.size() >= 0) {
                        for (Event event : listEvent2) {
                            for (Integer id : listRemove) {
                                if (event.getId() == id) {
                                    listEvent.remove(event);
                                    totalProductEvent=totalProductEvent-1;
                                }
                            }
                        }

                    }
                    requestUnFollow(listRemove);
                    passDataCount(totalProductEvent);
                }

                mEventAdapterRecicle.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void cancelButtonClick() {
            }
        }, "Do you want to delete ?");
    }

    //handle on load more
    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    if (!isLoading && !mSwipeRefreshLayout.isRefreshing()) {
                        if (totalItemCount >= 1 && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            isLoading = true;
                            loadMore();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


    }

    private void refreshDataLoadMore(Integer size, Integer page) {
        apiService.getListMyEvent(
                mTypeFullAccessToken,
                size,
                page
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<MyEventResponse>() {
                    @Override
                    public void onSuccess(MyEventResponse myEventResponse) {
                        Log.d("kkkkttt111", "Success: " + myEventResponse.getData().getItems().size());
                        listEvent.addAll(myEventResponse.getData().getItems());
                        passDataCount(myEventResponse.getData().getTotal());
                        totalProductEvent=myEventResponse.getData().getTotal();
                        mEventAdapterRecicle.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        isLoading=false;
                    }

                    @Override
                    public void onError(Throwable error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        isLoading=false;
                        mSwipeRefreshLayout.setRefreshing(false);
                        Log.d("kkkk11", "onError: " + error.getMessage());
                        DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));
                    }
                });
    }
    private  void requestUnFollow( List<Integer> listId){
        apiService.updateMyEvent(
                mTypeFullAccessToken,
                listId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<MyFollowResponse>() {
                    @Override
                    public void onSuccess(MyFollowResponse myFollowResponse) {
                        if(ids!=null && !ids.isEmpty()){
                            ids.clear();
                        }

                    }
                    @Override
                    public void onError(Throwable error) {
                        DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));

                    }
                });
    }

    private void loadMore() {
        progressBar.setVisibility(View.VISIBLE);
        sCurrentPageEvent++;
        refreshDataLoadMore(4, sCurrentPageEvent);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        changeCountItemEvent = (changeCountItemEvent) context;
        GlobalBus.getBus().register(this);
    }

    @Subscribe
    public void getMessage(vn.co.honda.hondacrm.navigator.bus.Events.ActivityFragmentMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equals("2")) {
            if (activityFragmentMessage.getType() == 0) {
                viewContainerViewEdit.setVisibility(View.VISIBLE);
                mEventAdapterRecicle.setEdit(true);
                //send id object
            } else {
                //  viewContainerSelected.setVisibility(View.INVISIBLE);
                viewContainerViewEdit.setVisibility(View.GONE);
                mEventAdapterRecicle.setEdit(false);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public void clickOnItem(ArrayList<Integer> listRemove) {

        this.listRemove = listRemove;

    }

    @Override
    public void onRefresh() {

        mSwipeRefreshLayout.setRefreshing(true);
        sCurrentPageEvent=1;
        requestListMyEvent(4, sCurrentPageEvent);
        mEventAdapterRecicle.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //update count item
    public void passDataCount(int count) {
        changeCountItemEvent.sendCountEvent(count);
    }

    @Override
    public void clickOnItemCheck(Boolean aBoolean) {
        checkBoxAll = aBoolean;
        if (!aBoolean) {
            checkbox_follow.setChecked(false);
        }
    }

    public interface changeCountItemEvent {
        void sendCountEvent(int count);
    }
}
