package ru.gopstop.bot.engine.filters;

import com.google.common.base.Strings;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.engine.search.FoundGopSong;
import ru.gopstop.bot.engine.stress.WordStressHelper;
import ru.gopstop.bot.engine.stress.WordStressMap;
import ru.gopstop.bot.util.SymbolsUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created: aam
 * Date:    20.08.16
 */
public final class ExperimentalSophisticatedMetreFilter {

    private static final Logger LOGGER = LogManager.getLogger(ExperimentalMetreFilter.class);

    private static final Map<String, Pair<Integer, Integer>> STRESS_MAP = WordStressMap.getCoreWordDict();

    private static List<String> buildLexemList(final String request) {
        return Arrays.asList(
                SymbolsUtils
                        .replaceUseless(request, " ")
                        .toLowerCase()
                        .split("\\s+"));
    }

    private static String buildFuzzyPattern(final List<String> words) {

        final StringBuilder sb = new StringBuilder();

        for (final String word : words) {

            final int countSyllables = WordStressHelper.countVowels(word);
            final Pair<Integer, Integer> info = STRESS_MAP.get(word);

            if (info != null && info.getRight() >= 0 && countSyllables > 1) {
                sb.append(WordStressMap.formRhythmicPattern(word, info));
            } else if (countSyllables > 0) {
                sb.append(Strings.repeat(".", countSyllables));
            }

//            LOGGER.info(word + " " + countSyllables + " " + info + " " + sb.toString());
        }
        sb.append(".*");
        return sb.toString();
    }

    public static boolean filter(final String request, final FoundGopSong gopSong) {

        //todo: очень неэффективно, конечно, строить это дело каждый раз, надо подумать
        final List<String> reqLexems = buildLexemList(request);
        final List<String> gopLexems = buildLexemList(gopSong.getRhyme());
        final String gopPattern = buildFuzzyPattern(gopLexems);
        final String reqPattern = buildFuzzyPattern(reqLexems);
        final boolean match = gopPattern.matches(reqPattern) || reqPattern.matches(gopPattern);

        LOGGER.debug("PATTERNS " + match + " "
                + reqLexems + " " + reqPattern + " "
                + gopLexems + " " + gopPattern);
        return true;
    }

    private ExperimentalSophisticatedMetreFilter() {

    }
}