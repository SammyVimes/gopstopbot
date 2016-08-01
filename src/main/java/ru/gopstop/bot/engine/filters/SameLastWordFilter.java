package ru.gopstop.bot.engine.filters;

import ru.gopstop.bot.engine.search.FoundGopSong;

import java.util.Arrays;
import java.util.List;

/**
 * Отбрасываем тексты с таким же последним словом
 * <p>
 * Created by aam on 31.07.16.
 */
public final class SameLastWordFilter {

    private static List<String> buildLexemList(final String request) {
        return Arrays.asList(request.replaceAll("[^A-Za-zА-Яа-я ]", " ").toLowerCase().split("\\s+"));
    }

    public static boolean filter(final String request, final FoundGopSong gopSong) {

        final List<String> reqLL = buildLexemList(request);
        final List<String> reqR = buildLexemList(gopSong.getRhyme());

        return !reqLL.get(reqLL.size() - 1).equals(reqR.get(reqR.size() - 1));
    }

    private SameLastWordFilter() {

    }
}
