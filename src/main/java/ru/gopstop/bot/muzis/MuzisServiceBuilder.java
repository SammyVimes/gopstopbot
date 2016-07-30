package ru.gopstop.bot.muzis;

import retrofit2.Retrofit;
import ru.gopstop.bot.muzis.config.RetrofitConfig;

/**
 * Created by Semyon on 30.07.2016.
 */
public class MuzisServiceBuilder {

    public static MuzisService getMuzisService() {
        final Retrofit retrofit = RetrofitConfig.create();
        final MuzisService muzisService = retrofit.create(MuzisService.class);
        return muzisService;
    }

    public static ResourcesService getResourcesService() {
        final Retrofit retrofit = RetrofitConfig.createResourceRetrofit();
        final ResourcesService resourcesService = retrofit.create(ResourcesService.class);
        return resourcesService;
    }


}
