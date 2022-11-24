//package vn.co.honda.hondacrm.ui.fragments.vehicles;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Paint;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.helper.ItemTouchHelper;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.squareup.otto.Subscribe;
//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import rx.SingleSubscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//import vn.co.honda.hondacrm.R;
//import vn.co.honda.hondacrm.navigator.bus.Events;
//import vn.co.honda.hondacrm.navigator.bus.GlobalBus;
//import vn.co.honda.hondacrm.net.ApiService;
//import vn.co.honda.hondacrm.net.core.ApiClient;
//import vn.co.honda.hondacrm.net.model.vehicle.UpdateVehicleResponse;
//import vn.co.honda.hondacrm.net.model.vehicle.Vehicle;
//import vn.co.honda.hondacrm.net.model.vehicle.VehicleResponse;
//import vn.co.honda.hondacrm.ui.activities.connected.view.ConnectedMainActivity;
//import vn.co.honda.hondacrm.ui.activities.vehicles.AddVehiclesActivity;
//import vn.co.honda.hondacrm.ui.adapters.vehicles.IVehicleListener;
//import vn.co.honda.hondacrm.ui.adapters.vehicles.IVehiclesCallBack;
//import vn.co.honda.hondacrm.ui.adapters.vehicles.ItemTouchListener;
//import vn.co.honda.hondacrm.ui.adapters.vehicles.SimpleItemTouchHelperCallback;
//import vn.co.honda.hondacrm.ui.adapters.vehicles.VehicleAdapter;
//import vn.co.honda.hondacrm.utils.BitmapUtil;
//import vn.co.honda.hondacrm.utils.Constants;
//import vn.co.honda.hondacrm.utils.DialogUtils;
//import vn.co.honda.hondacrm.utils.PrefUtils;
//
//import static android.app.Activity.RESULT_OK;
//import static vn.co.honda.hondacrm.utils.Constants.REQUEST_CAMERA_CODE;
//import static vn.co.honda.hondacrm.utils.Constants.REQUEST_GALLERY_CODE;
//import static vn.co.honda.hondacrm.utils.DialogUtils.hideDialogLoadProgress;
//import static vn.co.honda.hondacrm.utils.DialogUtils.showDialogLoadProgress;
//
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link VehiclesFragment} interface
// * to handle interaction events.
// * Use the {@link VehiclesFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class VehiclesFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private ImageView imgChosePicture;
//    private RecyclerView rvVehicle;
//    private List<Vehicle> vehicleArrayList;
//    private TextView txtAddVehicle;
//    private ItemTouchHelper itemTouchHelper;
//    private static final int REQUEST_CODE = 0;
//    private VehicleAdapter adapter;
//    private FrameLayout layout_fragment_vehicle;
//    private TextView txtIntroduce;
//    ApiService apiService;
//    String mTypeFullAccessToken;
//    private SwipeRefreshLayout swipeContainer;
//    private File fileUpload;
//    boolean isRestoreImage = false;
//    public ProgressBar progressBar;
//    boolean isLoading;
//    int lastVisibleItem, totalItemCount;
//    private static int sCurrentPage = 1;
//    private int visibleItemCount;
//    private int pastVisibleItems;
//    private Context mContext;
//    public VehiclesFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment DealerFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static VehiclesFragment newInstance(String param1, String param2) {
//        VehiclesFragment fragment = new VehiclesFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_list_vehicles, container, false);
//        initViews(view);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Your code to refresh the list here.
//                // Make sure you call swipeContainer.setRefreshing(false)
//                // once the network request has completed successfully.
//                isLoading = true;
//                fetchTimelineAsync(1);
//            }
//        });
//        return view;
//    }
//
//    public void fetchTimelineAsync(int page) {
//        // Send the network request to fetch the updated data
//        // `client` here is an instance of Android Async HTTP
//        // getHomeTimeline is an example endpoint.
//        refreshData(page);
//    }
//
//    private void initViews(View view) {
//        layout_fragment_vehicle = view.findViewById(R.id.layout_fragment_vehicle);
//        txtIntroduce = view.findViewById(R.id.txtIntroduce);
//        rvVehicle = view.findViewById(R.id.rvVehicle);
//        swipeContainer = view.findViewById(R.id.swipeContainer);
//        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
//                R.color.colorPrimary);
//        swipeContainer.setEnabled(true);
//        vehicleArrayList = new ArrayList<>();
//        progressBar = view.findViewById(R.id.progressBar);
//        adapter = new VehicleAdapter(getActivity(), vehicleArrayList, mTypeFullAccessToken, apiService);
//        adapter.setVehicleListener(mVehicleListener);
//        adapter.setVehiclesCallBack(mVehiclesCallBack);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        rvVehicle.setLayoutManager(linearLayoutManager);
//        rvVehicle.setAdapter(adapter);
//        requestData(1);
//        txtAddVehicle = view.findViewById(R.id.txtAddVehicle);
//        txtAddVehicle.setOnClickListener(v -> {
//            Intent intent = new Intent(v.getContext(), AddVehiclesActivity.class);
//            startActivityForResult(intent, REQUEST_CODE);
//
//        });
//
//        initScrollListener();
//    }
//
//    private void initScrollListener() {
//        rvVehicle.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                    visibleItemCount = layoutManager.getChildCount();
//                    totalItemCount = layoutManager.getItemCount();
//                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
//                    if (!isLoading && !swipeContainer.isRefreshing()) {
//                        if (totalItemCount >= Constants.MAX_ITEMS && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
//                            isLoading = true;
//                            loadMore();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
//
//
//    }
//
//    private void loadMore() {
//        progressBar.setVisibility(View.VISIBLE);
//        sCurrentPage++;
//        refreshDataLoadMore(sCurrentPage);
//
//    }
//
//    private void setLayoutDisplay(int size) {
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//        if (size != Constants.ZERO) {
//            layoutParams.gravity = Gravity.TOP;
//            layout_fragment_vehicle.setLayoutParams(layoutParams);
//            txtIntroduce.setVisibility(View.GONE);
//        } else {
//            layoutParams.gravity = Gravity.CENTER;
//            layout_fragment_vehicle.setLayoutParams(layoutParams);
//            txtIntroduce.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private IVehiclesCallBack mVehiclesCallBack = this::setLayoutDisplay;
//
//    private IVehicleListener mVehicleListener = new IVehicleListener() {
//        @Override
//        public void editVehicle(Vehicle vehicle) {
//            if (vehicle.getBodyStyle() != null) {
//                showDialogEditVehicle(vehicle);
//            }
//        }
//
//        @Override
//        public void deleteVehicle(Vehicle vehicle) {
//            DialogUtils.showDialogLogout(getContext(), R.layout.diaglog_confirm_delete, 0.9f, 0.3f, new DialogUtils.DialogListener() {
//                @Override
//                public void okButtonClick(Dialog dialog) {
//                    deleteRequestVehicle(vehicle);
//                    dialog.dismiss();
//                }
//
//                @Override
//                public void cancelButtonClick() {
//                }
//            }, mContext.getString(R.string.msg_want_to_delete) + vehicle.getVehicleName() + " ?");
//        }
//
//        @Override
//        public void onItemClick(Vehicle vehicle) {
//            // do not something
//        }
//    };
//
//    /**
//     * This method using to update vehicle information.
//     *
//     * @param vehicle {@link Vehicle}
//     */
//    private void updateRequestVehicle(Vehicle vehicle, int index, boolean isRestoreImage) {
//        showDialogLoadProgress(getContext());
//        if (isRestoreImage) {
//            apiService
//                    .updateVehicleInfoImageEmpty(
//                            mTypeFullAccessToken,
//                            vehicle.getId(),
//                            setNullObject(vehicle.getVehicleName()),
//                            setNullObject(vehicle.getLicensePlate()),
//                            ""
//                    )
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new SingleSubscriber<UpdateVehicleResponse>() {
//                        @Override
//                        public void onSuccess(UpdateVehicleResponse response) {
//                            hideDialogLoadProgress();
//                            vehicle.setTempImage(null);
//                            vehicle.setVehicleImage(null);
//                            vehicleArrayList.set(index, vehicle);
//                            adapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            hideDialogLoadProgress();
//                            DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
//                        }
//                    });
//        } else {
//            RequestBody requestFile;
//            MultipartBody.Part body = null;
//            if (fileUpload != null) {
//                requestFile = RequestBody.create(MediaType.parse("image/png"), fileUpload);
//                // MultipartBody.Part is used to send also the actual file name
//                body = MultipartBody.Part.createFormData("vehicle_image", fileUpload.getName(), requestFile);
//            }
//
//            RequestBody vehicleName = RequestBody.create(
//                    MediaType.parse("text/plain"),
//                    vehicle.getVehicleName());
//
//            RequestBody vehiclePlate = RequestBody.create(
//                    MediaType.parse("text/plain"),
//                    vehicle.getLicensePlate());
//
//            apiService
//                    .updateVehicleInfoNew(
//                            mTypeFullAccessToken,
//                            vehicle.getId(),
//                            vehicleName,
//                            vehiclePlate,
//                            body
//                    )
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new SingleSubscriber<UpdateVehicleResponse>() {
//                        @Override
//                        public void onSuccess(UpdateVehicleResponse response) {
//                            hideDialogLoadProgress();
//                            if (response.getVehicle() != null) {
//                                vehicle.setVehicleImage(response.getVehicle().getVehicleImage());
//                            }
//                            vehicleArrayList.set(index, vehicle);
//                            adapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            hideDialogLoadProgress();
//                            DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getContext().getString(R.string.networkError));
//                        }
//                    });
//        }
//    }
//
//    private String setNullObject(String content) {
//        if (content == null || TextUtils.isEmpty(content)) {
//            return null;
//        }
//        return content;
//    }
//
//    /**
//     * This method using to remove vehicle.
//     *
//     * @param vehicle {@link Vehicle}
//     */
//    private void deleteRequestVehicle(Vehicle vehicle) {
//        showDialogLoadProgress(getContext());
//        apiService
//                .deleteVehicle(
//                        mTypeFullAccessToken,
//                        vehicle.getId()
//                )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleSubscriber<VehicleResponse>() {
//                    @Override
//                    public void onSuccess(VehicleResponse response) {
//                        hideDialogLoadProgress();
//                        vehicleArrayList.remove(vehicle);
//                        Events.FragmentActivityMessage fragmentActivityMessage = new Events.FragmentActivityMessage(vehicleArrayList.size(), true);
//                        GlobalBus.getBus().post(fragmentActivityMessage);
//                        adapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        hideDialogLoadProgress();
//                        DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
//                    }
//                });
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mContext = context;
//        GlobalBus.getBus().register(this);
//        apiService = ApiClient.getClient(getContext()).create(ApiService.class);
//        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(getContext());
//    }
//
//    @Subscribe
//    public void getMessage(Events.ActivityFragmentMessage activityFragmentMessage) {
//        switch (activityFragmentMessage.getMessage()) {
//            case Constants.EDIT:
//                setMovePointList(true);
//                adapter.notifyDataSetChanged();
//                swipeContainer.setEnabled(false);
//                break;
//            case Constants.DONE:
//                setMovePointList(false);
//                adapter.notifyDataSetChanged();
//                swipeContainer.setEnabled(true);
//                break;
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mVehiclesCallBack = null;
//        GlobalBus.getBus().unregister(this);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQUEST_CODE:
//                if (resultCode == RESULT_OK) {
//                    if (data != null && data.getIntExtra("vehicleReturn", -1) != -1) {
//
//                        adapter.setEdit(false);
//                        swipeContainer.setEnabled(true);
//                        requestData(1);
//                        Events.FragmentActivityMessage fragmentActivityMessage = new Events.FragmentActivityMessage(vehicleArrayList.size());
//                        GlobalBus.getBus().post(fragmentActivityMessage);
//                        if (data.getIntExtra("vehicleReturn", -1) == 1) {
//                            Intent intent = new Intent();
//                            intent.putExtra("VIN", data.getStringExtra("VINVEHICLE"));
//                            intent.putExtra("vehicleId", data.getStringExtra("vehicleId"));
//                            startActivity(new Intent(getActivity(), ConnectedMainActivity.class));
//                            getActivity().finish();
////                            Intent intent = new Intent();
////                            intent.setAction(UPDATE_CURRENT_VIEWPAGER);
////                            intent.putExtra(TYPE_VIEWPAGE, TAB_CONNECTED);
////                            intent.putExtra(VehicleStatusFragment.KEY_WAITING_BTU_DISCONNECT, true);
////                            intent.putExtra("VIN", data.getStringExtra("VINVEHICLE"));
////                            intent.putExtra("vehicleId", data.getStringExtra("vehicleId"));
////                            getActivity().sendBroadcast(intent);
//                        }
//                    }
//                    break;
//                }
//            case REQUEST_CAMERA_CODE:
//                if (resultCode == RESULT_OK) {
//                    if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
//                        Bitmap photo = (Bitmap) data.getExtras().get("data");
//                        fileUpload = BitmapUtil.getFileFromBitmap(getContext(), photo);
//                        imgChosePicture.setScaleType(ImageView.ScaleType.FIT_XY);
//                        imgChosePicture.setImageBitmap(photo);
//                    }
//                }
//                break;
//            case REQUEST_GALLERY_CODE:
//                if (resultCode == RESULT_OK) {
//                    if (data == null) {
//                        //Display an error
//                        return;
//                    }
//                    try {
//                        if (data.getData() != null) {
//                            InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
//                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                            fileUpload = BitmapUtil.getFileFromBitmap(getContext(), bitmap);
//                            imgChosePicture.setScaleType(ImageView.ScaleType.FIT_XY);
//                            imgChosePicture.setImageBitmap(bitmap);
//                        }
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//        }
//
//    }
//
//    /**
//     * Just only move item in case Edit List.
//     *
//     * @param isEdit true/false
//     */
//    private void setMovePointList(boolean isEdit) {
//        adapter.setEdit(isEdit);
//        if (isEdit) {
//            swipeContainer.setRefreshing(false);
//            if (itemTouchHelper == null) {
//                addItemTouchCallback(rvVehicle);
//            } else {
//                itemTouchHelper.attachToRecyclerView(rvVehicle);
//            }
//        } else {
//            itemTouchHelper.attachToRecyclerView(null);
//        }
//    }
//
//    private void addItemTouchCallback(RecyclerView recyclerView) {
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(new ItemTouchListener() {
//            @Override
//            public void onMove(int oldPosition, int newPosition) {
//                adapter.onMove(oldPosition, newPosition);
//            }
//        });
//        itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
//    }
//
//    private void requestData(Integer page) {
//        showDialogLoadProgress(getContext());
//        apiService
//                .getListVehicleByUser(mTypeFullAccessToken, page)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleSubscriber<VehicleResponse>() {
//                    @Override
//                    public void onSuccess(VehicleResponse response) {
//                        hideDialogLoadProgress();
//                        vehicleArrayList.clear();
//                        if (response.getVehicle() != null && response.getVehicle().getVehicles() != null) {
//                            vehicleArrayList.addAll(response.getVehicle().getVehicles());
//                            adapter.notifyDataSetChanged();
//                            Events.FragmentActivityMessage fragmentActivityMessage = new Events.FragmentActivityMessage(vehicleArrayList.size(), adapter.isEdit());
//                            GlobalBus.getBus().post(fragmentActivityMessage);
//                        }
//                        swipeContainer.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        hideDialogLoadProgress();
//                        DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
//                    }
//                });
//    }
//
//    private void refreshData(Integer page) {
//        apiService
//                .getListVehicleByUser(mTypeFullAccessToken, page)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleSubscriber<VehicleResponse>() {
//                    @Override
//                    public void onSuccess(VehicleResponse response) {
//                        vehicleArrayList.clear();
//                        if (response.getVehicle() != null && response.getVehicle().getVehicles() != null) {
//                            vehicleArrayList.addAll(response.getVehicle().getVehicles());
//                            adapter.notifyDataSetChanged();
//                            Events.FragmentActivityMessage fragmentActivityMessage = new Events.FragmentActivityMessage(vehicleArrayList.size());
//                            GlobalBus.getBus().post(fragmentActivityMessage);
//                        }
//                        swipeContainer.setRefreshing(false);
//                        isLoading = false;
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        swipeContainer.setRefreshing(false);
//                        isLoading = false;
//                        DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
//                    }
//                });
//    }
//
//    private void refreshDataLoadMore(Integer page) {
//        apiService
//                .getListVehicleByUser(mTypeFullAccessToken, page)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleSubscriber<VehicleResponse>() {
//                    @Override
//                    public void onSuccess(VehicleResponse response) {
//                        if (response.getVehicle() != null && response.getVehicle().getVehicles() != null) {
//                            vehicleArrayList.addAll(response.getVehicle().getVehicles());
//                            adapter.notifyDataSetChanged();
//                            isLoading = false;
//                            Events.FragmentActivityMessage fragmentActivityMessage = new Events.FragmentActivityMessage(vehicleArrayList.size());
//                            GlobalBus.getBus().post(fragmentActivityMessage);
//                        }
//                        progressBar.setVisibility(View.INVISIBLE);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        DialogUtils.showDialogConfirmLogin(getContext(), R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, getString(R.string.networkError));
//                        progressBar.setVisibility(View.INVISIBLE);
//                    }
//                });
//    }
//
//
//    public boolean isEmptyVehicle() {
//        return vehicleArrayList.isEmpty();
//    }
//
//    public void showDialogEditVehicle(Vehicle vehicle) {
//        Dialog dialog = DialogUtils.getDialogDefault(getContext(), R.layout.dialog_edit_vehicle, 0.9f, 0.8f);
//        TextView btnOK = dialog.findViewById(R.id.btn_ok);
//        TextView btnCancel = dialog.findViewById(R.id.btn_cancel);
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        TextView txtName = dialog.findViewById(R.id.txt_current_vehicle_name);
//        TextView txtPlate = dialog.findViewById(R.id.txt_current_vehicle_plate);
//        TextView txtResetImage = dialog.findViewById(R.id.txt_restore_vehicle_picture);
//        imgChosePicture = dialog.findViewById(R.id.img_vehicle);
//        txtResetImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (vehicle.getVehicleImage() != null) {
//                    imgChosePicture.setScaleType(ImageView.ScaleType.CENTER);
//                    isRestoreImage = true;
//                    vehicle.setTempImage(null);
//                    vehicle.setVehicleImage(null);
//                    if (vehicle.getBodyStyle() == 2) {
//                        imgChosePicture.setImageResource(R.drawable.ic_edit_oto);
//                    } else {
//                        imgChosePicture.setImageResource(R.drawable.ic_edit_moto);
//                    }
//                }
//            }
//        });
//        txtName.setText(vehicle.getVehicleName());
//        txtPlate.setText(vehicle.getLicensePlate());
//        if (vehicle.getBodyStyle() == 2) {
//            Picasso.with(getContext())
//                    .load(vehicle.getVehicleImage())
//                    .placeholder(R.drawable.ic_edit_oto)
//                    .error(R.drawable.ic_edit_oto)
//                    .into(imgChosePicture, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            imgChosePicture.setScaleType(ImageView.ScaleType.FIT_XY);
//                            vehicle.setTempImage(((BitmapDrawable) imgChosePicture.getDrawable()).getBitmap());
//                        }
//
//                        @Override
//                        public void onError() {
//                            imgChosePicture.setScaleType(ImageView.ScaleType.CENTER);
//                        }
//                    });
//        } else {
//            Picasso.with(getContext())
//                    .load(vehicle.getVehicleImage())
//                    .placeholder(R.drawable.ic_edit_moto)
//                    .error(R.drawable.ic_edit_moto)
//                    .into(imgChosePicture, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            imgChosePicture.setScaleType(ImageView.ScaleType.FIT_XY);
//                            vehicle.setTempImage(((BitmapDrawable) imgChosePicture.getDrawable()).getBitmap());
//                        }
//
//                        @Override
//                        public void onError() {
//                            imgChosePicture.setScaleType(ImageView.ScaleType.CENTER);
//                        }
//                    });
//        }
//
//        imgChosePicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogUtils.showDialogChooseImage(getContext(), R.layout.dialog_capture, new DialogUtils.DialogChooseImageListener() {
//                    @Override
//                    public void onCameraClick() {
//                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(takePicture, REQUEST_CAMERA_CODE);
//                    }
//
//                    @Override
//                    public void onGalleryClick() {
//                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                        intent.setType("image/*");
//                        startActivityForResult(intent, REQUEST_GALLERY_CODE);
//                    }
//
//                    @Override
//                    public void cancelButtonClick() {
//
//                    }
//                });
//            }
//        });
//
//        btnOK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText editNewVehicleName = dialog.findViewById(R.id.edit_new_vehicle_name);
//                EditText editNewVehiclePlate = dialog.findViewById(R.id.edit_new_vehicle_plate);
//                TextView txtUnder = dialog.findViewById(R.id.txt_restore_vehicle_picture);
//                ImageView imgChosePicture = dialog.findViewById(R.id.img_vehicle);
//                txtUnder.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//                String vehicleNewName = String.valueOf(editNewVehicleName.getText());
//                String vehicleNewPlate = String.valueOf(editNewVehiclePlate.getText());
//                Integer indexVehicle = vehicleArrayList.indexOf(vehicle);
//                Bitmap vehiclePicture = ((BitmapDrawable) imgChosePicture.getDrawable()).getBitmap();
//                if (TextUtils.isEmpty(vehicleNewName) && TextUtils.isEmpty(vehicleNewPlate) && vehiclePicture.sameAs(vehicle.getTempImage())) {
//                    dialog.dismiss();
//                } else if (TextUtils.isEmpty(vehicleNewName) && TextUtils.isEmpty(vehicleNewPlate) && isRestoreImage == false && vehiclePicture.sameAs(((BitmapDrawable) getContext().getDrawable(R.drawable.ic_edit_oto)).getBitmap()) || vehiclePicture.sameAs(((BitmapDrawable) getContext().getDrawable(R.drawable.ic_edit_moto)).getBitmap())) {
//                    dialog.dismiss();
//                } else {
//                    if (!TextUtils.isEmpty(vehicleNewName)) {
//                        vehicle.setVehicleName(vehicleNewName);
//                    }
//                    if (!TextUtils.isEmpty(vehicleNewPlate)) {
//                        vehicle.setLicensePlate(vehicleNewPlate);
//                    }
//                    if (!vehiclePicture.sameAs(vehicle.getTempImage())) {
//                        vehicle.setTempImage(vehiclePicture);
//                    }
//                    if (vehiclePicture.sameAs(((BitmapDrawable) getContext().getDrawable(R.drawable.ic_edit_oto)).getBitmap()) || vehiclePicture.sameAs(((BitmapDrawable) getContext().getDrawable(R.drawable.ic_edit_moto)).getBitmap())) {
//                        updateRequestVehicle(vehicle, indexVehicle, true);
//                        isRestoreImage = false;
//                    } else {
//                        updateRequestVehicle(vehicle, indexVehicle, false);
//                    }
//                }
//                dialog.dismiss();
//            }
//        });
//    }
//
//}
