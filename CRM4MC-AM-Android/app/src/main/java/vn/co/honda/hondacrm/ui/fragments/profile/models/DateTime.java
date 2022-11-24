package vn.co.honda.hondacrm.ui.fragments.profile.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class DateTime implements Parcelable {
    private int dayOfMonth;

    public DateTime() {
        // no instance
    }
    public DateTime(Parcel in) {
        dayOfMonth = in.readInt();
        month = in.readInt();
        year = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dayOfMonth);
        dest.writeInt(month);
        dest.writeInt(year);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DateTime> CREATOR = new Creator<DateTime>() {
        @Override
        public DateTime createFromParcel(Parcel in) {
            return new DateTime(in);
        }

        @Override
        public DateTime[] newArray(int size) {
            return new DateTime[size];
        }
    };

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    private int month;
    private int year;
}
