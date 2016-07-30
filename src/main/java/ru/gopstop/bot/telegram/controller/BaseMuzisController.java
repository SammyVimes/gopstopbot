package ru.gopstop.bot.telegram.controller;

import org.apache.http.util.TextUtils;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import ru.gopstop.bot.FileUtils;
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
public abstract class BaseMuzisController extends Controller {

    protected MuzisService muzisService = MuzisServiceBuilder.getMuzisService();

    protected ResourcesService resourcesService = MuzisServiceBuilder.getResourcesService();

    public BaseMuzisController(final TGBot bot) {
        super(bot);
    }

    /**
     * Отправляем песню и картинку песни
     * @param request
     * @param song
     * @throws TelegramApiException
     */
    protected void sendSongAndCover(final Message request, final Song song) throws TelegramApiException {
        if (!TextUtils.isEmpty(song.getPoster())) {
            sendMessage(request.getChatId().toString(), "Вот картиночка");
            // скачиваем (или берём с диска) картинку и отправляем
            File cachedFile = FileUtils.getCachedFile(song.getPoster());
            if (cachedFile == null) {
                cachedFile = FileUtils.writeResponseBodyToDisk(resourcesService.downloadFile(song.getPoster()), song.getPoster());
            }

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(request.getChatId().toString());
            if (cachedFile == null) {
                sendMessage(request.getChatId().toString(), "Что-то пошло не так со скачиванием картинки");
                return;
            }
            sendPhoto.setNewPhoto(cachedFile);
            bot.sendPhoto(sendPhoto);
            sendMessage(request.getChatId().toString(), "Сейчас и песню пришлю");
        } else {
            sendMessage(request.getChatId().toString(), "Сейчас пришлю");
        }

        // скачиваем музло (или берём с диска) и отправляем
        File cachedMusicFile = FileUtils.getCachedFile(song.getFileMp3());
        if (cachedMusicFile == null) {
            cachedMusicFile = FileUtils.writeResponseBodyToDisk(resourcesService.downloadFile(song.getFileMp3()), song.getFileMp3());
        }

        SendAudio audio = new SendAudio();
        if (cachedMusicFile == null) {
            sendMessage(request.getChatId().toString(), "Что-то пошло не так со скачиванием музыки");
            return;
        }
        audio.setNewAudio(cachedMusicFile);
        // телеграм не ест кириллицу, транслитим транслитом
        audio.setPerformer(Translit.cyr2lat(song.getPerformer()));
        audio.setTitle(Translit.cyr2lat(song.getTrackName()));
        audio.setChatId(request.getChatId().toString());
        bot.sendAudio(audio);
        sendMessage(request.getChatId().toString(), "Послушай, а потом можешь искать новые рифмы и песни");
    }

}
