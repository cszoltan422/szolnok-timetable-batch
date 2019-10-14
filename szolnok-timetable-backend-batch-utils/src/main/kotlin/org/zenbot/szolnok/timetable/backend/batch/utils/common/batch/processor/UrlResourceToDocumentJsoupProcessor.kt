package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor

import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.JsoupDocumentService

/**
 * Reads an url into an HTML document
 */
@Component
class UrlResourceToDocumentJsoupProcessor(
    private val jsoupDocumentService: JsoupDocumentService
) : ItemProcessor<String, Document> {

    private val log = LoggerFactory.getLogger(UrlResourceToDocumentJsoupProcessor::class.java)

    /**
     * @param url The url of the Documenbt to fetch
     * @return the document represented by the url
     */
    override fun process(url: String): Document {
        log.info("Process url=[{}]", url)
        return jsoupDocumentService.getDocument(url)
    }
}
