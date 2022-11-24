package vn.co.honda.hondacrm.utils;

/**
 * @author CuongNV31
 */
public class Constants {

    public static final int ZERO = 0;
    public static final int MAX_TUTORIAL = 4;
    public static final int MENU_ITEMS = 7;
    public static final String DONE = "DONE";
    public static final String EDIT = "EDIT";
    public static final int FIRST_VEHICLE = 1;
    public static final int MENU_FAQ = 0;
    public static final int MENU_TUTORIAL = 1;
    public static final int MENU_POLICY = 2;
    public static final int MENU_TEMRS = 3;
    public static final int MENU_LANGUAGE = 4;
    public static final int MENU_RATE = 5;
    public static final int MENU_LOGOUT = 6;
    public static final int FIRST = 1;
    public static final float ONE = 1;
    public static final String BASE_URL = "https://crm.wingadium.space/";
    public static final String SPACE = " ";
    public static final String GRAND_TYPE = "password";
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd";
    public static final String SPLIT_DATE = "/";
    public static final String EMPTY = "";
    public static final String ZERO_STRING = "0";
    public static final String KEY_SOCIAL = "key_social";
    public static final String KEY_ACCESS_TOKEN = "key_access_token";
    public static final String GRAND_TYPE_SOCIAL = "social";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_PASS = "pass";
    public static final String BEAR = "";
    public static final int MAX_ITEMS = 15;
    public static final int NUMBER_ONE = 1;
    public static final int AVAILABLE = 1;
    public static final int UNAVAILABLE = 1;
    public static final String KEY_TUTORIAL = "tutorial";
    public static final String KEY_TYPE_SOCIAL = "socialtype";

    private Constants() {
        // for preventing new Constants()
    }

    public static final int DELAY_TIME = 3000;

    public static final String NEWS_DETAILED_FRAGMENT_NEWS_MODEL_KEY = "NEWS_DETAILED_FRAGMENT_NEWS_MODEL_KEY";

    public static final int TAB_PRODUCTS = 0;
    public static final int TAB_VEHICLES = 1;
    public static final int TAB_HOME = 2;
    public static final int TAB_DEALERS = 3;
    public static final int TAB_CONNECTED = 4;
    public static final int TAB_NUMBERS = 5;

    // Define constant otp
    public static final int MAX_REQUEST_OTP = 10;
    public static final int MAX_LENGHT_OTP = 6;
    public static final int MAX_REMAINING_TIME = 300000;

    // Define constant phone number
    public static final int LENGHT_PHONE_NUMBER = 9;

    // This define width of screen
    public static final double DIALOG_WIDTH = 0.9;

    //define change oil
    public static final String VEHICLE_OIL = "vehicleoil";
    public static final String DISTANCE_OIL = "distanceoil";
    public static final int KM_CHANGE_OIL = 2000;
    public static final int NUM_DAY_NOTIFICATION = 3;
    public static final int NUM_IN_DAY_NOTIFICATION = 2;

    // Define network social
    public static final int FACE_BOOK = 0;
    public static final int INSTAGRAM = 1;
    public static final int TWITTER = 2;
    public static final int YOUTUBE = 3;
    public static final int HOME = 4;

    // Define tab profile
    public static final int TAB_MY_INFO = 0;
    public static final int TAB_MY_FOLLOW = 1;
    public static final int TAB_MY_EVENT = 2;

    // API constant
    public static final String CLIENT_ID = "2";
    public static final String SECRET_KEY = "poAm2UPB6BuKepNmWrHsomd1KGyDOjN8Bl1aN9pO";
    public static final String ERROR_EXIST = "ERROR_EXIST";

    //Max VIN number
    public static final int MAX_VIN_NUMBER = 17;

    // API Constant
    // Login api
    public static final String ACCOUNT_IS_NOT_EXISTS = "IS_NOT_EXISTS";
    public static final String ACCOUNT_IS_NOT_ACTIVE = "IS_NOT_ACTIVE";
    public static final String ACCOUNT_IS_NOT_MATCH_PASSWORD = "IS_NOT_MATCH_PASSWORD";

    // Register api
    public static final String NUMBER_PHONE_EXISTS = "The phone has already been taken.";

    // Send forgot pass OTP
    public static final String OTP_SEND_WITHIN_5_MINS = "OTP_SENT_WITHIN_5_MINUTES";
    public static final String OTP_SEND_WITHIN_MAX_TIMES = "OTP_SENT_MAX_TIMES";
    public static final String OTP_IS_NOT_MATCH = "OTP_NOT_MATCH";

    // connected vehicle
    public static final int TIME_TO_UPDATE = 0;
    public static final String ITEM_BOOK_SERVICE = "ITEM_BOOK_SERVICE";
    public static final String ITEM_CHANGE_OIL = "ITEM_CHANGE_OIL";
    public static final String ITEM_CONNECT_NOW = "ITEM_CONNECT_NƠW";
    public static final String TIME_TO_UPDATE_VEHICLE_DETAIL = "TIME_TO_UPDATE_VEHICLE_DETAIL";
    public static final String DEFAULT_VEHICLE = "DEFAULT_VEHICLE";
    public static final String KEY_WAITING_BTU_DISCONNECT = "KEY_WAITING_BTU_DISCONNECT";
    public static final String VIN_OF_CONNECTED_VEHICLE_NOW = "VIN_OF_CONNECTED_VEHICLE_NOW";
    public static final String KEY_BTU_NAME_REGISTER_SUCCESS = "key_btu_name_register_success";
    public static final String MY_LOG_TAG = "MY_LOG_TAG";

    public static final int BTU_ACTIVE = 1;
    public static final int BTU_INACTIVE = 2;
    public static final int BTU_UNREGISTERED = 4;
    public static final int DISCONNECT = 3;
    public static final String ACTION_UPDATE_API = "actionupdateapi";
    public static final String UPDATE_CURRENT_VIEWPAGER = "UPDATE_CURRENT_VIEWPAGER";
    public static final String TYPE_VIEWPAGE = "TYPE_VIEWPAGE";


    public static final int REQUEST_CAMERA_CODE = 1;
    public static final int REQUEST_GALLERY_CODE = 2;
    public static final String POSITION_VEHICLE_DETAIL = "POSITION_VEHICLE_DETAIL";
    public static final String ACTION_SET_CURRENT_DETAIL_VEHICLE = "ACTION_SET_CURRENT_DETAIL_VEHICLE";

    // Define flag for Animation transition screen.
    public static final int ANIMATE_NONE = -1;
    public static final int ANIMATE_FORWARD = 0;
    public static final int ANIMATE_BACK = 1;
    public static final int ANIMATE_EASE_IN_OUT = 2;
    public static final int ANIMATE_SLIDE_TOP_FROM_BOTTOM = 3;
    public static final int ANIMATE_SLIDE_BOTTOM_FROM_TOP = 4;
    public static final int ANIMATE_SCALE_IN = 5;
    public static final int ANIMATE_SCALE_OUT = 6;

    //Permission camera
    public static final int PERMISSION_REQUEST_CAMERA_CODE = 200;

    public static final String DATABASE_NAME = "connected_vehicle_db";
}
