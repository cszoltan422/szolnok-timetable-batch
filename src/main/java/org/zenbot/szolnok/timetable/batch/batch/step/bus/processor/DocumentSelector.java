package org.zenbot.szolnok.timetable.batch.batch.step.bus.processor;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentSelector {

    public String getText(Document htmlDocument, String selector) {
        return htmlDocument.select(selector).text();
    }
}
