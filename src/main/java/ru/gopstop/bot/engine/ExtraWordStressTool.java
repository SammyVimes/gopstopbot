package ru.gopstop.bot.engine;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by aam on 31.07.16.
 */
public class ExtraWordStressTool {

    public static String[] VOWELS = new String[]{"а", "ы", "о", "э", "е", "я", "и", "ю", "ё", "у"};

    private final static
    Set<Character> VOWELS_SET =
            Arrays
                    .stream(VOWELS)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toSet());

    public static String upperCaseByPattern(String word, String pattern) {

        int vowIndex = 0;
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {

            if (VOWELS_SET.contains(word.charAt(i))) {
                if (pattern.charAt(vowIndex) == '1') {
                    sb.append(Character.toUpperCase(word.charAt(i)));
                } else {
                    sb.append(word.charAt(i));
                }
                vowIndex += 1;
            } else {
                sb.append(word.charAt(i));
            }
        }
        return sb.toString();
    }

    public static String upperCaseStress(String word) {

        final String rhPattern =
                EmphasisMap.getInstance().findRhythmicPattern(word);
        return upperCaseByPattern(word, rhPattern);
    }
}
