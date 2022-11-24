package vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecallInfo {
    @SerializedName("item")
    @Expose
    private RecallDetail item;

    public RecallDetail getItem() {
        return item;
    }

    public void setItem(RecallDetail item) {
        this.item = item;
    }
}