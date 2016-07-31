package ru.gopstop.bot.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by n.pritykovskaya on 30.07.16.
 */
public class EmphasisMap {

    private final static Logger LOGGER = LogManager.getLogger(EmphasisMap.class);

    public static String[] VOWELS = new String[]{"а", "ы", "о", "э", "е", "я", "и", "ю", "ё", "у"};

    public static void setPath(String path) {
        EmphasisMap.path = path;
    }

    public static String path = "./data/emphasis.txt";
    public static HashMap<String, ArrayList<int[]>> emphasisDict =
            new HashMap<String, ArrayList<int[]>>(1600000);

    public static final String SER_PATH = "stress_map.bin";

    private static EmphasisMap INSTANCE;

    public static EmphasisMap getInstance() {
        return INSTANCE;
    }

    static {
        try {
            INSTANCE = new EmphasisMap();
        } catch (IOException ioe) {
            LOGGER.error("emp map down", ioe);
            throw new RuntimeException(ioe);
        }
    }

    private int countVowels(String word) {
        return word.length() - word.replaceAll("а|ы|е|ё|и|у|о|э|я|ю", "").length();
    }

    private int emphasisPosition(String word) {
        word = word.replaceAll("й|ц|к|н|г|ш|щ|з|х|ъ|ф|в|п|р|л|д|ж|ч|с|м|т|ь|б|-", "");
        if (word.contains("'")) {
            return word.indexOf("'") - 1;
        } else if (word.contains("ё")) {
            return word.indexOf("ё");
        } else {
            return 0;
        }
    }

    private boolean alreadyIn(int[] curRhythmicPattern, ArrayList<int[]> prevRhythmicPatterns) {
        for (int i = 0; i < prevRhythmicPatterns.size(); i++) {
            if (Arrays.equals(curRhythmicPattern, prevRhythmicPatterns.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void parseLine(String line) {
        String[] parts = line.split("#");
        String[] words = parts[1].split(",");

        for (int i = 0; i < words.length; i++) {
            String wordNoEmphasis = words[i].replace("'", "").replace("`", "");
            int vowelsNumb = countVowels(wordNoEmphasis);
            if (vowelsNumb > 0) {
                int emphasisPos = emphasisPosition(words[i].replace("`", ""));
                int[] rhythmicPattern = new int[2];
                rhythmicPattern[0] = vowelsNumb;
                rhythmicPattern[1] = emphasisPos;

                if (emphasisDict.get(wordNoEmphasis) == null) {
                    ArrayList<int[]> newArr = new ArrayList<int[]>(1);
                    newArr.add(rhythmicPattern);
                    emphasisDict.put(wordNoEmphasis, newArr);
                } else {
                    if (!alreadyIn(rhythmicPattern, emphasisDict.get(wordNoEmphasis)))
                        emphasisDict.get(wordNoEmphasis).add(rhythmicPattern);
                }
            }
        }
    }

    private String arrToStr(Object arrObj) {
        ArrayList<int[]> arr = (ArrayList<int[]>) arrObj;
        String strs = "";
        for (int i = 0; i < arr.size(); i++) {
            int[] curArr = arr.get(i);
            String str = "";
            for (int j = 0; j < curArr.length; j++) {
                str += curArr[j];
            }
            strs = strs + "|" + str;
        }
        return strs;
    }

    private EmphasisMap() throws IOException {

        HashMap<String, ArrayList<int[]>> map = null;

        try {
            FileInputStream fis = new FileInputStream(SER_PATH);
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (HashMap) ois.readObject();
            emphasisDict = map;
            ois.close();
            fis.close();
        } catch (Exception e) {

            LOGGER.warn("COULD NOT DESER MAP");

            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String line = br.readLine();
                int cntLine = 0;
                while (line != null) {
                    parseLine(line);
                    cntLine++;
                    if (cntLine % 10000 == 0) {
                        LOGGER.info("Lines of wod stress dict indexed: " + cntLine);
                    }
                    line = br.readLine();
                }

                LOGGER.info("Serialization...");

                try {
                    FileOutputStream fos =
                            new FileOutputStream(SER_PATH);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(emphasisDict);
                    oos.close();
                    fos.close();
                    LOGGER.info("Serialized HashMap data is saved in " + SER_PATH);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                LOGGER.info("Serizalization done");

            } catch (IOException ie) {
                ie.printStackTrace();
            }

        }

    }

    public int getSize() {
        return emphasisDict.size();
    }

    private String[] processPoemLine(String poemLine) {
        return poemLine.trim().replaceAll("[^a-zA-Zа-яА-я ]", "").toLowerCase().split(" ");

    }

    private String formRhythmicPattern(int[] rhythmicPattern) {
        // комменты расставил как догадался
        // rhythmicPatter[0] -- кол-во слогов
        char[] str = new char[rhythmicPattern[0]];
        Arrays.fill(str, '0');
        // rhythmicPatter[1] -- на каком слоге ударение
        final int emphasisIndex = rhythmicPattern[1];
        if (emphasisIndex != -1) {
            str[emphasisIndex] = '1';
        }
        return new String(str);
    }

    public String findRhythmicPattern(String poemLine) {
        String[] words = processPoemLine(poemLine);
        String rhythmicPattern = "";

        for (int i = 0; i < words.length; i++) {
            int[] curWordRhythmicPattern;
            if (emphasisDict.get(words[i]) != null) {
                curWordRhythmicPattern = emphasisDict.get(words[i]).get(0);
            } else {
                // нет такого слова в словаре
                // забиваем нулями
                curWordRhythmicPattern = new int[2];
                curWordRhythmicPattern[0] = countVowels(words[i]);
                curWordRhythmicPattern[1] = -1; // это значит, что не знаем ударение
            }
            rhythmicPattern = rhythmicPattern + formRhythmicPattern(curWordRhythmicPattern);

        }
        return rhythmicPattern;
    }

//    public ArrayList<String> findRhythmicPattern(String poemLine) {
//        String[] words = processPoemLine(poemLine);
//        ArrayList<String> rhythmicPatterns = new ArrayList<String>(1);
//
//        for (int i = 0; i < words.length; i++) {
//            int curLengthRhythmicPatterns = rhythmicPatterns.size();
//
//            if (emphasisDict.get(words[i]) != null) {
//
//                ArrayList<int[]> curWordRhythmicPatterns = emphasisDict.get(words[i]);
//                for (int j = 0; j < curLengthRhythmicPatterns; j++) {
//                    rhythmicPatterns.set(j, rhythmicPatterns.get(j) + formRhythmicPattern(curWordRhythmicPatterns.get(0)));
//                    if (curWordRhythmicPatterns.size() >= 1) {
//                        for (int k = 1; k < curWordRhythmicPatterns.size(); k++) {
//                            rhythmicPatterns.add(rhythmicPatterns.get(j) + formRhythmicPattern(curWordRhythmicPatterns.get(k)));
//                        }
//                    }
//                }
//            } else {
//                char[] str = new char[countVowels(words[i])];
//                Arrays.fill(str, '?');
//                for (int j = 0; j < curLengthRhythmicPatterns; j++) {
//                    rhythmicPatterns.set(j, rhythmicPatterns.get(j) + new String(str));
//                }
//            }
//        }
//
//        return rhythmicPatterns;
//
//    }
}
