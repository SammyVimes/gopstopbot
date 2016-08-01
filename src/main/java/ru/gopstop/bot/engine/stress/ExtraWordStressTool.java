package ru.gopstop.bot.engine.stress;

import static ru.gopstop.bot.engine.tools.PhoneticsKnowledgeTools.*;

/**
 * Получение ударений в слове по паттерну, полученному с помощью словаря
 * Created by aam on 31.07.16.
 */
public final class ExtraWordStressTool {

    public static String upperCaseByPattern(final String word, final String pattern) {

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

    public static String upperCaseStress(final String word) {

        final String rhPattern = WordStressMap.getInstance().findRhythmicPattern(word);
        return upperCaseByPattern(word, rhPattern);
    }

    private ExtraWordStressTool() {

    }
}
