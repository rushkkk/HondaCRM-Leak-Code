//package vn.co.honda.hondacrm.ui.activities.benefits_warranty;
//
//import android.app.Activity;
//import android.view.View;
//import android.widget.TextView;
//
//import vn.co.honda.hondacrm.R;
//
//public class RecallInformation {
//    private Activity context;
//
//    public RecallInformation(Activity context) {
//        this.context = context;
//        showRecallInfor();
//    }
//    public void showRecallInfor() {
//        String mess = null;
//        String mesageRecall = "sua chan phanh";
//        String duaration = "2";
//        String vin = "123456";
//        String note = "abcdèghyklm";
//        View viewRecall = context.findViewById(R.id.view_infor_recall);
//        View viewErrServer = context.findViewById(R.id.view_err_server_recall);
//        TextView tvMessageRecall, tvDuration, tvVin, tvNote, txt_err_server_recall;
//        tvMessageRecall = context.findViewById(R.id.txt_message_recall);
//        tvDuration = context.findViewById(R.id.txt_time_repair);
//        tvVin = context.findViewById(R.id.txt_number_vin);
//        tvNote = context.findViewById(R.id.txt_not_recall);
//        txt_err_server_recall = context.findViewById(R.id.txt_err_server_recall);
//        if (mess == null) {
//            viewErrServer.setVisibility(View.GONE);
//            viewRecall.setVisibility(View.VISIBLE);
//            tvMessageRecall.setText(mesageRecall);
//            tvDuration.setText(duaration + " hours");
//            tvVin.setText(vin);
//            tvNote.setText(note);
//        } else {
//            viewErrServer.setVisibility(View.VISIBLE);
//            viewRecall.setVisibility(View.GONE);
//            txt_err_server_recall.setText(mess);
//        }
//    }
//}
