package org.zenbot.szolnok.timetable.backend.batch.utils.common.service

import org.apache.commons.io.IOUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.URL

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
                i++
                val urlValue = URL(url)
                val openStream = urlValue.openStream()
                val htmlString = IOUtils.toString(openStream, "UTF-8")
                return Jsoup.parse(htmlString)
            } catch (e: IOException) {
                log.warn("{}", e)
                log.debug("Read timed out [{}]", url)
                log.debug("Retry last operation for the [{}] time", i)
            }
        }
        throw IllegalStateException("Cannot fetch document=[$url]")
    }

    companion object {
        val MAX_RETRY_COUNT = 4
        val INITIAL_RETRY_COUNT = 0
    }
}
