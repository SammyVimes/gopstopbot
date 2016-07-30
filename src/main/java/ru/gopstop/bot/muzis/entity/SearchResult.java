package ru.gopstop.bot.muzis.entity;

import java.util.List;

/**
 * Created by Semyon on 30.07.2016.
 */
public class SearchResult {

    private List<Song> songs;

    private List<Performer> performers;

    public SearchResult() {
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(final List<Song> songs) {
        this.songs = songs;
    }

    public List<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(final List<Performer> performers) {
        this.performers = performers;
    }

}
