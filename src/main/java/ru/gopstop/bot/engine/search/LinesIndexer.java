package ru.gopstop.bot.engine.search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import ru.gopstop.bot.engine.search.preprocessing.BasicPreprocessor;
import ru.gopstop.bot.engine.tools.SongsUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by aam on 30.07.16.
 */
public class LinesIndexer {

    private static LinesIndexer INSTANCE;

    public static LinesIndexer getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new LinesIndexer("data_test/songs/", "index/");
    }

    private final Logger LOGGER = LogManager.getLogger(LinesIndexer.class);

    private IndexWriter writer;

    private Directory directory;

    private CustomAnalyzer analyzer;

    private LetterTokenizer tokenizer;

    private String dataPath;

    private LinesIndexSearcher searcher;

    private IndexWriterConfig rebuildConfig() {
        return new IndexWriterConfig(analyzer);
    }

    private LinesIndexer(final String songsPath, final String indexPath) {

        try {
            directory = new SimpleFSDirectory(Paths.get(indexPath));
            tokenizer = new LetterTokenizer();
            analyzer = new CustomAnalyzer(tokenizer, 2, 3);
            dataPath = songsPath;
            final IndexWriterConfig conf = rebuildConfig();
            writer = new IndexWriter(directory, conf);
            //todo remove
            writer.deleteAll();
            rebuild();
            searcher = new LinesIndexSearcher(directory, analyzer);

        } catch (final IOException ioe) {

            LOGGER.warn("Need index rebuild");

            try {
                rebuild();
            } catch (final IOException ioee) {
                LOGGER.error("Index dead");
                throw new RuntimeException("Всему конец, индекс не поднялся", ioee);
            }
        }
    }

    private void withWriter(Consumer<IndexWriter> t) throws IOException {
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
        } catch (IOException ioe) {
            LOGGER.error("ERRORE WHILE SEARCHE", ioe);
            return new ArrayList<>();
        }
    }

    private void rebuild() throws IOException {

        SongsUtils.listSongsByDir(dataPath)
                .forEach(song -> {
                            try {
                                withWriter(wr -> {

                                    for (final String line : song.getLyrics()) {

                                        final String processedLine = BasicPreprocessor.postfix(line);

                                        final Document doc = new Document();
                                        doc.add(new TextField("text", processedLine.substring(0, 5), Field.Store.YES));
                                        doc.add(new StringField("fulltext", line, Field.Store.YES));
                                        doc.add(new StringField("title", song.getName(), Field.Store.YES));
                                        doc.add(new StringField("author", song.getAuthor(), Field.Store.YES));

                                        System.out.println("INDEXING " + doc);

                                        try {
                                            wr.addDocument(doc);
                                        } catch (IOException e) {
                                            LOGGER.error("Bullshit while adding docs to index", e);
                                            e.printStackTrace();
                                            throw new RuntimeException("ppc", e);
                                        }
                                    }
                                });
                            } catch (IOException ioe) {
                                LOGGER.error("Bullshit while adding docs to index", ioe);
                                ioe.printStackTrace();
                                throw new RuntimeException("ppc", ioe);
                            }
                        }
                );
    }
}
