package ru.gopstop.bot.engine.search;

import junit.framework.TestCase;
import org.junit.Ignore;
import ru.gopstop.bot.engine.CleverEngine;
import ru.gopstop.bot.engine.entities.Rhyme;

/**
 * Created by aam on 30.07.16.
 */
public class IndexTester extends TestCase {

    @Ignore
    public void testCleverEngine() {
        final Rhyme rhyme =
                CleverEngine.getRhyme("как нынче дышится вольготно чистый мёд");
        System.out.println("" + rhyme.getRhyme());
        System.out.println(rhyme.getGopSong());
    }
}
