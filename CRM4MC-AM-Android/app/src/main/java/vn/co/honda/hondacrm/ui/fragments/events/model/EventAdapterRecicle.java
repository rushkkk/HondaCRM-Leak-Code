package vn.co.honda.hondacrm.ui.fragments.events.model;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import vn.co.honda.hondacrm.R;

public class EventAdapterRecicle extends RecyclerView.Adapter<EventAdapterRecicle.AuthorViewHolder> implements Parcelable {
    private ArrayList<Events> listEvent;
    private Activity context;
    private int resource;
    public EventAdapterRecicle(Activity context, int resource, ArrayList<Events> listEvent) {
        this.context = context;
        this.resource = resource;
        this.listEvent = listEvent;
    }

    protected EventAdapterRecicle(Parcel in) {
        listEvent = in.createTypedArrayList(Events.CREATOR);
        resource = in.readInt();
    }

    public static final Creator<EventAdapterRecicle> CREATOR = new Creator<EventAdapterRecicle>() {
        @Override
        public EventAdapterRecicle createFromParcel(Parcel in) {
            return new EventAdapterRecicle(in);
        }

        @Override
        public EventAdapterRecicle[] newArray(int size) {
            return new EventAdapterRecicle[size];
        }
    };

    @Override
    public AuthorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View convertView = layoutInflater.inflate(R.layout.item_event,parent,false);

        return new AuthorViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(AuthorViewHolder holder, final int position) {
        if(listEvent.size()>0&&position< listEvent.size()) {
           // Log.d("TAGppposition", "getView: " + listCar.size()+position);
            Events events = listEvent.get(position);
            holder.title.setText(events.getTitle().trim());
            holder.content.setText(events.getContent());
            holder.dateStart.setText(events.getDateStart());
            holder.dateEnd.setText(events.getDateEnd());
            holder.address.setText(events.getAddress());
//            holder.line.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return listEvent.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(listEvent);
        dest.writeInt(resource);
    }

    public class AuthorViewHolder extends RecyclerView.ViewHolder {
        TextView title,content,dateStart,dateEnd,address;
        ImageView image;
        RelativeLayout line;
        public AuthorViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.label_profile_title);
            content = itemView.findViewById(R.id.label_profile_content);
            dateStart = itemView.findViewById(R.id.label_profile_datestart);
            dateEnd = itemView.findViewById(R.id.label_profile_dateEnd);
            address = itemView.findViewById(R.id.label_profile_address);
            image = itemView.findViewById(R.id.image_profile_tabevent);
            //line=itemView.findViewById(R.id.line9);
        }
    }


}