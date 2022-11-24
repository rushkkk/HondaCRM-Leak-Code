package vn.co.honda.hondacrm.net.model.user;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("event_category")
    @Expose
    private Integer eventCategory;
    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("key_visual_image")
    @Expose
    private String keyVisualImage;
    @SerializedName("place_of_event")
    @Expose
    private String placeOfEvent;
    @SerializedName("teaser")
    @Expose
    private Object teaser;
    @SerializedName("event_start_date")
    @Expose
    private String eventStartDate;
    @SerializedName("event_end_date")
    @Expose
    private String eventEndDate;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("view")
    @Expose
    private Integer view;
    @SerializedName("concern")
    @Expose
    private Integer concern;
    @SerializedName("will_join")
    @Expose
    private Integer willJoin;
    @SerializedName("dont_care")
    @Expose
    private Integer dontCare;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("action")
    @Expose
    private Object action;
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

    public Integer getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(Integer eventCategory) {
        this.eventCategory = eventCategory;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
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

    public String getKeyVisualImage() {
        return keyVisualImage;
    }

    public void setKeyVisualImage(String keyVisualImage) {
        this.keyVisualImage = keyVisualImage;
    }

    public String getPlaceOfEvent() {
        return placeOfEvent;
    }

    public void setPlaceOfEvent(String placeOfEvent) {
        this.placeOfEvent = placeOfEvent;
    }

    public Object getTeaser() {
        return teaser;
    }

    public void setTeaser(Object teaser) {
        this.teaser = teaser;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public Integer getConcern() {
        return concern;
    }

    public void setConcern(Integer concern) {
        this.concern = concern;
    }

    public Integer getWillJoin() {
        return willJoin;
    }

    public void setWillJoin(Integer willJoin) {
        this.willJoin = willJoin;
    }

    public Integer getDontCare() {
        return dontCare;
    }

    public void setDontCare(Integer dontCare) {
        this.dontCare = dontCare;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getAction() {
        return action;
    }

    public void setAction(Object action) {
        this.action = action;
    }

}