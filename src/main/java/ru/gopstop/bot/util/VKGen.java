package ru.gopstop.bot.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.FileUtils;

import java.net.URLEncoder;

/**
 * Created by aam on 03.08.16.
 */
public final class VKGen {

    //todo: http://vkontakte.ru/share.php?url=&title=&description=&image=&noparse=true

    private static final Logger LOGGER = LogManager.getLogger(FileUtils.class);

    public static String generate(final String userInpuit,
                                  final String botAnswer,
                                  final String songTitle) {

        final String txt = "— " + userInpuit + "\n" + "— " + botAnswer;

        final String vkLink =
                "http://vkontakte.ru/share.php?title="
                        + URLEncoder.encode("Гоп-стоп-бот")
                        + "&description="
                        + URLEncoder.encode(txt)
                        + "&noparse=true"
                        + "&url=http://telegram.me/gop_stop_bot";

        LOGGER.debug("POST GENERATED: " + vkLink);

        return vkLink;
    }

    private VKGen() {
    }
}
