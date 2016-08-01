package ru.gopstop.bot.engine.search.preprocessing;

import com.google.common.base.Joiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.engine.stress.ExtraWordStressTool;
import ru.gopstop.bot.util.SymbolsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by aam on 31.07.16.
 */
public final class BasicPreprocessor {

    private static final Logger LOGGER = LogManager.getLogger(BasicPreprocessor.class);

    private static final List<LastWordProcessor> PROCESSORS = new ArrayList<>();

    static {
        PROCESSORS.add(new SubstringCollapsingReducer());
        PROCESSORS.add(new ConsonantHack());
    }

    public static String postfix(final String line, final boolean logStuff) {

        final String normalLine =
                SymbolsUtils.replaceUseless(line.trim(), " ").toLowerCase();

        if (logStuff) {
            LOGGER.info("REQ2NORM\t" + line.replaceAll("\\t", " ") + "\t" + normalLine);
        }

        final String[] splitted = normalLine.replaceAll("\\s+", " ").split(" ");

        final int len = splitted.length - 1;

        if (len == -1) {
            return null;
        }

        // проставляем последнему слову ударения
        String lastStressed =
                ExtraWordStressTool.upperCaseStress(splitted[len]);

        if (logStuff) {
            LOGGER.info("WORD2STRESSED\t" + splitted[len] + "\t" + lastStressed);
        }

        // применяем всякие эвристики
        for (final LastWordProcessor processor : PROCESSORS) {

            lastStressed = processor.process(lastStressed);

            if (logStuff) {
                LOGGER.info("PROCESSED\t" + processor.getClass()
                        + "\t" + splitted[len]
                        + "\t" + lastStressed);
            }
        }

        // приклеиваем последнее слово
        final List<String> res = new ArrayList<>(Arrays.asList(splitted));
        res.remove(len);
        res.add(lastStressed);

        final String fixedline = Joiner.on("").join(res);

        if (logStuff) {
            LOGGER.info("FIXEDLINE\t" + res + "\t" + fixedline);
        }

        return new StringBuilder(fixedline).reverse().toString();
    }

    private BasicPreprocessor() {

    }
}
