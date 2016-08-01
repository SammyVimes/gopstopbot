package ru.gopstop.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import ru.gopstop.bot.engine.WordStressMap;
import ru.gopstop.bot.engine.search.LinesIndexer;
import ru.gopstop.bot.telegram.TGBot;

/**
 * Created by Semyon on 30.07.2016.
 */
public final class GopStop {

    private static final Logger LOGGER = LogManager.getLogger(GopStop.class);

    private GopStop() {
        LOGGER.info("Стартуем!");
        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        LinesIndexer.getInstance();
        WordStressMap.getInstance();

        try {
            telegramBotsApi.registerBot(new TGBot());
        } catch (final TelegramApiException e) {
            LOGGER.error("Error while registering bot: " + e.getMessage(), e);
        }
    }

    private void start() {
        LOGGER.trace("Есть чо?");
    }

    public static void main(final String[] args) {
        new GopStop().start();
    }

}
