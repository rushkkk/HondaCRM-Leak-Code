package vn.co.honda.hondacrm.net.model.dealer;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dealers implements Parcelable {

    @SerializedName("dealer_id")
    @Expose
    private String dealerId;
    @SerializedName("dealer_name")
    @Expose
    private String dealerName;
    @SerializedName("province_id")
    @Expose
    private Integer provinceId;
    @SerializedName("district_id")
    @Expose
    private Integer districtId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("dealer_type")
    @Expose
    private String dealerType;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
//    @Expose
//    private String latitude;
    //@SerializedName("longtitude")
    @Expose
    private Double longtitude;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("working_start")
    @Expose
    private String workingStart;
    @SerializedName("working_end")
    @Expose
    private String workingEnd;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("fax")
    @Expose
    private String fax;
    @SerializedName("dealer_namager_id")
    @Expose
    private String dealerNamagerId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Float distance;

    @SerializedName("location")
    @Expose
    private List<Double> location = null;

    private String idmarker;

    public String getIdmarker() {
        return idmarker;
    }

    public void setIdmarker(String idmarker) {
        this.idmarker = idmarker;
    }

    protected Dealers(Parcel in) {
        dealerId = in.readString();
        dealerName = in.readString();
        if (in.readByte() == 0) {
            provinceId = null;
        } else {
            provinceId = in.readInt();
        }
        if (in.readByte() == 0) {
            districtId = null;
        } else {
            districtId = in.readInt();
        }
        address = in.readString();
        phone = in.readString();
        email = in.readString();
        dealerType = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longtitude = null;
        } else {
            longtitude = in.readDouble();
        }
        rate = in.readString();
        workingStart = in.readString();
        workingEnd = in.readString();
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        fax = in.readString();
        dealerNamagerId = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        if (in.readByte() == 0) {
            distance = null;
        } else {
            distance = in.readFloat();
        }
        idmarker = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dealerId);
        dest.writeString(dealerName);
        if (provinceId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(provinceId);
        }
        if (districtId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(districtId);
        }
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(dealerType);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longtitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longtitude);
        }
        dest.writeString(rate);
        dest.writeString(workingStart);
        dest.writeString(workingEnd);
        if (status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(status);
        }
        dest.writeString(fax);
        dest.writeString(dealerNamagerId);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        if (distance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(distance);
        }
        dest.writeString(idmarker);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Dealers> CREATOR = new Creator<Dealers>() {
        @Override
        public Dealers createFromParcel(Parcel in) {
            return new Dealers(in);
        }

        @Override
        public Dealers[] newArray(int size) {
            return new Dealers[size];
        }
    };

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }


    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDealerType() {
        return dealerType;
    }

    public void setDealerType(String dealerType) {
        this.dealerType = dealerType;
    }

    //    public String getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(String latitude) {
//        this.latitude = latitude;
//    }
    //---------------------------------
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    //---------------------------------

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getWorkingStart() {
        return workingStart;
    }

    public void setWorkingStart(String workingStart) {
        this.workingStart = workingStart;
    }

    public String getWorkingEnd() {
        return workingEnd;
    }

    public void setWorkingEnd(String workingEnd) {
        this.workingEnd = workingEnd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getDealerNamagerId() {
        return dealerNamagerId;
    }

    public void setDealerNamagerId(String dealerNamagerId) {
        this.dealerNamagerId = dealerNamagerId;
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

    public Dealers() {
    }

    public Dealers(String dealerId, String dealerName, Integer provinceId, Integer districtId, String address, String phone, String email, String dealerType, Double latitude, Double longtitude, String rate, String workingStart, String workingEnd, Integer status, String fax, String dealerNamagerId, String createdAt, String updatedAt) {
        this.dealerId = dealerId;
        this.dealerName = dealerName;
        this.provinceId = provinceId;
        this.districtId = districtId;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.dealerType = dealerType;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.rate = rate;
        this.workingStart = workingStart;
        this.workingEnd = workingEnd;
        this.status = status;
        this.fax = fax;
        this.dealerNamagerId = dealerNamagerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    // Dealer dealer = new Dealer("Mỹ Đình", "0965845544", 100, "17:00", "08:00", 100, 0, "4 Hoàng Diệu", 21.038107, 105.800898);

    public Dealers(String dealerName, String address, String phone, String rate, String workingStart, String workingEnd, Double latitude, Double longtitude) {
        this.dealerName = dealerName;
        this.address = address;
        this.phone = phone;
        this.rate = rate;
        this.workingStart = workingStart;
        this.workingEnd = workingEnd;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }


}