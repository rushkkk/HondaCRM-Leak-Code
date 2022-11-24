package vn.co.honda.hondacrm.ui.fragments.connected.local.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by TienTM13 on 15/07/2019.
 */

@Entity(tableName = "connected_vehicle")
public class VehicleEntity {
    @PrimaryKey
    @NonNull
    private String vinId;

    private String vehicleName;

    private String licensePlate;

    private String type;

    private boolean isConnected;

    private boolean isDefault;

    private boolean isConnecting;

    private String btuName;

    private String vehicleImage;


    private String distanceOfMaintenance;
    private String dateReminder;
    private String reminderCount;
    private String lastDistance;
    public VehicleEntity() {
    }

    public VehicleEntity(@NonNull String vinId, String vehicleName, String licensePlate, String type, boolean isConnected, boolean isDefault, boolean isConnecting, String btuName, String vehicleImage, String distanceOfMaintenance, String dateReminder, String reminderCount, String lastDistance) {
        this.vinId = vinId;
        this.vehicleName = vehicleName;
        this.licensePlate = licensePlate;
        this.type = type;
        this.isConnected = isConnected;
        this.isDefault = isDefault;
        this.isConnecting = isConnecting;
        this.btuName = btuName;
        this.vehicleImage = vehicleImage;
        this.distanceOfMaintenance = distanceOfMaintenance;
        this.reminderCount = reminderCount;
        this.dateReminder = dateReminder;
        this.lastDistance = lastDistance;
    }

    @NonNull
    public String getVinId() {
        return vinId;
    }

    public void setVinId(@NonNull String vinId) {
        this.vinId = vinId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isConnecting() {
        return isConnecting;
    }

    public void setConnecting(boolean connecting) {
        isConnecting = connecting;
    }

    public String getBtuName() {
        return btuName;
    }

    public void setBtuName(String btuName) {
        this.btuName = btuName;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
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
}