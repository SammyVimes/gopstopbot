package ru.gopstop.bot.engine.search.preprocessing;

import com.google.common.primitives.Chars;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by aam on 30.07.16.
 */

public class HackySoundexConverter {

    public static Set<Character> VOWELS =
            Chars.asList("аоуыэяёюие".toCharArray())
                    .stream()
                    .map(a -> (Character) a)
                    .collect(Collectors.toSet());

    public static Set<Character> CONSONANTS_ALL =
            Chars.asList("йцкнгшщзхфвпрлджчсмтб".toCharArray())
                    .stream()
                    .map(a -> (Character) a)
                    .collect(Collectors.toSet());

    public static Map<Character, Character> CONSONANTS_PAIRS =
            Arrays.stream("кг,шж,сз,фв,пб,тд".split(","))
//                    .flatMap(pair -> Stream.of(pair, pair.substring(1) + pair.substring(0, 1)))
                    .collect(Collectors.toMap(s -> s.charAt(1), s -> s.charAt(0)));


    public static String convert(String str) {

        final StringBuilder res = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            char c = str.charAt(i);
            if (c == 'ё') {
                res.append('о');
            } else if (CONSONANTS_PAIRS.containsKey(c)) {
                res.append(CONSONANTS_PAIRS.get(c));
            } else {
                res.append(c);
            }
        }

        return res.toString();
    }
}