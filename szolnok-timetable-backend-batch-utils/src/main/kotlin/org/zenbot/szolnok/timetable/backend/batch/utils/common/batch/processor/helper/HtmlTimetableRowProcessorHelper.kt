package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.jsoup.nodes.Element
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.JsoupDocumentToTimetableProcessor
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties

/**
 * Maps one row of a timetable into a map by hour
 */
@Component
class HtmlTimetableRowProcessorHelper {

    /**
     * @param row One arrival row
     * @param selectorProperties The CSS selectors encapsulated in a property object
     * @return a Map of all the arrivals per hour for a row
     */
    fun processRow(row: Element, selectorProperties: TimetableSelectorProperties): Map<Int, Map<String, String>> {
        val result = HashMap<Int, Map<String, String>>()
        val tds = row.select(selectorProperties.tableColumnSelector)
        if (tds.size == VALID_ROW_LENGTH) {
            val hour = tds[FIRST_COLUMN].text()
            val weekdayArrivals = tds[SECOND_COLUMN].text().replace(WHITESPACE_REGEX, EMPTY_STRING)
            val saturdayArrivals = tds[THIRD_COLUMN].text().replace(WHITESPACE_REGEX, EMPTY_STRING)
            val sundayArrivals = tds[LAST_COLUMN].text().replace(WHITESPACE_REGEX, EMPTY_STRING)
            result[Integer.parseInt(hour)] = mapOf(JsoupDocumentToTimetableProcessor.WEEKDAY_KEY to weekdayArrivals,
                    JsoupDocumentToTimetableProcessor.SATURDAY_KEY to saturdayArrivals,
                    JsoupDocumentToTimetableProcessor.SUNDAY_KEY to sundayArrivals)
        }
        return result
    }

    companion object {
        val VALID_ROW_LENGTH = 4
        val FIRST_COLUMN = 0
        val SECOND_COLUMN = 1
        val THIRD_COLUMN = 2
        val LAST_COLUMN = 3
        val WHITESPACE_REGEX = " ".toRegex()
        val EMPTY_STRING = ""
    }
}
