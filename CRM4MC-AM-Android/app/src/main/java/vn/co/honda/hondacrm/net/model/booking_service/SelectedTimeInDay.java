package vn.co.honda.hondacrm.net.model.booking_service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectedTimeInDay {

    @SerializedName("slot")
    @Expose
    private String timeSlot;

    @SerializedName("status")
    @Expose
    private Integer status;

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.timeSlot;
    }

    public SelectedTimeInDay(String timeSlot, Integer status) {
        this.timeSlot = timeSlot;
        this.status = status;
    }
}
