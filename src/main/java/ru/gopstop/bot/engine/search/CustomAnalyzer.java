package ru.gopstop.bot.engine.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class CustomAnalyzer extends Analyzer {

    final private Tokenizer wordTokenizer;
    final private int from;
    final private int to;

    public CustomAnalyzer(final Tokenizer tokenizer, final int from, final int to) {
        this.wordTokenizer = tokenizer;
        this.from = from;
        this.to = to;
    }

    @Override
    protected TokenStreamComponents createComponents(final String fieldName) {
        TokenStream stream = new StandardFilter(wordTokenizer);
        stream = new SymNGramWrappingTokenFilter(stream);
        stream = new NGramTokenFilter(stream, from, to);
        return new TokenStreamComponents(wordTokenizer, stream);
    }


    /**
     * Токенизация по требованию
     */
    public List<String> handle(final String query) {

        final String queryNosub = query.replaceAll("_", " ");
        final List<String> result = new ArrayList<String>();

        try (final TokenStream stream =
                     this.tokenStream(null, new StringReader(queryNosub))) {

            stream.reset();
            final CharTermAttribute termAttribute =
                    stream.getAttribute(CharTermAttribute.class);
            while (stream.incrementToken()) {
                result.add(termAttribute.toString());
            }
            stream.end();
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
