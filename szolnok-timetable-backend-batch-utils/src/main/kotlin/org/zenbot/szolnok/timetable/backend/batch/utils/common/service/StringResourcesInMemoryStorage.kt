package org.zenbot.szolnok.timetable.backend.batch.utils.common.service

import org.slf4j.LoggerFactory
import java.util.LinkedList

class StringResourcesInMemoryStorage {

    private val log = LoggerFactory.getLogger(StringResourcesInMemoryStorage::class.java)

    private val urls: MutableList<String> = LinkedList()

    fun addUrl(url: String?) {
        log.info("Adding url to in memory storage url=[{}]", url)
        if (url != null) {
            urls.add(url)
        }
    }

    fun get(): String? {
        if (urls.size == 0) {
            log.info("Urls are empty! Returning null")
            return null
        }
        val url = urls[0]
        urls.removeAt(0)
        return url
    }
}
