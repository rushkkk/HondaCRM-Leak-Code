package vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VehicleInfo implements Serializable {

    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("vin")
    @Expose
    private String vin;
    @SerializedName("vehicle_name")
    @Expose
    private String vehicleName;
    @SerializedName("license_plate")
    @Expose
    private String licensePlate;
    @SerializedName("vehicle_default")
    @Expose
    private Integer vehicleDefault;
    @SerializedName("vehicle_color")
    @Expose
    private String vehicleColor;
    @SerializedName("vehicle_type")
    @Expose
    private Integer vehicleType;
    @SerializedName("vehicle_model")
    @Expose
    private String vehicleModel;
    @SerializedName("vehicle_image")
    @Expose
    private String vehicleImage;
    @SerializedName("body_style")
    @Expose
    private String bodyStyle;

    @SerializedName("is_connected")
    @Expose
    private Integer isConnected;

    @SerializedName("is_honda")
    @Expose
    private Integer isHonda;
    @SerializedName("sort")
    @Expose
    private Integer sort;

    @SerializedName("cylinder_capacity")
    @Expose
    private String cylinderCapacity;

    private Boolean isConnect;

    private Boolean isWarning;

    public String getCylinderCapacity() {
        return cylinderCapacity;
    }

    public void setCylinderCapacity(String cylinderCapacity) {
        this.cylinderCapacity = cylinderCapacity;
    }

    public Boolean getConnect() {
        return isConnect;
    }

    public void setConnect(Boolean connect) {
        isConnect = connect;
    }

    public Boolean getWarning() {
        return isWarning;
    }

    public void setWarning(Boolean warning) {
        isWarning = warning;
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

    public Integer getVehicleDefault() {
        return vehicleDefault;
    }

    public void setVehicleDefault(Integer vehicleDefault) {
        this.vehicleDefault = vehicleDefault;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getBodyStyle() {
        return bodyStyle;
    }

    public void setBodyStyle(String bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public Integer getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(Integer isConnected) {
        this.isConnected = isConnected;
    }

    public Integer getIsHonda() {
        return isHonda;
    }

    public void setIsHonda(Integer isHonda) {
        this.isHonda = isHonda;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}