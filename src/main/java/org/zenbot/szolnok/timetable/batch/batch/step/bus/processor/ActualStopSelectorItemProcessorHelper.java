package org.zenbot.szolnok.timetable.batch.batch.step.bus.processor;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class ActualStopSelectorItemProcessorHelper {

    private final StringCleaner stringCleaner;

    public ActualStopSelectorItemProcessorHelper(StringCleaner stringCleaner) {
        this.stringCleaner = stringCleaner;
    }

    public String getActualStop(Document htmlDocument, String actualStopSelector) {
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
