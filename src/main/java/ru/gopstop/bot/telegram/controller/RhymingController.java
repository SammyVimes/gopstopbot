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
public class RhymingController extends Controller {

    public RhymingController(final TGBot bot) {
        super(bot);
    }

    @Override
    public String getKey() {
        return Constants.RHYMES;
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
        String reply = "wtf mawwwn";
        if ("Сэмэн, засунь ей под ребро".equalsIgnoreCase(text)) {
            reply = "Купите камеру GoPro";
        } else if ("Тучки небесные будут наказаны".equalsIgnoreCase(text)) {
            reply = "Степью лазурною секьюрити выйдите!";
        } else if (text.equals(BACK)) {
            back(request, session);
            return;
        }

        SendMessage msg = new SendMessage();
        msg.setChatId(request.getChatId().toString());
        msg.setText(reply);
        bot.sendMessage(msg);
    }

    private void onMain(final Message request, final TGSession session) throws TelegramApiException {
        final ReplyKeyboardMarkup replyKeyboardMarkup = buildKeyboard(Arrays.asList(BACK));
        final SendMessage msg = createMessageWithKeyboard(request.getChatId().toString(), request.getMessageId(), "Сегодня мы с тобой рифмуем", replyKeyboardMarkup);
        bot.sendMessage(msg);
    }

}
