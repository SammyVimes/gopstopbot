package ru.gopstop.bot.telegram.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.ActionType;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.gopstop.bot.FileUtils;
import ru.gopstop.bot.cache.SessionCache;
import ru.gopstop.bot.engine.CleverEngine;
import ru.gopstop.bot.engine.entities.GopSong;
import ru.gopstop.bot.engine.entities.Rhyme;
import ru.gopstop.bot.muzis.entity.Performer;
import ru.gopstop.bot.muzis.entity.SearchResult;
import ru.gopstop.bot.muzis.entity.Song;
import ru.gopstop.bot.telegram.Constants;
import ru.gopstop.bot.telegram.TGBot;
import ru.gopstop.bot.telegram.internal.Emoji;
import ru.gopstop.bot.telegram.user.TGSession;
import ru.gopstop.bot.util.TweetGen;
import ru.gopstop.bot.util.VKGen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

//        sendMessage(
////                FileUtils.REPORT_CHAT_ID,
//                request.getChatId().toString(),
//                session.getUser().getUserName() + "\n"
//                        + session.getUser().getFirstName() + "\n"
//                        + session.getUser().getLastName() + "\n"
//                        + text.replaceAll("\t", " ") + "\n"
//                        + (rhyme == null ? "NO_RHYME" : rhyme.getRhyme()) + "\t");

        if (rhyme != null) {
            onRhymeFound(request, session, rhyme);
        } else {
            sendMessage(request.getChatId().toString(), "Ничего не нашли");
        }
    }

    private void onRhymeFound(final Message request, final TGSession session, final Rhyme rhyme) throws TelegramApiException {

        final GopSong gopSong = rhyme.getGopSong();

        final String sharingText =
                String.format(
                        "Рассказать пацанам из [вконтача](%s) или [твиттерка](%s)",
                        VKGen.generate(request.getText(), rhyme.getRhyme(), rhyme.getGopSong().getName()),
                        TweetGen.generate(request.getText(), rhyme.getRhyme(), rhyme.getGopSong().getName()));

        sendHtmlMessage(
                request.getChatId().toString(),
                String.format(
                        "<b>%s\n%s</b>\n(%s — %s)",
                        request.getText(), rhyme.getRhyme(),
                        gopSong.getAuthor(), gopSong.getName()));
        sendMessage(
                request.getChatId().toString(),
                sharingText);

        final String gopSongName = gopSong.getName().replace("-", " "); // иначе не ищет!

        final SearchResult res = getMuzisService().search(gopSongName + " " + gopSong.getAuthor(), null, null, null, null, null, null);

        final Optional<Song> foundSong = res.getSongs()
                .stream()
                .filter(song -> song.getPerformer().contains(gopSong.getAuthor()) || gopSong.getAuthor().contains(song.getPerformer()))
                .findFirst();

        if (!foundSong.isPresent()) {
            // ничего не нашли, пробуем исполнителя и ищем по его id 6 треков
            final List<Performer> performers = getMuzisSearchHelper().searchByPerformer(gopSong.getAuthor());
            final Optional<Performer> first = performers
                    .stream()
                    // возможно это надо убрать
                    // проверка, что это точно тот автор, который нам нужен
                    .filter(performer -> getMuzisSearchHelper().checkPerformer(performer, gopSong.getAuthor()))
                    .findFirst();

            final List<Song> songs = first
                    .map(performer -> getMuzisService().songsByPerformer(performer.getId()))
                    .map(byAuthorRes ->
                            byAuthorRes.getSongs().stream()
                                    .limit(TOP_SONGS_COUNT)
                                    .collect(Collectors.toList()))
                    .orElseGet(Collections::emptyList);

            // к названию приклеиваем "Слушать ", чтобы потом было понятно, что это запрос на прослушивание
            // NB! временно отрываю
            //todo: либо убить совсем, либо вернуть, но поправить историю с кнопками
            final List<String> keyboard = new ArrayList<>();
            // songs
            // .stream()
            // .map(song -> "Слушать " + song.getTitle())
            // .collect(Collectors.toList());

            final String reply;

            if (keyboard.isEmpty()) {
                reply = String.format("Но мы не нашли репертуар автора (%s) на Muzis. Можешь искать рифмы дальше.", gopSong.getAuthor());
            } else {
                reply = "Эту песню мы не нашли, но вот другие песни автора";
                // НЕ перекидываем на поиск по песням, оставляем режим рифмовки
                session.setLastController(Constants.ControllersTags.RHYMES.getName());
                SessionCache.getInstance().updateSession(session);
            }

            keyboard.add(SettingsController.ABOUT);
            final ReplyKeyboardMarkup replyKeyboardMarkup = buildKeyboard(keyboard);

            session.put("results", songs);

            final SendMessage msg =
                    createMessageWithKeyboard(
                            request.getChatId().toString(),
                            request.getMessageId(),
                            reply,
                            replyKeyboardMarkup);
            getBot().sendMessage(msg);
            return;
        }

        final Song song = foundSong.get();
        sendSongAndCover(request, song);
        // sendMessage(request.getChatId().toString(), "Послушай, а потом можешь искать новые рифмы и песни");
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
