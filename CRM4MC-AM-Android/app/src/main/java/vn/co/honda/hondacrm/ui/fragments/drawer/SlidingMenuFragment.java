package vn.co.honda.hondacrm.ui.fragments.drawer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.user.UserProfile;
import vn.co.honda.hondacrm.net.model.user.UserProfileResponse;
import vn.co.honda.hondacrm.ui.activities.login.model.User;
import vn.co.honda.hondacrm.ui.activities.login.presenter.SaveSharedPreference;
import vn.co.honda.hondacrm.ui.activities.profile.ProfileActivity;
import vn.co.honda.hondacrm.ui.adapters.drawer.Menu;
import vn.co.honda.hondacrm.ui.adapters.drawer.MenuRecyclerViewAdapter;
import vn.co.honda.hondacrm.ui.fragments.common.BaseFragment;
import vn.co.honda.hondacrm.ui.fragments.drawer.interfaces.IDrawerListener;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.PrefUtils;

/**
 * Created by CuongNV31
 */
public class SlidingMenuFragment extends BaseFragment implements View.OnClickListener {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;

    private CircleImageView avatar;
    private ImageButton btnNextProfile;

    RecyclerView recyclerView;
    MenuRecyclerViewAdapter adapter;
    ArrayList<Menu> menuArrayList;

    ImageButton btnSocialFacebook;
    ImageButton btnSocialInstagram;
    ImageButton btnSocialTwitter;
    ImageButton btnSocialYoutube;

    View viewHeader;

    TextView tvUserName;

    @BindView(R.id.profile_image)
    ImageView profile_image;
    @BindView(R.id.image_bg_backgroud)
    ImageView image_bg_backgroud;
    //API
    ApiService apiService;
    String mTypeFullAccessToken;
    private UserProfile userApi;

