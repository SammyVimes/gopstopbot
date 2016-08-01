package ru.gopstop.bot.engine.stress;

import junit.framework.TestCase;
import org.apache.commons.lang3.tuple.Pair;
import ru.gopstop.bot.engine.tools.SongsUtils;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by aam on 02.08.16.
 */
public class DBStressViewer extends TestCase {

    public void testPrintAllLinesMarkupForAnalysis() throws IOException {

        final FileWriter fw = new FileWriter("comparison.tsv");

        final WordStressMap wsm = WordStressMap.getInstance();

        SongsUtils.listSongsByDir("data/songs/")
                .forEach(gopSong -> {
                    try {
                        fw.write("\nTITLE\t" + gopSong.getAuthor() + "\t" + gopSong.getName() + "\n");
                        for (final String line : gopSong.getLyrics()) {
                            final String patternOld = wsm.findRhythmicPattern(line);
                            final Pair<FuzzyWordStressDeterminer.Foot, String> patternNew =
                                    FuzzyWordStressDeterminer.getPatternFittedToSomeSimpleFoot(line);
                            final String modifiedStringOld = ExtraWordStressTool.upperCaseByPattern(line, patternOld);
                            final String modifiedStringNew = ExtraWordStressTool.upperCaseByPattern(line, patternNew.getRight());

                            fw.write((patternNew.getLeft() == null ? "FAIL_NEW" : "SHOW_NEW")
                                    + "\t" + modifiedStringOld
                                    + "\t" + modifiedStringNew
                                    + "\t" + patternOld
                                    + "\t" + patternNew.getRight()
                                    + "\n");
                        }
                    } catch (final IOException ioe) {
                        System.out.println("FAIL" + gopSong + " ");
                    }
                });
        fw.close();
    }
}
