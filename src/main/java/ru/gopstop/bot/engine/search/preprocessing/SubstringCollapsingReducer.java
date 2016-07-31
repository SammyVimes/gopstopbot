package ru.gopstop.bot.engine.search.preprocessing;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by aam on 31.07.16.
 */
public class SubstringCollapsingReducer implements LastWordProcessor {

    private final Map<String, String> replacements =
            new HashMap<>();

    public SubstringCollapsingReducer() {
        replacements.put("сс", "с");
        replacements.put("цк", "ск");
        replacements.put("тся", "ца");
        replacements.put("ться", "ца");
        replacements.put("цц", "ц");
        replacements.put("кк", "к");
        replacements.put("тт", "т");
        replacements.put("е", "и");
        replacements.put("о", "а");
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
