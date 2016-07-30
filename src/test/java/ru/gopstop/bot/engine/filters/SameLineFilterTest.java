package ru.gopstop.bot.engine.filters;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Ignore;
import ru.gopstop.bot.engine.search.FoundGopSong;

/**
 * Created by aam on 31.07.16.
 */
public class SameLineFilterTest extends TestCase {

    @Ignore
    public void testCleverFilter() {

        Assert.assertFalse(
                SameLineFilter.filter("я сегодня ночевал с женщиной",
                        new FoundGopSong(null, "я сегодня ночевал с женщиной любимою", 0.0)));

    }
}