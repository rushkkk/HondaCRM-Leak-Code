package vn.co.honda.hondacrm.net.model.connected;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OilSetting {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("vin")
    @Expose
    private String vin;
    @SerializedName("distance_of_maintenance")
    @Expose
    private String distanceOfMaintenance;
    @SerializedName("date_reminder")
    @Expose
    private String dateReminder;
    @SerializedName("reminder_count")
    @Expose
    private String reminderCount;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("last_distance")
    @Expose
    private String lastDistance;
    @SerializedName("last_change_oil")
    @Expose
    private String lastChangeOil;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getDistanceOfMaintenance() {
        return distanceOfMaintenance;
    }

    public void setDistanceOfMaintenance(String distanceOfMaintenance) {
        this.distanceOfMaintenance = distanceOfMaintenance;
    }

    public String getDateReminder() {
        return dateReminder;
    }

    public void setDateReminder(String dateReminder) {
        this.dateReminder = dateReminder;
    }

    public String getReminderCount() {
        return reminderCount;
    }

    public void setReminderCount(String reminderCount) {
        this.reminderCount = reminderCount;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCreatedAt() {
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

    public String getLastDistance() {
        return lastDistance;
    }

    public void setLastDistance(String lastDistance) {
        this.lastDistance = lastDistance;
    }

    public String getLastChangeOil() {
        return lastChangeOil;
    }

    public void setLastChangeOil(String lastChangeOil) {
        this.lastChangeOil = lastChangeOil;
    }

}