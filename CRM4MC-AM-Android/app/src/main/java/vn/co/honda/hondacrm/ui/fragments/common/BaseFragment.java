package vn.co.honda.hondacrm.ui.fragments.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.home.MainActivity;
import vn.co.honda.hondacrm.utils.LogUtils;

/**
 * @author CuongNV31
 */
public abstract class BaseFragment extends Fragment implements IBaseFragment {

    private String mTag;
    private Unbinder mUnbinder;

    public abstract int getLayout();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null && getActivity() instanceof MainActivity)
            if (getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.fragmentContainer) != null) {
                mTag = getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentContainer)
                        .getClass().getSimpleName();
            }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View rootLayout = getLayoutInflater().inflate(getLayout(), container, false);
        mUnbinder = ButterKnife.bind(this, rootLayout);
        return rootLayout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void log(String message) {
        LogUtils.i(mTag, message);
    }

    @Override
    public void logError(String error) {
        LogUtils.e(mTag, error);
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message An string representing a message to be shown.
     */
    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    protected boolean isExistCurrentFragment(int fragmentID, Fragment fragment) {
        //find fragment current
        if (getActivity() != null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Fragment currentFragment = fm.findFragmentById(fragmentID);
            return currentFragment != fragment;
        }
        return false;
    }
}