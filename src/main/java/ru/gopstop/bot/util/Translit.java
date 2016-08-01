package ru.gopstop.bot.util;

import static java.lang.Character.toUpperCase;

/**
 * Обратимая транслитерация кириллицы в латиницу
 */
public final class Translit {

    private static String cyr2lat(final char ch) {

        switch (ch) {
            case 'А':
                return "A";
            case 'Б':
                return "B";
            case 'В':
                return "V";
            case 'Г':
                return "G";
            case 'Д':
                return "D";
            case 'Е':
                return "E";
            case 'Ё':
                return "Jo";
            case 'Ж':
                return "Zh";
            case 'З':
                return "Z";
            case 'И':
                return "I";
            case 'Й':
                return "Y";
            case 'К':
                return "K";
            case 'Л':
                return "L";
            case 'М':
                return "M";
            case 'Н':
                return "N";
            case 'О':
                return "O";
            case 'П':
                return "P";
            case 'Р':
                return "R";
            case 'С':
                return "S";
            case 'Т':
                return "T";
            case 'У':
                return "U";
            case 'Ф':
                return "F";
            case 'Х':
                return "Kh";
            case 'Ц':
                return "C";
            case 'Ч':
                return "Ch";
            case 'Ш':
                return "Sh";
            case 'Щ':
                return "Shh";
            case 'Ъ':
                return "Jhh";
            case 'Ы':
                return "Ih";
            case 'Ь':
                return "Jh";
            case 'Э':
                return "Eh";
            case 'Ю':
                return "Ju";
            case 'Я':
                return "Ja";
            default:
                return String.valueOf(ch);
        }
    }

    public static String cyr2lat(final String s) {

        final StringBuilder sb = new StringBuilder(s.length() * 2);

        for (char ch : s.toCharArray()) {
            final char upCh = toUpperCase(ch);
            String lat = cyr2lat(upCh);

            if (ch != upCh) {
                lat = lat.toLowerCase();
            }
            sb.append(lat);
        }
        return sb.toString();
    }

    /**
     * Вспомогательная функция для восстановления регистра
     */
    private static char ch(final char ch, final boolean toLowerCase) {
        return toLowerCase ? Character.toLowerCase(ch) : ch;
    }

    private Translit() {

    }
}
