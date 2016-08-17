package ru.gopstop.bot.engine.experiments;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import edu.uci.ics.jung.algorithms.cluster.BicomponentClusterer;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import org.apache.commons.lang3.StringUtils;
import ru.gopstop.bot.engine.search.preprocessing.BasicPreprocessor;
import ru.gopstop.bot.engine.tools.SongsUtils;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.gopstop.bot.engine.tools.PhoneticsKnowledgeTools.VOWELS_PAIRS;
import static ru.gopstop.bot.engine.tools.PhoneticsKnowledgeTools.VOWELS_SET;

/**
 * Created by aam on 17.08.16.
 */
public class CorpusMarkupBuilder {

    private static final int POSTFIX = 5;

    private static final Set<Character> UPPERCASE_VOWELS =
            VOWELS_SET.stream().map(Character::toUpperCase).collect(Collectors.toSet());

    // nullable
    private static Character stress(String string) {

        final List<Character> vowels = Lists.newArrayList();

        for (final Character c : string.toCharArray()) {

            if (UPPERCASE_VOWELS.contains(c)) {
                return Character.toLowerCase(c);
            }

            if (VOWELS_SET.contains(c)) {
                vowels.add(c);
            }
        }

        if (vowels.isEmpty())
            return null;

        return vowels.get(vowels.size() - 1);
    }

    private static boolean likeEq(final Character first, final Character second) {

        if (first == null || second == null) {
            return false;
        }

        return first.equals(second)
                || VOWELS_PAIRS.containsKey(first) && VOWELS_PAIRS.get(first).equals(second)
                || VOWELS_PAIRS.containsKey(second) && VOWELS_PAIRS.get(second).equals(first);
    }

    private static void markup(final List<String> lines,
                               final Graph<String, Integer> postfixes) {

        Set<Integer> rhymed = Sets.newHashSet();

        // по всем строкам
        for (int i = 0; i < lines.size(); i++) {

            if (!rhymed.contains(i)) {

                // предобработка -- оставляем последнее
                final String currentLine =
                        BasicPreprocessor.postfix(lines.get(i), false, true);

                if (currentLine == null) {
                    continue;
                }

                // берём ограниченный постфикс
                final String currentPostfix = currentLine
                        .substring(0, Math.min(POSTFIX, currentLine.length()));

                int minDist = Integer.MAX_VALUE;
                int minDistIndex = -1;
                String bestPostfix = "";

                // для небольшого окна
                for (int d = 1; d < Math.min(4, lines.size() - i); d++) {

                    // не рассматриваем уже зарифмованное
                    if (!rhymed.contains(i + d)) {

                        // готовим кандидата
                        final String candidate =
                                BasicPreprocessor.postfix(lines.get(i + d), false, true);

                        // отбрасываем подозрительно коротких, нам не нужны false positives
                        if (candidate == null || lines.get(i + d).length() < 4) {
                            continue;
                        }

                        // постфикс от кандидата
                        final String candidatePostfix = candidate
                                .substring(0, Math.min(POSTFIX, candidate.length()));

                        // текцщее растояние Левенштейна
                        final int currdist =
                                StringUtils.getLevenshteinDistance(currentPostfix, candidatePostfix);

                        // проверяем, корректная ли гласная -- кандидат на ударную
                        final boolean eqOrSo = likeEq(stress(candidatePostfix), stress(currentPostfix));

                        // если похоже, что гласная та, и расстояние приемлемо, мы принимаем
                        if (currdist < minDist && eqOrSo) {
                            minDist = currdist;
                            minDistIndex = i + d;
                            bestPostfix = candidatePostfix;
                        }
                    }
                }

                if (minDistIndex >= 0 && minDist < 4) {

                    rhymed.add(minDistIndex);

                    if (postfixes.findEdge(currentPostfix, bestPostfix) == null) {
                        postfixes.addEdge(
                                postfixes.getEdgeCount(),
                                currentPostfix,
                                bestPostfix);
                    }

                    System.out.println(minDist + " " + lines.get(i) + " <=> " + lines.get(minDistIndex));
                } else {
                    System.out.println(minDist + " " + "skip");
                }
            }
        }
    }

    public static void main(final String[] args) throws IOException {

        final UndirectedSparseGraph<String, Integer> graph =
                new UndirectedSparseGraph<>();

        SongsUtils
                .listSongsByDir("data/songs")
                .limit(1000)
                .forEach(song -> {
                    System.out.println("---------------------");
                    markup(song.getLyrics(), graph);
                });

        final BicomponentClusterer<String, Integer> clusterer = new BicomponentClusterer<>();
        final Set<Set<String>> connectedSets = clusterer.apply(graph);

        for (final Set<String> set : connectedSets) {
            System.out.println(set);
        }
    }

}
