package ru.gopstop.bot.engine.filters;

import com.google.common.collect.Sets;
import ru.gopstop.bot.engine.search.FoundGopSong;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Отбрасываем слишком похожие по составу строки
 * <p>
 * Created by aam on 31.07.16.
 */
public class SameLineFilter {

    private static final double MAX_OVERLAP = 0.8;

    private static Set<String> buildSet(final String request) {
        return new HashSet<String>(Arrays.asList(request.replaceAll("[^A-Za-zА-Яа-я ]", " ").split("\\s+")));
    }

    public static boolean filter(final String request, final FoundGopSong gopSong) {

        final Set<String> a = buildSet(request);
        final Set<String> b = buildSet(gopSong.getRhyme());
        int size0 = a.size();
        int size1 = b.size();
        int sizeInter = Sets.intersection(a, b).size();

        return (sizeInter + 0.0) / size0 < MAX_OVERLAP && (sizeInter + 0.0) / size1 < MAX_OVERLAP;
    }
}
