package vn.co.honda.hondacrm.net.model.vehicle.warranty;
import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class WarrantyResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private WarrantyExtension data;
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

    public WarrantyExtension getData() {
        return data;
    }

    public void setData(WarrantyExtension data) {
        this.data = data;
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