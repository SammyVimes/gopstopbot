package ru.gopstop.bot.engine.search.preprocessing;

import com.google.common.base.Joiner;
import ru.gopstop.bot.engine.ExtraWordStressTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by aam on 31.07.16.
 */
public class BasicPreprocessor {

    private static final List<LastWordProcessor> PROCESSORS = new ArrayList<>();

    static {
        PROCESSORS.add(new SubstringCollapsingReducer());
        PROCESSORS.add(new ConsonantHack());
    }

    public static String postfix(final String line) {

        final String normalLine = line.trim().replaceAll("[^a-zA-Zа-яА-я ]", " ").toLowerCase();

        final String[] splitted = normalLine.replaceAll("\\s+", " ").split(" ");

        final int len = splitted.length - 1;

        if (len == -1) {
            return null;
        }

        // проставляем последнему слову ударения
        String lastStressed =
                ExtraWordStressTool.upperCaseStress(splitted[len]);

        // применяем всякие эвристики
        for (LastWordProcessor processor : PROCESSORS) {
            lastStressed = processor.process(lastStressed);
        }

        // приклеиваем последнее слово
        final List<String> res = new ArrayList<>(Arrays.asList(splitted));
        res.remove(len);
        res.add(lastStressed);

        final String fixedline = Joiner.on("").join(res);

        return new StringBuilder(fixedline).reverse().toString();
    }
}
