package vn.co.honda.hondacrm.net.model.booking_service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DayAvailable {

    @SerializedName("day")
    @Expose
    private String day;


    @SerializedName("date")
    @Expose
    private String date;


    @SerializedName("status")
    @Expose
    private Integer status;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.day;
    }

    public DayAvailable(String day, String date, Integer status) {
        this.day = day;
        this.date = date;
        this.status = status;
    }
}
