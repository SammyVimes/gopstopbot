package ru.gopstop.bot.telegram.controller;

import org.apache.http.util.TextUtils;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.ActionType;
import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import ru.gopstop.bot.FileUtils;
import ru.gopstop.bot.muzis.MuzisSearchHelper;
import ru.gopstop.bot.muzis.MuzisService;
import ru.gopstop.bot.muzis.MuzisServiceBuilder;
import ru.gopstop.bot.muzis.ResourcesService;
import ru.gopstop.bot.muzis.entity.Song;
import ru.gopstop.bot.telegram.TGBot;
import ru.gopstop.bot.util.Translit;

import java.io.File;

/**
 * Created by Semyon on 30.07.2016.
 */
abstract class BaseMuzisController extends Controller {

    private MuzisSearchHelper muzisSearchHelper = new MuzisSearchHelper();

    private MuzisService muzisService = MuzisServiceBuilder.getMuzisService();

    private ResourcesService resourcesService = MuzisServiceBuilder.getResourcesService();

    BaseMuzisController(final TGBot bot) {
        super(bot);
    }

    /**
     * Отправляем песню и картинку песни
     */
    void sendSongAndCover(final Message request, final Song song) throws TelegramApiException {

        if (!TextUtils.isEmpty(song.getPoster())) {
            // sendMessage(request.getChatId().toString(), "Вот фотокарточка");

            // скачиваем (или берём с диска) картинку и отправляем
            File cachedFile = FileUtils.getCachedFile(song.getPoster());

            if (cachedFile == null) {
                cachedFile =
                        FileUtils.writeResponseBodyToDisk(
                                resourcesService.downloadFile(song.getPoster()),
                                song.getPoster());
            }

            sendAction(request.getChatId().toString(), ActionType.UPLOADPHOTO);
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(request.getChatId().toString());

            if (cachedFile == null) {
                sendMessage(
                        request.getChatId().toString(),
                        "Что-то пошло не так со скачиванием картинки");
                return;
            }

            sendPhoto.setNewPhoto(cachedFile);
            getBot().sendPhoto(sendPhoto);
            // sendMessage(request.getChatId().toString(), "Сейчас и песню пришлю");

        } else {
            sendMessage(request.getChatId().toString(), "Сейчас пришлю");
        }

        // скачиваем музло (или берём с диска) и отправляем
        File cachedMusicFile = FileUtils.getCachedFile(song.getFileMp3());

        if (cachedMusicFile == null) {
            cachedMusicFile = FileUtils.writeResponseBodyToDisk(resourcesService.downloadFile(song.getFileMp3()), song.getFileMp3());
        }

        sendAction(request.getChatId().toString(), ActionType.UPLOADAUDIO);
        SendAudio audio = new SendAudio();

        if (cachedMusicFile == null) {
            sendMessage(
                    request.getChatId().toString(),
                    "Что-то пошло не так со скачиванием музыки");
            return;
        }
        audio.setNewAudio(cachedMusicFile);
        // телеграм не ест кириллицу, транслитим транслитом
        audio.setPerformer(Translit.cyr2lat(song.getPerformer()));
        audio.setTitle(Translit.cyr2lat(song.getTrackName()));
        audio.setChatId(request.getChatId().toString());
        getBot().sendAudio(audio);
    }

    public MuzisSearchHelper getMuzisSearchHelper() {
        return muzisSearchHelper;
    }

    public MuzisService getMuzisService() {
        return muzisService;
    }
}
