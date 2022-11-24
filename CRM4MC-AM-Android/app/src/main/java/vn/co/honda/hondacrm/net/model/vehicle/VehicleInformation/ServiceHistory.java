package vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceHistory {

    @SerializedName("service_id")
    @Expose
    private Integer serviceId;
    @SerializedName("service_type")
    @Expose
    private Integer serviceType;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("rating")
    @Expose
    private Integer rating;

    @SerializedName("service_name")
    @Expose
    private String serviceName;

    @SerializedName("dealer_name")
    @Expose
    private String dealerName;

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}