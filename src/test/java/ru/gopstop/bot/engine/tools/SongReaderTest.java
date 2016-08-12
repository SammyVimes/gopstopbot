package ru.gopstop.bot.engine.tools;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.gopstop.bot.engine.entities.GopSong;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Created by aam on 30.07.16.
 */
public class SongReaderTest extends TestCase {

    @Test
    @Ignore
    public void testReading() throws IOException {

//        Assert.assertTrue(SongsUtils
//                .listSongFilesByDir("data/songs/").size()  != 0);
//
//        System.out.println(SongsUtils
//                .listSongsByDir("data_test/songs/"));

        SongsUtils
                .listSongFilesByDir("data/")
                .limit(15)
                .forEach(f -> {
                    try {
                     final GopSong song = SongsUtils.readSongFromFile(f);
                     System.out.println(song.getLyrics().stream().limit(5).collect(Collectors.joining("\n")));
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("bsh");
                    }
                });
    }
}
