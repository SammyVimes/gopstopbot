package ru.gopstop.bot.engine.search;

import junit.framework.TestCase;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Ignore;
import org.junit.Test;
import ru.gopstop.bot.engine.search.preprocessing.BasicPreprocessor;
import ru.gopstop.bot.engine.tools.SongsUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by aam on 30.07.16.
 */
public class RhymesHypothesisTest extends TestCase {

    @Test
    @Ignore
    public void testSelfContainedHypo() throws IOException {

//        final Directory directory = new RAMDirectory();
//        final Tokenizer tokenizer = new LetterTokenizer();
//        final CustomAnalyzer customAnalyzer = new CustomAnalyzer(tokenizer, 2, 3);
//        final IndexWriterConfig conf = new IndexWriterConfig(customAnalyzer);
//        final IndexWriter writer = new IndexWriter(directory, conf);
//
//        int num = 0;
//
//        List<String> testLines =
//                SongsUtils.listSongsByDir("data/songs/")
//                        .limit(1000)
//                        .flatMap(s -> s.getLyrics().stream())
//                        .collect(Collectors.toList());
//
//        System.out.println(testLines.toString());
//
//        for (String line : testLines) {
//
//            String processedLine = BasicPreprocessor.postfix(line, true);
//
//            System.out.println(processedLine);
//
//            final Document doc = new Document();
//            doc.add(new TextField("text", processedLine.substring(0, Math.min(6, processedLine.length())), Field.Store.YES));
//            doc.add(new StringField("fulltext", line, Field.Store.YES));
//            doc.add(new IntField("id", num, Field.Store.YES));
//            writer.addDocument(doc);
//        }
//
//        writer.close();
//
//        final IndexReader ir = DirectoryReader.open(writer.getDirectory());
//        final IndexSearcher is = new IndexSearcher(ir);
//
//        final BooleanQuery q = new BooleanQuery();
//        final String request =
//                BasicPreprocessor.postfix("мы подошли из-за угла", true);
//
//        for (String token : customAnalyzer.handle(request)) {
//            System.out.print(token + " ");
//            q.add(new TermQuery(new Term("text", token)), BooleanClause.Occur.SHOULD);
//        }
//
//        System.out.println();
//
//        int pageSize = 10;
//
//        final TopDocs docs = is.search(q, pageSize);
//
//        System.out.println("REQUEST: [" + BasicPreprocessor.postfix(request, true) + "]");
//
//        for (int i = 0; i < Math.min(pageSize, docs.totalHits); i++) {
//            System.out.println();
//            System.out.println(is.doc(docs.scoreDocs[i].doc).get("fulltext"));
//            System.out.println(is.doc(docs.scoreDocs[i].doc).get("text"));
//            System.out.println(is.doc(docs.scoreDocs[i].doc).get("author"));
//            System.out.println(is.doc(docs.scoreDocs[i].doc).get("title"));
//            System.out.println(docs.scoreDocs[i].score);
//        }
//
//
    }
}
