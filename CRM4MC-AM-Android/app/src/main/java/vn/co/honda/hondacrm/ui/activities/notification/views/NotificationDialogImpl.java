package vn.co.honda.hondacrm.ui.activities.notification.views;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.notification.BookingReminderDialog;
import vn.co.honda.hondacrm.ui.activities.notification.Presenter.INotificationPresenter;
import vn.co.honda.hondacrm.ui.activities.notification.Presenter.NotificationPresenterImpl;
//import vn.co.honda.hondacrm.ui.activities.notification.RecallReminderDialog;
import vn.co.honda.hondacrm.ui.activities.notification.RecallReminderDialog;
import vn.co.honda.hondacrm.ui.activities.notification.ServiceFeedbackDialog;
import vn.co.honda.hondacrm.ui.activities.notification.model.Notification;
import vn.co.honda.hondacrm.ui.adapters.notification.NotificationListAdapter;
import vn.co.honda.hondacrm.utils.DialogUtils;

public class NotificationDialogImpl extends PopupWindow implements NotificationListAdapter.ClickItemNotifi, INotificationDialog, SwipeRefreshLayout.OnRefreshListener {
    // public static final int PERMISSION_GRANTED=0;
    private RecyclerView recyclerView;
    private NotificationListAdapter notificationListAdapter;
    private List<Notification> notifications;
    private List<Notification> notificationsTemp;

    private Activity context;
    private ImageButton btnDismiss;
    float numberRating = 0.0f;
    private PopupWindow pw;
    private View layout;
    private TextView txtNumberNotify;
    private INotificationPresenter iNotificationPresenter;
    private View itemNotification;
    boolean isLoading = false;
    private FrameLayout conTroNotify;
    private int items_limited = 0;
    private SwipeRefreshLayout swipeContainer;

    private final int callbackId = 42;

    public NotificationDialogImpl(Activity context) {
        super(context);
        this.context = context;
        notifications = new ArrayList<>();
        notificationsTemp = new ArrayList<>();
        iNotificationPresenter = new NotificationPresenterImpl(context, this);
        iNotificationPresenter.readNotifications();
        this.txtNumberNotify = this.context.findViewById(R.id.txt_numder_notify);
        this.itemNotification = this.context.findViewById(R.id.containerNotify);
        popupNotification();

        checkPermissions(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);
    }

