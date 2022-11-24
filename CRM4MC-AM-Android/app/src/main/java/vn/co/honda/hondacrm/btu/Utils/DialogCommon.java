package vn.co.honda.hondacrm.btu.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import vn.co.honda.hondacrm.R;


public class DialogCommon {

    public static Dialog createPopup(final Context context,
                                     final String textButtonLeft,
                                     final String textButtonRight,
                                     final String message,
                                     final View.OnClickListener onLeftButtonClick,
                                     final View.OnClickListener onRightButtonClick) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout_option);
        TextView txtMessage = dialog.findViewById(R.id.txtMessageDialog);
        TextView btnLeft = dialog.findViewById(R.id.btnLeftDialog);
        TextView btnRight = dialog.findViewById(R.id.btnRightDialog);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.border_dialog);
        txtMessage.setText(message);
        btnLeft.setText(textButtonLeft);
        btnRight.setText(textButtonRight);
        btnLeft.setOnClickListener(view -> {
            dialog.dismiss();
            if (onLeftButtonClick != null) {
                onLeftButtonClick.onClick(view);
            }
        });
        btnRight.setOnClickListener(view -> {
//                dialog.dismiss();
            if (onRightButtonClick != null) {
                onRightButtonClick.onClick(view);
            }
        });
//        dialog.show();
        return dialog;
    }

    public static Dialog createPopup(final Context context,
                                     final String message,
                                     final String textButton,
                                     final View.OnClickListener onButtonClick) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_report);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.border_dialog);
        final TextView txtCancle = dialog.findViewById(R.id.btn_cancel);
        txtCancle.setVisibility(View.GONE);
        final TextView btnConfirm = dialog.findViewById(R.id.btn_ok);
        final TextView txtMessage = dialog.findViewById(R.id.txt_title_logout);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (onButtonClick != null) {
                    onButtonClick.onClick(view);
                }
            }
        });
        txtMessage.setText(message);
        btnConfirm.setText(textButton);
//        dialog.show();
        return dialog;
    }
}
