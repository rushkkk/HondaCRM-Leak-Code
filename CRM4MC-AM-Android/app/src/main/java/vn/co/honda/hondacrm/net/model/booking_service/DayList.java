package vn.co.honda.hondacrm.net.model.booking_service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DayList {

    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("data")
    @Expose
    private List<DayAvailable> data = null;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("errorCode")
    @Expose
    private String errorCode;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<DayAvailable> getData() {
        return data;
    }

    public void setData(List<DayAvailable> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
