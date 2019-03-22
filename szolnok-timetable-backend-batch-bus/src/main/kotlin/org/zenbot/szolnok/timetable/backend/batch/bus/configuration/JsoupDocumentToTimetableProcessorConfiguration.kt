package org.zenbot.szolnok.timetable.backend.batch.bus.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.JsoupDocumentToTimetableProcessor
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.ActualStopSelectorItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.EndBusStopSelectorItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.StartBusStopSelectorItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.batch.processor.helper.TimetableRowBuilderItemProcessorHelper
import org.zenbot.szolnok.timetable.backend.batch.utils.common.properties.TimetableProperties
import org.zenbot.szolnok.timetable.backend.batch.utils.common.util.StringCleaner

@Configuration
@EnableConfigurationProperties(TimetableProperties::class)
class JsoupDocumentToTimetableProcessorConfiguration(
    private val properties: TimetableProperties,
    private val stringCleaner: StringCleaner
) {

    @Bean
    fun jsoupDocumentToTimetableProcessor(): JsoupDocumentToTimetableProcessor {
        return JsoupDocumentToTimetableProcessor(properties, startBusStopSelectorItemProcessorHelper(), endBusStopSelectorItemProcessorHelper(), actualStopSelectorItemProcessorHelper(), timetableRowBuilderItemProcessorHelper())
    }

    @Bean
    fun startBusStopSelectorItemProcessorHelper(): StartBusStopSelectorItemProcessorHelper {
        return StartBusStopSelectorItemProcessorHelper(stringCleaner)
    }

    @Bean
    fun actualStopSelectorItemProcessorHelper(): ActualStopSelectorItemProcessorHelper {
        return ActualStopSelectorItemProcessorHelper(stringCleaner)
    }

    @Bean
    fun endBusStopSelectorItemProcessorHelper(): EndBusStopSelectorItemProcessorHelper {
        return EndBusStopSelectorItemProcessorHelper(stringCleaner)
    }

    @Bean
    fun timetableRowBuilderItemProcessorHelper(): TimetableRowBuilderItemProcessorHelper {
        return TimetableRowBuilderItemProcessorHelper()
    }
}