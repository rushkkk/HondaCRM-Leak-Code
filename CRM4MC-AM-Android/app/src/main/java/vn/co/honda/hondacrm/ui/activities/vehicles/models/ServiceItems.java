package vn.co.honda.hondacrm.ui.activities.vehicles.models;


import java.io.Serializable;

public class ServiceItems implements Serializable {
    private int id;
    private String address;
    private String price;
    private double km;
    private String date;

    public ServiceItems() {
    }

    public ServiceItems(int id, String address, String price, double km, String date) {
        this.id = id;
        this.address = address;
        this.price = price;
        this.km = km;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
