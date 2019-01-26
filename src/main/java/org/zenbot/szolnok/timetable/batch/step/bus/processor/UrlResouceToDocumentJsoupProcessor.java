package org.zenbot.szolnok.timetable.batch.step.bus.processor;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UrlResouceToDocumentJsoupProcessor implements ItemProcessor<String, Document> {

    private final JsoupDocumentService jsoupDocumentService;

    public UrlResouceToDocumentJsoupProcessor(JsoupDocumentService jsoupDocumentService) {
        this.jsoupDocumentService = jsoupDocumentService;
    }

    @Override
    public Document process(String url) {
        log.info("Process url=[{}]", url);
         return jsoupDocumentService.getDocument(url);
    }
}
