package org.zenbot.szolnok.timetable.batch.step.bus.processor;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class JsoupDocumentService {

    private static final int RETRY_COUNT = 4;

    public Document getDocument(String url) {
        Document result = null;
        int i = 0;
        while (i <= RETRY_COUNT) {
            try  {
                result = Jsoup.connect(url).get();
                break;
            } catch (IOException e) {
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
