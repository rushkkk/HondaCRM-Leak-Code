package vn.co.honda.hondacrm.net.model.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListAccessory {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name_vi")
    @Expose
    private String nameVi;
    @SerializedName("name_en")
    @Expose
    private String nameEn;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("price_vat")
    @Expose
    private String priceVat;
    @SerializedName("cost")
    @Expose
    private String cost;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameVi() {
        return nameVi;
    }

    public void setNameVi(String nameVi) {
        this.nameVi = nameVi;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceVat() {
        return priceVat;
    }

    public void setPriceVat(String priceVat) {
        this.priceVat = priceVat;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }


}