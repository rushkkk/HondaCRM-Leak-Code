package vn.co.honda.hondacrm.net.model.vehicle;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vehicle implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("customer_id")
    @Expose
    private String customerId;

    @SerializedName("vin")
    @Expose
    private String vin;

    @SerializedName("vehicle_name")
    @Expose
    private String vehicleName;

    @SerializedName("vehicle_type")
    @Expose
    private Integer vehicleType;

    @SerializedName("body_style")
    @Expose
    private Integer bodyStyle;

    @SerializedName("is_honda")
    @Expose
    private Integer isHonda;

    @SerializedName("is_connected")
    @Expose
    private Integer isConnected;

    @SerializedName("vehicle_brand")
    @Expose
    private Integer vehicleBrand;

    @SerializedName("vehicle_model")
    @Expose
    private String vehicleModel;

    @SerializedName("vehicle_image")
    @Expose
    private String vehicleImage;

    @SerializedName("vehicle_color")
    @Expose
    private String vehicleColor;

    @SerializedName("license_plate")
    @Expose
    private String licensePlate;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("vehicle_default")
    @Expose
    private Integer vehicleDefault;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;

    @SerializedName("vehicle_model_name")
    @Expose
    private String vehicleModelName;

    @SerializedName("vehicle_type_name")
    @Expose
    private String vehicleTypeName;

    @SerializedName("vehicle_brand_name")
    @Expose
    private String vehicleBrandName;

    @SerializedName("body_style_name")
    @Expose
    private String bodyStyleName;

    @SerializedName("vehicle_image_url")
    @Expose
    private String vehicleImageUrl;

    @SerializedName("next_pm")
    @Expose
    private String nextPm;

    private Bitmap tempImage;


    protected Vehicle(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        customerId = in.readString();
        vin = in.readString();
        vehicleName = in.readString();
        if (in.readByte() == 0) {
            vehicleType = null;
        } else {
            vehicleType = in.readInt();
        }
        if (in.readByte() == 0) {
            bodyStyle = null;
        } else {
            bodyStyle = in.readInt();
        }
        if (in.readByte() == 0) {
            isHonda = null;
        } else {
            isHonda = in.readInt();
        }
        if (in.readByte() == 0) {
            isConnected = null;
        } else {
            isConnected = in.readInt();
        }
        vehicleBrand = in.readInt();
        vehicleModel = in.readString();
        vehicleImage = in.readString();
        vehicleColor = in.readString();
        licensePlate = in.readString();
        note = in.readString();
        if (in.readByte() == 0) {
            vehicleDefault = null;
        } else {
            vehicleDefault = in.readInt();
        }
        createdAt = in.readString();
        updatedAt = in.readString();
        deletedAt = in.readString();
        vehicleModelName = in.readString();
        vehicleTypeName = in.readString();
        vehicleBrandName = in.readString();
        bodyStyleName = in.readString();
        vehicleImageUrl = in.readString();
        nextPm = in.readString();
        tempImage = in.readParcelable(Bitmap.class.getClassLoader());
        isConnecting = in.readByte() != 0;
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    public Bitmap getTempImage() {
        return tempImage;
    }

    public void setTempImage(Bitmap tempImage) {
        this.tempImage = tempImage;
    }

    public Vehicle() {
    }


    public boolean isConnecting() {
        return isConnecting;
    }

    public void setConnecting(boolean connecting) {
        isConnecting = connecting;
    }

    private boolean isConnecting;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getBodyStyle() {
        return bodyStyle;
    }

    public void setBodyStyle(Integer bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public Integer getIsHonda() {
        return isHonda;
    }

    public void setIsHonda(Integer isHonda) {
        this.isHonda = isHonda;
    }

    public Integer getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(Integer isConnected) {
        this.isConnected = isConnected;
    }

    public Integer getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(Integer vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getVehicleDefault() {
        return vehicleDefault;
    }

    public void setVehicleDefault(Integer vehicleDefault) {
        this.vehicleDefault = vehicleDefault;
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

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getVehicleModelName() {
        return vehicleModelName;
    }

    public void setVehicleModelName(String vehicleModelName) {
        this.vehicleModelName = vehicleModelName;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getVehicleBrandName() {
        return vehicleBrandName;
    }

    public void setVehicleBrandName(String vehicleBrandName) {
        this.vehicleBrandName = vehicleBrandName;
    }

    public String getBodyStyleName() {
        return bodyStyleName;
    }

    public void setBodyStyleName(String bodyStyleName) {
        this.bodyStyleName = bodyStyleName;
    }

    public String getVehicleImageUrl() {
        return vehicleImageUrl;
    }

    public void setVehicleImageUrl(String vehicleImageUrl) {
        this.vehicleImageUrl = vehicleImageUrl;
    }

    public String getNextPm() {
        return nextPm;
    }

    public void setNextPm(String nextPm) {
        this.nextPm = nextPm;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(customerId);
        parcel.writeString(vin);
        parcel.writeString(vehicleName);
        if (vehicleType == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(vehicleType);
        }
        if (bodyStyle == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(bodyStyle);
        }
        if (isHonda == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(isHonda);
        }
        if (isConnected == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(isConnected);
        }
        parcel.writeInt(vehicleBrand == null? 0 : vehicleBrand);
        parcel.writeString(vehicleModel);
        parcel.writeString(vehicleImage);
        parcel.writeString(vehicleColor);
        parcel.writeString(licensePlate);
        parcel.writeString(note);
        if (vehicleDefault == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(vehicleDefault);
        }
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
        parcel.writeString(deletedAt);
        parcel.writeString(vehicleModelName);
        parcel.writeString(vehicleTypeName);
        parcel.writeString(vehicleBrandName);
        parcel.writeString(bodyStyleName);
        parcel.writeString(vehicleImageUrl);
        parcel.writeString(nextPm);
        parcel.writeParcelable(tempImage, i);
        parcel.writeByte((byte) (isConnecting ? 1 : 0));
    }
}