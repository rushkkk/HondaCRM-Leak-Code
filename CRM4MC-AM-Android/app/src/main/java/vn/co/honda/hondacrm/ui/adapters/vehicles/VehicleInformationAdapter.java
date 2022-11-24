package vn.co.honda.hondacrm.ui.adapters.vehicles;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation.Item;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation.PiDetail;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation.ServiceHistory;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation.VehicleInfo;
import vn.co.honda.hondacrm.ui.activities.booking_service.BookingServiceActivity;
import vn.co.honda.hondacrm.ui.activities.connected.view.ConnectedMainActivity;
import vn.co.honda.hondacrm.ui.activities.vehicles.ServiceHistoryFullActivity;
import vn.co.honda.hondacrm.ui.activities.warrantyextension.WarrantyExtensionActivity;
import vn.co.honda.hondacrm.ui.fragments.connected.local.model.VehicleEntity;
import vn.co.honda.hondacrm.utils.PrefUtils;

import static vn.co.honda.hondacrm.utils.DialogUtils.getDialog;

public class VehicleInformationAdapter extends PagerAdapter {
    private Context mContext;
    private RecyclerView recyclerView;
    private List<ServiceHistory> serviceItemsArrayList;
    private ServiceHistoryAdapter serviceHistoryAdapter;
    private TextView txtExtendWarranty, txtActiveWarranty, txtPI1, txtPI2, txtPI3, txtPI4, txtPI5, txtPI6, tvTimingPI, txtTheNextPI, tvTimingExpired, txtTheLastPI, txtTimeMaintain, noteRecallInformation, txtDuration, txtRecallContent, txt_vin, txtCanGetServiceHistory, txtCanGetDataCurrentBooking, btn_book_service, btn_connect, tv_seeAll, btnCancel, btnYes, btnNo, btnRevise, tv_dialog_cancel, txtCanGetDataPI, txtVehicleLicensePlate, txtVehicleName, txtVehicleModel, txtVehicleType, txtVehicleColor, txtVehicleCC, txt_vehicle_warranty_infor;
    private RadioButton radio1, radio2, radio3, radio4;
    private ApiService apiService;
    private String mTypeFullAccessToken;
    private View layout_vehicle_warranty_infor, layout_service_booking, layout_recall_infor, layout_pi_information, layout_pi, layout_connected, layout_current_booking_infor, layout_service_history, layout_mc_cc, layout_extended_warranty_info, layout_maintain;
    private List<Item> listVehicleInformation;
    private ImageView imgVehicleImage, imgVehicleWarning;
    private TextView btn_book_service_in_recall;
    private TextView lb_vehicle_extend_warranty_infor;


    public VehicleInformationAdapter(Context context, List<Item> listVehicleInformation) {
        mContext = context;
        this.listVehicleInformation = listVehicleInformation;
    }


    @Override
    public int getCount() {
        if (listVehicleInformation == null) {
            return 0;
        }
        return listVehicleInformation.size();
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object o) {
        return view == o;
    }


