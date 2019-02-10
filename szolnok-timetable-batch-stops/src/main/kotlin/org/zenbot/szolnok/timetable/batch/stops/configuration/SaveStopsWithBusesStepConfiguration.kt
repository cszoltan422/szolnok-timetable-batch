package org.zenbot.szolnok.timetable.batch.stops.configuration

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.support.CompositeItemProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.batch.stops.batch.step.stops.processor.TimetableToBusStopWithBusesItemProcessor
import org.zenbot.szolnok.timetable.batch.stops.batch.step.stops.writer.StopsWithBusesMongoItemWriter
import org.zenbot.szolnok.timetable.batch.stops.domain.BusStopWithBuses
import org.zenbot.szolnok.timetable.batch.utils.common.batch.processor.JsoupDocumentToTimetableProcessor
import org.zenbot.szolnok.timetable.batch.utils.common.batch.reader.UrlResourceItemReader
import org.zenbot.szolnok.timetable.batch.utils.common.batch.processor.UrlResourceToDocumentJsoupProcessor
import java.util.Arrays

@Configuration
class SaveStopsWithBusesStepConfiguration(
    private val stepBuilderFactory: StepBuilderFactory,
    private val urlResourceItemReader: UrlResourceItemReader,
    private val stopsWithBusesMongoItemWriter: StopsWithBusesMongoItemWriter
) {

    @Bean
    fun saveBusStopsStep(jsoupProcessor: UrlResourceToDocumentJsoupProcessor, jsoupDocumentToTimetableProcessor: JsoupDocumentToTimetableProcessor, timetableToBusStopWithBusesItemProcessor: TimetableToBusStopWithBusesItemProcessor): Step {
        return stepBuilderFactory.get("saveBusStopsStep")
                .chunk<String, BusStopWithBuses>(1)
                .reader(urlResourceItemReader)
                .processor(compositeIemProcessor(jsoupProcessor, jsoupDocumentToTimetableProcessor, timetableToBusStopWithBusesItemProcessor))
                .writer(stopsWithBusesMongoItemWriter)
                .build()
    }

    @Bean
    fun compositeIemProcessor(jsoupProcessor: UrlResourceToDocumentJsoupProcessor, jsoupDocumentToTimetableProcessor: JsoupDocumentToTimetableProcessor, timetableToBusStopWithBusesItemProcessor: TimetableToBusStopWithBusesItemProcessor): CompositeItemProcessor<String, BusStopWithBuses> {
        val compositeItemProcessor = CompositeItemProcessor<String, BusStopWithBuses>()
        compositeItemProcessor.setDelegates(Arrays.asList<ItemProcessor<out Any, out Any>>(jsoupProcessor, jsoupDocumentToTimetableProcessor, timetableToBusStopWithBusesItemProcessor))
        return compositeItemProcessor
    }
}