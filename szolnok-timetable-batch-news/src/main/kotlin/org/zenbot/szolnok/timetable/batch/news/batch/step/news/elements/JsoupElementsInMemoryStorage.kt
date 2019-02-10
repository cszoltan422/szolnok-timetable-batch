package org.zenbot.szolnok.timetable.batch.news.batch.step.news.elements

import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.LinkedList

@Component
class JsoupElementsInMemoryStorage {

    private val log = LoggerFactory.getLogger(JsoupElementsInMemoryStorage::class.java)

    private val elements: MutableList<Element> = LinkedList()

    fun addElement(element: Element?) {
        log.info("Adding element to in memory storage url=[{}]", element)
        if (element != null) {
            elements.add(element)
        }
    }

    fun get(): Element? {
        if (elements.size == 0) {
            log.info("Elements are empty! Returning null")
            return null
        }
        val element = elements[0]
        elements.removeAt(0)
        return element
    }
}