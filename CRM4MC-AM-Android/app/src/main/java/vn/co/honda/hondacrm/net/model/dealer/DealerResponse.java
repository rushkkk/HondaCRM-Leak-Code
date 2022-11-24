package vn.co.honda.hondacrm.net.model.dealer;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class DealerResponse {
        @SerializedName("code")
        @Expose
        private Integer code;
        @SerializedName("data")
        @Expose
        private List<Dealers> data = null;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("errorCode")
        @Expose
        private String errorCode;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public List<Dealers> getData() {
            return data;
        }

        public void setData(List<Dealers> data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

    }