package vn.co.honda.hondacrm.ui.activities.benefits_warranty.ModelMaintenance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Maintenance {
    @SerializedName("last_maintenance")
    @Expose
    private LastMaintenance lastMaintenance;
    @SerializedName("next_maintenance")
    @Expose
    private NextMaintenance nextMaintenance;

    public LastMaintenance getLastMaintenance() {
        return lastMaintenance;
    }

    public void setLastMaintenance(LastMaintenance lastMaintenance) {
        this.lastMaintenance = lastMaintenance;
    }

    public NextMaintenance getNextMaintenance() {
        return nextMaintenance;
    }

    public void setNextMaintenance(NextMaintenance nextMaintenance) {
        this.nextMaintenance = nextMaintenance;
    }
}
