package vn.co.honda.hondacrm.ui.activities.Dealer.model;

public class Dealer {
  private String dealerName;
  private String dealerPhoneNumber;
  private double distance;
  private String timeClose;
  private String timeOpen;
  private int numberOfRating;
  private int imgDealer;
  private String address;
  private double latitudep;
  private double longitude;

  public Dealer(String dealerName, String dealerPhoneNumber, double distance, String timeClose, String timeOpen, int numberOfRating, int imgDealer, String address, double latitudep, double longitude) {
    this.dealerName = dealerName;
    this.dealerPhoneNumber = dealerPhoneNumber;
    this.distance = distance;
    this.timeClose = timeClose;
    this.timeOpen = timeOpen;
    this.numberOfRating = numberOfRating;
    this.imgDealer = imgDealer;
    this.address = address;
    this.latitudep = latitudep;
    this.longitude = longitude;
  }

  public String getDealerName() {
    return dealerName;
  }

  public void setDealerName(String dealerName) {
    this.dealerName = dealerName;
  }

  public String getDealerPhoneNumber() {
    return dealerPhoneNumber;
  }

  public void setDealerPhoneNumber(String dealerPhoneNumber) {
    this.dealerPhoneNumber = dealerPhoneNumber;
  }

  public double getDistance() {
    return distance;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  public String getTimeClose() {
    return timeClose;
  }

  public void setTimeClose(String timeClose) {
    this.timeClose = timeClose;
  }

  public String getTimeOpen() {
    return timeOpen;
  }

  public void setTimeOpen(String timeOpen) {
    this.timeOpen = timeOpen;
  }

  public int getNumberOfRating() {
    return numberOfRating;
  }

  public void setNumberOfRating(int numberOfRating) {
    this.numberOfRating = numberOfRating;
  }

  public int getImgDealer() {
    return imgDealer;
  }

  public void setImgDealer(int imgDealer) {
    this.imgDealer = imgDealer;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public double getLatitudep() {
    return latitudep;
  }

  public void setLatitudep(double latitudep) {
    this.latitudep = latitudep;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

}
