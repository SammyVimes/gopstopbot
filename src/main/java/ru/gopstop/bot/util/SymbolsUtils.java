package ru.gopstop.bot.util;

/**
 * Created by aam on 02.08.16.
 */
public final class SymbolsUtils {

    //todo: compiled regex
    private static final String USELESS_SYM_PATTERN = "[^A-Za-zА-Яа-яё ]";

    public static String replaceUseless(final String what, final String replacement) {
        return what.replaceAll(USELESS_SYM_PATTERN, replacement);
    }

    private SymbolsUtils() {

    }
}
