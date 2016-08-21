package ru.gopstop.bot.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.engine.entities.GopSong;
import ru.gopstop.bot.engine.entities.Rhyme;
import ru.gopstop.bot.engine.filters.*;
import ru.gopstop.bot.engine.search.FoundGopSong;
import ru.gopstop.bot.engine.search.LinesIndexer;

import java.util.Collections;
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

    private static final int MIN_FINAL_SUGGESTIONS_SIZE = 3;

    private static final double ACCEPTABLE_SCORE_THRESHOLD = 0.05;

    private static List<FoundGopSong> getAcceptableRhymes(
            final List<FoundGopSong> songs,
            final double topScore) {
        return songs
                .stream()
                //todo: set epsilon?
                .filter(fs -> fs.getScore() >= topScore - ACCEPTABLE_SCORE_THRESHOLD)
                .collect(Collectors.toList());
    }

    public static Rhyme getRhyme(final String userInput) {

        // простите меня, святые отцы информационного поиска, я не хотел
        if (userInput.trim().toLowerCase().matches("(.*[^A-Za-zА-Яа-яёЁ]|^)(путин(а|у|ым|е)?|putin)[^A-Za-zА-Яа-яёЁ]*")) {
            return new Rhyme(
                    "Lorem ipsum dolor sit amet, consectetur adipiscin...",
                    new GopSong("Untitled Track", "Unknown Artist", null)
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

        Collections.sort(foundGopSongList, (gs0, gs1) -> -Double.compare(gs0.getScore(), gs1.getScore()));

        foundGopSongList
                .stream()
                .limit(LOGGED_TOP)
                .forEach(gopSong ->
                        LOGGER.debug("AFTER_FILTERING\t" + gopSong.getScore() + "\t|\t" + gopSong.getRhyme()));

        //  хитрая фильтрация на имитации метра
        final List<FoundGopSong> experimentalGopSongList =
                foundGopSongList
                        .stream()
                        .filter(g -> ExperimentalMetreFilter.filter(userInput, g))
                        //todo: пока работает очень неважно в плане качества, надо наделать хаков
                        // .filter(g -> ExperimentalSophisticatedMetreFilter.filter(userInput, g))
                        .collect(Collectors.toList());

        final List<FoundGopSong> resultingGopSongList;

        if (!experimentalGopSongList.isEmpty()) {

            final double scoreDiff = foundGopSongList.get(0).getScore() - experimentalGopSongList.get(0).getScore();

            if (scoreDiff < ACCEPTABLE_SCORE_THRESHOLD
                    && getAcceptableRhymes(
                    experimentalGopSongList,
                    experimentalGopSongList.get(0).getScore())
                    .size() >= MIN_FINAL_SUGGESTIONS_SIZE) {
                LOGGER.info("Extra filter applied, "
                        + foundGopSongList.size() + "->"
                        + experimentalGopSongList.size() + ", score diff is OK: " + scoreDiff);
                resultingGopSongList = experimentalGopSongList;
            } else {
                LOGGER.info("Score diff too big [" + scoreDiff
                        + "] or size is too small = [ "
                        + experimentalGopSongList.size() + " ] "
                        + experimentalGopSongList.get(0).getRhyme() + " || "
                        + foundGopSongList.get(0).getRhyme());
                resultingGopSongList = foundGopSongList;
            }

        } else {
            LOGGER.debug("Experimental metre filtering was too strict");
            resultingGopSongList = foundGopSongList;
        }

        if (!resultingGopSongList.isEmpty()) {

            // let's add some spice and randomize the whole thing
            final FoundGopSong topFoundGopSong = resultingGopSongList.get(0);
            LOGGER.info("Top song: " + topFoundGopSong);

            final List<FoundGopSong> filteredList =
                    getAcceptableRhymes(resultingGopSongList, topFoundGopSong.getScore());

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
