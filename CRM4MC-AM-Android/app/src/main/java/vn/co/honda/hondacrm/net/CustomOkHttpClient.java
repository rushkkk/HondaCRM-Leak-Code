package vn.co.honda.hondacrm.net;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import vn.co.honda.hondacrm.BuildConfig;

/**
 * @author CuongNV31
 */
@Singleton
public class CustomOkHttpClient extends OkHttpClient {

    private static final int CACHE_SIZE = 10 * 1024 * 1024; // 10 Mb
    private static final int CONNECT_TIMEOUT = 60; // 60 seconds
    private static final int READ_WRITE_TIMEOUT = 120; // 120 seconds

    public OkHttpClient getOkHttpClient(Context context) {
        Builder okHttpClientBuilder = new Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(READ_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(setCacheSettings(context));

//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        okHttpClientBuilder.addInterceptor(interceptor);
        if (BuildConfig.DEBUG) okHttpClientBuilder.addInterceptor(setLogging());

        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json");

                // Adding Authorization token (API Key)
                // Requests will be denied without API key
//                if (!TextUtils.isEmpty(PrefUtils.getApiKey(context))) {
//                    requestBuilder.addHeader("Authorization", "apikey");
//                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return okHttpClientBuilder.build();
    }

    private Cache setCacheSettings(Context context) {
        File cacheDir = new File(context.getCacheDir(), "HttpCache");
        return new Cache(cacheDir, CACHE_SIZE);
    }

    private Interceptor setLogging() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}
