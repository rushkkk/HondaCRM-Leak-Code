package vn.co.honda.hondacrm.ui.fragments.connected.models;

/**
 * Created by TienTM13 on 03/07/2019.
 */

public class VehicleParameter {
    private String vin;
    private String engineTemperature;
    private String distanceOfMaintenance;
    private String batteryVoltage;
    private String waterTemperature;
    private String intakeAirTemperature;
    private String gridOpeningDegree;
    private String throttleOpeningDegree;
    private String o2Sensor;

    public VehicleParameter(String vin, String engineTemperature, String distanceOfMaintenance, String batteryVoltage, String waterTemperature, String intakeAirTemperature, String gridOpeningDegree, String throttleOpeningDegree, String o2Sensor) {
        this.vin = vin;
        this.engineTemperature = engineTemperature;
        this.distanceOfMaintenance = distanceOfMaintenance;
        this.batteryVoltage = batteryVoltage;
        this.waterTemperature = waterTemperature;
        this.intakeAirTemperature = intakeAirTemperature;
        this.gridOpeningDegree = gridOpeningDegree;
        this.throttleOpeningDegree = throttleOpeningDegree;
        this.o2Sensor = o2Sensor;
    }

    public VehicleParameter() {

    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        vin = vin;
    }

    public String getEngineTemperature() {
        return engineTemperature;
    }

    public void setEngineTemperature(String engineTemperature) {
        this.engineTemperature = engineTemperature;
    }

    public String getDistanceOfMaintenance() {
        return distanceOfMaintenance;
    }

    public void setDistanceOfMaintenance(String distanceOfMaintenance) {
        this.distanceOfMaintenance = distanceOfMaintenance;
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

    public String getGridOpeningDegree() {
        return gridOpeningDegree;
    }

    public void setGridOpeningDegree(String gridOpeningDegree) {
        this.gridOpeningDegree = gridOpeningDegree;
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
}
