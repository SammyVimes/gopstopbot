package ru.gopstop.bot.engine.filters;

import ru.gopstop.bot.engine.search.FoundGopSong;
import ru.gopstop.bot.engine.stress.WordStressHelper;

/**
 * Created: aam
 * Date:    20.08.16
 */
public final class ExperimentalMetreFilter {

    private static final int MIN_FIRE_SYLLABLES_COUNT = 5;

    private static final int SYLLABLES_DIFF = 2;

    public static boolean filter(final String request, final FoundGopSong gopSong) {

        // тупо смотрим на количество слогов

        final int requestSyllablesCount = WordStressHelper.countVowels(request);
        final int songnlineSyllablesCount = WordStressHelper.countVowels(gopSong.getRhyme());

        return WordStressHelper.countVowels(request) < MIN_FIRE_SYLLABLES_COUNT
                || Math.abs(requestSyllablesCount - songnlineSyllablesCount) < SYLLABLES_DIFF;
    }

    private ExperimentalMetreFilter() {

    }
}
