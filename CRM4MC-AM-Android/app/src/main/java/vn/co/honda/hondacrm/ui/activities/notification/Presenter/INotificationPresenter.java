package vn.co.honda.hondacrm.ui.activities.notification.Presenter;

import java.util.ArrayList;
import java.util.List;

import vn.co.honda.hondacrm.ui.activities.notification.model.Notification;

public interface INotificationPresenter {
    void setShowPopupNotification();
    void readNotifications();
    void readAllNotification();
    void setShowRecallDialog();
    void setShowMainteanceDialog();
    void setShowSurveyDialog();
}
