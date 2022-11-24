package vn.co.honda.hondacrm.ui.fragments.profile;


import android.app.Dialog;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.navigator.bus.Events;
import vn.co.honda.hondacrm.navigator.bus.GlobalBus;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.user.Follow;
import vn.co.honda.hondacrm.net.model.user.MyFollowResponse;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;


public class FragmentProfileMyFollowEdit extends Fragment implements FollowEditAdapterRecicle.ClickCheckbox, SwipeRefreshLayout.OnRefreshListener, FollowEditAdapterRecicle.ClickCheckboxAll, FollowEditAdapterRecicle.ClickCheckboxNot {
    //implements FollowEditAdapterRecicle.ClickCheckbox
    //khai bao bien
    private Dialog dialog;
    List<Follow> listCar;
    List<Follow> listCar2 = new ArrayList<>();
    List<Integer> listRemove, listNotRemove;
    public FollowEditAdapterRecicle mFollowAdapterRecicle;
    CheckBox checkbox_follow;
    ImageButton btn_profile_delete;
    RecyclerView recyclerView;
    View btn_delete_all;
    SwipeRefreshLayout mSwipeRefreshLayout;
    View viewContainerViewEdit;
    changeCountItemFollow changeCountItemFollow;
    Boolean checkBoxAll = false;
    //page load
    private  int sCurrentPage = 1;
    public ProgressBar progressBar;
    private int visibleItemCount;
    private int pastVisibleItems;
    int lastVisibleItem, totalItemCount;
    boolean isLoading;
    private  int totalProductFollow;
    //data test
    List<Integer> ids;
    //api
    ApiService apiService;
    String mTypeFullAccessToken;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listRemove = new ArrayList<>();
        listNotRemove = new ArrayList<>();
        listCar = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(getContext());
        ids= new ArrayList<>();
        requestListMyFollow(4,1);
        // Inflate the layout for this fragment
        mFollowAdapterRecicle = new FollowEditAdapterRecicle(getActivity(), R.layout.item_car_edit, listCar, this, this, this);
        View view = inflater.inflate(R.layout.fragment_profile_my_follow_edit_loadmore, container, false);
        //get api
        mFollowAdapterRecicle.notifyDataSetChanged();
        //khai bao bien

        progressBar = view.findViewById(R.id.progressBar);
        checkbox_follow = view.findViewById(R.id.checkbox_select_all);
        btn_profile_delete = view.findViewById(R.id.btnDelete);
        btn_delete_all = view.findViewById(R.id.btn_delete_all);
        //repalace view edit
        //viewContainerSelected = view.findViewById(R.id.brand_select);
        viewContainerViewEdit = view.findViewById(R.id.llEditFollow);

        //select all
        checkbox_follow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mFollowAdapterRecicle.setChoseAllTrue(true);
                //mFollowAdapterRecicle.setChoseAll(true);
                for (Follow car : listCar) {
                    car.setChose(true);
                }

