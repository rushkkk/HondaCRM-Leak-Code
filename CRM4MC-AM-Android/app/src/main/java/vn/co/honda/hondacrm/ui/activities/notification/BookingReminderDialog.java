package vn.co.honda.hondacrm.ui.activities.notification;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import vn.co.honda.hondacrm.R;

public class BookingReminderDialog extends Dialog {
    private Context context;
    private TextView btnCancel, btnBookServiceNow;

    public BookingReminderDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_booking_reminder);
        Window window = getWindow();
        setCancelable(false);
        int widthLayout = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9f);
        if (window != null) {
            window.setLayout(widthLayout, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(R.drawable.border_dialog);
        }
        btnCancel = findViewById(R.id.btn_cancel_booking_reminder);
        btnBookServiceNow = findViewById(R.id.btn_navigation_dealer);
        btnBookServiceNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
