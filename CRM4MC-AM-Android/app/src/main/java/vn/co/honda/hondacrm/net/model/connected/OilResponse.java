package vn.co.honda.hondacrm.net.model.connected;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OilResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private Oil oil;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("errorCode")
    @Expose
    private Object errorCode;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Oil getOil() {
        return oil;
    }

    public void setData(Oil oil) {
        this.oil = oil;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

}