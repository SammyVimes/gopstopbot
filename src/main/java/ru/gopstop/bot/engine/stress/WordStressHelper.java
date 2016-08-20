package ru.gopstop.bot.engine.stress;


import com.google.common.base.Joiner;
import ru.gopstop.bot.engine.tools.PhoneticsKnowledgeTools;
import ru.gopstop.bot.util.SymbolsUtils;

/**
 * Created: aam
 * Date:    20.08.16
 */
public final class WordStressHelper {

    private static final String VOWELS_JOINED_PATTERN =
            Joiner.on("|").join(PhoneticsKnowledgeTools.VOWELS_SET);


    public static String[] processPoemLine(final String poemLine) {

        return SymbolsUtils
                .replaceUseless(poemLine.trim(), "")
                .toLowerCase()
                .split(" ");
    }


    public static int countVowels(final String word) {
        return word.length() - word.replaceAll(VOWELS_JOINED_PATTERN, "").length();
    }

    /**
     * Определяем положение ударения в полном слове
     */
    public static int stressPosition(final String word) {

        final String fixedWord = word.replaceAll("-", "");

        if (fixedWord.contains("'")) {
            return fixedWord.indexOf("'") - 1;
            // у ё не проставлены ударения
        } else if (fixedWord.contains("ё")) {
            return fixedWord.indexOf("ё");
        } else {
            return -1;
        }
    }


    private WordStressHelper() {

    }
}
