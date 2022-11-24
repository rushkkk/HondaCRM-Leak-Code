package com.honda.connmc.View.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.honda.connmc.R;

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
        Button btnLeft = dialog.findViewById(R.id.btnLeftDialog);
        Button btnRight = dialog.findViewById(R.id.btnRightDialog);
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
                                     final String textButton,
                                     final String message,
                                     final View.OnClickListener onButtonClick) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout_confirm);
        final TextView txtMessage = dialog.findViewById(R.id.txtMessageDialog);
        final Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
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
