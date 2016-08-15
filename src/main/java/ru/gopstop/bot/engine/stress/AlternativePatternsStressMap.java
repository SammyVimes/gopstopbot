package ru.gopstop.bot.engine.stress;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.engine.tools.PhoneticsKnowledgeTools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by aam on 08.08.16.
 */
public class AlternativePatternsStressMap {

    private static final Logger LOGGER = LogManager.getLogger(WordStressMap.class);

    private static final int DICT_SIZE_HINT = 90000;

    private static final int BUILDING_REPORTING_FREQUENCY = 10000;

    private static final int MIN_SIZE_FOR_YOFICATION_SECOND_WORD = 4;

    private static final String VOWELS_JOINED_PATTERN =
            Joiner.on("|").join(PhoneticsKnowledgeTools.VOWELS_SET);

    public String findRhythmicPattern(final String poemLine) {

        final String[] words = WordStressMap.processPoemLine(poemLine);
        final Map<String, Pair<Integer, Integer>> stressDict = WordStressMap.getCoreWordDict();

        if (words.length > 0) {
            words[words.length - 1] = WordStressMap.fixYo(words[words.length - 1]);

            // фиксим и второе с конца, может быть полезно для коротких слов
            if (words[words.length - 1].length() < MIN_SIZE_FOR_YOFICATION_SECOND_WORD && words.length > 1) {
                words[words.length - 2] = WordStressMap.fixYo(words[words.length - 2]);
            }
        }

        String rhythmicPattern = "";

//        todo
//        for (int i = 0; i < words.length; i++) {
//
//            Pair<Integer, Integer> curWordRhythmicPattern;
//
//            if (stressDict.get(words[i]) != null) {
//                final Pair<Integer, Set<Integer>> p = stressDict.get(words[i]);
//
//                // todo: find a smarter solution
//                final Integer randomStress = p.getRight().iterator().next();
//                curWordRhythmicPattern = Pair.of(p.getLeft(), randomStress);
//            } else {
//                // нет такого слова в словаре
//                // забиваем нулями
//                curWordRhythmicPattern = Pair.of(countVowels(words[i]), -1); // это значит, что не знаем ударение
//            }
//
//            rhythmicPattern += WordStressMap.formRhythmicPattern(words[i], curWordRhythmicPattern);
//        }
        return rhythmicPattern;
    }
}
