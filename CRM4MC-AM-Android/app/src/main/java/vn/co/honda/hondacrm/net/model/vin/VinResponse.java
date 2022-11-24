package vn.co.honda.hondacrm.net.model.vin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VinResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private VinType data;
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

    public VinType getVinType() {
        return data;
    }

    public void setVinType(VinType data) {
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