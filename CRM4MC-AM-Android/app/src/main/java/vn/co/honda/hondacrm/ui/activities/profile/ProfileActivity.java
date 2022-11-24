package vn.co.honda.hondacrm.ui.activities.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.navigator.bus.Events;
import vn.co.honda.hondacrm.navigator.bus.GlobalBus;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.user.UserProfile;
import vn.co.honda.hondacrm.net.model.user.UserProfileResponse;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.vehicles.VerifyVehicle.CameraCustom;
import vn.co.honda.hondacrm.ui.activities.vehicles.VerifyVehicle.CameraPreview;
import vn.co.honda.hondacrm.ui.customview.HomeListener;
import vn.co.honda.hondacrm.ui.customview.tabs.TabProfile;
import vn.co.honda.hondacrm.ui.fragments.profile.FragmentProfileMyEventEdit;
import vn.co.honda.hondacrm.ui.fragments.profile.FragmentProfileMyFollowEdit;
import vn.co.honda.hondacrm.ui.fragments.profile.FragmentProfileMyInfor;
import vn.co.honda.hondacrm.utils.BitmapUtil;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PermissionUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;
import vn.co.honda.hondacrm.utils.Utils;

public class ProfileActivity extends BaseActivity implements HomeListener, FragmentProfileMyInfor.Status, FragmentProfileMyInfor.StatusDate, FragmentProfileMyInfor.DataUser,FragmentProfileMyInfor.StatusIdNumber,FragmentProfileMyFollowEdit.changeCountItemFollow ,FragmentProfileMyEventEdit.changeCountItemEvent{
    private ViewPager viewPager;
    String tfUserName;
    boolean status = true;
    boolean statusDate = true;
    boolean statusEdit = false;
    boolean statusId=true;
    private int REQUEST_CAMERA_CODE = 1;
    private int PICK_PHOTO_FOR_AVATAR = 2;
    int tabPage;
    Bitmap bitmap;
    private UserProfile userProfile;
    ViewPagerAdapter adapter;

    //My follow \
    int countFollow=0,countEvent=0;
    //-----------------------------------

    //set tab
    @BindView(R.id.tvTabFollow)
    TextView tvTabFollow;
    @BindView(R.id.tvTabEvent)
    TextView tvTabEvent;
    @BindView(R.id.tvTabInfo)
    TextView tvTabInfo;
    //view edit
    @BindView(R.id.llEditProfile)
    View viewEdit;

    @BindView(R.id.tvDone)
    TextView tvDone;

    @BindView(R.id.llInfoProfileEdit)
    View llInfoProfileEdit;

    @BindView(R.id.llInfoProfileDone)
    View llInfoProfileDone;
    @BindView(R.id.edit_profile_username)
    EditText tfProfileUsername;
    @BindView(R.id.tf_phoneNumber)
    EditText tf_phoneNumber;

    @BindView(R.id.tf_profile_username)
    TextView tf_profile_username;
    @BindView(R.id.tf_id_number)
    TextView tf_id_number;
    //tabprofile

