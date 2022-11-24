package vn.co.honda.hondacrm.net.model.vehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private DataVehicle vehicle;
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

    public DataVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(DataVehicle vehicle) {
        this.vehicle = vehicle;
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