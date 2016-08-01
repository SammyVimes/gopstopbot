package ru.gopstop.bot.muzis;

import retrofit2.Retrofit;
import ru.gopstop.bot.muzis.config.RetrofitConfig;

/**
 * Created by Semyon on 30.07.2016.
 */
public final class MuzisServiceBuilder {

    public static MuzisService getMuzisService() {
        final Retrofit retrofit = RetrofitConfig.create();
        return retrofit.create(MuzisService.class);
    }

    public static ResourcesService getResourcesService() {
        final Retrofit retrofit = RetrofitConfig.createResourceRetrofit();
        return  retrofit.create(ResourcesService.class);
    }

    private MuzisServiceBuilder() {

    }
}
