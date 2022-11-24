package vn.co.honda.hondacrm.ui.activities.benefits_warranty.ModelMaintenance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastMaintenance {

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("maintenance_id")
    @Expose
    private String maintenanceId;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
    }
}
