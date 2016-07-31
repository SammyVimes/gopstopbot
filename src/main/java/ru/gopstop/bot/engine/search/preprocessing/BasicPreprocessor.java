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

    public static String postfix(String line) {

        String normalLine = line.trim().replaceAll("[^a-zA-Zа-яА-я ]", " ").toLowerCase();

//        String[] splitted = normalLine.replaceAll("\\s+", " ").split(" ");

//        String lastStressed = ExtraWordStressTool.upperCaseStress(splitted[splitted.length - 1]);
//
//        final List<String> res = new ArrayList<>(Arrays.asList(splitted));
//        res.remove(splitted.length - 1);
//
//        res.add(lastStressed);
//
//        String fixedline = Joiner.on(" ").join(res);

        String fixedline = normalLine;

        String processedLine =
                new StringBuilder(fixedline).reverse().toString();
        //todo augmentation
        return processedLine;
    }
}
