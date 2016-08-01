package ru.gopstop.bot.muzis.config;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

class SynchronousCallAdapterFactory extends CallAdapter.Factory {

    static CallAdapter.Factory create() {
        return new SynchronousCallAdapterFactory();
    }

    @Override
    public CallAdapter<Object> get(final Type returnType,
                                   final Annotation[] annotations,
                                   final Retrofit retrofit) {
        // if returnType is retrofit2.Call, do nothing
        if (returnType.getTypeName().contains("retrofit2.Call")) {
            return null;
        }

        return new CallAdapter<Object>() {

            @Override
            public Type responseType() {
                return returnType;
            }

            @Override
            public <R> Object adapt(final Call<R> call) {
                try {
                    return call.execute().body();
                } catch (final IOException e) {
                    throw new RuntimeException("call.execute().body() failed", e); // do something better
                }
            }
        };
    }
}