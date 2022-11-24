package vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("vehicle_info")
    @Expose
    private VehicleInfo vehicleInfo;
    @SerializedName("maintenance")
    @Expose
    private Maintenance maintenance  = null;
    @SerializedName("pi")
    @Expose
    private PiNumber pi  = null;
    @SerializedName("recall_info")
    @Expose
    private RecallInfo recallInfo = null;
    @SerializedName("warranty_info")
    @Expose
    private WarrantyInfo warrantyInfo  = null;
    @SerializedName("service_history")
    @Expose
    private List<ServiceHistory> serviceHistory = null;

    @SerializedName("booking_current")
    @Expose
    private BookingCurrent bookingCurrent = null;


    public BookingCurrent getBookingCurrent() {
        return bookingCurrent;
    }

    public void setBookingCurrent(BookingCurrent bookingCurrent) {
        this.bookingCurrent = bookingCurrent;
    }

    public VehicleInfo getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(VehicleInfo vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    public PiNumber getPi() {
        return pi;
    }

    public void setPi(PiNumber pi) {
        this.pi = pi;
    }

    public RecallInfo getRecallInfo() {
        return recallInfo;
    }

    public void setRecallInfo(RecallInfo recallInfo) {
        this.recallInfo = recallInfo;
    }

    public WarrantyInfo getWarrantyInfo() {
        return warrantyInfo;
    }

    public void setWarrantyInfo(WarrantyInfo warrantyInfo) {
        this.warrantyInfo = warrantyInfo;
    }

    public List<ServiceHistory> getServiceHistory() {
        return serviceHistory;
    }

    public void setServiceHistory(List<ServiceHistory> serviceHistory) {
        this.serviceHistory = serviceHistory;
    }


}