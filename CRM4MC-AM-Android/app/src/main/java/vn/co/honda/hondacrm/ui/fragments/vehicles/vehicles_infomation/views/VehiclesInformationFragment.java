package vn.co.honda.hondacrm.ui.fragments.vehicles.vehicles_infomation.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.vehicle.Vehicle;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation.Item;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation.VehicelDetailResponse;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation.VehicleInfo;
import vn.co.honda.hondacrm.ui.activities.vehicles.ListVehicleActivity;
import vn.co.honda.hondacrm.ui.adapters.vehicles.VehicleInformationAdapter;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static vn.co.honda.hondacrm.utils.DialogUtils.hideDialogLoadProgress;
import static vn.co.honda.hondacrm.utils.DialogUtils.showDialogLoadProgress;

public class VehiclesInformationFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private static final int REQUEST_CODE = 100;
    @BindView(R.id.indicator)
    ScrollingPagerIndicator indicator;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.btn_vehicle_list)
    TextView btnVehicleList;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshVehiclesLayout;


    ApiService apiService;
    String mTypeFullAccessToken;

    private int page = 0;
    private VehicleInformationAdapter pagerAdapter;
    private int currentPosition = 0;
    private List<Item> listVehicleInformation;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_vehicles_informations, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
//        instaDotView.setVisibleDotCounts(6);
        viewPager = v.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        listVehicleInformation = new ArrayList<>();
        pagerAdapter = new VehicleInformationAdapter(mContext, listVehicleInformation);
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        viewPager.addOnPageChangeListener(this);
        indicator.attachToPager(viewPager);
        indicator.setVisibleDotCount(7);
        indicator.setPositionDefault(0);

        btnVehicleList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), ListVehicleActivity.class), REQUEST_CODE);
            }
        });
        apiService = ApiClient.getClient(mContext.getApplicationContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(mContext);
        requestGetListVehicleInformation();
        swipeRefreshVehiclesLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshVehiclesLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestRefreshData();
            }
        });
    }

