package vn.co.honda.hondacrm.ui.fragments.connected.models;

import java.io.Serializable;

public class VehicleIntroduce implements Serializable {

    private String name;
    private Integer imgVehicle;
    private String content;

    public VehicleIntroduce() {
    }

    public VehicleIntroduce(String name, Integer imgVehicle, String content) {
        this.name = name;
        this.imgVehicle = imgVehicle;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImgVehicle() {
        return imgVehicle;
    }

    public void setImgVehicle(Integer imgVehicle) {
        this.imgVehicle = imgVehicle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
