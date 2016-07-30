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
                CleverEngine.getRhyme("я сегодня ночевал с крастоа ыфваы ываы ыа ы ваы ва ыааф фа женщиной любимою");
        System.out.println("" + rhyme);
    }
}
