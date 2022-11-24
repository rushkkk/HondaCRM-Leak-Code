package vn.co.honda.hondacrm.btu.Utils;

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
import android.widget.ProgressBar;

import vn.co.honda.hondacrm.R;


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
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_load_progress);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ProgressBar progressBar = dialog.findViewById(R.id.progress_bar_login);
//        progressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.color.colorPrimary));
        dialog.show();

        return dialog;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, getClass().getName());
    }
}

