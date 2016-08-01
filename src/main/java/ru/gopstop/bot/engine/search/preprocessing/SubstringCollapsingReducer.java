package ru.gopstop.bot.engine.search.preprocessing;

import java.util.HashMap;
import java.util.Map;

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