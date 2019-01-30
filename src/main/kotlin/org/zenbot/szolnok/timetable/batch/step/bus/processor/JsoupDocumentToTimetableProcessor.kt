package org.zenbot.szolnok.timetable.batch.step.bus.processor

import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.configuration.properties.TimetableProperties
import org.zenbot.szolnok.timetable.configuration.properties.TimetableSelectorProperties
import org.zenbot.szolnok.timetable.domain.Timetable

@Component
@EnableConfigurationProperties(TimetableProperties::class)
class JsoupDocumentToTimetableProcessor(properties: TimetableProperties, private val startBusStopSelectorItemProcessorHelper: StartBusStopSelectorItemProcessorHelper, private val endBusStopSelectorItemProcessorHelper: EndBusStopSelectorItemProcessorHelper, private val actualStopSelectorItemProcessorHelper: ActualStopSelectorItemProcessorHelper, private val timetableRowBuilderItemProcessorHelper: TimetableRowBuilderItemProcessorHelper) : ItemProcessor<Document, Timetable> {

    private val log = LoggerFactory.getLogger(JsoupDocumentToTimetableProcessor::class.java)

    private val selectorProperties: TimetableSelectorProperties

    init {
        this.selectorProperties = properties.selector
    }

    override fun process(htmlDocument: Document): Timetable {
        val timetable = Timetable()
        val busName = htmlDocument.select(selectorProperties.routeNameSelector).text()
        val startBusStop = startBusStopSelectorItemProcessorHelper.getStartBusStop(htmlDocument, selectorProperties)
        val endBusStop = endBusStopSelectorItemProcessorHelper.getEndBusStop(htmlDocument, selectorProperties)
        val actualStop = actualStopSelectorItemProcessorHelper.getActualStop(htmlDocument, selectorProperties)
        val timetableRows = timetableRowBuilderItemProcessorHelper.getTimetableRows(htmlDocument, selectorProperties)

        timetable.busName = busName
        timetable.startBusStopName = startBusStop
        timetable.endBusStopName = endBusStop
        timetable.activeStopName = actualStop
        timetableRows.forEach { hour, values -> timetable.addRow(hour, values) }

        log.info("Process html with busName [#{}] and timetable for [{}] stop", timetable.busName, timetable.activeStopName)
        return timetable
    }

    companion object {

        val WEEKDAY_KEY = "weekday"
        val SATURDAY_KEY = "saturday"
        val SUNDAY_KEY = "sunday"
    }
}
