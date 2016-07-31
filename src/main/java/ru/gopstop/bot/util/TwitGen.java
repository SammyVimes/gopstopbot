package ru.gopstop.bot.util;

import java.net.URLEncoder;

/**
 * Created by Semyon on 31.07.2016.
 */
public class TwitGen {

    public static final String generate(final String userInpuit, final String botAnswer, final String songTitle) {
        String twit = "— " + userInpuit + "\n";
        twit += "— " + botAnswer + "\n";
        twit += "gop_stop_bot@telegram ";

        String twitLink = "http://twitter.com/share?text=" + URLEncoder.encode(twit) + "&url=http://telegram.me/gop_stop_bot";

        return twitLink;
    }

}
