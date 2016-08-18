package ru.gopstop.bot.muzis;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Semyon on 30.07.2016.
 */
public interface ResourcesService {

    @GET("/{file}")
    ResponseBody downloadFile(@Path("file") final String file);

}