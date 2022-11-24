package vn.co.honda.hondacrm.btu.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import vn.co.honda.hondacrm.R;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class FragmentUtils {


    public enum FragmentAnimationType {TYPE_NO_ANIMATION, TYPE_ANIMATION_BACK, TYPE_ANIMATION_GO}

    public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int id, boolean isAddToBackStack, FragmentAnimationType typeAnimation) {
        if (fragmentManager == null) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (typeAnimation) {
            case TYPE_NO_ANIMATION:
                fragmentTransaction.replace(id, fragment);
                break;
            case TYPE_ANIMATION_BACK:
                fragmentTransaction.setCustomAnimations(0, R.anim.slide_left, 0, R.anim.slide_right)
                        .replace(id, fragment);
                break;
            case TYPE_ANIMATION_GO:
                fragmentTransaction.setCustomAnimations(R.anim.slide_right, 0, 0, R.anim.slide_left)
                        .replace(id, fragment);
                break;
        }
        if (!isAddToBackStack) {
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            fragmentTransaction
                    .addToBackStack(fragment.getClass().getName())
                    .commitAllowingStateLoss();
        }
    }

    public static void addFragment(FragmentManager fragmentManager, Fragment fragment, int id, boolean isAddToBackStack) {
        if (fragmentManager == null) {
            return;
        }
        if (!isAddToBackStack) {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_right, 0, 0, R.anim.slide_left)
                    .add(id, fragment)
                    .commitAllowingStateLoss();
        } else {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_right, 0, 0, R.anim.slide_left)
                    .add(id, fragment, fragment.getClass().getName())
                    .addToBackStack(fragment.getClass().getName())
                    .commitAllowingStateLoss();
        }
    }

    public static void clearFragmentByTag(final FragmentManager fm, String tag) {
        try {
            for (int i = fm.getBackStackEntryCount() - 1; i >= 0; i--) {
                String backEntry = fm.getBackStackEntryAt(i).getName();
                if (TextUtils.equals(backEntry, tag)) {
                    fm.popBackStack(backEntry, POP_BACK_STACK_INCLUSIVE);
                    break;
                }
            }
        } catch (Exception e) {
            LogUtils.d("!====Popbackstack error : " + e);
        }
    }

    public static void clearAllFragment(final FragmentManager fm) {
        try {
            for (int i = fm.getBackStackEntryCount() - 1; i >= 0; i--) {
                fm.popBackStack();
            }
        } catch (Exception e) {
            LogUtils.d("!====Popbackstack error : " + e);
        }
    }

    public static void pop(final FragmentManager fm) {
        if (fm == null) {
            return;
        }
        pop(fm, true);
    }

    public static void pop(final FragmentManager fm, final boolean isImmediate) {
        if (fm == null) {
            return;
        }
        if (isImmediate) {
            fm.popBackStackImmediate();
        } else {
            fm.popBackStack();
        }
    }

    public static void popTo(final FragmentManager fm, final Class<? extends Fragment> popClz) {
        if (fm == null) {
            return;
        }
        popTo(fm, popClz, false, true);
    }

    public static void popTo(final FragmentManager fm,
                             final Class<? extends Fragment> popClz,
                             final boolean isInclusive,
                             final boolean isImmediate) {
        if (isImmediate) {
            fm.popBackStackImmediate(popClz.getName(),
                    isInclusive ? POP_BACK_STACK_INCLUSIVE : 0);
        } else {
            fm.popBackStack(popClz.getName(),
                    isInclusive ? POP_BACK_STACK_INCLUSIVE : 0);
        }
    }
}
