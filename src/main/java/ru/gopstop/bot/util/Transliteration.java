package ru.gopstop.bot.util;

import java.util.LinkedHashMap;

/**
 * Очень тупая озвучка фром май харт
 * Created by aam on 05.08.16.
 */
public final class Transliteration {

    private static final LinkedHashMap<String, String> TRANSLIT_MAP;

    static {
        TRANSLIT_MAP = new LinkedHashMap<>();
        TRANSLIT_MAP.put("fuck", "фак");
        TRANSLIT_MAP.put("ck", "к");
        TRANSLIT_MAP.put("ch", "ч");
        TRANSLIT_MAP.put("sh", "ш");
        TRANSLIT_MAP.put("oo", "у");
        TRANSLIT_MAP.put("th", "с");
        TRANSLIT_MAP.put("e$", "");
        TRANSLIT_MAP.put("ay", "эй");
        TRANSLIT_MAP.put("ght", "т");

        final String lat = "qwertyuiopasdfghjklzxcvbnm";
        final String sound = "кв,в,э,р,т,и,у,и,о,п,а,с,д,ф,г,х,дж,к,л,з,кс,к,в,б,н,м";

        int i = 0;
        for (final String r : sound.split(",")) {
            TRANSLIT_MAP.put(lat.charAt(i) + "", r);
            i += 1;
        }
    }

    public static String fixEnglishWord(final String latinWord) {
        String result = latinWord;

        for (final String key : TRANSLIT_MAP.keySet()) {
            result = result.replaceAll(key, TRANSLIT_MAP.get(key));
        }
        return result;
    }

    private Transliteration() {}
}
