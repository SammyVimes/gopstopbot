package ru.gopstop.bot.engine.filters;

import com.google.common.collect.Sets;
import ru.gopstop.bot.engine.search.FoundGopSong;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by aam on 31.07.16.
 */
public class SameLineFilter  {

    private static Set<String> buildSet(String request) {

        return new HashSet<String>(Arrays.asList(request.replaceAll("[A-Za-zА-Яа-я ]", " ").split("\\s+")));
    }

    public static boolean filter(String request, FoundGopSong gopSong) {

        final Set<String> a = buildSet(request);
        final Set<String> b = buildSet(gopSong.getRhyme());
        int size0 = a.size();
        int size1 = b.size();
        int sizeInter = Sets.intersection(a, b).size();

        return (sizeInter + 0.0) / size0 < 0.8 && (sizeInter + 0.0) / size1 < 0.8;
    }
}
