package vn.co.honda.hondacrm.net.model.booking_service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TypeService {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name_vi")
    @Expose
    private String nameVi;
    @SerializedName("name_en")
    @Expose
    private String nameEn;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameVi() {
        return nameVi;
    }

    public void setNameVi(String nameVi) {
        this.nameVi = nameVi;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }
}
