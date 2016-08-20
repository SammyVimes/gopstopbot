package ru.gopstop.bot.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.engine.entities.GopSong;
import ru.gopstop.bot.engine.entities.Rhyme;
import ru.gopstop.bot.engine.filters.SameLastWordFilter;
import ru.gopstop.bot.engine.filters.SameLineFilter;
import ru.gopstop.bot.engine.filters.UglyDataFilter;
import ru.gopstop.bot.engine.filters.WordStressFilter;
import ru.gopstop.bot.engine.search.FoundGopSong;
import ru.gopstop.bot.engine.search.LinesIndexer;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Поиск и отбор рифм перед итоговой отправкой в мессенджер
 * Created by aam on 31.07.16.
 */
public final class CleverEngine {

    private static final int LOGGED_TOP = 5;

    private static final Logger LOGGER = LogManager.getLogger(CleverEngine.class);

    private static final int TOP_SUGGESTIONS = 50;

    public static Rhyme getRhyme(final String userInput) {

        // простите меня, святые отцы информационного поиска, я не хотел
        if (userInput.trim().toLowerCase().matches("(.*[^A-Za-zА-Яа-яёЁ]|^)(путин(а|у|ым|е)?|putin)[^A-Za-zА-Яа-яёЁ]*")) {
            return new Rhyme(
                    "Lorem ipsum dolor sit amet, consectetur adipiscin...",
                    new GopSong("Unknown track", "Various artists", null)
            );
        }

        //  тупой поиск без учёта ударения
        final List<FoundGopSong> foundGopSongList =
                LinesIndexer
                        .getInstance()
                        .search(userInput)
                        .stream()
                        .filter(g -> SameLineFilter.filter(userInput, g))
                        .filter(g -> WordStressFilter.filter(userInput, g))
                        .filter(g -> SameLastWordFilter.filter(userInput, g))
                        .filter(g -> UglyDataFilter.filter(userInput, g))
                        .collect(Collectors.toList());

        foundGopSongList
                .stream()
                .limit(LOGGED_TOP)
                .forEach(gopSong ->
                        LOGGER.debug("AFTER_FILTERING\t" + gopSong.getScore() + "\t|\t" + gopSong.getRhyme()));

        if (!foundGopSongList.isEmpty()) {

            // let's add some spice and randomize the whole thing
            final FoundGopSong topFoundGopSong = foundGopSongList.get(0);
            LOGGER.info("Top song: " + topFoundGopSong);

            final List<FoundGopSong> filteredList =
                    foundGopSongList
                            .stream()
                            //todo: set epsilon?
                            .filter(fs -> fs.getScore() >= topFoundGopSong.getScore())
                            .collect(Collectors.toList());

            final Random random = new Random();
            final int chosenRandomRhymeId = random.nextInt(filteredList.size());
            final FoundGopSong chosenSong = filteredList.get(chosenRandomRhymeId);
            LOGGER.info("Chosen gop song number "
                    + chosenRandomRhymeId + "/"
                    + filteredList.size() + ": " + chosenSong);

            return new Rhyme(chosenSong.getRhyme(), chosenSong.getGopSong());
        } else {
            return null;
        }
    }

    private CleverEngine() {

    }
}
