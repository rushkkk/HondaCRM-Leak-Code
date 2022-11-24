package vn.co.honda.hondacrm.net.model.base;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CuongNV31.
 */

public class User extends BaseResponse {

    @SerializedName("api_key")
    String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}
