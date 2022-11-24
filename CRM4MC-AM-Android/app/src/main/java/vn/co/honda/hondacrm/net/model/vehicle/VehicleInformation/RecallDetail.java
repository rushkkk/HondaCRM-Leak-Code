package vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecallDetail {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("vin")
    @Expose
    private String vin;
    @SerializedName("dealer_id")
    @Expose
    private String dealerId;
    @SerializedName("recall_campaign_id")
    @Expose
    private Integer recallCampaignId;
    @SerializedName("recall_status")
    @Expose
    private Integer recallStatus;

    @SerializedName("reason")
    @Expose
    private String reason;

    @SerializedName("sent_time")
    @Expose
    private Integer sentTime;
    @SerializedName("view_status")
    @Expose
    private String viewStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("campaign_id")
    @Expose
    private Integer campaignId;
    @SerializedName("campaign_description")
    @Expose
    private String campaignDescription;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getRecallCampaignId() {
        return recallCampaignId;
    }

    public void setRecallCampaignId(Integer recallCampaignId) {
        this.recallCampaignId = recallCampaignId;
    }

    public Integer getRecallStatus() {
        return recallStatus;
    }

    public void setRecallStatus(Integer recallStatus) {
        this.recallStatus = recallStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getSentTime() {
        return sentTime;
    }

    public void setSentTime(Integer sentTime) {
        this.sentTime = sentTime;
    }

    public String getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(String viewStatus) {
        this.viewStatus = viewStatus;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignDescription() {
        return campaignDescription;
    }

    public void setCampaignDescription(String campaignDescription) {
        this.campaignDescription = campaignDescription;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}