package ru.gopstop.bot.engine.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.engine.search.FoundGopSong;
import ru.gopstop.bot.engine.stress.WordStressHelper;

/**
 * Created: aam
 * Date:    20.08.16
 */
public final class ExperimentalMetreFilter {

    public static final int MIN_FIRE_SYLLABLES_COUNT = 5;

    private static final int SYLLABLES_DIFF = 2;

    private static final Logger LOGGER = LogManager.getLogger(ExperimentalMetreFilter.class);

    public static boolean filter(final String request, final FoundGopSong gopSong) {

        // тупо смотрим на количество слогов
        final int requestSyllablesCount = WordStressHelper.countVowels(request);

        if (requestSyllablesCount < MIN_FIRE_SYLLABLES_COUNT) {
            LOGGER.debug("Not applying extra metre filter for " + requestSyllablesCount + " syllables in " + request);
        }

        final int songnlineSyllablesCount = WordStressHelper.countVowels(gopSong.getRhyme());
        return Math.abs(requestSyllablesCount - songnlineSyllablesCount) < SYLLABLES_DIFF;
    }

    private ExperimentalMetreFilter() {

    }
}
