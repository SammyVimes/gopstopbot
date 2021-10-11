package ru.gopstop.bot.telegram.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.ActionType;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.gopstop.bot.FileUtils;
import ru.gopstop.bot.engine.CleverEngine;
import ru.gopstop.bot.engine.entities.GopSong;
import ru.gopstop.bot.engine.entities.Rhyme;
import ru.gopstop.bot.telegram.Constants;
import ru.gopstop.bot.telegram.TGBot;
import ru.gopstop.bot.telegram.internal.Emoji;
import ru.gopstop.bot.telegram.user.TGSession;

import java.util.Collections;

/**
 * Created by Semyon on 30.07.2016.
 */
public class RhymingController extends BaseMuzisController {

    private static final int TOP_SONGS_COUNT = 6;

    private static final Logger LOGGER = LogManager.getLogger(RhymingController.class);

    public RhymingController(final TGBot bot) {
        super(bot);
    }

    @Override
    public String getKey() {
        return Constants.ControllersTags.RHYMES.getName();
    }

    @Override
    public String getEntry() {
        return Constants.RHYMES_PLS + " " + Emoji.FACE_THROWING_A_KISS.toString();
    }

    @Override
    public void handleMessage(final Message request, final TGSession session) throws TelegramApiException {

        final String text = request.getText();

        if (getEntry().equals(text)) {
            onMain(request, session);
            return;
        }

        if (text.equals(BACK)) {
            back(request, session);
            return;
        }

        sendAction(request.getChatId().toString(), ActionType.TYPING);

        final Rhyme rhyme = CleverEngine.getRhyme(text);

        LOGGER.info("RHYME_REPORT\t"
                + session.getChatId() + "\t"
                + session.getUser().getId() + "\t"
                + session.getUser().getUserName() + "\t"
                + session.getUser().getFirstName() + "\t"
                + session.getUser().getLastName() + "\t"
                + text.replaceAll("\t", " ") + "\t"
                + (rhyme == null ? "NO_RHYME" : rhyme.getRhyme()) + "\t"
                + (rhyme == null ? "NO_SONG" : rhyme.getGopSong().getName()) + "\t"
                + (rhyme == null ? "NO_SONG" : rhyme.getGopSong().getAuthor()));

        LOGGER.info("REPORT_CHAT_ID " + FileUtils.REPORT_CHAT_ID);

        if (rhyme != null) {
            onRhymeFound(request, session, rhyme);
        } else {
            sendMessage(request.getChatId().toString(), "Ничего не нашли");
        }
    }

    private void onRhymeFound(final Message request, final TGSession session, final Rhyme rhyme) throws TelegramApiException {

        final GopSong gopSong = rhyme.getGopSong();

        sendHtmlMessage(
                request.getChatId().toString(),
                String.format(
                        "<b>%s\n%s</b>\n(%s — %s)",
                        request.getText(), rhyme.getRhyme(),
                        gopSong.getAuthor(), gopSong.getName()));
    }

    private void onMain(final Message request, final TGSession session) throws TelegramApiException {

        final ReplyKeyboardMarkup replyKeyboardMarkup =
                buildKeyboard(Collections.singletonList(BACK));

        final SendMessage msg =
                createMessageWithKeyboard(
                        request.getChatId().toString(),
                        request.getMessageId(),
                        "Сегодня мы с тобой рифмуем... \nНапиши что-нибудь, по-братски прошу.",
                        replyKeyboardMarkup);
        getBot().sendMessage(msg);
    }
}
