package vn.co.honda.hondacrm.net.model.vehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("vin")
    @Expose
    private String vin;
    @SerializedName("repair_date")
    @Expose
    private String repairDate;
    @SerializedName("service_type")
    @Expose
    private Integer serviceType;
    @SerializedName("counter_reading")
    @Expose
    private Integer counterReading;
    @SerializedName("counter_unit")
    @Expose
    private Integer counterUnit;
    @SerializedName("customer_text")
    @Expose
    private String customerText;
    @SerializedName("license_plate")
    @Expose
    private String licensePlate;
    @SerializedName("vehicle_text")
    @Expose
    private String vehicleText;
    @SerializedName("odometer")
    @Expose
    private String odometer;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("dealer_name")
    @Expose
    private String dealerName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("dealer_type")
    @Expose
    private String dealerType;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("working_start")
    @Expose
    private String workingStart;
    @SerializedName("working_end")
    @Expose
    private String workingEnd;
    @SerializedName("name_vi")
    @Expose
    private String nameVi;
    @SerializedName("name_en")
    @Expose
    private String nameEn;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("service_history_id")
    @Expose
    private Integer serviceHistoryId;
    @SerializedName("job_service_type_id")
    @Expose
    private Integer jobServiceTypeId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(String repairDate) {
        this.repairDate = repairDate;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getCounterReading() {
        return counterReading;
    }

    public void setCounterReading(Integer counterReading) {
        this.counterReading = counterReading;
    }

    public Integer getCounterUnit() {
        return counterUnit;
    }

    public void setCounterUnit(Integer counterUnit) {
        this.counterUnit = counterUnit;
    }

    public String getCustomerText() {
        return customerText;
    }

    public void setCustomerText(String customerText) {
        this.customerText = customerText;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleText() {
        return vehicleText;
    }

    public void setVehicleText(String vehicleText) {
        this.vehicleText = vehicleText;
    }

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDealerType() {
        return dealerType;
    }

    public void setDealerType(String dealerType) {
        this.dealerType = dealerType;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getWorkingStart() {
        return workingStart;
    }

    public void setWorkingStart(String workingStart) {
        this.workingStart = workingStart;
    }

    public String getWorkingEnd() {
        return workingEnd;
    }

    public void setWorkingEnd(String workingEnd) {
        this.workingEnd = workingEnd;
    }

    public String getNameVi() {
        return nameVi;
    }

    public void setNameVi(String nameVi) {
        this.nameVi = nameVi;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getServiceHistoryId() {
        return serviceHistoryId;
    }

    public void setServiceHistoryId(Integer serviceHistoryId) {
        this.serviceHistoryId = serviceHistoryId;
    }

    public Integer getJobServiceTypeId() {
        return jobServiceTypeId;
    }

    public void setJobServiceTypeId(Integer jobServiceTypeId) {
        this.jobServiceTypeId = jobServiceTypeId;
    }

}
