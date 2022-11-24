package vn.co.honda.hondacrm.ui.fragments.connected.models;

import java.io.Serializable;

/**
 * Created by TienTM13 on 24/06/2019.
 */

public class Vehicle implements Serializable {
    private String VIN;
    private String name;
    private String license_plate;
    private String model;
    private String type;
    private String vehicleImage;
    private boolean isConnected;
    private boolean isNeedChangeOil;
    private VehicleParameter vehicleParameter;
    private String distanceOfMaintenance;
    private String dateReminder;
    private String reminderCount;
    private String lastDistance;

    public Vehicle() {
    }

    public Vehicle(String VIN, String name, String license_plate, String model, String type, String vehicleImage, boolean isConnected, boolean isNeedChangeOil, VehicleParameter vehicleParameter, String distanceOfMaintenance, String dateReminder, String reminderCount, String lastDistance) {
        this.VIN = VIN;
        this.name = name;
        this.license_plate = license_plate;
        this.model = model;
        this.type = type;
        this.vehicleImage = vehicleImage;
        this.isConnected = isConnected;
        this.isNeedChangeOil = isNeedChangeOil;
        this.vehicleParameter = vehicleParameter;
        this.distanceOfMaintenance = distanceOfMaintenance;
        this.dateReminder = dateReminder;
        this.reminderCount = reminderCount;
        this.lastDistance = lastDistance;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isNeedChangeOil() {
        return isNeedChangeOil;
    }

    public void setNeedChangeOil(boolean needChangeOil) {
        isNeedChangeOil = needChangeOil;
    }

    public VehicleParameter getVehicleParameter() {
        return vehicleParameter;
    }

    public void setVehicleParameter(VehicleParameter vehicleParameter) {
        this.vehicleParameter = vehicleParameter;
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
