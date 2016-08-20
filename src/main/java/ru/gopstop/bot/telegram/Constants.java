package ru.gopstop.bot.telegram;

/**
 * Created by Semyon on 30.07.2016.
 */
public final class Constants {

    public static final boolean SKIP_MUSIC = true;

    public enum ControllersTags {
        SONGS("Песни"),
        RHYMES("Рифмы"),
        ABOUT("О боте");

        private final String name;

        ControllersTags(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


    public static final String RHYMES_PLS = "Рифмуй!";

    public static final int THREAD_POOL_SIZE = 15;

    public static final String BOT_SELF_DESCRIPTION =
            "«Мне говорят, что Окна ТАСС моих стихов полезнее...»\n"
                    + "Like, share, re-post or stay with us,\n"
                    + "Ведь и шансон — поэзия.\n\n"
            + "[Что здесь происходит?](https://github.com/SammyVimes/gopstopbot/blob/master/README.md)\n"
            + "Команда:\n"
            + "Антон @AntonAlexeyev\n"
            + "Наташа @pritykovskaya\n"
            + "Семён @SemyonDanilov\n";

    private Constants() {

    }
}
