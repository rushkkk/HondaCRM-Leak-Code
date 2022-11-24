package vn.co.honda.hondacrm.net.model.connected;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OilSettingResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private OilSetting oilSetting;
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

    public OilSetting getOilSetting() {
        return oilSetting;
    }

    public void setOilSetting(OilSetting oilSetting) {
        this.oilSetting = oilSetting;
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