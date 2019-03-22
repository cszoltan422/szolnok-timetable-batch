package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor

import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.zenbot.szolnok.timetable.backend.batch.utils.common.service.JsoupDocumentService

class UrlResourceToDocumentJsoupProcessor(private val jsoupDocumentService: JsoupDocumentService) : ItemProcessor<String, Document> {

    private val log = LoggerFactory.getLogger(UrlResourceToDocumentJsoupProcessor::class.java)

    override fun process(url: String): Document {
        log.info("Process url=[{}]", url)
        return jsoupDocumentService.getDocument(url)
    }
}
