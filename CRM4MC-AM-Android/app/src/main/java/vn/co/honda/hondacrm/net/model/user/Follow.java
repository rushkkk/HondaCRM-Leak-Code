package vn.co.honda.hondacrm.net.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Follow {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("banner_image")
    @Expose
    private String bannerImage;

    private boolean chose;

    public boolean isChose() {
        return chose;
    }

    public void setChose(boolean chose) {
        this.chose = chose;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

}