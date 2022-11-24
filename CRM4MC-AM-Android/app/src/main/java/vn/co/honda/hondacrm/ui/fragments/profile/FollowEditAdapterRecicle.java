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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import vn.co.honda.hondacrm.R;
import vn.co.honda.hondacrm.net.model.user.Follow;
import vn.co.honda.hondacrm.ui.activities.profile.ProfileActivity;
import vn.co.honda.hondacrm.ui.fragments.profile.models.Car;


public class FollowEditAdapterRecicle extends RecyclerView.Adapter<FollowEditAdapterRecicle.AuthorViewHolder> implements Parcelable {
    private Activity context;
    List<Follow> mOriginCars;
    private int resource;
    List<Integer> listId;
    List<Integer> listId_Not;
    public ClickCheckbox clickCheckbox;
    public ClickCheckboxAll clickCheckboxAll;
    public ClickCheckboxNot clickCheckboxNot;
    //change view

    boolean isEdit, isChose, isChoseTrue;

    public FollowEditAdapterRecicle(Activity context, int resource, List<Follow> listCar, ClickCheckbox clickCheckbox, ClickCheckboxAll clickCheckboxAll, ClickCheckboxNot clickCheckboxNot) {
        this.context = context;
        this.resource = resource;
        this.clickCheckbox = clickCheckbox;
        this.clickCheckboxAll = clickCheckboxAll;
        this.mOriginCars = new ArrayList<>();
        this.mOriginCars = listCar;
        this.clickCheckboxNot = clickCheckboxNot;
        listId = new ArrayList<>();
        listId_Not = new ArrayList<>();
    }


    protected FollowEditAdapterRecicle(Parcel in) {
        resource = in.readInt();
        isEdit = in.readByte() != 0;
        isChose = in.readByte() != 0;
        isChoseTrue = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resource);
        dest.writeByte((byte) (isEdit ? 1 : 0));
        dest.writeByte((byte) (isChose ? 1 : 0));
        dest.writeByte((byte) (isChoseTrue ? 1 : 0));
    }

    public static final Creator<FollowEditAdapterRecicle> CREATOR = new Creator<FollowEditAdapterRecicle>() {
        @Override
        public FollowEditAdapterRecicle createFromParcel(Parcel in) {
            return new FollowEditAdapterRecicle(in);
        }

        @Override
        public FollowEditAdapterRecicle[] newArray(int size) {
            return new FollowEditAdapterRecicle[size];
        }
    };

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }

    public void setChoseAll(boolean isChose) {
        this.isChose = isChose;
        notifyDataSetChanged();
    }

    public void setChoseAllTrue(boolean isChoseTrue) {
        this.isChoseTrue = isChoseTrue;
        notifyDataSetChanged();
    }


    @Override
    public AuthorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View convertView = layoutInflater.inflate(R.layout.item_car_edit, parent, false);

        return new AuthorViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(AuthorViewHolder holder, final int position) {
        Log.d("TAGpp", "getView: " + position);


        if (!mOriginCars.isEmpty() ) {
            Follow car = mOriginCars.get(position);
            if (car.getBannerImage() != null && car.getProductName() != null && car.getPrice() != null) {
                //set image view
                Picasso.with(context)
                        .load(car.getBannerImage())
                        .placeholder(R.mipmap.fake_image_car_tabfollow)
                        .error(R.mipmap.fake_image_car_tabfollow)
                        .into(holder.image);
                holder.name.setText(car.getProductName());
                holder.price.setText(car.getPrice() + " VND");

                if (car.isChose()) {
                    holder.selectionState.setChecked(true);
                } else {
                    holder.selectionState.setChecked(false);
                }

                if (isEdit) {
                    holder.selectionState.setVisibility(View.VISIBLE);
                } else {
                    holder.selectionState.setVisibility(View.INVISIBLE);
                }

                holder.line.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("TAGpp", "getView: " + position);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return mOriginCars.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public class AuthorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CheckBox selectionState;
        TextView name, price;
        ImageView image;
        LinearLayout line;

        public AuthorViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textnamecar);
            price = itemView.findViewById(R.id.textprice);
            image = itemView.findViewById(R.id.imageCarProfileTab1);
            line = itemView.findViewById(R.id.line5);
            selectionState = itemView.findViewById(R.id.brand_select);

            itemView.setOnClickListener(this);

            selectionState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if(isChose&&(getAdapterPosition()==1)){
//                        for (Car car : mOriginCars) {
//                            listId.add(car.getId());
//                        }
//                        clickCheckbox.clickOnItem(listId);
//                        //listId.clear();
//                        return ;
//                    }

                    if (isChecked) {
                        listId.add(mOriginCars.get(getAdapterPosition()).getId());
                        clickCheckbox.clickOnItem(listId);
                        clickCheckboxAll.clickOnItemCheck(true);
                    } else {
                        if (listId.size() > 0) {
                            clickCheckboxAll.clickOnItemCheck(false);
                            if (isChose) {
                                listId.clear();
                                return;
                            }
                            listId_Not.clear();
                            for (Integer integer : listId) {
                                listId_Not.add(integer);
                            }
                            for (Integer item : listId_Not) {
                                if (item == mOriginCars.get(getAdapterPosition()).getId()) {
                                    listId.remove(mOriginCars.get(getAdapterPosition()).getId());
                                }
                            }
                            clickCheckboxNot.clickOnItemNot(listId);
                        }
                    }
                }


            });
        }

        @Override
        public void onClick(View v) {
            TextView brndName = (TextView) v.findViewById(R.id.textnamecar);
        }
    }

    public interface ClickCheckbox {
        void clickOnItem(List<Integer> listId);
    }

    public interface ClickCheckboxNot {
        void clickOnItemNot(List<Integer> listId);
    }

    public interface ClickCheckboxAll {
        void clickOnItemCheck(Boolean aBoolean);
    }

}
