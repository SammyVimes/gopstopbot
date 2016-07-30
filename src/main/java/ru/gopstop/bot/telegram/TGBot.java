package ru.gopstop.bot.telegram;

import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import ru.gopstop.bot.telegram.controller.Controller;
import ru.gopstop.bot.telegram.controller.RhymingController;
import ru.gopstop.bot.telegram.controller.SettingsController;
import ru.gopstop.bot.telegram.user.TGSession;
import ru.gopstop.bot.telegram.user.TGSessionKey;

import java.io.IOException;
import java.util.*;

/**
 * Created by Semyon on 30.07.2016.
 *
 * Телеграм бот
 *
 */
public class TGBot extends TelegramLongPollingBot {

    private final Logger LOGGER = LogManager.getLogger(TGBot.class);

    private Map<TGSessionKey, TGSession> sessionMap = new HashMap<>();

    private Map<String, Controller> controllerMap = new HashMap<>();

    private List<Controller> mainControllers = new ArrayList<>();

    private String token = "";

    public TGBot() {
        final RhymingController rhymingController = new RhymingController(this);
        controllerMap.put(rhymingController.getKey(), rhymingController);
        mainControllers.add(rhymingController);
        final SettingsController settingsController = new SettingsController(this);
        controllerMap.put(settingsController.getKey(), settingsController);
        mainControllers.add(settingsController);
    }

    public void onUpdateReceived(final Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText() || message.hasLocation()) {
                handleIncomingMessage(message);
            }
        }
    }

    private void handleIncomingMessage(final Message message) {
        final Long chatId = message.getChatId();
        final User fromUser = message.getFrom();

        LOGGER.trace("Message from "
                + fromUser.getLastName()
                + fromUser.getFirstName()
                + "(" + fromUser.getUserName() + ")"
                + " : " + message.getText());

        TGSessionKey key = new TGSessionKey(fromUser, chatId);
        TGSession session = sessionMap.get(key);

        if (session == null) {
            // новый пользователь (или старый, но пишет из группового чата, неважно)
            session = new TGSession(chatId, fromUser);
            session.setNew(true);
            sessionMap.put(key, session);
        }

        try {
            final String lastController = session.getLastController();
            if (TextUtils.isEmpty(lastController)) {
                // у юзера не проставлен контроллер, выберем из меню
                for (Controller controller : mainControllers) {
                    if (controller.getEntry().equals(message.getText())) {
                        controller.handleMessage(message, session);
                        session.setLastController(controller.getKey());
                        return;
                    }
                }
                // это непонятное сообщение или например /start, просто покажем меню
                showMainMenu(message, session);
                return;
            }
            final Controller controller = controllerMap.get(lastController);
            // обработаем сообщение в последнем контроллере
            controller.handleMessage(message, session);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while handling incoming message: " + e.getMessage(), e);
        }
    }

    public void showMainMenu(final Message request, final TGSession session) throws TelegramApiException {
        ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();
        final SendMessage msg = createMessageWithKeyboard(request.getChatId().toString(), request.getMessageId(), replyKeyboardMarkup);
        if (session.isNew()) {
            session.setNew(false);
            msg.setText("Привет, бла-бла");
        } else {
            msg.setText("Меню");
        }
        sendMessage(msg);
    }

    private ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = null;
        for (int i = 0; i < mainControllers.size(); i++) {
            // группируем кнопки по две
            if (i % 2 == 0 || row == null) {
                row = new KeyboardRow();
                keyboard.add(row);
            }
            final Controller controller = mainControllers.get(i);
            final String entry = controller.getEntry();
            row.add(entry);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    private SendMessage createMessageWithKeyboard(final String chatId, final Integer messageId, final ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyToMessageId(messageId);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    public String getBotUsername() {
        return "gop_stop_bot";
    }

    @Override
    public String getBotToken() {
        if (TextUtils.isEmpty(token)) {
            Properties properties = new Properties();
            try {
                properties.load(properties.getClass().getResourceAsStream("/secret.properties"));
            } catch (IOException e) {
                throw new RuntimeException("No secret.properties with telegram token found in resources/");
            }
            token = properties.getProperty("token");
            if (TextUtils.isEmpty(token)) {
                throw new RuntimeException("No telegram token found in resources/secret.properties");
            }
            return token;
        }
        return token;
    }

}
