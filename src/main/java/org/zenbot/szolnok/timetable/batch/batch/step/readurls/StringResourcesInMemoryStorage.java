package org.zenbot.szolnok.timetable.batch.batch.step.readurls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class StringResourcesInMemoryStorage {

    private final List<String> urls;

    public StringResourcesInMemoryStorage() {
        this.urls = new LinkedList<>();
    }

    public void addUrl(String url) {
        log.info("Adding url to in memory storage url=[{}]", url);
        if (url != null) {
            urls.add(url);
        }
    }

    public String get() {
        if (urls.size() == 0) {
            log.info("Urls are empty! Returning null");
            return null;
        }
        String url = urls.get(0);
        urls.remove(0);
        return url;
    }
}
