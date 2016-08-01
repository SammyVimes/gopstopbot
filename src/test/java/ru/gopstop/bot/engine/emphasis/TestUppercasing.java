package ru.gopstop.bot.engine.emphasis;

import junit.framework.TestCase;
import ru.gopstop.bot.engine.stress.ExtraWordStressTool;

/**
 * Created by aam on 31.07.16.
 */
public class TestUppercasing extends TestCase {

    public void test() {

        final String res =

                ExtraWordStressTool.upperCaseByPattern("подушка", "010");

        System.out.println(res);

    }

}
