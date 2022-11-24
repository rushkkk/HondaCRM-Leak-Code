package vn.co.honda.hondacrm.net.model.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListJob {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("job_service_name_en")
    @Expose
    private String jobServiceNameEn;
    @SerializedName("job_service_name_vi")
    @Expose
    private String jobServiceNameVi;
    @SerializedName("list_accessory")
    @Expose
    private List<ListAccessory> listAccessory = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getJobServiceNameEn() {
        return jobServiceNameEn;
    }

    public void setJobServiceNameEn(String jobServiceNameEn) {
        this.jobServiceNameEn = jobServiceNameEn;
    }

    public String getJobServiceNameVi() {
        return jobServiceNameVi;
    }

    public void setJobServiceNameVi(String jobServiceNameVi) {
        this.jobServiceNameVi = jobServiceNameVi;
    }

    public List<ListAccessory> getListAccessory() {
        return listAccessory;
    }

    public void setListAccessory(List<ListAccessory> listAccessory) {
        this.listAccessory = listAccessory;
    }

}