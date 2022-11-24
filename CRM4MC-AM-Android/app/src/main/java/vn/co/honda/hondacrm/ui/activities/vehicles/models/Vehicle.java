package vn.co.honda.hondacrm.ui.activities.vehicles.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Vehicle implements Parcelable {

    private String name;
    private String license_plate;
    private String VIN;
    private String model;
    private String type;
    private String nextPMDate;
    private String brand;
    private String color;
    private boolean isWarning;
    private boolean isConnected;
    private boolean isHaveConnectFunc;
    private Bitmap imgVehicle;

    public Vehicle() {
    }

    public Vehicle(String name, String license_plate, String VIN, String model, String type, String nextPMDate, String brand, String color, boolean isWarning, boolean isConnected, boolean isHaveConnectFunc, Bitmap imgVehicle) {
        this.name = name;
        this.license_plate = license_plate;
        this.VIN = VIN;
        this.model = model;
        this.type = type;
        this.nextPMDate = nextPMDate;
        this.brand = brand;
        this.color = color;
        this.isWarning = isWarning;
        this.isConnected = isConnected;
        this.isHaveConnectFunc = isHaveConnectFunc;
        this.imgVehicle = imgVehicle;
    }

    protected Vehicle(Parcel in) {
        name = in.readString();
        license_plate = in.readString();
        VIN = in.readString();
        model = in.readString();
        type = in.readString();
        nextPMDate = in.readString();
        brand = in.readString();
        color = in.readString();
        isWarning = in.readByte() != 0;
        isConnected = in.readByte() != 0;
        isHaveConnectFunc = in.readByte() != 0;
        imgVehicle = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNextPMDate() {
        return nextPMDate;
    }

    public void setNextPMDate(String nextPMDate) {
        this.nextPMDate = nextPMDate;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isHaveConnectFunc() {
        return isHaveConnectFunc;
    }

    public void setHaveConnectFunc(boolean haveConnectFunc) {
        isHaveConnectFunc = haveConnectFunc;
    }

    public Bitmap getImgVehicle() {
        return imgVehicle;
    }

    public void setImgVehicle(Bitmap imgVehicle) {
        this.imgVehicle = imgVehicle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(license_plate);
        parcel.writeString(VIN);
        parcel.writeString(model);
        parcel.writeString(type);
        parcel.writeString(nextPMDate);
        parcel.writeString(brand);
        parcel.writeString(color);
        parcel.writeByte((byte) (isWarning ? 1 : 0));
        parcel.writeByte((byte) (isConnected ? 1 : 0));
        parcel.writeByte((byte) (isHaveConnectFunc ? 1 : 0));
        parcel.writeParcelable(imgVehicle, i);
    }
}
