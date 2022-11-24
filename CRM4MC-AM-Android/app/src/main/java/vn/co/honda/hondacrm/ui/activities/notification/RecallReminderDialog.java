package vn.co.honda.hondacrm.ui.activities.notification;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.benefits_warranty.BenefitsWarrantyActivity;
import vn.co.honda.hondacrm.ui.activities.booking_service.BookingServiceActivity;

public class RecallReminderDialog extends Dialog {
    private Context context;
    private TextView btnCancel,btnBookServiceNow,txtTitle, txtContent;
    private String title, content;
    public RecallReminderDialog(Context context,String title,String content) {
        super(context);
        this.context = context;
        this.title = title;
        this.content = content;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recall_reminder);
        setCancelable(false);
        Window window = getWindow();
        int widthLayout = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9f);
        if (window != null) {
            window.setLayout(widthLayout, ViewGroup.LayoutParams.WRAP_CONTENT );
            window.setBackgroundDrawableResource(R.drawable.border_dialog);
        }
        txtTitle =  findViewById(R.id.txt_title_recall);
        txtContent = findViewById(R.id.txt_conten_recall);
        btnCancel = findViewById(R.id.btn_cancel_reminder);
        btnBookServiceNow = findViewById(R.id.btn_book_service_now);
        txtTitle.setText(title);
        txtContent.setText(content);
        btnBookServiceNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
//                Intent intent = new Intent(context, TestDriveActivity.class);
//                intent.putExtra("recall", "recall");
//                context.startActivity(intent);
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