    @NotNull
    @Override
    public Object instantiateItem(@NotNull final ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View rootView = layoutInflater.inflate(R.layout.fragment_item_vehicle_information, container, false);
        Item vehicle = listVehicleInformation.get(position);
        recyclerView = rootView.findViewById(R.id.rv_serviceHistory);
        tv_seeAll = rootView.findViewById(R.id.tv_seeAll);
        apiService = ApiClient.getClient(mContext).create(ApiService.class);
        tv_seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ServiceHistoryFullActivity.class);
                intent.putExtra("vehicle_name", vehicle.getVehicleInfo().getVehicleName());
                intent.putExtra("vin", vehicle.getVehicleInfo().getVin());
                mContext.startActivity(intent);
            }
        });


        btn_book_service = rootView.findViewById(R.id.btn_book_service);
        btn_book_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BookingServiceActivity.class);
                mContext.startActivity(intent);
            }
        });


        serviceItemsArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        serviceHistoryAdapter = new ServiceHistoryAdapter(mContext, serviceItemsArrayList, vehicle.getVehicleInfo().getVehicleName());
        recyclerView.setAdapter(serviceHistoryAdapter);
        serviceHistoryAdapter.notifyDataSetChanged();
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCancelBooking(view.getContext(), R.layout.dialog_cancel_booking, true);
            }
        });

        btnRevise = rootView.findViewById(R.id.btn_revise);
        btnRevise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCancelBooking(view.getContext(), R.layout.dialog_cancel_booking, false);
            }
        });


        btn_connect = rootView.findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VehicleEntity v = new VehicleEntity(vehicle.getVehicleInfo().getVin(), vehicle.getVehicleInfo().getVehicleName(), vehicle.getVehicleInfo().getLicensePlate(),
                        vehicle.getVehicleInfo().getVehicleType() + "", vehicle.getVehicleInfo().getIsConnected() == 1, false, false, "", "", "2000", "3", "2", "0");
                Gson gson = new Gson();
                String jsonData = gson.toJson(v);
                PrefUtils.setStringPref(mContext, "LAST_ADDED_VEHICLE", jsonData);
                view.getContext().startActivity(new Intent(mContext, ConnectedMainActivity.class));
            }
        });


        btn_book_service_in_recall = rootView.findViewById(R.id.btn_book_service_in_recall);
        btn_book_service_in_recall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BookingServiceActivity.class);
                intent.putExtra("recall", "recall");
                mContext.startActivity(intent);
            }
        });
        checkPI(rootView, vehicle);
        setVehicleInfor(rootView, vehicle);
        isWarning(rootView, vehicle);
        isConnected(rootView, vehicle);
        checkCurrentServiceBooking(rootView, vehicle);
        checkServiceHistory(rootView, vehicle);
        txtExtendWarranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WarrantyExtensionActivity.class);
                mContext.startActivity(intent);
            }
        });
        container.addView(rootView);
        return rootView;
    }

    public int getItemPosition(@NotNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
        container.removeView((View) object);
    }


    private void showDialogCancelBooking(Context context, int layoutRes, boolean isCancel) {
        Dialog dialog = getDialog(context, layoutRes, 0.9f);
        tv_dialog_cancel = dialog.findViewById(R.id.tv_dialog_cancel);
        radio1 = dialog.findViewById(R.id.radio1);
        radio2 = dialog.findViewById(R.id.radio2);
        radio3 = dialog.findViewById(R.id.radio3);
        radio4 = dialog.findViewById(R.id.radio4);
        if (isCancel) {
            tv_dialog_cancel.setText(R.string.dialog_cancel);
            radio1.setText(R.string.dialog_cancel_radio_1);
            radio2.setText(R.string.dialog_cancel_radio_2);
            radio3.setText(R.string.dialog_cancel_radio_3);
            btnYes = dialog.findViewById(R.id.btn_yes);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            btnNo = dialog.findViewById(R.id.btn_no);
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else {
            tv_dialog_cancel.setText(R.string.dialog_revise);
            radio1.setText(R.string.dialog_revise_radio_1);
            radio2.setText(R.string.dialog_revise_radio_2);
            radio3.setText(R.string.dialog_revise_radio_3);
            btnYes = dialog.findViewById(R.id.btn_yes);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Resive", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            btnNo = dialog.findViewById(R.id.btn_no);
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void setVehicleInfor(View container, Item vehicle) {
        VehicleInfo vehicleInfo = vehicle.getVehicleInfo();
        txtVehicleLicensePlate = container.findViewById(R.id.txtVehicleLicensePlate);
        txtVehicleName = container.findViewById(R.id.txtVehicleName);
        txtVehicleModel = container.findViewById(R.id.txtVehicleModel);
        txtVehicleType = container.findViewById(R.id.txtVehicleType);
        txtVehicleCC = container.findViewById(R.id.txtVehicleCC);
        txtVehicleColor = container.findViewById(R.id.txtVehicleColor);
        imgVehicleImage = container.findViewById(R.id.imgVehicleImage);
        txt_vin = container.findViewById(R.id.txt_vin);
        if(vehicle.getVehicleInfo().getVin() != null){
            Integer vinLength = vehicle.getVehicleInfo().getVin().length();
            txt_vin.setText("***" + vehicle.getVehicleInfo().getVin().substring(vinLength-6, vinLength));
        }
        imgVehicleWarning = container.findViewById(R.id.imgVehicleWarning);
        txt_vehicle_warranty_infor = container.findViewById(R.id.txt_vehicle_warranty_infor);
        lb_vehicle_extend_warranty_infor = container.findViewById(R.id.lb_vehicle_extend_warranty_infor);
        layout_extended_warranty_info = container.findViewById(R.id.layout_extended_warranty_info);
        layout_mc_cc = container.findViewById(R.id.layout_mc_cc);
        layout_vehicle_warranty_infor = container.findViewById(R.id.layout_vehicle_warranty_infor);
        txtExtendWarranty = container.findViewById(R.id.txtExtendWarranty);
        txtActiveWarranty = container.findViewById(R.id.txtActiveWarranty);
        if (vehicleInfo.getVehicleName() != null) {
            txtVehicleName.setText(vehicleInfo.getVehicleName());
        }
        if (vehicleInfo.getVehicleModel() != null) {
            txtVehicleModel.setText(vehicleInfo.getVehicleModel());
        }
        if (vehicleInfo.getBodyStyle() != null) {
            txtVehicleType.setText(vehicleInfo.getBodyStyle());
        }
        if (vehicleInfo.getVehicleColor() != null) {
            txtVehicleColor.setText(vehicleInfo.getVehicleColor());
        }
        if (vehicleInfo.getLicensePlate() != null) {
            txtVehicleLicensePlate.setText(vehicleInfo.getLicensePlate());
        }
        if (vehicleInfo.getVehicleType() == 2) {
            layout_mc_cc.setVisibility(View.GONE);
            if (vehicleInfo.getVehicleImage() != null) {
                Picasso.with(mContext)
                        .load(vehicleInfo.getVehicleImage())
                        .error(R.drawable.ic_vehicle_fake)
                        .into(imgVehicleImage);
            } else {
                imgVehicleImage.setBackground(mContext.getDrawable(R.drawable.ic_vehicle_fake));
            }
            if (vehicleInfo.getIsHonda() == 1) {
                if (vehicle.getWarrantyInfo() != null && vehicle.getWarrantyInfo().getStandardWarranty() != null) {
                    if (vehicle.getWarrantyInfo().getStandardWarranty().getStartDate() != null
                            && vehicle.getWarrantyInfo().getStandardWarranty().getEndDate() != null) {
                        txt_vehicle_warranty_infor.setVisibility(View.VISIBLE);
                        txt_vehicle_warranty_infor.setText(vehicle.getWarrantyInfo().getStandardWarranty().getStartDate()
                                + " - " + vehicle.getWarrantyInfo().getStandardWarranty().getEndDate());
                    } else {
                        txt_vehicle_warranty_infor.setVisibility(View.GONE);
                    }
                } else {
                    txt_vehicle_warranty_infor.setVisibility(View.GONE);
                }

                if (vehicle.getWarrantyInfo() != null && vehicle.getWarrantyInfo().getExtensionWarranty() != null &&
                        vehicle.getWarrantyInfo().getExtensionWarranty().getPackage() != null &&
                        vehicle.getWarrantyInfo().getExtensionWarranty().getEndDate() != null) {
                    layout_extended_warranty_info.setVisibility(View.VISIBLE);
                    lb_vehicle_extend_warranty_infor.setVisibility(View.VISIBLE);
                    lb_vehicle_extend_warranty_infor.setText(vehicle.getWarrantyInfo().getExtensionWarranty().getPackage() + ", " + mContext.getString(R.string.lb_until) + " " + vehicle.getWarrantyInfo().getExtensionWarranty().getEndDate());
                } else {
                    layout_extended_warranty_info.setVisibility(View.GONE);
                    lb_vehicle_extend_warranty_infor.setVisibility(View.GONE);
                    if (vehicle.getWarrantyInfo() != null && vehicle.getWarrantyInfo().getExtensionWarranty() != null &&
                            vehicle.getWarrantyInfo().getExtensionWarranty().getPackage() == null) {
                        txtExtendWarranty.setVisibility(View.VISIBLE);
                    } else {
                        txtExtendWarranty.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                layout_extended_warranty_info.setVisibility(View.GONE);
                layout_vehicle_warranty_infor.setVisibility(View.GONE);
            }
        } else {
            layout_mc_cc.setVisibility(View.VISIBLE);
            layout_extended_warranty_info.setVisibility(View.GONE);
            lb_vehicle_extend_warranty_infor.setVisibility(View.GONE);
            if (vehicleInfo.getCylinderCapacity() != null) {
                txtVehicleCC.setText(vehicleInfo.getCylinderCapacity());
            }
            if (vehicleInfo.getVehicleImage() != null) {
                Picasso.with(mContext)
                        .load(vehicleInfo.getVehicleImage())
                        .error(R.drawable.ic_moto)
                        .into(imgVehicleImage);
            } else {
                imgVehicleImage.setBackground(mContext.getDrawable(R.drawable.ic_moto));
            }
            if (vehicleInfo.getIsHonda() != null && vehicleInfo.getIsHonda() == 1) {
                if (vehicle.getWarrantyInfo() != null
                        && vehicle.getWarrantyInfo().getStandardWarranty() != null
                        && vehicle.getWarrantyInfo().getStandardWarranty().getStartDate() != null
                        && vehicle.getWarrantyInfo().getStandardWarranty().getEndDate() != null) {
                    txt_vehicle_warranty_infor.setVisibility(View.VISIBLE);
                    txt_vehicle_warranty_infor.setText(vehicle.getWarrantyInfo().getStandardWarranty().getStartDate()
                            + " - " + vehicle.getWarrantyInfo().getStandardWarranty().getEndDate());
                } else {
                    txt_vehicle_warranty_infor.setVisibility(View.GONE);
                }
            }
        }
    }

    private void checkPI(View container, Item vehicle) {
        layout_maintain = container.findViewById(R.id.layout_maintain);
        txtTheLastPI = container.findViewById(R.id.txtTheLastPI);
        tvTimingPI = container.findViewById(R.id.tvTimingPI);
        layout_pi = container.findViewById(R.id.layout_pi);
        tvTimingPI = container.findViewById(R.id.tvTimingPI);
        txtTheNextPI = container.findViewById(R.id.txtTheNextPI);
        tvTimingExpired = container.findViewById(R.id.tvTimingExpired);
        if (vehicle.getVehicleInfo().getVehicleType() == 1) {
            layout_maintain.setVisibility(View.GONE);
            layout_pi.setVisibility(View.VISIBLE);
            layout_pi_information = container.findViewById(R.id.layout_pi_information);
            txtCanGetDataPI = container.findViewById(R.id.txtCanGetDataPI);
            if (vehicle.getPi() != null && vehicle.getPi().getItem() != null && vehicle.getPi().getItem().size() > 0 && vehicle.getPi().getLastTextPi() != null && vehicle.getPi().getLastDatePi() != null && vehicle.getPi().getNextTextPi() != null && vehicle.getPi().getNextDatePi() != null) {
                txtPI1 = container.findViewById(R.id.txtPI1);
                txtPI2 = container.findViewById(R.id.txtPI2);
                txtPI3 = container.findViewById(R.id.txtPI3);
                txtPI4 = container.findViewById(R.id.txtPI4);
                txtPI5 = container.findViewById(R.id.txtPI5);
                txtPI6 = container.findViewById(R.id.txtPI6);
                layout_pi.setVisibility(View.VISIBLE);
                layout_pi_information.setVisibility(View.VISIBLE);
                txtCanGetDataPI.setVisibility(View.GONE);
                txtTheLastPI.setText(vehicle.getPi().getLastTextPi());
                tvTimingPI.setText(vehicle.getPi().getLastDatePi());
                txtTheNextPI.setText(vehicle.getPi().getNextTextPi());
                tvTimingExpired.setText(vehicle.getPi().getNextDatePi());
                changeColorPI(vehicle.getPi().getItem());

            } else {
                layout_pi.setVisibility(View.GONE);
                layout_pi_information.setVisibility(View.GONE);
                txtCanGetDataPI.setVisibility(View.VISIBLE);
            }
        } else {
            if (vehicle.getMaintenance() != null && vehicle.getMaintenance().getLastMaintenance() != null
                    && vehicle.getMaintenance().getLastMaintenance().getMaintenanceId() == null) {
                layout_maintain.setVisibility(View.GONE);
            } else {
                txtTimeMaintain = container.findViewById(R.id.txtTimeMaintain);
                layout_maintain.setVisibility(View.VISIBLE);
                if (vehicle.getMaintenance() != null && vehicle.getMaintenance().getLastMaintenance() != null
                        && vehicle.getMaintenance().getLastMaintenance().getTime() != null) {
                    txtTimeMaintain.setText(vehicle.getMaintenance().getLastMaintenance().getTime());
                }
            }
            layout_pi.setVisibility(View.GONE);
        }
    }


    //Fix recall data fake
    private void isWarning(View container, Item vehicle) {
        imgVehicleWarning = container.findViewById(R.id.imgVehicleWarning);
        layout_recall_infor = container.findViewById(R.id.layout_recall_infor);
        txtDuration = container.findViewById(R.id.txtDuration);
        txtRecallContent = container.findViewById(R.id.txtRecallContent);
        noteRecallInformation = container.findViewById(R.id.noteRecallInformation);
        if (vehicle.getRecallInfo() != null && vehicle.getRecallInfo().getItem() != null) {
            imgVehicleWarning.setVisibility(View.VISIBLE);
            layout_recall_infor.setVisibility(View.VISIBLE);
            if (vehicle.getRecallInfo().getItem().getReason() != null) {
                txtRecallContent.setText(vehicle.getRecallInfo().getItem().getReason());
            }
            if (vehicle.getRecallInfo().getItem().getSentTime() != null) {
                txtDuration.setText(vehicle.getRecallInfo().getItem().getSentTime() + mContext.getString(R.string.lb_hours));
            }
            if (vehicle.getRecallInfo().getItem().getCampaignDescription() != null) {
                noteRecallInformation.setText(vehicle.getRecallInfo().getItem().getCampaignDescription());
            }
        } else {
            imgVehicleWarning.setVisibility(View.GONE);
            layout_recall_infor.setVisibility(View.GONE);
        }
    }

    private void isConnected(View container, Item vehicle) {
        btn_connect = container.findViewById(R.id.btn_connect);
//        layout_connected = container.findViewById(R.id.layout_connected);
        if (vehicle.getVehicleInfo().getConnect() != null && vehicle.getVehicleInfo().getConnect()) {
            btn_connect.setVisibility(View.GONE);
//            layout_connected.setVisibility(View.VISIBLE);
        } else {
            btn_connect.setVisibility(View.VISIBLE);
//            layout_connected.setVisibility(View.GONE);
        }
    }

    private void checkCurrentServiceBooking(View container, Item vehicle) {
        txtCanGetDataCurrentBooking = container.findViewById(R.id.txtCanGetDataCurrentBooking);
        layout_current_booking_infor = container.findViewById(R.id.layout_current_booking_infor);
        layout_service_booking = container.findViewById(R.id.layout_service_booking);
//        if (vehicle.getBookingCurrent() != null) {
//            layout_current_booking_infor.setVisibility(View.VISIBLE);
//            txtCanGetDataCurrentBooking.setVisibility(View.GONE);
//            layout_service_booking.setVisibility(View.VISIBLE);
//        } else {
//            layout_service_booking.setVisibility(View.GONE);
//            txtCanGetDataCurrentBooking.setVisibility(View.VISIBLE);
//            layout_current_booking_infor.setVisibility(View.GONE);
//        }
        layout_service_booking.setVisibility(View.GONE);
    }

    public void checkServiceHistory(View container, Item vehicle) {
        layout_service_history = container.findViewById(R.id.layout_service_history);
        txtCanGetServiceHistory = container.findViewById(R.id.txtCanGetServiceHistory);
        if (vehicle.getServiceHistory() != null && vehicle.getServiceHistory().size() > 0) {
            serviceItemsArrayList.addAll(vehicle.getServiceHistory());
            serviceHistoryAdapter.notifyDataSetChanged();
            layout_service_history.setVisibility(View.VISIBLE);
            txtCanGetServiceHistory.setVisibility(View.GONE);
        } else {
            txtCanGetServiceHistory.setVisibility(View.VISIBLE);
            layout_service_history.setVisibility(View.GONE);
        }
    }

    public void changeColorPI(List<PiDetail> piDetailList) {
        for (int i = 0; i < 6; i++) {
            PiDetail piDetail = piDetailList.get(i);
            switch (piDetail.getPiNo()) {
                case 1:
                    switch (piDetail.getPiStatus()) {
                        case 0:
                            txtPI1.setBackground(mContext.getDrawable(R.drawable.item_pi_1st_default));
                            break;
                        case 1:
                            txtPI1.setBackground(mContext.getDrawable(R.drawable.item_pi_1st_on_time));
                            break;
                        case 2:
                            txtPI1.setBackground(mContext.getDrawable(R.drawable.item_pi_1st_expired));
                            break;
                        case 3:
                            txtPI1.setBackground(mContext.getDrawable(R.drawable.item_pi_1st_next_time));
                            break;
                        default:
                            txtPI1.setBackground(mContext.getDrawable(R.drawable.item_pi_1st_default));
                            break;
                    }
                    break;
                case 2:
                    switch (piDetail.getPiStatus()) {
                        case 0:
                            txtPI2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIDefault));
                            break;
                        case 1:
                            txtPI2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIOnTime));
                            break;
                        case 2:
                            txtPI2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIExpired));
                            break;
                        case 3:
                            txtPI2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPINextTime));
                            break;
                        default:
                            txtPI2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIDefault));
                            break;
                    }
                    break;
                case 3:
                    switch (piDetail.getPiStatus()) {
                        case 0:
                            txtPI3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIDefault));
                            break;
                        case 1:
                            txtPI3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIOnTime));
                            break;
                        case 2:
                            txtPI3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIExpired));
                            break;
                        case 3:
                            txtPI3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPINextTime));
                            break;
                        default:
                            txtPI3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIDefault));
                            break;
                    }
                    break;
                case 4:
                    switch (piDetail.getPiStatus()) {
                        case 0:
                            txtPI4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIDefault));
                            break;
                        case 1:
                            txtPI4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIOnTime));
                            break;
                        case 2:
                            txtPI4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIExpired));
                            break;
                        case 3:
                            txtPI4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPINextTime));
                            break;
                        default:
                            txtPI4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIDefault));
                            break;
                    }
                    break;
                case 5:
                    switch (piDetail.getPiStatus()) {
                        case 0:
                            txtPI5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIDefault));
                            break;
                        case 1:
                            txtPI5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIOnTime));
                            break;
                        case 2:
                            txtPI5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIExpired));
                            break;
                        case 3:
                            txtPI5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPINextTime));
                            break;
                        default:
                            txtPI5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorItemPIDefault));
                            break;
                    }
                    break;
                case 6:
                    switch (piDetail.getPiStatus()) {
                        case 0:
                            txtPI6.setBackground(mContext.getDrawable(R.drawable.item_pi_last_default));
                            break;
                        case 1:
                            txtPI6.setBackground(mContext.getDrawable(R.drawable.item_pi_last_on_time));
                            break;
                        case 2:
                            txtPI6.setBackground(mContext.getDrawable(R.drawable.item_pi_last_expired));
                            break;
                        case 3:
                            txtPI6.setBackground(mContext.getDrawable(R.drawable.item_pi_last_next_time));
                            break;
                        default:
                            txtPI6.setBackground(mContext.getDrawable(R.drawable.item_pi_last_default));
                            break;
                    }
            }
        }
    }
}
