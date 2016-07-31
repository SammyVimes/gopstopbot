package ru.gopstop.bot.engine.search.preprocessing;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Semyon on 31.07.2016.
 */
public class ConsonantHackTest {

    @Test
    public void testProcess() throws Exception {
        ConsonantHack consonantHack = new ConsonantHack();
        Assert.assertEquals("суккУп", consonantHack.process("суккУб"));
        Assert.assertEquals("трубА", consonantHack.process("трубА"));
        Assert.assertEquals("поЕхафший", consonantHack.process("поЕхавший"));
        Assert.assertEquals("фьЮк", consonantHack.process("вьЮг"));
    }

}