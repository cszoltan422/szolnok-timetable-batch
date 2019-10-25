package org.zenbot.szolnok.timetable.backend.batch.utils.common.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.LinkedList

/**
 * In memory string storage for further processing
 */
@Component
class StringResourcesInMemoryStorage {

    private val log = LoggerFactory.getLogger(StringResourcesInMemoryStorage::class.java)

    private val urls: MutableList<String> = LinkedList()

    /**
     * @param url The url to add to the storage. If null it won't be added
     */
    fun addUrl(url: String?) {
        log.info("Adding url to in memory storage url=[{}]", url)
        if (url != null) {
            urls.add(url)
        }
    }

    /**
     * Returns the first item from the storage
     * @return null if no items present. The first item in the list otherwise and removes from the list.
     */
    fun get(): String? {
        if (urls.size == 0) {
            log.info("Urls are empty! Returning null")
            return null
        }
        val url = urls[0]
        urls.removeAt(0)
        return url
    }

    /**
     * Clears the url list for new processing
     */
    fun clear() {
        log.info("Resetting URL list")
        urls.clear()
    }
}
