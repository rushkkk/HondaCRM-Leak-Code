package vn.co.honda.hondacrm.ui.adapters.home;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.ui.adapters.notification.NotificationListAdapter;
import vn.co.honda.hondacrm.ui.fragments.home.models.SliderEvents;

public class EventRecycleAdapter extends RecyclerView.Adapter<EventRecycleAdapter.EventHomeViewHolder> {
    private Activity context;
    private int resource;
    private  List<SliderEvents> listEvent;
    private ClickItemEventHome clickItemEventHome;

    public EventRecycleAdapter(Activity context, int resource, List<SliderEvents> listEvent, ClickItemEventHome clickItemEventHome) {
        this.context = context;
        this.resource = resource;
        this.listEvent = listEvent;
        this.clickItemEventHome = clickItemEventHome;
    }
    @NonNull
    @Override
    public EventHomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_events_home, viewGroup, false);
        // Return a new holder instance
        RecyclerView.ViewHolder viewHolder = new EventRecycleAdapter.EventHomeViewHolder(contactView);
        return ( EventRecycleAdapter.EventHomeViewHolder) viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventHomeViewHolder eventHomeViewHolder, int i) {
        SliderEvents events = listEvent.get(i);
        eventHomeViewHolder.title.setText(events.getTitle());
        eventHomeViewHolder.content.setText(events.getContent());
        eventHomeViewHolder.time.setText(events.getStartTime()+" - "+ events.getEndTime());
        eventHomeViewHolder.address.setText(events.getAddress());
        eventHomeViewHolder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItemEventHome.clickOnItem(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listEvent.size();
    }

    public class EventHomeViewHolder extends RecyclerView.ViewHolder {

        private TextView title,content, time, address;
        RelativeLayout line;

        public EventHomeViewHolder(View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.txtTitleEventList);
            content =  itemView.findViewById(R.id.txtContentEventList);
            time =  itemView.findViewById(R.id.txtTimeEventList);
            address =  itemView.findViewById(R.id.txtAddressEventList);
            line = itemView.findViewById(R.id.line_event_home);
        }
    }
    public interface ClickItemEventHome {
        void clickOnItem(int s);

    }
}
