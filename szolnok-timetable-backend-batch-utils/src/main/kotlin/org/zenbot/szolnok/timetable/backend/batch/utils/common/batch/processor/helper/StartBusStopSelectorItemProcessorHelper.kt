package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.util.StringCleaner

/**
 * Selects the start bus stop of the timetable from an HTML document
 */
@Component
class StartBusStopSelectorItemProcessorHelper(private val stringCleaner: StringCleaner) {

    /**
     * @param htmlDocument The HTML document to select the start bus stop from
     * @param selectorProperties The CSS selectors encapsulated in a property object
     * @return the start bus stop of a timetable from the HTML document
     */
    fun getStartBusStop(htmlDocument: Document, selectorProperties: TimetableSelectorProperties): String {
        return stringCleaner.clean(htmlDocument.select(selectorProperties.fromSelector).text())
    }
}
