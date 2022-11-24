package vn.co.honda.hondacrm.net;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Single;
import vn.co.honda.hondacrm.net.model.base.Guide;
import vn.co.honda.hondacrm.net.model.base.Policy;
import vn.co.honda.hondacrm.net.model.base.Response;
import vn.co.honda.hondacrm.net.model.base.ResponseOTPObject;
import vn.co.honda.hondacrm.net.model.base.ResponseObject;
import vn.co.honda.hondacrm.net.model.base.Terms;
import vn.co.honda.hondacrm.net.model.booking_service.DayList;
import vn.co.honda.hondacrm.net.model.booking_service.ListTypeService;
import vn.co.honda.hondacrm.net.model.booking_service.TimeSlot;
import vn.co.honda.hondacrm.net.model.connected.OilResponse;
import vn.co.honda.hondacrm.net.model.connected.OilSettingResponse;
import vn.co.honda.hondacrm.net.model.connectedVehicle.ConnectedVehicleResponse;
import vn.co.honda.hondacrm.net.model.consumption.ConsumptionListResponse;
import vn.co.honda.hondacrm.net.model.consumption.ConsumptionResponse;
import vn.co.honda.hondacrm.net.model.customer.CustomerResponse;
import vn.co.honda.hondacrm.net.model.dealer.DealerResponse;
import vn.co.honda.hondacrm.net.model.dealer.DistrictResponse;
import vn.co.honda.hondacrm.net.model.dealer.ProvinceResponse;
import vn.co.honda.hondacrm.net.model.service.CancelBookingResponse;
import vn.co.honda.hondacrm.net.model.service.DetailServiceHistoryResponse;
import vn.co.honda.hondacrm.net.model.service.ServiceHistoryResponse;
import vn.co.honda.hondacrm.net.model.tutorial.TutorialResponse;
import vn.co.honda.hondacrm.net.model.user.MyEventResponse;
import vn.co.honda.hondacrm.net.model.user.MyFollowResponse;
import vn.co.honda.hondacrm.net.model.user.UserProfileResponse;
import vn.co.honda.hondacrm.net.model.user.UserRegisterResponse;
import vn.co.honda.hondacrm.net.model.user.UserResponse;
import vn.co.honda.hondacrm.net.model.vehicle.ServiceHistoryFullResponse;
import vn.co.honda.hondacrm.net.model.vehicle.UpdateVehicleResponse;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleInformation.VehicelDetailResponse;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleResponse;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleSingleResponse;
import vn.co.honda.hondacrm.net.model.vehicle.VehicleType;
import vn.co.honda.hondacrm.net.model.vehicle.warranty.WarrantyResponse;
import vn.co.honda.hondacrm.net.model.vin.VinResponse;
import vn.co.honda.hondacrm.ui.activities.notification.model.ListNotifications;
import vn.co.honda.hondacrm.ui.fragments.booking_service.models.SubmitBookService;
import vn.co.honda.hondacrm.ui.fragments.news.models.NewsModelRSS;
import vn.co.honda.hondacrm.ui.fragments.users.models.User;
import vn.co.honda.hondacrm.ui.fragments.users.models.UserModel;


/**
 * @author CuongNV31
 */
public interface ApiService {

    @GET("rss")
    Single<NewsModelRSS> getNews();

    @GET("users.json")
    Single<ArrayList<UserModel>> getUserDetails();

    // Register new user
    @FormUrlEncoded
    @POST("notes/user/register")
    Single<User> register(@Field("device_id") String deviceId);

    /**
     * User Login, Register, Profile
     */

    @FormUrlEncoded
    @POST("api/auth/register")
    Single<UserRegisterResponse> registerPhone(
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("c_password") String confirmPassword,
            @Field("client_id") String clientID,
            @Field("client_secret") String clientSecret,
            @Field("gender") Integer gender,
            @Field("date_of_birth") String date);

