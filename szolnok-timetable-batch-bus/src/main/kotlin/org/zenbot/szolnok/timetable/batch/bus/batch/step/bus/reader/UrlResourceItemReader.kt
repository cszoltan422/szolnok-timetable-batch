package org.zenbot.szolnok.timetable.batch.bus.batch.step.bus.reader

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.batch.bus.batch.step.readurls.StringResourcesInMemoryStorage

@Component
class UrlResourceItemReader(private val stringResourcesInMemoryStorage: StringResourcesInMemoryStorage) : ItemReader<String> {

    private val log = LoggerFactory.getLogger(UrlResourceItemReader::class.java)

    override fun read(): String? {
        log.info("Reading next item from [UrlResouceInMemoryStorage]")
        return stringResourcesInMemoryStorage.get()
    }
}
