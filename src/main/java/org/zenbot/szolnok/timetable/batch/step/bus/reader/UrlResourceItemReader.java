package org.zenbot.szolnok.timetable.batch.step.bus.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import org.zenbot.szolnok.timetable.batch.step.readurls.StringResourcesInMemoryStorage;

@Slf4j
@Component
public class UrlResourceItemReader implements ItemReader<String> {

    private final StringResourcesInMemoryStorage stringResourcesInMemoryStorage;

    public UrlResourceItemReader(StringResourcesInMemoryStorage stringResourcesInMemoryStorage) {
        this.stringResourcesInMemoryStorage = stringResourcesInMemoryStorage;
    }

    @Override
    public String read() {
        log.info("Reading next item from [UrlResouceInMemoryStorage]");
        return stringResourcesInMemoryStorage.get();
    }
}
