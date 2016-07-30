package ru.gopstop.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import ru.gopstop.bot.telegram.TGBot;

/**
 * Created by Semyon on 30.07.2016.
 */
public class GopStop {

    private final Logger LOGGER = LogManager.getLogger(GopStop.class);

    private GopStop() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new TGBot());
        } catch (TelegramApiException e) {
            LOGGER.error("Error while registering bot: " + e.getMessage(), e);
        }
    }

    private void start() {
        LOGGER.trace("Есть чо?");
    }

    public static void main(String[] args) {
        new GopStop().start();
    }

}
