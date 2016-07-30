package ru.gopstop.bot.engine.entities;

/**
 * Created by Semyon on 30.07.2016.
 */
public class Rhyme {

    private String rhyme;

    private GopSong gopSong;

    public Rhyme(final String rhyme, final GopSong gopSong) {
        this.rhyme = rhyme;
        this.gopSong = gopSong;
    }

    public GopSong getGopSong() {
        return gopSong;
    }

    public void setGopSong(final GopSong gopSong) {
        this.gopSong = gopSong;
    }

    public String getRhyme() {
        return rhyme;
    }

    public void setRhyme(final String rhyme) {
        this.rhyme = rhyme;
    }

}
