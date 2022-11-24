package vn.co.honda.hondacrm.ui.fragments.booking_service.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.booking_service.SelectedTimeInDay;
import vn.co.honda.hondacrm.net.model.booking_service.TimeSlot;
import vn.co.honda.hondacrm.ui.activities.booking_service.BookingServiceActivity;
import vn.co.honda.hondacrm.ui.activities.booking_service.ICurrentDateCallBack;
import vn.co.honda.hondacrm.ui.activities.booking_service.INumberStepCallBack;
import vn.co.honda.hondacrm.ui.customview.CalendarView;
import vn.co.honda.hondacrm.ui.activities.booking_service.IControlCalendarCallBack;
import vn.co.honda.hondacrm.ui.fragments.booking_service.models.BookService;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.PrefUtils;

public class SelectTimeSlotFragment extends Fragment {

    public BookService bookService = new BookService();
    private INumberStepCallBack mNumberStepCallBack;
    private ImageView imgNextStep, imgLogoDealer;
    private RadioGroup radioGroupAM, radioGroupPM;
    private TextView tvPhoneNumberDealer, tvDateSelected, tvDealerName, tvDealerAddress, tvDealerWorkingTime, tvDealerDistance;
    private CalendarView mControlCalendar;
    private Context mContext;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private List<SelectedTimeInDay> timeSlotLit = new ArrayList<>();
    private RadioButton radioSelectTime8h, radioSelectTime9h, radioSelectTime10h, radioSelectTime11h, radioSelectTime13h, radioSelectTime14h, radioSelectTime15h, radioSelectTime16h;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof BookingServiceActivity) {
            this.mNumberStepCallBack = (BookingServiceActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_time_slot, container, false);
        if (getArguments() != null){
            bookService = (BookService) getArguments().getSerializable("bookService");
        }
        initViews(view);
        imgNextStep.setOnClickListener(view1 -> {
            if (bookService.getTimeSelected() != null) {
                if (mNumberStepCallBack != null) {
                    mNumberStepCallBack.setStepIndicator(4);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bookService", bookService);
                    Fragment toFragment = new SelectServiceTypeFragment();
                    toFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.frame_booking_service, toFragment).commit();
                }
            } else {
                Toast.makeText(getActivity(), "Please select time slot!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void callPhoneDealer() {
        tvPhoneNumberDealer.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + tvPhoneNumberDealer.getText()));
            startActivity(callIntent);
        });
    }

    private void initViews(View view) {
        mControlCalendar = view.findViewById(R.id.calendar_view);
        tvPhoneNumberDealer = view.findViewById(R.id.tv_dealer_phone_number);
        tvDateSelected = view.findViewById(R.id.tv_date_selected);
        tvDealerAddress = view.findViewById(R.id.tv_dealer_address);
        tvDealerName = view.findViewById(R.id.tv_dealer_name);
        tvDealerDistance = view.findViewById(R.id.tv_dealer_distance);
        tvDealerWorkingTime = view.findViewById(R.id.tv_dealer_work_time);
        imgNextStep = view.findViewById(R.id.img_time_slot_next);
        imgLogoDealer = view.findViewById(R.id.img_logo_honda);
        radioSelectTime8h = view.findViewById(R.id.rd_time_slot_8h);
        radioSelectTime9h = view.findViewById(R.id.rd_time_slot_9h);
        radioSelectTime10h = view.findViewById(R.id.rd_time_slot_10h);
        radioSelectTime11h = view.findViewById(R.id.rd_time_slot_11h);
        radioSelectTime13h = view.findViewById(R.id.rd_time_slot_13h);
        radioSelectTime14h = view.findViewById(R.id.rd_time_slot_14h);
        radioSelectTime15h = view.findViewById(R.id.rd_time_slot_15h);
        radioSelectTime16h = view.findViewById(R.id.rd_time_slot_16h);
        radioGroupAM = view.findViewById(R.id.gr_radio_am);
        radioGroupPM = view.findViewById(R.id.gr_radio_pm);

        if (bookService.getDealer() != null) {
            tvPhoneNumberDealer.setText(bookService.getDealer().getPhone());
            tvDealerName.setText(bookService.getDealer().getDealerName());
            tvDealerWorkingTime.setText(bookService.getDealer().getWorkingStart() + "-" + bookService.getDealer().getWorkingEnd());
            tvDealerAddress.setText(bookService.getDealer().getAddress());
            tvDealerDistance.setText(String.valueOf(bookService.getDealer().getDistance())+"km");
        }

        radioGroupAM.clearCheck();
        radioGroupPM.clearCheck();
        radioGroupAM.setOnCheckedChangeListener(listener1);
        radioGroupPM.setOnCheckedChangeListener(listener2);
        mControlCalendar.getDaysOfMonth();
//        List<Date> events = new ArrayList<>();
//        events.add(new Date());
        mControlCalendar.setCurrentDate(mCurrentDateCallBack);
        mControlCalendar.setControlCalendar(mControlCalendarCallBack);
        callPhoneDealer();
//        mControlCalendar.updateCalendar(events);
    }

    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                radioGroupPM.setOnCheckedChangeListener(null);
                radioGroupPM.clearCheck();
                radioGroupPM.setOnCheckedChangeListener(listener2);
                int id = radioGroupAM.getCheckedRadioButtonId();
                RadioButton radioButton = radioGroupAM.findViewById(id);
                String time = (String) radioButton.getText();
                bookService.setTimeSelected(time);
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                radioGroupAM.setOnCheckedChangeListener(null);
                radioGroupAM.clearCheck();
                radioGroupAM.setOnCheckedChangeListener(listener1);
                int id = radioGroupPM.getCheckedRadioButtonId();
                RadioButton radioButton = radioGroupPM.findViewById(id);
                String time = (String) radioButton.getText();
                bookService.setTimeSelected(time);
            }
        }
    };

    private ICurrentDateCallBack mCurrentDateCallBack = new ICurrentDateCallBack() {
        @Override
        public void getDate(String date, List<SelectedTimeInDay> slotOfDays) {
            tvDateSelected.setText(date);
            setAvailable(slotOfDays);
            bookService.setDateSelected(date);
        }
    };

    private IControlCalendarCallBack mControlCalendarCallBack = new IControlCalendarCallBack() {
        @Override
        public void onItemClick(Date content) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
            String dateFormat = formatDate.format(content);
            bookService.setDateSelected(dateFormat);
            tvDateSelected.setText(dateFormat);
            String dateSelect = format.format(content);
            ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
            String mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(getContext());
            apiService.getTimeSlotOfDay(mTypeFullAccessToken, "D001", dateSelect)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleSubscriber<TimeSlot>() {
                        @Override
                        public void onSuccess(TimeSlot timeSlot) {
                            List<SelectedTimeInDay> timeSlots = timeSlot.getData();
                            timeSlotLit = timeSlots;
                        }

                        @Override
                        public void onError(Throwable error) {
                        }
                    });
            setAvailable(timeSlotLit);
        }
    };

    private void setAvailable(List<SelectedTimeInDay> timeSlots) {
        Drawable imgAvailable = getActivity().getResources().getDrawable(R.drawable.ic_point_available);
        for (SelectedTimeInDay slot : timeSlots) {
            if (String.valueOf(radioSelectTime8h.getText()).replaceAll("\\s", "").equals(slot.getTimeSlot()) && slot.getStatus() == Constants.AVAILABLE) {
                radioSelectTime8h.setCompoundDrawablesWithIntrinsicBounds(null, null, imgAvailable, null);
                radioSelectTime8h.setEnabled(true);
            }
            if (String.valueOf(radioSelectTime9h.getText()).replaceAll("\\s", "").equals(slot.getTimeSlot()) && slot.getStatus() == Constants.AVAILABLE) {
                radioSelectTime9h.setCompoundDrawablesWithIntrinsicBounds(null, null, imgAvailable, null);
                radioSelectTime9h.setEnabled(true);
            }
            if (String.valueOf(radioSelectTime10h.getText()).replaceAll("\\s", "").equals(slot.getTimeSlot()) && slot.getStatus() == Constants.AVAILABLE) {
                radioSelectTime10h.setCompoundDrawablesWithIntrinsicBounds(null, null, imgAvailable, null);
                radioSelectTime10h.setEnabled(true);
            }
            if (String.valueOf(radioSelectTime11h.getText()).replaceAll("\\s", "").equals(slot.getTimeSlot()) && slot.getStatus() == Constants.AVAILABLE) {
                radioSelectTime11h.setCompoundDrawablesWithIntrinsicBounds(null, null, imgAvailable, null);
                radioSelectTime11h.setEnabled(true);
            }
            if (String.valueOf(radioSelectTime13h.getText()).replaceAll("\\s", "").equals(slot.getTimeSlot()) && slot.getStatus() == Constants.AVAILABLE) {
                radioSelectTime13h.setCompoundDrawablesWithIntrinsicBounds(null, null, imgAvailable, null);
                radioSelectTime13h.setEnabled(true);
            }
            if (String.valueOf(radioSelectTime14h.getText()).replaceAll("\\s", "").equals(slot.getTimeSlot()) && slot.getStatus() == Constants.AVAILABLE) {
                radioSelectTime14h.setCompoundDrawablesWithIntrinsicBounds(null, null, imgAvailable, null);
                radioSelectTime14h.setEnabled(true);
            }
            if (String.valueOf(radioSelectTime15h.getText()).replaceAll("\\s", "").equals(slot.getTimeSlot()) && slot.getStatus() == Constants.AVAILABLE) {
                radioSelectTime15h.setCompoundDrawablesWithIntrinsicBounds(null, null, imgAvailable, null);
                radioSelectTime15h.setEnabled(true);
            }
            if (String.valueOf(radioSelectTime16h.getText()).replaceAll("\\s", "").equals(slot.getTimeSlot()) && slot.getStatus() == Constants.AVAILABLE) {
                radioSelectTime16h.setCompoundDrawablesWithIntrinsicBounds(null, null, imgAvailable, null);
                radioSelectTime16h.setEnabled(true);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCurrentDateCallBack != null) {
            mCurrentDateCallBack = null;
        }
        if (mControlCalendarCallBack != null) {
            mControlCalendarCallBack = null;
        }
    }
}
