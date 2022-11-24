package vn.co.honda.hondacrm.net.model.customer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customer {

    @SerializedName("id")
    @Expose
    private Object id;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("api_token")
    @Expose
    private Object apiToken;
    @SerializedName("remember_token")
    @Expose
    private Object rememberToken;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("phone_verified_at")
    @Expose
    private String phoneVerifiedAt;
    @SerializedName("customer_id")
    @Expose
    private Object customerId;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("google_id")
    @Expose
    private Object googleId;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("facebook_id")
    @Expose
    private Object facebookId;
    @SerializedName("zalo_id")
    @Expose
    private Object zaloId;
    @SerializedName("province_id")
    @Expose
    private Object provinceId;
    @SerializedName("district_id")
    @Expose
    private Object districtId;
    @SerializedName("id_number")
    @Expose
    private Object idNumber;
    @SerializedName("job")
    @Expose
    private Object job;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("avatar")
    @Expose
    private Object avatar;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("date_of_birth")
    @Expose
    private Object dateOfBirth;
    @SerializedName("grouping")
    @Expose
    private Object grouping;
    @SerializedName("authorization_group")
    @Expose
    private Object authorizationGroup;
    @SerializedName("company_code")
    @Expose
    private Object companyCode;
    @SerializedName("partner_cat")
    @Expose
    private Object partnerCat;
    @SerializedName("title")
    @Expose
    private Object title;
    @SerializedName("mobile_phone")
    @Expose
    private Object mobilePhone;
    @SerializedName("occupation")
    @Expose
    private Object occupation;
    @SerializedName("region")
    @Expose
    private Object region;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("district")
    @Expose
    private Object district;
    @SerializedName("street")
    @Expose
    private Object street;
    @SerializedName("identification_type")
    @Expose
    private Object identificationType;
    @SerializedName("identification_number")
    @Expose
    private Object identificationNumber;
    @SerializedName("reconciliation_acct")
    @Expose
    private Object reconciliationAcct;
    @SerializedName("terms_of_payment")
    @Expose
    private Object termsOfPayment;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getApiToken() {
        return apiToken;
    }

    public void setApiToken(Object apiToken) {
        this.apiToken = apiToken;
    }

    public Object getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(Object rememberToken) {
        this.rememberToken = rememberToken;
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

    public Object getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Object customerId) {
        this.customerId = customerId;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getGoogleId() {
        return googleId;
    }

    public void setGoogleId(Object googleId) {
        this.googleId = googleId;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(Object facebookId) {
        this.facebookId = facebookId;
    }

    public Object getZaloId() {
        return zaloId;
    }

    public void setZaloId(Object zaloId) {
        this.zaloId = zaloId;
    }

    public Object getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Object provinceId) {
        this.provinceId = provinceId;
    }

    public Object getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Object districtId) {
        this.districtId = districtId;
    }

    public Object getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(Object idNumber) {
        this.idNumber = idNumber;
    }

    public Object getJob() {
        return job;
    }

    public void setJob(Object job) {
        this.job = job;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Object dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Object getGrouping() {
        return grouping;
    }

    public void setGrouping(Object grouping) {
        this.grouping = grouping;
    }

    public Object getAuthorizationGroup() {
        return authorizationGroup;
    }

    public void setAuthorizationGroup(Object authorizationGroup) {
        this.authorizationGroup = authorizationGroup;
    }

    public Object getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Object companyCode) {
        this.companyCode = companyCode;
    }

    public Object getPartnerCat() {
        return partnerCat;
    }

    public void setPartnerCat(Object partnerCat) {
        this.partnerCat = partnerCat;
    }

    public Object getTitle() {
        return title;
    }

    public void setTitle(Object title) {
        this.title = title;
    }

    public Object getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(Object mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Object getOccupation() {
        return occupation;
    }

    public void setOccupation(Object occupation) {
        this.occupation = occupation;
    }

    public Object getRegion() {
        return region;
    }

    public void setRegion(Object region) {
        this.region = region;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getDistrict() {
        return district;
    }

    public void setDistrict(Object district) {
        this.district = district;
    }

    public Object getStreet() {
        return street;
    }

    public void setStreet(Object street) {
        this.street = street;
    }

    public Object getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(Object identificationType) {
        this.identificationType = identificationType;
    }

    public Object getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(Object identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public Object getReconciliationAcct() {
        return reconciliationAcct;
    }

    public void setReconciliationAcct(Object reconciliationAcct) {
        this.reconciliationAcct = reconciliationAcct;
    }

    public Object getTermsOfPayment() {
        return termsOfPayment;
    }

    public void setTermsOfPayment(Object termsOfPayment) {
        this.termsOfPayment = termsOfPayment;
    }

}