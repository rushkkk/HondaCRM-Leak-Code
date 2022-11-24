package vn.co.honda.hondacrm.ui.fragments.profile;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.model.user.Event;
import vn.co.honda.hondacrm.utils.DateTimeUtils;

public class EventEditAdapterRecicle extends RecyclerView.Adapter<EventEditAdapterRecicle.AuthorViewHolder> implements Parcelable {
    private List<Event> listEvent;
    private Activity context;
    private int resource;
    boolean isEdit,isChose;
    ArrayList<Integer> listId_Not;
    ArrayList<Integer> listId;
    public ClickCheckbox clickCheckbox;
    public  ClickCheckboxAllEvent clickCheckboxAllEvent;
    public EventEditAdapterRecicle(Activity context, int resource, List<Event> listEvent, ClickCheckbox clickCheckbox , ClickCheckboxAllEvent clickCheckboxAllEvent) {
        this.context = context;
        this.resource = resource;
        this.clickCheckbox = clickCheckbox;
        this.listEvent = listEvent;
        listId = new ArrayList<>();
        listId_Not= new ArrayList<>();
        this.clickCheckboxAllEvent=clickCheckboxAllEvent;

    }


    protected EventEditAdapterRecicle(Parcel in) {
        resource = in.readInt();
        isEdit = in.readByte() != 0;
        isChose = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resource);
        dest.writeByte((byte) (isEdit ? 1 : 0));
        dest.writeByte((byte) (isChose ? 1 : 0));
    }


    public static final Creator<EventEditAdapterRecicle> CREATOR = new Creator<EventEditAdapterRecicle>() {
        @Override
        public EventEditAdapterRecicle createFromParcel(Parcel in) {
            return new EventEditAdapterRecicle(in);
        }

        @Override
        public EventEditAdapterRecicle[] newArray(int size) {
            return new EventEditAdapterRecicle[size];
        }
    };

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }
    public void setChoseAllEvent(boolean isChose) {
        this.isChose = isChose;
        notifyDataSetChanged();
    }



    @Override
    public AuthorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View convertView = layoutInflater.inflate(R.layout.item_profile_event_edit, parent, false);

        return new AuthorViewHolder(convertView);
    }
    private String convertStringObject(String object) {
        return object == null ? "" : object;
    }
    @Override
    public void onBindViewHolder(AuthorViewHolder holder, final int position) {

        // Log.d("TAGppposition", "getView: " + listCar.size()+position);

        Event events = listEvent.get(position);
        if(events.getKeyVisualImage()!=null&&events.getTitle()!=null && events.getContent()!=null && events.getEventStartDate()!=null && events.getEventEndDate()!=null && events.getPlaceOfEvent()!=null) {
            //set image view
            Picasso.with(context)
                    .load(events.getKeyVisualImage())
                    .placeholder(R.drawable.honda)
                    .error(R.drawable.honda)
                    .into(holder.image);
            holder.title.setText(events.getTitle().trim());
            holder.content.setText(events.getContent());
            holder.dateStart.setText(convertStringObject(DateTimeUtils.convertToDisplay(events.getEventStartDate())));
            holder.dateEnd.setText(convertStringObject(DateTimeUtils.convertToDisplay(events.getEventEndDate())));
            holder.address.setText(events.getPlaceOfEvent());
            holder.line.setOnClickListener(v -> Log.d("TAGpp", "getView: " + position));

            if (events.isChose()) {
                holder.selectionState.setChecked(true);
            } else {
                holder.selectionState.setChecked(false);
            }

            if (isEdit) {
                holder.selectionState.setVisibility(View.VISIBLE);
                holder.line_event.setVisibility(View.VISIBLE);
//                if(position==(listEvent.size()-1)){
//                    holder.line_event.setVisibility(View.GONE);
//                }

            } else {
                holder.selectionState.setVisibility(View.GONE);
                holder.line_event.setVisibility(View.GONE);
            }

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



    public class AuthorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, content, dateStart, dateEnd, address;
        ImageView image;
        View line,line_event;
        public CheckBox selectionState;

        public AuthorViewHolder(View itemView) {
            super(itemView);
            selectionState = itemView.findViewById(R.id.checkbox_select);
            title = itemView.findViewById(R.id.label_profile_title);
            content = itemView.findViewById(R.id.label_profile_content);
            dateStart = itemView.findViewById(R.id.label_profile_datestart);
            dateEnd = itemView.findViewById(R.id.label_profile_dateEnd);
            address = itemView.findViewById(R.id.label_profile_address);
            image = itemView.findViewById(R.id.image_profile_tabevent);
            line = itemView.findViewById(R.id.line9);
            line_event=itemView.findViewById(R.id.line_event);
            itemView.setOnClickListener(this);
            selectionState.setOnCheckedChangeListener((buttonView, isChecked) -> {



                if (isChecked) {
                    listId.add(listEvent.get(getAdapterPosition()).getId());
                    clickCheckbox.clickOnItem(listId);
                } else {
                    if (listId.size() > 0) {
                        clickCheckboxAllEvent.clickOnItemCheck(false);
                        if(isChose){
                            listId.clear();
                            return;
                        }
                        listId_Not.clear();
                        for (Integer integer : listId) {
                            listId_Not.add(integer);
                        }
                        for (Integer item : listId_Not) {
                            if (item.intValue() == listEvent.get(getAdapterPosition()).getId()) {
                                listId.remove(listEvent.get(getAdapterPosition()).getId());
                            }
                        }
                        clickCheckbox.clickOnItem(listId);
                    }
                }

//                    //gfdhgfh
//                    if (isChecked) {
//                        if (listId.size() >= 0) {
//                            listId.add(listEvent.get(getAdapterPosition()).getId());
//                            clickCheckbox.clickOnItem(listId);
//                        }
//                    } else {
//
//                    }

            });

        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface ClickCheckbox {
        void clickOnItem(ArrayList<Integer> listId);
    }
    public interface ClickCheckboxAllEvent {
        void clickOnItemCheck(Boolean aBoolean);
    }
}
