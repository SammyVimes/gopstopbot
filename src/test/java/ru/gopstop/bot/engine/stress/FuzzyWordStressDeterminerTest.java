package ru.gopstop.bot.engine.stress;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aam on 01.08.16.
 */
public class FuzzyWordStressDeterminerTest extends TestCase {

    @Test
    public void testAFewCases() {
        WordStressMap.getInstance();
        String rhythmicPattern =
                FuzzyWordStressDeterminer
                        .getStressedWords("Буря мглою небо кроет,");
        System.out.println(rhythmicPattern);
    }
}