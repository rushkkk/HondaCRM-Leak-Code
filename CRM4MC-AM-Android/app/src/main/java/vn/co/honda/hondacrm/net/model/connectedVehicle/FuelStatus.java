package vn.co.honda.hondacrm.net.model.connectedVehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TienTM13 on 23/07/2019.
 */

public class FuelStatus {
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
    @SerializedName("last_distance")
    @Expose
    private String lastDistance;
    @SerializedName("last_change_oil")
    @Expose
    private String lastChangeOil;

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