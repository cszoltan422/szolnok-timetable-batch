package org.zenbot.szolnok.timetable.batch.step.bus.processor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
        Elements rows = stationsTable.select(selectorProperties.getTableRowSelector());
        int indexOfEndBusStop = rows.size() - 2;
        Element lastRow = rows.get(indexOfEndBusStop);
        Elements lastRowColumns = lastRow.select(selectorProperties.getTableColumnSelector());
        Element lastColumn = lastRowColumns.get(2);
        String to = lastColumn.text();
        return  stringCleaner.clean(to);
    }
}
