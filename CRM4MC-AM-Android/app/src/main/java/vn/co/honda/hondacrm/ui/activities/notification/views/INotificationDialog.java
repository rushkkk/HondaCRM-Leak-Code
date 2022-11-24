package vn.co.honda.hondacrm.ui.activities.notification.views;

import android.content.Context;

import java.util.List;

import vn.co.honda.hondacrm.ui.activities.notification.model.Notification;

public interface INotificationDialog {
    void showPopupNotification();
    void closePupopNotification();
    void setNumberNotification(int number);
    void setListForAdapter(List<Notification> notifications);
    void setHideNumberNotification();
    void showDialogBookingReminder();
    void showDialogRecallReminder(String bks,String dealer);
    void showDialogMaintenanceReminder(String bks,String dealer);
    void showDialogServiceFeedback();
    void hideSwipeRefresh();
    void showProgressBar();
    void hideProgressBar();
}
