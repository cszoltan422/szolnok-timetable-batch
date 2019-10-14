package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.jsoup.nodes.Element
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties

/**
 * Maps each row of a timetable into a map by hour
 */
@Component
class HtmlTimetableProcessorHelper(
    private val htmlTimetableRowProcessorHelper: HtmlTimetableRowProcessorHelper
) {

    /**
     * @param table The HTML table to iterate through
     * @param selectorProperties The CSS selectors encapsulated in a property object
     * @return a Map of all the arrivals per hour
     */
    fun processHtmlTable(table: Element, selectorProperties: TimetableSelectorProperties):
            Map<Int, Map<String, String>> {
        val result = HashMap<Int, Map<String, String>>()
        for (row in table.select(selectorProperties.tableRowSelector)) {
            result.putAll(htmlTimetableRowProcessorHelper.processRow(row, selectorProperties))
        }
        return result
    }
}
