package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper

import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableSelectorProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.util.StringCleaner

@Component
class StartBusStopSelectorItemProcessorHelper(private val stringCleaner: StringCleaner) {

    fun getStartBusStop(htmlDocument: Document, selectorProperties: TimetableSelectorProperties): String {
        return stringCleaner.clean(htmlDocument.select(selectorProperties.fromSelector).text())
    }
}
