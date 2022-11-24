package vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WarrantyInfo {

    @SerializedName("standard_warranty")
    @Expose
    private StandardWarranty standardWarranty;

    @SerializedName("extension_warranty")
    @Expose
    private ExtensionWarranty extensionWarranty;

    @Expose
    private String distance;

    public StandardWarranty getStandardWarranty() {
        return standardWarranty;
    }

    public void setStandardWarranty(StandardWarranty standardWarranty) {
        this.standardWarranty = standardWarranty;
    }

    public ExtensionWarranty getExtensionWarranty() {
        return extensionWarranty;
    }

    public void setExtensionWarranty(ExtensionWarranty extensionWarranty) {
        this.extensionWarranty = extensionWarranty;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}