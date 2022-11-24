package vn.co.honda.hondacrm.ui.activities.home;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.navigator.bus.GlobalBus;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.user.UserProfile;
import vn.co.honda.hondacrm.net.model.user.UserProfileResponse;
import vn.co.honda.hondacrm.ui.activities.BaseActivity;
import vn.co.honda.hondacrm.ui.activities.login.model.User;
import vn.co.honda.hondacrm.ui.activities.login.presenter.SaveSharedPreference;
import vn.co.honda.hondacrm.ui.activities.login.ui.LoginActivity;
import vn.co.honda.hondacrm.ui.activities.login.ui.WelcomeActivity;
import vn.co.honda.hondacrm.ui.activities.notification.views.NotificationDialogImpl;
import vn.co.honda.hondacrm.ui.activities.policy.PolicyActivity;
import vn.co.honda.hondacrm.ui.activities.policy.TermsActivity;
import vn.co.honda.hondacrm.ui.activities.profile.ProfileActivity;
import vn.co.honda.hondacrm.ui.adapters.common.ViewPagerAdapter;
import vn.co.honda.hondacrm.ui.adapters.drawer.Menu;
import vn.co.honda.hondacrm.ui.customview.ViewPagerNoSwipe;
import vn.co.honda.hondacrm.ui.fragments.connected.views.ConnectedContainerFragment;
import vn.co.honda.hondacrm.ui.fragments.connected.views.VehicleStatusFragment;
import vn.co.honda.hondacrm.ui.fragments.drawer.SlidingMenuFragment;
import vn.co.honda.hondacrm.ui.fragments.drawer.interfaces.IDrawerListener;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.PrefUtils;

import static vn.co.honda.hondacrm.utils.Constants.TAB_CONNECTED;
import static vn.co.honda.hondacrm.utils.Constants.TAB_DEALERS;
import static vn.co.honda.hondacrm.utils.Constants.TYPE_VIEWPAGE;
import static vn.co.honda.hondacrm.utils.Constants.UPDATE_CURRENT_VIEWPAGER;

public class MainActivity extends BaseActivity implements IDrawerListener {


    // Offset page limit
    private static final int LIMIT_OFF_SET = 4;

    // Image Tab Icons
    private static final int[] TAB_ICONS = {
            R.drawable.selector_tab_products,
            R.drawable.selector_tab_vehicles,
            R.drawable.selector_tab_home,
            R.drawable.selector_tab_dealers,
            R.drawable.selector_tab_connects,
    };

    // milliseconds, desired time passed between 2 back press
    private static final int TIME_INTERAL = 2000;
    private long mBackPressed;


    @BindView(R.id.imgMenu)
    ImageView imgMenu;

    @BindView(R.id.imgUserAvatar)
    CircleImageView circleImageViewAvatar;

    @BindView(R.id.containerNotify)
    View containerNotify;

    @BindView(R.id.tabsBottom)
    TabLayout tabsBottom;

    @BindView(R.id.viewpager)
    ViewPagerNoSwipe viewPagerNoSwipe;

    @BindView(R.id.txt_numder_notify)
    TextView txtNumberNotify;

    private SlidingMenuFragment fragmentDrawer;

    private ViewPagerAdapter adapter;

    private String[] tabTitles;

    private String typeEdit = Constants.EDIT;


    //API
    ApiService apiService;
    String mTypeFullAccessToken;
    private UserProfile userApi;
    private NotificationDialogImpl iNotificationDialog;
    private boolean isShow = false;

    private static TextView tvTitle;

    private String booking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeStatusBarColor(Color.GRAY);
        ButterKnife.bind(this);
        GlobalBus.getBus().register(this);
        iNotificationDialog = new NotificationDialogImpl(this);

