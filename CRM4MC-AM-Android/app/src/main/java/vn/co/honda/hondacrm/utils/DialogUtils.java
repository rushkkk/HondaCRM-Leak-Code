package vn.co.honda.hondacrm.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import vn.co.honda.hondacrm.R;

public class DialogUtils {

    public static Dialog dialog;
    public static Dialog progressDialog;

    // Handel callback button click
    public interface DialogListener {
        void okButtonClick(Dialog dialog);

        void cancelButtonClick();
    }

    // Handel callback button click
    public interface DialogChooseImageListener {
        void onCameraClick();

        void onGalleryClick();

        void cancelButtonClick();
    }

    // Handel callback button click dialog help
    public interface DialogHelpListener {
        void onFindAgency();
    }


    public static void showDialogConfirm(Context context, int layoutRes, float width, float height, DialogListener dialogListener) {
        Dialog dialog = getDialogDefault(context, layoutRes, width, height);
        TextView txtOK = dialog.findViewById(R.id.btn_ok);
        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogListener.okButtonClick(dialog);
            }
        });
    }

    public static void showDialogConfirmLogin(Context context, int layoutRes, float width, float height, String title) {
        Dialog dialog = getDialogDefault(context, layoutRes, width, height);
        TextView txtOK = dialog.findViewById(R.id.btn_ok);
        TextView txtTitle = dialog.findViewById(R.id.txt_title_confirm);
        txtTitle.setText(title);
        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }

    public static void showDialogDefault(Context context, int layoutRes, float width, float height, String title, DialogListener dialogListener) {
        Dialog dialog = getDialogDefault(context, layoutRes, width, height);
        TextView txtOK = dialog.findViewById(R.id.btn_ok);
        TextView txtTitle = dialog.findViewById(R.id.txt_title_confirm);
        txtTitle.setText(title);
        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogListener.okButtonClick(dialog);
            }
        });
    }

    public static void showDialogLoadProgress(Context context) {
        progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.dialog_load_progress);
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            ProgressBar progressBar = progressDialog.findViewById(R.id.progress_bar_login);
//        progressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.color.colorPrimary));
            progressDialog.show();
        }

    }

    public static void hideDialogLoadProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public static Dialog showDialogDefault(Context context, int layoutRes, float width, float height, DialogListener dialogListener) {
        Dialog dialog = getDialogDefault(context, layoutRes, width, height);
        TextView txtOK = dialog.findViewById(R.id.btn_ok);
        TextView txtCancle = dialog.findViewById(R.id.btn_cancel);
        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListener.okButtonClick(dialog);
            }
        });
        txtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    private Dialog getDialog(Context context, int layoutRes) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(layoutRes);
        Window window = dialog.getWindow();
        int widthLayout = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9f);
        if (window != null) {
            window.setLayout(widthLayout, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(R.drawable.border_dialog);
        }
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();
        return dialog;
    }

    public static void showDialogLogout(Context context, int layoutRes, float width, float height, DialogListener dialogListener, String title) {
        Dialog dialog = getDialogDefault(context, layoutRes, width, height);
        TextView textView = dialog.findViewById(R.id.txt_title_logout);
        textView.setText(title);
        TextView txtOK = dialog.findViewById(R.id.btn_ok);
        TextView txtCancle = dialog.findViewById(R.id.btn_cancel);
        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListener.okButtonClick(dialog);
            }
        });
        txtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public static void showDialogHotlineHonda(Context context, int layoutRes, float width, float height, DialogListener dialogListener) {
        Dialog dialog = getDialogDefault(context, layoutRes, width, height);
        TextView textView = dialog.findViewById(R.id.txt_title_logout);
        TextView txtOK = dialog.findViewById(R.id.btn_ok);
        TextView txtCancle = dialog.findViewById(R.id.btn_cancel);
        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListener.okButtonClick(dialog);
            }
        });
        txtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public static Dialog showDialogDefaultNew(Context context, int layoutRes, DialogListener dialogListener) {
        Dialog dialog = getDialogDefault(context, layoutRes);
        TextView txtOK = dialog.findViewById(R.id.btn_ok);
        TextView txtCancle = dialog.findViewById(R.id.btn_cancel);
        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListener.okButtonClick(dialog);
            }
        });
        txtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        return dialog;
    }


    public static void showDialogChangeOil(Context context, int layoutRes, float width, DialogListener dialogListener) {
        Dialog dialog = getDialog(context, layoutRes, width);
        TextView txtOK = dialog.findViewById(R.id.btn_ok);
        TextView txtCancel = dialog.findViewById(R.id.btn_cancel);
        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListener.okButtonClick(dialog);
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public static Dialog getDialog(Context context, int layoutRes, float width) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(layoutRes);
        Window window = dialog.getWindow();
        int widthLayout = (int) (context.getResources().getDisplayMetrics().widthPixels * width);
        window.setLayout(widthLayout, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.drawable.border_dialog);
        dialog.show();
        return dialog;
    }

    public static void showDialogTutorial(Context context, int layoutRes, float width, float height, DialogListener dialogListener) {
        Dialog dialog = getDialogGuide(context, layoutRes, width, height);
        ImageView close = dialog.findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogListener.cancelButtonClick();
            }
        });
    }

    public static void showDialogGuideGetVin(String url, String title, Context context, int layoutRes, float width, float height, DialogListener dialogListener) {
        Dialog dialog = getDialogGuide(context, layoutRes, width, height);
        ImageView close = dialog.findViewById(R.id.btn_close);
        TextView title_guideline = dialog.findViewById(R.id.title_guideline);
        title_guideline.setText(title);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogListener.cancelButtonClick();
            }
        });

        // todo waiting url image from api updated
