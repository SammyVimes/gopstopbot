package ru.gopstop.bot.telegram.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.gopstop.bot.muzis.MuzisService;
import ru.gopstop.bot.muzis.MuzisServiceBuilder;
import ru.gopstop.bot.muzis.entity.Performer;
import ru.gopstop.bot.muzis.entity.SearchResult;
import ru.gopstop.bot.muzis.entity.Song;
import ru.gopstop.bot.telegram.Constants;
import ru.gopstop.bot.telegram.TGBot;
import ru.gopstop.bot.telegram.user.TGSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Semyon on 30.07.2016.
 */
public class SongSearchController extends BaseMuzisController {

    private static final int TOP_SONGS_COUNT = 6;

    private static final Logger LOGGER = LogManager.getLogger(SongSearchController.class);

    private MuzisService muzisService = MuzisServiceBuilder.getMuzisService();

    public SongSearchController(final TGBot bot) {
        super(bot);
    }

    @Override
    public String getKey() {
        return Constants.ControllersTags.SONGS.getName();
    }

    @Override
    public String getEntry() {
        return Constants.ControllersTags.SONGS.getName();
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
            final Optional<Song> foundSong =
                    songsResult
                            .stream()
                            .filter(song -> song.getTitle().equals(title))
                            .findAny();

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
        List<Song> songsResults;

        try {
            songsResults = res.getSongs()
                    .stream()
                    .limit(TOP_SONGS_COUNT)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e);
            songsResults = new ArrayList<>();
        }

        final List<Song> songsResult = new ArrayList<>(songsResults);

        if (songsResult.isEmpty()) {
            // ничего не нашли, пробуем исполнителя и ищем по его id 6 треков
            final SearchResult byAuthor = muzisService.search(null, text, null, null, null, null, null);

            final Optional<Performer> first =
                    byAuthor.getPerformers()
                            .stream()
                            .findFirst();

            first
                    .map(performer -> muzisService.songsByPerformer(performer.getId()))
                    .map(byAuthorRes ->
                            songsResult.addAll(
                                    byAuthorRes
                                            .getSongs()
                                            .stream()
                                            .limit(TOP_SONGS_COUNT)
                                            .collect(Collectors.toList())));
        }

        // к названию приклеиваем "Слушать ", чтобы потом было понятно, что это запрос на прослушивание
        final List<String> keyboard =
                songsResult
                        .stream()
                        .map(song -> "Слушать " + song.getTitle())
                        .collect(Collectors.toList());

        final String reply;

        if (keyboard.isEmpty()) {
            reply = "Ничего не найдено :(";
        } else {
            reply = "Найденные песни";
        }

        keyboard.add(BACK);

        final ReplyKeyboardMarkup replyKeyboardMarkup = buildKeyboard(keyboard);

        session.put("results", songsResult);

        final SendMessage msg =
                createMessageWithKeyboard(
                        request.getChatId().toString(),
                        request.getMessageId(),
                        reply,
                        replyKeyboardMarkup);
        getBot().sendMessage(msg);
    }

    private void onMain(final Message request, final TGSession session) throws TelegramApiException {

        final ReplyKeyboardMarkup replyKeyboardMarkup = buildKeyboard(Arrays.asList(BACK));
        final SendMessage msg =
                createMessageWithKeyboard(
                        request.getChatId().toString(),
                        request.getMessageId(),
                        "Введи название песни",
                        replyKeyboardMarkup);
        getBot().sendMessage(msg);
    }
}