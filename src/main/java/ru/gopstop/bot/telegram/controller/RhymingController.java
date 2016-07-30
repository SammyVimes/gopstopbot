package ru.gopstop.bot.telegram.controller;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.gopstop.bot.engine.CleverEngine;
import ru.gopstop.bot.engine.StubEngine;
import ru.gopstop.bot.engine.entities.GopSong;
import ru.gopstop.bot.engine.entities.Rhyme;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Semyon on 30.07.2016.
 */
public class RhymingController extends BaseMuzisController {

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

        if (text.equals(BACK)) {
            back(request, session);
            return;
        }

        final Rhyme rhyme = CleverEngine.getRhyme(text);

        if (rhyme != null) {
            onRhymeFound(request, session, rhyme);
        } else {
            sendMessage(request.getChatId().toString(), "Ничего не нашли");
        }
    }

    private void onRhymeFound(final Message request, final TGSession session, final Rhyme rhyme) throws TelegramApiException {

        final GopSong gopSong = rhyme.getGopSong();
        sendMessage(
                request.getChatId().toString(),
                String.format("Рифмы подъехали (%s - %s)", gopSong.getAuthor(), gopSong.getName()));
        sendMessage(
                request.getChatId().toString(),
                rhyme.getRhyme());


        String gopSongName = gopSong.getName().replace("-", " "); // иначе не ищет!

        final SearchResult res = muzisService.search(gopSongName, gopSong.getAuthor(), null, null, null, null, null);

        final Optional<Song> foundSong = res.getSongs()
                .stream()
                .findFirst();

        if (!foundSong.isPresent()) {
            // ничего не нашли, пробуем исполнителя и ищем по его id 6 треков
            final SearchResult byAuthor = muzisService.search(null, gopSong.getAuthor(), null, null, null, null, null);
            final Optional<Performer> first = byAuthor.getPerformers().stream().findFirst();
            final List<Song> songs = first.map(performer ->
                    muzisService.songsByPerformer(performer.getId())).map(byAuthorRes ->
                    byAuthorRes.getSongs().stream()
                            .limit(6)
                            .collect(Collectors.toList())).orElseGet(Collections::emptyList);

            // к названию приклеиваем "Слушать ", чтобы потом было понятно, что это запрос на прослушивание
            final List<String> keyboard = songs.stream()
                    .map(song -> "Слушать " + song.getTitle())
                    .collect(Collectors.toList());

            String reply = "";
            if (keyboard.isEmpty()) {
                reply = String.format("Но мы не нашли репертуар автора (%s) на Muzis", gopSong.getAuthor());
            } else {
                reply = "Эту песню мы не нашли, но вот другие песни автора";
            }
            keyboard.add(BACK);
            final ReplyKeyboardMarkup replyKeyboardMarkup = buildKeyboard(keyboard);

            session.put("results", songs);

            final SendMessage msg = createMessageWithKeyboard(request.getChatId().toString(), request.getMessageId(), reply, replyKeyboardMarkup);
            bot.sendMessage(msg);
            // перекидываем на поиск по песням
            session.setLastController(Constants.SONGS);
            return;
        }

        sendMessage(request.getChatId().toString(), "А вот и сама песня:");
        final Song song = foundSong.get();
        sendSongAndCover(request, song);
    }

    private void onMain(final Message request, final TGSession session) throws TelegramApiException {
        final ReplyKeyboardMarkup replyKeyboardMarkup = buildKeyboard(Arrays.asList(BACK));
        final SendMessage msg = createMessageWithKeyboard(request.getChatId().toString(), request.getMessageId(), "Сегодня мы с тобой рифмуем", replyKeyboardMarkup);
        bot.sendMessage(msg);
    }

}
