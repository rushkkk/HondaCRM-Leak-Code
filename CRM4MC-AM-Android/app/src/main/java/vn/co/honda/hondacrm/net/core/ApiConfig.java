package vn.co.honda.hondacrm.net.core;

import android.content.Context;
import android.support.annotation.NonNull;

public class ApiConfig {
    Context context;
    String baseUrl;
    boolean isAuth;
    boolean isAgent;

    public ApiConfig(Builder builder) {
        context = builder.context;
        baseUrl = builder.baseUrl;
        isAuth = builder.isAuth;
        isAgent = builder.isAgent;

    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public static class Builder {
        Context context;
        String baseUrl;
        boolean isAuth;
        boolean isAgent;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder baseUrl(@NonNull String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setAgent(boolean agent) {
            isAgent = agent;
            return this;
        }

        public Builder setAuth(boolean auth) {
            isAuth = auth;
            return this;
        }

        public ApiConfig build() {
            return new ApiConfig(this);
        }
    }

}
