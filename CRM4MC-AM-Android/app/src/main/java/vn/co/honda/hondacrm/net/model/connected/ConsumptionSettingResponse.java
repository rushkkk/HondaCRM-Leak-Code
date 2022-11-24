package vn.co.honda.hondacrm.net.model.connected;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsumptionSettingResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private ConsumptionSetting consumptionSetting;
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

    public ConsumptionSetting getConsumptionSetting() {
        return consumptionSetting;
    }

    public void setConsumptionSetting(ConsumptionSetting consumptionSetting) {
        this.consumptionSetting = consumptionSetting;
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