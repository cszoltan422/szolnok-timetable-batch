package org.zenbot.szolnok.timetable.batch.bus.batch.step.bus.processor

import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.batch.utils.configuration.properties.TimetableSelectorProperties

@Component
class ActualStopSelectorItemProcessorHelper(private val stringCleaner: StringCleaner) {

    fun getActualStop(htmlDocument: Document, selectorProperties: TimetableSelectorProperties): String {
        val actualStopSelector = selectorProperties.actualStopSelector
        var actualStop = htmlDocument.select(actualStopSelector).text()
        // In the html page they put it insede () signs.
        actualStop = actualStop.subSequence(actualStop.indexOf("(") + 1, actualStop.indexOf(")")) as String
        if (actualStop.contains("(")) {
            actualStop += ")"
        }
        if (actualStop.endsWith(".")) {
            actualStop = actualStop.substring(0, actualStop.length - 1)
        }
        return stringCleaner.clean(actualStop)
    }
}
