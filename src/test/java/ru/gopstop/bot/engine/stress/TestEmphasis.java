package ru.gopstop.bot.engine.stress;

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
        WordStressMap.getInstance();
        String rhythmicPattern =
                WordStressMap.getInstance().findRhythmicPattern("Буря мглою небо кроет,");
        System.out.println(rhythmicPattern);
    }
}
