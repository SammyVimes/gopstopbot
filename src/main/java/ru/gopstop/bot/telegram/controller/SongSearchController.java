package ru.gopstop.bot.telegram.controller;

import org.apache.http.util.TextUtils;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.gopstop.bot.FileUtils;
import ru.gopstop.bot.muzis.MuzisService;
import ru.gopstop.bot.muzis.MuzisServiceBuilder;
import ru.gopstop.bot.muzis.ResourcesService;
import ru.gopstop.bot.muzis.entity.Performer;
import ru.gopstop.bot.muzis.entity.SearchResult;
import ru.gopstop.bot.muzis.entity.Song;
import ru.gopstop.bot.telegram.Constants;
import ru.gopstop.bot.telegram.TGBot;
import ru.gopstop.bot.telegram.internal.Emoji;
import ru.gopstop.bot.telegram.user.TGSession;
import ru.gopstop.bot.util.Translit;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Semyon on 30.07.2016.
 */
public class SongSearchController extends BaseMuzisController {

    private MuzisService muzisService = MuzisServiceBuilder.getMuzisService();

    private ResourcesService resourcesService = MuzisServiceBuilder.getResourcesService();

    public SongSearchController(final TGBot bot) {
        super(bot);
    }

    @Override
    public String getKey() {
        return Constants.SONGS;
    }

    @Override
    public String getEntry() {
        return Constants.SONGS;
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
            // если были результаты поиска, удаляем
            session.remove("results");
            return;
        }

        if (text.startsWith("Слушать ")) {
            // чувак пришёл с названием песни, достаём у него результаты последнего поиска
            String title = text.replace("Слушать ", "");
            final List<Song> songsResult = (List<Song>) session.get("results");
            if (songsResult == null) {
                sendMessage(request.getChatId().toString(), "Вы ничего не искали");
                return;
            }
            // ищем песню в его списке
            final Optional<Song> foundSong = songsResult.stream().filter(song -> song.getTitle().equals(title)).findAny();
            if (foundSong.isPresent()) {
                // отправим ему песню и картинку
                sendSongAndCover(request, foundSong.get());
            } else {
                sendMessage(request.getChatId().toString(), "Что-то пошло не так");
            }
            return;
        }

        final SearchResult res = muzisService.search(text, null, null, null, null, null, null);
        // берём 6 найденных песен и шлём
        final List<Song> songsResult = res.getSongs().stream().limit(6).collect(Collectors.toList());

        if (songsResult.isEmpty()) {
            // ничего не нашли, пробуем исполнителя и ищем по его id 6 треков
            final SearchResult byAuthor = muzisService.search(null, text, null, null, null, null, null);
            final Optional<Performer> first = byAuthor.getPerformers().stream().findFirst();
            first.map(performer ->
                    muzisService.songsByPerformer(performer.getId())).map(byAuthorRes ->
                            songsResult.addAll(byAuthorRes.getSongs().stream()
                                    .limit(6)
                                    .collect(Collectors.toList())));
        }

        // к названию приклеиваем "Слушать ", чтобы потом было понятно, что это запрос на прослушивание
        final List<String> keyboard = songsResult.stream()
                .map(song -> "Слушать " + song.getTitle())
                .collect(Collectors.toList());

        String reply = "";
        if (keyboard.isEmpty()) {
            reply = "Ничего не найдено :(";
        } else {
            reply = "Найденные песни";
        }
        keyboard.add(BACK);
        final ReplyKeyboardMarkup replyKeyboardMarkup = buildKeyboard(keyboard);

        session.put("results", songsResult);

        final SendMessage msg = createMessageWithKeyboard(request.getChatId().toString(), request.getMessageId(), reply, replyKeyboardMarkup);
        bot.sendMessage(msg);
    }

    private void onMain(final Message request, final TGSession session) throws TelegramApiException {
        final ReplyKeyboardMarkup replyKeyboardMarkup = buildKeyboard(Arrays.asList(BACK));
        final SendMessage msg = createMessageWithKeyboard(request.getChatId().toString(), request.getMessageId(), "Введи название песни", replyKeyboardMarkup);
        bot.sendMessage(msg);
    }

}
