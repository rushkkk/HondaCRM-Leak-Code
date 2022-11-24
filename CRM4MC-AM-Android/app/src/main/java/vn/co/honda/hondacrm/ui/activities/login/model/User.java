package vn.co.honda.hondacrm.ui.activities.login.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String numberPhone;
    private String password;
    private String fullName;
    private String avatar;
    private String gender;
    private String birtDay;
    private String email;

    public User(  String fullName, String avatar, String gender, String birtDay, String email) {
        this.fullName = fullName;
        this.avatar = avatar;
        this.gender = gender;
        this.birtDay = birtDay;
        this.email =  email;
    }

    public User() {
    }

    public User(String numberPhone, String password) {
        this.numberPhone = numberPhone;
        this.password = password;
    }

    public User(String numberPhone, String fullName, String avatar) {
        this.numberPhone = numberPhone;
        this.fullName = fullName;
        this.avatar = avatar;
    }

    protected User(Parcel in) {
        numberPhone = in.readString();
        password = in.readString();
        fullName = in.readString();
        avatar = in.readString();
        gender = in.readString();
        birtDay = in.readString();
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirtDay() {
        return birtDay;
    }

    public void setBirtDay(String birtDay) {
        this.birtDay = birtDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(numberPhone);
        dest.writeString(password);
        dest.writeString(fullName);
        dest.writeString(avatar);
        dest.writeString(gender);
        dest.writeString(birtDay);
        dest.writeString(email);
    }
}
