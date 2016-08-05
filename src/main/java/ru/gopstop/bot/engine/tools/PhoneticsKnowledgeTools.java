package ru.gopstop.bot.engine.tools;


import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by aam on 01.08.16.
 */
public final class PhoneticsKnowledgeTools {

    public static final String[] VOWELS =
            new String[]{"а", "ы", "о", "э", "е", "я", "и", "ю", "ё", "у"};

    public static final Map<Character, Character> CONSONANTS_PAIRS =
            Arrays
                    .stream("кг,шж,сз,фв,пб,тд".split(","))
                    .collect(Collectors.toMap(s -> s.charAt(1), s -> s.charAt(0)));

    public static final Map<Character, Character> VOWELS_PAIRS =
            Arrays
                    .stream("яа,ёо,юу,еэ".split(","))
                    .collect(Collectors.toMap(s -> s.charAt(1), s -> s.charAt(0)));

    public static final Set<Character> VOWELS_SET =
            Arrays
                    .stream(VOWELS)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toSet());

    public static final String CONSONANT_PATTERN = "й|ц|к|н|г|ш|щ|з|х|ъ|ф|в|п|р|л|д|ж|ч|с|м|т|ь|б";

    public static final List<Character> CONSONANTS_LIST =
            Arrays
                    .stream(CONSONANT_PATTERN.split("|"))
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList());

    public static final Set<Character> CONSONANTS_SET = new HashSet<>(CONSONANTS_LIST);

    private PhoneticsKnowledgeTools() {

    }
}
