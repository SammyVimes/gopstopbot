package ru.gopstop.bot.engine.tools;

import com.google.common.io.Files;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.engine.entities.GopSong;
import ru.gopstop.bot.engine.search.LinesIndexer;
import ru.gopstop.bot.muzis.entity.Song;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Чтение корпуса
 * Created by aam on 30.07.16.
 */
public final class SongsUtils {

    private static final Logger LOGGER = LogManager.getLogger(LinesIndexer.class);

    public static Stream<GopSong> listSongsByDir(final String dir) throws IOException {

        return listSongFilesByDir(dir)
                .map(f -> {
                    try {
                        return readSongFromFile(f);
                    } catch (IOException ioe) {
                        LOGGER.error("cant list songs", ioe);
                        throw new RuntimeException("cant list songs", ioe);
                    }
                });
    }

    public static Stream<File> listSongFilesByDir(final String dir) throws IOException {

        return java.nio.file.Files
                .walk(java.nio.file.Paths.get(dir))
                .filter(java.nio.file.Files::isRegularFile)
                .filter(f -> !f.getFileName().endsWith("README.txt"))
                .map(Path::toFile);
    }

    static GopSong readSongFromFile(final File file) throws IOException {

        final List<String> lines = Files.readLines(file, Charset.forName("utf-8"));
        return new GopSong(
                lines.get(1).trim(),
                lines.get(0).trim(),
                lines.subList(2, lines.size()));
    }

    private SongsUtils() {

    }
}
