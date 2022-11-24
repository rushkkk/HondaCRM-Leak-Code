package vn.co.honda.hondacrm.net.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private UserProfile userProfile;
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

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setData(UserProfile userProfile) {
        this.userProfile = userProfile;
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