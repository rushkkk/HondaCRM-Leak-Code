package vn.co.honda.hondacrm.net.model.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable
{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("customer_vehicle_id")
    @Expose
    private Integer customerVehicleId;
    @SerializedName("vehicle_type")
    @Expose
    private Integer vehicleType;
    @SerializedName("service_type")
    @Expose
    private Integer serviceType;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_text")
    @Expose
    private String customerText;
    @SerializedName("odometer")
    @Expose
    private String odometer;
    @SerializedName("arrival_time")
    @Expose
    private String arrivalTime;
    @SerializedName("leave_time")
    @Expose
    private String leaveTime;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("dealer_id")
    @Expose
    private String dealerId;
    @SerializedName("service_type_name_en")
    @Expose
    private String serviceTypeNameEn;
    @SerializedName("service_type_name_vi")
    @Expose
    private String serviceTypeNameVi;
    @SerializedName("dealer_name")
    @Expose
    private String dealerName;
    @SerializedName("list_jobs")
    @Expose
    private List<ListJob> listJobs = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerVehicleId() {
        return customerVehicleId;
    }

    public void setCustomerVehicleId(Integer customerVehicleId) {
        this.customerVehicleId = customerVehicleId;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerText() {
        return customerText;
    }

    public void setCustomerText(String customerText) {
        this.customerText = customerText;
    }

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
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

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getServiceTypeNameEn() {
        return serviceTypeNameEn;
    }

    public void setServiceTypeNameEn(String serviceTypeNameEn) {
        this.serviceTypeNameEn = serviceTypeNameEn;
    }

    public String getServiceTypeNameVi() {
        return serviceTypeNameVi;
    }

    public void setServiceTypeNameVi(String serviceTypeNameVi) {
        this.serviceTypeNameVi = serviceTypeNameVi;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public List<ListJob> getListJobs() {
        return listJobs;
    }

    public void setListJobs(List<ListJob> listJobs) {
        this.listJobs = listJobs;
    }


}