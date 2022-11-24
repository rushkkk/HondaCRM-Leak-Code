package vn.co.honda.hondacrm.ui.activities.vehicles;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;

import vn.co.honda.hondacrm.net.model.vehicle.Item;
import vn.co.honda.hondacrm.net.model.vehicle.ServiceHistoryFullResponse;

import vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation.VehicleInfo;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.adapters.vehicles.IItemListener;
import vn.co.honda.hondacrm.ui.adapters.vehicles.ServiceFullHistoryAdapter;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

public class ServiceHistoryFullActivity extends BaseActivity {
    RecyclerView recyclerView;
    TextView tv_error, tv_vehicleName;
    ArrayList<Item> serviceItemsArrayList;
    ServiceFullHistoryAdapter serviceHistoryAdapter;
    ApiService apiService;
    String mTypeFullAccessToken;
    private String vin = "";
    boolean isLoading;
    public ProgressBar progressBar;
    private static int sCurrentPage = 1;
    int totalItemCount;
    private int visibleItemCount;
    private int pastVisibleItems;
    VehicleInfo vehicleInfo;
    String mVehicelName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_history_full);
        initViews();
    }

    private void initViews() {
        if (getIntent() != null) {
            mVehicelName = getIntent().getStringExtra("vehicle_name");
            if (getIntent().getStringExtra("vin") != null) {
                vin = getIntent().getStringExtra("vin");
            }
        }

        recyclerView = findViewById(R.id.rv_serviceHistory);
        tv_error = findViewById(R.id.tv_error);
        tv_vehicleName = findViewById(R.id.tv_titleVehicle);

        tv_vehicleName.setText(mVehicelName);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(ServiceHistoryFullActivity.this);

        apiService = ApiClient.getClient(ServiceHistoryFullActivity.this).create(ApiService.class);

        serviceItemsArrayList = new ArrayList<>();
//        recycleList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        serviceHistoryAdapter = new ServiceFullHistoryAdapter(this, serviceItemsArrayList);
        recyclerView.setAdapter(serviceHistoryAdapter);
        serviceHistoryAdapter.setListener(mIItemListener);
        requestServiceHistoryAPI(vin, sCurrentPage);

//        initScrollListener();
    }

    /**
     * Handle item click history list
     */
    private final IItemListener mIItemListener = new IItemListener() {
        @Override
        public void onItemClick(Integer id) {
            Intent intent = new Intent(ServiceHistoryFullActivity.this, DetailCarServiceHistoryActivity.class);
            intent.putExtra("vehicle_name", mVehicelName);
            intent.putExtra("id_service_history", id);
            startActivity(intent);
        }
    };

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
                    if (!isLoading) {
                        if (totalItemCount >= Constants.MAX_ITEMS && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            isLoading = true;
//                            loadMore();
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


    private void requestServiceHistoryAPI(String vin, int sCurrentPage) {
        DialogUtils.showDialogLoadProgress(this);
        apiService.getFullServiceHistory(mTypeFullAccessToken, vin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ServiceHistoryFullResponse>() {
                    @Override
                    public void onSuccess(ServiceHistoryFullResponse serviceHistoryFullResponse) {
                        DialogUtils.hideDialogLoadProgress();
                        if (serviceHistoryFullResponse != null && serviceHistoryFullResponse.getData().getItems() != null && serviceHistoryFullResponse.getData().getItems().size() > 0) {
                            serviceItemsArrayList.addAll(serviceHistoryFullResponse.getData().getItems());
                            recyclerView.setVisibility(View.VISIBLE);
                            tv_error.setVisibility(View.GONE);
                            serviceHistoryAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        DialogUtils.hideDialogLoadProgress();
                        recyclerView.setVisibility(View.GONE);
                        tv_error.setVisibility(View.VISIBLE);
                    }
                });
    }


//    private void refreshDataLoadMore(String VIN, Integer page) {
//        apiService
//                .getServiceHistory(mTypeFullAccessToken,
//                        VIN)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleSubscriber<ServiceHistoryResponse>() {
//                    @Override
//                    public void onSuccess(ServiceHistoryResponse serviceHistoryResponse) {
//                        serviceItemsArrayList.clear();
//                        if (serviceHistoryResponse.getData() != null) {
//                            serviceItemsArrayList.addAll(serviceHistoryResponse.getData());
//                            Log.d("AAAAAA", serviceHistoryResponse.getData().toString());
//                            serviceHistoryAdapter.notifyDataSetChanged();
//                            isLoading = false;
//                            progressBar.setVisibility(View.INVISIBLE);
//                        }
//                        DialogUtils.hideDialogLoadProgress();
//                    }
//
//                    @Override
//                    public void onError(Throwable error) {
//                        DialogUtils.hideDialogLoadProgress();
//                        recyclerView.setVisibility(View.GONE);
//                        tv_error.setVisibility(View.VISIBLE);
//                        Log.d("aaa", "error");
//                    }
//                });
//    }

    private void loadMore() {
        progressBar.setVisibility(View.VISIBLE);
        sCurrentPage++;
//        refreshDataLoadMore(vin, sCurrentPage);
    }

//    private void recycleList() {
//        serviceItemsArrayList.add(new ServiceItems(0, "123 Ba Dinh", "2.150.000", 96.969, "30-Apr-2019"));
//        serviceItemsArrayList.add(new ServiceItems(0, "123 Ba Dinh", "840.000", 96.969, "30-March-2019"));
//        serviceItemsArrayList.add(new ServiceItems(0, "123 Ba Dinh", "150.000", 96.969, "20-Feb-2019"));
//        serviceItemsArrayList.add(new ServiceItems(0, "123 Ba Dinh", "840.000", 96.969, "30-Dec-2019"));
//        serviceItemsArrayList.add(new ServiceItems(0, "123 Ba Dinh", "150.000", 96.969, "25-Sep-2019"));
//        serviceItemsArrayList.add(new ServiceItems(0, "123 Ba Dinh", "840.000", 96.969, "20-Jun-2019"));
//    }
}
