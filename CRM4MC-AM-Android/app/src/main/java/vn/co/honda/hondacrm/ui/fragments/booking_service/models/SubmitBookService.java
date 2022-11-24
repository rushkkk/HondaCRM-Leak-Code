package vn.co.honda.hondacrm.ui.fragments.booking_service.models;

import java.io.Serializable;

public class SubmitBookService implements Serializable {

    private String vin;
    private String dropDate;
    private String dropTime;
    private String dateRepair;
    private String serviceType;
    private String note;
    private String expectTime;
    private String expectDate;
    private int dealerId;

    public SubmitBookService() {
    }

    public SubmitBookService(String vin, String dropDate, String dropTime, String dateRepair, String serviceType, String note, String expectTime, String expectDate, int dealerId) {
        this.vin = vin;
        this.dropDate = dropDate;
        this.dropTime = dropTime;
        this.dateRepair = dateRepair;
        this.serviceType = serviceType;
        this.note = note;
        this.expectTime = expectTime;
        this.expectDate = expectDate;
        this.dealerId = dealerId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getDropDate() {
        return dropDate;
    }

    public void setDropDate(String dropDate) {
        this.dropDate = dropDate;
    }

    public String getDropTime() {
        return dropTime;
    }

    public void setDropTime(String dropTime) {
        this.dropTime = dropTime;
    }

    public String getDateRepair() {
        return dateRepair;
    }

    public void setDateRepair(String dateRepair) {
        this.dateRepair = dateRepair;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }

    public String getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(String expectDate) {
        this.expectDate = expectDate;
    }

    public int getDealerId() {
        return dealerId;
    }

    public void setDealerId(int dealerId) {
        this.dealerId = dealerId;
    }
}
