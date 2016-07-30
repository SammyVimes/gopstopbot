package ru.gopstop.bot.telegram.controller;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.gopstop.bot.telegram.TGBot;
import ru.gopstop.bot.telegram.internal.Emoji;
import ru.gopstop.bot.telegram.user.TGSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Semyon on 30.07.2016.
 *
 *
 * Базовый класс контроллера пользовательских запросов
 *
 */
public abstract class Controller {

    protected TGBot bot;

    public static final String BACK = "Назад " + Emoji.BACK_WITH_LEFTWARDS_ARROW_ABOVE.toString();

    public Controller(final TGBot bot) {
        this.bot = bot;
    }

    /**
     * Ключ контроллера в мапе (кладётся в сессию как lastController)
     * @return
     */
    public abstract String getKey();

    /**
     * Сообщение для входа в этот контроллер (e.g. "Настройки")
     * @return
     */
    public abstract String getEntry();

    /**
     * Обработка входящего сообщения
     * @param request
     * @throws TelegramApiException
     */
    public abstract void handleMessage(final Message request, final TGSession session) throws TelegramApiException;

    /**
     * Создание клавиатуры
     * клавиатура группируется по два сообщения в строке
     * @param messages
     * @return
     */
    public ReplyKeyboardMarkup buildKeyboard(final List<String> messages) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = null;
        for (int i = 0; i < messages.size(); i++) {
            // группируем кнопки по две
            if (i % 2 == 0 || row == null) {
                row = new KeyboardRow();
                keyboard.add(row);
            }
            final String entry = messages.get(i);
            row.add(entry);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    /**
     * Отправка сообщения с клавиатурой и текстом
     * сообщение отправляется с прикреплённым запросом пользователя
     * @param chatId
     * @param messageId
     * @param text
     * @param replyKeyboardMarkup
     * @return
     */
    public SendMessage createMessageWithKeyboard(final String chatId,
                                                  final Integer messageId,
                                                  final String text,
                                                  final ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyToMessageId(messageId);
        sendMessage.setText(text);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    /**
     * Обработка нажатия на кнопку "назад"
     * Возвращение в главное меню
     * @param request
     * @param session
     * @throws TelegramApiException
     */
    protected void back(final Message request, final TGSession session) throws TelegramApiException {
        session.setLastController(null);
        bot.showMainMenu(request, session);
    }

}
