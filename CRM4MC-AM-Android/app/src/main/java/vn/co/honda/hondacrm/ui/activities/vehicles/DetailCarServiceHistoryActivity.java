package vn.co.honda.hondacrm.ui.activities.vehicles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.service.Data;
import vn.co.honda.hondacrm.net.model.service.DetailServiceHistoryResponse;
import vn.co.honda.hondacrm.net.model.service.ListAccessory;
import vn.co.honda.hondacrm.net.model.service.ListJob;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.vehicles.models.AddOn;
import vn.co.honda.hondacrm.ui.adapters.vehicles.AddOnAdapter;
import vn.co.honda.hondacrm.ui.fragments.booking_service.views.SelectTimeSlotFragment;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

public class DetailCarServiceHistoryActivity extends BaseActivity {
    private RecyclerView recycler;
    private RecyclerView.LayoutManager manager;
    private AddOnAdapter adapter;
    RatingBar mRatingBar;
    Data dataDetail;
    private ArrayList<AddOn> list = new ArrayList<>();
    TextView tv_arrivalTime, tv_leaveTime, tv_ODO, tv_address, tv_maintain, btn_book_service, tv_vehicleName;
    Integer id;
    ApiService apiService;
    String mTypeFullAccessToken;
    String VIN = "";
    private String mVehicleName;
    private TextView tvRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_car_service_history);
        initViews();

        tv_vehicleName = findViewById(R.id.tv_titleVehicle);
        tvRating = findViewById(R.id.tv_rating);
        if (getIntent() != null && getIntent().getStringExtra("vehicle_name") != null) {
            VIN = getIntent().getStringExtra("vehicle_name");
            id = getIntent().getIntExtra("id_service_history", -1);
        }

        requestGetDetailServiceHistory(id);
        tv_vehicleName.setText(VIN);
        recycler.setHasFixedSize(true);
        manager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);
//        getList();    //initialize your list in this method
        adapter = new AddOnAdapter(list, this);
        recycler.setAdapter(adapter);

        btn_book_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataDetail = new Data();
                Intent intent = new Intent(DetailCarServiceHistoryActivity.this, SelectTimeSlotFragment.class);
                intent.putExtra("list_store", dataDetail);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(this);
        apiService = ApiClient.getClient(this).create(ApiService.class);
        recycler = findViewById(R.id.recyclerView);
        mRatingBar = (RatingBar) findViewById(R.id.rating);
        tv_arrivalTime = (TextView) findViewById(R.id.tv_arrivalTime);
        tv_leaveTime = (TextView) findViewById(R.id.tv_leaveTime);
        tv_ODO = (TextView) findViewById(R.id.tv_numKm);
        tv_address = (TextView) findViewById(R.id.tv_numAgency);
        tv_maintain = (TextView) findViewById(R.id.tv_maintain);
        btn_book_service = (TextView) findViewById(R.id.btn_book_service_now_detail);

    }

//    private void getList() {
//        list.add(new AddOn("Job", "Giá tiền",0));
//        list.add(new AddOn("Công việc 1", "854.000",0));
//        list.add(new AddOn("Phu tùng 1", "40.000",1));
//        list.add(new AddOn("Phu tùng 2", "150.000",1));
//        list.add(new AddOn("Phu tùng 3", "200.000",1));
//        list.add(new AddOn("Phu tùng 4", "500.000",1));
//        list.add(new AddOn("Công việc 1", "854.000",0));
//        list.add(new AddOn("Phu tùng 1", "40.000",1));
//        list.add(new AddOn("Phu tùng 2", "150.000",1));
//        list.add(new AddOn("Phu tùng 3", "200.000",1));
//        list.add(new AddOn("Phu tùng 4", "500.000",1));
//        list.add(new AddOn("Tổng tiền", "1.708.000",0));
//    }

    public void requestGetDetailServiceHistory(Integer id) {
        DialogUtils.showDialogLoadProgress(this);
        apiService
                .getDetailServiceHistory(mTypeFullAccessToken, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<DetailServiceHistoryResponse>() {
                    @Override
                    public void onSuccess(DetailServiceHistoryResponse detailServiceHistoryResponse) {
                        if (detailServiceHistoryResponse.getData() != null) {

                            tv_arrivalTime.setText(detailServiceHistoryResponse.getData().getArrivalTime() == null
                                    ? Constants.EMPTY : detailServiceHistoryResponse.getData().getArrivalTime());
                            tv_leaveTime.setText(detailServiceHistoryResponse.getData().getLeaveTime() == null
                                    ? Constants.EMPTY : detailServiceHistoryResponse.getData().getLeaveTime());
                            tv_ODO.setText(detailServiceHistoryResponse.getData().getOdometer() == null
                                    ? Constants.EMPTY  + " km" : detailServiceHistoryResponse.getData().getOdometer() + " km");
                            tv_maintain.setText(detailServiceHistoryResponse.getData().getServiceTypeNameVi() == null
                                    ? Constants.EMPTY : detailServiceHistoryResponse.getData().getServiceTypeNameVi());
                            tv_address.setText(detailServiceHistoryResponse.getData().getDealerName() == null
                                    ? Constants.EMPTY : detailServiceHistoryResponse.getData().getDealerName());


                            if (detailServiceHistoryResponse.getData().getRating() == null || detailServiceHistoryResponse.getData().getRating() <= 3) {
                                mRatingBar.setVisibility(View.GONE);
                                tvRating.setVisibility(View.GONE);
                            } else {
                                mRatingBar.setVisibility(View.VISIBLE);
                                tvRating.setVisibility(View.VISIBLE);
                                mRatingBar.setRating(detailServiceHistoryResponse.getData().getRating());
                            }


                            list.add(new AddOn("Job", "Giá tiền", 0));
                            for (ListJob listJob : detailServiceHistoryResponse.getData().getListJobs()) {
                                list.add(new AddOn(listJob.getJobServiceNameVi(), listJob.getTotalPrice(), 0));
                                for (ListAccessory listAccessory : listJob.getListAccessory()) {
                                    list.add(new AddOn(listAccessory.getNameVi(), listAccessory.getCost(), 1));
                                }
                            }
                            list.add(new AddOn("Tổng tiền", detailServiceHistoryResponse.getData().getTotalAmount(), 0));
                            adapter.notifyDataSetChanged();

                        }
                        DialogUtils.hideDialogLoadProgress();
                    }

                    @Override
                    public void onError(Throwable error) {
                        DialogUtils.hideDialogLoadProgress();
                        //THieu thong bao loi
                    }
                });
    }
}
