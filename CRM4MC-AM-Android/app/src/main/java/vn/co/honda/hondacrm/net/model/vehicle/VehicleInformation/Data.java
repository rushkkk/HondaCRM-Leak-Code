package vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("perPage")
    @Expose
    private Integer perPage;
    @SerializedName("currentPage")
    @Expose
    private Integer currentPage;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
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

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

}