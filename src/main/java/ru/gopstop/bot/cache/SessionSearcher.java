package ru.gopstop.bot.cache;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import ru.gopstop.bot.telegram.user.TGSessionKey;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by aam on 13.08.16.
 */
public class SessionSearcher {

    private static final int COUNT_RETURNED = 3;

    private final IndexSearcher is;

    SessionSearcher(final Directory dir) throws IOException {

        final IndexReader ir = DirectoryReader.open(dir);
        is = new IndexSearcher(ir);
    }

    /**
     * Поиск состояния пользовательской сессии в индексе
     */
    public List<String> search(final TGSessionKey request) throws IOException {

        final BooleanQuery q = new BooleanQuery();
        final TermQuery tq = new TermQuery(new Term("session_key", request.hashCode() + ""));
        q.add(tq, BooleanClause.Occur.MUST);
        final TopDocs docs = is.search(q, COUNT_RETURNED);

        if (docs.totalHits == 0) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(is.doc(docs.scoreDocs[0].doc).get("controller"));
        }
    }
}
