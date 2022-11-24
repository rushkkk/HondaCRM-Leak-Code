package vn.co.honda.hondacrm.ui.fragments.connected.models;

/**
 * Created by TienTM13 on 24/06/2019.
 */

public class DetailInformationItem {
    private int id;
    private String name;
    private Integer img;
    private String detail;

    public DetailInformationItem(int id, String name, Integer img, String detail) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
