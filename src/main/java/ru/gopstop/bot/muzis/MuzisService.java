package ru.gopstop.bot.muzis;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.gopstop.bot.muzis.entity.SearchResult;
import ru.gopstop.bot.muzis.entity.Song;

import java.util.List;

/**
 * Created by Semyon on 30.07.2016.
 */
public interface MuzisService {

    @FormUrlEncoded
    @POST("search.api")
    SearchResult search(@Field("q_track") final String track,
                        @Field("q_performer") final String performer,
                        @Field("q_lyrics") final String lyrics,
                        @Field("q_value") final String value,
                        @Field("size") final Integer size,
                        @Field("offset") final Integer offset,
                        @Field("sort") final String sort);

    @FormUrlEncoded
    @POST("stream_from_obj.api")
    SearchResult relevantSearch(@Field("type") final int type,
                              @Field("id") final long id,
                              @Field("size") final long size);

    @FormUrlEncoded
    @POST("stream_from_lyrics.api")
    SearchResult relevantSearchByText(@Field("lyrics") final String lyrics,
                              @Field("size") final int size,
                              @Field("operator") final String operator);

    @FormUrlEncoded
    @POST("get_songs_by_performer.api")
    SearchResult songsByPerformer(@Field("performer_id") final long performerId);

}
