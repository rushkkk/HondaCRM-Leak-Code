package vn.co.honda.hondacrm.ui.activities.connected.model;

import java.io.Serializable;

import vn.co.honda.hondacrm.ui.fragments.connected.models.VehicleParameter;

/**
 * Created by TienTM13 on 24/06/2019.
 */

public class Vehicle implements Serializable {
    private String VIN;
    private String name;
    private String license_plate;
    private String model;
    private String type;
    private boolean isConnected;
    private boolean isNeedChangeOil;
    private VehicleParameter vehicleParameter;

    public Vehicle() {
    }

    public Vehicle(String VIN, String name, String license_plate, String model, String type, boolean isConnected, boolean isNeedChangeOil, VehicleParameter vehicleParameter) {
        this.VIN = VIN;
        this.name = name;
        this.license_plate = license_plate;
        this.model = model;
        this.type = type;
        this.isConnected = isConnected;
        this.isNeedChangeOil = isNeedChangeOil;
        this.vehicleParameter = vehicleParameter;
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
}
