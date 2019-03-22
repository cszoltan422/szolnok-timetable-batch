package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.JsoupDocumentToTimetableProcessor.Companion.SATURDAY_KEY
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.JsoupDocumentToTimetableProcessor.Companion.SUNDAY_KEY
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.JsoupDocumentToTimetableProcessor.Companion.WEEKDAY_KEY
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties

@Component
class TimetableRowBuilderItemProcessorHelper {
    fun getTimetableRows(htmlDocument: Document, selectorProperties: TimetableSelectorProperties): Map<Int, Map<String, String>> {
        val result = HashMap<Int, Map<String, String>>()
        for (table in htmlDocument.select(selectorProperties.timetableSelector)) {
            for (row in table.select(selectorProperties.tableRowSelector)) {
                val tds = row.select(selectorProperties.tableColumnSelector)
                if (tds.size == 4) {
                    val hour = tds[0].text()
                    val weekdayArrivals = tds[1].text().replace(" ".toRegex(), "")
                    val saturdayArrivals = tds[2].text().replace(" ".toRegex(), "")
                    val sundayArrivals = tds[3].text().replace(" ".toRegex(), "")
                    result[Integer.parseInt(hour)] = mapOf(WEEKDAY_KEY to weekdayArrivals,
                            SATURDAY_KEY to saturdayArrivals,
                            SUNDAY_KEY to sundayArrivals)
                }
            }
        }
        return result
    }
}
