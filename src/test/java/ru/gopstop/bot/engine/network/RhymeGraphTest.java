package ru.gopstop.bot.engine.network;

import junit.framework.TestCase;

import java.util.Collection;

/**
 * Created: aam
 * Date:    20.08.16
 */
public class RhymeGraphTest extends TestCase {

    public void testCloseRhymes() {

        final String line = "что ж ты фраер сдал скачу";
        final RhymeGraph rhymeGraph = RhymeGraph.getInstance();
        final Collection<String> rhymePostfices = rhymeGraph.getCloseRhymes(line);

        for (final String rhyme : rhymePostfices) {
            System.out.println(rhyme);
        }
    }
}
