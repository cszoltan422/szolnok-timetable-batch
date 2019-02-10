package org.zenbot.szolnok.timetable.batch.utils.common.batch.reader

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemReader
import org.zenbot.szolnok.timetable.batch.utils.common.service.StringResourcesInMemoryStorage

class UrlResourceItemReader(private val stringResourcesInMemoryStorage: StringResourcesInMemoryStorage) : ItemReader<String> {

    private val log = LoggerFactory.getLogger(UrlResourceItemReader::class.java)

    override fun read(): String? {
        log.info("Reading next item from [UrlResouceInMemoryStorage]")
        return stringResourcesInMemoryStorage.get()
    }
}
