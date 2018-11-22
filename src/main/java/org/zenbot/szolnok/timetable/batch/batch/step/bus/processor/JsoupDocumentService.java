package org.zenbot.szolnok.timetable.batch.batch.step.bus.processor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsoupDocumentService {

    public Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
