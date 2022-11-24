package vn.co.honda.hondacrm.di.application;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import vn.co.honda.hondacrm.net.ApiService;
import vn.co.honda.hondacrm.net.CustomOkHttpClient;
import vn.co.honda.hondacrm.utils.Constants;

/**
 * @author CuongNV31
 */
@Module
public class NetworkModule {

    private static final String BASE_URL = "https://lenta.ru/";
    private static final String BASE_URL_NEW = "https://raw.githubusercontent.com/android10/Sample-DataVehicleInformation/master/Android-CleanArchitecture/";
    private ApiService mApi;
    private final Context appContext;

    public NetworkModule(@NonNull Context context) {
        appContext = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return appContext;
    }

    @Provides
    CustomOkHttpClient provideOkHttpClient() {
        return new CustomOkHttpClient();
    }

    @Provides
    ApiService provideApiClient() {
        if (mApi == null) {
            return mApi = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(provideOkHttpClient().getOkHttpClient(appContext))
                    .build()
                    .create(ApiService.class);
        }
        return mApi;
    }
}
