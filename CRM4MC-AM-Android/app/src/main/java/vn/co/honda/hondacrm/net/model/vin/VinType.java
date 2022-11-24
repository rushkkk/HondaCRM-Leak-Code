package vn.co.honda.hondacrm.net.model.vin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VinType {

    @SerializedName("is_honda")
    @Expose
    private Boolean isHonda;
    @SerializedName("is_connected")
    @Expose
    private Boolean isConnected;

    public Boolean getIsHonda() {
        return isHonda;
    }

    public void setIsHonda(Boolean isHonda) {
        this.isHonda = isHonda;
    }

    public Boolean getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(Boolean isConnected) {
        this.isConnected = isConnected;
    }

}