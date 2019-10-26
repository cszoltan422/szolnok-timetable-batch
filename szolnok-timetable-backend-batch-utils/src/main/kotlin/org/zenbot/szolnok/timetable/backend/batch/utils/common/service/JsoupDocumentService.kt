package org.zenbot.szolnok.timetable.backend.batch.utils.common.service

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.IOException

/**
 * Reads a certain URL and return the HTML page. Reads the urls in a retry manner, maximum 4 times
 */
@Component
class JsoupDocumentService {

    private val log = LoggerFactory.getLogger(JsoupDocumentService::class.java)

    /**
     * Reads the url from the internet and returns the parsed HTML page.
     * Retries the read 4 times before throwing an exception.
     * @param url The url of the html page
     * @return The html page parsed
     * @throws IllegalStateException if the retries fail
     */
    fun getDocument(url: String): Document {
        log.info("Getting Jsoup Document with url=[$url]")
        var i = INITIAL_RETRY_COUNT
        while (i <= MAX_RETRY_COUNT) {
            try {
                val connection = Jsoup.connect(url)
                connection.timeout(CONNECT_TIMEOUT)
                return connection.get()
            } catch (e: IOException) {
                log.debug("Read timed out [{}]", url)
                log.debug("Retry last operation for the [{}] time", i)
            }

            i++
        }
        throw IllegalStateException("Cannot fetch document=[$url]")
    }

    companion object {
        val MAX_RETRY_COUNT = 4
        val CONNECT_TIMEOUT = 300000
        val INITIAL_RETRY_COUNT = 0
    }
}
