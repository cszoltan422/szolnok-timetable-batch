package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties

/**
 * Selects the name of the bus from the HTML document
 */
@Component
class BusNameSelectorItemProcessorHelper {

    /**
     * @param htmlDocument The HTML document to select the bus name from
     * @param selectorProperties The CSS selectors encapsulated in a property object
     * @return The bus name of the timetable HTML page
     */
    fun getBusName(htmlDocument: Document, selector: TimetableSelectorProperties): String {
        return htmlDocument.select(selector.routeNameSelector).text()
    }
}
