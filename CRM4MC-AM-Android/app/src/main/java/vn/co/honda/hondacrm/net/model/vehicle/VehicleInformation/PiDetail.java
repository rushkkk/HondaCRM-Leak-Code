package vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PiDetail {

    @SerializedName("pi_no")
    @Expose
    private Integer piNo;
    @SerializedName("pi_time")
    @Expose
    private String piTime;
    @SerializedName("expire_time")
    @Expose
    private String expireTime;
    @SerializedName("expire_distance")
    @Expose
    private Integer expireDistance;
    @SerializedName("pi_status")
    @Expose
    private Integer piStatus;

    public Integer getPiNo() {
        return piNo;
    }

    public void setPiNo(Integer piNo) {
        this.piNo = piNo;
    }

    public String getPiTime() {
        return piTime;
    }

    public void setPiTime(String piTime) {
        this.piTime = piTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getExpireDistance() {
        return expireDistance;
    }

    public void setExpireDistance(Integer expireDistance) {
        this.expireDistance = expireDistance;
    }

    public Integer getPiStatus() {
        return piStatus;
    }

    public void setPiStatus(Integer piStatus) {
        this.piStatus = piStatus;
    }
}