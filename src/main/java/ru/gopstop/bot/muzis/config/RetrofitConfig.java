package ru.gopstop.bot.muzis.config;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Semyon on 30.07.2016.
 */
public class RetrofitConfig {

    public static Retrofit create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://muzis.ru/api/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit createResourceRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://f.muzis.ru/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

}
