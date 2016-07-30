package ru.gopstop.bot.engine;

import java.io.*;
import java.util.*;

/**
 * Created by n.pritykovskaya on 30.07.16.
 */
public class EmphasisMap {

    public static String[] VOWELS = new String[]{"а", "ы", "о", "э", "е", "я", "и", "ю", "ё", "у"};

    public static void setPath(String path) {
        EmphasisMap.path = path;
    }

    public static String path = "./data/emphasis.txt";
    public static HashMap<String, ArrayList<int[]>> emphasisDict =
            new HashMap<String, ArrayList<int[]>>(1540873);

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

    private boolean alreadyIn(int [] curRhythmicPattern, ArrayList<int[]> prevRhythmicPatterns) {
        for (int i = 0; i < prevRhythmicPatterns.size(); i++){
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

    public EmphasisMap() throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            int cntLine = 0;
            while (line != null) {
                parseLine(line);
                cntLine++;
                if (cntLine % 10000 == 0) {
                    System.out.println(cntLine);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getSize() {
        return emphasisDict.size();
    }

    private String[] processPoemLine(String poemLine) {
        return poemLine.trim().replaceAll("[^a-zA-Zа-яА-я ]", "").toLowerCase().split(" ");

    }

    private String formRhythmicPattern(int[] rhythmicPattern) {
        char[] str = new char[rhythmicPattern[0]];
        Arrays.fill(str, '0');
        str[rhythmicPattern[1]] = '1';
        return new String(str);
    }

    public String findRhythmicPattern(String poemLine) {
        String[] words = processPoemLine(poemLine);
        String rhythmicPattern = "";
        for (int i = 0; i < words.length; i++) {
            int[] curWordRhythmicPattern = emphasisDict.get(words[i]).get(0);
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
