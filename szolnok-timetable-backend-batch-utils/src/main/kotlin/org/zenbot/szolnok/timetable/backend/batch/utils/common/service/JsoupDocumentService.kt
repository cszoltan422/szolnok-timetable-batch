package org.zenbot.szolnok.timetable.backend.batch.utils.common.service

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import java.io.IOException

class JsoupDocumentService {

    private val log = LoggerFactory.getLogger(JsoupDocumentService::class.java)

    fun getDocument(url: String): Document {
        log.info("Getting Jsoup Document with url=[$url]")
        var i = 0
        while (i <= 4) {
            try {
                return Jsoup.connect(url).get()
            } catch (e: IOException) {
                log.debug("Read timed out [{}]", url)
                log.debug("Retry last operation for the [{}] time", i + 1)
            }

            i++
        }
        throw IllegalStateException("Cannot fetch document=[$url]")
    }
}