                mFollowAdapterRecicle.notifyDataSetChanged();
            }
            if (!isChecked) {

                try{
                mFollowAdapterRecicle.setChoseAll(true);
                for (Follow car : listCar) {
                    car.setChose(false);
                }
                    mFollowAdapterRecicle.notifyDataSetChanged();
                }catch (Exception e){
                    Log.d("", "onCreateView: loi"+e.getMessage());
                }

                //mFollowAdapterRecicle.setChoseAll(true);
            }
        });
        btn_delete_all.setOnClickListener(v -> showDialogCormfirm());
        recyclerView = view.findViewById(R.id.listCar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(mFollowAdapterRecicle);


        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(() -> {
            passDataCount(listCar.size());
            mSwipeRefreshLayout.setRefreshing(false);

            // Fetching data from server
        });
        //Load more
        initScrollListener();
        return view;
    }

    //request list my follow from api
    private void requestListMyFollow(Integer size,Integer page) {
        apiService.getListMyFollow(
                mTypeFullAccessToken,
                size,
                page
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<MyFollowResponse>() {
                    @Override
                    public void onSuccess(MyFollowResponse myFollowResponse) {
                        if(myFollowResponse.getData()!=null&&myFollowResponse.getData().getItems()!=null &&myFollowResponse.getData().getTotal()!=null){
                            if(listCar!=null && !listCar.isEmpty()){
                                listCar.clear();
                            }
                            listCar.addAll(myFollowResponse.getData().getItems());
                            passDataCount(myFollowResponse.getData().getTotal());
                            totalProductFollow = myFollowResponse.getData().getTotal();
                            mFollowAdapterRecicle.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onError(Throwable error) {
                        DialogUtils.showDialogConfirmLogin(getActivity(), R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));

                    }
                });
    }
        private  void requestUnFollow( List<Integer> listId){
              apiService.updateMyFollow(
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
                        DialogUtils.showDialogConfirmLogin(getActivity(), R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));

                    }
                });
    }

    public void showDialogCormfirm(){
        DialogUtils.showDialogLogout(getContext(), R.layout.diaglog_confirm_delete, 0.9f, 0.3f, new DialogUtils.DialogListener() {
            @Override
            public void okButtonClick(Dialog dialog) {
                if (checkbox_follow.isChecked()) {
                    for (Follow car:listCar) {
                        ids.add(car.getId());
                    }
                    requestUnFollow(ids);
                    passDataCount(totalProductFollow-ids.size());
                    listCar2.clear();
                    listCar.clear();
                } else {

                    listRemove.size();
                    listCar2.clear();
                    for (Follow car : listCar) {
                        listCar2.add(car);
                    }
                    if (listRemove.size() >= 0) {

                        for (Follow car : listCar2) {
                            for (Integer id : listRemove) {
                                if (car.getId() == id) {
                                    listCar.remove(car);
                                    totalProductFollow=totalProductFollow-1;
                                }
                            }
                        }
                    }

                    requestUnFollow(listRemove);
                    passDataCount(totalProductFollow);
                }

                mFollowAdapterRecicle.notifyDataSetChanged();
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
                            mSwipeRefreshLayout.setRefreshing(false);
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
    private void refreshDataLoadMore(Integer size,Integer page) {
        apiService.getListMyFollow(
                mTypeFullAccessToken,
                size,
                page
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<MyFollowResponse>() {
                    @Override
                    public void onSuccess(MyFollowResponse myFollowResponse) {
                        Log.d("success", "success1111: "+myFollowResponse.getData().getItems().size());
                        listCar.addAll(myFollowResponse.getData().getItems());
                        passDataCount(myFollowResponse.getData().getTotal());
                        totalProductFollow=myFollowResponse.getData().getTotal();
                        mFollowAdapterRecicle.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        isLoading=false;
                    }
                    @Override
                    public void onError(Throwable error) {
                        Log.d("erro", "onError: "+error.getMessage());
                        mSwipeRefreshLayout.setRefreshing(false);
                        isLoading=false;
                        progressBar.setVisibility(View.INVISIBLE);
                        DialogUtils.showDialogConfirmLogin(getActivity(), R.layout.dialog_add_vehicle_success, 0.9f, 0.2f, getString(R.string.networkError));

                    }
                });
    }
    private void loadMore() {
        progressBar.setVisibility(View.VISIBLE);
        sCurrentPage++;
        refreshDataLoadMore(4,sCurrentPage);

    }

    @Override
    public void clickOnItem(List<Integer> listRemove) {
        this.listRemove = listRemove;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        changeCountItemFollow = (changeCountItemFollow) context;
        GlobalBus.getBus().register(this);
    }

    @Subscribe
    public void getMessage(Events.ActivityFragmentMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equals("1")) {
            if (activityFragmentMessage.getType() == 0) {
                viewContainerViewEdit.setVisibility(View.VISIBLE);
                mFollowAdapterRecicle.setEdit(true);
                //send id object
            } else {
                viewContainerViewEdit.setVisibility(View.GONE);
                mFollowAdapterRecicle.setEdit(false);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        GlobalBus.getBus().unregister(this);
    }


    @Override
    public void onRefresh() {

        mSwipeRefreshLayout.setRefreshing(true);
        sCurrentPage=1;
        requestListMyFollow(4,sCurrentPage);
        mFollowAdapterRecicle.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void passDataCount(int count) {
        changeCountItemFollow.sendCountFollow(count);
    }

    @Override
    public void clickOnItemCheck(Boolean aBoolean) {
        checkBoxAll = aBoolean;
        if(!checkBoxAll){
            checkbox_follow.setChecked(false);
        }

    }

    @Override
    public void clickOnItemNot(List<Integer> listId) {
        listNotRemove = listId;

    }

    public interface changeCountItemFollow {
        void sendCountFollow(int count);
    }

}
