package vn.co.honda.hondacrm.net.model.user;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfile implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("api_token")
    @Expose
    private String apiToken;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("phone_verified_at")
    @Expose
    private String phoneVerifiedAt;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("google_id")
    @Expose
    private String googleId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("facebook_id")
    @Expose
    private String facebookId;
    @SerializedName("zalo_id")
    @Expose
    private String zaloId;
    @SerializedName("province_id")
    @Expose
    private String provinceId;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("id_number")
    @Expose
    private String idNumber;
    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;

    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;

    private Bitmap tempImage;

    public UserProfile() {

    }

    protected UserProfile(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        phone = in.readString();
        apiToken = in.readString();
        status = in.readString();
        phoneVerifiedAt = in.readString();
        customerId = in.readString();
        deletedAt = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        googleId = in.readString();
        email = in.readString();
        facebookId = in.readString();
        zaloId = in.readString();
        provinceId = in.readString();
        districtId = in.readString();
        idNumber = in.readString();
        job = in.readString();
        address = in.readString();
        avatar = in.readString();
        gender = in.readString();
        dateOfBirth = in.readString();
        avatarUrl = in.readString();
        tempImage = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    public Bitmap getTempImage() {
        return tempImage;
    }

    public void setTempImage(Bitmap tempImage) {
        this.tempImage = tempImage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneVerifiedAt() {
        return phoneVerifiedAt;
    }

    public void setPhoneVerifiedAt(String phoneVerifiedAt) {
        this.phoneVerifiedAt = phoneVerifiedAt;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
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

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getZaloId() {
        return zaloId;
    }

    public void setZaloId(String zaloId) {
        this.zaloId = zaloId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(apiToken);
        dest.writeString(status);
        dest.writeString(phoneVerifiedAt);
        dest.writeString(customerId);
        dest.writeString(deletedAt);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(googleId);
        dest.writeString(email);
        dest.writeString(facebookId);
        dest.writeString(zaloId);
        dest.writeString(provinceId);
        dest.writeString(districtId);
        dest.writeString(idNumber);
        dest.writeString(job);
        dest.writeString(address);
        dest.writeString(avatar);
        dest.writeString(gender);
        dest.writeString(dateOfBirth);
        dest.writeString(avatarUrl);
        dest.writeParcelable(tempImage, flags);
    }
}