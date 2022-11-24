package vn.co.honda.hondacrm.net.model.customer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private DataCustomer data;
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

    public DataCustomer getData() {
        return data;
    }

    public void setData(DataCustomer data) {
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