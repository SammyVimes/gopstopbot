package ru.gopstop.bot.engine.search;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

/**
 * Токенизатор для обеспечения индексации малых символьных нграмм
 * Created by aam on 30.07.16.
 */
public class SymNGramWrappingTokenFilter extends TokenFilter {

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    public SymNGramWrappingTokenFilter(final TokenStream tokenStream) {
        super(tokenStream);
    }

    @Override
    public final boolean incrementToken() throws IOException {
        if (input.incrementToken()) {
                final String term = " " + new String(termAtt.buffer(), 0, termAtt.length()) + " ";
                termAtt.copyBuffer(term.toCharArray(), 0, term.length());
                termAtt.setLength(term.length());
            return true;
        } else {
            return false;
        }
    }
}
