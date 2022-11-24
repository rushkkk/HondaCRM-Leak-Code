package vn.co.honda.hondacrm.ui.fragments.testdrive.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import vn.co.honda.hondacrm.net.model.base.User;
import vn.co.honda.hondacrm.net.model.dealer.Dealers;
import vn.co.honda.hondacrm.net.model.user.UserProfile;
import vn.co.honda.hondacrm.net.model.vehicle.Vehicle;

public class TestDrive implements Parcelable {

    private String vin;
    private String timeSelected;
    private String dateSelected;
    private Dealers dealer;
    private String typeService;
    private String yourAdditionalInfor;
    private String expectDate;
    private String expectTime;
    private String nameVin;
    private UserProfile user;
    private Vehicle vehicle;


    public TestDrive() {
    }

    public TestDrive(String vin, String timeSelected, String dateSelected, Dealers dealer, String typeService, String yourAdditionalInfor, String expectDate, String expectTime) {
        this.vin = vin;
        this.timeSelected = timeSelected;
        this.dateSelected = dateSelected;
        this.dealer = dealer;
        this.typeService = typeService;
        this.yourAdditionalInfor = yourAdditionalInfor;
        this.expectDate = expectDate;
        this.expectTime = expectTime;
    }

    public TestDrive(String vin, String timeSelected, String dateSelected, Dealers dealer) {
        this.vin = vin;
        this.timeSelected = timeSelected;
        this.dateSelected = dateSelected;
        this.dealer = dealer;
    }


    protected TestDrive(Parcel in) {
        vin = in.readString();
        timeSelected = in.readString();
        dateSelected = in.readString();
        dealer = in.readParcelable(Dealers.class.getClassLoader());
        typeService = in.readString();
        yourAdditionalInfor = in.readString();
        expectDate = in.readString();
        expectTime = in.readString();
        nameVin = in.readString();
        user = in.readParcelable(UserProfile.class.getClassLoader());
        vehicle = in.readParcelable(Vehicle.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vin);
        dest.writeString(timeSelected);
        dest.writeString(dateSelected);
        dest.writeParcelable(dealer, flags);
        dest.writeString(typeService);
        dest.writeString(yourAdditionalInfor);
        dest.writeString(expectDate);
        dest.writeString(expectTime);
        dest.writeString(nameVin);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(vehicle, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TestDrive> CREATOR = new Creator<TestDrive>() {
        @Override
        public TestDrive createFromParcel(Parcel in) {
            return new TestDrive(in);
        }

        @Override
        public TestDrive[] newArray(int size) {
            return new TestDrive[size];
        }
    };

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getNameVin() {
        return nameVin;
    }

    public void setNameVin(String nameVin) {
        this.nameVin = nameVin;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public String getTimeSelected() {
        return timeSelected;
    }

    public void setTimeSelected(String timeSelected) {
        this.timeSelected = timeSelected;
    }

    public String getDateSelected() {
        return dateSelected;
    }

    public void setDateSelected(String dateSelected) {
        this.dateSelected = dateSelected;
    }

    public Dealers getDealer() {
        return dealer;
    }

    public void setDealer(Dealers dealer) {
        this.dealer = dealer;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getTypeService() {
        return typeService;
    }

    public void setTypeService(String typeService) {
        this.typeService = typeService;
    }

    public String getYourAdditionalInfor() {
        return yourAdditionalInfor;
    }

    public void setYourAdditionalInfor(String yourAdditionalInfor) {
        this.yourAdditionalInfor = yourAdditionalInfor;
    }

    public String getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(String expectDate) {
        this.expectDate = expectDate;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }


}
