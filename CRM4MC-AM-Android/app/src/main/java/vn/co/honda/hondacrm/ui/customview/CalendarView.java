package vn.co.honda.hondacrm.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.booking_service.DayAvailable;
import vn.co.honda.hondacrm.net.model.booking_service.DayList;
import vn.co.honda.hondacrm.net.model.booking_service.SelectedTimeInDay;
import vn.co.honda.hondacrm.net.model.booking_service.TimeSlot;
import vn.co.honda.hondacrm.ui.activities.booking_service.IControlCalendarCallBack;
import vn.co.honda.hondacrm.ui.activities.booking_service.ICurrentDateCallBack;
import vn.co.honda.hondacrm.utils.Constants;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

public class CalendarView extends LinearLayout {
    private static final int DAYS_COUNT = 42;
    private static final String DATE_FORMAT = "MMM yyyy";
    private String dateFormat;
    private Calendar currentDate = Calendar.getInstance();
    private LinearLayout btnPrevMonth, btnNextMonth;
    private TextView txtDate;
    private GridViewScrollable gridViewScrollable;
    View linearDay;
    private IControlCalendarCallBack mControlCalendarCallBack;
    private ICurrentDateCallBack mCurrentDateCallBack;
    private SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    private List<DayAvailable> daysOfMonth = new ArrayList<>();
    private Context context;
    CalendarAdapter calendarAdapter;
    boolean check = false;
    public CalendarView(Context context) {
        super(context);
        this.context = context;
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
        this.context = context;
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
        this.context = context;
    }

    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);
        loadDateFormat(attrs);
        initViews();
    }

    public void setControlCalendar(IControlCalendarCallBack controlCalendarCallBack) {
        mControlCalendarCallBack = controlCalendarCallBack;
    }

    public void setCurrentDate(ICurrentDateCallBack currentDateCallBack) {
        mCurrentDateCallBack = currentDateCallBack;
    }

    private void loadDateFormat(AttributeSet attrs) {
        @SuppressLint("CustomViewStyleable") TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);
        try {
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        } finally {
            ta.recycle();
        }
    }

    private void initViews() {
        btnPrevMonth = findViewById(R.id.linear_prev_calendar);
        btnNextMonth = findViewById(R.id.linear_next_calendar);
        txtDate = findViewById(R.id.tv_date_display);
        gridViewScrollable = findViewById(R.id.calendar_grid);
        updateCalendar();
        assignClickHandlers();
    }

    private void assignClickHandlers() {
        btnNextMonth.setOnClickListener(v -> {
            currentDate.add(Calendar.MONTH, Constants.NUMBER_ONE);
            getDaysOfMonth();
        });

        btnPrevMonth.setOnClickListener(v -> {
            currentDate.add(Calendar.MONTH, -Constants.NUMBER_ONE);
            getDaysOfMonth();
        });

        gridViewScrollable.setOnItemClickListener((parent, view, position, id) -> {
            linearDay = view;
            if (mControlCalendarCallBack != null) {
                mControlCalendarCallBack.onItemClick((Date) parent.getItemAtPosition(position));
            }

        });
    }

    public void updateCalendar() {
        updateCalendar(null);
    }

    public void getDaysOfMonth() {
        showProgressBar();
        ApiService apiService = ApiClient.getClient(CalendarView.this.getContext()).create(ApiService.class);
        String mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(getContext());
        String date = format.format(currentDate.getTime());
        // truyen dealer id
        apiService.getDayUnavailable(mTypeFullAccessToken, "D001", date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<DayList>() {
                               @Override
                               public void onSuccess(DayList dayList) {
                                   hideProgressBar();
                                   daysOfMonth = dayList.getData();
                                   updateCalendar();
                                   hideProgressBar();
                               }

                               @Override
                               public void onError(Throwable error) {

                               }
                           }
                );
    }

    public void updateCalendar(List<Date> events) {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();
        calendar.set(Calendar.DAY_OF_MONTH, Constants.NUMBER_ONE);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - Constants.NUMBER_ONE;
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, Constants.NUMBER_ONE);
        }

        calendarAdapter = new CalendarAdapter(getContext(), cells, events, daysOfMonth);
        gridViewScrollable.setAdapter(calendarAdapter);
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
        txtDate.setText(sdf.format(currentDate.getTime()));
        String current = formatDate.format(currentDate.getTime());
        String dateSelect = format.format(currentDate.getTime());

    }

    private class CalendarAdapter extends ArrayAdapter<Date> {
        private List<Date> eventDays;
        private List<DayAvailable> daysOfMonth;
        ImageView imgPoint;
        TextView tvDay;
        private LayoutInflater inflater;

        CalendarAdapter(Context context, ArrayList<Date> days, List<Date> eventDays, List<DayAvailable> daysOfMonth) {
            super(context, R.layout.item_calendar_day, days);
            this.eventDays = eventDays;
            this.daysOfMonth = daysOfMonth;
            inflater = LayoutInflater.from(context);
        }

        @NotNull
        @Override
        public View getView(int position, View view, @NotNull ViewGroup parent) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = getItem(position);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();
            Date today = new Date();

            if (view == null) {
                view = inflater.inflate(R.layout.item_calendar_day, parent, false);
                imgPoint = view.findViewById(R.id.img_point);
                tvDay = view.findViewById(R.id.tv_day);
                linearDay = view.findViewById(R.id.linear_day);
            }

            tvDay.setTextColor(Color.BLACK);
            if (today.getYear() != year) {
                view.setClickable(false);
                imgPoint.setImageResource(R.drawable.ic_point_unavailable);
            } else {
                view.setClickable(true);
                if (daysOfMonth != null) {
                    for (DayAvailable dayAvailable : daysOfMonth) {
                        String dayString;
                        if (0 < day && day < 10) {
                            dayString = "0" + day;
                        } else {
                            dayString = String.valueOf(day);
                        }
                        if (dayAvailable.getDay().equals(dayString) && dayAvailable.getStatus() == 1) {
                            imgPoint.setImageResource(R.drawable.ic_point_available);
                            view.setClickable(false);
                            break;
                        }
                    }
                    if (date.equals(today) && imgPoint.getDrawable().equals(getResources().getDrawable(R.drawable.ic_point_available))) {
                        mControlCalendarCallBack.onItemClick(date);
                    } else if(date.after(today) && !check){
                        try{
                        if (day == currentDate.getTime().getDay() && imgPoint.getDrawable().equals(getResources().getDrawable(R.drawable.ic_point_available))) {
                            mControlCalendarCallBack.onItemClick(date);
                            check = true;
                        }
                        }catch (Exception e){
                            Log.d("", "getView111: "+currentDate);
                        }
                    }
                }
            }

            tvDay.setTextColor(Color.BLACK);
            if (!sdf.format(date.getTime()).equals(sdf.format(currentDate.getTime()))) {
                tvDay.setTextColor(getResources().getColor(R.color.colorBlack08));
                imgPoint.setVisibility(GONE);
            }
            tvDay.setText(String.valueOf(date.getDate()));
            return view;
        }
    }

    public void showProgressBar() {
        DialogUtils.showDialogLoadProgress(getContext());
    }

    public void hideProgressBar() {
        DialogUtils.hideDialogLoadProgress();
    }
}
