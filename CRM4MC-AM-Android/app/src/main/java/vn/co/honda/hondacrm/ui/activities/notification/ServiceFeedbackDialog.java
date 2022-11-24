package vn.co.honda.hondacrm.ui.activities.notification;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import vn.co.honda.hondacrm.R;

public class ServiceFeedbackDialog extends Dialog {
    private Context context;
    private TextView btnCancel,btnSubmit;
    public ServiceFeedbackDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_service_feedback);
        Window window = getWindow();
        setCancelable(false);
        int widthLayout = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9f);
        if (window != null) {
            window.setLayout(widthLayout, ViewGroup.LayoutParams.WRAP_CONTENT );
            window.setBackgroundDrawableResource(R.drawable.border_dialog);
        }
        btnCancel = findViewById(R.id.btn_cancel_service_feedback);
        btnSubmit = findViewById(R.id.btn_submit_service_feedback);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
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
