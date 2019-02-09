package org.zenbot.szolnok.timetable.batch.bus.configuration

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.support.CompositeItemProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zenbot.szolnok.timetable.batch.bus.batch.step.bus.processor.JsoupDocumentToTimetableProcessor
import org.zenbot.szolnok.timetable.batch.bus.batch.step.bus.processor.TimetableToBusItemProcessor
import org.zenbot.szolnok.timetable.batch.bus.batch.step.bus.processor.UrlResouceToDocumentJsoupProcessor
import org.zenbot.szolnok.timetable.batch.bus.batch.step.bus.reader.UrlResourceItemReader
import org.zenbot.szolnok.timetable.batch.bus.batch.step.bus.writer.BusMongoRepositoryItemWriter
import org.zenbot.szolnok.timetable.batch.bus.domain.Bus
import java.util.*

@Configuration
open class SaveBusStepConfiguration(private val stepBuilderFactory: StepBuilderFactory, private val urlResourceItemReader: UrlResourceItemReader, private val busMongoRepositoryItemWriter: BusMongoRepositoryItemWriter) {

    @Bean
    open fun saveBusStep(jsoupProcessor: UrlResouceToDocumentJsoupProcessor, documentToTimetableProcessor: JsoupDocumentToTimetableProcessor, timetableToBusItemProcessor: TimetableToBusItemProcessor): Step {
        return stepBuilderFactory.get("saveBusStep")
                .chunk<String, Bus>(1)
                .reader(urlResourceItemReader)
                .processor(compositeItemProcessor(jsoupProcessor, documentToTimetableProcessor, timetableToBusItemProcessor))
                .writer(busMongoRepositoryItemWriter)
                .build()
    }

    @Bean
    open fun compositeItemProcessor(jsoupProcessor: UrlResouceToDocumentJsoupProcessor, documentToTimetableProcessor: JsoupDocumentToTimetableProcessor, timetableToBusItemProcessor: TimetableToBusItemProcessor): CompositeItemProcessor<String, Bus> {
        val compositeItemProcessor = CompositeItemProcessor<String, Bus>()
        compositeItemProcessor.setDelegates(Arrays.asList<ItemProcessor<out Any, out Any>>(jsoupProcessor, documentToTimetableProcessor, timetableToBusItemProcessor))
        return compositeItemProcessor
    }
}
