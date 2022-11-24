package vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StandardWarranty {

    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}