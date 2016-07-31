package ru.gopstop.bot.engine.filters;

import ru.gopstop.bot.engine.EmphasisMap;
import ru.gopstop.bot.engine.search.FoundGopSong;

import java.util.Arrays;
import java.util.List;

/**
 * Отбрасываем сильно отличающиеся по ударениям на хвосте строки
 *
 * Created by aam on 31.07.16.
 */
public class WordStressFilter {

    public static boolean filter(String request, FoundGopSong gopSong) {

        final String reqPattern = EmphasisMap.getInstance().findRhythmicPattern(request);
        final String songPattern = EmphasisMap.getInstance().findRhythmicPattern(gopSong.getRhyme());

        for (int i = 1; i < 3; i++) {
            if (reqPattern.length() < 3 || songPattern.length() < 3 ||
                    reqPattern.charAt(reqPattern.length() - i) != songPattern.charAt(songPattern.length() - i))
                return false;
        }
        return true;
    }
}
