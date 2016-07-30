package ru.gopstop.bot.engine;

import ru.gopstop.bot.engine.entities.GopSong;
import ru.gopstop.bot.engine.entities.Rhyme;

import java.util.Collections;

/**
 * Created by Semyon on 30.07.2016.
 */
public class StubEngine {

    public static Rhyme getRhyme(final String userInput) {
        final String[] split = userInput.split(",");
        final GopSong gopSong = new GopSong(split[0], split[1], Collections.emptyList());
        return new Rhyme("Fake Rhyme", gopSong);
    }

}
