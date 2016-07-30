package ru.gopstop.bot.engine.tools;

import com.google.common.io.Files;
import ru.gopstop.bot.engine.entities.GopSong;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by aam on 30.07.16.
 */
public class SongsUtils {


    public static List<GopSong> listSongsByDir(final String dir) throws IOException {

        final List<GopSong> songs = new ArrayList<>();

        for (final File songFile : listSongFilesByDir(dir)) {
            songs.add(readSongFromFile(songFile));
        }

        return songs;
    }

    static List<File> listSongFilesByDir(final String dir) throws IOException {

        return java.nio.file.Files
                .walk(java.nio.file.Paths.get(dir))
                .filter(java.nio.file.Files::isRegularFile)
                .filter(f -> !f.getFileName().endsWith("README.txt"))
                .map(Path::toFile)
                .collect(Collectors.toList());
    }


    public static GopSong readSongFromFile(final File file) throws IOException {
        final List<String> lines = Files.readLines(file, Charset.forName("utf-8"));
        return new GopSong(
                lines.get(0).trim(),
                lines.get(1).trim(),
                lines.subList(2, lines.size()));
    }
}
