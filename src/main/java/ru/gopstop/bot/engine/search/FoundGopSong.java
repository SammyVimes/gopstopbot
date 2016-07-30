package ru.gopstop.bot.engine.search;

import ru.gopstop.bot.engine.entities.GopSong;

/**
 * Created by aam on 31.07.16.
 */
public class FoundGopSong {

    // lyrics may be null
    private GopSong gopSong;

    private String rhyme;

    private double score;

    public FoundGopSong(final GopSong gs, final String rh, final double score) {
        rhyme = rh;
        gopSong = gs;
        this.score = score;
    }

    public GopSong getGopSong() {
        return gopSong;
    }

    public String getRhyme() {
        return rhyme;
    }

    public double getScore() {
        return score;
    }
}
