package ru.gopstop.bot.engine.tools;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by aam on 30.07.16.
 */
public class SongReaderTest extends TestCase {

    @Test
    public void testReading() throws IOException {

        SongsUtils
                .listSongsByDir("data/").stream()
                .limit(15)
                .forEach(f -> {
                    try {
                        SongsUtils.readSongFromFile(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("bsh");
                    }
                });
    }
}
