package vn.co.honda.hondacrm.ui.fragments.booking_service.views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.utils.DialogUtils;

public class ReviewAndSubmitBookingFragment extends Fragment {
    private ImageView imgLogoDealer;
    private TextView tvPhoneNumberDealer, tvDealerName, tvDealerAddress, tvDealerWorkingTime, tvDealerDistance, tvSelectedTime, tvSlectType, tvDescribeRequest, tvFinishingTime, btnSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_and_submit_booking, container, false);
        initView(view);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.showDialogConfirm(getActivity(), R.layout.dialog_service_booking_successfully, 0.9f, 0.5f, new DialogUtils.DialogListener() {
                    @Override
                    public void okButtonClick(Dialog dialog) {
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void cancelButtonClick() {

                    }
                });
            }
        });
        return view;
    }

    private void initView(View view){
        tvDealerName = view.findViewById(R.id.tv_dealer_name);
        tvPhoneNumberDealer = view.findViewById(R.id.tv_dealer_phone_number);
        tvDealerAddress = view.findViewById(R.id.tv_dealer_address);
        tvDealerWorkingTime = view.findViewById(R.id.tv_dealer_work_time);
        tvDealerDistance = view.findViewById(R.id.tv_dealer_distance);
        tvSelectedTime = view.findViewById(R.id.tv_calendar_time);
        tvSlectType = view.findViewById(R.id.tv_selected_type);
        tvDescribeRequest = view.findViewById(R.id.tv_describe_request);
        tvFinishingTime = view.findViewById(R.id.tv_finishing_time);
        imgLogoDealer = view.findViewById(R.id.img_logo_honda);
        btnSubmit = view.findViewById(R.id.btn_ok);

    }
}