        //api
        apiService = ApiClient.getClient(MainActivity.this.getApplicationContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(MainActivity.this.getApplicationContext());
        fragmentDrawer = (SlidingMenuFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        if (fragmentDrawer != null) {
            fragmentDrawer.setDrawerListener(this);
            fragmentDrawer.setUp(R.id.fragment_navigation_drawer, findViewById(R.id.drawer_layout));
            fragmentDrawer.getDrawerLayout().setStatusBarBackgroundColor(Color.WHITE);
        }
        setupViewPager(viewPagerNoSwipe);
        viewPagerNoSwipe.setCurrentItem(Constants.TAB_HOME);
        tabTitles = getResources().getStringArray(R.array.tabs);
        setupTabIcons();
        tabsBottom.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case Constants.TAB_PRODUCTS:
                        setTitleHeader(getString(R.string.tab_bottom_product));
                        break;
                    case Constants.TAB_VEHICLES:
                        setTitleHeader(getString(R.string.lb_my_vehicle_title));
                        break;
                    case Constants.TAB_HOME:
                        setTitleHeader(getString(R.string.tab_bottom_home));
                        break;
                    case Constants.TAB_DEALERS:
                        setTitleHeader(getString(R.string.tab_bottom_dealer));
                        break;
                    case Constants.TAB_CONNECTED:
                        try {
                            setTitleHeader(ConnectedContainerFragment.getTitleCurrent());
                        } catch (Exception e) {
                            setTitleHeader(getString(R.string.lb_connected_title));
                        }
                        resetDisplayAvatarInfor();
                        if (mOnClickItemListener != null) {
                            mOnClickItemListener.onItemClick();
                        }
                        break;
                    default:
                        setTitleHeader(getString(R.string.tab_bottom_home));
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tvTitle = getTextViewTitle();
        // Check to switch tabs (Home/VehicleInformation)
        if (SaveSharedPreference.getCountLogin(getApplicationContext()) == Constants.ZERO) {
            viewPagerNoSwipe.setCurrentItem(Constants.TAB_VEHICLES);
            SaveSharedPreference.setCountLogin(getApplicationContext(), Constants.FIRST);
        } else {
            viewPagerNoSwipe.setCurrentItem(Constants.TAB_HOME);
        }
        booking=getIntent().getStringExtra("booking");
        if(TextUtils.equals(booking, "booking")) {
            viewPagerNoSwipe.setCurrentItem(Constants.TAB_DEALERS);
        }
    }


    private OnClickItemListener mOnClickItemListener;

    public interface OnClickItemListener {
        void onItemClick();
    }

    public void setOnClickItemListener(OnClickItemListener listener) {
        this.mOnClickItemListener = listener;
    }

    private OnAboutDataReceivedListener mAboutDataListener;

    public interface OnAboutDataReceivedListener {
        void onDataReceived(boolean key);
    }

    public void setAboutDataListener(OnAboutDataReceivedListener listener) {
        this.mAboutDataListener = listener;
    }


    // set tab connected
    public void setupConnectedFragment(Boolean isWait) {
        viewPagerNoSwipe.setCurrentItem(TAB_CONNECTED);
        tabTitles = getResources().getStringArray(R.array.tabs);
        setupTabIcons();
        if (mAboutDataListener != null) {
            mAboutDataListener.onDataReceived(isWait);
        }

    }

    public void setUpDealerFragment() {
        viewPagerNoSwipe.setCurrentItem(Constants.TAB_DEALERS);
        tabTitles = getResources().getStringArray(R.array.tabs);
        setupTabIcons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SaveSharedPreference.getAvatar(this).isEmpty()) {
            requestProfile();
        } else {
            requestProfileAPI();
        }
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(UPDATE_CURRENT_VIEWPAGER);
        registerReceiver(broadcastReceiver, mIntentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    private IntentFilter mIntentFilter;

    //Get profile local
    private void requestProfile() {
        Picasso.with(MainActivity.this)
                .load(SaveSharedPreference.getAvatar(this))
                .placeholder(R.drawable.image_user_profile_default)
                .error(R.drawable.image_user_profile_default)
                .into(circleImageViewAvatar);
    }

    //Get profile api
    private void requestProfileAPI() {
        apiService.getUserProfile(
                mTypeFullAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<UserProfileResponse>() {
                    @Override
                    public void onSuccess(UserProfileResponse response) {
                        userApi = response.getUserProfile();
                        if (userApi != null) {
                            Picasso.with(MainActivity.this)
                                    .load(userApi.getAvatar())
                                    .placeholder(R.drawable.image_user_profile_default)
                                    .error(R.drawable.image_user_profile_default)
                                    .into(circleImageViewAvatar);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    private void showProgressDialog() {
    }

    private void hideProgressDialog() {
    }


    public void showPopup() {
        isShow = true;
        iNotificationDialog.showPopupNotification();
    }

    @OnClick(R.id.imgMenu)
    public void onMenuClick() {
        fragmentDrawer.open();
    }

    @OnClick(R.id.imgUserAvatar)
    public void onUserAvatarClick() {
        startActivity(MainActivity.this, ProfileActivity.class, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.containerNotify)
    public void onNotifyClick() {
        showPopup();
    }


//    @OnClick(R.id.imgEditVehicle)
//    public void onEditVehicleClick() {
//        typeEdit = Constants.DONE;
//        tvDoneVehicle.setVisibility(View.VISIBLE);
//        imgEditVehicle.setVisibility(View.INVISIBLE);
//        Events.ActivityFragmentMessage activityFragmentMessage = new Events.ActivityFragmentMessage(Constants.EDIT);
//        GlobalBus.getBus().post(activityFragmentMessage);
//    }
//
//    @OnClick(R.id.tvDoneVehicle)
//    public void onDoneVehicleClick() {
//        typeEdit = Constants.EDIT;
//        tvDoneVehicle.setVisibility(View.INVISIBLE);
//        imgEditVehicle.setVisibility(View.VISIBLE);
//        Events.ActivityFragmentMessage activityFragmentMessage = new Events.ActivityFragmentMessage(Constants.DONE);
//        GlobalBus.getBus().post(activityFragmentMessage);
//    }

    /**
     * This method using to unit tab item
     *
     * @param index Tab item
     */
    private void createViewTab(final int index) {
        View tabMovie = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView title = tabMovie.findViewById(R.id.title);
        title.setText(tabTitles[index]);

        ImageView icon = tabMovie.findViewById(R.id.icon);
        icon.setImageResource(TAB_ICONS[index]);
        TabLayout.Tab tab = tabsBottom.getTabAt(index);
        if (tab != null) {
            tab.setCustomView(tabMovie);
        }
    }

    /**
     * This method using to bind Tab number
     */
    private void setupTabIcons() {
        for (int index = Constants.ZERO; index < Constants.TAB_NUMBERS; index++) {
            createViewTab(index);
        }
    }

    /**
     * This method using to set data viewpager
     *
     * @param viewPager ViewPager
     */
    private void setupViewPager(final ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(LIMIT_OFF_SET);
        tabsBottom.setupWithViewPager(viewPagerNoSwipe);
    }


    private void goToFacebookPage(String id) {
        try {
            //ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/453582431466467"));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + id));
            startActivity(intent);
        }
    }

    private void goToInstagramPage(String id) {
        //Uri uri = Uri.parse("http://instagram.com/explore/tags/hondavietnam/");
        Uri uri = Uri.parse("https://www.instagram.com/" + id);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com")));
        }

    }

    private void goToTwitterPage(String id) {
        Intent intent;
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=" + id));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/cristiano"));
        }
        startActivity(intent);
    }

    private void goToYoutubePage(String id) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/channel/" + id));
        intent.setPackage("com.google.android.youtube");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if (fragmentDrawer.isOpen()) {
            fragmentDrawer.close();
        } else {
            if (iNotificationDialog != null && isShow) {
                iNotificationDialog.closePupopNotification();
                isShow = false;
            } else {
                if(TextUtils.equals(booking, "booking")) {
                    finish();
                } else {
                    if (viewPagerNoSwipe.getCurrentItem() != Constants.TAB_HOME) {
                        viewPagerNoSwipe.setCurrentItem(Constants.TAB_HOME);
                    } else {
                        if (mBackPressed + TIME_INTERAL > System.currentTimeMillis()) {
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.msg_2double_click), Toast.LENGTH_SHORT).show();
                        }
                        mBackPressed = System.currentTimeMillis();

                    }
                }
            }


        }
    }


    @Override
    public void onSocialNetWorkClick(int type) {
        switch (type) {
            case Constants.FACE_BOOK:
                goToFacebookPage("453582431466467");
                break;
            case Constants.INSTAGRAM:
                goToInstagramPage("cristiano");
                break;
            case Constants.TWITTER:
                goToTwitterPage("155659213");
                break;
            case Constants.YOUTUBE:
                goToYoutubePage("UC8kEfAobphtFXE43PLB7ZtA");
                break;
        }
    }

    @Override
    public void onItemMenuClick(Menu menu) {
        switch (menu.getId()) {
            case Constants.MENU_FAQ:
                break;
            case Constants.MENU_TUTORIAL:
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.KEY_TUTORIAL, true);
                startActivity(MainActivity.this, WelcomeActivity.class, false, bundle);
                break;
            case Constants.MENU_POLICY:
                startActivity(MainActivity.this, PolicyActivity.class, false);
                break;
            case Constants.MENU_TEMRS:
                startActivity(MainActivity.this, TermsActivity.class, false);
                break;
            case Constants.MENU_LANGUAGE:
                break;
            case Constants.MENU_RATE:
                break;
            case Constants.MENU_LOGOUT:
                SaveSharedPreference.setUser(getApplicationContext(), new User("", ""));
                PrefUtils.storeAccessTokenKey(MainActivity.this, null);
                startActivity(MainActivity.this, LoginActivity.class, true);
                break;
        }
    }

    @Override
    public void onHeaderClick() {
        startActivity(MainActivity.this, ProfileActivity.class, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(UPDATE_CURRENT_VIEWPAGER)) {
                int type = intent.getIntExtra(TYPE_VIEWPAGE, -1);
                if (type != -1) {
                    switch (type) {
                        case TAB_DEALERS:
                            setUpDealerFragment();
                            break;
                        case TAB_CONNECTED:
                            Boolean b = intent.getBooleanExtra(VehicleStatusFragment.KEY_WAITING_BTU_DISCONNECT, false);
                            setupConnectedFragment(b);
                            break;
                        default:
                            break;
                    }

                }
            }

        }
    };

    public static void updateTitle(String tittle) {
        tvTitle.setText(tittle);
    }


}
