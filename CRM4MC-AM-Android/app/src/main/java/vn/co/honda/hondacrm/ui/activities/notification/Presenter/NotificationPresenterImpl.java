package vn.co.honda.hondacrm.ui.activities.notification.Presenter;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.core.ApiClient;
import vn.co.honda.hondacrm.net.model.user.UserProfileResponse;
import vn.co.honda.hondacrm.ui.activities.forgot_pass.view.InputNewPassword;
import vn.co.honda.hondacrm.ui.activities.notification.model.ListNotifications;
import vn.co.honda.hondacrm.ui.activities.notification.model.Notification;
import vn.co.honda.hondacrm.ui.activities.notification.views.INotificationDialog;
import vn.co.honda.hondacrm.utils.DialogUtils;
import vn.co.honda.hondacrm.utils.PrefUtils;

public class NotificationPresenterImpl implements INotificationPresenter {
    private INotificationDialog iNotificationDialog;
    private Context context;
    private ApiService apiService;
    private String mTypeFullAccessToken;
    List<Notification> notifications;
    int numberUnread = 0;


    public NotificationPresenterImpl(Context context, INotificationDialog iNotificationDialog) {
        this.context = context;
        this.iNotificationDialog = iNotificationDialog;
        this.notifications = new ArrayList<>();
        apiService = ApiClient.getClient(context).create(ApiService.class);
        mTypeFullAccessToken = PrefUtils.getFullTypeAccessToken(context);
    }

    @Override
    public void setShowPopupNotification() {
       // iNotificationDialog.showProgressBar();
        numberUnread = 0;
        apiService.getListNotification(
                mTypeFullAccessToken
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ListNotifications>() {
                    @Override
                    public void onSuccess(ListNotifications response) {
                       // iNotificationDialog.hideProgressBar();
                        if (response.getData().size() > 0) {
                           iNotificationDialog.setListForAdapter(response.getData());
                            for (Notification x : response.getData()) {
                                if (x.getReadAt() == null) {
                                    numberUnread += 1;
                                }
                            }
                        }
                        if (numberUnread > 0) {
                            iNotificationDialog.setNumberNotification(numberUnread);
                        } else {
                            iNotificationDialog.setHideNumberNotification();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtils.showDialogConfirmLogin(context, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, context.getString(R.string.networkError));
                       // iNotificationDialog.hideProgressBar();
                        iNotificationDialog.hideSwipeRefresh();
                    }
                });

    }

    @Override
    public void readNotifications() {
       // iNotificationDialog.showProgressBar();
        numberUnread = 0;
        apiService.getListNotification(
                mTypeFullAccessToken
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ListNotifications>() {
                    @Override
                    public void onSuccess(ListNotifications response) {
                        //iNotificationDialog.hideProgressBar();
                        if (response.getData().size() > 0) {
                            for (Notification x : response.getData()) {
                                if (x.getReadAt() == null) {
                                    numberUnread += 1;
                                }
                            }
                        }
                        if (numberUnread > 0) {
                            iNotificationDialog.setNumberNotification(numberUnread);
                        } else {
                            iNotificationDialog.setHideNumberNotification();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtils.showDialogConfirmLogin(context, R.layout.dialog_add_vehicle_success, 0.9f, 0.5f, context.getString(R.string.networkError));
                        //iNotificationDialog.hideProgressBar();
                    }
                });

    }

    @Override
    public void readAllNotification() {

    }

    @Override
    public void setShowRecallDialog() {

    }

    @Override
    public void setShowMainteanceDialog() {

    }

    @Override
    public void setShowSurveyDialog() {

    }
}
