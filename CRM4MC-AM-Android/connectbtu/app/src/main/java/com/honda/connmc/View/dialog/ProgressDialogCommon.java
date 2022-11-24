package com.honda.connmc.View.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.honda.connmc.R;

public class ProgressDialogCommon extends DialogFragment {

    public ProgressDialogCommon() {
        // Required empty public constructor
        this.setCancelable(false);
    }

    public static ProgressDialogCommon newInstance() {
        return new ProgressDialogCommon();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = null;
        if (getActivity() != null) {
            dialog = new ProgressDialog(getActivity(), android.R.style.Theme_Holo_Dialog);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setMessage(getString(R.string.progress));
            dialog.setIndeterminate(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        return dialog;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, getClass().getName());
    }
}