    @FormUrlEncoded
    @POST("api/auth/login")
    Single<UserResponse> loginPhone(
            @Field("username") String name,
            @Field("client_id") String clientID,
            @Field("client_secret") String clientSecret,
            @Field("grant_type") String grantType,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("oauth/token")
    Single<UserResponse> loginGoogle(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientID,
            @Field("client_secret") String clientSecret,
            @Field("network") String network,
            @Field("access_token") String accessToken);

    @GET("api/customer/profile")
    Single<UserProfileResponse> getUserProfile(
            @Header("Authorization") String authorization);

    @GET("api/tutorial")
    Single<TutorialResponse> getTutorial();

    @GET("api/product-follow")
    Single<MyFollowResponse> getListMyFollow(
            @Header("Authorization") String authorization,
            @Query("size") Integer size,
            @Query("page") Integer page
    );

    @GET("api/event-customer")
    Single<MyEventResponse> getListMyEvent(
            @Header("Authorization") String authorization,
            @Query("size") Integer size,
            @Query("page") Integer page
    );

    @FormUrlEncoded
    @POST("api/product-follow/un-follow")
    Single<MyFollowResponse> updateMyFollow(
            @Header("Authorization") String authorization,
            @Field("ids[]") List<Integer> ids);

    @FormUrlEncoded
    @POST("api/event-customer/un-follow")
    Single<MyFollowResponse> updateMyEvent(
            @Header("Authorization") String authorization,
            @Field("ids[]") List<Integer> ids);

    @Multipart
    @POST("api/customer/profile")
    Single<UserProfileResponse> updateProfile(
            @Header("Authorization") String authorization,
            @Part MultipartBody.Part avatar,
            @Part("name") RequestBody name,
            @Part("address") RequestBody address,
            @Part("gender") Integer gender,
            @Part("date_of_birth") RequestBody date_of_birth,
            @Part("job") RequestBody job,
            @Part("id_number") RequestBody id_number,
            @Part("email") RequestBody email);


    @Multipart
    @POST("api/customer/profile")
    Single<UserProfileResponse> updateProfileSocial(
            @Header("Authorization") String authorization,
            @Part("name") RequestBody name,
            @Part("phone") RequestBody phone,
            @Part("password") RequestBody password,
            @Part("c_password") RequestBody confirmPass,
            @Part("date_of_birth") RequestBody date_of_birth,
            @Part("gender") Integer gender);

    @Multipart
    @POST("api/customer/profile")
    Single<UserProfileResponse> updateProfileSocial(
            @Header("Authorization") String authorization,
            @Part("name") RequestBody name,
            @Part("phone") RequestBody phone,
            @Part("gender") Integer gender,
            @Part("date_of_birth") RequestBody date_of_birth);

    @Multipart
    @POST("api/customer/profile")
    Single<UserProfileResponse> updateProfileSocials(
            @Header("Authorization") String authorization,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("password") RequestBody password,
            @Part("c_password") RequestBody confirmPass);


    @FormUrlEncoded
    @POST("api/auth/forgetPassword")
    Single<ResponseOTPObject> forgotPassword(
            @Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/auth/resetPassword")
    Single<ResponseObject> resetPassword(
            @Field("otp") String otp,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("c_password") String confirmPassword);

    @FormUrlEncoded
    @POST("api/auth/verifyForgetPassword")
    Single<ResponseOTPObject> verifyOTPForgotPassword(
            @Field("otp") String otp,
            @Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/auth/verifyActive")
    Single<ResponseOTPObject> verifyOTPActiveAccount(
            @Field("otp") String otp,
            @Field("phone") String phone,
            @Field("password") String pass);

    @FormUrlEncoded
    @POST("api/auth/active")
    Single<ResponseOTPObject> activeCustomer(
            @Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/auth/verifyActiveWithoutPassword")
    Single<ResponseOTPObject> verifyActiveWithoutPassword(
            @Field("otp") String otp,
            @Field("phone") String phone);

    /**
     * OTP
     */
    @FormUrlEncoded
    @POST("api/auth/verifyOtp")
    Single<UserResponse> verifyOTP(
            @Header("Authorization") String authorization,
            @Field("otp") String otp);

    @POST("api/auth/sendOtp")
    Single<ResponseOTPObject> sendOTP(
            @Header("Authorization") String authorization);

    @POST("api/auth/sendOtp")
    Single<UserResponse> reSendOTP(
            @Header("Authorization") String authorization);

    /**
     * VehicleInformation
     */

    @Multipart
    @POST("api/vehicle/register")
    Single<VehicleResponse> registerVehicleInfoNew(
            @Header("Authorization") String authorization,
            @Part("vin") RequestBody vin,
            @Part("vehicle_name") RequestBody vehicleName,
            @Part("license_plate") RequestBody licensePlate,
            @Part("vehicle_type") Integer vehicleType,
            @Part("is_connected") Integer isConnected,
            @Part("is_honda") Integer isHonda,
            @Part("body_style") Integer bodyStyle,
            @Part MultipartBody.Part imageVehicle);


    @Multipart
    @POST("api/vehicle/register")
    Single<VehicleResponse> registerVehicleInfoOtherBrand(
            @Header("Authorization") String authorization,
            @Part("vin") RequestBody vin,
            @Part("vehicle_name") RequestBody vehicleName,
            @Part("license_plate") RequestBody licensePlate,
            @Part("vehicle_type") Integer vehicleType,
            @Part("is_connected") Integer isConnected,
            @Part("is_honda") Integer isHonda,
            @Part("vehicle_brand") Integer vehicle_brand,
            @Part("vehicle_model") RequestBody vehicle_model,
            @Part("vehicle_color") RequestBody vehicle_color,
            @Part("body_style") Integer bodyStyle,
            @Part MultipartBody.Part imageVehicle);

    /**
     * VehicleInformation
     */
    @Multipart
    @POST("api/vehicle/update/{id}")
    Single<VehicleResponse> updateVehicleInfo(
            @Header("Authorization") String authorization,
            @Path("id") int id,
            @Part("vehicle_name") @NonNull RequestBody vehicleName,
            @Part("license_plate") @NonNull RequestBody licensePlate,
            @Part MultipartBody.Part imageVehicle);

    /**
     * VehicleInformation
     */
    @Multipart
    @POST("api/vehicle/request-verify")
    Single<Response> requestVerifyVehicle(
            @Header("Authorization") String authorization,
            @Part("vin") RequestBody VIN,
            @Part("note") RequestBody note,
            @Part("type") Integer type,
            @Part MultipartBody.Part[] imageVerify);

    /**
     * VehicleInformation
     */
    @Multipart
    @POST("api/vehicle/update/{id}")
    Single<UpdateVehicleResponse> updateVehicleInfoNew(
            @Header("Authorization") String authorization,
            @Path("id") int id,
            @Part("vehicle_name") @NonNull RequestBody vehicleName,
            @Part("license_plate") @NonNull RequestBody licensePlate,
            @Part MultipartBody.Part imageVehicle);

    @FormUrlEncoded
    @POST("api/vehicle/update/{id}")
    Single<UpdateVehicleResponse> updateVehicleInfoImageEmpty(
            @Header("Authorization") String authorization,
            @Path("id") int id,
            @Field("vehicle_name") @NonNull String vehicleName,
            @Field("license_plate") @NonNull String licensePlate,
            @Field("vehicle_image") String imageVehicle);

    @GET("api/vehicle/{id}")
    Single<VehicleResponse> getDetailVehicleInfo(
            @Header("Authorization") String authorization,
            @Path("id") int id);


    @DELETE("api/vehicle/{id}")
    Single<VehicleResponse> deleteVehicle(
            @Header("Authorization") String authorization,
            @Path("id") Integer id);

    @GET("api/vehicle?")
    Single<VehicleResponse> getListVehicleByUser(
            @Header("Authorization") String authorization,
            @Query("page") int page);


    @GET("api/vehicle/{vin}/owner")
    Single<CustomerResponse> getCustomerByVIN(
            @Header("Authorization") String authorization,
            @Path("vin") String vin);

    @FormUrlEncoded
    @POST("api/vehicle/verifyVin")
    Single<VinResponse> verifyVin(
            @Header("Authorization") String authorization,
            @Field("vin") String vin);


    @GET("api/province")
    Single<ProvinceResponse> getProvinceList(
            @Header("Authorization") String authorization);

    @GET("api/province/{id}/district")
    Single<DistrictResponse> getProvinceListId(
            @Header("Authorization") String authorization,
            @Path("id") Integer id);


    @GET("api/vehicle-body-style")
    Single<VehicleType> getVehicleBodyStype(
            @Header("Authorization") String authorization,
            @Query("type") Integer type);

    @GET("api/vehicle-brand")
    Single<VehicleType> getVehicleBrand(
            @Query("type") Integer type);

    @GET("api/vehicle-model")
    Single<VehicleType> getVehicleModel(
            @Query("type") Integer type);

    /**
     * Policy
     *
     * @return
     */
    @GET("api/term-condition")
    Single<Terms> getTermCondition();

    @GET("api/privacy-policy")
    Single<Policy> getPolicy();

    @GET("api/warranty/package-info")
    Single<WarrantyResponse> getWarranty(
            @Header("Authorization") String authorization);

    @GET("api/guideline")
    Single<Guide> getGuideline();

    /**
     * Consumption
     */

    @GET("api/vehicle/1/consumption?")
    Single<ConsumptionListResponse> getListConsumption(
            @Header("Authorization") String authorization);

    @GET("api/vehicle/1/consumption/{id}")
    Single<ConsumptionResponse> getDetailConsumption(
            @Header("Authorization") String authorization,
            @Path("id") int id);

    @FormUrlEncoded
    @PUT("api/vehicle/{vehicleID}/consumption//{id}")
    Single<ConsumptionResponse> updateConsumption(
            @Header("Authorization") String authorization,
            @Path("vehicleID") int vehicleID,
            @Path("id") int id);

    @FormUrlEncoded
    @POST("api/vehicle/consumption/update")
    Single<ConsumptionResponse> updateConsumptionFromBTU(
            @Header("Authorization") String authorization,
            @Field("vin") String vin,
            @Field("distance_of_maintenance") int distance_of_maintenance,
            @Field("engine_temperature") int engine_temperature,
            @Field("battery_voltage") int battery_voltage,
            @Field("water_temperature") int water_temperature,
            @Field("intake_air_temperature") int intake_air_temperature,
            @Field("grip_opening_degree") int grip_opening_degree,
            @Field("throttle_opening_degree") int throttle_opening_degree,
            @Field("o2_sensor") int o2_sensor);

    /**
     * Connected API
     */
    @FormUrlEncoded
    @POST("api/vehicle/consumption/recount")
    Single<Response> setOilChange(
            @Header("Authorization") String authorization,
            @Field("last_distance") String distance,
            @Field("vin") String vin);

    //vin: vin1234567
    @GET("api/vehicle/consumption/getLastOilChange/{vin}")
    Single<OilResponse> getLastOilChange(
            @Header("Authorization") String authorization,
            @Path("vin") String vin);


    @GET("api/vehicle/settings/{vin}")
    Single<OilSettingResponse> getConsumptionSetting(
            @Header("Authorization") String authorization,
            @Path("vin") String vin);

    @FormUrlEncoded
    @POST("api/vehicle/settings/{vin}")
    Single<OilSettingResponse> configureSetting(
            @Header("Authorization") String authorization,
            @Path("vin") String vin,
            @Field("date_reminder") String date_reminder,
            @Field("reminder_count") String reminder_count,
            @Field("distance_of_maintenance") String distance_of_maintenance
    );

    @FormUrlEncoded
    @POST("api/vehicle/settings/{vin}")
    Single<OilSettingResponse> configConsumptionSetting(
            @Header("Authorization") String authorization,
            @Path("vin") String vin,
            @Field("distance_of_maintenance") String distance_of_maintenance,
            @Field("date_reminder") String date_reminder,
            @Field("reminder_count") String reminder_count,
            @Field("vehicle_type") String vehicle_type,
            @Field("vin") String vinlkjj
    );

    //    notification API
    @GET("api/notification/owner")
    Single<ListNotifications> getListNotification(
            @Header("Authorization") String authorization
    );

    //    notification API
    @GET("api/get-service-type/1")
    Single<ListTypeService> getListTypeService(
            @Header("Authorization") String authorization
    );

    @FormUrlEncoded
    @PUT("api/vehicle/settings/{vin}")
    Single<OilSettingResponse> configConsumptionSettingFirst(
            @Header("Authorization") String authorization,
            @Path("vin") String vin,
            @Field("date_reminder") String date_reminder,
            @Field("reminder_count") String reminder_count,
            @Field("distance_of_maintenance") String distance_of_maintenance
    );

    /**
     * Booking service
     */
    @FormUrlEncoded
    @POST("api/dealer/time-slot")
    Single<TimeSlot> getTimeSlotOfDay(
            @Header("Authorization") String authorization,
            @Field("dealer_id") String id,
            @Field("date") String date);

    @GET("api/dealer/list")
    Single<DealerResponse> getDealerList(
            @Header("Authorization") String authorization,
            @Query("district_id") Integer district_id,
            @Query("province_id") Integer province_id);

    @GET("api/province/{id}/district")
    Single<DistrictResponse> getDistrictListId(
            @Header("Authorization") String authorization,
            @Path("id") Integer id);

    @FormUrlEncoded
    @POST("api/dealer/day-unavailable")
    Single<DayList> getDayUnavailable(
            @Header("Authorization") String authorization,
            @Field("dealer_id") String id,
            @Field("date") String date);


    @FormUrlEncoded
    @POST("api/service/post-booking-service")
    Single<SubmitBookService> bookAService(
            @Header("Authorization") String authorization,
            @Field("vin") String vin,
            @Field("drop_date") String dropDate,
            @Field("drop_time") String dropTime,
            @Field("date_repair") String dateRepair,
            @Field("service_type") String serviceType,
            @Field("note") String note,
            @Field("expect_time") String expectTime,
            @Field("expect_date") String expectDate,
            @Field("dealer_id") int dealerId);

    @GET("api/service/history/details/{idService}")
    Single<DetailServiceHistoryResponse> getDetailServiceHistory(
            @Header("Authorization") String authorization,
            @Path("idService") Integer id);

    @GET("api/service/history/list/{vin}")
    Single<ServiceHistoryResponse> getServiceHistory(
            @Header("Authorization") String authorization,
            @Path("vin") String vin,
            @Query("page") int page);

    @GET("api/service/history/list/{vin}")
    Single<ServiceHistoryResponse> getServiceHistory(
            @Header("Authorization") String authorization,
            @Path("vin") String vin);

    @GET("api/service/history/list/{vin}")
    Single<ServiceHistoryFullResponse> getFullServiceHistory(
            @Header("Authorization") String authorization,
            @Path("vin") String vin);

    @FormUrlEncoded
    @POST("api/service/cancel-booking")
    Single<CancelBookingResponse> submitCancelBooking(
            @Header("Authorization") String authorization,
            @Path("vin") String vin,
            @Field("reason_id") Integer reason_id,
            @Field("booking_id") Integer booking_id);


    @GET("api/vehicle/list-vehicle-detail")
    Single<VehicelDetailResponse> getListVehicleInformation(
            @Header("Authorization") String authorization);

    @GET("api/vehicle?")
    Single<VehicleResponse> getAllVehicleByUser(
            @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("api/vehicle/list-vehicle-by-ids")
    Single<ConnectedVehicleResponse> getAllConnectedVehicle(
            @Header("Authorization") String authorization,
            @Field("vin[]") List<String> vin);

    @GET("api/vehicle/{id}")
    Single<VehicleSingleResponse> getDetailVehicleByVehicleId(
            @Header("Authorization") String authorization,
            @Path("id") int id
    );
}
