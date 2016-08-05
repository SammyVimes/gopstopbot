package ru.gopstop.bot.engine.search.preprocessing;

import java.util.HashMap;
import java.util.Map;

import static ru.gopstop.bot.engine.tools.PhoneticsKnowledgeTools.VOWELS;
import static ru.gopstop.bot.engine.tools.PhoneticsKnowledgeTools.VOWELS_PAIRS;

/**
 * Заменяем удвоенные согласные, созвучия
 * Можно дополнять до бесконечности
 * <p>
 * Created by aam on 31.07.16.
 */
class SubstringCollapsingReducer implements LastWordProcessor {

    private final Map<String, String> replacements = new HashMap<>();

    SubstringCollapsingReducer() {
        replacements.put("сс", "с");
        replacements.put("цк", "ск");
        replacements.put("тся", "ца");
        replacements.put("ться", "ца");
        replacements.put("цц", "ц");
        replacements.put("кк", "к");
        replacements.put("тт", "т");

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
        }
        replacements.put("ьИ", "йИ");
        replacements.put("ьи", "йи");
    }

    @Override
    public String process(final String lastWord) {

        String result = lastWord;

        for (Map.Entry<String, String> substring : replacements.entrySet()) {
            result = result.replaceAll(substring.getKey(), substring.getValue());
        }

        return result;
    }
}