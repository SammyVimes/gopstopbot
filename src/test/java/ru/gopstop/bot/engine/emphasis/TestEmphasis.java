package ru.gopstop.bot.engine.emphasis;

import org.junit.Ignore;
import org.junit.Test;
import ru.gopstop.bot.engine.stress.WordStressMap;

import java.io.IOException;

/**
 * Created by n.pritykovskaya on 30.07.16.
 */
public class TestEmphasis {


    @Test
    @Ignore
    public void testEmphasisDict() throws IOException {
//        WordStressMap.setPath("./data/emphasis.txt");
//        WordStressMap emphasisMap = new WordStressMap();

        WordStressMap.getInstance();

//
//        ArrayList<String> rhythmicPatterns = emphasisMap.findRhythmicPattern("Буря мглою небо кроет,");
//        for (int i = 0; i < rhythmicPatterns.size(); i++) {
//            System.out.println(rhythmicPatterns.get(i));
//        }

        String rhythmicPattern = WordStressMap.getInstance().findRhythmicPattern("Буря мглою небо кроет,");
        System.out.println(rhythmicPattern);
    }
}
