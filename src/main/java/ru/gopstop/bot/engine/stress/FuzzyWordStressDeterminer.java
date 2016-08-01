package ru.gopstop.bot.engine.stress;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.util.SymbolsUtils;

/**
 * Ой а давайте считать, что кроме
 * <p>
 * ямба-хорея
 * амфибрахия-анапеста-дактиля
 * <p>
 * на свете больше ничего не бывает, по крайней мере, в шансоне.
 * Этот код будет пытаться понять, с чем мы имеем дело.
 * <p>
 * Словарь же весьма так себе.
 * <p>
 * Created by aam on 01.08.16.
 */
public final class FuzzyWordStressDeterminer {

    private static final Logger LOGGER = LogManager.getLogger(FuzzyWordStressDeterminer.class);

    private static final int MAX_LEVE_DIST = 4;

    public enum Foot {
        IAMB("01"),
        CHOREE("10"),
        DACTYL("100"),
        AMPHIBRACH("010"),
        ANAPEST("001");

        private String repeatedStuff;

        Foot(final String repe) {
            repeatedStuff = repe;
        }

        public String getRepeatedStuff() {
            return repeatedStuff;
        }
    }

    private static String[] processPoemLine(final String poemLine) {
        return
                SymbolsUtils
                        .replaceUseless(poemLine.trim(), " ")
                        .toLowerCase()
                        .split(" ");
    }

    private static Pair<Pair<Foot, Integer>, String> detectSimpleFoot(final String pattern) {

        // считаем просто растояние Левенштейна между паттернами
        // кто победил -- молодец
        int bestEditDistance = pattern.length() * MAX_LEVE_DIST;
        Foot bestFoot = null;
        String bestPattern = null;

        for (final Foot foot : Foot.values()) {

            final int repetitions = pattern.length() / foot.getRepeatedStuff().length() + 1;
            final String footGenPattern =
                    StringUtils
                            .repeat(foot.getRepeatedStuff(), repetitions)
                            .substring(0, pattern.length());

            boolean neighbouringStresses = false;

            for (int i = 1; i < footGenPattern.length(); i++) {

                neighbouringStresses = neighbouringStresses
                        || footGenPattern.charAt(i) == '1' && pattern.charAt(i - 1) == '1'
                        || footGenPattern.charAt(i - 1) == '1' && pattern.charAt(i) == '1';
            }

            if (neighbouringStresses) {
                LOGGER.debug("Neighbouring stresses " + footGenPattern + " " + pattern
                        + ", skipping foot " + foot);
                continue;
            }

            int dist = StringUtils.getLevenshteinDistance(pattern, footGenPattern);

            if (dist < bestEditDistance) {
                bestEditDistance = dist;
                bestFoot = foot;
                bestPattern = footGenPattern;
            }
        }
        return Pair.of(Pair.of(bestFoot, bestEditDistance), bestPattern);
    }

    public static Pair<Foot, String> getPatternFittedToSomeSimpleFoot(final String text) {

        final WordStressMap wsm = WordStressMap.getInstance();
        final String pattern = wsm.findRhythmicPattern(text);
        final Pair<Pair<Foot, Integer>, String> bestFittingFoot = detectSimpleFoot(pattern);

        LOGGER.debug("Text [" + text + "] foot " + bestFittingFoot);
        return Pair.of(bestFittingFoot.getLeft().getLeft(), bestFittingFoot.getRight());
    }

    private FuzzyWordStressDeterminer() {

    }
}
