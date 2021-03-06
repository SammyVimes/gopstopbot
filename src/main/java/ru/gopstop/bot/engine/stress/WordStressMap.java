package ru.gopstop.bot.engine.stress;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.engine.tools.PhoneticsKnowledgeTools;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ru.gopstop.bot.engine.stress.WordStressHelper.*;

/**
 * Created by n.pritykovskaya on 30.07.16.
 */
public final class WordStressMap {

    private static final Logger LOGGER = LogManager.getLogger(WordStressMap.class);

    private static final String STRESS_DICT_PATH = "./data/emphasis.txt";

    private static final String SERIALIZED_DICT_PATH = "stress_map.bin";

    private static final int DICT_SIZE_HINT = 90000;

    private static final int BUILDING_REPORTING_FREQUENCY = 10000;

    private static final int MIN_SIZE_FOR_YOFICATION_SECOND_WORD = 4;


    /**
     * Варианты ударений для данного слова
     * слово -> (число гласных, порядковый номера ОДНОГО ударного слога, начиная с нуля)
     */
    private static HashMap<String, Pair<Integer, Integer>> stressDict =
            new HashMap<>(DICT_SIZE_HINT);

    private static final WordStressMap INSTANCE;

    public static WordStressMap getInstance() {
        return INSTANCE;
    }

    static {
        try {
            INSTANCE = new WordStressMap();
        } catch (final IOException ioe) {
            LOGGER.error("Stress map is dead, no use in continuations", ioe);
            throw new RuntimeException(ioe);
        }
    }

    private static void parseLine(final String line) {

        final String[] parts = line.split("#");
        final String[] words = parts[1].split(",");

        for (int i = 0; i < words.length; i++) {

            // строим просто слово
            final String wordNoStress = words[i].replace("'", "").replace("`", "");
            final int vowelsCount = countVowels(wordNoStress);

            if (vowelsCount > 0) {

                final int stressPos = stressPosition(words[i].replace("`", ""));

                if (!stressDict.containsKey(wordNoStress)) {
                    stressDict.put(wordNoStress, Pair.of(vowelsCount, stressPos));
                } else {
                    // какой вариант ударения первый -- тот и молодец
                    // фу как грубо
                }
            }
        }
    }

    private WordStressMap() throws IOException {

        final HashMap<String, Pair<Integer, Integer>> map;

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

    /**
     * Ищем в словаре слово, если подозреваем, что просто требуется Ё-фикация
     */
    public static String fixYo(final String word) {

        //todo: write effective code

        if (!stressDict.containsKey(word) && word.contains("е")) {

            final StringBuilder sb = new StringBuilder(word);

            // чаще всего всё-таки одна буква 'ё'
            for (int i = 0; i < word.length(); i++) {

                if (word.charAt(i) == 'е') {

                    sb.setCharAt(i, 'ё');
                    final String attempt = sb.toString();

                    if (stressDict.containsKey(attempt)) {
                        return attempt;
                    }

                    sb.setCharAt(i, 'е');
                }
            }
        }
        return word;
    }

    public static String formRhythmicPattern(final String word,
                                             final Pair<Integer, Integer> rhythmicPattern) {
        // комменты расставил как догадался
        // rhythmicPatter[0] -- кол-во слогов
        char[] str = new char[rhythmicPattern.getLeft()];
        Arrays.fill(str, '0');

        int stressIndex = -1;

        for (int i = 0; i <= rhythmicPattern.getRight(); i++) {
            if (PhoneticsKnowledgeTools.VOWELS_SET.contains(word.charAt(i))) {
                stressIndex += 1;
            }
        }

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

        if (words.length > 0) {
            words[words.length - 1] = fixYo(words[words.length - 1]);

            // фиксим и второе с конца, может быть полезно для коротких слов
            if (words[words.length - 1].length() < MIN_SIZE_FOR_YOFICATION_SECOND_WORD && words.length > 1) {
                words[words.length - 2] = fixYo(words[words.length - 2]);
            }
        }

        String rhythmicPattern = "";

        for (int i = 0; i < words.length; i++) {

            Pair<Integer, Integer> curWordRhythmicPattern;

            if (stressDict.get(words[i]) != null) {
                final Pair<Integer, Integer> p = stressDict.get(words[i]);
                // todo: find a smarter solution
                final Integer stress = p.getRight();
                curWordRhythmicPattern = Pair.of(p.getLeft(), stress);
            } else {
                // нет такого слова в словаре
                // забиваем нулями
                curWordRhythmicPattern = Pair.of(countVowels(words[i]), -1); // это значит, что не знаем ударение
            }

            rhythmicPattern = rhythmicPattern + formRhythmicPattern(words[i], curWordRhythmicPattern);
        }
        return rhythmicPattern;
    }

    public static Map<String, Pair<Integer, Integer>> getCoreWordDict() {
        return stressDict;
    }
}
