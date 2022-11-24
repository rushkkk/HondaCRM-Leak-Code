package vn.co.honda.hondacrm.ui.fragments.booking_service.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import vn.co.honda.hondacrm.navigator.bus.Events;
import vn.co.honda.hondacrm.navigator.bus.GlobalBus;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.vehicle.Vehicle;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleResponse;
import vn.co.honda.hondacrm.ui.activities.booking_service.BookingServiceActivity;
import vn.co.honda.hondacrm.ui.activities.booking_service.INumberStepCallBack;
import vn.co.honda.hondacrm.ui.adapters.booking_service.BookingVehicleAdapter;
import vn.co.honda.hondacrm.ui.fragments.booking_service.views.SelectDealerFragment;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

public class SelectVehicleFragment extends Fragment{

    private RecyclerView rvBookingVehicle;
    private BookingVehicleAdapter adapter;
    private List<Vehicle> bookingVehicleArrayList;
    ApiService apiService;
    public ProgressBar progressBar;
    boolean isLoading;
    SwipeRefreshLayout swipeContainer;
    int totalItemCount;
    private static int sCurrentPage = 1;
    private int visibleItemCount;
    private int pastVisibleItems;
    String mTypeFullAccessToken;
    private Context mContext;
    private INumberStepCallBack mNumberStepCallBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_vehicle, container, false);
        initViews(view);
        setAdapter();
        swipeContainer.setOnRefreshListener(() -> {
            isLoading = true;
            fetchTimelineAsync(1);
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(getContext());
        mContext = context;
        if (context instanceof BookingServiceActivity) {
            this.mNumberStepCallBack = (BookingServiceActivity) context;
        }
    }

    public void fetchTimelineAsync(int page) {
        refreshData(page);
    }

    private void initViews(View view) {
        rvBookingVehicle = view.findViewById(R.id.rvBookingVehicle);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary);
        swipeContainer.setEnabled(true);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setAdapter() {
        bookingVehicleArrayList = new ArrayList<>();
        adapter = new BookingVehicleAdapter(mContext, bookingVehicleArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext.getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvBookingVehicle.setLayoutManager(linearLayoutManager);
        rvBookingVehicle.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItem((VehicleType, vin) -> {
            if (mNumberStepCallBack != null) {
                mNumberStepCallBack.setStepIndicator(2);
                Bundle bundle = new Bundle();
                bundle.putInt("VehicleType", VehicleType);
                bundle.putString("vin", vin);
                Fragment toFragment = new SelectDealerFragment();
                toFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.frame_booking_service, toFragment).commit();
            }
        });
        requestData(1);
        initScrollListener();
    }

    private void initScrollListener() {
        rvBookingVehicle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    if (!isLoading && !swipeContainer.isRefreshing()) {
                        if (totalItemCount >= Constants.MAX_ITEMS && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
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

    private void requestData(Integer page) {
        showProgressDialog();
        apiService.getListVehicleByUser(mTypeFullAccessToken, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VehicleResponse>() {
                    @Override
                    public void onSuccess(VehicleResponse response) {
                        hideProgressDialog();
                        bookingVehicleArrayList.clear();
                        if (response.getVehicle() != null && response.getVehicle().getVehicles() != null) {
                            bookingVehicleArrayList.addAll(response.getVehicle().getVehicles());
                            adapter.notifyDataSetChanged();
                        }
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressDialog();
                        DialogUtils.showDialogConfirmLogin(getActivity(), R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, e.getMessage());
                    }
                });
    }

    private void loadMore() {
        progressBar.setVisibility(View.VISIBLE);
        sCurrentPage++;
        refreshDataLoadMore(sCurrentPage);
    }

    private void refreshData(Integer page) {
        apiService
                .getListVehicleByUser(mTypeFullAccessToken, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VehicleResponse>() {
                    @Override
                    public void onSuccess(VehicleResponse response) {
                        bookingVehicleArrayList.clear();
                        if (response.getVehicle() != null && response.getVehicle().getVehicles() != null) {
                            bookingVehicleArrayList.addAll(response.getVehicle().getVehicles());
                            adapter.notifyDataSetChanged();
                            Events.FragmentActivityMessage fragmentActivityMessage = new Events.FragmentActivityMessage(bookingVehicleArrayList.size());
                            GlobalBus.getBus().post(fragmentActivityMessage);
                        }
                        swipeContainer.setRefreshing(false);
                        isLoading = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeContainer.setRefreshing(false);
                        isLoading = false;
                        DialogUtils.showDialogConfirmLogin(getActivity(), R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, e.getMessage());
                    }
                });
    }

    private void refreshDataLoadMore(Integer page) {
        apiService
                .getListVehicleByUser(mTypeFullAccessToken, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VehicleResponse>() {
                    @Override
                    public void onSuccess(VehicleResponse response) {
                        if (response.getVehicle() != null && response.getVehicle().getVehicles() != null) {
                            bookingVehicleArrayList.addAll(response.getVehicle().getVehicles());
                            adapter.notifyDataSetChanged();
                            isLoading = false;
                            Events.FragmentActivityMessage fragmentActivityMessage = new Events.FragmentActivityMessage(bookingVehicleArrayList.size());
                            GlobalBus.getBus().post(fragmentActivityMessage);
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtils.showDialogConfirmLogin(getActivity(), R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, e.getMessage());
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void hideProgressDialog() {
        DialogUtils.hideDialogLoadProgress();
    }

    private void showProgressDialog() {
        DialogUtils.showDialogLoadProgress(getContext());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mNumberStepCallBack != null) {
            mNumberStepCallBack = null;
        }
    }
}
