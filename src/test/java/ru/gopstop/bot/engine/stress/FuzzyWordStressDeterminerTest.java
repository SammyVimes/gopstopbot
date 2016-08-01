package ru.gopstop.bot.engine.stress;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static ru.gopstop.bot.engine.stress.FuzzyWordStressDeterminer.getPatternFittedToSomeSimpleFoot;

/**
 * Created by aam on 01.08.16.
 */
public class FuzzyWordStressDeterminerTest extends TestCase {

    @Test
    public void testAFewCases() {

        Map<String, String> m = new HashMap<>();

        m.put("Буря мглою небо кроет,", "10101010");
        m.put("Как волнуются зяблики чорнай земою", "0010010010010");
        m.put("Варька, ну-ка выйди быстро надо говорить", "1010101010101");
        m.put("Идёт дождь", "010");

        for (final String key : m.keySet()) {
            System.out.println("Testing    \t" + key);
            System.out.println("Old version\t" + WordStressMap.getInstance().findRhythmicPattern(key));
            Assert.assertEquals(
                    m.get(key),
                    getPatternFittedToSomeSimpleFoot(key).getRight()
            );
            System.out.println("OK");
        }
    }
}