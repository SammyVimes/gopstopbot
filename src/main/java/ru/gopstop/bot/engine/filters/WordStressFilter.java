package ru.gopstop.bot.engine.filters;

import ru.gopstop.bot.engine.WordStressMap;
import ru.gopstop.bot.engine.search.FoundGopSong;

/**
 * Отбрасываем сильно отличающиеся по ударениям на хвосте строки
 * <p>
 * Created by aam on 31.07.16.
 */
public class WordStressFilter {

    private static final int STRESS_POSTFIX_LENGTH = 3;

    public static boolean filter(String request, FoundGopSong gopSong) {

        final String reqPattern =
                WordStressMap.getInstance().findRhythmicPattern(request);
        final String songPattern =
                WordStressMap.getInstance().findRhythmicPattern(gopSong.getRhyme());

        for (int i = 1; i < STRESS_POSTFIX_LENGTH; i++) {
            if (reqPattern.length() < STRESS_POSTFIX_LENGTH
                    || songPattern.length() < STRESS_POSTFIX_LENGTH
                    || reqPattern.charAt(reqPattern.length() - i) != songPattern.charAt(songPattern.length() - i)) {
                return false;
            }
        }
        return true;
    }
}
