package ru.gopstop.bot.engine.search.preprocessing;

import ru.gopstop.bot.util.Transliteration;

import java.util.HashMap;
import java.util.Map;

import static ru.gopstop.bot.engine.tools.PhoneticsKnowledgeTools.*;

/**
 * Заменяем удвоенные согласные, созвучия
 * Можно дополнять до бесконечности
 * <p/>
 * Created by aam on 31.07.16.
 */
class SubstringCollapsingReducer implements LastWordProcessor {

    private final Map<String, String> replacements = new HashMap<>();

    SubstringCollapsingReducer() {

        for (final Character consonant : CONSONANTS_LIST) {
            replacements.put(consonant + "" + consonant, consonant + "");
        }

        // плохо работает
        replacements.put("Еверный$", "Ерено");
        replacements.put("Ыб$", "Ыв");
        replacements.put("вств", "ст");
        replacements.put("вс", "с");
        replacements.put("знь", "сь");
        replacements.put("цк", "ск");
        replacements.put("тся", "ца");
        replacements.put("ться", "ца");
        replacements.put("лн", "н");

        for (final Character hSound : HUSHING) {
            replacements.put(hSound + "(ь|ъ)$", hSound + "");
        }

        // боремся с парными гласными и мягким знаком

        for (final Character jotVChar : VOWELS_PAIRS.keySet()) {

            final String jotV = jotVChar + "";
            final String jotVPair = VOWELS_PAIRS.get(jotVChar) + "";

            for (final String anyV : VOWELS) {
                replacements.put(anyV + jotV, anyV + "й" + jotVPair);
                replacements.put(
                        anyV + jotV.toUpperCase(),
                        anyV + "й" + jotVPair.toUpperCase());
            }
            replacements.put("ь" + jotV, "й" + jotVPair);
            replacements.put("ь" + jotVPair, "й" + jotVPair);
            replacements.put("ь" + jotV.toUpperCase(), "й" + jotVPair.toUpperCase());
            replacements.put("ь" + jotVPair.toUpperCase(), "й" + jotVPair.toUpperCase());

            for (final Character any : ALL_SET) {
                // ударные йотированные можно заменить на парные
                // (иногда могут быть странные спецэффекты, посмотрим, как оно будет жить в продакшене)
                // upd: но на конце всё-таки нельзя
                replacements.put(jotV.toUpperCase() + any, jotVPair.toUpperCase() + any);
            }
        }

        replacements.put("ьИ", "йИ");
        replacements.put("ьи", "йи");

        // после того как заменили, где надо, ё на йо, можно вообще всё
        replacements.put("ё", "О");
        replacements.put("стн", "сн");
        replacements.put("пл$", "пал");
    }

    @Override
    public String process(final String lastWord) {

        String result = lastWord;

        for (Map.Entry<String, String> substring : replacements.entrySet()) {
            result = result.replaceAll(substring.getKey(), substring.getValue());
        }

        return Transliteration.fixEnglishWord(result);
    }
}
