package ru.gopstop.bot.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.FileUtils;

import java.net.URLEncoder;

/**
 * Created by Semyon on 31.07.2016.
 */
public final class TweetGen {

    private static final Logger LOGGER = LogManager.getLogger(TweetGen.class);

    public static String generate(final String userInpuit, final String botAnswer, final String songTitle) {

        final String tweet = "— " + userInpuit + "\n"
                + "— " + botAnswer + "\n"
                + "@gop_stop_bot";

        final String tweetLink =
                "http://twitter.com/share?text=" + URLEncoder.encode(tweet)
                        + "&url=http://telegram.me/gop_stop_bot";

        LOGGER.debug("TWEET GENERATED: " + tweetLink);

        return tweetLink;
    }

    private TweetGen() {
    }
}
