package ru.gopstop.bot.engine;

import ru.gopstop.bot.engine.entities.Rhyme;
import ru.gopstop.bot.engine.filters.SameLastWordFilter;
import ru.gopstop.bot.engine.filters.SameLineFilter;
import ru.gopstop.bot.engine.filters.WordStressFilter;
import ru.gopstop.bot.engine.search.FoundGopSong;
import ru.gopstop.bot.engine.search.LinesIndexer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by aam on 31.07.16.
 */
public class CleverEngine {

    public static Rhyme getRhyme(final String userInput) {

        //  тупой поиск без учёта ударения
        final List<FoundGopSong> foundGopSongList =
                LinesIndexer.getInstance()
                        .search(userInput)
                        .stream()
                        .filter(g -> SameLineFilter.filter(userInput, g))
                        .filter(g -> WordStressFilter.filter(userInput, g))
                        .filter(g -> SameLastWordFilter.filter(userInput, g))
                        .collect(Collectors.toList());

        //todo: фильтрация и хаки по скорингу!

        if (!foundGopSongList.isEmpty()) {
            final FoundGopSong foundGopSong = foundGopSongList.get(0);
            return new Rhyme(foundGopSong.getRhyme(), foundGopSong.getGopSong());
        } else {
            return null;
        }
    }
}
