package ru.gopstop.bot.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import ru.gopstop.bot.telegram.user.TGSession;
import ru.gopstop.bot.telegram.user.TGSessionKey;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Хранилище сессий. По-хорошему, надо в базу писать, конечно,
 * в люсине держать сессии странно + неудобно переносить.
 * Так что TODO
 * <p>
 * Created by aam on 13.08.16.
 */
public final class SessionCache {

    private static final SessionCache INSTANCE;

    public static SessionCache getInstance() {
        return INSTANCE;
    }

    private static final Logger LOGGER;

    static {
        LOGGER = LogManager.getLogger(SessionCache.class);
        INSTANCE = new SessionCache("sessions/");
        LOGGER.info("Session cache set");
    }

    private IndexWriter writer;

    private Analyzer analyzer;

    private SessionSearcher searcher;

    private SessionCache(final String indexPath) {

        LOGGER.info("Init sessions cache");
        try {
            final Directory directory = new SimpleFSDirectory(Paths.get(indexPath));

            analyzer = new StandardAnalyzer();

            final IndexWriterConfig conf = rebuildConfig();
            writer = new IndexWriter(directory, conf);

            LOGGER.info("Init searcher");
            searcher = new SessionSearcher(directory);

            LOGGER.info("Inited searcher");
        } catch (final IOException ioe) {

            LOGGER.error("Need sessions cache rebuilding, all sessions go to hell", ioe);

            try {
                writer.deleteAll();
            } catch (final IOException ioee) {
                LOGGER.error("Cache  cleaning impossibru, dying", ioee);
                throw new RuntimeException("Всему конец, индекс не поднялся", ioee);
            }
        } catch (Exception e) {
            LOGGER.error("wtf", e);
        }
    }

    private IndexWriterConfig rebuildConfig() {
        return new IndexWriterConfig(analyzer);
    }

    private void withWriter(final Consumer<IndexWriter> t) throws IOException {
        try {
            t.accept(writer);
        } finally {
            writer.commit();
            writer.forceMerge(1);
        }
    }

    /**
     * Поиск в индексе по ключу сессии
     */
    public List<String> search(final TGSessionKey request) {

        try {
            final List<String> res = searcher.search(request);
            LOGGER.info("Found sessions in cache: " + res.size());
            return res;
        } catch (final IOException ioe) {
            LOGGER.error("IO ERROR WHILE SEARCHING", ioe);
            return new ArrayList<>();
        } catch (final Exception e) {
            LOGGER.error("UNKNOWN ERROR WHILE SEARCHING", e);
            return new ArrayList<>();
        }
    }

    /**
     * Обновление состояния пользовательской сессии в индексе
     */
    public void updateSession(final TGSession session) {

        final BooleanQuery bq = new BooleanQuery();
        bq.add(
                new TermQuery(
                        new Term("session_key", session.getSessionKey().hashCode() + "")),
                BooleanClause.Occur.MUST);

        final Document doc = new Document();

        doc.add(new StringField("controller", session.getLastController(), Field.Store.YES));
        doc.add(new StringField("session_key", session.getSessionKey().hashCode() + "", Field.Store.YES));

        try {
            withWriter(wr -> {
                        try {
                            wr.deleteDocuments(bq);
                            wr.addDocument(doc);
                        } catch (final IOException e) {
                            LOGGER.error("Bullshit while adding docs to index", e);
                        }
                    }
            );
        } catch (final IOException e) {
            LOGGER.error("Bullshit while adding docs to index", e);
        }
    }
}
