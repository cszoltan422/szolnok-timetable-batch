package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.util.StringCleaner

@Component
class EndBusStopSelectorItemProcessorHelper(private val stringCleaner: StringCleaner) {

    fun getEndBusStop(htmlDocument: Document, selectorProperties: TimetableSelectorProperties): String {
        val stationsTable = htmlDocument.select(selectorProperties.busStopsSelector)
        val rows = stationsTable.select(selectorProperties.tableRowSelector)
        val indexOfEndBusStop = rows.size - 2
        val lastRow = rows[indexOfEndBusStop]
        val lastRowColumns = lastRow.select(selectorProperties.tableColumnSelector)
        val lastColumn = lastRowColumns[2]
        val to = lastColumn.text()
        return stringCleaner.clean(to)
    }
}
