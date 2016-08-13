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
import ru.gopstop.bot.engine.search.LinesIndexer;
import ru.gopstop.bot.telegram.user.TGSession;
import ru.gopstop.bot.telegram.user.TGSessionKey;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Хранилище сессий. По-хорошему, надо в базу писать, конечно
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
    }

    private IndexWriter writer;

    private Analyzer analyzer;

    private SessionSearcher searcher;

    private SessionCache(final String indexPath) {

        LOGGER.info("INit sessions cache");
        try {
            final Directory directory = new SimpleFSDirectory(Paths.get(indexPath));
            analyzer = new StandardAnalyzer();
            final IndexWriterConfig conf = rebuildConfig();
            writer = new IndexWriter(directory, conf);
            LOGGER.info("Init searcher");
            searcher = new SessionSearcher(directory);
        } catch (final IOException ioe) {

            LOGGER.error("Need cache rebuilding, all session go to hell", ioe);

            try {
                writer.deleteAll();
            } catch (final IOException ioee) {
                LOGGER.error("Cace dead", ioee);
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


    public List<String> search(final TGSessionKey request) {
        try {
            LOGGER.info("" + searcher);
            return searcher.search(request);
        } catch (final IOException ioe) {
            LOGGER.error("ERRORE WHILE SEARCHE", ioe);
            return new ArrayList<>();
        }
    }

    public void updateSession(final TGSession session) {

        final BooleanQuery bq = new BooleanQuery();
        bq.add(
                new TermQuery(
                        new Term("session_key", session.getSessionKey().hashCode() + "")),
                BooleanClause.Occur.MUST);

        final Document doc = new Document();

        doc.add(new StringField("controller", session.getLastController(), Field.Store.YES));
        doc.add(new StringField("session_key", session.getSessionKey().hashCode() + "", Field.Store.YES));
        doc.add(new StringField("chat_id", session.getChatId() + "", Field.Store.YES));
        doc.add(new StringField("user_id", session.getUser().getId() + "", Field.Store.YES));

        try {
            withWriter(wr -> {
                        try {
                            wr.deleteDocuments(bq);
                            wr.addDocument(doc);
                        } catch (final IOException e) {
                            LOGGER.error("Bullshit while adding docs to index", e);
//                            throw new RuntimeException("Bullshit while adding docs to index", e);
                        }
                    }
            );
        } catch (final IOException e) {
            LOGGER.error("Bullshit while adding docs to index", e);
//            throw new RuntimeException("Bullshit while adding docs to index", e);
        }
    }
}
