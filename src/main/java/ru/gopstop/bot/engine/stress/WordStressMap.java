package ru.gopstop.bot.engine.stress;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.engine.tools.PhoneticsKnowledgeTools;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by n.pritykovskaya on 30.07.16.
 */
public final class WordStressMap {

    private static final Logger LOGGER = LogManager.getLogger(WordStressMap.class);

    private static final String STRESS_DICT_PATH = "./data/emphasis.txt";

    private static final String SERIALIZED_DICT_PATH = "stress_map.bin";

    private static final int DICT_SIZE_HINT = 1600000;

    private static final int BUILDING_REPORTING_FREQUENCY = 10000;

    private static final String VOWELS_JOINED_PATTERN = Joiner.on("|").join(PhoneticsKnowledgeTools.VOWELS_SET);

    /**
     * Варианты ударений для данного слова
     * слово -> (число гласных, порядковый номер ОДНОГО ударного слога, начиная с нуля)
     */
    private static HashMap<String, ArrayList<Pair<Integer, Integer>>> stressDict = new HashMap<>(DICT_SIZE_HINT);

    private static final WordStressMap INSTANCE;

    public static WordStressMap getInstance() {
        return INSTANCE;
    }

    static {
        try {
            INSTANCE = new WordStressMap();
        } catch (final IOException ioe) {
            LOGGER.error("emp map down", ioe);
            throw new RuntimeException(ioe);
        }
    }

    private int countVowels(final String word) {
        return word.length() - word.replaceAll(VOWELS_JOINED_PATTERN, "").length();
    }

    /**
     * Определяем положение ударения в полном слове
     */
    private int stressPosition(final String word) {

        final String fixedWord = word.replaceAll("-", "");

        if (fixedWord.contains("'")) {
            return fixedWord.indexOf("'") - 1;
            // у ё не проставлены ударения
        } else if (fixedWord.contains("ё")) {
            return fixedWord.indexOf("ё");
        } else {
            return 0;
        }
    }

    private boolean alreadyIn(final Pair<Integer, Integer> curRhythmicPattern,
                              final ArrayList<Pair<Integer, Integer>> prevRhythmicPatterns) {

        for (final Pair<Integer, Integer> pattern : prevRhythmicPatterns) {
            if (curRhythmicPattern.equals(pattern)) {
                return true;
            }
        }
        return false;
    }

    private void parseLine(final String line) {

        final String[] parts = line.split("#");
        final String[] words = parts[1].split(",");

        for (int i = 0; i < words.length; i++) {

            // строим просто слово
            final String wordNoStress = words[i].replace("'", "").replace("`", "");
            final int vowelsNumb = countVowels(wordNoStress);

            if (vowelsNumb > 0) {

                final int stressPos = stressPosition(words[i].replace("`", ""));

                final Pair<Integer, Integer> rhythmicPattern = Pair.of(vowelsNumb, stressPos);

                if (stressDict.get(wordNoStress) == null) {

                    final ArrayList<Pair<Integer, Integer>> newArr = new ArrayList<>(1);
                    newArr.add(rhythmicPattern);
                    stressDict.put(wordNoStress, newArr);

                } else {

                    if (!alreadyIn(rhythmicPattern, stressDict.get(wordNoStress))) {
                        stressDict
                                .get(wordNoStress)
                                .add(rhythmicPattern);
                    }
                }
            }
        }
    }

    private WordStressMap() throws IOException {

        final HashMap<String, ArrayList<Pair<Integer, Integer>>> map;

        try {
            final FileInputStream fis = new FileInputStream(SERIALIZED_DICT_PATH);
            final ObjectInputStream ois = new ObjectInputStream(fis);
            map = (HashMap) ois.readObject();
            stressDict = map;
            ois.close();
            fis.close();
        } catch (Exception e) {

            LOGGER.warn("COULD NOT DESER MAP");

            //todo: читать нормально, например, заюзать StreamAPI
            try (final BufferedReader br = new BufferedReader(new FileReader(STRESS_DICT_PATH))) {

                String line = br.readLine();
                int cntLine = 0;

                while (line != null) {
                    parseLine(line);
                    cntLine++;

                    if (cntLine % BUILDING_REPORTING_FREQUENCY == 0) {
                        LOGGER.info("Lines of wod stress dict indexed: " + cntLine);
                    }
                    line = br.readLine();
                }

                LOGGER.info("Serialization...");

                try {
                    final FileOutputStream fos = new FileOutputStream(SERIALIZED_DICT_PATH);
                    final ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(stressDict);
                    oos.close();
                    fos.close();
                    LOGGER.info("Serialized HashMap data is saved in " + SERIALIZED_DICT_PATH);
                } catch (final IOException ioe) {
                    LOGGER.error("Stress dict dumping failure", ioe);
                }
                LOGGER.info("Stress dict serialization done");

            } catch (final IOException ie) {
                LOGGER.error("Мапа ударений не зачитана", ie);
                throw new RuntimeException(ie);
            }
        }
    }

    public int getSize() {
        return stressDict.size();
    }

    private String[] processPoemLine(final String poemLine) {

        return poemLine
                .trim()
                .replaceAll("[^a-zA-Zа-яА-я ]", "")
                .toLowerCase()
                .split(" ");
    }

    private String formRhythmicPattern(final Pair<Integer, Integer> rhythmicPattern) {
        // комменты расставил как догадался
        // rhythmicPatter[0] -- кол-во слогов
        char[] str = new char[rhythmicPattern.getLeft()];
        Arrays.fill(str, '0');

        // rhythmicPatter[1] -- на каком слоге ударение
        final int stressIndex = rhythmicPattern.getRight();

        if (stressIndex >= str.length) {
            LOGGER.warn("WEIRD BUG " + new String(str) + " - " + stressIndex);
            return new String(str);
        }

        if (stressIndex != -1) {
            str[stressIndex] = '1';
        }

        return new String(str);
    }

    public String findRhythmicPattern(final String poemLine) {

        final String[] words = processPoemLine(poemLine);
        String rhythmicPattern = "";

        for (int i = 0; i < words.length; i++) {

            Pair<Integer, Integer> curWordRhythmicPattern;

            if (stressDict.get(words[i]) != null) {
                curWordRhythmicPattern = stressDict.get(words[i]).get(0);
            } else {
                // нет такого слова в словаре
                // забиваем нулями
                curWordRhythmicPattern = Pair.of(countVowels(words[i]), -1); // это значит, что не знаем ударение
            }

            rhythmicPattern = rhythmicPattern + formRhythmicPattern(curWordRhythmicPattern);
        }
        return rhythmicPattern;
    }

    public Map<String, ArrayList<Pair<Integer, Integer>>> getCoreWordDict() {
        return stressDict;
    }
}
