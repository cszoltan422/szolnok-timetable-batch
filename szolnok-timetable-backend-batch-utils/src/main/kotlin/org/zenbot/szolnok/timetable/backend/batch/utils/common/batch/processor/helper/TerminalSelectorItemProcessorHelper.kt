package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.util.StringCleaner

/**
 * Selects the terminal bus stop of the timetable from an HTML document
 */
@Component
class TerminalSelectorItemProcessorHelper(private val stringCleaner: StringCleaner) {

    /**
     * @param htmlDocument The HTML document to select the ending bus stop from
     * @param selectorProperties The CSS selectors encapsulated in a property object
     * @return the ending bus stop of a timetable from the HTML document
     */
    fun getTerminal(htmlDocument: Document, selectorProperties: TimetableSelectorProperties): String {
        val rows = getAllStops(htmlDocument, selectorProperties)
        val endBusStopRow = getTerminalStopRow(rows)
        val endBusStopName = getTerminalText(endBusStopRow, selectorProperties)
        return stringCleaner.clean(endBusStopName)
    }

    private fun getTerminalText(terminalStopRow: Element, selectorProperties: TimetableSelectorProperties): String {
        val columns = terminalStopRow.select(selectorProperties.tableColumnSelector)
        val lastColumn = columns[LAST_COLUMN_INDEX]
        return lastColumn.text()
    }

    private fun getTerminalStopRow(rows: Elements): Element {
        val indexOfTerminalStop = rows.size - LAST_ROW_INDEX
        return rows[indexOfTerminalStop]
    }

    private fun getAllStops(htmlDocument: Document, selectorProperties: TimetableSelectorProperties): Elements {
        val busStopsTable = htmlDocument.select(selectorProperties.busStopsSelector)
        return busStopsTable.select(selectorProperties.tableRowSelector)
    }

    companion object {
        val LAST_ROW_INDEX = 2
        val LAST_COLUMN_INDEX = 2
    }
}