//        final PhotoView photoView = dialog.findViewById(R.id.imCheckPin);
//        Picasso.with(context)
//                .load(url)
//                .into(photoView);

    }


    public static Dialog getDialogDefault(Context context, int layoutRes, float width, float height) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(layoutRes);
        Window window = dialog.getWindow();
        int widthLayout = (int) (context.getResources().getDisplayMetrics().widthPixels * width);
        int heighLayout = (int) (context.getResources().getDisplayMetrics().heightPixels * height);
        if (window != null) {
            window.setLayout(widthLayout, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(R.drawable.border_dialog);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
        return dialog;
    }

    private static Dialog getDialogGuide(Context context, int layoutRes, float width, float height) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(layoutRes);
        Window window = dialog.getWindow();
        int widthLayout = (int) (context.getResources().getDisplayMetrics().widthPixels * width);
        int heighLayout = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.8f);
        if (window != null) {
            window.setLayout(widthLayout, heighLayout);
            window.setBackgroundDrawableResource(R.drawable.border_dialog);
        }
        dialog.show();
        return dialog;
    }

    private static Dialog getDialogDefault(Context context, int layoutRes) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(layoutRes);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.border_dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();
        return dialog;
    }

    //    public static void showDialogDefault(Context context, int layoutRes, DialogListener dialogListener) {
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(layoutRes);
//
//        Button okButton = dialog.findViewById(R.id.btn_ok);
//        okButton.setOnClickListener(v -> {
//            dialog.dismiss();
//            dialogListener.okButtonClick();
//        });
//
//        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * Constants.DIALOG_WIDTH);
//        dialog.show();
//        Window window = dialog.getWindow();
//        if (window != null) {
//            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
//            window.setBackgroundDrawableResource(R.drawable.border_dialog);
//        }
//
//    }

    public static void showDialogCheckPin(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_check_pin);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.border_dialog);
        ImageView imClose = dialog.findViewById(R.id.imClose);
        imClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public static void showDialogChooseImage(Context context, int layoutRes, DialogChooseImageListener dialogListener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutRes);

        TextView galleryButton = dialog.findViewById(R.id.btnGallery);
        TextView cameraButton = dialog.findViewById(R.id.btnCamera);
        TextView cancelButton = dialog.findViewById(R.id.btn_cancel);
        cameraButton.setOnClickListener(v -> {
            dialog.dismiss();
            dialogListener.onCameraClick();
            dialog.dismiss();

        });

        galleryButton.setOnClickListener(v -> {
            dialog.dismiss();
            dialogListener.onGalleryClick();

        });

        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
            dialogListener.cancelButtonClick();
        });
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * Constants.DIALOG_WIDTH);


        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(R.drawable.bg_null);
            window.getAttributes().windowAnimations = R.style.Animations_SmileWindow;
            window.setGravity(Gravity.BOTTOM);
        }

    }

    public static void showDialogHelp(Context context, int layoutRes, DialogHelpListener dialogListener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(layoutRes);

        TextView tvCancel = dialog.findViewById(R.id.btn_cancel);
        TextView tvFind = dialog.findViewById(R.id.tvFind);
        tvFind.setOnClickListener(v -> {
            dialog.dismiss();
            dialogListener.onFindAgency();

        });

        tvCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * Constants.DIALOG_WIDTH);
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(R.drawable.bg_null);
            window.getAttributes().windowAnimations = R.style.Animations_SmileWindow;
            window.setGravity(Gravity.BOTTOM);
        }

    }

    public static void showDialogReconnected(Context context, String vehicle, DialogRegisterAgain dialogRegisterAgain) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.border_dialog);
        dialog.setContentView(R.layout.dialog_report);
        TextView tvTitle = dialog.findViewById(R.id.txt_title_logout);
        TextView tvcancel = dialog.findViewById(R.id.btn_cancel);
        TextView tvOk = dialog.findViewById(R.id.btn_ok);
        tvOk.setText(context.getResources().getString(R.string.register_again));
        tvTitle.setText("Connection of " + vehicle + "\n" +
                "was removed be a vehicel. \n" +
                "Please register again.");
        tvcancel.setOnClickListener(v -> dialog.dismiss());

        tvOk.setOnClickListener(v -> {
            dialog.dismiss();
            dialogRegisterAgain.onRegisterAgain();
        });

        dialog.show();
    }

    public static void showDialogConfirmInfo(Context context, int layoutRes, float width, float height, DialogListener dialogListener) {
        Dialog dialog = getDialogDefault(context, layoutRes, width, height);
        TextView txtOK = dialog.findViewById(R.id.btn_ok);
        TextView txtCancle = dialog.findViewById(R.id.btn_cancel);
        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListener.okButtonClick(dialog);
            }
        });
        txtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public interface DialogRegisterAgain {
        void onRegisterAgain();
    }


}