package ru.gopstop.bot.engine.filters;

import ru.gopstop.bot.engine.search.FoundGopSong;

/**
 * Created by aam on 08.08.16.
 */
public final class UglyDataFilter {

    public static boolean filter(final String request, final FoundGopSong gopSong) {
        return !gopSong.getRhyme().contains("nbsp")
                && !gopSong.getRhyme().toLowerCase().contains("припев");
    }

    private UglyDataFilter() {

    }
}
