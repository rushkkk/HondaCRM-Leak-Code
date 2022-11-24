package vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NextMaintenance {

@SerializedName("time")
@Expose
private String time;

public String getTime() {
return time;
}

public void setTime(String time) {
this.time = time;
}

}