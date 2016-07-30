package ru.gopstop.bot.muzis.entity;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Semyon on 30.07.2016.
 */
public class Song {

    private Long id;

    // тип обьекта (для song всегда 2)
    private Long type;

    // название трека
    @SerializedName("track_name")
    private String trackName;

    // название исполнителя
    private String performer;

    // постер
    private String poster;

    // длина трека в мс
    private long timestudy;

    // id исполнителя
    @SerializedName("performer_id")
    private long performerId;

    // ids исполнителей входящих в состав
    private List<Long> performers;

    // текст трека
    private String lyrics;

    // аудио файл в формате mp3
    @SerializedName("file_mp3")
    private String fileMp3;

    // значения трэка
    @SerializedName("values_all")
    private List<Long> valuesAll;

    public Song() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getType() {
        return type;
    }

    public void setType(final Long type) {
        this.type = type;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(final String trackName) {
        this.trackName = trackName;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(final String performer) {
        this.performer = performer;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(final String poster) {
        this.poster = poster;
    }

    public long getTimestudy() {
        return timestudy;
    }

    public void setTimestudy(final long timestudy) {
        this.timestudy = timestudy;
    }

    public long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(final long performerId) {
        this.performerId = performerId;
    }

    public List<Long> getPerformers() {
        return performers;
    }

    public void setPerformers(final List<Long> performers) {
        this.performers = performers;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(final String lyrics) {
        this.lyrics = lyrics;
    }

    public String getFileMp3() {
        return fileMp3;
    }

    public void setFileMp3(final String fileMp3) {
        this.fileMp3 = fileMp3;
    }

    public List<Long> getValuesAll() {
        return valuesAll;
    }

    public void setValuesAll(final List<Long> valuesAll) {
        this.valuesAll = valuesAll;
    }

    public String getTitle() {
        return performer + " " + trackName;
    }

}
