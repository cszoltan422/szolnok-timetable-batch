package org.zenbot.szolnok.timetable.batch.step.bus.processor;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.zenbot.szolnok.timetable.configuration.properties.TimetableSelectorProperties;

@Component
public class ActualStopSelectorItemProcessorHelper {

    private final StringCleaner stringCleaner;

    public ActualStopSelectorItemProcessorHelper(StringCleaner stringCleaner) {
        this.stringCleaner = stringCleaner;
    }

    public String getActualStop(Document htmlDocument, TimetableSelectorProperties selectorProperties) {
        String actualStopSelector = selectorProperties.getActualStopSelector();
        String actualStop = htmlDocument.select(actualStopSelector).text();
        // In the html page they put it insede () signs.
        actualStop = (String) actualStop.subSequence(actualStop.indexOf("(") + 1, actualStop.indexOf(")"));
        if (actualStop.contains("(")) {
            actualStop += ")";
        }
        if (actualStop.endsWith(".")) {
            actualStop = actualStop.substring(0, actualStop.length() - 1);
        }
        return stringCleaner.clean(actualStop);
    }
}
