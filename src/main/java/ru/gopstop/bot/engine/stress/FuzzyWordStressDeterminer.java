package ru.gopstop.bot.engine.stress;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Ой а давайте считать, что кроме
 * <p>
 * ямба-хорея
 * амфибрахия-анапеста-дактиля
 * <p>
 * на свете больше ничего не бывает, по крайней мере, в шансоне.
 * Этот код будет пытаться понять, с чем мы имеем дело.
 * <p>
 * Created by aam on 01.08.16.
 */
public final class FuzzyWordStressDeterminer {

    private static String[] processPoemLine(final String poemLine) {

        return poemLine
                .trim()
                .replaceAll("[^a-zA-Zа-яА-я ]", "")
                .toLowerCase()
                .split(" ");
    }

    public static String getStressedWords(final String text) {

        final Map<String, Pair<Integer, Set<Integer>>> dict =
                WordStressMap.getInstance().getCoreWordDict();

        for (final String word : processPoemLine(text)) {

            System.out.println(word);
            System.out.println(dict.get(word).toString());
        }

        return null;
    }

    private FuzzyWordStressDeterminer() {

    }
}
