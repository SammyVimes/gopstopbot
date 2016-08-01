package ru.gopstop.bot.muzis.config;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by Semyon on 30.07.2016.
 */
public final class RetrofitConfig {

    private static final boolean DEBUG = false;

    private static final int READ_TIMEOUT = 10;

    private static final int CONNECT_TIMEOUT = 5;

    private static OkHttpClient createClient() {

        final OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);

        if (DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        return builder.build();
    }

    public static Retrofit create() {

        return new Retrofit.Builder()
                .baseUrl("http://muzis.ru/api/")
                .client(createClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit createResourceRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://f.muzis.ru/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private RetrofitConfig() {

    }
}