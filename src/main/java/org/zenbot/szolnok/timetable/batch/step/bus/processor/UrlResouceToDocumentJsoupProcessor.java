package org.zenbot.szolnok.timetable.batch.step.bus.processor;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;

@Slf4j
@Component
public class UrlResouceToDocumentJsoupProcessor implements ItemProcessor<String, Document> {

    private final JsoupDocumentService jsoupDocumentService;

    public static final int RETRY_COUNT = 4;

    public UrlResouceToDocumentJsoupProcessor(JsoupDocumentService jsoupDocumentService) {
        this.jsoupDocumentService = jsoupDocumentService;
    }

    @Override
    public Document process(String url) throws Exception {
        log.info("Process url=[{}]", url);
        Document result = null;
        int i = 0;
        while (i <= RETRY_COUNT) {
            try  {
                result = jsoupDocumentService.getDocument(url);
                break;
            } catch (SocketTimeoutException e) {
                log.debug("Read timed out [{}]", url);
                log.debug("Retry last operation for the [{}] time", i + 1);
            }
            i++;
        }
        if (result == null) {
            throw new IllegalStateException(String.format("Cannot fetch documet=[%s]", url));
        }
        return result;
    }
}
