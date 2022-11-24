package vn.co.honda.hondacrm.ui.activities.vehicles;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.vehicle.UpdateVehicleResponse;
import vn.co.honda.hondacrm.net.model.vehicle.Vehicle;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleResponse;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.connected.view.ConnectedMainActivity;
import vn.co.honda.hondacrm.ui.adapters.vehicles.IVehicleListener;
import vn.co.honda.hondacrm.ui.adapters.vehicles.IVehiclesCallBack;
import vn.co.honda.hondacrm.ui.adapters.vehicles.ItemTouchListener;
import vn.co.honda.hondacrm.ui.adapters.vehicles.SimpleItemTouchHelperCallback;
import vn.co.honda.hondacrm.ui.adapters.vehicles.VehicleAdapter;
import vn.co.honda.hondacrm.ui.fragments.connected.local.model.VehicleEntity;
import vn.co.honda.hondacrm.ui.fragments.connected.local.roomDatabase.VehicleRoomDb;
import vn.co.honda.hondacrm.utils.BitmapUtil;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PermissionUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

import static vn.co.honda.hondacrm.utils.Constants.REQUEST_CAMERA_CODE;
import static vn.co.honda.hondacrm.utils.Constants.REQUEST_GALLERY_CODE;
import static vn.co.honda.hondacrm.utils.DialogUtils.hideDialogLoadProgress;
import static vn.co.honda.hondacrm.utils.DialogUtils.showDialogLoadProgress;

