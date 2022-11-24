package vn.co.honda.hondacrm.net.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;
import vn.co.honda.hondacrm.utils.Constants;


/**
 * Created by CuongNV31.
 */

public class ApiClient {
    private static Retrofit retrofit = null;
    private static int REQUEST_TIMEOUT = 30;
    private static OkHttpClient okHttpClient;

    public static String authen = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjJhNGU0ODAwODVkNzUwNWFmNjU4NTY0NmYxNWNkN2Y4YmQwYWY0NjkxMzIxYWE4Yzg1ZWI1Y2RkZTY3MzQ3NjZkMjJlOTgyOTg2NGI1ZDEyIn0.eyJhdWQiOiIyIiwianRpIjoiMmE0ZTQ4MDA4NWQ3NTA1YWY2NTg1NjQ2ZjE1Y2Q3ZjhiZDBhZjQ2OTEzMjFhYThjODVlYjVjZGRlNjczNDc2NmQyMmU5ODI5ODY0YjVkMTIiLCJpYXQiOjE1NjE1Mzc2NjIsIm5iZiI6MTU2MTUzNzY2MiwiZXhwIjoxNTkzMTYwMDYyLCJzdWIiOiIxMDYiLCJzY29wZXMiOltdfQ.UxpCKVF2FUqn5b215OPb5wynPBegO33qrnt4ifQolsOQ82F0da7P4aPXEGqwTMq6juy7QpzfeSnu19D1lSY7Yc9kLhhGF2DrBU-t-GJZG_0YJ0bCaId7udogu5LkliLfJiTjPLMAVfYwDn3qQC1w3H4aTLtg2BaBUUkuH9XKPcr1EB9n-qTHGlWQt4Rl-y0uGKUZuhQhbCkYzKG3XIfED1GxnKmCZTnTnXY7XzSMyPaMRkVZgAWwvkJXmnaCYefggpgegV3X3IWhQGvFqzy6BIKyc87pLNLTUgvMlpu2YNf6fMc02x0Bd7_es0CpRJM0lKJMy4NbUX7cvrwlX7rm_JbKvcpiWOFYgpd4o8GBd8YHmOxa8MKrqhtITLOTuSkr3D9TmdaFdn8kH2-xxv-bgBQwGfjvgdiO05ek8-3dpdGZQowjhqHQTexmNWnl2zlD7QAj6uzhxXMQ0kgeZRHAZ4hywcdjjOWuNumY4u8qsRXlq_BZRI2firlCoCFQws_fMoaZcO6fmStNDWf-Anyn-5PlDvR5Ay5rE6LdemtUPLJL6RH-KESdQ_ofIHcMeAAVjFwmM_ZncgR0v4EMzCbCP_jg8ZUhKrWYAXi-NbWvmsKb7nBvD80oLWMlL-inyyXtLhbQZwu3tMLUBrNRyIvcEW6RgjlrLASBQfhdnS3C5l8";

    public static Retrofit getClient(Context context) {

        if (okHttpClient == null)
            initOkHttp(context);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static void initOkHttp(final Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json");
//                        .addHeader("Authorization", authen);
                // Adding Authorization token (API Key)
                // Requests will be denied without API key
//                if (!TextUtils.isEmpty(PrefUtils.getApiKey(context))) {
//                    requestBuilder.addHeader("Authorization", PrefUtils.getApiKey(context));
//                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        okHttpClient = httpClient.build();
    }
}
