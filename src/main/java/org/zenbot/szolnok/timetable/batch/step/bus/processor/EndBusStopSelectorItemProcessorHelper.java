package org.zenbot.szolnok.timetable.batch.step.bus.processor;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableSelectorProperties;

@Component
public class EndBusStopSelectorItemProcessorHelper {

    private final StringCleaner stringCleaner;

    public EndBusStopSelectorItemProcessorHelper(StringCleaner stringCleaner) {
        this.stringCleaner = stringCleaner;
    }

    public String getEndBusStop(Document htmlDocument, TimetableSelectorProperties selectorProperties) {
        Elements stationsTable = htmlDocument.select(selectorProperties.getBusStopsSelector());
        int indexOfEndBusStop = stationsTable.select(selectorProperties.getTableRowSelector()).size() - 2;
        Elements rows = stationsTable.select(selectorProperties.getTableRowSelector());
        String to = rows.get(indexOfEndBusStop).select(selectorProperties.getTableColumnSelector()).get(2).text();
        return  stringCleaner.clean(to);
    }
}
