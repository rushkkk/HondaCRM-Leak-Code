package vn.co.honda.hondacrm.ui.fragments.booking_service.models;

import java.io.Serializable;

import vn.co.honda.hondacrm.net.model.dealer.Dealers;

public class BookService implements Serializable {

    private String vin;
    private String timeSelected;
    private String dateSelected;
    private Dealers dealer;
    private String typeService;
    private String yourAdditionalInfor;
    private String expectDate;
    private String expectTime;

    public BookService() {
    }

    public BookService(String vin, String timeSelected, String dateSelected, Dealers dealer, String typeService, String yourAdditionalInfor, String expectDate, String expectTime) {
        this.vin = vin;
        this.timeSelected = timeSelected;
        this.dateSelected = dateSelected;
        this.dealer = dealer;
        this.typeService = typeService;
        this.yourAdditionalInfor = yourAdditionalInfor;
        this.expectDate = expectDate;
        this.expectTime = expectTime;
    }

    public BookService(String vin, String timeSelected, String dateSelected, Dealers dealer) {
        this.vin = vin;
        this.timeSelected = timeSelected;
        this.dateSelected = dateSelected;
        this.dealer = dealer;
    }

    public String getTimeSelected() {
        return timeSelected;
    }

    public void setTimeSelected(String timeSelected) {
        this.timeSelected = timeSelected;
    }

    public String getDateSelected() {
        return dateSelected;
    }

    public void setDateSelected(String dateSelected) {
        this.dateSelected = dateSelected;
    }

    public Dealers getDealer() {
        return dealer;
    }

    public void setDealer(Dealers dealer) {
        this.dealer = dealer;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getTypeService() {
        return typeService;
    }

    public void setTypeService(String typeService) {
        this.typeService = typeService;
    }

    public String getYourAdditionalInfor() {
        return yourAdditionalInfor;
    }

    public void setYourAdditionalInfor(String yourAdditionalInfor) {
        this.yourAdditionalInfor = yourAdditionalInfor;
    }

    public String getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(String expectDate) {
        this.expectDate = expectDate;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }
}