    public void popupNotification() {

    }
    public static Rect locateView(View v)
    {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try
        {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe)
        {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

    @Override
    public void showPopupNotification() {
        //itemNotification.setEnabled(false);
        items_limited = 20;
        int x = (int) itemNotification.getX();
        int y = (int) itemNotification.getY();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.dialog_list_notification,
                context.findViewById(R.id.popupnotify));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        pw = new PopupWindow(layout, (int) (context.getResources().getDisplayMetrics().widthPixels * 0.93f), (int) (height - (height*0.13 )), false);
        ImageButton btnDismiss = layout.findViewById(R.id.btn_dismiss_dialog_notification);
        recyclerView = layout.findViewById(R.id.lv_notification_list);
        conTroNotify = layout.findViewById(R.id.con_tro_notify);
        swipeContainer = layout.findViewById(R.id.swipeNotify);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(55, 55);
        layoutParams.setMargins((int) (x * 0.95), 0, 0, 0);
        conTroNotify.setLayoutParams(layoutParams);
        iNotificationPresenter.setShowPopupNotification();
        pw.setOutsideTouchable(true);
        initScrollListener();
        pw.setAnimationStyle(android.R.transition.slide_top);
        pw.showAtLocation(layout,  Gravity.CENTER, 0, y-20);

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePupopNotification();
            }
        });


    }

    @Override
    public void setListForAdapter(List<Notification> notificationss) {
        hideSwipeRefresh();
        notifications.clear();
        this.notificationsTemp = notificationss;
        Collections.sort(notificationsTemp, new Comparator<Notification>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            @Override
            public int compare(Notification o1, Notification o2) {
                try {
                    return f.parse(o2.getCreatedAt()).compareTo(f.parse(o1.getCreatedAt()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        if (notificationsTemp.size() >= items_limited) {
            for (int i = 0; i < items_limited; i++) {
                notifications.add(notificationsTemp.get(i));
            }
        } else {
            notifications.addAll(notificationsTemp);
        }
        notificationListAdapter = new NotificationListAdapter(context, R.layout.item_profile_event_edit, notifications, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notificationListAdapter);
        notificationListAdapter.notifyDataSetChanged();
        swipeContainer.setOnRefreshListener(this);
    }

    @Override
    public void closePupopNotification() {
        //itemNotification.setEnabled(true);
        if (pw != null) {
            boolean isShow = pw.isShowing();
            if (isShow) {
                pw.dismiss();
            }
        }
    }

    @Override
    public void setNumberNotification(int number) {
        this.txtNumberNotify.setVisibility(View.VISIBLE);
        if (number >= 99) {
            this.txtNumberNotify.setText(99 + "+");
        } else {
            this.txtNumberNotify.setText(number + "");
        }
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int first = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (recyclerView.getChildAt(0) != null) {
                    swipeContainer.setEnabled(first == 0 && recyclerView.getChildAt(0).getTop() == 0);
                }
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == notifications.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        notifications.add(null);
        notificationListAdapter.notifyItemInserted(notifications.size() - 1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifications.remove(notifications.size() - 1);
//                int scrollPosition = notifications.size();
//                notificationListAdapter.notifyItemRemoved(scrollPosition);

                if (notifications.size() + items_limited <= notificationsTemp.size()) {
                    items_limited = notifications.size() + items_limited;
                    for (int i = notifications.size(); i < notificationsTemp.size(); i++) {
                        notifications.add(notificationsTemp.get(i));
                    }
                } else {
                    for (int i = notifications.size(); i < notificationsTemp.size(); i++) {
                        notifications.add(notificationsTemp.get(i));
                    }
                }
                notificationListAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);

    }

    @Override
    public void setHideNumberNotification() {
        this.txtNumberNotify.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showDialogBookingReminder() {
        BookingReminderDialog bookingReminderDialog = new BookingReminderDialog(context);
        bookingReminderDialog.show();
        TextView tvVehicel, tvService, tvTime, tvAddress;
        tvVehicel = bookingReminderDialog.findViewById(R.id.txt_vehicle_booking);
        tvService = bookingReminderDialog.findViewById(R.id.txt_service);
        tvTime = bookingReminderDialog.findViewById(R.id.txt_time_booking);
        tvAddress = bookingReminderDialog.findViewById(R.id.txt_address_booking);

    }

    @Override
    public void showDialogRecallReminder(String bks, String dealer) {
        String title = "Recall Reminder";
        String content = "Xe Honda BKS [" + bks + "] của Quý Khách đã đến thời gian làm bảo dưỡng định kì [cấp bảo dưỡng]," +
                " Quý Khách vui lòng đưa xe đến [" + dealer + "] làm dịch vụ để xe có thể hoạt động trong tình trạng tốt nhất. Cảm ơn Quý Khách.";
        RecallReminderDialog recallReminder = new RecallReminderDialog(context, title, content);
        recallReminder.show();

    }

    @Override
    public void showDialogMaintenanceReminder(String bks, String dealer) {
        String title = "Maintenance Reminder";
        String content = "Xe Honda BKS [" + bks + "] của Quý Khách cần triệu hồi để sửa lỗi [recall infor]," +
                "Quý Khách vui lòng đưa xe đến [" + dealer + "] làm dịch vụ để xe có thể hoạt động trong tình trạng tốt nhất.";
        RecallReminderDialog recallReminder = new RecallReminderDialog(context, title, content);
        recallReminder.show();

    }

    @Override
    public void showDialogServiceFeedback() {

        ServiceFeedbackDialog serviceFeedbackDialog = new ServiceFeedbackDialog(context);
        serviceFeedbackDialog.show();
        RatingBar rtFeedback = serviceFeedbackDialog.findViewById(R.id.ratingBar_feedback);
        rtFeedback.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                numberRating = rating;
            }
        });
    }

    @Override
    public void hideSwipeRefresh() {
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void showProgressBar() {
        DialogUtils.showDialogLoadProgress(context);
    }

    @Override
    public void hideProgressBar() {
        DialogUtils.hideDialogLoadProgress();
    }

    @Override
    public void clickOnItem(int s) {


        if (3 == s) {
            showDialogRecallReminder("73-N8 00412", "Honda My Dinh");
        } else if (2 == s) {
            showDialogMaintenanceReminder("73-N8 00412", "Honda My Dinh");
        } else if (4 == s) {
            showDialogServiceFeedback();
        } else if (1 == s) {
            showDialogBookingReminder();
        } else {

        }
//        if (checkPermissions(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)) {
//            long calID = 3;
//            long startMillis = 0;
//            long endMillis = 0;
//            Calendar beginTime = Calendar.getInstance();
//            beginTime.set(2019, 6, 27, 2, 27);
//            startMillis = beginTime.getTimeInMillis();
//            Calendar endTime = Calendar.getInstance();
//            endTime.set(2019, 6, 27, 2, 30);
//            endMillis = endTime.getTimeInMillis();
//
//
//            ContentResolver cr = context.getContentResolver();
//            ContentValues values = new ContentValues();
//            values.put(CalendarContract.Events.CALENDAR_ID, 1);
//            values.put(CalendarContract.Events.TITLE, "calendar://1");
//            values.put(CalendarContract.Events.DTSTART, startMillis);
//            values.put(CalendarContract.Events.DTEND, endMillis);
//            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
//
//            values.put(CalendarContract.Events.CUSTOM_APP_PACKAGE, context.getPackageName());
//            values.put(CalendarContract.Events.CUSTOM_APP_URI, "calendar://1");
//            Uri uri = cr.insert(Events.CONTENT_URI, values);
//
//            long eventID = Long.parseLong(uri.getLastPathSegment());
//            Toast.makeText(context, "event id = " + eventID, Toast.LENGTH_SHORT).show();
//        }else {
//            checkPermissions(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);
//        }
    }

    private boolean checkPermissions(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_GRANTED;
        }

        if (!permissions) {
            ActivityCompat.requestPermissions(context, permissionsId, callbackId);
        }
        return permissions;
    }

    @Override
    public void clickOnItemClickHere() {

    }

    @Override
    public void onRefresh() {
        iNotificationPresenter.setShowPopupNotification();

    }
}
