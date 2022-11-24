package vn.co.honda.hondacrm.net.model.connected;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsumptionSetting {

    @SerializedName("last_distance")
    @Expose
    private String lastDistance;
    @SerializedName("last_change_oil")
    @Expose
    private String lastChangeOil;

    public String getLastDistance() {
        return lastDistance;
    }

    public void setLastDistance(String lastDistance) {
        this.lastDistance = lastDistance;
    }

    public String getLastChangeOil() {
        return lastChangeOil;
    }

    public void setLastChangeOil(String lastChangeOil) {
        this.lastChangeOil = lastChangeOil;
    }

}