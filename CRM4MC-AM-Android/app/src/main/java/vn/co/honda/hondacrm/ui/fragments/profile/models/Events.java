package vn.co.honda.hondacrm.ui.fragments.profile.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HCD-Fresher015 on 3/5/2019.
 */

public class Events implements Parcelable{
    private  int id;
    private String title;
    private String content;
    private String dateStart;
    private String dateEnd;
    private String address;
    private boolean chose;

    protected Events(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        dateStart = in.readString();
        dateEnd = in.readString();
        address = in.readString();
        chose = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(dateStart);
        dest.writeString(dateEnd);
        dest.writeString(address);
        dest.writeByte((byte) (chose ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Events> CREATOR = new Creator<Events>() {
        @Override
        public Events createFromParcel(Parcel in) {
            return new Events(in);
        }

        @Override
        public Events[] newArray(int size) {
            return new Events[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isChose() {
        return chose;
    }

    public void setChose(boolean chose) {
        this.chose = chose;
    }
    public Events(int id, String title, String content, String dateStart, String dateEnd, String address) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.address = address;
    }

    public Events(int id,String title, String content, String dateStart, String dateEnd, String address, boolean chose) {
        this.title = title;
        this.content = content;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.address = address;
        this.chose = chose;
        this.id=id;
    }

    public Events(String title, String content, String dateStart, String dateEnd, String address) {
        this.title = title;
        this.content = content;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.address = address;
    }
}
