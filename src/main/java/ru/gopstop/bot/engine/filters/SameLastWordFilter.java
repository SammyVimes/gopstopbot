package ru.gopstop.bot.engine.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.engine.search.FoundGopSong;
import ru.gopstop.bot.util.SymbolsUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Отбрасываем тексты с таким же последним словом
 * <p>
 * Created by aam on 31.07.16.
 */
public final class SameLastWordFilter {

    private static final Logger LOGGER = LogManager.getLogger(SameLastWordFilter.class);

    /**
     * Дело в том, что это обычно то же слово с приставкой
     */
    private static final int UNACC_POSTFIX = 4;


    private static final List<String> PREFIXES =
            Arrays.asList(
                    // longer go first
                    "сверх", "пере", "анти", "разъ", "подъ",
                    "при", "про", "над", "обо", "бес", "раз", "без", "рас",
                    "об", "из", "ис", "за", "въ", "съ", "не", "во", "со",
                    "о", "а", "c", "в", "у");

    private static List<String> buildLexemList(final String request) {
        return Arrays.asList(
                SymbolsUtils
                        .replaceUseless(request, " ")
                        .toLowerCase()
                        .replaceAll("ё", "е")
                        .split("\\s+"));
    }

    public static boolean filter(final String request, final FoundGopSong gopSong) {

        final List<String> reqLL = buildLexemList(request);
        final List<String> reqR = buildLexemList(gopSong.getRhyme());

        final String lastGop = reqR.get(reqR.size() - 1);
        final String lastUser = reqLL.get(reqLL.size() - 1);

        LOGGER.debug("Checking if approximately equal " + lastGop + " " + lastUser);

        // одинаковые не пропускаем
        if (lastGop.equals(lastUser)) {
            LOGGER.debug("Same word, discarding: " + lastGop + " " + lastUser);
            return false;
        }

        // отличающиеся приставкой не пропускаем
        if (lastGop.length() >= UNACC_POSTFIX && lastUser.length() >= UNACC_POSTFIX) {

            final int lastGopLength = lastGop.length();
            final int lastUserLength = lastUser.length();

            final String gopPosttfix =
                    lastGop.substring(lastGop.length() - UNACC_POSTFIX, lastGopLength);
            final String inputPostfix =
                    lastUser.substring(lastUser.length() - UNACC_POSTFIX, lastUserLength);

            if (gopPosttfix.equals(inputPostfix)) {

                LOGGER.debug("Likely same base words: " + lastGop + " " + lastUser);
                LOGGER.debug("Likely same base words endings: " + gopPosttfix + " " + inputPostfix);

                final String longer = (lastGopLength > lastUserLength ? lastGop : lastUser);
                final String shorter = (lastGopLength < lastUserLength ? lastGop : lastUser);

                for (final String pr : PREFIXES) {

                    if (longer.startsWith(pr)
                            && longer.length() > pr.length()
                            && longer.substring(pr.length(), longer.length()).equals(shorter)) {

                        LOGGER.debug("Skipping words as однокоренные " + longer + " " + shorter);
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private SameLastWordFilter() {

    }
}
