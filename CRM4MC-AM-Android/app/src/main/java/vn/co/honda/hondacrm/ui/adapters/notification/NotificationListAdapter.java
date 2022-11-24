package vn.co.honda.hondacrm.ui.adapters.notification;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.activities.notification.model.Notification;

public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity context;
    List<Notification> mNotifications;
    private int resource;
    private ClickItemNotifi clickItemNotifi;
    long year = 0;
    long month = 0;
    long date = 0;
    long hours = 0;
    long minutes = 0;
    long seccond = 0;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;


    //change view

    boolean isEdit;

    public NotificationListAdapter(Activity context, int resource, List<Notification> listNotifi, ClickItemNotifi clickItemNotifi) {
        this.context = context;
        this.resource = resource;
        this.mNotifications = listNotifi;
        this.clickItemNotifi = clickItemNotifi;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.item_notification, parent, false);
            // Return a new holder instance
            ViewHolder viewHolder = new NotificationViewHolder(contactView);
            return (NotificationViewHolder) viewHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_notify, parent, false);
            return new LoadingViewHolder(view);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (viewHolder instanceof NotificationViewHolder) {
            notifyItemRows((NotificationViewHolder) viewHolder, i);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, i);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return mNotifications.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mNotifications == null ? 0 : mNotifications.size();
    }


    public class NotificationViewHolder extends ViewHolder {

        TextView title, content, timeAgo;
        LinearLayout line;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title_item_notification);
            content = itemView.findViewById(R.id.txt_content_item_notifi);
            timeAgo = itemView.findViewById(R.id.txt_time_ago);
            line = itemView.findViewById(R.id.item_notification);
        }
    }

    private class LoadingViewHolder extends ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public interface ClickItemNotifi {
        void clickOnItem(int s);

        void clickOnItemClickHere();
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void notifyItemRows(NotificationViewHolder notificationViewHolder, int i) {
        String text ="";
        if (mNotifications.get(i).getData()!= null) {
             text = mNotifications.get(i).getData();
        }
        String textTypeNotify = mNotifications.get(i).getType();
        String wordToFind = "Click here";
        if (mNotifications.get(i).getReadAt() != null) {
            notificationViewHolder.title.setTextColor(ContextCompat.getColor(context, R.color.colorGrayNotifi));
            notificationViewHolder.line.setBackground(context.getDrawable(R.drawable.bg_list_notification_seened));
        } else {
            notificationViewHolder.title.setTextColor(ContextCompat.getColor(context, R.color.colorBlueNotifiTitle));
            notificationViewHolder.line.setBackground(context.getDrawable(R.drawable.bg_list_notification_not_seen));
        }

        if (text.indexOf(wordToFind) != -1) {
            int start = 0;
            int end = 0;
            Pattern word = Pattern.compile(wordToFind);
            Matcher match = word.matcher(text);

            while (match.find()) {
                start = match.start();
                end = match.end();
            }
            // show access policy
            Spannable spannableString = new SpannableString(text);
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorBlueNotifi)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            notificationViewHolder.content.setText(spannableString);

        } else {
            if (text.length() > 90) {
                notificationViewHolder.content.setText(text.substring(0, 88) + "...");
            } else {
                notificationViewHolder.content.setText(text);
            }
        }
        notificationViewHolder.title.setText(mNotifications.get(i).getNotifiableType());
        String dateStart = mNotifications.get(i).getCreatedAt();


        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String dateStop = format.format(new Date());

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long diffMonth = (diff / (24 * 60 * 60 * 1000)) / 30;
            long diffYear = ((diff / (24 * 60 * 60 * 1000)) / 30) / 12;
            year = diffYear;
            month = diffMonth;
            date = diffDays;
            hours = diffHours;
            minutes = diffMinutes;
            seccond = diffSeconds;

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (year > 0) {
            if (year == 1) {
                notificationViewHolder.timeAgo.setText("A year ago");
            } else {
                notificationViewHolder.timeAgo.setText(year + " years ago");
            }
        } else if (month > 0) {
            if (month == 1) {
                notificationViewHolder.timeAgo.setText("A month ago");
            } else {
                notificationViewHolder.timeAgo.setText(month + " months ago");
            }
        } else if (date > 0) {
            if (date == 1) {
                notificationViewHolder.timeAgo.setText("A day ago");
            } else {
                notificationViewHolder.timeAgo.setText(date + " days ago");
            }
        } else if (hours > 0) {
            if (hours == 1) {
                notificationViewHolder.timeAgo.setText("A hour ago");
            } else {
                notificationViewHolder.timeAgo.setText(hours + " hours ago");
            }
        } else if (minutes > 0) {
            if (minutes == 1) {
                notificationViewHolder.timeAgo.setText("A minute ago");
            } else {
                notificationViewHolder.timeAgo.setText(minutes + " minutes ago");
            }

        } else if (seccond > 0) {
            if (seccond == 1) {
                notificationViewHolder.timeAgo.setText("A seccond ago");
            } else {
                notificationViewHolder.timeAgo.setText(seccond + " secconds ago");
            }

        }


        notificationViewHolder.line.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int typeNotification = 0;
                int type = Integer.parseInt(textTypeNotify);
                if (type == 1) {
                    typeNotification = 1;
                } else if (type == 2) {
                    typeNotification = 2;
                } else if (type == 3) {
                    typeNotification = 3;
                } else if (type == 4) {
                    typeNotification = 4;
                } else {
                    typeNotification = 0;
                }
                clickItemNotifi.clickOnItem(typeNotification);
                notificationViewHolder.title.setTextColor(ContextCompat.getColor(context, R.color.colorGrayNotifi));
                notificationViewHolder.line.setBackground(context.getDrawable(R.drawable.bg_list_notification_seened));
            }
        });

    }

}