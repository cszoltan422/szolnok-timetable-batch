package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties

/**
 * Creates a Map of all the arrivals per hour for a given timetable from an HTML page
 */
@Component
class TimetableRowBuilderItemProcessorHelper(
    private val htmlTimetableProcessorHelper: HtmlTimetableProcessorHelper
) {

    /**
     * @param htmlDocument The HTML document to select the timetable from
     * @param selectorProperties The CSS selectors encapsulated in a property object
     * @return a Map of all the arrivals per hour
     */
    fun getTimetableRows(htmlDocument: Document, selectorProperties: TimetableSelectorProperties): Map<Int, Map<String, String>> {
        val result = HashMap<Int, Map<String, String>>()
        for (table in htmlDocument.select(selectorProperties.timetableSelector)) {
            result.putAll(htmlTimetableProcessorHelper.processHtmlTable(table, selectorProperties))
        }
        return result
    }
}
