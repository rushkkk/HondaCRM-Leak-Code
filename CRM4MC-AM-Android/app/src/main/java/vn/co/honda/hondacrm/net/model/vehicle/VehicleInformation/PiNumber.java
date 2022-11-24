package vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PiNumber {

    @SerializedName("item")
    @Expose
    private List<PiDetail> item = null;
    @SerializedName("last_text_pi")
    @Expose
    private String lastTextPi;
    @SerializedName("last_date_pi")
    @Expose
    private String lastDatePi;
    @SerializedName("next_text_pi")
    @Expose
    private String nextTextPi;
    @SerializedName("next_date_pi")
    @Expose
    private String nextDatePi;

    public List<PiDetail> getItem() {
        return item;
    }

    public void setItem(List<PiDetail> item) {
        this.item = item;
    }

    public String getLastTextPi() {
        return lastTextPi;
    }

    public void setLastTextPi(String lastTextPi) {
        this.lastTextPi = lastTextPi;
    }

    public String getLastDatePi() {
        return lastDatePi;
    }

    public void setLastDatePi(String lastDatePi) {
        this.lastDatePi = lastDatePi;
    }

    public String getNextTextPi() {
        return nextTextPi;
    }

    public void setNextTextPi(String nextTextPi) {
        this.nextTextPi = nextTextPi;
    }

    public String getNextDatePi() {
        return nextDatePi;
    }

    public void setNextDatePi(String nextDatePi) {
        this.nextDatePi = nextDatePi;
    }

}