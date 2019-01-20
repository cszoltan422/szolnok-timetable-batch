package org.zenbot.szolnok.timetable.configuration;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zenbot.szolnok.timetable.batch.step.bus.processor.JsoupDocumentToTimetableProcessor;
import org.zenbot.szolnok.timetable.batch.step.bus.processor.TimetableToBusItemProcessor;
import org.zenbot.szolnok.timetable.batch.step.bus.processor.UrlResouceToDocumentJsoupProcessor;
import org.zenbot.szolnok.timetable.batch.step.bus.reader.UrlResourceItemReader;
import org.zenbot.szolnok.timetable.batch.step.bus.writer.BusMongoRepositoryItemWriter;
import org.zenbot.szolnok.timetable.domain.Bus;

import java.util.Arrays;

@Configuration
public class SaveBusStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final UrlResourceItemReader urlResourceItemReader;
    private final BusMongoRepositoryItemWriter busMongoRepositoryItemWriter;

    public SaveBusStepConfiguration(StepBuilderFactory stepBuilderFactory, UrlResourceItemReader urlResourceItemReader, BusMongoRepositoryItemWriter busMongoRepositoryItemWriter) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.urlResourceItemReader = urlResourceItemReader;
        this.busMongoRepositoryItemWriter = busMongoRepositoryItemWriter;
    }

    @Bean
    public Step saveBusStep(UrlResouceToDocumentJsoupProcessor jsoupProcessor, JsoupDocumentToTimetableProcessor documentToTimetableProcessor, TimetableToBusItemProcessor timetableToBusItemProcessor) {
        return stepBuilderFactory.get("saveBusStep")
                .<String, Bus> chunk(1)
                .reader(urlResourceItemReader)
                .processor(compositeItemProcessor(jsoupProcessor, documentToTimetableProcessor, timetableToBusItemProcessor))
                .writer(busMongoRepositoryItemWriter)
                .build();
    }

    @Bean
    public CompositeItemProcessor<String, Bus> compositeItemProcessor(UrlResouceToDocumentJsoupProcessor jsoupProcessor, JsoupDocumentToTimetableProcessor documentToTimetableProcessor, TimetableToBusItemProcessor timetableToBusItemProcessor) {
        CompositeItemProcessor<String, Bus> compositeItemProcessor = new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(Arrays.asList(jsoupProcessor, documentToTimetableProcessor, timetableToBusItemProcessor));
        return compositeItemProcessor;
    }
}
