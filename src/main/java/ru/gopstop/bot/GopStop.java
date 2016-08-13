package ru.gopstop.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import ru.gopstop.bot.cache.SessionCache;
import ru.gopstop.bot.engine.search.LinesIndexer;
import ru.gopstop.bot.engine.stress.WordStressMap;
import ru.gopstop.bot.telegram.TGBot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by Semyon on 30.07.2016.
 */
public final class GopStop {

    private static final Logger LOGGER = LogManager.getLogger(GopStop.class);

    private GopStop() {

        LOGGER.info("START Стартуем! Сегодня мы с тобой стартуем.");

        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        LOGGER.info("Init singletons");

        SessionCache.getInstance();
        LinesIndexer.getInstance();
        WordStressMap.getInstance();

        LOGGER.info("Singletons inited");

        try {
            telegramBotsApi.registerBot(new TGBot());
        } catch (final TelegramApiException e) {
            LOGGER.error("Error while registering bot: " + e.getMessage(), e);
        }
    }

    private void start() {
        LOGGER.trace("Есть чо?");
    }

    static class WeirdExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(final Thread arg0, final Throwable arg1) {
            System.out.println("[DEFAULT EXCEPTION HANDLER] Caught some exception " + arg0 + " " + arg1 + " " + arg1.getMessage());
            System.err.println("[DEFAULT EXCEPTION HANDLER] Caught some exception " + arg0 + " " + arg1 + " " + arg1.getMessage());
            arg1.printStackTrace();
            arg1.printStackTrace(System.err);
            System.exit(-1);
        }
    }

    public static void main(final String[] args) {

        try {
            final File fileErr = new File("err.log");
            final PrintStream printStreamErr = new PrintStream(new FileOutputStream(fileErr));
            System.setErr(printStreamErr);


            final File file = new File("out.log");
            final PrintStream printStream = new PrintStream(new FileOutputStream(file));
            System.setErr(printStream);
            LOGGER.info("Streams reset");

        } catch (final FileNotFoundException fnfe) {
            LOGGER.error("Can't reseet System printstreams", fnfe);
            fnfe.printStackTrace();
            fnfe.printStackTrace(System.err);
        }

        Thread.setDefaultUncaughtExceptionHandler(new WeirdExceptionHandler());
        new GopStop().start();
    }

}