//    private void setupViewPager(ViewPager viewPager) {
//        fragmentVehicle = new FragmentVehicle();
//        fragmentVehicleOther = new FragmentVehicleOther();
//        pagerAdapter.addFragment(fragmentVehicleOther, "1");
//        pagerAdapter.addFragment(fragmentVehicle, "2");
//        viewPager.setAdapter(pagerAdapter);
//        pagerAdapter.notifyDataSetChanged();
//    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @OnClick(R.id.btn_vehicle_list)
    public void onVehicleListClick() {
        startActivityForResult(new Intent(getActivity(), ListVehicleActivity.class), REQUEST_CODE);
    }

    @Override
    public void onPageSelected(int i) {
        swipeRefreshVehiclesLayout.setEnabled(true);
        if (currentPosition < i) {
            // handle swipe RIGHT
            page++;
//            if (page > instaDotView.getNoOfPages() - 1) page = instaDotView.getNoOfPages() - 1;
//            instaDotView.onPageChange(page);
        } else if (currentPosition > i) {
            // handle swipe LEFT
            page--;
            if (page < 0) page = 0;
            //counter.setText(page + "");
//            instaDotView.onPageChange(page);
        }
        currentPosition = i; // Update current position
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        if(i == SCROLL_STATE_DRAGGING){
            swipeRefreshVehiclesLayout.setEnabled(false);
        } else {
            swipeRefreshVehiclesLayout.setEnabled(true);
        }

    }


    private void requestGetListVehicleInformation() {
        showDialogLoadProgress(mContext);
        apiService
                .getListVehicleInformation(mTypeFullAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VehicelDetailResponse>() {
                    @Override
                    public void onSuccess(VehicelDetailResponse response) {
                        hideDialogLoadProgress();
                        if (response != null && response.getData() != null && response.getData().getItems() != null) {
                            if (response.getData().getItems() != null && response.getData().getItems().size() == Constants.ONE) {
                                indicator.setVisibility(View.INVISIBLE);
                            } else {
                                indicator.setVisibility(View.VISIBLE);
                            }
                            listVehicleInformation.addAll(response.getData().getItems());
//                        instaDotView.setNoOfPages(listVehicleInformation.size());
                            pagerAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialogLoadProgress();
                        DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));

                    }
                });
    }

    private void requestRefreshData() {
        apiService
                .getListVehicleInformation(mTypeFullAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VehicelDetailResponse>() {
                    @Override
                    public void onSuccess(VehicelDetailResponse response) {
                        if (listVehicleInformation != null) {
                            listVehicleInformation.clear();
                            if (response.getData().getItems() != null && response.getData().getItems().size() == Constants.ONE) {
                                indicator.setVisibility(View.INVISIBLE);
                            } else {
                                indicator.setVisibility(View.VISIBLE);
                            }
                            listVehicleInformation.addAll(response.getData().getItems());
                            pagerAdapter.notifyDataSetChanged();
                            swipeRefreshVehiclesLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshVehiclesLayout.setRefreshing(false);
                        DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        if (data.getStringExtra("key_vehicle") != null) {
                            String vehicle = data.getStringExtra("key_vehicle");
                            if(data.getBooleanExtra("is_add_new",false) == true) {
                                showDialogLoadProgress(mContext);
                                apiService
                                        .getListVehicleInformation(mTypeFullAccessToken)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new SingleSubscriber<VehicelDetailResponse>() {
                                            @Override
                                            public void onSuccess(VehicelDetailResponse response) {
                                                if (response != null && response.getData() != null && response.getData().getItems() != null) {
                                                    if (listVehicleInformation != null) {
                                                        listVehicleInformation.clear();
                                                        if (response.getData().getItems() != null && response.getData().getItems().size() == Constants.ONE) {
                                                            indicator.setVisibility(View.INVISIBLE);
                                                        } else {
                                                            indicator.setVisibility(View.VISIBLE);
                                                        }
                                                        listVehicleInformation.addAll(response.getData().getItems());
                                                        pagerAdapter.notifyDataSetChanged();
                                                        for (int i = 0; i < listVehicleInformation.size();i++) {
                                                            Item item = listVehicleInformation.get(i);
                                                            if (item != null && item.getVehicleInfo() != null
                                                                    && item.getVehicleInfo().getVin().equals(vehicle)) {
                                                                currentPosition = i;
                                                                viewPager.setCurrentItem(i);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                hideDialogLoadProgress();
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                hideDialogLoadProgress();
                                                DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                                            }
                                        });
                            } else {
                                for (int i = 0; i < listVehicleInformation.size();i++) {
                                    Item item = listVehicleInformation.get(i);
                                    if (item != null && item.getVehicleInfo() != null
                                            && item.getVehicleInfo().getVin().equals(vehicle)) {
                                        currentPosition = i;
                                        viewPager.setCurrentItem(i);
                                        break;
                                    }
                                }
                            }

                        } else {
                            showDialogLoadProgress(mContext);
                            apiService
                                    .getListVehicleInformation(mTypeFullAccessToken)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new SingleSubscriber<VehicelDetailResponse>() {
                                        @Override
                                        public void onSuccess(VehicelDetailResponse response) {
                                            if (response != null && response.getData() != null && response.getData().getItems() != null) {
                                                if (listVehicleInformation != null) {
                                                    listVehicleInformation.clear();
                                                    if (response.getData().getItems() != null && response.getData().getItems().size() == Constants.ONE) {
                                                        indicator.setVisibility(View.INVISIBLE);
                                                    } else {
                                                        indicator.setVisibility(View.VISIBLE);
                                                    }
                                                    listVehicleInformation.addAll(response.getData().getItems());
                                                    pagerAdapter.notifyDataSetChanged();
                                                    viewPager.setCurrentItem(currentPosition);
                                                }
                                            }
                                            hideDialogLoadProgress();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            hideDialogLoadProgress();
                                            DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                                        }
                                    });
                        }
                    }
                    break;
                }
                break;
        }
    }
}
