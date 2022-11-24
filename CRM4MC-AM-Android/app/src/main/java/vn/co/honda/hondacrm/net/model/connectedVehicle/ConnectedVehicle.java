package vn.co.honda.hondacrm.net.model.connectedVehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TienTM13 on 23/07/2019.
 */
public class ConnectedVehicle {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("vin")
    @Expose
    private String vin;
    @SerializedName("vehicle_name")
    @Expose
    private String vehicleName;
    @SerializedName("vehicle_type")
    @Expose
    private Integer vehicleType;
    @SerializedName("body_style")
    @Expose
    private Integer bodyStyle;
    @SerializedName("is_honda")
    @Expose
    private Integer isHonda;
    @SerializedName("is_connected")
    @Expose
    private Integer isConnected;
    @SerializedName("vehicle_brand")
    @Expose
    private Integer vehicleBrand;
    @SerializedName("vehicle_model")
    @Expose
    private String vehicleModel;
    @SerializedName("vehicle_image")
    @Expose
    private String vehicleImage;
    @SerializedName("vehicle_color")
    @Expose
    private String vehicleColor;
    @SerializedName("license_plate")
    @Expose
    private String licensePlate;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("vehicle_default")
    @Expose
    private Integer vehicleDefault;
    @SerializedName("sort")
    @Expose
    private Integer sort;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("fuel_status")
    @Expose
    private FuelStatus fuelStatus;

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

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getBodyStyle() {
        return bodyStyle;
    }

    public void setBodyStyle(Integer bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public Integer getIsHonda() {
        return isHonda;
    }

    public void setIsHonda(Integer isHonda) {
        this.isHonda = isHonda;
    }

    public Integer getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(Integer isConnected) {
        this.isConnected = isConnected;
    }

    public Integer getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(Integer vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
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

    public Object getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getVehicleDefault() {
        return vehicleDefault;
    }

    public void setVehicleDefault(Integer vehicleDefault) {
        this.vehicleDefault = vehicleDefault;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public FuelStatus getFuelStatus() {
        return fuelStatus;
    }

    public void setFuelStatus(FuelStatus fuelStatus) {
        this.fuelStatus = fuelStatus;
    }

}
