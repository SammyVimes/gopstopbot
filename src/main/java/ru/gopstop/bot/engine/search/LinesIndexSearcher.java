package ru.gopstop.bot.engine.search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import ru.gopstop.bot.engine.entities.GopSong;
import ru.gopstop.bot.engine.search.preprocessing.BasicPreprocessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Поиск рифм
 * Created by aam on 31.07.16.
 */
class LinesIndexSearcher {

    private static final Logger LOGGER = LogManager.getLogger(LinesIndexSearcher.class);

    private static final int COUNT_RETURNED = 400;

    private static final int ANALYZED_POSTFIX_LENGTH = 7;

    private static final int LOGGED_TOP = 5;

    private final IndexSearcher is;

    LinesIndexSearcher(final Directory dir) throws IOException {

        final IndexReader ir = DirectoryReader.open(dir);
        is = new IndexSearcher(ir);
    }

    public List<FoundGopSong> search(final String request) throws IOException {

        try {
            final BooleanQuery q = new BooleanQuery();
            final String processedRequest = BasicPreprocessor.postfix(request, true);

            LOGGER.info("REQ2POSTFIX\t" + request.replaceAll("\t", " ") + "\t" + processedRequest);

            if (processedRequest == null || processedRequest.isEmpty()) {
                LOGGER.warn("Processed request is empty all of a sudden: [" + processedRequest + "]");
                return Collections.emptyList();
            }

            // тупая комбинация префиксных запросов
            for (int i = ANALYZED_POSTFIX_LENGTH; i > 0; i--) {
                q.add(new PrefixQuery(
                                new Term("text", processedRequest.substring(0, Math.min(i, processedRequest.length())))),
                        BooleanClause.Occur.SHOULD);
            }

            final TopDocs docs = is.search(q, COUNT_RETURNED);
            final List<FoundGopSong> foundSongs = new ArrayList<>();

            LOGGER.info("REQUEST: [" + request + "] hits " + docs.totalHits);
            LOGGER.info("QUERY: [" + q.toString() + "]");

            for (int i = 0; i < Math.min(COUNT_RETURNED, docs.totalHits); i++) {

                final Document doc = is.doc(docs.scoreDocs[i].doc);

                if (i <= LOGGED_TOP) {
                    LOGGER.trace(docs.scoreDocs[i].score + "\t|\t" + doc.get("text") + "\t|\t" + doc.get("fulltext"));
                }

                foundSongs.add(
                        new FoundGopSong(
                                new GopSong(
                                        doc.get("title"),
                                        doc.get("author"), null),
                                doc.get("fulltext"),
                                docs.scoreDocs[i].score));

            }

            return foundSongs;
        } catch (Exception e) {
            LOGGER.error("Weird search error", e);
            return Collections.emptyList();
        }
    }
}
