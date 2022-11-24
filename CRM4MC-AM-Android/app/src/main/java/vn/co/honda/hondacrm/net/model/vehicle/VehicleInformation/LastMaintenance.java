package vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastMaintenance {

@SerializedName("time")
@Expose
private String time;
@SerializedName("maintenance_id")
@Expose
private Integer maintenanceId;

public String getTime() {
return time;
}

public void setTime(String time) {
this.time = time;
}

public Integer getMaintenanceId() {
return maintenanceId;
}

public void setMaintenanceId(Integer maintenanceId) {
this.maintenanceId = maintenanceId;
}

}