package ru.gopstop.bot.engine.filters;

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

    /**
     * Дело в том, что это обычно то же слово с приставкой
     */
    private static final int UNACC_POSTFIX = 6;

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

        if (lastGop.length() >= UNACC_POSTFIX && lastUser.length() >= UNACC_POSTFIX) {
            final String gopPosttfix =
                    lastGop.substring(lastGop.length() - UNACC_POSTFIX, lastGop.length());
            final String inputPostfix =
                    lastUser.substring(lastUser.length() - UNACC_POSTFIX, lastUser.length());
            return !gopPosttfix.equals(inputPostfix);
        }

        return !lastGop.equals(lastUser);
    }

    private SameLastWordFilter() {

    }
}
