package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.util.StringCleaner

/**
 * Selects the actual stop of the timetable from an HTML document
 */
@Component
class ActualStopSelectorItemProcessorHelper(private val stringCleaner: StringCleaner) {

    /**
     * @param htmlDocument The HTML document to select the actual stop from
     * @param selectorProperties The CSS selectors encapsulated in a property object
     * @return The actual stop of the timetable HTML page. Note that in the HTML, the actual stop
     *          is surrounded by parentheses, like "(BUS STOP)"
     */
    fun getActualStop(htmlDocument: Document, selectorProperties: TimetableSelectorProperties): String {
        var actualStop = getHtmlText(selectorProperties, htmlDocument)
        actualStop = removeParentheses(actualStop)
        actualStop = addParenthesesIfStopShouldContain(actualStop)
        actualStop = removeDotFromText(actualStop)
        return cleanString(actualStop)
    }

    private fun cleanString(actualStop: String): String {
        return stringCleaner.clean(actualStop)
    }

    private fun removeDotFromText(actualStop: String): String {
        var actualStop1 = actualStop
        if (actualStop1.endsWith(DOT)) {
            actualStop1 = actualStop1.substring(0, actualStop1.length - 1)
        }
        return actualStop1
    }

    private fun addParenthesesIfStopShouldContain(actualStop: String): String {
        var actualStop1 = actualStop
        if (actualStop1.contains(OPEN_PARENTHESES)) {
            actualStop1 += CLOSE_PARENTHESES
        }
        return actualStop1
    }

    private fun removeParentheses(actualStop: String): String {
        var actualStop1 = actualStop
        actualStop1 = actualStop1.subSequence(actualStop1.indexOf(OPEN_PARENTHESES) + 1, actualStop1.indexOf(CLOSE_PARENTHESES)) as String
        return actualStop1
    }

    private fun getHtmlText(selectorProperties: TimetableSelectorProperties, htmlDocument: Document): String {
        val actualStopSelector = selectorProperties.actualStopSelector
        val actualStop = htmlDocument.select(actualStopSelector).text()
        return actualStop
    }

    companion object {
        val OPEN_PARENTHESES = "("
        val CLOSE_PARENTHESES = ")"
        val DOT = "."
    }
}
