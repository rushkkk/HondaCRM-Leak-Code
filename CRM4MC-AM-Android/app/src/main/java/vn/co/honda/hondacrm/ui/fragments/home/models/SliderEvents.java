package vn.co.honda.hondacrm.ui.fragments.home.models;

public class SliderEvents {

    private String imgEvent;
    private String title;
    private String content;
    private String startTime;
    private String endTime;
    private String address;

    public SliderEvents(String imgEvent, String title, String content, String startTime, String endTime, String address) {
        this.imgEvent = imgEvent;
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
    }

    public String getImgEvent() {
        return imgEvent;
    }

    public void setImgEvent(String imgEvent) {
        this.imgEvent = imgEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
