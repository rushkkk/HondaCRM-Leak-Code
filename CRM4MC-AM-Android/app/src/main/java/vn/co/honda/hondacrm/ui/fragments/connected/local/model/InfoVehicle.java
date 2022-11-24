package vn.co.honda.hondacrm.ui.fragments.connected.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "info_vehicle_table")
public class InfoVehicle {
    @PrimaryKey
    @NonNull
    private String vinId;

    private int enginner;

    private int distance;

    private int battery;

    private int water;

    private int intake;

    private int grip;

    private int throttle;

    private int sensor;

    public InfoVehicle() {
    }

    public InfoVehicle(@NonNull String vinId, int enginner, int distance, int battery, int water, int intake, int grip, int throttle, int sensor) {
        this.vinId = vinId;
        this.enginner = enginner;
        this.distance = distance;
        this.battery = battery;
        this.water = water;
        this.intake = intake;
        this.grip = grip;
        this.throttle = throttle;
        this.sensor = sensor;
    }

    @NonNull
    public String getVinId() {
        return vinId;
    }

    public void setVinId(@NonNull String vinId) {
        this.vinId = vinId;
    }

    public int getEnginner() {
        return enginner;
    }

    public void setEnginner(int enginner) {
        this.enginner = enginner;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getIntake() {
        return intake;
    }

    public void setIntake(int intake) {
        this.intake = intake;
    }

    public int getGrip() {
        return grip;
    }

    public void setGrip(int grip) {
        this.grip = grip;
    }

    public int getThrottle() {
        return throttle;
    }

    public void setThrottle(int throttle) {
        this.throttle = throttle;
    }

    public int getSensor() {
        return sensor;
    }

    public void setSensor(int sensor) {
        this.sensor = sensor;
    }
}
