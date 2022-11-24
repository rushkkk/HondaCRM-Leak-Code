package vn.co.honda.hondacrm.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.utils.ActivityUtils;
import vn.co.honda.hondacrm.utils.Constants;

/**
 * Base {@link AppCompatActivity} class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private final boolean mNeedDefaultAni = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adjustFontScale(getResources().getConfiguration());
    }

    /**
     * This method used to prevent effect font size system to font APP
     *
     * @param configuration {@link Configuration}
     */
    private void adjustFontScale(Configuration configuration) {
        if (configuration.fontScale != Constants.ONE) {
            configuration.fontScale = Constants.ONE;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            getBaseContext().getResources().updateConfiguration(configuration, metrics);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        changeStatusBarColor(getResources().getColor(R.color.colorStatusBar));
        setBackButton();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setBackButton();
    }

    private void setBackButton() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> onBackPressed());
        }
    }

    protected TextView getTextViewTitle() {
        TextView tvTitle = findViewById(R.id.tvTitle);
        return tvTitle;
    }



    protected void setTitleHeader(String titleHeader) {
        TextView tvTitle = findViewById(R.id.tvTitle);
        if (tvTitle != null) {
            tvTitle.setText(titleHeader);
        }
    }

    protected void setTitleHeader(String titleHeader, int textSize) {
        TextView tvTitle = findViewById(R.id.tvTitle);
        if (tvTitle != null) {
            tvTitle.setText(titleHeader);
            tvTitle.setTextSize(textSize);
        }
    }

    protected void setDisplayEditButton(boolean isDisplayEditButton) {
        View llEditProfile = findViewById(R.id.llEditProfile);
        if (isDisplayEditButton) {
            llEditProfile.setVisibility(View.VISIBLE);
        } else {
            llEditProfile.setVisibility(View.GONE);
        }
    }

    protected void setDisplayDoneButton(boolean isDisplayEditButton) {
        TextView tvDone = findViewById(R.id.tvDone);
        if (isDisplayEditButton) {
            tvDone.setVisibility(View.VISIBLE);
        } else {
            tvDone.setVisibility(View.GONE);
        }
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment        The fragment to be added.
     * @param addBackStack    True/false add back stack.
     */
    protected void addFragment(final int containerViewId, final Fragment fragment, final boolean addBackStack) {
        final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        if (addBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment        The fragment to be added.
     * @param addBackStack    True/false add back stack.
     */
    protected void replaceFragment(final int containerViewId, final Fragment fragment, final boolean addBackStack) {
        final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        if (addBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    /**
     * Check current fragment has been existed.
     *
     * @param fragmentID id container fragment.
     * @param fragment   Fragment to check.
     * @return true/false
     */
    protected boolean isExistCurrentFragment(int fragmentID, Fragment fragment) {
        //find fragment current
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(fragmentID);
        return currentFragment != fragment;
    }

    /**
     * Check current fragment has been existed.
     *
     * @param fragmentID id container fragment.
     * @return true/false
     */
    protected Fragment getCurrentFragment(int fragmentID) {
        //find fragment current
        FragmentManager fm = getSupportFragmentManager();
        return fm.findFragmentById(fragmentID);
    }

    protected void popBackStackFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }

    /**
     * @param context
     * @param destActivity
     * @param isFinish
     */
    protected void startActivity(Context context, Class destActivity, final boolean isFinish) {
        Intent intent = new Intent(context, destActivity);
        startActivity(intent);
        // Check to finish current Activity
        if (isFinish) {
            ((AppCompatActivity) context).finish();
        }
    }

    /**
     * @param context
     * @param destActivity
     * @param isFinish
     */
    protected void startActivity(Context context, Class destActivity, final boolean isFinish, final Bundle bundle) {
        Intent intent = new Intent(context, destActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        // Check to finish current Activity
        if (isFinish) {
            ((AppCompatActivity) context).finish();
        }
    }

    /**
     * @param context
     * @param destActivity
     */
    protected void startHomeActivity(Context context, Class destActivity) {
        Intent intent = new Intent(context, destActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ((AppCompatActivity) context).finish();
    }



    /**
     * Handle Visible view
     *
     * @param view View
     */
    protected void setViewVisible(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Handle Gone view
     *
     * @param view View
     */
    protected void setViewGone(View view) {
        if (view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * Handle Invisible view
     *
     * @param view View
     */
    protected void setViewInvisible(View view) {
        if (view.getVisibility() != View.INVISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }


    protected void resetDisplayAvatarInfor() {
        View containerNotify = findViewById(R.id.containerNotify);
        ImageView imgUserAvatar = findViewById(R.id.imgUserAvatar);
        setViewVisible(containerNotify);
        setViewVisible(imgUserAvatar);
    }

    protected void setHiddenAvatarIcon() {
        View containerNotify = findViewById(R.id.containerNotify);
        ImageView imgUserAvatar = findViewById(R.id.imgUserAvatar);
        setViewVisible(containerNotify);
        setViewGone(imgUserAvatar);
    }

    /**
     * Making notification bar transparent
     */
    protected void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    protected void setFullScreenNoStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (mNeedDefaultAni) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        if (mNeedDefaultAni) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void onBackPressed() {
        ActivityUtils.back(this);
        super.onBackPressed();
    }

}
