package ru.gopstop.bot.engine.filters;

import ru.gopstop.bot.engine.search.FoundGopSong;
import ru.gopstop.bot.engine.stress.WordStressMap;

/**
 * Отбрасываем сильно отличающиеся по ударениям на хвосте строки
 * <p>
 * Created by aam on 31.07.16.
 */
public final class WordStressFilter {

    private static final int STRESS_POSTFIX_LENGTH = 2;

    public static boolean filter(final String request, final FoundGopSong gopSong) {

        final String reqPattern =
                WordStressMap.getInstance().findRhythmicPattern(request);
        final String songPattern =
                WordStressMap.getInstance().findRhythmicPattern(gopSong.getRhyme());

        if (reqPattern.length() < STRESS_POSTFIX_LENGTH) {
            return true;
        }

        for (int i = 1; i < STRESS_POSTFIX_LENGTH; i++) {
            if (reqPattern.length() < STRESS_POSTFIX_LENGTH
                    || songPattern.length() < STRESS_POSTFIX_LENGTH
                    || reqPattern.charAt(reqPattern.length() - i) != songPattern.charAt(songPattern.length() - i)) {
                return false;
            }
        }

        return true;
    }

    private WordStressFilter() {

    }
}
