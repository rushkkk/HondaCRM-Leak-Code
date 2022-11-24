package vn.co.honda.hondacrm.net.model.consumption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConsumptionData {

    @SerializedName("consumption")
    @Expose
    private List<Consumption> consumption = null;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;

    public List<Consumption> getConsumption() {
        return consumption;
    }

    public void setConsumption(List<Consumption> consumption) {
        this.consumption = consumption;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

}