    @Override
    public int getLayout() {
        return 0;
    }

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiService = ApiClient.getClient(getActivity().getApplicationContext()).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(getActivity().getApplicationContext());
    }

    //Get profile api
    @SuppressLint("SetTextI18n")
    private void requestProfile() {
        if (tvUserName != null) {
            tvUserName.setText("Hello, " + SaveSharedPreference.getFullname(mContext));
        }
        if (!SaveSharedPreference.getAvatar(mContext).isEmpty()) {
            Picasso.with(getActivity())
                    .load(SaveSharedPreference.getAvatar(mContext))
                    .placeholder(R.drawable.image_user_profile_default)
                    .error(R.drawable.image_user_profile_default)
                    .into(profile_image);
            Picasso.with(getActivity())
                    .load(SaveSharedPreference.getAvatar(mContext))
                    .placeholder(R.drawable.image_user_profile_default)
                    .error(R.drawable.image_user_profile_default)
                    .into(image_bg_backgroud);
        } else {
            profile_image.setImageResource(R.drawable.image_user_profile_default);
            image_bg_backgroud.setImageResource(R.drawable.image_user_profile_default);
        }
    }

    //Get profile api
    private void requestProfileForServer() {
        apiService.getUserProfile(
                mTypeFullAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<UserProfileResponse>() {
                    @Override
                    public void onSuccess(UserProfileResponse response) {
                        userApi = response.getUserProfile();
                        if (userApi != null) {
                            tvUserName.setText("Hello ," + userApi.getName());
                        }
                        Picasso.with(getActivity())
                                .load(userApi.getAvatar())
                                .placeholder(R.drawable.image_user_profile_default)
                                .error(R.drawable.image_user_profile_default)
                                .into(profile_image);
                        Picasso.with(getActivity())
                                .load(userApi.getAvatar())
                                .placeholder(R.drawable.image_user_profile_default)
                                .error(R.drawable.image_user_profile_default)
                                .into(image_bg_backgroud);
                        hideProgressDialog();
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


    public IDrawerListener mIDrawerListener;
    private String[] contentList;

    private final int[] contentIcon = new int[]{
            R.drawable.selector_menu_faq_item,
            R.drawable.selector_menu_tutorials_item,
            R.drawable.selector_menu_privacypolicy_item,
            R.drawable.selector_menu_terms_item,
            R.drawable.selector_menu_change_language_item,
            R.drawable.selector_menu_rateourapp_item,
            R.drawable.selector_menu_logout_item
    };

    @Override
    public void onResume() {
        super.onResume();
        if (!SaveSharedPreference.getAvatar(mContext).isEmpty()) {
            requestProfile();
        } else {
            requestProfileForServer();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navi_drawer, container, false);
        ButterKnife.bind(this, layout);
        recyclerView = layout.findViewById(R.id.rv_profile);
        tvUserName = layout.findViewById(R.id.tvUserName);

        //set image
        avatar = layout.findViewById(R.id.profile_image);

        menuArrayList = new ArrayList<>();

        if (getActivity() != null) {
            contentList = getActivity().getResources().getStringArray(R.array.menu_list);
            for (int index = 0; index < Constants.MENU_ITEMS; index++) {
                menuArrayList.add(new Menu(index, contentList[index], contentIcon[index]));
            }
        }

        adapter = new MenuRecyclerViewAdapter(menuArrayList, getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getActivity(), R.drawable.divider_recyler_view));
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter.setOnItemClickListener((view, position) ->
                moveToPage(menuArrayList.get(position)));
        adapter.setOnItemClickListener(new MenuRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                moveToPage(menuArrayList.get(position));
            }
        });

        viewHeader = layout.findViewById(R.id.nav_header_container);
        btnSocialFacebook = layout.findViewById(R.id.btnSocialFacebook);
        btnSocialInstagram = layout.findViewById(R.id.btnSocialInstagram);
        btnSocialTwitter = layout.findViewById(R.id.btnSocialTwitter);
        btnSocialYoutube = layout.findViewById(R.id.btnSocialYoutube);

        btnSocialFacebook.setOnClickListener(this);
        btnSocialInstagram.setOnClickListener(this);
        btnSocialTwitter.setOnClickListener(this);
        btnSocialYoutube.setOnClickListener(this);
        viewHeader.setOnClickListener(this);

        if (!SaveSharedPreference.getFullname(mContext).isEmpty()) {
            requestProfile();
        } else {
            requestProfileForServer();
        }

        return layout;
    }

    private void moveToPage(Menu menu) {
        mDrawerLayout.closeDrawer(containerView);
        new Handler().post(() ->
                mIDrawerListener.onItemMenuClick(menu));
    }

    public void setDrawerListener(IDrawerListener iDrawerListener) {
        mIDrawerListener = iDrawerListener;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        if (getActivity() != null) {
            containerView = getActivity().findViewById(fragmentId);
        }
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (getActivity() != null) {
                    getActivity().invalidateOptionsMenu();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (getActivity() != null) {
                    getActivity().invalidateOptionsMenu();
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
//        mDrawerLayout.post(() -> mDrawerToggle.syncState());
        mDrawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        }, 2000);

    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        if (getActivity() != null) {
            containerView = getActivity().findViewById(fragmentId);
        }
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (getActivity() != null) {
                    getActivity().invalidateOptionsMenu();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (getActivity() != null) {
                    getActivity().invalidateOptionsMenu();
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(() -> mDrawerToggle.syncState());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSocialFacebook:
                mIDrawerListener.onSocialNetWorkClick(Constants.FACE_BOOK);
                mDrawerLayout.closeDrawer(containerView);
                break;
            case R.id.btnSocialInstagram:
                mIDrawerListener.onSocialNetWorkClick(Constants.INSTAGRAM);
                mDrawerLayout.closeDrawer(containerView);
                break;
            case R.id.btnSocialTwitter:
                mIDrawerListener.onSocialNetWorkClick(Constants.TWITTER);
                mDrawerLayout.closeDrawer(containerView);
                break;
            case R.id.btnSocialYoutube:
                mIDrawerListener.onSocialNetWorkClick(Constants.YOUTUBE);
                mDrawerLayout.closeDrawer(containerView);
                break;
            case R.id.nav_header_container:
                mDrawerLayout.closeDrawer(containerView);
                mIDrawerListener.onHeaderClick();
                break;
        }
    }

    public void close() {
        mDrawerLayout.closeDrawer(containerView);
    }

    public void open() {
        mDrawerLayout.openDrawer(containerView);
    }

    public boolean isOpen() {
        return mDrawerLayout.isDrawerOpen(containerView);
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }
}