public class ListVehicleActivity extends BaseActivity {
    private ImageView imgEditVehicle;
    private CircleImageView circleVehicle;
    private RecyclerView rvVehicle;
    private List<Vehicle> vehicleArrayList;
    private TextView txtAddVehicle, tvDoneVehicle;
    private ItemTouchHelper itemTouchHelper;
    private static final int REQUEST_CODE = 0;
    private VehicleAdapter adapter;
    private FrameLayout layout_fragment_vehicle;
    private TextView txtIntroduce;
    private View containerHolder, layout_imgVehicle;
    ApiService apiService;
    String mTypeFullAccessToken;
    private SwipeRefreshLayout swipeContainer;
    private File fileUpload;
    boolean isRestoreImage = false;
    public ProgressBar progressBar;
    boolean isLoading;
    int lastVisibleItem, totalItemCount;
    private static int sCurrentPage = 1;
    private int visibleItemCount;
    private int pastVisibleItems;
    private boolean isEdit = false;
    private boolean isAddNew = false;
    private VehicleRoomDb mDb;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_vehicles);
        fa = this;
        setTitleHeader(getString(R.string.vehicle_list));
        initViews();
    }

    private void initViews() {
        apiService = ApiClient.getClient(this).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(this);
        layout_fragment_vehicle = findViewById(R.id.layout_fragment_vehicle);
        tvDoneVehicle = findViewById(R.id.tvDoneVehicle);
        imgEditVehicle = findViewById(R.id.imgEditVehicle);
        imgEditVehicle.setVisibility(View.VISIBLE);
        txtIntroduce = findViewById(R.id.txtIntroduce);
        rvVehicle = findViewById(R.id.rvVehicle);
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(R.color.colorPrimary);
        swipeContainer.setEnabled(true);
        vehicleArrayList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        adapter = new VehicleAdapter(this, vehicleArrayList, mTypeFullAccessToken, apiService);
        adapter.setVehicleListener(mVehicleListener);
        adapter.setVehiclesCallBack(mVehiclesCallBack);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvVehicle.setLayoutManager(linearLayoutManager);
        rvVehicle.setAdapter(adapter);
        requestData(1);
        txtAddVehicle = findViewById(R.id.txtAddVehicle);
        txtAddVehicle.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddVehiclesActivity.class);
            startActivityForResult(intent, REQUEST_CODE);

        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                isLoading = true;
                fetchTimelineAsync(1);
            }
        });
        imgEditVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;
                setMovePointList(true);
            }
        });
        tvDoneVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = false;
                setMovePointList(false);
            }
        });


        initScrollListener();
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        refreshData(page);
    }


    private IVehiclesCallBack mVehiclesCallBack = this::setLayoutDisplay;

    private IVehicleListener mVehicleListener = new IVehicleListener() {
        @Override
        public void editVehicle(Vehicle vehicle) {
            if (vehicle.getVehicleType() != null) {
                showDialogEditVehicle(vehicle);
            }
        }

        @Override
        public void deleteVehicle(Vehicle vehicle) {
            DialogUtils.showDialogLogout(ListVehicleActivity.this, R.layout.diaglog_confirm_delete, 0.9f, 0.3f, new DialogUtils.DialogListener() {
                @Override
                public void okButtonClick(Dialog dialog) {
                    deleteRequestVehicle(vehicle);
                    dialog.dismiss();
                }

                @Override
                public void cancelButtonClick() {
                }
            }, "Do you want to delete " + vehicle.getVehicleName() + " ?");
        }

        @Override
        public void onItemClick(String VIN) {
            Intent intent = new Intent();
            intent.putExtra("key_vehicle", VIN);
            intent.putExtra("is_add_new", isAddNew);
            setResult(RESULT_OK, intent);
            // reset flag
            isAddNew = false;
            finish();
        }
    };

    private void initScrollListener() {
        rvVehicle.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void loadMore() {
        progressBar.setVisibility(View.VISIBLE);
        sCurrentPage++;
        refreshDataLoadMore(sCurrentPage);

    }

    private void setLayoutDisplay(int size) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        if (size != Constants.ZERO) {
            layoutParams.gravity = Gravity.TOP;
            layout_fragment_vehicle.setLayoutParams(layoutParams);
            txtIntroduce.setVisibility(View.GONE);
            if (isEdit) {
                imgEditVehicle.setVisibility(View.INVISIBLE);
                tvDoneVehicle.setVisibility(View.VISIBLE);
            } else {
                imgEditVehicle.setVisibility(View.VISIBLE);
                tvDoneVehicle.setVisibility(View.INVISIBLE);
            }
        } else {
            layoutParams.gravity = Gravity.CENTER;
            layout_fragment_vehicle.setLayoutParams(layoutParams);
            txtIntroduce.setVisibility(View.VISIBLE);
            imgEditVehicle.setVisibility(View.INVISIBLE);
            tvDoneVehicle.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * This method using to update vehicle information.
     *
     * @param vehicle {@link Vehicle}
     */
    private void updateRequestVehicle(Vehicle vehicle, int index, boolean isRestoreImage) {
        showDialogLoadProgress(this);
        if (isRestoreImage) {
            apiService
                    .updateVehicleInfoImageEmpty(
                            mTypeFullAccessToken,
                            vehicle.getId(),
                            setNullObject(vehicle.getVehicleName()),
                            setNullObject(vehicle.getLicensePlate()),
                            ""
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleSubscriber<UpdateVehicleResponse>() {
                        @Override
                        public void onSuccess(UpdateVehicleResponse response) {
                            try {
                                VehicleEntity vehicleEntity = new VehicleEntity(
                                        vehicle.getVin(),
                                        vehicle.getVehicleName(),
                                        vehicle.getLicensePlate(),
                                        vehicle.getVehicleType() + "",
                                        vehicle.getIsConnected() == 1,
                                        vehicle.getVin().equals(PrefUtils.getStringPref(getApplicationContext(), Constants.DEFAULT_VEHICLE)),
                                        vehicle.getVin().equals(PrefUtils.getStringPref(getApplicationContext(), Constants.VIN_OF_CONNECTED_VEHICLE_NOW)),
                                        mDb.daoAccess().fetchVehicleByVin(vehicle.getVin()).getBtuName(),
                                        vehicle.getVehicleImageUrl(), "", "", "", ""
                                );
                                mDb.daoAccess().updateVehicle(vehicleEntity);
                            } catch (Exception e) {
                                Log.e("Error", "Can't Update !");
                            }
                            hideDialogLoadProgress();
                            vehicle.setTempImage(null);
                            vehicle.setVehicleImage(null);
                            vehicleArrayList.set(index, vehicle);
                            adapter.notifyDataSetChanged();
                            Intent intent = new Intent();
                            isAddNew = true;
                            setResult(RESULT_OK, intent);
                        }

                        @Override
                        public void onError(Throwable e) {
                            hideDialogLoadProgress();
                            DialogUtils.showDialogConfirmLogin(ListVehicleActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                        }
                    });
        } else {
            RequestBody requestFile;
            MultipartBody.Part body = null;
            if (fileUpload != null) {
                requestFile = RequestBody.create(MediaType.parse("image/png"), fileUpload);
                // MultipartBody.Part is used to send also the actual file name
                body = MultipartBody.Part.createFormData("vehicle_image", fileUpload.getName(), requestFile);
            }

            RequestBody vehicleName = RequestBody.create(
                    MediaType.parse("text/plain"),
                    vehicle.getVehicleName());

            RequestBody vehiclePlate = RequestBody.create(
                    MediaType.parse("text/plain"),
                    vehicle.getLicensePlate());

            apiService
                    .updateVehicleInfoNew(
                            mTypeFullAccessToken,
                            vehicle.getId(),
                            vehicleName,
                            vehiclePlate,
                            body
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleSubscriber<UpdateVehicleResponse>() {
                        @Override
                        public void onSuccess(UpdateVehicleResponse response) {
                            hideDialogLoadProgress();
                            if (response.getVehicle() != null) {
                                vehicle.setVehicleImage(response.getVehicle().getVehicleImage());
                            }
                            vehicleArrayList.set(index, vehicle);
                            adapter.notifyDataSetChanged();
                            Intent intent = new Intent();
                            isAddNew = true;
                            setResult(RESULT_OK, intent);
                        }

                        @Override
                        public void onError(Throwable e) {
                            hideDialogLoadProgress();
                            DialogUtils.showDialogConfirmLogin(ListVehicleActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                        }
                    });
        }
    }

    private String setNullObject(String content) {
        if (content == null || TextUtils.isEmpty(content)) {
            return null;
        }
        return content;
    }

    /**
     * This method using to remove vehicle.
     *
     * @param vehicle {@link Vehicle}
     */
    private void deleteRequestVehicle(Vehicle vehicle) {
        showDialogLoadProgress(this);
        apiService
                .deleteVehicle(
                        mTypeFullAccessToken,
                        vehicle.getId()
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VehicleResponse>() {
                    @Override
                    public void onSuccess(VehicleResponse response) {
                        hideDialogLoadProgress();
                        vehicleArrayList.remove(vehicle);
                        adapter.notifyDataSetChanged();
                        Intent intent = new Intent();
                        isAddNew = true;
                        setResult(RESULT_OK, intent);

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialogLoadProgress();
                        DialogUtils.showDialogConfirmLogin(ListVehicleActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getIntExtra("vehicleReturn", -1) != -1) {
                        isEdit = false;
                        adapter.setEdit(false);
                        swipeContainer.setEnabled(true);
                        isAddNew = true;
                        requestData(1);
                        if (data.getIntExtra("vehicleReturn", -1) == 1) {
                            if (data.getStringExtra("vehicleData") != null) {
                                PrefUtils.setStringPref(this, "LAST_ADDED_VEHICLE", data.getStringExtra("vehicleData"));
                                startActivity(new Intent(this, ConnectedMainActivity.class));
                            }
                        }
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                    }
                    break;
                }
            case REQUEST_CAMERA_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        fileUpload = BitmapUtil.getFileFromBitmap(this, photo);
                        containerHolder.setVisibility(View.INVISIBLE);
                        circleVehicle.setVisibility(View.VISIBLE);
                        circleVehicle.setImageBitmap(photo);
                    }
                }
                break;
            case REQUEST_GALLERY_CODE:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        //Display an error
                        return;
                    }
                    try {
                        if (data.getData() != null) {
                            InputStream inputStream = getContentResolver().openInputStream(data.getData());
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            fileUpload = BitmapUtil.getFileFromBitmap(this, bitmap);
                            containerHolder.setVisibility(View.INVISIBLE);
                            circleVehicle.setVisibility(View.VISIBLE);
                            circleVehicle.setImageBitmap(bitmap);
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

    }

    /**
     * Just only move item in case Edit List.
     *
     * @param isEdit true/false
     */
    private void setMovePointList(boolean isEdit) {
        adapter.setEdit(isEdit);
        if (isEdit) {
            swipeContainer.setRefreshing(false);
            if (itemTouchHelper == null) {
                addItemTouchCallback(rvVehicle);
            } else {
                itemTouchHelper.attachToRecyclerView(rvVehicle);
            }
            swipeContainer.setEnabled(false);
        } else {
            itemTouchHelper.attachToRecyclerView(null);
            swipeContainer.setEnabled(true);
        }
        adapter.notifyDataSetChanged();
    }

    private void addItemTouchCallback(RecyclerView recyclerView) {
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(new ItemTouchListener() {
            @Override
            public void onMove(int oldPosition, int newPosition) {
                adapter.onMove(oldPosition, newPosition);
            }
        });
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    private void requestData(Integer page) {
        showDialogLoadProgress(this);
        apiService
                .getListVehicleByUser(mTypeFullAccessToken, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VehicleResponse>() {
                    @Override
                    public void onSuccess(VehicleResponse response) {
                        hideDialogLoadProgress();
                        vehicleArrayList.clear();
                        if (response.getVehicle() != null && response.getVehicle().getVehicles() != null) {
                            vehicleArrayList.addAll(response.getVehicle().getVehicles());
                            adapter.notifyDataSetChanged();
                        }
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialogLoadProgress();
                        DialogUtils.showDialogConfirmLogin(ListVehicleActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                    }
                });
    }

    private void refreshData(Integer page) {
        apiService
                .getListVehicleByUser(mTypeFullAccessToken, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<VehicleResponse>() {
                    @Override
                    public void onSuccess(VehicleResponse response) {
                        vehicleArrayList.clear();
                        if (response.getVehicle() != null && response.getVehicle().getVehicles() != null) {
                            vehicleArrayList.addAll(response.getVehicle().getVehicles());
                            adapter.notifyDataSetChanged();
                        }
                        swipeContainer.setRefreshing(false);
                        isLoading = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeContainer.setRefreshing(false);
                        isLoading = false;
                        DialogUtils.showDialogConfirmLogin(ListVehicleActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
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
                            vehicleArrayList.addAll(response.getVehicle().getVehicles());
                            adapter.notifyDataSetChanged();
                            isLoading = false;
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtils.showDialogConfirmLogin(ListVehicleActivity.this, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }


    public boolean isEmptyVehicle() {
        return vehicleArrayList.isEmpty();
    }

    public void showDialogEditVehicle(Vehicle vehicle) {
        Dialog dialog = DialogUtils.getDialogDefault(this, R.layout.dialog_edit_vehicle, 0.9f, 0.8f);
        TextView btnOK = dialog.findViewById(R.id.btn_ok);
        TextView btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView txtName = dialog.findViewById(R.id.txt_current_vehicle_name);
        TextView txtPlate = dialog.findViewById(R.id.txt_current_vehicle_plate);
        TextView txtResetImage = dialog.findViewById(R.id.txt_restore_vehicle_picture);
        containerHolder = dialog.findViewById(R.id.frContainerHolder);
        layout_imgVehicle = dialog.findViewById(R.id.layout_imgVehicle);
        ImageView imgVehicle = dialog.findViewById(R.id.imgVehicle);
        circleVehicle = dialog.findViewById(R.id.circleVehicle);
        txtResetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vehicle.getVehicleImage() != null) {
                    isRestoreImage = true;
                    vehicle.setTempImage(null);
                    vehicle.setVehicleImage(null);
                    if (vehicle.getVehicleType() == 2) {
                        containerHolder.setVisibility(View.VISIBLE);
                        circleVehicle.setVisibility(View.INVISIBLE);
                        imgVehicle.setImageResource(R.drawable.ic_vehicle_fake);
                    } else {
                        containerHolder.setVisibility(View.VISIBLE);
                        circleVehicle.setVisibility(View.INVISIBLE);
                        imgVehicle.setImageResource(R.drawable.ic_moto);
                    }
                }
            }
        });
        txtName.setText(vehicle.getVehicleName());
        txtPlate.setText(vehicle.getLicensePlate());
//        if (vehicle.getVehicleType() == 2) {
//            Picasso.with(this)
//                    .load(vehicle.getVehicleImage())
//                    .placeholder(R.drawable.ic_edit_oto)
//                    .error(R.drawable.ic_edit_oto)
//                    .into(circleVehicle, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            vehicle.setTempImage(((BitmapDrawable) circleVehicle.getDrawable()).getBitmap());
//                        }
//
//                        @Override
//                        public void onError() {
//                        }
//                    });
//        } else {
//            Picasso.with(this)
//                    .load(vehicle.getVehicleImage())
//                    .placeholder(R.drawable.ic_edit_moto)
//                    .error(R.drawable.ic_edit_moto)
//                    .into(circleVehicle, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            vehicle.setTempImage(((BitmapDrawable) circleVehicle.getDrawable()).getBitmap());
//                        }
//
//                        @Override
//                        public void onError() {
//                        }
//                    });
//        }

        if (vehicle.getVehicleType() != null && vehicle.getVehicleType() == 2) {
            if (vehicle.getVehicleImage() == null) {
                containerHolder.setVisibility(View.VISIBLE);
                circleVehicle.setVisibility(View.INVISIBLE);
                imgVehicle.setImageResource(R.drawable.ic_vehicle_fake);
            } else {
                Picasso.with(this)
                        .load(vehicle.getVehicleImage())
                        .placeholder(R.drawable.circle_holder)
                        .error(R.drawable.circle_holder)
                        .into(circleVehicle, new Callback() {
                            @Override
                            public void onSuccess() {
                                containerHolder.setVisibility(View.INVISIBLE);
                                circleVehicle.setVisibility(View.VISIBLE);
                                vehicle.setTempImage(((BitmapDrawable) circleVehicle.getDrawable()).getBitmap());
                            }

                            @Override
                            public void onError() {
                                containerHolder.setVisibility(View.VISIBLE);
                                circleVehicle.setVisibility(View.INVISIBLE);
                                imgVehicle.setImageResource(R.drawable.ic_vehicle_fake);
                                isRestoreImage = true;
                                vehicle.setTempImage(null);
                                vehicle.setVehicleImage(null);
                            }
                        });
            }
        } else if (vehicle.getVehicleType() != null && vehicle.getVehicleType() == 1) {
            if (vehicle.getVehicleImage() == null) {
                containerHolder.setVisibility(View.VISIBLE);
                circleVehicle.setVisibility(View.INVISIBLE);
                imgVehicle.setImageResource(R.drawable.ic_moto);
            } else {
                Picasso.with(this)
                        .load(vehicle.getVehicleImage())
                        .placeholder(R.drawable.circle_holder)
                        .error(R.drawable.circle_holder)
                        .into(circleVehicle, new Callback() {
                            @Override
                            public void onSuccess() {
                                containerHolder.setVisibility(View.INVISIBLE);
                                circleVehicle.setVisibility(View.VISIBLE);
                                vehicle.setTempImage(((BitmapDrawable) circleVehicle.getDrawable()).getBitmap());
                            }

                            @Override
                            public void onError() {
                                containerHolder.setVisibility(View.VISIBLE);
                                circleVehicle.setVisibility(View.INVISIBLE);
                                imgVehicle.setImageResource(R.drawable.ic_moto);
                            }
                        });
            }
        }

        layout_imgVehicle.setOnClickListener(v -> DialogUtils.showDialogChooseImage(ListVehicleActivity.this, R.layout.dialog_capture, new DialogUtils.DialogChooseImageListener() {
            @Override
            public void onCameraClick() {
                if (ContextCompat.checkSelfPermission(ListVehicleActivity.this, Manifest.permission.CAMERA) == PackageManager
                        .PERMISSION_GRANTED) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    ListVehicleActivity.this.startActivityForResult(takePicture, REQUEST_CAMERA_CODE);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (PermissionUtils.neverAskAgainSelected(ListVehicleActivity.this, Manifest.permission.CAMERA)) {
                            PermissionUtils.displayNeverAskAgainDialog(ListVehicleActivity.this);
                        } else {
                            PermissionUtils.requestPermission(ListVehicleActivity.this);
                        }
                    }
                }
            }

            @Override
            public void onGalleryClick() {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_GALLERY_CODE);
            }

            @Override
            public void cancelButtonClick() {

            }
        }));

        btnOK.setOnClickListener(v -> {
            EditText editNewVehicleName = dialog.findViewById(R.id.edit_new_vehicle_name);
            EditText editNewVehiclePlate = dialog.findViewById(R.id.edit_new_vehicle_plate);
            TextView txtUnder = dialog.findViewById(R.id.txt_restore_vehicle_picture);
            txtUnder.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            String vehicleNewName = String.valueOf(editNewVehicleName.getText()).trim();
            String vehicleNewPlate = String.valueOf(editNewVehiclePlate.getText()).trim();
            Integer indexVehicle = vehicleArrayList.indexOf(vehicle);
            Bitmap vehiclePicture;
            if(circleVehicle.getVisibility() == View.VISIBLE){
                 vehiclePicture = ((BitmapDrawable) circleVehicle.getDrawable()).getBitmap();
            } else {
                vehiclePicture = ((BitmapDrawable) imgVehicle.getDrawable()).getBitmap();
            }

            if (TextUtils.isEmpty(vehicleNewName) && TextUtils.isEmpty(vehicleNewPlate) && vehiclePicture.sameAs(vehicle.getTempImage())) {
                dialog.dismiss();
            } else if (TextUtils.isEmpty(vehicleNewName)
                    && TextUtils.isEmpty(vehicleNewPlate)
                    && isRestoreImage == false
                    && containerHolder.getVisibility() == View.VISIBLE) {
                dialog.dismiss();
            } else {
                if (!TextUtils.isEmpty(vehicleNewName)) {
                    vehicle.setVehicleName(vehicleNewName);
                }
                if (!TextUtils.isEmpty(vehicleNewPlate)) {
                    vehicle.setLicensePlate(vehicleNewPlate);
                }
                if (((BitmapDrawable) circleVehicle.getDrawable()) != null &&
                        ((BitmapDrawable) circleVehicle.getDrawable()).getBitmap() != null &&
                        !((BitmapDrawable) circleVehicle.getDrawable()).getBitmap().sameAs(vehicle.getTempImage())) {
                    vehicle.setTempImage(vehiclePicture);
                }
                if (containerHolder.getVisibility() == View.VISIBLE) {
                    updateRequestVehicle(vehicle, indexVehicle, true);
                    isRestoreImage = false;
                } else {
                    updateRequestVehicle(vehicle, indexVehicle, false);
                }
            }
            dialog.dismiss();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case  Constants.PERMISSION_REQUEST_CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, REQUEST_CAMERA_CODE);
                } else {
                    PermissionUtils.setShouldShowStatus(this, Manifest.permission.CAMERA);
                }
                break;
        }
    }
}
