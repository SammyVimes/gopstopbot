package ru.gopstop.bot.telegram.controller;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.gopstop.bot.telegram.Constants;
import ru.gopstop.bot.telegram.TGBot;
import ru.gopstop.bot.telegram.internal.Emoji;
import ru.gopstop.bot.telegram.user.TGSession;

import java.util.Arrays;

/**
 * Created by Semyon on 30.07.2016.
 */
public class SettingsController extends Controller {

    public static final String ABOUT = Constants.ABOUT + " " + Emoji.INFORMATION_SOURCE.toString();
    public static final String TEST = "TEST " + Emoji.AIRPLANE.toString();

    public SettingsController(final TGBot bot) {
        super(bot);
        this.bot = bot;
    }

    @Override
    public String getKey() {
        return Constants.SETTINGS;
    }

    @Override
    public String getEntry() {
        return Constants.SETTINGS + Emoji.WRENCH.toString();
    }

    @Override
    public void handleMessage(final Message request, final TGSession session) throws TelegramApiException {
        final String text = request.getText();
        if (getEntry().equals(text)) {
            onMain(request, session);
            return;
        }

        if (text.equals(ABOUT)) {
            about(request, session);
            return;
        } else if (text.equals(BACK)) {
            back(request, session);
            return;
        } else if (text.equals(TEST)) {
            SendMessage msg = new SendMessage();
            msg.setChatId(request.getChatId().toString());
            msg.setText("Yawwww");
            bot.sendMessage(msg);
            return;
        }
    }

    private void onMain(final Message request, final TGSession session) throws TelegramApiException {
        final ReplyKeyboardMarkup replyKeyboardMarkup = buildKeyboard(Arrays.asList(ABOUT, TEST, BACK));
        final SendMessage msg = createMessageWithKeyboard(request.getChatId().toString(), request.getMessageId(), "Настройки", replyKeyboardMarkup);
        bot.sendMessage(msg);
    }

    private void about(final Message request, final TGSession session) throws TelegramApiException {
        final ReplyKeyboardMarkup replyKeyboardMarkup = buildKeyboard(Arrays.asList(TEST, BACK));
        final SendMessage msg = createMessageWithKeyboard(request.getChatId().toString(), request.getMessageId(),
                Constants.BOT_SELF_DESCRIPTION, replyKeyboardMarkup);
        bot.sendMessage(msg);
    }

}
