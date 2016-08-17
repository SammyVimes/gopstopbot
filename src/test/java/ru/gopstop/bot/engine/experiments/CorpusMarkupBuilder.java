package ru.gopstop.bot.engine.experiments;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import ru.gopstop.bot.engine.search.preprocessing.BasicPreprocessor;
import ru.gopstop.bot.engine.tools.SongsUtils;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by aam on 17.08.16.
 */
public class CorpusMarkupBuilder {

    private static final int POSTFIX = 5;

    private static void markup(final List<String> lines) {

        Set<Integer> rhymed = Sets.newHashSet();

        for (int i = 0; i < lines.size(); i++) {

            System.out.println("****\n-------------------\nnew "+lines.get(i));

            if (!rhymed.contains(i)) {

                final String currentLine =
                        BasicPreprocessor.postfix(lines.get(i), false);

                final String currentPostfix = currentLine
                        .substring(0, Math.min(POSTFIX, currentLine.length()));

                int minDist = Integer.MAX_VALUE;
                int minDistIndex = -1;

                for (int d = 1; d < Math.min(4, lines.size() - i); d++) {

                    final String candidate =
                            BasicPreprocessor
                                    .postfix(lines.get(i + d), false);

                    final String candidatePostfix = candidate
                            .substring(0, Math.min(POSTFIX, candidate.length()));

                    final int currdist =
                            StringUtils.getLevenshteinDistance(currentPostfix, candidatePostfix);

                    if (currdist < minDist) {
                        minDist = currdist;
                        minDistIndex = i + d;
                    }
                }

                if (minDistIndex >= 0 && minDist < 4) {

                    rhymed.add(i);
                    rhymed.add(minDistIndex);

                    System.out.println("\n------\n" + minDist + "\n" + i + " " + minDistIndex + "\n"
                            + currentPostfix
                            + "  "
                            + BasicPreprocessor.postfix(
                            lines.get(minDistIndex), false) + "\n");
                    System.out.println(lines.get(i) + " <=> " + lines.get(minDistIndex));
                } else {
                    System.out.println("skip");
                }
            }
        }
    }

    public static void main(final String[] args) throws IOException {

        SongsUtils
                .listSongsByDir("data/songs")
                .limit(1)
                .forEach(song -> {
                    System.out.println("---------------------");
                    markup(song.getLyrics());
                });
    }

}
