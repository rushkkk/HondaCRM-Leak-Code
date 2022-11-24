//package vn.co.honda.hondacrm.ui.activities.benefits_warranty;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.view.View;
//import android.widget.TextView;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import vn.co.honda.hondacrm.R;
//import vn.co.honda.hondacrm.ui.activities.benefits_warranty.ModelMaintenance.Maintenance;
//
//public class MaintenanceInformation {
//    private Activity context;
//    private Maintenance maintenance;
//    public MaintenanceInformation(Activity context, Maintenance maintenance) {
//        this.context = context;
//        this.maintenance = maintenance;
//
//        setUpMaintenance();
//    }
//    public void setUpMaintenance(){
//        String mess = null;
//        String last = maintenance.getLastMaintenance().getTime();
//        String next = maintenance.getNextMaintenance().getTime();
//        View viewNextMaintenance = context.findViewById(R.id.view_next_maintenance);
//        View viewErrServer = context.findViewById(R.id.view_err_server_maintenance);
//        View viewInfor = context.findViewById(R.id.view_infor_maintenance);
//        TextView txtLast, txtNext, txtErr, btnBenefits;
//        txtLast = context.findViewById(R.id.txt_last_maintenance);
//        txtNext = context.findViewById(R.id.txt_next_maintenance);
//        txtErr = context.findViewById(R.id.txt_err_maintenance);
//        btnBenefits = context.findViewById(R.id.btn_Benefits_of_maintenance);
//        if (mess == null) {
//            viewErrServer.setVisibility(View.GONE);
//            viewInfor.setVisibility(View.VISIBLE);
//
//            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
//
//            try {
//
//                if (last != null && !last.isEmpty()) {
//                    Date dateLast = dt.parse(last);
//                    txtLast.setText(dt1.format(dateLast));
//                }
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            if (!next.isEmpty()) {
//                viewNextMaintenance.setVisibility(View.VISIBLE);
//                try {
//                    if (next != null && !next.isEmpty()){
//                        Date dateNext = dt.parse(next);
//                    txtNext.setText(dt1.format(dateNext));
//                }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                viewNextMaintenance.setVisibility(View.GONE);
//            }
//        } else {
//            viewErrServer.setVisibility(View.VISIBLE);
//            viewInfor.setVisibility(View.GONE);
//            txtErr.setText(mess);
//        }
//        btnBenefits.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context,BenefitsWarrantyActivity.class);
//                context.startActivity(intent);
//            }
//        });
//    }
//}
