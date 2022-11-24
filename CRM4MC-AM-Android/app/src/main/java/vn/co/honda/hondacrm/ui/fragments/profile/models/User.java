package vn.co.honda.hondacrm.ui.fragments.profile.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String username;
    private String bithdate;
    private String gender;
    private String email;
    private String job;
    private String id_Number;
    private String address;
    private int inLogin;
    private Bitmap imgUser;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getBithdate() {
        return bithdate;
    }

    public void setBithdate(String bithdate) {
        this.bithdate = bithdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getId_Number() {
        return id_Number;
    }

    public void setId_Number(String id_Number) {
        this.id_Number = id_Number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getInLogin() {
        return inLogin;
    }

    public void setInLogin(int inLogin) {
        this.inLogin = inLogin;
    }

    public Bitmap getImgUser() {
        return imgUser;
    }

    public void setImgUser(Bitmap imgUser) {
        this.imgUser = imgUser;
    }

    protected User(Parcel in) {
        bithdate = in.readString();
        gender = in.readString();
        email = in.readString();
        job = in.readString();
        id_Number = in.readString();
        address = in.readString();
        inLogin = in.readInt();
        username = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public User() {
    }
    public User(String bithdate, String gender, String email, String job, String id_Number, String address) {
        this.bithdate = bithdate;
        this.gender = gender;
        this.email = email;
        this.job = job;
        this.id_Number = id_Number;
        this.address = address;
    }
    public User(String bithdate, String gender, String email, String job, String id_Number, String address,int inLogin) {
        this.bithdate = bithdate;
        this.gender = gender;
        this.email = email;
        this.job = job;
        this.id_Number = id_Number;
        this.address = address;
        this.inLogin=inLogin;
    }

    public User(String bithdate, String gender, String email, String job, String id_Number, String address, int inLogin, Bitmap imgUser) {
        this.bithdate = bithdate;
        this.gender = gender;
        this.email = email;
        this.job = job;
        this.id_Number = id_Number;
        this.address = address;
        this.inLogin = inLogin;
        this.imgUser = imgUser;

    }
    public User(String username,String bithdate, String gender, String email, String job, String id_Number, String address, int inLogin) {
        this.bithdate = bithdate;
        this.gender = gender;
        this.email = email;
        this.job = job;
        this.id_Number = id_Number;
        this.address = address;
        this.inLogin = inLogin;
        this.imgUser = imgUser;
        this.username = username;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bithdate);
        dest.writeString(gender);
        dest.writeString(email);
        dest.writeString(job);
        dest.writeString(id_Number);
        dest.writeString(address);
        dest.writeInt(inLogin);
        dest.writeParcelable(imgUser, flags);
        dest.writeString(username);
    }
}
