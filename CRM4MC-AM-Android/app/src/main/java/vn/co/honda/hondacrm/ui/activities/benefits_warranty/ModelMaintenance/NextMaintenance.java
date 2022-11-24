package vn.co.honda.hondacrm.ui.activities.benefits_warranty.ModelMaintenance;

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
