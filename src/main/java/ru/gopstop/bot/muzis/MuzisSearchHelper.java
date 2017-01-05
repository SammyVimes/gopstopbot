package ru.gopstop.bot.muzis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gopstop.bot.muzis.entity.Performer;
import ru.gopstop.bot.muzis.entity.SearchResult;
import ru.gopstop.bot.telegram.controller.RhymingController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Semyon on 31.07.2016.
 */
public class MuzisSearchHelper {

    private MuzisService muzisService = MuzisServiceBuilder.getMuzisService();

    private static final Logger LOGGER = LogManager.getLogger(RhymingController.class);

    public List<Performer> searchByPerformer(final String performer) {

        try {

            final SearchResult search = muzisService.search(null, performer, null, null, null, null, null);

            final List<String> performerAsList = Arrays.asList(performer.split(" "));
            Collections.reverse(performerAsList);

            final String performerReversed = performerAsList.stream().collect(Collectors.joining(" "));
            final SearchResult search1 = muzisService.search(null, performerReversed, null, null, null, null, null);

            List<Performer> result = new ArrayList<>();

            if (search.getPerformers() != null) {
                result.addAll(search.getPerformers());
            }

            if (search1.getPerformers() != null) {
                result.addAll(search1.getPerformers());
            }

            return result;
        } catch (final NullPointerException npe) {
            LOGGER.error("NPE when calling to muzis " + npe.getMessage() + " moving on");
            return Collections.emptyList();
        }
    }

    public boolean checkPerformer(final Performer performer, final String performerName) {

        final String performerTitle = performer.getTitle();
        final List<String> performerAsList = Arrays.asList(performerName.split(" "));
        Collections.reverse(performerAsList);

        final String performerReversed = performerAsList.stream().collect(Collectors.joining(" "));

        return performerTitle.contains(performerName)
                || performerName.contains(performerTitle)
                || performerTitle.contains(performerReversed)
                || performerReversed.contains(performerTitle);
    }
}
