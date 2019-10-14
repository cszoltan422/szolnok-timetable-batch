package org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor

import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.ActualStopSelectorItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.TerminalSelectorItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.StartBusStopSelectorItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.TimetableRowBuilderItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
import org.zenbot.szolnok.timetable.backend.domain.batch.Timetable

@Component
@EnableConfigurationProperties(TimetableProperties::class)
class JsoupDocumentToTimetableProcessor(
        private val properties: TimetableProperties,
        private val startBusStopSelectorItemProcessorHelper: StartBusStopSelectorItemProcessorHelper,
        private val terminalSelectorItemProcessorHelper: TerminalSelectorItemProcessorHelper,
        private val actualStopSelectorItemProcessorHelper: ActualStopSelectorItemProcessorHelper,
        private val timetableRowBuilderItemProcessorHelper: TimetableRowBuilderItemProcessorHelper
) : ItemProcessor<Document, Timetable> {

    private val log = LoggerFactory.getLogger(JsoupDocumentToTimetableProcessor::class.java)

    override fun process(htmlDocument: Document): Timetable {
        val timetable = Timetable()
        val busName = htmlDocument.select(properties.selector.routeNameSelector).text()
        val startBusStop = startBusStopSelectorItemProcessorHelper.getStartBusStop(htmlDocument, properties.selector)
        val endBusStop = terminalSelectorItemProcessorHelper.getTerminal(htmlDocument, properties.selector)
        val actualStop = actualStopSelectorItemProcessorHelper.getActualStop(htmlDocument, properties.selector)
        val timetableRows = timetableRowBuilderItemProcessorHelper.getTimetableRows(htmlDocument, properties.selector)

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
