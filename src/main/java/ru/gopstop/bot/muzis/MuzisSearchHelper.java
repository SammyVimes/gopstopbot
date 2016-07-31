package ru.gopstop.bot.muzis;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import ru.gopstop.bot.muzis.entity.Performer;
import ru.gopstop.bot.muzis.entity.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Semyon on 31.07.2016.
 */
public class MuzisSearchHelper {

    protected MuzisService muzisService = MuzisServiceBuilder.getMuzisService();

    protected ResourcesService resourcesService = MuzisServiceBuilder.getResourcesService();

    public List<Performer> searchByPerformer(final String performer) {
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
    }

    public boolean checkPerformer(final Performer performer, final String performerName) {
        final String performerTitle = performer.getTitle();

        final List<String> performerAsList = Arrays.asList(performerName.split(" "));
        Collections.reverse(performerAsList);
        final String performerReversed = performerAsList.stream().collect(Collectors.joining(" "));
        return performerTitle.contains(performerName) || performerName.contains(performerTitle)
                || performerTitle.contains(performerReversed) || performerReversed.contains(performerTitle);
    }

}
