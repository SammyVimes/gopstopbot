package ru.gopstop.bot.engine.emphasis;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.gopstop.bot.engine.EmphasisMap;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by n.pritykovskaya on 30.07.16.
 */
public class TestEmphasis {


    @Test
    @Ignore
    public void testEmphasisDict() throws IOException {
//        EmphasisMap.setPath("./data/emphasis.txt");
//        EmphasisMap emphasisMap = new EmphasisMap();

        EmphasisMap.getInstance();

//
//        ArrayList<String> rhythmicPatterns = emphasisMap.findRhythmicPattern("Буря мглою небо кроет,");
//        for (int i = 0; i < rhythmicPatterns.size(); i++) {
//            System.out.println(rhythmicPatterns.get(i));
//        }

        String rhythmicPattern = EmphasisMap.getInstance().findRhythmicPattern("Буря мглою небо кроет,");
        System.out.println(rhythmicPattern);
    }
}
