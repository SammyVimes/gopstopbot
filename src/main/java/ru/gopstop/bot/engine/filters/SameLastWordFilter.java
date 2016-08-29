package ru.gopstop.bot.engine.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.engine.search.FoundGopSong;
import ru.gopstop.bot.engine.stress.WordStressMap;
import ru.gopstop.bot.util.SymbolsUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Отбрасываем тексты с таким же последним словом
 * <p/>
 * Created by aam on 31.07.16.
 */
public final class SameLastWordFilter {

    private static final Logger LOGGER = LogManager.getLogger(SameLastWordFilter.class);

    /**
     * Дело в том, что это обычно то же слово с приставкой
     */
    private static final int UNACC_POSTFIX = 3;

    private static final List<String> PREFIXES =
            Arrays.asList(
                    // longer go first
                    "сверх", "пере", "анти", "разъ", "подъ", "полу", "надо", "недо", "архи", "иеро", "подо",
                    "при", "про", "над", "обо", "бес", "раз", "без", "рас", "под", "пол", "взо",
                    "об", "из", "ис", "за", "въ", "съ", "не", "во", "со", "от", "до", "по", "на",
                    "о", "а", "c", "с", "в", "у");

    private static List<String> buildLexemList(final String request) {
        return Arrays.asList(
                SymbolsUtils
                        .replaceUseless(request, " ")
                        .toLowerCase()
                        .replaceAll("ё", "е")
                        .split("\\s+"));
    }

    public static boolean filter(final String request, final FoundGopSong gopSong) {

        final List<String> reqLL = buildLexemList(request);
        final List<String> reqR = buildLexemList(gopSong.getRhyme());

        final String lastGop = WordStressMap.fixYo(reqR.get(reqR.size() - 1));
        final String lastUser = WordStressMap.fixYo(reqLL.get(reqLL.size() - 1));

        // одинаковые не пропускаем
        if (lastGop.equals(lastUser)) {
            LOGGER.trace("Same word, discarding:\t" + lastGop + "\t" + lastUser + "\t// " + gopSong.getGopSong().getName());
            return false;
//        } else {
//            LOGGER.debug("NOT Same word, NOT discarding:\t" + lastGop + "\t" + lastUser + "\t// " + gopSong.getGopSong().getName());
        }

        // отличающиеся приставкой не пропускаем
        if (lastGop.length() >= UNACC_POSTFIX && lastUser.length() >= UNACC_POSTFIX) {

            final int lastGopLength = lastGop.length();
            final int lastUserLength = lastUser.length();

            final String gopPosttfix = lastGop.substring(lastGop.length() - UNACC_POSTFIX, lastGopLength);
            final String inputPostfix = lastUser.substring(lastUser.length() - UNACC_POSTFIX, lastUserLength);

            if (gopPosttfix.equals(inputPostfix)) {

                LOGGER.trace("Likely same base words:\t["
                        + lastGop + "]\t["
                        + lastUser + "] // "
                        + gopSong.getGopSong().getName());

                for (final String gopPr : PREFIXES) {

                    if (lastGop.startsWith(gopPr) && lastGop.length() > gopPr.length()) {

                        LOGGER.trace("Probable prefix: " + lastGop + " " + gopPr + " " + lastUser);

                        final int gopBaseLength = lastGop.length() - gopPr.length();

                        LOGGER.trace("gopbaselength: " + gopBaseLength + " " + lastGop + " " + gopPr + " " + lastUser);

                        for (final String userPr : PREFIXES) {

                            // если начинается на приставку и по длине подходит
                            if (userPr.startsWith(userPr)
                                    && lastUser.length() > userPr.length()) {

                                final int userBaseLength = lastUser.length() - userPr.length();
                                final boolean lengthsAcceptable =
                                        gopBaseLength == userBaseLength
                                                || lastUserLength == gopBaseLength
                                                || lastGopLength == userBaseLength;

                                // если разница по длине совсем бредовая, продолжаем дальше
                                if (!lengthsAcceptable) {
                                    LOGGER.trace("Lenths not acceptable "
                                            + lastGop + " " + lastUser + " " + gopPr + " " + userPr);
                                    continue;
                                }

                                // основы
                                final String subUser = lastUser.substring(userPr.length(), lastUserLength);
                                final String subGop = lastGop.substring(gopPr.length(), lastGopLength);

                                if (lastGop.equals(subUser) || subGop.equals(subUser) || lastUser.equals(subGop)) {

                                    LOGGER.trace("Skipping words as однокоренные "
                                            + lastGop + " " + lastUser + " "
                                            + subGop + " " + lastUser);
                                    return false;
                                }
                            }
                        } // end for user pr

                        // если у запроса нет приставки

                            final int userBaseLength = lastUser.length();
                            final boolean lengthsAcceptable =
                                    gopBaseLength == userBaseLength
                                            || lastUserLength == gopBaseLength
                                            || lastGopLength == userBaseLength;

                            // если разница по длине совсем бредовая, продолжаем дальше
                            if (!lengthsAcceptable) {
                                LOGGER.trace("Lenths not acceptable "
                                        + lastGop + " " + lastUser + " " + gopPr);
                                continue;
                            }

                            // основы
                            final String subUser = lastUser;
                            final String subGop = lastGop.substring(gopPr.length(), lastGopLength);

                            if (lastGop.equals(subUser) || subGop.equals(subUser) || lastUser.equals(subGop)) {

                                LOGGER.trace("Skipping words as однокоренные "
                                        + lastGop + " " + lastUser + " "
                                        + subGop + " " + lastUser);
                                return false;
                            }
                    } // end if gop matched
                }//end for gop


                // если не встретили приставки для шансона
                //todo: remove code duplicates
                LOGGER.trace("Gop has no prefix? " + lastGop);

                final int gopBaseLength = lastGop.length();

                LOGGER.trace("gopbaselength: " + gopBaseLength + " " + lastGop + lastUser);

                for (final String userPr : PREFIXES) {

                    // если начинается на приставку и по длине подходит
                    if (userPr.startsWith(userPr)
                            && lastUser.length() > userPr.length()) {

                        final int userBaseLength = lastUser.length() - userPr.length();
                        final boolean lengthsAcceptable =
                                gopBaseLength == userBaseLength
                                        || lastUserLength == gopBaseLength
                                        || lastGopLength == userBaseLength;

                        // если разница по длине совсем бредовая, продолжаем дальше
                        if (!lengthsAcceptable) {
                            LOGGER.trace("Lenths not acceptable "
                                    + lastGop + " " + lastUser +  " " + userPr);
                            continue;
                        }

                        // основы
                        final String subUser = lastUser.substring(userPr.length(), lastUserLength);
                        final String subGop = lastGop;

                        if (lastGop.equals(subUser) || subGop.equals(subUser) || lastUser.equals(subGop)) {

                            LOGGER.trace("Skipping words as однокоренные "
                                    + lastGop + " " + lastUser + " "
                                    + subGop + " " + lastUser);
                            return false;
                        }
                    }
                } // end for user pr
            }
        }

        return true;
    }

    private SameLastWordFilter() {

    }
}
