package ru.gopstop.bot.engine.search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import ru.gopstop.bot.telegram.TGBot;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by aam on 30.07.16.
 */
public class LinesIndexer {

    final IndexWriter writer;

    final Directory directory;

    private final Logger LOGGER = LogManager.getLogger(TGBot.class);

    public LinesIndexer(final String songsPath, final String indexPath) {

        try {
            directory = new SimpleFSDirectory(Paths.get(indexPath));
            final Tokenizer tokenizer = new LetterTokenizer();
            final CustomAnalyzer customAnalyzer = new CustomAnalyzer(tokenizer, 2, 3);
            final IndexWriterConfig conf = new IndexWriterConfig(customAnalyzer);
            writer = new IndexWriter(directory, conf);

        } catch (final IOException ioe) {

            throw new RuntimeException("Всему конец, индекс не поднялся");
        }
    }

    private void rebuild() {

    }
}
