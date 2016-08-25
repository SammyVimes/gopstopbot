package ru.gopstop.bot.telegram.controller;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.objects.Message;
import ru.gopstop.bot.telegram.Constants;
import ru.gopstop.bot.telegram.TGBot;
import ru.gopstop.bot.telegram.internal.Emoji;
import ru.gopstop.bot.telegram.user.TGSession;

/**
 * Created by Semyon on 30.07.2016.
 */
public class SettingsController extends Controller {

    public static final String ABOUT = Constants.ControllersTags.ABOUT.getName() + " " + Emoji.INFORMATION_SOURCE.toString();

    public SettingsController(final TGBot bot) {
        super(bot);
    }

    @Override
    public String getKey() {
        return Constants.ControllersTags.ABOUT.getName();
    }

    @Override
    public boolean rememberMe() {
        return false;
    }

    @Override
    public String getEntry() {
        return ABOUT;
    }

    @Override
    public void handleMessage(final Message request, final TGSession session) throws TelegramApiException {
        final String text = request.getText();

        if (getEntry().equals(text)) {
            sendMessage(request.getChatId().toString(), Constants.BOT_SELF_DESCRIPTION);
            return;
        }
    }

}