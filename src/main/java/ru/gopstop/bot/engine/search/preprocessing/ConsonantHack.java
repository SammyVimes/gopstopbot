package ru.gopstop.bot.engine.search.preprocessing;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Заменяем парные согласные, когда можно
 * Created by Semyon on 31.07.2016.
 */
class ConsonantHack implements LastWordProcessor {

    private static final Map<Character, Character> CONSONANTS_PAIRS =
            Arrays
                    .stream("кг,шж,сз,фв,пб,тд".split(","))
                    .collect(Collectors.toMap(s -> s.charAt(1), s -> s.charAt(0)));

    private static final List<Character> VOWELS =
            Arrays
                    .stream("а|ы|е|ё|и|у|о|э|я|ю".split("|"))
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList());

    @Override
    public String process(final String lastWord) {

        String processed = lastWord;

        for (int i = 0; i < processed.length(); i++) {

            if (i < processed.length() - 1) {
                // проверяем сл. букву
                final char nextChar = (processed.charAt(i + 1) + "").toLowerCase().charAt(0);

                if (VOWELS.indexOf(nextChar) == -1) {
                    // следующая буква не гласная
                    // проверяем текущую букву
                    final char curChar = processed.charAt(i);
                    final Character character = CONSONANTS_PAIRS.get(curChar);

                    if (character != null) {
                        // она парная согласная
                        processed = processed.substring(0, i) + character + processed.substring(i + 1);
                    }
                }
            } else {
                // последняя буква
                final char curChar = processed.charAt(i);
                final Character character = CONSONANTS_PAIRS.get(curChar);

                if (character != null) {
                    // она парная согласная
                    processed = processed.substring(0, i) + character;
                }
            }
        }
        return processed;
    }
}
