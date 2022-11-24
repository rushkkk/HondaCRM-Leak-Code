package vn.co.honda.hondacrm.net.model.products;

public class MCProduct {
    private String imgMCProduct;
    private String titleMCProduct;

    public MCProduct() {
    }

    public MCProduct(String imgMCProduct, String titleMCProduct) {
        this.imgMCProduct = imgMCProduct;
        this.titleMCProduct = titleMCProduct;
    }

    public String getImgMCProduct() {
        return imgMCProduct;
    }

    public void setImgMCProduct(String imgMCProduct) {
        this.imgMCProduct = imgMCProduct;
    }

    public String getTitleMCProduct() {
        return titleMCProduct;
    }

    public void setTitleMCProduct(String titleMCProduct) {
        this.titleMCProduct = titleMCProduct;
    }
}
