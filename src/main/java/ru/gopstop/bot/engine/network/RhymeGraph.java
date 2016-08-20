package ru.gopstop.bot.engine.network;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.engine.search.preprocessing.BasicPreprocessor;
import ru.gopstop.bot.engine.tools.SongsUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static ru.gopstop.bot.engine.tools.PhoneticsKnowledgeTools.VOWELS_PAIRS;
import static ru.gopstop.bot.engine.tools.PhoneticsKnowledgeTools.VOWELS_SET;

/**
 * Created by aam on 18.08.16.
 */
public final class RhymeGraph {

    private static final Logger LOGGER = LogManager.getLogger(RhymeGraph.class);

    private static final String DATA_PATH = "data/songs/";

    private static final String SERIALIZED_DICT_PATH = "rhyme_graph.bin";

    private static final RhymeGraph INSTANCE;

    private static final int SHORT_WORD_LENGTH = 4;

    private static final int POSTFIX = 5;

    private static final int MAX_LEVE_DIST = 4;

    private static final int MAX_WINDOW = 3;

    private static final Set<Character> UPPERCASE_VOWELS =
            VOWELS_SET
                    .stream()
                    .map(Character::toUpperCase)
                    .collect(Collectors.toSet());

    public static RhymeGraph getInstance() {
        return INSTANCE;
    }

    static {
        try {
            INSTANCE = new RhymeGraph();
        } catch (final IOException ioe) {
            LOGGER.error("Rhyme graph is dead, no use in continuations", ioe);
            throw new RuntimeException(ioe);
        }
    }

    private UndirectedSparseGraph<String, Integer> graph;

    /**
     * Метод получения рифмующихся хвостов
     */
    public Collection<String> getCloseRhymes(final String line) {

        final String postfix = BasicPreprocessor.postfix(line, true);

        final String cutPostfix =
                postfix.substring(0, Math.min(postfix.length(), POSTFIX));

        if (graph.containsVertex(cutPostfix)) {
            final Set<String> res = new HashSet<>(graph.getNeighbors(cutPostfix));
            res.add(cutPostfix);
            return res;
        } else {
            return Collections.singletonList(cutPostfix);
        }
    }

    // nullable
    private static Character stress(final String string) {

        final List<Character> vowels = Lists.newArrayList();

        for (final Character c : string.toCharArray()) {

            if (UPPERCASE_VOWELS.contains(c)) {
                return Character.toLowerCase(c);
            }

            if (VOWELS_SET.contains(c)) {
                vowels.add(c);
            }
        }

        if (vowels.isEmpty()) {
            return null;
        }

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

    private void markup(final List<String> lines,
                        final Graph<String, Integer> postfixes) {

        final Set<Integer> rhymed = Sets.newHashSet();

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
                for (int d = 1; d <= Math.min(MAX_WINDOW, lines.size() - i - 1); d++) {

                    // не рассматриваем уже зарифмованное
                    if (!rhymed.contains(i + d)) {

                        // готовим кандидата
                        final String candidate =
                                BasicPreprocessor.postfix(lines.get(i + d), false, true);

                        // отбрасываем подозрительно коротких, нам не нужны false positives
                        if (candidate == null || lines.get(i + d).length() < SHORT_WORD_LENGTH) {
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

                if (minDistIndex >= 0 && minDist < MAX_LEVE_DIST) {

                    rhymed.add(minDistIndex);

                    if (postfixes.findEdge(currentPostfix, bestPostfix) == null) {
                        postfixes.addEdge(
                                postfixes.getEdgeCount(),
                                currentPostfix,
                                bestPostfix);
                    }
                }
            }
        }
    }


    private RhymeGraph() throws IOException {

        final UndirectedSparseGraph<String, Integer> tempGraph;

        try {
            final FileInputStream fis = new FileInputStream(SERIALIZED_DICT_PATH);
            final ObjectInputStream ois = new ObjectInputStream(fis);
            tempGraph = (UndirectedSparseGraph) ois.readObject();
            graph = tempGraph;
            ois.close();
            fis.close();
        } catch (final Exception e) {

            LOGGER.warn("COULD NOT DESER graph", e);

            //todo: читать нормально, например, заюзать StreamAPI

            graph = new UndirectedSparseGraph<>();

            SongsUtils
                    .listSongsByDir(DATA_PATH)
                    .forEach(song -> markup(song.getLyrics(), graph));

            LOGGER.info("Serialization...");

            try {
                final FileOutputStream fos = new FileOutputStream(SERIALIZED_DICT_PATH);
                final ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(graph);
                oos.close();
                fos.close();
                LOGGER.info("Serialized graph data is saved in " + SERIALIZED_DICT_PATH);
            } catch (final IOException ioe) {
                LOGGER.error("Graph dumping failure", ioe);
            }
            LOGGER.info("Graph serialization done");
        }
    }
}
