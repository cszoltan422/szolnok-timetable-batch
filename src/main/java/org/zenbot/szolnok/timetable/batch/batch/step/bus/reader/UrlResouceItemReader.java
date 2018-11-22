package org.zenbot.szolnok.timetable.batch.batch.step.bus.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import org.zenbot.szolnok.timetable.batch.batch.step.readurls.StringResourcesInMemoryStorage;

import java.net.MalformedURLException;

@Slf4j
@Component
public class UrlResouceItemReader implements ItemReader<String> {

    private final StringResourcesInMemoryStorage stringResourcesInMemoryStorage;

    public UrlResouceItemReader(StringResourcesInMemoryStorage stringResourcesInMemoryStorage) {
        this.stringResourcesInMemoryStorage = stringResourcesInMemoryStorage;
    }

    @Override
    public String read() throws MalformedURLException {
        log.info("Reading next item from [UrlResouceInMemoryStorage]");
        return stringResourcesInMemoryStorage.get();
    }
}
