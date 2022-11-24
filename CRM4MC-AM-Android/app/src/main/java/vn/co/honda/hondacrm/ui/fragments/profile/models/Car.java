package vn.co.honda.hondacrm.ui.fragments.profile.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by FPT.
 */

public class Car implements Parcelable{
    private  int id;
    private String carName;
    private String price;
    private int  image;
    private boolean chose;

    protected Car(Parcel in) {
        id = in.readInt();
        carName = in.readString();
        price = in.readString();
        image = in.readInt();
        chose = in.readByte() != 0;
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isChose() {
        return chose;
    }

    public void setChose(boolean chose) {
        this.chose = chose;
    }

    public Car(String carName, String price) {
        this.carName = carName;
        this.price = price;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public Car(String carName, String price, boolean chose) {
        this.carName = carName;
        this.price = price;
        this.chose = chose;
    }
    public Car(int id,String carName, String price, boolean chose) {
        this.carName = carName;
        this.id = id;
        this.price = price;
        this.chose = chose;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(carName);
        dest.writeString(price);
        dest.writeInt(image);
        dest.writeByte((byte) (chose ? 1 : 0));
    }
}
