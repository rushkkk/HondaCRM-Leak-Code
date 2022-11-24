package vn.co.honda.hondacrm.net.model.consumption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Consumption {

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
    @SerializedName("engine_temperature")
    @Expose
    private String engineTemperature;
    @SerializedName("battery_voltage")
    @Expose
    private String batteryVoltage;
    @SerializedName("water_temperature")
    @Expose
    private String waterTemperature;
    @SerializedName("intake_air_temperature")
    @Expose
    private String intakeAirTemperature;
    @SerializedName("grip_opening_degree")
    @Expose
    private String gripOpeningDegree;
    @SerializedName("throttle_opening_degree")
    @Expose
    private String throttleOpeningDegree;
    @SerializedName("o2_sensor")
    @Expose
    private String o2Sensor;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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

    public String getEngineTemperature() {
        return engineTemperature;
    }

    public void setEngineTemperature(String engineTemperature) {
        this.engineTemperature = engineTemperature;
    }

    public String getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(String batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public String getWaterTemperature() {
        return waterTemperature;
    }

    public void setWaterTemperature(String waterTemperature) {
        this.waterTemperature = waterTemperature;
    }

    public String getIntakeAirTemperature() {
        return intakeAirTemperature;
    }

    public void setIntakeAirTemperature(String intakeAirTemperature) {
        this.intakeAirTemperature = intakeAirTemperature;
    }

    public String getGripOpeningDegree() {
        return gripOpeningDegree;
    }

    public void setGripOpeningDegree(String gripOpeningDegree) {
        this.gripOpeningDegree = gripOpeningDegree;
    }

    public String getThrottleOpeningDegree() {
        return throttleOpeningDegree;
    }

    public void setThrottleOpeningDegree(String throttleOpeningDegree) {
        this.throttleOpeningDegree = throttleOpeningDegree;
    }

    public String getO2Sensor() {
        return o2Sensor;
    }

    public void setO2Sensor(String o2Sensor) {
        this.o2Sensor = o2Sensor;
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

}