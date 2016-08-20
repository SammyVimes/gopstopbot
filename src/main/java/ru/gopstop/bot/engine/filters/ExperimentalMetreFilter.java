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

    private static final int MIN_FIRE_SYLLABLES_COUNT = 5;

    private static final int SYLLABLES_DIFF = 3;

    private static final Logger LOGGER = LogManager.getLogger(ExperimentalMetreFilter.class);

    public static boolean filter(final String request, final FoundGopSong gopSong) {

        // тупо смотрим на количество слогов
        final int requestSyllablesCount = WordStressHelper.countVowels(request.toLowerCase());

        if (requestSyllablesCount < MIN_FIRE_SYLLABLES_COUNT) {
            LOGGER.trace("Not applying extra metre filter for " + requestSyllablesCount + " syllables in " + request);
            return true;
        }

        final int songnlineSyllablesCount = WordStressHelper.countVowels(gopSong.getRhyme().toLowerCase());
        return Math.abs(requestSyllablesCount - songnlineSyllablesCount) < SYLLABLES_DIFF;
    }

    private ExperimentalMetreFilter() {

    }
}
