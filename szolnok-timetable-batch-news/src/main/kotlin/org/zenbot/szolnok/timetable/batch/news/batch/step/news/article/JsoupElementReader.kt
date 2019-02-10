package org.zenbot.szolnok.timetable.batch.news.batch.step.news.article

import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.batch.news.batch.step.news.elements.JsoupElementsInMemoryStorage

@Component
class JsoupElementReader(private val jsoupElementsInMemoryStorage: JsoupElementsInMemoryStorage) : ItemReader<Element> {

    private val log = LoggerFactory.getLogger(JsoupElementReader::class.java)

    override fun read(): Element? {
        log.info("Reading next item from [JsoupElementReader]")
        return jsoupElementsInMemoryStorage.get()
    }
}