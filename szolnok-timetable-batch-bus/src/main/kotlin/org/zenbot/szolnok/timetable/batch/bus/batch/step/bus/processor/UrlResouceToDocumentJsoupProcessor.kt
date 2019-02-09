package org.zenbot.szolnok.timetable.batch.bus.batch.step.bus.processor

import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
class UrlResouceToDocumentJsoupProcessor(private val jsoupDocumentService: JsoupDocumentService) : ItemProcessor<String, Document> {

    private val log = LoggerFactory.getLogger(UrlResouceToDocumentJsoupProcessor::class.java)

    override fun process(url: String): Document {
        log.info("Process url=[{}]", url)
        return jsoupDocumentService.getDocument(url)
    }
}
