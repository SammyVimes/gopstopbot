package ru.gopstop.bot.engine.search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import ru.gopstop.bot.engine.entities.GopSong;
import ru.gopstop.bot.engine.search.preprocessing.BasicPreprocessor;
import ru.gopstop.bot.engine.tools.SongsUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Индексатор строчек песен
 * Created by aam on 30.07.16.
 */
public final class LinesIndexer {

    private static final int ANALYZED_POSTFIX_SIZE = 7;

    private static final int INDEXING_REPORT_STEP = 500;

    private static final String DATA_PATH = "data/songs/";

    private static final LinesIndexer INSTANCE;

    public static LinesIndexer getInstance() {
        return INSTANCE;
    }

    private static final Logger LOGGER;

    static {
        LOGGER = LogManager.getLogger(LinesIndexer.class);
        INSTANCE = new LinesIndexer(DATA_PATH, "index/");
    }


    private IndexWriter writer;

    private Analyzer analyzer;

    private String dataPath;

    private LinesIndexSearcher searcher;

    private IndexWriterConfig rebuildConfig() {
        return new IndexWriterConfig(analyzer);
    }

    private LinesIndexer(final String songsPath, final String indexPath) {

        try {
            final Directory directory = new SimpleFSDirectory(Paths.get(indexPath));
            dataPath = songsPath;
            analyzer = new StandardAnalyzer();
            final IndexWriterConfig conf = rebuildConfig();
            writer = new IndexWriter(directory, conf);

            // ребилдим каждый раз, всё равно сейчас это быстро
            writer.deleteAll();
            rebuild();
            searcher = new LinesIndexSearcher(directory);
        } catch (final IOException ioe) {

            LOGGER.warn("Need index rebuilding");

            try {
                rebuild();
            } catch (final IOException ioee) {
                LOGGER.error("Index dead", ioee);
                throw new RuntimeException("Всему конец, индекс не поднялся", ioee);
            }
        }
    }

    private void withWriter(final Consumer<IndexWriter> t) throws IOException {
        try {
            t.accept(writer);
        } finally {
            writer.commit();
            writer.forceMerge(1);
        }
    }

    public List<FoundGopSong> search(final String request) {
        try {
            return searcher.search(request);
        } catch (final IOException ioe) {
            LOGGER.error("ERRORE WHILE SEARCHE", ioe);
            return new ArrayList<>();
        }
    }

    private void rebuild() throws IOException {

        final int size = SongsUtils.listSongFilesByDir(DATA_PATH).collect(Collectors.toList()).size();
        final Iterator<GopSong> gopSongIterator = SongsUtils.listSongsByDir(dataPath).iterator();

        LOGGER.info("Starting indexing!");

        withWriter(wr -> {

            int counter = 1;

            while (gopSongIterator.hasNext()) {

                final GopSong song = gopSongIterator.next();

                if (counter % INDEXING_REPORT_STEP == 0) {
                    LOGGER.info("Indexed songs: " + counter + " / " + size);
                }
                counter += 1;

                for (final String line : song.getLyrics()) {

                    final String processedLine = BasicPreprocessor.postfix(line);

                    if (processedLine == null) {
                        // в строке какая-нибудь ерунда, например * * * (как в стихах без названия)
                        continue;
                    }

                    final Document doc = new Document();

                    doc.add(new StringField("text",
                            processedLine
                                    .substring(0,
                                            Math.min(ANALYZED_POSTFIX_SIZE, processedLine.length())),
                            Field.Store.YES));

                    doc.add(new StringField("fulltext", line, Field.Store.YES));
                    doc.add(new StringField("title", song.getName(), Field.Store.YES));
                    doc.add(new StringField("author", song.getAuthor(), Field.Store.YES));

                    try {
                        wr.addDocument(doc);
                    } catch (final IOException e) {
                        LOGGER.error("Bullshit while adding docs to index", e);
                        throw new RuntimeException("Bullshit while adding docs to index", e);
                    }
                }
            }

        });
    }
}