    //title activity
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tabProfile)
    TabProfile tabView;

    @BindView(R.id.profile_image)
    ImageView profile_image;

    @BindView(R.id.profile_image_edit)
    ImageView profile_image_edit;

    @BindView(R.id.lb_Invalid_UserName)
    TextView lb_Invalid_UserName;

    @BindView(R.id.imgHeader)
    ImageView imgHeader;
    //API
    ApiService apiService;
    String mTypeFullAccessToken;

    private File fileUpload;

    FragmentProfileMyInfor fragmentProfileMyInfor;
    FragmentProfileMyFollowEdit fragmentProfileMyFollowEdit;
    FragmentProfileMyEventEdit fragmentProfileMyEventEdit;

    ProfileCallBackListener mProfileCallBackListener;
    private static final int PERMISSION_REQUEST_CODE = 200;
    //title dialog
    //------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        changeStatusBarColor(getResources().getColor(R.color.colorRedGrayHeader));
        ButterKnife.bind(this);
        apiService = ApiClient.getClient(ProfileActivity.this.getApplicationContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(ProfileActivity.this.getApplicationContext());
        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //tab new
        tabView.setHomeListener(this);
        tabView.updateTab(TabProfile.HomeStatus.PRODUCTS);

        fragmentProfileMyInfor = new FragmentProfileMyInfor();
        fragmentProfileMyFollowEdit = new FragmentProfileMyFollowEdit();
        fragmentProfileMyEventEdit = new FragmentProfileMyEventEdit();

        setupViewPager(viewPager);
        requestProfile();

        setTitleHeader(getString(R.string.lb_profile_user_title));
        setDisplayEditButton(true);
        setDisplayDoneButton(false);
        tfProfileUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tfProfileUsername.getText().toString().length() == 0) {
                    //tfProfileUsername.setError(getString(R.string.Invalid_Username));
                    lb_Invalid_UserName.setVisibility(View.VISIBLE);
                }else {
                    lb_Invalid_UserName.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(fragmentProfileMyInfor, getString(R.string.lb_profile_tab_infor_title));
        adapter.addFragment(fragmentProfileMyFollowEdit, getString(R.string.lb_profile_tab_follow_title)+(countFollow+1));
        adapter.addFragment(fragmentProfileMyEventEdit, getString(R.string.lb_profile_tab_event_title));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
    }

    private void requestProfile() {
        showProgressDialog();
        apiService.getUserProfile(
                mTypeFullAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<UserProfileResponse>() {
                    @Override
                    public void onSuccess(UserProfileResponse response) {
                        userProfile = response.getUserProfile();
                        if (userProfile != null) {
                            tf_profile_username.setText(userProfile.getName());
                            tfProfileUsername.setText(userProfile.getName());
                            tf_id_number.setText("+ (84) "+userProfile.getPhone().substring(1,userProfile.getPhone().length()));
                            tf_phoneNumber.setText("+ (84) "+userProfile.getPhone().substring(1,userProfile.getPhone().length()));
                            Picasso.with(ProfileActivity.this)
                                    .load(userProfile.getAvatar())
                                    .placeholder(R.drawable.image_user_profile_default)
                                    .error(R.drawable.image_user_profile_default)
                                    .into(profile_image, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            userProfile.setTempImage(((BitmapDrawable) profile_image.getDrawable()).getBitmap());
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                            Picasso.with(ProfileActivity.this)
                                    .load(userProfile.getAvatar())
                                    .placeholder(R.drawable.image_user_profile_default)
                                    .error(R.drawable.image_user_profile_default)
                                    .into(imgHeader);
                            URL imageurl = null;
                            try {

                                imageurl = new URL(userProfile.getAvatar());

                                bitmap = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressDialog();
                        DialogUtils.showDialogConfirmLogin(ProfileActivity.this, R.layout.dialog_notification_erro_network, 0.9f, 0.2f, getString(R.string.networkError));
                    }
                });
    }

    /**
     * This method used to send callback to Fragment.
     *
     * @param profileCallBack CallBack.
     */
    public void setProfileCallBack(ProfileCallBackListener profileCallBack) {
        mProfileCallBackListener = profileCallBack;
    }


    public void requestUpdateProfile(UserProfile userProfile) {
        if (((BitmapDrawable) profile_image.getDrawable()).getBitmap().sameAs(userProfile.getTempImage())) {
            fileUpload = null;
        }
        showProgressDialog();
        RequestBody requestFile;
        MultipartBody.Part body = null;
        if (fileUpload != null) {
            requestFile =
                    RequestBody.create(MediaType.parse("image/png"), fileUpload);
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("avatar", fileUpload.getName(), requestFile);
        }
        RequestBody userName = RequestBody.create(
                MediaType.parse("text/plain"),
                userProfile.getName());

        RequestBody address = RequestBody.create(
                MediaType.parse("text/plain"),
                userProfile.getAddress());

        RequestBody date = RequestBody.create(
                MediaType.parse("text/plain"),
                userProfile.getDateOfBirth());
        RequestBody job = RequestBody.create(
                MediaType.parse("text/plain"),
                userProfile.getJob());
        RequestBody idNumber = RequestBody.create(
                MediaType.parse("text/plain"),
                userProfile.getIdNumber());

        RequestBody email = RequestBody.create(
                MediaType.parse("text/plain"),
                userProfile.getEmail());

        apiService.updateProfile(
                mTypeFullAccessToken,
                body,
                userName,
                address,
                Integer.parseInt(userProfile.getGender()),
                date,
                job,
                idNumber,
                email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<UserProfileResponse>() {
                    @Override
                    public void onSuccess(UserProfileResponse response) {
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressDialog();
                        DialogUtils.showDialogConfirmLogin(ProfileActivity.this, R.layout.dialog_notification_erro_network, 0.9f, 0.2f, getString(R.string.networkError));
                    }
                });
    }

    private void hideProgressDialog() {
        DialogUtils.hideDialogLoadProgress();
    }

    private void showProgressDialog() {
        DialogUtils.showDialogLoadProgress(ProfileActivity.this);
    }

    @OnClick(R.id.llEditProfile)
    public void onEditClick() {
        if (userProfile == null) {
            DialogUtils.showDialogConfirmLogin(ProfileActivity.this, R.layout.dialog_notification_erro_network, 0.9f, 0.2f, getString(R.string.networkError));
            return;
        }

        statusEdit = true;
        // tf_phoneNumber.setText(userProfile.getPhone());
        setDisplayDoneButton(true);
        setDisplayEditButton(false);
        //set title toolbar
        tvTitle.setText(getString(R.string.Edit_User_Profile));
        Events.ActivityFragmentMessage activityFragmentMessage;

        switch (viewPager.getCurrentItem()) {
            case Constants.TAB_MY_INFO:
                profile_image_edit.setVisibility(View.VISIBLE);
                llInfoProfileEdit.setVisibility(View.VISIBLE);
                llInfoProfileDone.setVisibility(View.INVISIBLE);
                profile_image.setOnClickListener(v -> {
                    if (statusEdit && (viewPager.getCurrentItem()==Constants.TAB_MY_INFO)) {
                        DialogUtils.showDialogChooseImage(ProfileActivity.this, R.layout.dialog_capture, new DialogUtils.DialogChooseImageListener() {
                            @Override
                            public void onCameraClick() {
                                if(ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) == PackageManager
                                        .PERMISSION_GRANTED) {
                                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(takePicture, REQUEST_CAMERA_CODE);
                                }else{
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        if (PermissionUtils.neverAskAgainSelected(ProfileActivity.this, Manifest.permission.CAMERA)) {
                                            PermissionUtils.displayNeverAskAgainDialog(ProfileActivity.this);
                                        } else {
                                            requestPermission();
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onGalleryClick() {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
                            }
                            @Override
                            public void cancelButtonClick() {
                            }
                        });

                    }
                });
                activityFragmentMessage = new Events.ActivityFragmentMessage("0", 0);
                tabPage = 0;
                break;
            case Constants.TAB_MY_FOLLOW:
                tabPage = 1;
                activityFragmentMessage = new Events.ActivityFragmentMessage("1", 0);
                break;
            case Constants.TAB_MY_EVENT:
                tabPage = 2;
                activityFragmentMessage = new Events.ActivityFragmentMessage("2", 0);
                break;
            default:
                tabPage = 0;

                activityFragmentMessage = new Events.ActivityFragmentMessage("0", 0);
                break;
        }
        GlobalBus.getBus().post(activityFragmentMessage);
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }
    @OnClick(R.id.tvDone)
    public void onDoneEditClick() {
        if (statusDate && status && tfProfileUsername.getText().toString().length() > 0 && statusId) {
            setDisplayEditButton(true);
            setDisplayDoneButton(false);
            tvTitle.setText(getString(R.string.User_Profile));
            statusEdit = false;
        } else {
            if (tfProfileUsername.getText().toString().length() == 0) {
                //tfProfileUsername.setError(getString(R.string.Invalid_Username));
                lb_Invalid_UserName.setVisibility(View.VISIBLE);
                return;
            }
        }
        Utils.hideKeyboard(this);

        //set change text usename profile
        Events.ActivityFragmentMessage activityFragmentMessage;
        switch (viewPager.getCurrentItem()) {
            case Constants.TAB_MY_INFO:
                tfUserName = tfProfileUsername.getText().toString();
                if (tfProfileUsername.getText().toString().length() == 0) {
                    // tfProfileUsername.setError(getString(R.string.Invalid_Username));
                    lb_Invalid_UserName.setVisibility(View.VISIBLE);
                }
                if (statusDate && status && tfProfileUsername.getText().toString().length() > 0&&statusId) {
                    llInfoProfileEdit.setVisibility(View.INVISIBLE);
                    llInfoProfileDone.setVisibility(View.VISIBLE);
                    profile_image_edit.setVisibility(View.INVISIBLE);
                    if (tfUserName != null && tf_profile_username != null) {

                        tf_profile_username.setText(tfProfileUsername.getText().toString());
                        statusEdit = false;
                    }
                }
                activityFragmentMessage = new Events.ActivityFragmentMessage("0", 1);

                break;
            case Constants.TAB_MY_FOLLOW:

                activityFragmentMessage = new Events.ActivityFragmentMessage("1", 1);
                break;
            case Constants.TAB_MY_EVENT:

                activityFragmentMessage = new Events.ActivityFragmentMessage("2", 1);
                break;
            default:
                activityFragmentMessage = new Events.ActivityFragmentMessage("0", 1);
                break;
        }
        GlobalBus.getBus().post(activityFragmentMessage);
    }

    //check page
    public void checkPage() {
        if (tabPage == 0) {
            tvTabInfo.setSelected(true);
            tvTabFollow.setSelected(false);
            tvTabEvent.setSelected(false);
        }
        if (tabPage == 1) {
            tvTabInfo.setSelected(false);
            tvTabFollow.setSelected(true);
            tvTabEvent.setSelected(false);
        }
        if (tabPage == 2) {
            tvTabInfo.setSelected(false);
            tvTabFollow.setSelected(false);
            tvTabEvent.setSelected(true);
        }
    }

    @Override
    public void showHomeFragment() {

        if (statusEdit) {
            checkPage();
        } else {
            Utils.hideKeyboard(this);
            viewPager.setCurrentItem(2);
            tvTabEvent.setSelected(true);
        }
    }

    @Override
    public void showProductsFragment() {
        if (statusEdit) {
            checkPage();
        } else {
            Utils.hideKeyboard(this);
            viewPager.setCurrentItem(0);
            tvTabInfo.setSelected(true);
        }
    }

    @Override
    public void showConnectedFragment() {
    }

    @Override
    public void showVehiclesFragment() {
        if (statusEdit) {
            checkPage();
        } else {
            Utils.hideKeyboard(this);
            viewPager.setCurrentItem(1);
            tvTabFollow.setSelected(true);
        }


    }

    @Override
    public void showDealers() {
    }

    @Override
    public void setTextHeader(int status) {
    }

    @Override
    public void ChangeStatus(boolean changeSattus) {
        this.status = changeSattus;
    }

    @Override
    public void ChangeStatusDate(boolean changeSattus) {
        this.statusDate = changeSattus;
    }

    //request update profile
    @Override
    public void SendDataUser(UserProfile userProfile) {
        tf_profile_username.setText(tfProfileUsername.getText().toString());
        userProfile.setName(tfProfileUsername.getText().toString());
        requestUpdateProfile(userProfile);
    }

    @Override
    public void ChangeStatusId(boolean changeSattus) {
        this.statusId=changeSattus;

    }
    //update count follow
    @Override
    public void sendCountFollow(int count) {
        countFollow=count;
        tvTabFollow.setText(getString(R.string.tab_profile_follow)+"("+count+")");
    }
    //update count event
    @Override
    public void sendCountEvent(int count) {
        countEvent=count;
        tvTabEvent.setText(getString(R.string.tab_profile_event)+"("+count+")");
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        profile_image.setImageBitmap(photo);
                        //create a file to write bitmap data
                        fileUpload = BitmapUtil.getFileFromBitmap(ProfileActivity.this, photo);
                        Drawable d = getDrawable(R.drawable.image_user_profile_default);
                        if (photo != null && d != null && photo.sameAs(((BitmapDrawable) d).getBitmap())) {
                            Log.d("logimage", "onActivityResult: true ");

                        } else {
                            Log.d("logimage", "onActivityResult: false ");
                            //Drawable background = new BitmapDrawable(getResources(), photo);
                            imgHeader.setImageBitmap(photo);
                        }
                    }
                }
                break;
            case 2:
                if(resultCode == RESULT_OK)
                {
                    if (data == null) {
                        //Display an error
                        return;
                    }
                    Uri selectedImageUri = data.getData();
                    imgHeader.setImageURI(selectedImageUri);
                    profile_image.setImageURI(selectedImageUri);
                    try {
                        if (data.getData() != null) {
                            InputStream inputStream = getBaseContext().getContentResolver().openInputStream(data.getData());
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            fileUpload = BitmapUtil.getFileFromBitmap(ProfileActivity.this, bitmap);
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
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
