package org.zenbot.szolnok.timetable.batch.step.bus.processor;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableSelectorProperties;

@Component
public class StartBusStopSelectorItemProcessorHelper {

    private final StringCleaner stringCleaner;

    public StartBusStopSelectorItemProcessorHelper(StringCleaner stringCleaner) {
        this.stringCleaner = stringCleaner;
    }

    public String getStartBusStop(Document htmlDocument, TimetableSelectorProperties selectorProperties) {
        return stringCleaner.clean(htmlDocument.select(selectorProperties.getFromSelector()).text());
    }
}
