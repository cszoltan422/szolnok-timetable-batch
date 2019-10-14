package org.zenbot.szolnok.timetable.backend.batch.utils.common.service

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class JsoupDocumentService {

    private val log = LoggerFactory.getLogger(JsoupDocumentService::class.java)

    fun getDocument(url: String): Document {
        log.info("Getting Jsoup Document with url=[$url]")
        var i = INITIAL_RETRY_COUNT
        while (i <= MAX_RETRY_COUNT) {
            try {
                return Jsoup.connect(url).get()
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
        val INITIAL_RETRY_COUNT = 0
    }
}
